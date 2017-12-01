$(function() {
    var key = getCookie('key');
    if (!key) {
        window.location.href = WapSiteUrl+'/tmpl/members/login.html';
        return;
    }

    $.ajax({
        type:'get',
        url:ApiUrl+"/index.php?act=member_account&op=modify_password_step4",
        data:{key:key},
        dataType:'json',
        success:function(result){
            if(result.code != 200){
            	errorTipsShow('<p>权限不足或操作超时</p>');
            	setTimeout("location.href = WapSiteUrl+'/tmpl/members/members_password_step1.html'",2000);
            }
        }
    });

    $.sValid.init({
        rules:{
            password: {
            	required:true,
            	minlength:6,
            	maxlength:20
            },
            password1: {
            	required:true,
            	equalTo : '#password'
            }
        },
        messages:{
        	password: {
            	required : "请填写登录密码",
            	minlength : "请正确填写登录密码",
            	maxlength : "请正确填写登录密码"
            },
            password1 : {
            	required:"请填写确认密码",
            	equalTo : '两次密码输入不一致'
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
    
    function time(){
    	var currentTime=parseInt($("#time").text());
    	var timer=setInterval(function(){    		
    		currentTime-=1;
    		if(currentTime==0){
    			$("#time").text(currentTime);  	
    			$(".code-countdown").css("display","none");
        		$(".code-again").css("display","block");
    		}
    		$("#time").text(currentTime);  		
    	},1000);
    	
    	
    }
    setTimeout(time,1000);
    
    
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
        if($.sValid()){
            var password = $.trim($("#password").val());
            var password1 = $.trim($("#password1").val());
            var auth_code = $("#auth_code").val();
            $.ajax({
                type:'post',
                url:WapSiteUrl+"/security/updatePwd",
                data:{authCode:auth_code,password:password,password1:password1},
                dataType:'json',
                success:function(result){
                    if(result.code == 200){
                        $.sDialog({
                            skin:"block",
                            content:'密码修改成功',
                            okBtn:false,
                            cancelBtn:false
                        });
                    	setTimeout("location.href = WapSiteUrl+'/members/index'",2000);
                    }else{
                        errorTipsShow('<p>' + result.message + '</p>');
                    }
                }
            });
        }

    });
});
