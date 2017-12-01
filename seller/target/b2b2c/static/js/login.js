/**
 * Created by cj on 2015/12/17.
 */
$(document).ready(function() {



    $(document).keyup(function(event){
      if(event.keyCode ==13){
        $.find(".layui-layer").length || $("#submitLogin").trigger("click")
      }
    });

    $(document).keyup(function(event){
      if(event.keyCode ==13){
        $("#submit").trigger("click");
      }
    });

    //更换验证码
    function changeCaptcha() {
        $('#captcha_image').attr('src', ncGlobal.sellerRoot + 'captcha/captcha?t=' + Math.random());
        $('#captcha').select();
    }

    //点击验证码图片更换验证码
    $('#captchaImageChange').on('click', function() {
        changeCaptcha();
    });

    //注册失败处理
    $("#loginForm")
        .on("nc.ajaxSubmit.error", function (e, errorMessage) {
            changeCaptcha();
            Nc.alertError(errorMessage);
        })
        .on("nc.ajaxSubmit.success",function(e,data) {
             Nc.go(data.url);
        })

    $("#loginForm").validate({
        submitHandler: function(form) {
            $(form).ajaxSubmit();
        },
        onkeyup: false,
        rules:{
            username:{
                required:true
            },
            password:{
                required:true
            },
            captcha:{
                required:true,
                remote:{
                    url : ncGlobal.sellerRoot +'captcha/check',
                    type:"get",
                    data:{
                        captcha:function() {
                            return $("#captcha").val();
                        }
                    },
                    complete: function(data) {
                        if(data.responseText == 'false') {
                            changeCaptcha();
                        }
                    }
                }
            }
        },
        messages:{
            username:{
                required	:	'<i class="icon-exclamation-sign"></i>用户名不能为空'
            },
            password:{
                required	:	'<i class="icon-exclamation-sign"></i>密码不能为空'
            },
            captcha			: {
                required	:	'<i class="icon-exclamation-sign"></i>验证码不能为空',
                remote		:	'<i class="icon-exclamation-sign"></i>验证码错误'
            }
        }
    });

    /**
     * 注册
     */
    $("#btnShowRegisterDialog").on("click", function() {
        changeCaptchaRegister();
        layer.open({
            type: 1,
            area: ['640px', '540px'],
            title: "商家账号注册",
            shadeClose: true,
            content: $('#dialogRegister')
        });
    });

    //更换验证码
    function changeCaptchaRegister() {
        $('#captcha_register_image').attr('src', ncGlobal.sellerRoot +'captcha/captcha?t=' + Math.random());
        $('#captchaRegister').val("");
        $('#captchaRegister').select();
    }

    //点击验证码图片更换验证码
    $('#captchaRegisterChange').on('click', function() {
        changeCaptchaRegister();
    });

    //注册失败处理
    $("#registerForm")
        .on("nc.ajaxSubmit.error", function (e, errorMessage) {
            changeCaptchaRegister();
            Nc.alertError(errorMessage);
        })

    $("#registerForm").validate({
        submitHandler: function(form) {
            $(form).ajaxSubmit();
        },
        onkeyup: false,
        rules:{
            sellerName:{
                required:true,
                rangelength : [3,20],
                remote   : {
                    url :ncGlobal.sellerRoot +'register/is_seller_exist',
                    type:'get',
                    data:{
                        sellerName : function() {
                            return $('#sellerName').val();
                        }
                    }
                }
            },
            sellerPassword:{
                required:true
            },
            sellerPassword2: {
                required : true,
                equalTo  : '#sellerPassword'
            },
            sellerEmail : {
                required : true,
                email    : true,
                remote   : {
                    url :ncGlobal.sellerRoot +'register/is_email_exist',
                    type: 'get',
                    data: {
                        email : function() {
                            return $('#sellerEmail').val();
                        }
                    }
                }
            },
            captcha : {
                required : true,
                remote   : {
                    url : ncGlobal.sellerRoot +'captcha/check',
                    type: 'get',
                    data:{
                        captcha : function(){
                            return $('#captchaRegister').val();
                        }
                    },
                    complete: function(data) {
                        if(data.responseText == 'false') {
                            changeCaptchaRegister();
                        }
                    }
                }
            }
        },
        messages:{
            sellerName		:{
                required	:	'<i class="icon-exclamation-sign"></i>用户名不能为空',
                rangelength	:	'<i class="icon-exclamation-sign"></i>用户名必须在3-20个字符之间',
                remote		:	'<i class="icon-exclamation-sign"></i>用户名已存在',
            },
            sellerPassword	:{
                required	:	'<i class="icon-exclamation-sign"></i>密码不能为空'
            },
            sellerPassword2	:{
                required	:	'<i class="icon-exclamation-sign"></i>请再次输入密码',
                equalTo		:	'<i class="icon-exclamation-sign"></i>两次输入的密码不一致'
            },
            sellerEmail 	: {
                required 	:	'<i class="icon-exclamation-sign"></i>电子邮箱不能为空',
                email    	:	'<i class="icon-exclamation-sign"></i>这不是一个有效的电子邮箱',
                remote	 	:	'<i class="icon-exclamation-sign"></i>该电子邮箱已经注册'
            },
            captcha 		: {
                required 	:	'<i class="icon-exclamation-sign"></i>请输入验证码',
                remote	 	:	'<i class="icon-exclamation-sign"></i>验证码不正确'
            }
        }
    });
// banner切换
    var defaultInd = 0;
    var list = $('#js_ban_content').children();
    var count = 0;
    var change = function(newInd, callback) {
        if (count) return;
        count = 2;
        $(list[defaultInd]).fadeOut(400, function() {
            count--;
            if (count <= 0) {
                if (start.timer) window.clearTimeout(start.timer);
                callback && callback();
            }
        });
        $(list[newInd]).fadeIn(400, function() {
            defaultInd = newInd;
            count--;
            if (count <= 0) {
                if (start.timer) window.clearTimeout(start.timer);
                callback && callback();
            }
        });
    }

    var next = function(callback) {
        var newInd = defaultInd + 1;
        if (newInd >= list.length) {
            newInd = 0;
        }
        change(newInd, callback);
    }

    var start = function() {
        if (start.timer) window.clearTimeout(start.timer);
        start.timer = window.setTimeout(function() {
            next(function() {
                start();
            });
        }, 8000);
    }

    start();

    $('#js_ban_button_box').on('click', 'a', function() {
        var btn = $(this);
        if (btn.hasClass('right')) {
            //next
            next(function() {
                start();
            });
        } else {
            //prev
            var newInd = defaultInd - 1;
            if (newInd < 0) {
                newInd = list.length - 1;
            }
            change(newInd, function() {
                start();
            });
        }
        return false;
    });
// 入驻指南切换
    $(".tabs-nav > li > h3").bind('mouseover', (function(e) {
        if (e.target == this) {
            var tabs = $(this).parent().parent().children("li");
            var panels = $(this).parent().parent().parent().children(".tabs-panel");
            var index = $.inArray(this, $(this).parent().parent().find("h3"));
            if (panels.eq(index)[0]) {
                tabs.removeClass("tabs-selected").eq(index).addClass("tabs-selected");
                panels.addClass("tabs-hide").eq(index).removeClass("tabs-hide");
            }
        }
    }))
});
