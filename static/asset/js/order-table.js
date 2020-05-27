$(function (){

	var $wrapper = $('#div-table-container');
	var $table = $('#table-order');
	var statusMapper = {"1":"待支付", "2":"待配送", "3":"配送中", "4":"已完成", "5":"已取消", "6":"已退款"}
	var _table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION, {
		ajax : function(data, callback, settings) {//ajax配置为function,手动调用异步查询
			//手动控制遮罩
			$wrapper.spinModal();
			//封装请求参数
			var param = orderTable.getQueryCondition(data);
			$.ajax({
		            type: "POST",
		            url: "order/page",
		            cache : false,	//禁用缓存
					contentType : "application/json;charset=utf-8;",
		            data: JSON.stringify(param),	//传入已封装的参数
		            dataType: "json",
		            success: function(result) {
		            	//setTimeout仅为测试遮罩效果
		            	setTimeout(function(){
		            		//异常判断与处理
		            		if (result.code != 0) {
		            			$.dialog.alert("查询失败。错误码："+result.code);
		            			return;
							}
		            		
		            		//封装返回数据，这里仅演示了修改属性名
		            		var returnData = {};
			            	returnData.recordsTotal = result.data.total;
			            	returnData.recordsFiltered = result.data.total;//后台不实现过滤功能，每次查询均视作全部结果
			            	returnData.data = result.data.records;
			            	//关闭遮罩
			            	$wrapper.spinModal(false);
			            	//调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
			            	//此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
			            	callback(returnData);
		            	},200);
		            },
		            error: function(XMLHttpRequest, textStatus, errorThrown) {
		                $.dialog.tips("查询失败");
		                $wrapper.spinModal(false);
		            }
		        });
		},
        columns: [
            {
            	data: "id",
				width : "150px"
            },
			{
            	data: "userId",
				width : "100px",
				render : function(data,type, row, meta) {
					var contentText = data;
					for(var i=0;i<userList.length;i++){
						if(data != userList[i].id) continue;
						contentText = userList[i].name;
					}
					return contentText;
				}
            },
			{
				data : "consigneeName",
				width : "100px",
			},
			{
				data : "consigneeMobile",
				width : "120px",
			},
			{
				data: "consigneeAddress",
				render : CONSTANT.DATA_TABLES.RENDER.ELLIPSIS,//会显示省略号的列，需要用title属性实现划过时显示全部文本的效果
			},
			{
				data : "orderStatus",
				width : "100px",
				render : function(data,type, row, meta) {
					var content = statusMapper[data];
					return content ? content : data;
				}
			},
			{
				className : "td-operation",
				data: null,
				defaultContent:"",
				orderable : false,
				width : "120px"
			}
        ],
        "createdRow": function ( row, data, index ) {
        	//行渲染回调,在这里可以对该行dom元素进行任何操作
        	//给当前行加样式
        	if (data.role) {
        		$(row).addClass("info");
			}
        	//给当前行某列加样式
        	//不使用render，改用jquery文档操作呈现单元格
            var $btnEdit = $('<button type="button" class="btn btn-small btn-primary btn-edit">修改</button>');
            $('td', row).eq(6).append($btnEdit);
        },
        "drawCallback": function( settings ) {
        	//渲染完毕后的回调
        	//清空全选状态
			$(":checkbox[name='cb-check-all']",$wrapper).prop('checked', false);
        	//默认选中第一行
        	$("tbody tr",$table).eq(0).click();
        }
	})).api();//此处需调用api()方法,否则返回的是JQuery对象而不是DataTables的API对象

	//reload效果与draw(true)或者draw()类似,draw(false)则可在获取新数据的同时停留在当前页码,可自行试验
	$("#btn-advanced-search").click(function(){
		_table.draw();
	});

	$("#btn-save-edit").click(function(){
		orderTable.editItemSubmit();
	});

	//行点击事件
	$("tbody",$table).on("click","tr",function(event) {
		$(this).addClass("active").siblings().removeClass("active");
		//获取该行对应的数据
		var item = _table.row($(this).closest('tr')).data();
		orderTable.currentItem = item;
		if(event.target.nodeName !== "BUTTON" && item){
			orderTable.showItemDetail(item.id);
		}
    });
	
	$table.on("change",":checkbox",function() {
		if ($(this).is("[name='cb-check-all']")) {
			//全选
			$(":checkbox",$table).prop("checked",$(this).prop("checked"));
		}else{
			//一般复选
			var checkbox = $("tbody :checkbox",$table);
			$(":checkbox[name='cb-check-all']",$table).prop('checked', checkbox.length == checkbox.filter(':checked').length);
		}
    }).on("click",".td-checkbox",function(event) {
    	//点击单元格即点击复选框
    	!$(event.target).is(":checkbox") && $(":checkbox",this).trigger("click");
    }).on("click",".btn-edit",function() {
    	//点击编辑按钮
        var item = _table.row($(this).closest('tr')).data();
		$(this).closest('tr').addClass("active").siblings().removeClass("active");
		orderTable.currentItem = item;
		orderTable.editItemInit(item);
	});
	
	$("#btn-info-content-collapse").click(function(){
		$("i",this).toggleClass("fa-minus fa-plus");
		$("span",this).toggle();
		$("#user-view .info-content").slideToggle("fast");
	});

	$(".btn-cancel").click(function(){
		orderTable.showItemDetail(orderTable.currentItem.id);
	});
});

