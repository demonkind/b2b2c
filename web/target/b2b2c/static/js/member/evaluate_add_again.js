$(function(){
	
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
		$("#evalform").ajaxSubmit();
	});


	var uploadPicNum = 0;
	//上传图片
	$(".input-file").fileupload({
        dataType: 'json',
        url: ncGlobal.webRoot + "image/upload.json",
        done: function (e,data) {
            if (data.result.code == 200) {
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
			if (uploadPicNum <=0){
				Nc.alertError("最多可以上传5张图片");
				return;
			}
			uploadPicNum -- ;
            data.submit();
        },
		change: function (e, data) {
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
