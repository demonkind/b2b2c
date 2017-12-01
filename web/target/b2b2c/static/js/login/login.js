$(function() {
	//注册方式切换
	$('#loginModule').tabulous({
		effect: 'slideLeft'
	});

	$("#loginModule .tabs-nav li a").click(function(){
		var div_form = $(this).attr('href');
		$(div_form).find("[nc_type='changeCaptcha']").trigger("click");
		$("#mobileModule").show();
		return false;
	});
	
	// 勾选自动登录显示隐藏文字
    $('input[name="autoLogin"]').click(function(){
        if ($(this).attr('checked')){
            $(this).attr('checked', true).next().show();
        } else {
            $(this).attr('checked', false).next().hide();
        }
    });

	$("#loginSubmit").click(function(){
		if($("#loginForm").valid()){
			var autoLogin = 0;
			if ($("#autoLogin").attr("checked") == "checked") {
				autoLogin = 1;
			}
			var params = {"loginName":$("#loginName").val(), "memberPwd":$("#memberPwd").val(),"captcha":$("#captcha").val(),"autoLogin":autoLogin};
			$.post(ncGlobal.webRoot + "login/common", params, function(xhr){
				if (xhr.code == 200) {
					window.location.href = ncGlobal.memberRoot;
				}else{
					$("#codeimage").attr("src", ncGlobal.webRoot+'captcha/getcaptcha?t=' + Math.random());
					Nc.alertError(xhr.message);
				}
			});
		}else{
			$("#codeimage").attr("src", ncGlobal.webRoot+'captcha/getcaptcha?t=' + Math.random());
		}
		return false;
	});

	$("#mobileSubmit").click(function(){
		if($("#mobileForm").valid()){
			var params = {"mobile":$("#mobile").val(), "authCode":$("#authCode").val()};
			$.post(ncGlobal.webRoot + "login/mobile", params, function(xhr){
				if (xhr.code == 200) {
					window.location.href = ncGlobal.memberRoot;
				}else{
					Nc.alertError(xhr.message);
				}
			});
		}
		return false;
	});

	$("#loginForm").validate({
		errorPlacement: function(error, element){
			var error_item = element.parent('dd');
			error_item.append(error);
		},
		onkeyup: false,
		rules : {
			loginName : {
				required : true
			},
			memberPwd : {
				required : true
			},
			captcha : {
				required : true,
				remote   : {
					url : ncGlobal.webRoot + 'captcha/check',
					type: 'get',
					data:{
						captcha : function(){
							return $('#captcha').val();
						}
					},
					complete: function(data) {
						if(data.responseText == 'false') {
							$("#codeimage").attr("src", ncGlobal.webRoot+'captcha/getcaptcha?t=' + Math.random());
						}
					}
				}
			}
		},
		messages : {
			loginName : {
				required : '<i class="icon-exclamation-sign"></i>用户名不能为空'
			},
			memberPwd  : {
				required : '<i class="icon-exclamation-sign"></i>密码不能为空'
			},
			captcha : {
				required : '<i class="icon-remove-circle" title="验证码不能为空"></i>',
				remote   : '<i class="icon-remove-circle" title="验证码错误"></i>'
			}
		}
	});

	$("#mobileForm").validate({
		errorPlacement: function(error, element){
			var error_item = element.parent('dd');
			error_item.append(error);
		},
		onkeyup: false,
		rules : {
			mobile : {
				required : true,
				mobile : true
			},
			authCode : {
				required : true,
				rangelength:[6,6]
			}
		},
		messages : {
			mobile : {
				required : '<i class="icon-exclamation-sign"></i>手机号不能为空',
				mobile   : '<i class="icon-exclamation-sign"></i>请输入正确的手机号'
			},
			authCode : {
				required : '<i class="icon-exclamation-sign"></i>短信动态码不能为空',
				rangelength : '<i class="icon-exclamation-sign"></i>短信动态码错误',
			}
		}
	});

	//发送手机验证码
	$("#sendSmsCode").click(function () {
		if($("#mobile").val().length != 11){
			$("#codeimageMobile").attr("src", ncGlobal.webRoot+'captcha/getcaptcha?t=' + Math.random());
			Nc.alertError("请输入正确的手机号");
			return false;
		}
		//验证码验证
		var captchaMobile = $("#captchaMobile").val();
		if (!captchaMobile) {
			$("#codeimageMobile").attr("src", ncGlobal.webRoot+'captcha/getcaptcha?t=' + Math.random());
			Nc.alertError("请输入验证码");
			return false;
		}
		var result = true;
		$.ajax({
			type: "get",
			url: ncGlobal.webRoot + "captcha/check",
			data: {captcha:captchaMobile},
			dataType: "text",
			async : false,
			success: function(xhr){
				if (xhr == "false") {
					result = false;
				}
			}
		});
		if (result == false) {
			Nc.alertError("验证码错误");
			$("#codeimageMobile").attr("src", ncGlobal.webRoot+'captcha/getcaptcha?t=' + Math.random());
			return false;
		}
		//发送动态码
		var ajaxurl = ncGlobal.webRoot + 'login/sendsmscode?mobile='+$('#mobile').val();
		$.ajax({
			type: "post",
			url: ajaxurl,
			async: false,
			success: function(xhr){
				if(xhr.code == 200) {
					Nc.alertSucceed("短信动态码已发出");
					$("#authCode").val(xhr.data.authCode);
					return;
				} else {
					$("#codeimageMobile").attr("src", ncGlobal.webRoot+'captcha/getcaptcha?t=' + Math.random());
					Nc.alertError(xhr.message);
					return;
				}
			}
		});
		return false;
	});
});