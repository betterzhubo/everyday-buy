$(function (){

	var $wrapper = $('#div-table-container');
	var $table = $('#table-goods');
	
	var _table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION, {
		ajax : function(data, callback, settings) {//ajax配置为function,手动调用异步查询
			//手动控制遮罩
			$wrapper.spinModal();
			//封装请求参数
			var param = goodsTable.getQueryCondition(data);
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
                width : "40px"
            },
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
				width : "100px",
				render : function(data,type, row, meta) {
					var contentText = data;
					for(var i=0;i<goodsTypeList.length;i++){
						if(data != goodsTypeList[i].id) continue;
						contentText = goodsTypeList[i].goodsType;
					}
					return contentText;
				}
			},
			{
				data : "purpose",
				width : "100px",
				render : function(data,type, row, meta) {
					return data == 1 ? "自用" : "餐饮"
				}
			},
			{
				data : "selling",
				width : "100px",
				render : function(data,type, row, meta) {
					return data?"上架":"下架";
				}
			},
			{
				data : "isHot",
				width : "100px",
				render : function(data,type, row, meta) {
					return data?"热卖中":"非热卖";
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
        	$('td', row).eq(7).addClass(data.selling?"text-success":"text-error");
        	//不使用render，改用jquery文档操作呈现单元格
            var $btnEdit = $('<button type="button" class="btn btn-small btn-primary btn-edit">修改</button>');
            var $btnDel = $('<button type="button" class="btn btn-small btn-danger btn-del">删除</button>');
            $('td', row).eq(7).append($btnEdit).append($btnDel);
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
		goodsTable.addItemInit();
	});
	
	$("#btn-del").click(function(){
		var arrItemId = [];
        $("tbody :checkbox:checked",$table).each(function(i) {
        	var item = _table.row($(this).closest('tr')).data();
        	arrItemId.push(item);
        });
		goodsTable.deleteItem(arrItemId);
	});
	
	//reload效果与draw(true)或者draw()类似,draw(false)则可在获取新数据的同时停留在当前页码,可自行试验
	$("#btn-advanced-search").click(function(){
		_table.draw();
	});
	
	$("#btn-save-add").click(function(){
		goodsTable.addItemSubmit();
	});
	
	$("#btn-save-edit").click(function(){
		goodsTable.editItemSubmit();
	});

	$("#btn-save-img").click(function(){
		var imgArr = $("#goods-image-list img");

		var param = {};
		param.urls = [];
		for(var i=0;i<imgArr.length;i++){
			param.urls[i] = imgArr.eq(i).attr("src");
		}

		param.goodsId = goodsTable.currentItem.id;
		param.imagesType = $("#goods-image-list").attr("data-img-type");
		$.ajax({
			type: "POST",
			url: "goods-images/save",
			traditional:true,
			cache: false,	//禁用缓存goodsTypeList
			contentType: "application/json;charset=utf-8;",
			data: JSON.stringify(param),	//传入已封装的参数
			dataType: "json",
			success: function (result) {
				if(result.code == 0){
					$.dialog.tips("保存成功");
					$(".btn-back").click();
				}else{
					$.dialog.alert("保存失败, error code => "+ result.code);
				}
			},
			error: function(err) {
				$.dialog.tips("保存失败");
				console.log(err);
			}
		})
	});
	
	//行点击事件
	$("tbody",$table).on("click","tr",function(event) {
		$(this).addClass("active").siblings().removeClass("active");
		//获取该行对应的数据
		var item = _table.row($(this).closest('tr')).data();
		goodsTable.currentItem = item;
		if(event.target.nodeName !== "BUTTON"){
			goodsTable.showItemDetail(item);
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
		goodsTable.currentItem = item;
		goodsTable.editItemInit(item);
	}).on("click",".btn-del",function() {
		//点击删除按钮
		var item = _table.row($(this).closest('tr')).data();
		$(this).closest('tr').addClass("active").siblings().removeClass("active");
		goodsTable.deleteItem([item]);
	});
	
	$("#btn-info-content-collapse").click(function(){
		$("i",this).toggleClass("fa-minus fa-plus");
		$("span",this).toggle();
		$("#user-view .info-content").slideToggle("fast");
	});

	$(".btn-cancel").click(function(){
		goodsTable.showItemDetail(goodsTable.currentItem);
	});
});

function bigImgShow(imgUrl){
    $("#big-img").attr("src", imgUrl);//设置#big-img元素的src属性

    /*获取当前点击图片的真实大小，并显示弹出层及大图*/
    $("<img/>").attr("src", imgUrl).load(function(){
        var windowW = $(window).width();//获取当前窗口宽度
        var windowH = $(window).height();//获取当前窗口高度
        var realWidth = this.width;//获取图片真实宽度
        var realHeight = this.height;//获取图片真实高度
        var imgWidth, imgHeight;
        var scale = 0.8;//缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放

        if(realHeight>windowH*scale) {//判断图片高度
            imgHeight = windowH*scale;//如大于窗口高度，图片高度进行缩放
            imgWidth = imgHeight/realHeight*realWidth;//等比例缩放宽度
            if(imgWidth>windowW*scale) {//如宽度扔大于窗口宽度
                imgWidth = windowW*scale;//再对宽度进行缩放
            }
        } else if(realWidth>windowW*scale) {//如图片高度合适，判断图片宽度
            imgWidth = windowW*scale;//如大于窗口宽度，图片宽度进行缩放
            imgHeight = imgWidth/realWidth*realHeight;//等比例缩放高度
        } else {//如果图片真实高度和宽度都符合要求，高宽不变
            imgWidth = realWidth;
            imgHeight = realHeight;
        }
        $("#big-img").css("width",imgWidth);//以最终的宽度对图片缩放

        var w = (windowW-imgWidth)/2;//计算图片与窗口左边距
        var h = (windowH-imgHeight)/2;//计算图片与窗口上边距
        $("#inner-div").css({"top":h, "left":w});//设置#innerdiv的top和left属性
        $("#outer-div").fadeIn("fast");//淡入显示#outerdiv及.pimg
    });

    $("#outer-div").click(function(){//再次点击淡出消失弹出层
        $(this).fadeOut("fast");
    });
}
var bigImgShowTag = function(imgUrl){
    return $("<a>").text(imgUrl).click(function () {
        bigImgShow(imgUrl);
    });
}

//operateType 0:查看,1:编辑  imageType 1:轮播图片,2:详情图片
var clickImageTag = function(operateType, imageType){
    if(!goodsTable.currentItem){
        return;
    }

	//设置返回按钮点击事件
	$(".btn-back").click(function(){
		$("#user-edit").show().siblings(".info-block").hide();
		if(operateType == 1){
			$("#goods-edit-head").show().siblings(".block-header").hide();
		}else{
			$("#user-view").show().siblings(".info-block").hide();
		}
	});

	$("#goods-image-list").attr("data-img-type", imageType);
	$("#goods-image-list").show().siblings(".info-block").hide();
	$(".imageDiv").remove();
	//显示与隐藏添加图片按钮
	if(0 == operateType){
		$("#goods-image-list").find(".addImages").hide();
		$("#btn-save-img").hide();
	}else{
		$("#goods-image-list").find(".addImages").show();
		$("#btn-save-img").show();
	}

	//展示对应图片
	$.ajax({
		url: '/goods-images/list/'+goodsTable.currentItem.id+"/"+imageType,
		type: 'GET',
		cache : false,	//禁用缓存
		contentType : "application/x-www-form-urlencoded;charset=utf-8;",
		dataType: "json",
		success: function (result) {
			if(result.code != 0){
				$.dialog.tips("获取图片失败!");
				return;
			}
			showImageList(result.data, operateType);
		},error: function (err) {
			$.dialog.tips("获取图片失败!");
		}
	});
}
var showImageList = function(fileList, operateType){
	var picDiv = $("#fileInput").parents(".picDiv");
	//循环遍历
	for (var i = 0; i < fileList.length; i++) {
		//动态添加html元素
		var imgId = "img-" + fileList[i].imagesUrl
		var delTag = "<div class='cover'><i class='delbtn'>删除</i></div>";
		var picHtml = $("<div class='imageDiv' > <img id='" + imgId + "' /> </div>");
		if(operateType == 1){
			picHtml.append(delTag);
		}
		picDiv.append(picHtml);
		//获取图片img的对象
		var imgObjPreview = picHtml.find("img");
		if (fileList && fileList[i]) {
			//图片属性
			imgObjPreview.css({"display":"block", "width":"160px", "height":"130px"});
			imgObjPreview.attr("src", fileList[i].imagesUrl);
		}
	}
	if(operateType == 0) return;

	$(".delbtn").click(function () {
		var _this = $(this);
		_this.parents(".imageDiv").remove();
	});
}

$("#upload-file").change(function () {
    var fileObj = $(this)[0].files[0];
    if(!fileObj) return;
    var formData = new FormData();
    formData.append("uploadFile", fileObj);
    $.ajax({
        url: '/goods-images/upload',
        type: 'POST',
        cache: false,
        processData: false,
        contentType: false,
        data: formData,
        success: function (data) {
            if(0 == data.code){
				$.dialog.tips("上传图片成功!");
                $("#show-image-edit").html(bigImgShowTag("static/image/"+fileObj.name));
            }else{
				$.dialog.alert("error code=" + data.code);
            }
        },
        error: function (err) {
			$.dialog.tips("上传图片失败!");
        }
    });
});

$(".file").change(function() {
	//得到所有的图片文件
	var fileObj = $(this)[0].files[0];
	if (!fileObj) return;
	//获取选择图片的对象
	var picDiv = $(this).parents(".picDiv");
	var formData = new FormData();
	formData.append("uploadFile", fileObj);
	$.ajax({
		url: '/goods-images/upload',
		type: 'POST',
		cache: false,
		processData: false,
		contentType: false,
		data: formData,
		success: function (data) {
			if (0 == data.code) {
				//动态添加html元素
				var picHtml = $("<div class='imageDiv' > <img id='img-" + fileObj.name + "' /> <div class='cover'><i class='delbtn'>删除</i></div></div>");
				picDiv.append(picHtml);
				//获取图片img的对象
				var imgObjPreview = picHtml.find("img");
				//图片属性
				imgObjPreview.css({"display": "block", "width": "160px", "height": "130px"});
				imgObjPreview.attr("src", "static/image/" + fileObj.name);
				/*删除功能*/
				$(".delbtn").click(function () {
					var _this = $(this);
					_this.parents(".imageDiv").remove();
				});
			} else {
				$.dialog.alert("request error code=" + data.code);
			}
		},
		error: function (err) {
			$.dialog.tips("上传图片失败!");
			console.log(err)
		}
	});
});
$("#banner-image-view").click(function() {
	clickImageTag(0, 1);
});

$("#detail-image-view").click(function() {
	clickImageTag(0, 2);
});

$("#banner-image-edit").click(function() {
	clickImageTag(1, 1);
});
$("#detail-image-edit").click(function() {
	clickImageTag(1, 2);
});

var getGoodsInfo = function(){
	var goodsInfo = {};
	goodsInfo.goodsName = $("#goods-name-edit").val();
	goodsInfo.purpose = $("#purpose-edit").val();
	goodsInfo.price = $("#price-edit").val();
	goodsInfo.goodsTypeId = $("#goods-type-edit").val();
	goodsInfo.showImage = $("#show-image-edit a").eq(0).text();
	goodsInfo.selling = $('input[name="sell-edit"]:checked').val()==1
	goodsInfo.isHot = $('input[name="hot-edit"]:checked').val()==1
	return goodsInfo;
}

var goodsTable = {
	currentItem : null,
	getQueryCondition : function(data) {
		var param = {};
		//组装排序参数
		if (data.order&&data.order.length&&data.order[0]) {
			switch (data.order[0].column) {
			case 0:
				param.orderColumn = "goodsName";
				break;
			case 1:
				param.orderColumn = "price";
				break;
			case 2:
				param.orderColumn = "goodsTypeId";
				break;
			case 3:
				param.orderColumn = "purpose";
				break;
			case 4:
				param.orderColumn = "selling";
				break;
			case 5:
				param.orderColumn = "isHot";
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
		var sellingSearch = $("#selling-search").val();
		param.selling = sellingSearch ? sellingSearch == 1 : sellingSearch;
		var hotSearch = $("#is-hot-search").val();
		param.isHot = hotSearch ? hotSearch == 1 : hotSearch;
		//组装分页参数
		param.current = data.start / data.length;
		param.pageSize = data.length;

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
		var contentText = item.goodsTypeId;
		for(var i=0;i<goodsTypeList.length;i++){
			if(item.goodsTypeId != goodsTypeList[i].id) continue;
			contentText = goodsTypeList[i].goodsType;
		}
		$("#goods-type-view").text(contentText);
		$("#purpose-view").text(item.purpose == 1 ? "自用" : "餐饮");
		$("#selling-view").text(item.selling?"上架":"下架");
		$("#is-hot-view").text(item.isHot?"热卖中":"非热卖");
        if(item.showImage){
            $("#show-image-view").html(bigImgShowTag(item.showImage));
        }else{
			$("#show-image-view").html("");
		}
	},
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
		$("#goods-name-edit").val(item.goodsName);
		$("#price-edit").val(item.price);
		$("#goods-type-edit").val(item.goodsTypeId);
		$("#purpose-edit").val(item.purpose);
		item.selling ? $("#selling-edit").attr("checked","checked"):$("#un-selling-edit").attr("checked","checked");
		item.isHot ? $("#is-hot-edit").attr("checked","checked"):$("#un-hot-edit").attr("checked","checked");
        $("#show-image-edit").html(bigImgShowTag(item.showImage));
		$("#user-edit").show().siblings(".info-block").hide();
		$("#goods-edit-head").show().siblings(".block-header").hide();
		$("#banner-image-div").show();
		$("#detail-image-div").show();
	},
	addItemSubmit : function() {

		$.ajax({
			type: "POST",
			url: "goods/add",
			cache: false,	//禁用缓存
			contentType: "application/json;charset=utf-8;",
			data: JSON.stringify(getGoodsInfo()),	//传入已封装的参数
			dataType: "json",
			success: function (result) {
				$.dialog.tips('保存当前新增商品');
				$(".btn-cancel").click();
				$('#table-goods').DataTable().draw("full-hold");
			},
			error: function (err) {
				$.dialog.tips("新增失败!");
				console.log(err)
			}
		});
	},
	editItemSubmit : function() {
		var goodsInfo = getGoodsInfo();
		goodsInfo.id = goodsTable.currentItem.id;
		$.ajax({
			type: "PUT",
			url: "goods/update",
			cache: false,	//禁用缓存
			contentType: "application/json;charset=utf-8;",
			data: JSON.stringify(goodsInfo),	//传入已封装的参数
			dataType: "json",
			success: function (result) {
				if(result.code == 0){
					$.dialog.tips('保存当前编辑商品');
					$(".btn-cancel").click();
					// $('#table-user').DataTable().clear();
					// $('#table-user').DataTable().ajax.reload(null, false);
					$('#table-goods').DataTable().draw("full-hold");
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
	deleteItem : function(selectedItems) {
		var message;
		if (selectedItems&&selectedItems.length) {
			if (selectedItems.length == 1) {
				message = "确定要删除 '"+selectedItems[0].goodsName+"' 吗?";
				
			}else{
				message = "确定要删除选中的"+selectedItems.length+"项记录吗?";
			}
			$.dialog.confirmDanger(message, function(){
				$.ajax({
					type: "DELETE",
					url: "goods/delete/"+selectedItems[0].id,
					cache: false,	//禁用缓存
					contentType: "application/x-www-form-urlencoded;charset=utf-8;",
					dataType: "json",
					success: function (result) {
						if(result.code == 0){
							$.dialog.tips('执行删除成功');
							$('#table-user').DataTable().clear().draw("full-hold");
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