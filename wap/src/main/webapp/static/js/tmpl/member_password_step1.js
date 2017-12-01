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

  //重新获取验证码
    $('#send').click(function(){
        if($.sValid()){
            var captcha = $.trim($("#captcha").val());
            var mobile = $.trim($("#mobile").val());
            $.ajax({
                type:'post',
                url:WapSiteUrl+"/security/verMobile",
                data:{captcha:captcha,mobile:mobile},
                dataType:'json',
                success:function(result){
                    if(result.code == 200){
                    	location.href=WapSiteUrl+"/security/changePwd";
                    }else{
                        errorTipsShow('<p>' + result.message+ '</p>');
                        $("#codeimage").attr('src',ApiUrl+'/index.php?act=seccode&op=makecode&k='+$("#codekey").val()+'&t=' + Math.random());
                        $('#captcha').val('');
                    }
                }
            });
        }
    });

    $('#nextform').click(function(){
        if (!$(this).parent().hasClass('ok')) {
            return false;
        }
        var auth_code = $.trim($("#auth_code").val());
        if (auth_code) {
            $.ajax({
                type:'post',
                url:ApiUrl+"/index.php?act=member_account&op=modify_password_step3",
                data:{key:key,auth_code:auth_code},
                dataType:'json',
                success:function(result){
                    if(result.code == 200){
                        $.sDialog({
                            skin:"block",
                            content:'手机验证成功，正在跳转',
                            okBtn:false,
                            cancelBtn:false
                        });
                    	setTimeout("location.href = WapSiteUrl+'/tmpl/members/members_password_step2.html'",1000);
                    }else{
                        errorTipsShow('<p>' + result.message+ '</p>');
                    }
                }
            });
        }
    });
});
