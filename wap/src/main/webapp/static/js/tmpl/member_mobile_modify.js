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

    $.ajax({
        type:'get',
        url:ApiUrl+"/index.php?act=member_account&op=get_mobile_info",
        data:{key:key},
        dataType:'json',
        success:function(result){
            if(result.code == 200){
            	if (result.datas.state) {
            		$('#mobile').html(result.datas.mobile);
            	} else {
            		location.href = WapSiteUrl+'/tmpl/members/members_mobile_bind.html';
            	}
            }
        }
    });

    $.sValid.init({
        rules:{
            captcha: {
            	required:true,
            	minlength:4
            }
        },
        messages:{
            captcha: {
            	required : "请填写图形验证码",
            	minlength : "图形验证码不正确"
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
                url:WapSiteUrl+"/security/verMobile",
                data:{mobile:mobile,captcha:captcha},
                dataType:'json',
                success:function(result){
                    if(result.code == 200){
                    	$('#send').hide();
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

    $('#nextform').click(function(){
    	var auth_code = $.trim($("#auth_code").val());
    	var mobile = $.trim($("#mobile").val());
    	$.ajax({
            type:'post',
            url:WapSiteUrl+"/security/mobChange",
            data:{authCode:auth_code,mobile:mobile},
            dataType:'json',
            success:function(result){
                if(result.code == 200){
                	location.href = WapSiteUrl+"/security/mobChangePage";            
                }else{
                	errorTipsShow('<p>' + result.message + '</p>');
                    loadSeccode();
                    $('#captcha').val('');
                }
            }
        });
    });
        
});
