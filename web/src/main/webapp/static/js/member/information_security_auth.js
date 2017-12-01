var submiting = false;

//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //发送动态码
        $("#firstButton").click(function(){
            //加载中
            if (submiting == true) {
                return false;
            }
            submiting = true;
            if($("#authFormFirst").valid()){
                var params = {"authType":$('#authType').val(), "captcha":$('#authCaptcha').val()};
                $.ajax({
                    type : "post",
                    url : ncGlobal.memberRoot + "security/sendcode/auth",
                    data : params,
                    async : false,
                    success : function(xhr){
                        if(xhr.code == 200) {
                            if ($('#authType').val() == "email") {
                                $("#authFormFirst").hide();
                                $("#authFormSecondMobile").hide();
                                $("#authFormSecondEmail").show();
                                $("#emailMsg").html("邮件已发送至："+ $("#emailEncrypt").val() +"，请勿泄露！");
                                $("#authFormSecondEmail").find("[ncType='sendAuthCode']").hide();
                                $("#authFormSecondEmail").find("[ncType='sendAuthCodeTimes']").html(xhr.data.authCodeResendTime);
                                $("#authFormSecondEmail").find("[ncType='sendAuthCodeAgain']").show();
                                $("#authFormSecondEmail").find("[ncType='sendAuthCodeTipTimes']").html(xhr.data.authCodeValidTime+"分钟");
                                $("#authFormSecondEmail").find("[ncType='sendAuthCodeTip']").show();
                                setTimeout("StepTimesEmail()", 1000);
                                //---------上线需要去除---------------------
                                //$("#authCodeEmail").val(xhr.data.authCode);
                            }else{
                                $("#authFormFirst").hide();
                                $("#authFormSecondEmail").hide();
                                $("#authFormSecondMobile").show();
                                $("#mobileMsg").html("短信已发送至："+ $("#mobileEncrypt").val() +"，请勿泄露！");
                                $("#authFormSecondMobile").find("[ncType='sendAuthCode']").hide();
                                $("#authFormSecondMobile").find("[ncType='sendAuthCodeTimes']").html(xhr.data.authCodeResendTime);
                                $("#authFormSecondMobile").find("[ncType='sendAuthCodeAgain']").show();
                                $("#authFormSecondMobile").find("[ncType='sendAuthCodeTipTimes']").html(xhr.data.authCodeValidTime+"分钟");
                                $("#authFormSecondMobile").find("[ncType='sendAuthCodeTip']").show();
                                setTimeout("StepTimesMobile()", 1000);
                                //---------上线需要去除---------------------
                                //$("#authCodeMobile").val(xhr.data.authCode);
                            }
                        } else {
                            Nc.alertError(xhr.message);
                        }
                        submiting = false;
                    }
                });
            }else{
                $('#auth_codeimage').attr("src", ncGlobal.webRoot+'captcha/getcaptcha?t='+Math.random());
                submiting = false;
            }
            return false;
        });

        $('#authFormFirst').validate({
            errorPlacement: function(error, element){
                var error_item = element.parent('dd');
                error_item.append(error);
            },
            onkeyup: false,
            rules : {
                authType :{
                    required  : true
                },
                authCaptcha : {
                    required : true,
                    remote   : {
                        url : ncGlobal.webRoot + 'captcha/check',
                        type: 'get',
                        data:{
                            captcha : function(){
                                return $('#authCaptcha').val();
                            }
                        }
                    }
                }
            },
            messages : {
                authType :{
                    required  : '<i class="icon-exclamation-sign"></i>请选择身份认证方式'
                },
                authCaptcha : {
                    required : "验证码不能为空",
                    remote   : "验证码错误"
                }
            }
        });

        $('#authFormSecondEmail').validate({
            onkeyup: false,
            rules : {
                authCode : {
                    required   : true,
                    rangelength:[6,6]
                }
            },
            messages : {
                authCode : {
                    required : '<i class="icon-exclamation-sign"></i>请填写动态码',
                    rangelength:"动态码错误"
                }
            }
        });

        $('#authFormSecondMobile').validate({
            onkeyup: false,
            rules : {
                authCode : {
                    required   : true,
                    rangelength:[6,6]
                }
            },
            messages : {
                authCode : {
                    required : '<i class="icon-exclamation-sign"></i>请填写动态码',
                    rangelength:"动态码错误"
                }
            }
        });

        $('#secondButtonEmail').click(function () {
            //加载中
            if (submiting == true) {
                return false;
            }
            submiting = true;
            if($("#authFormSecondEmail").valid()){
                var params = {"authCode":$('#authCodeEmail').val(),"authType":$('#authType').val()};
                $.ajax({
                    type : "post",
                    url : ncGlobal.memberRoot + "security/auth/" + $("#type").val(),
                    data : params,
                    async : false,
                    success : function(xhr){
                        if (xhr.code == 200) {
                            window.location.href = xhr.url;
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

        $('#secondButtonMobile').click(function () {
            //加载中
            if (submiting == true) {
                return false;
            }
            submiting = true;
            if($("#authFormSecondMobile").valid()){
                var params = {"authCode":$('#authCodeMobile').val(),"authType":$('#authType').val()};
                $.ajax({
                    type : "post",
                    url : ncGlobal.memberRoot + "security/auth/" + $("#type").val(),
                    data : params,
                    async : false,
                    success : function(xhr){
                        if (xhr.code == 200) {
                            window.location.href = xhr.url;
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

        //重新发送动态码
        $("[ncType='sendAuthCode']").click(function(){
            //加载中
            if (submiting == true) {
                return false;
            }
            submiting = true;
            var params = {"authType":$('#authType').val()};
            $.ajax({
                type : "post",
                url : ncGlobal.memberRoot + "security/sendcode/auth/simple",
                data : params,
                async : false,
                success : function(xhr){
                    if(xhr.code == 200) {
                        if ($('#authType').val() == "email") {
                            $("#authFormSecondEmail").find("[ncType='sendAuthCode']").hide();
                            $("#authFormSecondEmail").find("[ncType='sendAuthCodeTimes']").html(xhr.data.authCodeResendTime);
                            $("#authFormSecondEmail").find("[ncType='sendAuthCodeAgain']").show();
                            setTimeout("StepTimesEmail()", 1000);
                            //---------上线需要去除---------------------
                            //$("#authCodeEmail").val(xhr.data.authCode);
                        }else{
                            $("#authFormSecondMobile").find("[ncType='sendAuthCode']").hide();
                            $("#authFormSecondMobile").find("[ncType='sendAuthCodeTimes']").html(xhr.data.authCodeResendTime);
                            $("#authFormSecondMobile").find("[ncType='sendAuthCodeAgain']").show();
                            setTimeout("StepTimesMobile()", 1000);
                            //---------上线需要去除---------------------
                            //$("#authCodeMobile").val(xhr.data.authCode);
                        }
                    } else {
                        Nc.alertError(xhr.message);
                    }
                    submiting = false;
                }
            });
            return false;
        });
    }
    //外部可调用
    return {
        bindEvent: _bindEvent
    }
}();
//操作处理结束

function StepTimesEmail() {
    var form = $("#authFormSecondEmail");
    var num = parseInt($(form).find("[ncType='sendAuthCodeTimes']").html());
    num = num - 1;
    $(form).find("[ncType='sendAuthCodeTimes']").html(num);
    if (num <= 0) {
        $(form).find("[ncType='sendAuthCode']").show();
        $(form).find("[ncType='sendAuthCodeAgain']").hide();
    } else {
        setTimeout("StepTimesEmail()", 1000);
    }
}
function StepTimesMobile() {
    var form = $("#authFormSecondMobile");
    var num = parseInt($(form).find("[ncType='sendAuthCodeTimes']").html());
    num = num - 1;
    $(form).find("[ncType='sendAuthCodeTimes']").html(num);
    if (num <= 0) {
        $(form).find("[ncType='sendAuthCode']").show();
        $(form).find("[ncType='sendAuthCodeAgain']").hide();
    } else {
        setTimeout("StepTimesMobile()", 1000);
    }
}
$(function () {
    //页面绑定事件
    OperateHandle.bindEvent();
});