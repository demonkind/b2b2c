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
            if($("#cashForm").valid()){
                var params = {"amount":$("#amount").val(),"receiveCompany":$("#receiveCompany").val(),"receiveAccount":$("#receiveAccount").val(),"receiveUser":$("#receiveUser").val(),"payPwd":$("#payPwd").val()};
                $.ajax({
                    type : "post",
                    url : ncGlobal.memberRoot + "predeposit/cash",
                    data : params,
                    async : false,
                    success : function(xhr){
                        if (xhr.code == 200) {
                            window.location.href = ncGlobal.memberRoot+"predeposit/cash/list";
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

        $("#cashForm").validate({
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
                },
                receiveCompany : {
                    required  : true
                },
                receiveAccount : {
                    required  : true
                },
                receiveUser : {
                    required  : true
                },
                payPwd : {
                    required  : true
                }
            },
            messages : {
                amount : {
                    required  : '<i class="icon-exclamation-sign"></i>请添加提现金额',
                    number    : '<i class="icon-exclamation-sign"></i>提现金额为大于或者等于0.01的数字',
                    min       : '<i class="icon-exclamation-sign"></i>提现金额为大于或者等于0.01的数字'
                },
                receiveCompany : {
                    required  : '<i class="icon-exclamation-sign"></i>请输入收款方式'
                },
                receiveAccount : {
                    required  : '<i class="icon-exclamation-sign"></i>请输入收款账号'
                },
                receiveUser : {
                    required  : '<i class="icon-exclamation-sign"></i>请输入开户人姓名'
                },
                payPwd : {
                    required  : '<i class="icon-exclamation-sign"></i>请输入支付密码'
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