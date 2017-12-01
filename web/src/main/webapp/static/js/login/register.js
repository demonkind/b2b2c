var submiting = false;

$(function() {
	//注册方式切换
	$('#registerModule').tabulous({
		effect: 'slideLeft'
	});

	$("#registerModule .tabs-nav li a").click(function(){
		var div_form = $(this).attr('href');
		$(div_form).find("[nc_type='changeCaptcha']").trigger("click");
		$("#mobileModule").show();
		$("#mobileFormFirst").show();
		$("#mobileFormSecond").hide();
		return false;
	});

	$("[nc_type='btnShowClauseDialog']").on("click", function() {
		layer.open({
			type: 1,
			area: ['750px', '550px'],
			title: "会员注册协议",
			shadeClose: true,
			content: $('#dialogClause')
		});
		return false;
	});

	$("#registerSubmit").click(function(){
		//加载中
		if (submiting == true) {
			return false;
		}
		submiting = true;
		if($("#registerForm").valid()){
			var agreeClause = 0;
			if ($("#agreeClause").attr("checked") == "checked") {
				agreeClause = 1;
			}
			var params = {"memberName":$("#memberName").val(), "memberPwd":$("#memberPwd").val(), "repeatMemberPwd":$("#repeatMemberPwd").val(),"email":$("#email").val(),"captcha":$("#captcha").val(),"agreeClause":agreeClause};
			$.ajax({
				type : "post",
				url : ncGlobal.webRoot + "register/common",
				data : params,
				async : false,
				success : function(xhr){
					if (xhr.code == 200) {
						window.location.href = ncGlobal.memberRoot;
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

	$("#mobileSubmitFirst").click(function(){
		//加载中
		if (submiting == true) {
			return false;
		}
		submiting = true;
		if($("#mobileFormFirst").valid()){
			var agreeClause = 0;
			if ($("#mobileAgreeClause").attr("checked") == "checked") {
				agreeClause = 1;
			}
			var mobile = $("#mobile").val();
			var params = {"mobile":mobile, "agreeClause":agreeClause, "captcha":$("#captchaMobile").val()};
			$.ajax({
				type : "post",
				url : ncGlobal.webRoot + "register/mobilefirst",
				data : params,
				async : false,
				success : function(xhr){
					if(xhr.code == 200) {
						$("#mobileFormFirst").hide();
						$("#mobileFormSecond").show();
						$("#authCode").val(xhr.data.authCode);
						$("#mobileSecond").val(mobile);
						var mobileStr = mobile.replace(/([0-9]{3})([0-9]{6})([0-9]{2})/, "$1******$3");
						$("#mobilemsg").html("短信已发送至"+ mobileStr +"，请在"+xhr.data.authCodeValidTime+"分钟内完成验证。");
						$("#mobileFormSecond").find("[ncType='sendAuthCode']").hide();
						$("#mobileFormSecond").find("[ncType='sendAuthCodeTimes']").html(xhr.data.authCodeResendTime);
						$("#mobileFormSecond").find("[ncType='sendAuthCodeAgain']").show();
						setTimeout("StepTimesMobile()", 1000);
					} else {
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
	/**
	 * 重新发送动态码
	 */
	$("#mobileFormSecond").find("[ncType='sendAuthCode']").click(function(){
		//加载中
		if (submiting == true) {
			return false;
		}
		submiting = true;
		var mobile = $("#mobile").val();
		var params = {"mobile":mobile};
		$.ajax({
			type : "post",
			url : ncGlobal.webRoot + "register/mobilesecond/again",
			data : params,
			async : false,
			success : function(xhr){
				if(xhr.code == 200) {
					$("#mobileFormSecond").find("[ncType='sendAuthCode']").hide();
					$("#mobileFormSecond").find("[ncType='sendAuthCodeTimes']").html(xhr.data.authCodeResendTime);
					$("#mobileFormSecond").find("[ncType='sendAuthCodeAgain']").show();
					setTimeout("StepTimesMobile()", 1000);
				} else {
					Nc.alertError(xhr.message);
				}
				submiting = false;
			}
		});
		return false;
	});
	$("#mobileSubmitSecond").click(function(){
		//加载中
		if (submiting == true) {
			return false;
		}
		submiting = true;
		if($("#mobileFormSecond").valid()){
			var params = {"mobile":$("#mobile").val(), "authCode":$("#authCode").val(), "memberPwd":$("#mobileMemberPwd").val(), "repeatMemberPwd":$("#mobileRepeatMemberPwd").val()};
			$.ajax({
				type : "post",
				url : ncGlobal.webRoot + "register/mobilesecond",
				data : params,
				async : false,
				success : function(xhr){
					if (xhr.code == 200) {
						window.location.href = ncGlobal.memberRoot;
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

	jQuery.validator.addMethod("nccheckname", function(value, element) {
		return this.optional(element) || (/^[A-Za-z0-9\u4e00-\u9fa5_-]+$/i.test(value) && !/^\d+$/.test(value));
	}, "Letters only please");
	$("#registerForm").validate({
		errorPlacement: function(error, element){
			var error_item = element.parent('dd');
			error_item.append(error);
		},
		onkeyup: false,
		rules : {
			memberName : {
				required : true,
				rangelength : [6,20],
				nccheckname : true,
				remote   : {
					url : ncGlobal.webRoot + 'register/membernameexist',
					type:'get',
					data:{
						memberName : function(){
							return $('#memberName').val();
						}
					}
				}
			},
			memberPwd : {
				required : true,
				minlength: 6,
				maxlength: 20
			},
			repeatMemberPwd : {
				required : true,
				equalTo  : '#memberPwd'
			},
			email : {
				required : true,
				email    : true,
				remote   : {
					url : ncGlobal.webRoot + 'register/emailexist',
					type: 'get',
					data:{
						email : function(){
							return $('#email').val();
						}
					}
				}
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
			},
			agreeClause : {
				required : true
			}
		},
		messages : {
			memberName : {
				required : "<i class='icon-exclamation-sign'></i>用户名不能为空",
				rangelength : "<i class='icon-exclamation-sign'></i>用户名必须在6-20个字符之间",
				nccheckname: "<i class='icon-exclamation-sign'></i>请使用6-20个中、英文、数字及“-”符号，且不能全为数字",
				remote	 : "<i class='icon-exclamation-sign'></i>用户名已存在"
			},
			memberPwd  : {
				required : "<i class='icon-exclamation-sign'></i>密码不能为空",
				minlength: "<i class='icon-exclamation-sign'></i>密码长度应在6-20个字符之间",
				maxlength: "<i class='icon-exclamation-sign'></i>密码长度应在6-20个字符之间"
			},
			repeatMemberPwd : {
				required : "<i class='icon-exclamation-sign'></i>请再次输入密码",
				equalTo  : "<i class='icon-exclamation-sign'></i>两次输入的密码不一致"
			},
			email : {
				required : "<i class='icon-exclamation-sign'></i>邮箱不能为空",
				email    : "<i class='icon-exclamation-sign'></i>这不是一个有效的电子邮箱",
				remote   : "<i class='icon-exclamation-sign'></i>邮箱已存在"
			},
			captcha : {
				required : "<i class='icon-remove-circle' title='验证码不能为空'></i>",
				remote   : "<i class='icon-remove-circle' title='验证码错误'></i>"
			},
			agreeClause : {
				required : "<i class='icon-exclamation-sign'></i>请勾选服务协议"
			}
		}
	});

	$("#mobileFormFirst").validate({
		errorPlacement: function(error, element){
			var error_item = element.parent('dd');
			error_item.append(error);
		},
		onkeyup: false,
		rules : {
			mobile : {
				required : true,
				mobile : true,
				remote   : {
					url : ncGlobal.webRoot + 'register/mobileexist',
					type:'get',
					data:{
						mobile : function(){
							return $('#mobile').val();
						}
					}
				}
			},
			captcha : {
				required : true,
				remote   : {
					url : ncGlobal.webRoot + 'captcha/check',
					type:'get',
					data:{
						captcha : function(){
							return $('#captchaMobile').val();
						}
					},
					complete: function(data) {
						if(data.responseText == 'false') {
							$("#codeimageMobile").attr("src", ncGlobal.webRoot+'captcha/getcaptcha?t=' + Math.random());
						}
					}
				}
			},
			agreeClause : {
				required : true
			}
		},
		messages : {
			mobile : {
				required : "<i class='icon-exclamation-sign'></i>手机号不能为空",
				mobile   : "<i class='icon-exclamation-sign'></i>请输入正确的手机号",
				remote	 : "<i class='icon-exclamation-sign'></i>手机号已存在"
			},
			captcha : {
				required : "<i class='icon-remove-circle' title='验证码不能为空'></i>",
				remote   : "<i class='icon-remove-circle' title='验证码错误'></i>"
			},
			agreeClause : {
				required : "<i class='icon-exclamation-sign'></i>请勾选服务协议"
			}
		}
	});

	$("#mobileFormSecond").validate({
		errorPlacement: function(error, element){
			var error_item = element.parent('dd');
			error_item.append(error);
		},
		onkeyup: false,
		rules : {
			authCode : {
				required : true,
				rangelength:[6,6]
			},
			memberPwd : {
				required : true,
				minlength: 6,
				maxlength: 20
			},
			repeatMemberPwd : {
				required : true,
				equalTo  : '#mobileMemberPwd'
			}
		},
		messages : {
			authCode : {
				required : "<i class='icon-exclamation-sign'></i>短信动态码不能为空",
				rangelength : "<i class='icon-exclamation-sign'></i>短信动态码错误",
			},
			memberPwd  : {
				required : "<i class='icon-exclamation-sign'></i>密码不能为空",
				minlength: "<i class='icon-exclamation-sign'></i>密码长度应在6-20个字符之间",
				maxlength: "<i class='icon-exclamation-sign'></i>密码长度应在6-20个字符之间"
			},
			repeatMemberPwd : {
				required : "<i class='icon-exclamation-sign'></i>请再次输入密码",
				equalTo  : "<i class='icon-exclamation-sign'></i>两次输入的密码不一致"
			}
		}
	});
});

function StepTimesMobile() {
	var form = $("#mobileFormSecond");
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