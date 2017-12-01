var order_id;
$(function(){
    $.getJSON(ApiUrl + '/index.php?act=member_refund&op=refund_all_form',{key:key,order_id:getQueryString('order_id')}, function(result) {
    	result.datas.WapSiteUrl = WapSiteUrl;
    	$('#order-info-container').html(template.render('order-info-tmpl',result.datas));
    	order_id = result.datas.order.order_id;
    	$('#allow_refund_amount').val('￥'+result.datas.order.allow_refund_amount);

        // 图片上传
        $('input[name="refund_pic"]').ajaxUploadImage({
            url : ApiUrl + "/index.php?act=member_refund&op=upload_pic",
            data:{key:key},
            start :  function(element){
                element.parent().after('<div class="upload-loading"><i></i></div>');
                element.parent().siblings('.pic-thumb').remove();
            },
            success : function(element, result){
                checkLogin(result.login);
                if (result.datas.error) {
                    element.parent().siblings('.upload-loading').remove();
                    $.sDialog({
                        skin:"red",
                        content:'图片尺寸过大！',
                        okBtn:false,
                        cancelBtn:false
                    });
                    return false;
                }
                element.parent().after('<div class="pic-thumb"><img src="'+result.datas.pic+'"/></div>');
                element.parent().siblings('.upload-loading').remove();
                element.parents('a').next().val(result.datas.file_name);
            }
        });

        
    });
});

$('.btn-l').click(function(){
	var ordersId = $('#ordersId').text();
	var buyerMessage = $('.inp').val();
	var picJson = $('#picJson').val();
	
	if (buyerMessage == null || buyerMessage == "") {
		$.sDialog({
			skin:"red",
			content:'请填写退款说明',
			okBtn:false,
			cancelBtn:false
		});
		return false;
	}else{
		$.ajax({//整单保存
			type:'post',
			url:WapSiteUrl+'/member/refund/saveall',
			data:{buyerMessage:buyerMessage,picJson:picJson,ordersId:ordersId},
			dataType:'json',
			async:false,
			success:function(result){
				checkLogin(result.login);
				if (result.code != 200) {
					$.sDialog({
						skin:"red",
						content:result.message,
						okBtn:false,
						cancelBtn:false
					});
					return false;
				}
				window.location.href = WapSiteUrl + '/orders/list';
			}
		});
	}
});



