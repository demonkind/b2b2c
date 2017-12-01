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
    
	$('#find_password_btn').click(function(){
        if (!$(this).parent().hasClass('form-btn')) {
            return false;
        }
	    if ($.sValid()) {
	    	
	    	$.ajax({
	            type:'post',
	            url:WapSiteUrl+'/findpwd/mobile',
	            data:{'mobile':$('#usermobile').val(),'captcha':$('#captcha').val()},
	            async : false,
	            dataType: 'json',
	            success:function(result){
	            	if(result.code != 200){
	            		errorTipsShow(result.message);
	            		//判断result中的回调数据是true还是false
	            	}else{
	            		window.location.href=WapSiteUrl+'/findpwd/findPasswordPassword?mobile=' + $('#usermobile').val() + '&memberId='+ result.data.memberId;
	            	}
	            }
	    	});
//	        $(this).attr('href', WapSiteUrl+'/findpwd/mobile?mobile=' + $('#usermobile').val() + '&captcha=' + $('#captcha').val() + '&codekey=' + $('#codekey').val());
	    } else {
	        return false;
	    }
	});
});