/**
 * Created by shopnc on 2016/1/12.
 */
//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {
        if (payGlobal.predepositAmount == 0) {
            $('#predepositPay').attr("disabled","disabled");
        }
        $('#predepositPay').on('click',function(){
            if ($(this).prop("checked")) {
                $("#payDiffPanel").html(Nc.priceFormat(Nc.number.sub(payGlobal.ordersOnlineAmount,payGlobal.predepositAmount)>0 ? Nc.number.sub(payGlobal.ordersOnlineAmount,payGlobal.predepositAmount) : 0 ));
                $("#payPwdPanel").show();
            } else {
                $("#payDiffPanel").html(Nc.priceFormat(payGlobal.ordersOnlineAmount));
                $("#payPwdPanel").hide();
            }
        })
        $('li[ncType="paymentType"]').on('click',function(){
            var isRemoveClass = false;
            if ($(this).hasClass("using")) {
                isRemoveClass = true;
            }
            $('li[ncType="paymentType"]').removeClass('using');
            if (isRemoveClass == false) {
                $(this).addClass('using');
                $("#paymentCode").val($(this).attr("dataPaymentCode"));
            } else {
                $("#paymentCode").val("");
            }

        })
        $("#payForm").on("nc.ajaxSubmit.success",function(e,result){
            if (result.code == 200) {
                window.location.href = result.url;
            } else {
                Nc.alertError(result.message);
            }
        })
        $('#pay').on('click',function(){
            if (!$('#predepositPay').prop('checked') && $("#paymentCode").val() == '') {
                Nc.alertError("请选择一种在线支付方式");
                return;
            }
            if ($('#predepositPay').prop('checked') && $("#paymentCode").val() == '' && $("#payDiffPanel").html() != '0.00') {
                Nc.alertError("请选择一种在线支付方式");
                return;
            }
            var payPwd = $("#payPwd").val();
            if ($('#predepositPay').prop('checked')) {
                if (payPwd == "") {
                    Nc.alertError("请输入支付密码");
                    return;
                }
                if (payPwd.length<6 || payPwd.length>20) {
                    Nc.alertError("请正确输入6~20位支付密码");
                    return;
                }
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