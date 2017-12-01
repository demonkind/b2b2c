$(function(){
    var mobile = getQueryString("mobile");
    var captcha = getQueryString("captcha");
    
    // 显示密码
    $('#checkbox').click(function(){
        if ($(this).prop('checked')) {
            $('#password').attr('type', 'text');
        } else {
            $('#password').attr('type', 'password');
        }
    });

    $.sValid.init({//注册验证
        rules:{
            password:"required"
        },
        messages:{
            password:"密码必填!"
        },
        callback:function (eId,eMsg,eRules){
            if(eId.length >0){
                var errorHtml = "";
                $.map(eMsg,function (idx,item){
                    errorHtml += "<p>"+idx+"</p>";
                });
                errorTipsShow(errorHtml);
            }else{
                errorTipsHide()
            }
        }  
    });
    
    $('#completebtn').click(function(){
        if (!$(this).parent().hasClass('ok')) {
            return false;
        }
        var password = $("#password").val();
        var client = 'wap';
        if($.sValid()){
            $.ajax({
                type:'post',
                url:WapSiteUrl+"/register/mobilesecond",  
                data:{mobile:mobile,memberPwd:password},
                dataType:'json',
                success:function(result){
                    if(result.code == 200){
                    	$.ajax({
							type:'post',
							url: WapSiteUrl+"/login/common",	
							data:{username:mobile,password:password,client:client,autoLogin:0},
							dataType:'json',
							success:function(result){
								if(result.code == 200){
									    var expireHours = 0;
									    if ($('#checkbox').prop('checked')) {
									        expireHours = 188;
									    }
									    // 更新cookie购物车
									    updateCookieCart(result.data.key);
										addCookie('username',result.data.username, expireHours);
										addCookie('key',result.data.key, expireHours);
										location.href = WapSiteUrl+'/members/index';
								}else{
					                errorTipsShow('<p>' + result.message + '</p>');
								}
							},
							error: function(request) {
			                    alert("Connection error");
			                }
						 });
                    }else{
                        errorTipsShow("<p>"+result.message+"</p>");
                    }
                }
            });         
        }
    });
});


