$(function(){
    var key = getCookie('key');
/*    $.getJSON(ApiUrl + '/index.php?act=connect&op=get_state&t=connect_sms_reg', function(result){
        if (result.datas != '0') {
            $('.register-tab').show();
        }
    });*/
    
	$.sValid.init({//注册验证
        rules:{
        	username:"required",
            userpwd:"required",            
            password_confirm:"required",
            email:{
            	required:true,
            	email:true
            }
        },
        messages:{
            username:"用户名必须填写！",
            userpwd:"密码必填!", 
            password_confirm:"确认密码必填!",
            email:{
            	required:"邮件必填!",
            	email:"邮件格式不正确"
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
	
	$('#registerbtn').click(function(){
        if (!$(this).parent().hasClass('form-btn')) {
            return false;
        }
		var username = $("input[name=username]").val();
		var pwd = $("input[name=userpwd]").val();
		var password_confirm = $("input[name=password_confirm]").val();
		var email = $("input[name=email]").val();
		var client = 'wap';
		
		
		if($.sValid()){
			if(username.length>15 || username.length<6){
				errorTipsShow("用户名请输入6-15个字符");
			}else if(pwd.length>15 || pwd.length<6){
				errorTipsShow("请输入6-15为密码");
			}else {
				
				$.ajax({
					type:'post',
					url:WapSiteUrl+"/register/common",	
					data:{memberName:username,memberPwd:pwd,repeatMemberPwd:password_confirm,email:email,client:client,agreeClause:1},
					dataType:'json',
					success:function(result){
						if(result.code==200){
							$.ajax({
								type:'post',
								url: WapSiteUrl+"/login/common",	
								data:{username:username,password:pwd,client:client,autoLogin:0},
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
				
		 }
		
	});
});