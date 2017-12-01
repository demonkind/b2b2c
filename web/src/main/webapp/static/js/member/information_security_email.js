var submiting = false;

//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //发送动态码
        $("#sendAuthCode").click(function(){
            var email = $('#email').val();
            //验证邮箱是否有效
            if (!email) {
                Nc.alertError("请填写邮箱");
                return false;
            }
            if (!/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i.test(email)) {
                Nc.alertError("请填写有效邮箱");
                return false;
            }
            $("#sendAuthCode").hide();
            $("#sendAuthCodeSending").show();
            var params = {"email":email};
            $.ajax({
                type : "post",
                url : ncGlobal.memberRoot + "security/sendcode/bind/email",
                data : params,
                async : false,
                success : function(xhr){
                    if(xhr.code == 200) {
                        $("#sendAuthCodeSending").hide();
                        $("#sendAuthCodeAgain").show();
                        $("#authCode").val(xhr.data.authCode);
                        $("#sendAuthCodeTimes").html(xhr.data.authCodeResendTime);
                        $("#sendAuthCodeTipTimes").html(xhr.data.authCodeValidTime+"分钟");
                        $("#sendAuthCodeTip").show();
                        setTimeout("StepTimes()", 1000);
                    } else {
                        $("#sendAuthCode").show();
                        $("#sendAuthCodeSending").hide();
                        Nc.alertError(xhr.message);
                    }
                }
            });
            return false;
        });

        $('#email_form').validate({
            rules : {
                email : {
                    required : true,
                    email    : true
                },
                authCode : {
                    required   : true,
                    rangelength:[6,6]
                }
            },
            messages : {
                email : {
                    required : '<i class="icon-exclamation-sign"></i>邮箱不能为空',
                    email    : '<i class="icon-exclamation-sign"></i>这不是一个有效的电子邮箱'
                },
                authCode : {
                    required : '<i class="icon-exclamation-sign"></i>请填写动态码',
                    rangelength:"动态码错误"
                }
            }
        });

        $('#submitButton').click(function () {
            //加载中
            if (submiting == true) {
                return false;
            }
            submiting = true;
            if($("#email_form").valid()){
                var params = {"authCode":$('#authCode').val(),"email":$('#email').val()};
                $.ajax({
                    type : "post",
                    url : ncGlobal.memberRoot + "security/email",
                    data : params,
                    async : false,
                    success : function(xhr){
                        if (xhr.code == 200) {
                            Nc.alertSucceed("邮箱绑定成功",{end:function(){
                                window.location.href = ncGlobal.memberRoot+"security";
                            }});
                        }else{
                            if (xhr.url) {
                                window.location.href = xhr.url;
                            }else{
                                Nc.alertError(xhr.message);
                            }
                        }
                        submiting = false;
                    }
                });
            }else{
                submiting = false;
            }
            return false;
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

function StepTimes() {
    var num = parseInt($("#sendAuthCodeTimes").html());
    num = num - 1;
    $("#sendAuthCodeTimes").html(num);
    if (num <= 0) {
        $("#sendAuthCode").show();
        $("#sendAuthCodeSending").hide();
        $("#sendAuthCodeAgain").hide();
    } else {
        setTimeout("StepTimes()", 1000);
    }
}