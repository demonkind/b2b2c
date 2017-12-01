$(function(){
    var key = getCookie('key');
    // TODO comment by yfb  暂时没有第3方登陆，这个source 暂时注释掉 start  ===================================
//    $.getJSON(ApiUrl + '/index.php?act=connect&op=get_state', function(result){
//        var ua = navigator.userAgent.toLowerCase();
//        var allow_login = 0;
//        if (result.data.pc_qq == '1') {
//            allow_login = 1;
//            $('.qq').parent().show();
//        }
//        if (result.data.pc_sn == '1') {
//            allow_login = 1;
//            $('.weibo').parent().show();
//        }
//        if ((ua.indexOf('micromessenger') > -1) && result.data.connect_wap_wx == '1') {
//            allow_login = 1;
//            $('.wx').parent().show();
//        }
//        if (allow_login) {
//            $('.joint-login').show();
//        }
//    });
 // TODO comment by yfb  暂时没有第3方登陆，这个source 暂时注释掉 end  ===================================
    
	var referurl = document.referrer;//上级网址
	$.sValid.init({
        rules:{
            username:"required",
            userpwd:"required"
        },
        messages:{
            username:"用户名必须填写！",
            userpwd:"密码必填!"
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
    var allow_submit = true;
	$('#loginbtn').click(function(){//会员登陆
        if (!$(this).parent().hasClass('form-btn')) {
            return false;
        }
        if (allow_submit) {
            allow_submit = false;
        } else {
            return false;
        }
		var username = $('#username').val();
		var pwd = $('#userpwd').val();
		var client = 'wap';
		if($.sValid()){
	          $.ajax({
				type:'post',
				url: WapSiteUrl+"/login/common",	
				data:{username:username,password:pwd,client:client,autoLogin:0},
				dataType:'json',
				success:function(result){
					
				    allow_submit = true;
					if(result.code == 200){
						if(typeof(result.data.key)=='undefined'){
							return false;
						}else{
						    var expireHours = 0;
						    if ($('#checkbox').prop('checked')) {
						        expireHours = 188;
						    }
						    // 更新cookie购物车
						    updateCookieCart(result.data.key);
							addCookie('username',result.data.username, expireHours);
							addCookie('key',result.data.key, expireHours);
							var url=getCookie('url');
							delCookie('url');
							if(url){
								location.href = url;
							}else{
								location.href = WapSiteUrl+'/members/index';
							}
						}
		                errorTipsHide();
					}else{
		                errorTipsShow('<p>' + result.message + '</p>');
					}
				},
				error: function(request) {
                    alert("Connection error");
                }
			 });  
        }
	});
	
	$('#updata_pwd').click(function(){
	    location.href =  WapSiteUrl+'/findpwd';
	})
	
	$('.weibo').click(function(){
	    location.href = ApiUrl+'/index.php?act=connect&op=get_sina_oauth2';
	})
    $('.qq').click(function(){
        location.href = ApiUrl+'/index.php?act=connect&op=get_qq_oauth2';
    })
    $('.wx').click(function(){
        location.href = ApiUrl+'/index.php?act=connect&op=index';
    })
});