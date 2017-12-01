$(function(){
    var mobile = getQueryString("mobile");
    var memberId = getQueryString("memberId");
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
        var repeatMemberPwd = $("#repeatMemberPwd").val();
        var mobilecode = $("#mobilecode").val();
        
        if($.sValid()){
            $.ajax({
                type:'post',
                url: WapSiteUrl +"/findpwd/mobile/second",  
                data:{memberId:memberId, mobile:mobile, authCode:mobilecode, memberPwd:password, repeatMemberPwd:repeatMemberPwd, client:'wap'},
                dataType:'json',
                success:function(result){
                    if(result.code == 200){
                    	//TODO
                        addCookie('memberId',memberId);
//                        addCookie('key',result.data.key);
                        location.href = WapSiteUrl + '/members/index';
                    }else{
                        errorTipsShow("<p>"+result.datas.error+"</p>");
                    }
                }
            });         
        }
    });
});
