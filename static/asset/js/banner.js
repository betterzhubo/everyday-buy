$(function (){

	var $wrapper = $('#div-table-container');
	var $table = $('#table-banner');
	
	var _table = $table.dataTable($.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION, {
		"paging" : false,
		language : {"sInfoEmpty":"", "sInfoFiltered" : ""},
		ajax : function(data, callback, settings) {//ajax配置为function,手动调用异步查询
			//手动控制遮罩
			$wrapper.spinModal();
			//封装请求参数
			$.ajax({
		            type: "GET",
		            url: "banner/list",
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
            	data: "bannerName",
            	render : CONSTANT.DATA_TABLES.RENDER.ELLIPSIS,//会显示省略号的列，需要用title属性实现划过时显示全部文本的效果
            },
			{
				data: "sort",
				width : "70px"
			},
			{
				data : "isShow",
				width : "90px",
				render : function(data,type, row, meta) {
					return data == 1 ?"显示":"隐藏";
				}
			},
			{
				data: "imageUrl",
				width : "240px",
				render : function(data){
					return "<a onclick='bannerImgClick(this)'>"+data+"</a>";
				}
			},
			{
				data: "pageUrl",
				width : "240px"
			},
			{
				className : "td-operation",
				data: null,
				defaultContent:"",
				orderable : false,
				width : "120px"
			}
        ],
        "createdRow": function ( row, data, index ){
        	//行渲染回调,在这里可以对该行dom元素进行任何操作
        	//给当前行加样式
        	if (data.role) {
        		$(row).addClass("info");
			}
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
		bannerTable.addItemInit();
	});
	
	$("#btn-del").click(function(){
		var arrItemId = [];
        $("tbody :checkbox:checked",$table).each(function(i) {
        	var item = _table.row($(this).closest('tr')).data();
        	arrItemId.push(item);
        });
		bannerTable.deleteItem(arrItemId);
	});
	

	$("#btn-save-add").click(function(){
		bannerTable.addItemSubmit();
	});
	
	$("#btn-save-edit").click(function(){
		bannerTable.editItemSubmit();
	});

	//行点击事件
	$("tbody",$table).on("click","tr",function(event) {
		$(this).addClass("active").siblings().removeClass("active");
		//获取该行对应的数据
		var item = _table.row($(this).closest('tr')).data();
		bannerTable.currentItem = item;
    });
	
	$table.on("click",".btn-edit",function() {
    	//点击编辑按钮
        var item = _table.row($(this).closest('tr')).data();
		$(this).closest('tr').addClass("active").siblings().removeClass("active");
		bannerTable.currentItem = item;
		bannerTable.editItemInit(item);
	}).on("click",".btn-del",function() {
		//点击删除按钮
		var item = _table.row($(this).closest('tr')).data();
		$(this).closest('tr').addClass("active").siblings().removeClass("active");
		bannerTable.deleteItem(item);
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
				$("#banner-image-edit").html(bigImgShowTag("static/image/"+fileObj.name));
			}else{
				$.dialog.alert("error code=" + data.code);
			}
		},
		error: function (err) {
			$.dialog.tips("上传图片失败!");
		}
	});
});

function bannerImgClick(e){
	var imgUrl = $(e).html();
	bigImgShow(imgUrl);
}


var bigImgShowTag = function(imgUrl){
	return $("<a>").text(imgUrl).click(function () {
		bigImgShow(imgUrl);
	});
}

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

var getBanner = function(){
	var banner = {};
	banner.bannerName = $("#banner-edit").val();
	banner.imageUrl = $("#banner-image-edit a").eq(0).text();
	banner.pageUrl = $("#page-url-edit").val();
	banner.isShow = $('input[name="show-edit"]:checked').val()==1
	return banner;
}

var bannerTable = {
	currentItem : null,
	addItemInit : function() {
		$("#form-edit")[0].reset();
		$("#user-edit").show().siblings(".info-block").hide();
		$("#banner-add-head").show().siblings(".block-header").hide();

		$("#page-url-edit").html("");
		$("#banner-image-edit").html("");

	},
	editItemInit : function(item) {
		if (!item) return;

		$("#form-edit")[0].reset();
		$("#banner-edit").val(item.bannerName);
		$("#page-url-edit").val(item.pageUrl);
		$("#user-edit").show().siblings(".info-block").hide();
		$("#banner-edit-head").show().siblings(".block-header").hide();
		item.isShow ? $("#is-show-edit").attr("checked","checked"):$("#un-show-edit").attr("checked","checked");
		$("#banner-image-edit").html(bigImgShowTag(item.imageUrl));

	},
	addItemSubmit : function() {

		$.ajax({
			type: "POST",
			url: "banner/add",
			cache: false,	//禁用缓存
			contentType: "application/json;charset=utf-8;",
			data: JSON.stringify(getBanner()),	//传入已封装的参数
			dataType: "json",
			success: function (result) {
				if(result.code == 0){
					$.dialog.tips('保存当前新增商品');
					$(".btn-cancel").click();
					$('#table-banner').DataTable().draw("full-hold");
				}else{
					$.dialog.tips("新增失败!");
					console.log(result)
				}

			},
			error: function (err) {
				$.dialog.tips("新增失败!");
				console.log(err)
			}
		});
	},
	editItemSubmit : function() {
		var bannerType = getBanner();
		bannerType.id = bannerTable.currentItem.id;
		$.ajax({
			type: "PUT",
			url: "banner/update",
			cache: false,	//禁用缓存
			contentType: "application/json;charset=utf-8;",
			data: JSON.stringify(bannerType),	//传入已封装的参数
			dataType: "json",
			success: function (result) {
				if(result.code == 0){
					$.dialog.tips('保存当前编辑商品');
					$(".btn-cancel").click();
					// $('#table-user').DataTable().clear();
					// $('#table-user').DataTable().ajax.reload(null, false);
					$('#table-banner').DataTable().clear().draw("full-hold");
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
					url: "banner/delete/"+selectedItem.id,
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