var getOrderInfo = function(){
	var orderInfo = {};
	orderInfo.id = orderTable.currentItem.id;
	orderInfo.consigneeName = $("#consignee-name-edit").val();
	orderInfo.consigneeMobile = $("#consignee-mobile-edit").val();
	orderInfo.consigneeAddress = $("#consignee-address-edit").val();
	orderInfo.orderStatus = $("#order-status-edit").val();
	return orderInfo;
}

var orderTable = {
	currentItem : null,
	getQueryCondition : function(data) {
		var param = {};
		//组装排序参数
		if (data.order&&data.order.length&&data.order[0]) {
			switch (data.order[0].column) {
			case 0:
				param.orderColumn = "id";
				break;
			case 1:
				param.orderColumn = "userId";
				break;
			case 2:
				param.orderColumn = "consigneeName";
				break;
			case 3:
				param.orderColumn = "consigneeMobile";
				break;
			case 4:
				param.orderColumn = "consigneeAddress";
				break;
			case 5:
				param.orderColumn = "orderStatus";
				break;
			default:
				param.orderColumn = "createTime";
			}
			param.orderDir = data.order[0].dir;
		}
		//组装查询参数
		param.id = $("#id-search").val();
		param.userName = $("#user-name-search").val();
		param.orderStatus = $("#order-status-search").val();
		//组装分页参数
		param.current = data.start / data.length;
		param.pageSize = data.length;
		return param;
	},
	showItemDetail : function(id) {
		$.ajax({
			type: "GET",
			url: "order-goods/get/"+id,
			cache: false,	//禁用缓存
			contentType: "application/json;charset=utf-8;",
			dataType: "json",
			success: function (result) {
				if(result.code != 0) {
					console.log("接口返回："+result.code);
					return;
				}
				$("#order-goods-view").show().siblings(".info-block").hide();
				if (!id) {
					$("#order-goods-view .prop-value").text("");
					return;
				}

				var goodsItmeDom = $("#order-goods-item");
				var siblingsDom = goodsItmeDom.siblings();
				for(var i=0;i<siblingsDom.length;i++){
					siblingsDom[i].remove();
				}

				var list = result.data;
				for(var i=0;i<list.length;i++) {
					var goodsItem = goodsItmeDom.clone().attr("id", "order-goods-item"+i).show();
					goodsItem.find("[name=goods-name-view]").text(list[i].goodsName);
					goodsItem.find("[name=price-view]").text(list[i].goodsPrice);
					goodsItem.find("[name=goods-count-view]").text(list[i].goodsCount);
					goodsItmeDom.after(goodsItem);
				}
			},
			error: function (err) {
				console.log(err)
			}
		});
	},
	editItemInit : function(item) {
		if (!item) return;
		$("#order-edit").show().siblings(".info-block").hide();

		$("#form-edit")[0].reset();
		$("#consignee-name-edit").val(item.consigneeName);
		$("#consignee-mobile-edit").val(item.consigneeMobile);
		$("#consignee-address-edit").val(item.consigneeAddress);
		$("#order-status-edit").val(item.orderStatus);
	},
	editItemSubmit : function() {
		var orderInfo = getOrderInfo();
		$.ajax({
			type: "PUT",
			url: "order/update",
			cache: false,	//禁用缓存
			contentType: "application/json;charset=utf-8;",
			data: JSON.stringify(orderInfo),	//传入已封装的参数
			dataType: "json",
			success: function (result) {
				if(result.code == 0){
					$.dialog.tips('保存当前编辑商品');
					$(".btn-cancel").click();
					$('#table-order').DataTable().clear().draw("full-hold");
				}else{
					$.dialog.alert("保存编辑失败 error code = "+ result.code);
				}
			},
			error: function (err) {
				$.dialog.tips("保存编辑失败!");
				console.log(err)
			}
		});
	}
};