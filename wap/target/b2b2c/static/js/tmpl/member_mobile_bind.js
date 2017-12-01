$(function() {
    var key = getCookie('key');
    if (!key) {
        window.location.href = WapSiteUrl+'/tmpl/members/login.html';
        return;
    }

    //加载验证码
    loadSeccode();
    $("#refreshcode").bind('click',function(){
        loadSeccode();
    });

	$('#mobile').on('blur',function(){
		if ($(this).val() != '' && ! /^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test($(this).val())) {
			$(this).val(/\d+/.exec($(this).val()));
		}
	});

    $.ajax({
        type:'get',
        url:ApiUrl+"/index.php?act=member_account&op=get_mobile_info",
        data:{key:key},
        dataType:'json',
        success:function(result){
            if(result.datas.state){
				$('#mobile').val(result.datas.mobile);
            }
        }
    });

    $.sValid.init({
        rules:{
            captcha: {
            	required:true,
            	minlength:4
            },
        	mobile: {
                required:true,
                mobile:true
        	}
        },
        messages:{
            captcha: {
            	required : "请填写图形验证码",
            	minlength : "图形验证码不正确"
            },
            mobile: {
            	required : "请填写手机号",
                mobile : "手机号码不正确"
            }
        },
        callback:function (eId,eMsg,eRules){
            if(eId.length >0){
                var errorHtml = "";
                $.map(eMsg,function (idx,item){
                    errorHtml += "<p>"+idx+"</p>";
                });
                errorTipsShow(errorHtml);
            }else{
                errorTipsHide();
            }
        }
    });

    $('#send').click(function(){
        if($.sValid()){
            var mobile = $.trim($("#mobile").val());
            var captcha = $.trim($("#captcha").val());
            $.ajax({
                type:'post',
                url:WapSiteUrl+"/security/bindMobile",
                data:{mobile:mobile,captcha:captcha},
                dataType:'json',
                success:function(result){
                    if(result.code == 200){
                    	$('#send').hide();
                    	$('#auth_code').removeAttr('readonly');
                        $('.code-countdown').show().find('em').html(result.data.authCodeResendTime);
                        $.sDialog({
                            skin:"block",
                            content:'短信验证码已发出',
                            okBtn:false,
                            cancelBtn:false
                        });
                        var times_Countdown = setInterval(function(){
                            var em = $('.code-countdown').find('em');
                            var t = parseInt(em.html() - 1);
                            if (t == 0) {
                            	$('#send').show();
                                $('.code-countdown').hide();
                                clearInterval(times_Countdown);
                                loadSeccode();
                                $('#captcha').val('');
                            } else {
                                em.html(t);
                            }
                        },1000);
                    }else{
                        errorTipsShow('<p>' + result.message + '</p>');
                        loadSeccode();
                        $('#captcha').val('');
                    }
                }
            });
        }
    });
    
    
    //点击下一步
    $('#nextform').click(function(){
        if (!$(this).parent().hasClass('form-btn')) {
            return false;
        }
        var auth_code = $.trim($("#auth_code").val());
        var mobile = $.trim($("#mobile").val());
        var codekey = $.trim($("#codekey").val());
        
        if (auth_code) {
            $.ajax({
                type:'post',
                url:WapSiteUrl+"/security/boundPhone",
                data:{mobile:mobile,authCode:auth_code},
                dataType:'json',
                success:function(result){
                    if(result.code == 200){
                        $.sDialog({
                            skin:"block",
                            content:'绑定成功',
                            okBtn:false,
                            cancelBtn:false
                        });
                    	setTimeout("location.href=WapSiteUrl + '/security/index';",2000);
                    }else{
                        errorTipsShow('<p>' + result.message + '</p>');
                    }
                }
            });
        }
    });
});
