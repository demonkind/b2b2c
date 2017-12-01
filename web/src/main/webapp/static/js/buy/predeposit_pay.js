/**
 * Created by zxy on 2016-01-26
 */
//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {
        $('li[ncType="paymentType"]').on('click',function(){
            $('li[ncType="paymentType"]').removeClass('using');
            $(this).addClass('using');
            $("#paymentCode").val($(this).attr("dataPaymentCode"));
        });

        $("#payForm").on("nc.ajaxSubmit.success",function(e,result){
            window.location.href = result.url;
        });

        $('#pay').on('click',function(){
            if (!$.trim($("#paymentCode").val())) {
                Nc.alertError("请选择一种在线支付方式");
                return;
            }
            $("#payForm").ajaxSubmit();
        });

    }
    //外部可调用
    return {
        bindEvent: _bindEvent
    }
}();
//操作处理结束

$(function () {
    //页面绑定事件
    OperateHandle.bindEvent();
});