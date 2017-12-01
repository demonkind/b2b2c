$(function(){
	//小星星评分
	initRaty();
	
	//提交按钮
	$("#btn_submit").click(function(){
		var flag=true;
		$("tr.bd-line").each(function(){
			var str=$(this).find("input[name='imageList']").val();
			if(str!=""){
				var arr=str.split("_");
				if(arr.length>5){
					flag=false;
					return false;
				}
			}
		});
		if(!flag){
			Nc.alertError("每个商品最多可以上传5张图片");
			return;
		}
		
		$(".raty").each(function(){
			var str=$(this).find("input[nctype='score']").val();
			if(str=="" || str=="0"){
				flag=false;
				return false;
			}
		});
		if(!flag){
			Nc.alertError("请为商品评分");
			return;
		}
		
//		$("tr.bd-line").each(function(){
//			var str=$(this).find("textarea[name='contentList']").val();
//			if(str==""){
//				flag=false;
//				return false;
//			}
//		});
//		if(!flag){
//			Nc.alertError("请输入评价内容");
//			return;
//		}

		$(".raty-x2").each(function(){
			var str=$(this).find("input[nctype='score']").val();
			if(str=="" || str=="0"){
				flag=false;
				return false;
			}
		});
		if(!flag){
			Nc.alertError("请为店铺评分");
			return;
		}
		
		$("#evalform").ajaxSubmit();
	});
	
	//匿名checkbox
	$("#evalform .checkbox").ncCheckBox();

	var uploadPicNum = 0;
	//上传图片
	$(".input-file").fileupload({
        dataType: 'json',
        url: ncGlobal.webRoot + "image/upload.json",
        done: function (e,data) {
            if (data.result.code == 200) {
//                var html = '<span class="col-md-3 col-sm-6 m-b-10"><img src="' + data.result.data.url + '"><a name="insert" href="javascript:;" class="btn btn-success btn-xs m-t-5"><i class="fa fa-arrow-circle-o-up"></i>&nbsp;插入</a><a name="delete" href="javascript:;" class="btn btn-danger btn-xs m-t-5 m-l-5"><i class="fa fa-trash-o"></i>&nbsp;删除</a></span>';
//                $('#addModal').find('div[name="preview"]').append(html);
//                var item = $('#addModal').find('div[name="preview"] > span').last();
//                $(item).find('a[name="insert"]').on('click',function(){
//                    article.ueAdd.execCommand('inserthtml', '<img src="' + data.result.data.url + '">');
//                });
                
            	
            	var $list=$(this).parents("tr.bd-line");
            	var html='<li>';
                html+='<div class="upload-thumb" nctype="image_item">';
                html+='<img src="'+ data.result.data.url +'">';
                html+='<a href="javascript:;" nctype="del" data-file-id="0" class="del" title="移除">X</a>';
                html+='<input type="hidden" value="'+data.result.data.name+'" class="imgName">';
                html+='</div></li>';
                $list.find("div.evaluation-image ul").append(html);
                doImgStr($list);
                
                $list.find("div.evaluation-image ul li a[nctype='del']").on('click',function(){
                    $(this).parents('li').remove();
                    doImgStr($list);
                });    
            } else {
				Nc.alertError(data.result.message);
            }
        },
        add: function (e, data) {
        	var $list=$(this).parents("tr.bd-line");
            if ($list.find("div.evaluation-image ul").find("li").length >= 5) {
					Nc.alertError("最多可以上传5张图片");
                return;
            }
			//console.log("uploadPicNum",uploadPicNum);
			if (uploadPicNum <=0){
				Nc.alertError("最多可以上传5张图片");
				return;
			}
			uploadPicNum -- ;
            data.submit();
        },
		change:function(e,data){
			var $ele =  $(e.target),
				ordersGoodsId = $ele.data("ordersGoodsId"),
				$imageList = $("#imageList_" + ordersGoodsId),
				imageCount = $imageList.length ? $imageList.find("li").length : 0 ;
				uploadPicNum = 5 - imageCount
			;
		}
    });
	
});

function doImgStr(obj){
	var imgArr=new Array();
	obj.find("div.evaluation-image ul li").find("input.imgName").each(function(){
		imgArr.push($(this).val());
	});
	obj.find("input[name='imageList']").val((imgArr+"").replace(/,/g,"_"));
}

function initRaty(){
	$('.raty').raty({
        path: ncGlobal.publicRoot + "toolkit/jquery.raty/img",
        hints:['很不满意', '不满意', '一般', '满意', '很满意'],
        click: function(score) {
            $(this).find('[nctype="score"]').val(score);
        }
    });
	
	$('.raty-x2').raty({
        path: ncGlobal.publicRoot + "toolkit/jquery.raty/img",
        starOff: 'star-off-x2.png',
        starOn: 'star-on-x2.png',
        width: 150,
        hints:['很不满意', '不满意', '一般', '满意', '很满意'],
        click: function(score) {
            $(this).find('[nctype="score"]').val(score);
        }
    });
}
