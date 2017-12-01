$(function(){
    //加载验证码
    loadSeccode();
    $("#refreshcode").bind('click',function(){
        loadSeccode();
    });
    $.sValid.init({//注册验证
        rules:{
            usermobile:{
                required:true,
                mobile:true
            }
        },
        messages:{
            usermobile:{
                required:"请填写手机号！",
                mobile:"手机号码不正确"
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
    
    
    $('#refister_mobile_btn').click(function(){
        if (!$(this).parent().hasClass('form-btn')) {
            return false;
        }
        var mobile = $('#usermobile').val();
        var captcha = $('#captcha').val();
        var agreeClause = $('#agreeClause').is(':checked');
    	$.ajax({
            type:'post',
            url:WapSiteUrl+'/register/mobilefirst',
            data:{mobile:mobile,captcha:captcha,agreeClause:agreeClause=true?true:false},
            async : false,
            dataType: 'json',
            success:function(result){
            	if(result.code != 200){
            		errorTipsShow(result.message);
            		//判断result中的回调数据是true还是false
            	}else{
            		window.location.href=WapSiteUrl+'/register/mobileNest?mobile='+mobile;
            	}
            }
    	});
	});
});