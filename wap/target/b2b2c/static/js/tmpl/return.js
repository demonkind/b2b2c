var order_id,order_goods_id,goods_pay_price,goods_num;
$(function(){
/*	var key = getCookie('key');
	if(!key){
		window.location.href = WapSiteUrl+'/tmpl/members/login.html';
	}*/
//    $.getJSON(ApiUrl + '/index.php?act=member_refund&op=refund_form',{key:key,order_id:getQueryString('order_id'),order_goods_id:getQueryString('order_goods_id')}, function(result) {
//        result.datas.WapSiteUrl = WapSiteUrl;
//        $('#order-info-container').html(template.render('order-info-tmpl',result.datas));
//        
//        order_id = result.datas.order.order_id;
//        order_goods_id = result.datas.goods.order_goods_id;
//        
//        // 退款原因
//	    var _option = '';
//	    for (var k in result.datas.reason_list) {
//	        _option += '<option value="' + result.datas.reason_list[k].reason_id + '">' + result.datas.reason_list[k].reason_info + '</option>'
//	    }
//	    $('#refundReason').append(_option);
//	    
//	    // 可退金额
//	    goods_pay_price = result.datas.goods.goods_pay_price;
//	    $('input[name="refund_amount"]').val(goods_pay_price);
//	    $('#returnAble').html('￥'+goods_pay_price);
//	    
//	    // 可退数量
//	    goods_num = result.datas.goods.goods_num;
//	    $('input[name="goods_num"]').val(goods_num);
//        $('#goodsNum').html('最多' + goods_num + '件');
//	    
//	    // 图片上传
//        $('input[name="refund_pic"]').ajaxUploadImage({
//            url : ApiUrl + "/index.php?act=member_refund&op=upload_pic",
//            data:{key:key},
//            start :  function(element){
//                element.parent().after('<div class="upload-loading"><i></i></div>');
//                element.parent().siblings('.pic-thumb').remove();
//            },
//            success : function(element, result){
//                checkLogin(result.login);
//                if (result.datas.error) {
//                    element.parent().siblings('.upload-loading').remove();
//                    $.sDialog({
//                        skin:"red",
//                        content:'图片尺寸过大！',
//                        okBtn:false,
//                        cancelBtn:false
//                    });
//                    return false;
//                }
//                element.parent().after('<div class="pic-thumb"><img src="'+result.datas.pic+'"/></div>');
//                element.parent().siblings('.upload-loading').remove();
//                element.parents('a').next().val(result.datas.file_name);
//            }
//        });
//        
//        });
//    });

//图片上传
$('input[name="refund_pic"]').ajaxUploadImage({
    url :  WapSiteUrl + "/image/upload",
    //data:{key:key},
    start :  function(element){
        element.parent().after('<div class="upload-loading"><i></i></div>');
        element.parent().siblings('.pic-thumb').remove();
    },
    success : function(element, result){
        checkLogin(result.login);
        if (result.code != 200) {
            element.parent().siblings('.upload-loading').remove();
            $.sDialog({
                skin:"red",
                content: result.message,
                okBtn:false,
                cancelBtn:false
            });
            return false;
        }
        element.parent().after('<div class="pic-thumb"><img src="'+result.data.url+'"/></div>');
        element.parent().siblings('.upload-loading').remove();
        element.parents('a').next().val(result.data.name);
    }
});

$('.btn-l').click(function(){
	var buyerMessage = $("#buyer_message").val();
	var refundAmount = $("#refund_amount").val();
    var ordersId = $(this).attr("ordersId");
    var ordersGoodsId = $(this).attr("ordersGoodsId");
    var reasonId = $("#reason_id").val();
    var goodsNum = $("#goods_num").val();
    var pay = $("#goods_pay_price").text();
    var picJson = "";
    
    $.map($(".refundpic"), function (n) {
    	var picUrl = $(n).val();
    	if (picUrl){
    		picJson += "," + picUrl;
    	}
    });
    if(picJson!=""){
    	picJson = picJson.substring(1);
    }
    var data = {buyerMessage:buyerMessage,refundAmount:refundAmount,picJson:picJson,ordersId:ordersId,ordersGoodsId:ordersGoodsId,reasonId:reasonId,goodsNum:goodsNum};
    
    if (isNaN(refundAmount) || parseFloat(refundAmount) == 0|| parseFloat(refundAmount) > parseFloat(pay)) {
        $.sDialog({
            skin:"red",
            content:'退款金额不能为空，或不能超过可退金额',
            okBtn:false,
            cancelBtn:false
        });
        return false;
    }
    if (buyerMessage == 0) {
        $.sDialog({
            skin:"red",
            content:'请填写退款说明',
            okBtn:false,
            cancelBtn:false
        });
        return false;
    }
    if (isNaN(goodsNum) || parseInt(goodsNum) == 0 || parseInt(goodsNum) > parseInt($("#goods_buy_num").val())) {
        $.sDialog({
            skin:"red",
            content:'退货数据不能为空，或不能超过可退数量',
            okBtn:false,
            cancelBtn:false
        });
        return false;
    }
    // 退货申请提交
    $.ajax({
        type:'post',
        url:WapSiteUrl+'/member/return/save',
        data:data,
        dataType:'json',
        async:false,
        success:function(result){
            checkLogin(result.login);
            if (result.code == 200) {
                $.sDialog({
                    skin:"red",
                    content:result.message,
                    okBtn:false,
                    cancelBtn:false
                });
                
                window.location.href = WapSiteUrl + '/orders/list';
            }else{
            	$.sDialog({
                    skin:"red",
                    content:result.message,
                    okBtn:false,
                    cancelBtn:false
                });
            	return false;
            }
        }
    });
});

});