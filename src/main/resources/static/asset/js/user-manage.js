$(function (){
	var $wrapper = $('#div-table-container');
	var $table = $('#table-user');
	
	var _table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION, {
		ajax : function(data, callback, settings) {//ajax配置为function,手动调用异步查询
			//手动控制遮罩
			$wrapper.spinModal();
			//封装请求参数
			var param = userManage.getQueryCondition(data);
			$.ajax({
		            type: "POST",
		            url: "goods/page",
		            cache : false,	//禁用缓存
					contentType : "application/json;charset=utf-8;",
		            data: JSON.stringify(param),	//传入已封装的参数
		            dataType: "json",
		            success: function(result) {
		            	//setTimeout仅为测试遮罩效果
		            	setTimeout(function(){
		            		//异常判断与处理
		            		if (result.errorCode) {
		            			$.dialog.alert("查询失败。错误码："+result.errorCode);
		            			return;
							}
		            		
		            		//封装返回数据，这里仅演示了修改属性名
		            		var returnData = {};
			            	returnData.draw = result.data.current;//这里直接自行返回了draw计数器,应该由后台返回
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
		                $.dialog.alert("查询失败");
		                $wrapper.spinModal(false);
		            }
		        });
		},
        columns: [
            {
            	className : "ellipsis",	//文字过长时用省略号显示，CSS实现
            	data: "goodsName",
            	render : CONSTANT.DATA_TABLES.RENDER.ELLIPSIS,//会显示省略号的列，需要用title属性实现划过时显示全部文本的效果
            },
            {
            	className : "ellipsis",	
            	data: "price",
				width : "120px"
            },
			{
				data : "goodsTypeId",
				width : "100px"
			},
			{
				data : "purpose",
				width : "100px"
			},
			{
				data : "selling",
				width : "100px",
				render : function(data,type, row, meta) {
					return data?"上架":"下架";
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
        	$('td', row).eq(4).addClass(data.selling?"text-success":"text-error");
        	//不使用render，改用jquery文档操作呈现单元格
            var $btnEdit = $('<button type="button" class="btn btn-small btn-primary btn-edit">修改</button>');
            var $btnDel = $('<button type="button" class="btn btn-small btn-danger btn-del">删除</button>');
            $('td', row).eq(5).append($btnEdit).append($btnDel);
            
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
		userManage.addItemInit();
	});
	
	$("#btn-del").click(function(){
		var arrItemId = [];
        $("tbody :checkbox:checked",$table).each(function(i) {
        	var item = _table.row($(this).closest('tr')).data();
        	arrItemId.push(item);
        });
		userManage.deleteItem(arrItemId);
	});
	
	$("#btn-simple-search").click(function(){

		//reload效果与draw(true)或者draw()类似,draw(false)则可在获取新数据的同时停留在当前页码,可自行试验
//		_table.ajax.reload();
//		_table.draw(false);
		_table.draw();
	});
	
	$("#btn-advanced-search").click(function(){
		_table.draw();
	});
	
	$("#btn-save-add").click(function(){
		userManage.addItemSubmit();
	});
	
	$("#btn-save-edit").click(function(){
		userManage.editItemSubmit();
	});
	
	//行点击事件
	$("tbody",$table).on("click","tr",function(event) {
		$(this).addClass("active").siblings().removeClass("active");
		//获取该行对应的数据
		var item = _table.row($(this).closest('tr')).data();
		userManage.currentItem = item;
		userManage.showItemDetail(item);
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
		userManage.currentItem = item;
		userManage.editItemInit(item);
	}).on("click",".btn-del",function() {
		//点击删除按钮
		var item = _table.row($(this).closest('tr')).data();
		$(this).closest('tr').addClass("active").siblings().removeClass("active");
		userManage.deleteItem([item]);
	});
	
	$("#btn-info-content-collapse").click(function(){
		$("i",this).toggleClass("fa-minus fa-plus");
		$("span",this).toggle();
		$("#user-view .info-content").slideToggle("fast");
	});

	$(".btn-cancel").click(function(){
		userManage.showItemDetail(userManage.currentItem);
	});
});

//modeSource : 0:查看1:编辑, 0:展示图片,1:详情图片
var clickImageTag = function(clickSource){

	$("#goods-image-list").show().siblings(".info-block").hide();
	$(".imageDiv").remove();
	if(0 == type){
		$("#goods-image-list").find(".text-detail").hide();
	}else{
		$("#goods-image-list").find(".text-detail").show();
	}
	if(1 != type){
		//TODO:展示图片
	}


	$(".btn-back").click(function(){
		$("#user-edit").show().siblings(".info-block").hide();
		if(imageSource){}

		$("#goods-add-head").show().siblings(".block-header").hide();
	});
}

//建立一個可存取到該file的url
function getObjectURL(file) {
	var url = null ;
	if (window.createObjectURL!=undefined) { // basic
		url = window.createObjectURL(file);
	} else if (window.URL!=undefined) { // mozilla(firefox)
		url = window.URL.createObjectURL(file);
	} else if (window.webkitURL!=undefined) { // webkit or chrome
		url = window.webkitURL.createObjectURL(file);
	}
	return url ;
}

$(".file").change(function() {
	//获取选择图片的对象
	var docObj = $(this)[0];
	var picDiv = $(this).parents(".picDiv");
	//得到所有的图片文件
	var fileList = docObj.files;
	//循环遍历
	for (var i = 0; i < fileList.length; i++) {
		//动态添加html元素
		var picHtml = $("<div class='imageDiv' > <img id='img-" + fileList[i].name + "' /> <div class='cover'><i class='delbtn'>删除</i></div></div>");
		picDiv.append(picHtml);
		//获取图片img的对象
		var imgObjPreview = picHtml.find("img");
		if (fileList && fileList[i]) {
			//图片属性
			imgObjPreview.css({"display":"block", "width":"160px", "height":"130px"});
			imgObjPreview.attr("src", getObjectURL(docObj.files[i]));
			// imgObjPreview.src = window.URL.createObjectURL(docObj.files[i]); //获取上传图片文件的物理路径;
			console.log(imgObjPreview.attr("src"));

		}
	}

	/*删除功能*/
	$(".delbtn").click(function() {
		var _this = $(this);
		_this.parents(".imageDiv").remove();
	});
});


var userManage = {
	currentItem : null,
	getQueryCondition : function(data) {
		var param = {};
		//组装排序参数
		if (data.order&&data.order.length&&data.order[0]) {
			switch (data.order[0].column) {
			case 1:
				param.orderColumn = "goodsName";
				break;
			case 2:
				param.orderColumn = "price";
				break;
			case 3:
				param.orderColumn = "goodsTypeId";
				break;
			case 4:
				param.orderColumn = "purpose";
				break;
			default:
				param.orderColumn = "selling";
				break;
			}
			param.orderDir = data.order[0].dir;
		}
		//组装查询参数
		param.goodsName = $("#goods-name-search").val();
		param.goodsTypeId = $("#goods-type-search").val();
		param.minPrice = $("#min-price-search").val();
		param.maxPrice = $("#max-price-search").val();
		param.purpose = $("#purpose-search").val();
		param.selling = $("#selling-search").val();
		//组装分页参数
		param.startIndex = data.start;
		param.pageSize = data.length;
		
		param.draw = data.draw;
		
		return param;
	},
	showItemDetail : function(item) {
		$("#user-view").show().siblings(".info-block").hide();
		if (!item) {
			$("#user-view .prop-value").text("");
			return;
		}
		$("#goods-name-view").text(item.goodsName);
		$("#price-view").text(item.price);
		$("#goods-type-view").text(item.goodsTypeId);
		$("#purpose-view").text(item.purpose);
		$("#selling-view").text(item.selling?"上架":"下架");

		$("#show-image-view").click(function() {
			clickImageTag();
		});

		$("#detail-image-view").click(function() {
			clickImageTag();
		});
	},
	addItemInit : function() {
		$("#form-edit")[0].reset();

		$("#show-image-edit").click(function() {
			clickImageTag();
		});

		$("#detail-image-edit").click(function() {
			clickImageTag();
		});
		$("#user-edit").show().siblings(".info-block").hide();
		$("#goods-add-head").show().siblings(".block-header").hide();
	},
	editItemInit : function(item) {
		if (!item) return;

		$("#form-edit")[0].reset();
		$("#goods-name-edit").val(item.goodsName);
		$("#price-edit").val(item.price);
		$("#goods-type-edit").val(item.goodsTypeId);
		$("#purpose-edit").val(item.purpose);
		$("#selling-edit").val(item.selling);
		$("input[name='sell-edit'][value=" + item.selling).attr("checked",true);
		$("#show-image-edit").click(function() {
			clickImageTag();
		});
		$("#detail-image-edit").click(function() {
			clickImageTag();
		});
		$("#user-edit").show().siblings(".info-block").hide();
		$("#goods-edit-head").show().siblings(".block-header").hide();
	},
	addItemSubmit : function() {


		$.dialog.tips('保存当前添加用户');
	},
	editItemSubmit : function() {
		$.dialog.tips('保存当前编辑用户');
	},
	deleteItem : function(selectedItems) {
		var message;
		if (selectedItems&&selectedItems.length) {
			if (selectedItems.length == 1) {
				message = "确定要删除 '"+selectedItems[0].goodsName+"' 吗?";
				
			}else{
				message = "确定要删除选中的"+selectedItems.length+"项记录吗?";
			}
			$.dialog.confirmDanger(message, function(){
				$.dialog.tips('执行删除操作');
			});
		}else{
			$.dialog.tips('请先选中要操作的行');
		}
	}
};