$(function(){
    //加载验证码
    loadSeccode();
    $("#refreshcode").bind('click',function(){
        loadSeccode();
    });
//    $('#register_mobile_password').click(function(){
//        if (!$(this).parent().hasClass('form-btn')) {
//            return false;
//        }
//        var captcha = $('#mobilecode').val();
//        if (captcha.length == 0) {
//            errorTipsShow('<p>请填写验证码<p>');
//        }
//        return false;
//        
//    });
});
// 发送手机验证码
$('#again').click(function(){
    var mobile = getQueryString("mobile");
    $.ajax({
    	type:'post',
        url:WapSiteUrl+'/register/mobilesecond/again',
        data:{mobile:mobile},
        async : false,
        dataType: 'json',
        success:function(result){
        	if(result.code == 200){
        		$.sDialog({
        			skin:"green",
        			content:'重新发送成功',
        			okBtn:false,
        			cancelBtn:false
        		});
        	}else{
        		$.sDialog({
        			skin:"green",
        			content:result.message,
        			okBtn:false,
        			cancelBtn:false
        		});
        	}
        }
    });
})


$('#register_mobile_password').click(function(){
    var mobile = getQueryString("mobile");
    var captcha = $('#captcha').val();
    var mobilecode = $('#mobilecode').val();
    $.ajax({
    	type:'post',
        url:WapSiteUrl+'/register/codeCheck',
        data:{mobile:mobile,captcha:captcha,mobilecode:mobilecode},
        async : false,
        dataType: 'json',
        success:function(result){
        	if(result.code == 200){
        		location.href = WapSiteUrl + '/register/modsetPwd?mobile=' + mobile;
        	}else{
        		$.sDialog({
        			skin:"green",
        			content:result.message,
        			okBtn:false,
        			cancelBtn:false
        		});
        	}
        }
    });
})
