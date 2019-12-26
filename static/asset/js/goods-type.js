$(function (){

	var $wrapper = $('#div-table-container');
	var $table = $('#table-goods-type');
	
	var _table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION, {
		"paging" : false,
		language : {"sInfoEmpty":"", "sInfoFiltered" : ""},
		ajax : function(data, callback, settings) {//ajax配置为function,手动调用异步查询
			//手动控制遮罩
			$wrapper.spinModal();
			//封装请求参数
			$.ajax({
		            type: "GET",
		            url: "goods-type/list",
		            cache : false,	//禁用缓存
					contentType : "application/x-www-form-urlencoded;charset=utf-8;",
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
			            	// returnData.recordsTotal = result.data.total;
			            	// returnData.recordsFiltered = result.data.total;//后台不实现过滤功能，每次查询均视作全部结果
			            	returnData.data = result.data;
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
            	className : "ellipsis",	//文字过长时用省略号显示，CSS实现
            	data: "goodsType",
            	render : CONSTANT.DATA_TABLES.RENDER.ELLIPSIS,//会显示省略号的列，需要用title属性实现划过时显示全部文本的效果
            },
			{
				className : "td-operation",
				data: null,
				defaultContent:"",
				orderable : false,
				width : "220px"
			}
        ],
        "createdRow": function ( row, data, index ) {
        	//行渲染回调,在这里可以对该行dom元素进行任何操作
        	//给当前行加样式
        	if (data.role) {
        		$(row).addClass("info");
			}
        	//不使用render，改用jquery文档操作呈现单元格
            var $btnEdit = $('<button type="button" class="btn btn-small btn-primary btn-edit">修改</button>');
            var $btnDel = $('<button type="button" class="btn btn-small btn-danger btn-del">删除</button>');
            $('td', row).eq(1).append($btnEdit).append($btnDel);
            
        },
        "drawCallback": function( settings ) {
        	//渲染完毕后的回调
        	//清空全选状态
			$(":checkbox[name='cb-check-all']",$wrapper).prop('checked', false);
        	//默认选中第一行
        	$("tbody tr",$table).eq(0).click();
        }
	})).api();//此处需调用api()方法,否则返回的是JQuery对象而不是DataTables的API对象

	$("#btn-add").click(function(){
		goodsTypeTable.addItemInit();
	});
	
	$("#btn-del").click(function(){
		var arrItemId = [];
        $("tbody :checkbox:checked",$table).each(function(i) {
        	var item = _table.row($(this).closest('tr')).data();
        	arrItemId.push(item);
        });
		goodsTypeTable.deleteItem(arrItemId);
	});
	

	$("#btn-save-add").click(function(){
		goodsTypeTable.addItemSubmit();
	});
	
	$("#btn-save-edit").click(function(){
		goodsTypeTable.editItemSubmit();
	});

	//行点击事件
	$("tbody",$table).on("click","tr",function(event) {
		$(this).addClass("active").siblings().removeClass("active");
		//获取该行对应的数据
		var item = _table.row($(this).closest('tr')).data();
		goodsTypeTable.currentItem = item;
    });
	
	$table.on("click",".btn-edit",function() {
    	//点击编辑按钮
        var item = _table.row($(this).closest('tr')).data();
		$(this).closest('tr').addClass("active").siblings().removeClass("active");
		goodsTypeTable.currentItem = item;
		goodsTypeTable.editItemInit(item);
	}).on("click",".btn-del",function() {
		//点击删除按钮
		var item = _table.row($(this).closest('tr')).data();
		$(this).closest('tr').addClass("active").siblings().removeClass("active");
		goodsTypeTable.deleteItem(item);
	});
	
	$("#btn-info-content-collapse").click(function(){
		$("i",this).toggleClass("fa-minus fa-plus");
		$("span",this).toggle();
		$("#user-view .info-content").slideToggle("fast");
	});

	$(".btn-cancel").click(function(){
		$("#user-edit").hide();
	});
});

var getGoodsType = function(){
	var goodsType = { "goodsType" : $("#goods-type-edit").val()};
	return goodsType;
}

var goodsTypeTable = {
	currentItem : null,
	addItemInit : function() {
		$("#form-edit")[0].reset();
		$("#user-edit").show().siblings(".info-block").hide();
		$("#goods-add-head").show().siblings(".block-header").hide();

		$("#show-image-edit").html("");
		$("#banner-image-div").hide();
		$("#detail-image-div").hide();
	},
	editItemInit : function(item) {
		if (!item) return;

		$("#form-edit")[0].reset();
		$("#goods-type-edit").val(item.goodsType);
		$("#user-edit").show().siblings(".info-block").hide();
		$("#goods-edit-head").show().siblings(".block-header").hide();
		$("#banner-image-div").show();
		$("#detail-image-div").show();
	},
	addItemSubmit : function() {

		$.ajax({
			type: "POST",
			url: "goods-type/add",
			cache: false,	//禁用缓存
			contentType: "application/json;charset=utf-8;",
			data: JSON.stringify(getGoodsType()),	//传入已封装的参数
			dataType: "json",
			success: function (result) {
				$.dialog.tips('保存当前新增商品');
				$(".btn-cancel").click();
				$('#table-goods-type').DataTable().draw("full-hold");
			},
			error: function (err) {
				$.dialog.tips("新增失败!");
				console.log(err)
			}
		});
	},
	editItemSubmit : function() {
		var goodsType = getGoodsType();
		goodsType.id = goodsTypeTable.currentItem.id;
		$.ajax({
			type: "PUT",
			url: "goods-type/update",
			cache: false,	//禁用缓存
			contentType: "application/json;charset=utf-8;",
			data: JSON.stringify(goodsType),	//传入已封装的参数
			dataType: "json",
			success: function (result) {
				if(result.code == 0){
					$.dialog.tips('保存当前编辑商品');
					$(".btn-cancel").click();
					// $('#table-user').DataTable().clear();
					// $('#table-user').DataTable().ajax.reload(null, false);
					$('#table-goods-type').DataTable().clear().draw("full-hold");
				}else{
					$.dialog.alert("保存编辑失败 error code = "+ result.code);
				}
			},
			error: function (err) {
				$.dialog.tips("保存编辑失败!");
				console.log(err)
			}
		});
	},
	deleteItem : function(selectedItem) {
		if (selectedItem) {
			var message = "确定要删除 '"+selectedItem.goodsType+"' 吗?";

			$.dialog.confirmDanger(message, function(){
				$.ajax({
					type: "DELETE",
					url: "goods-type/delete/"+selectedItem.id,
					cache: false,	//禁用缓存
					contentType: "application/x-www-form-urlencoded;charset=utf-8;",
					dataType: "json",
					success: function (result) {
						if(result.code == 0){
							$.dialog.tips('执行删除成功');
							$('#table-goods-type').DataTable().clear().draw("full-hold");
						}else{
							$.dialog.tips('执行删除失败,code='+result.code);
						}

					},
					error: function (err) {
						$.dialog.tips("删除失败!");
						console.log(err)
					}
				});

			});
		}else{
			$.dialog.alert('请先选中要操作的行');
		}
	}
};