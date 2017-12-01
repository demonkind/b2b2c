var submiting = false;

$(function() {
	//注册方式切换
	$('#findpwdModule').tabulous({
		effect: 'slideLeft'
	});
	//切换tab刷新验证码
	$("#findpwdModule .tabs-nav li a").click(function(){
		$("#mobileModule").show();
		var div_form = $(this).attr('href');
		$(div_form).find("[nc_type='changeCaptcha']").trigger("click");
		$("#findFormFirst").show();
		$("#findFormSecond").hide();
		$("#mobileFormFirst").show();
		$("#mobileFormSecond").hide();
		return false;
	});

	//邮箱找回密码第一步
	$("#findFormFirst").on("nc.ajaxSubmit.error",function(e, result){
		Nc.alertError(result);
	});
	$("#findFormFirst").on("nc.ajaxSubmit.success",function(e, xhr){
		$("#findFormFirst").hide();
		$("#findFormSecond").show();
		var email = $("#findFormFirst").find("[id='email']").val();
		$("#emailSecond").val(email);
		$("#authCode").val(xhr.data.authCode);
		$("#memberIdSecond").val(xhr.data.memberId);
		var emailStr = email.replace(/(.{3})(.?)@(.)/, "$1****@$3");
		$("#emailmsg").html("邮件已发送至"+ emailStr +"，请在"+xhr.data.authCodeValidTime+"分钟内完成验证。");
		$("#findFormSecond").find("[ncType='sendAuthCode']").hide();
		$("#findFormSecond").find("[ncType='sendAuthCodeTimes']").html(xhr.data.authCodeResendTime);
		$("#findFormSecond").find("[ncType='sendAuthCodeAgain']").show();
		setTimeout("StepTimesEmail()", 1000);
	});
	$("#findSubmitFirst").click(function(){
		if($("#findFormFirst").valid()){
			$("#findFormFirst").ajaxSubmit();
		}
		return false;
	});
	//邮箱找回密码重新发送动态码
	$("#findFormSecond").find("[ncType='sendAuthCode']").click(function () {
		//加载中
		if (submiting == true) {
			return false;
		}
		submiting = true;
		var params = {"memberName":$('#memberName').val(),"email":$('#email').val()};
		$.ajax({
			type : "post",
			url : ncGlobal.webRoot + "findpwd/email/second/again",
			data : params,
			async : false,
			success : function(xhr){
				if(xhr.code == 200) {
					$("#findFormSecond").find("[ncType='sendAuthCode']").hide();
					$("#findFormSecond").find("[ncType='sendAuthCodeTimes']").html(xhr.data.authCodeResendTime);
					$("#findFormSecond").find("[ncType='sendAuthCodeAgain']").show();
					setTimeout("StepTimesEmail()", 1000);
				} else {
					Nc.alertError(xhr.message);
				}
				submiting = false;
			}
		});
		return false;
	});

	//邮箱找回密码第二步
	$("#findFormSecond").on("nc.ajaxSubmit.success",function(e, result){
		window.location.href = ncGlobal.webRoot+"login";
	});
	$("#findFormSecond").on("nc.ajaxSubmit.error",function(e, result){
		Nc.alertError(result);
	});
	$("#findSubmitSecond").click(function(){
		if($("#findFormSecond").valid()){
			$("#findFormSecond").ajaxSubmit();
		}
		return false;
	});
	//邮箱找回密码第一步验证
	$("#findFormFirst").validate({
		errorPlacement: function(error, element){
			var error_item = element.parent('dd');
			error_item.append(error);
		},
		onkeyup: false,
		rules : {
			memberName : {
				required : true
			},
			email : {
				required : true,
				email    : true
			},
			captcha : {
				required : true,
				remote   : {
					url : ncGlobal.webRoot + 'captcha/check',
					type:'get',
					data:{
						captcha : function(){
							return $('#findCaptcha').val();
						}
					},
					complete: function(data) {
						if(data.responseText == 'false') {
							$("#findCodeimage").attr("src", ncGlobal.webRoot+'captcha/getcaptcha?t=' + Math.random());
						}
					}
				}
			}
		},
		messages : {
			memberName : {
				required : "<i class='icon-exclamation-sign'></i>登录账号不能为空"
			},
			email : {
				required : "<i class='icon-exclamation-sign'></i>邮箱不能为空",
				email   : "<i class='icon-exclamation-sign'></i>邮箱格式错误"
			},
			captcha : {
				required : "<i class='icon-remove-circle' title='验证码不能为空'></i>",
				remote   : "<i class='icon-remove-circle' title='验证码错误'></i>"
			}
		}
	});
	//邮箱找回密码第二步验证
	$("#findFormSecond").validate({
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
				equalTo  : '#memberPwd'
			}
		},
		messages : {
			authCode : {
				required : "<i class='icon-exclamation-sign'></i>动态码不能为空</i>",
				rangelength : "<i class='icon-exclamation-sign'></i>动态码错误</i>"
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

	//手机找回密码第一步
	$("#mobileFormFirst").on("nc.ajaxSubmit.success",function(e, xhr){
		$("#mobileFormFirst").hide();
		$("#mobileFormSecond").show();
		var mobile = $("#mobileFormFirst").find("[id='mobile']").val();
		$("#mobileSecond").val(mobile);
		//-----------------------------可以去除---------------------------------
		$("#mobileAuthCode").val(xhr.data.authCode);
		$("#mobileMemberIdSecond").val(xhr.data.memberId);
		var mobileStr = mobile.replace(/([0-9]{3})([0-9]{6})([0-9]{2})/, "$1******$3");
		$("#mobileMsg").html("短信已发送至"+ mobileStr +"，请在"+xhr.data.authCodeValidTime+"分钟内完成验证。");
		$("#mobileFormSecond").find("[ncType='sendAuthCode']").hide();
		$("#mobileFormSecond").find("[ncType='sendAuthCodeTimes']").html(xhr.data.authCodeResendTime);
		$("#mobileFormSecond").find("[ncType='sendAuthCodeAgain']").show();
		setTimeout("StepTimesMobile()", 1000);
	});
	$("#mobileFormFirst").on("nc.ajaxSubmit.error",function(e, result){
		Nc.alertError(result);
	});
	$("#mobileSubmitFirst").click(function(){
		if($("#mobileFormFirst").valid()){
			$("#mobileFormFirst").ajaxSubmit();
		}
		return false;
	});
	//手机找回密码重新发送动态码
	$("#mobileFormSecond").find("[ncType='sendAuthCode']").click(function () {
		//加载中
		if (submiting == true) {
			return false;
		}
		submiting = true;
		var params = {"mobile":$('#mobile').val()};
		$.ajax({
			type : "post",
			url : ncGlobal.webRoot + "findpwd/mobile/second/again",
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
	//手机找回密码第二步
	$("#mobileFormSecond").on("nc.ajaxSubmit.success",function(e, result){
		window.location.href = ncGlobal.webRoot+"login";
	});
	$("#mobileFormSecond").on("nc.ajaxSubmit.error",function(e, result){
		Nc.alertError(result);
	});
	$("#mobileSubmitSecond").click(function(){
		if($("#mobileFormSecond").valid()){
			$("#mobileFormSecond").ajaxSubmit();
		}
		return false;
	});
	//手机找回密码第一步验证
	$("#mobileFormFirst").validate({
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
			}
		},
		messages : {
			mobile : {
				required : "<i class='icon-exclamation-sign'></i>手机号不能为空",
				mobile   : "<i class='icon-exclamation-sign'></i>手机号格式错误"
			},
			captcha : {
				required : "<i class='icon-remove-circle' title='验证码不能为空'></i>",
				remote   : "<i class='icon-remove-circle' title='验证码错误'></i>"
			}
		}
	});
	//手机找回密码第二步验证
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
				required : "<i class='icon-exclamation-sign'></i>动态码不能为空",
				rangelength : "<i class='icon-exclamation-sign'></i>动态码错误"
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

function StepTimesEmail() {
	var form = $("#findFormSecond");
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