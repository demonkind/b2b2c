var submiting = false;

//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        $("#submitBtn").click(function(){
            //加载中
            if (submiting == true) {
                return false;
            }
            submiting = true;
            if($("#rechargeForm").valid()){
                var params = {"amount":$("#amount").val()};
                $.ajax({
                    type : "post",
                    url : ncGlobal.memberRoot + "predeposit/recharge",
                    data : params,
                    async : false,
                    success : function(xhr){
                        if (xhr.code == 200) {
                            window.location.href = ncGlobal.webRoot+"buy/pay/pdpay/payment/"+xhr.data.rechargeId;
                        }else{
                            Nc.alertError(xhr.message);
                        }
                        submiting = false;
                    }
                });
            }else{
                submiting = false;
            }
            return false;
        });

        $("#rechargeForm").validate({
            errorPlacement: function(error, element){
                var error_td = element.parent('dd').children('span');
                error_td.append(error);
            },
            onkeyup: false,
            rules : {
                amount : {
                    required  : true,
                    number    : true,
                    min       : 0.01
                }
            },
            messages : {
                amount : {
                    required  : '<i class="icon-exclamation-sign"></i>请添加充值金额',
                    number    : '<i class="icon-exclamation-sign"></i>充值金额为大于或者等于0.01的数字',
                    min       : '<i class="icon-exclamation-sign"></i>充值金额为大于或者等于0.01的数字'
                }
            }
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