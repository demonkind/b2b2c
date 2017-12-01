var submiting = false;

//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //修改手机
        $("#sendAuthCode").click(function(){
            var mobile = $('#mobile').val();
            //验证手机是否有效
            if (!mobile) {
                Nc.alertError("请填写手机号");
                return false;
            }
            if (!/^0?(13|15|17|18|14)[0-9]{9}$/.test(mobile)) {
                Nc.alertError("请填写有效手机号");
                return false;
            }
            $("#sendAuthCode").hide();
            $("#sendAuthCodeSending").show();
            var params = {"mobile":mobile};
            $.ajax({
                type : "post",
                url : ncGlobal.memberRoot + "security/sendcode/bind/mobile",
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

        $('#mobile_form').validate({
            rules : {
                mobile : {
                    required : true,
                    mobile   : true
                },
                authCode : {
                    required   : true,
                    rangelength:[6,6]
                }
            },
            messages : {
                mobile : {
                    required : '<i class="icon-exclamation-sign"></i>手机号不能为空',
                    mobile   : '<i class="icon-exclamation-sign"></i>这不是一个有效的手机号'
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
            if($("#mobile_form").valid()){
                var params = {"authCode":$('#authCode').val(),"mobile":$('#mobile').val()};
                $.ajax({
                    type : "post",
                    url : ncGlobal.memberRoot + "security/mobile",
                    data : params,
                    async : false,
                    success : function(xhr){
                        if (xhr.code == 200) {
                            Nc.alertSucceed("手机绑定成功",{end:function(){
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