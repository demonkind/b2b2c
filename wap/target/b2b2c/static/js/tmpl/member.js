$(function(){
    if (getQueryString('key') != '') {
        var key = getQueryString('key');
        var username = getQueryString('username');
        addCookie('key', key);
        addCookie('username', username);
        updateCookieCart(key);
    } else {
        var key = getCookie('key');
    }
    
	  //滚动header固定到顶部
	  $.scrollTransparent();
});
	$(document).on("click","#btnLogin",function(){
		location.href=WapSiteUrl + '/login/popuplogin';
	})
	

	// 登陆
	$('#loginBtn').click(function(){
		location.href=WapSiteUrl+'/login/popuplogin';
	});
	
	// 注册
	$('#registerBtn').click(function(){
		location.href=WapSiteUrl+'/register/popupRegister';
	});
	
	//我的订单
	$('#order').click(function(){
		window.location.href= WapSiteUrl + '/orders/list';	
	});
	
	//收货地址管理
	$('#address_adm').click(function(){
		window.location.href= WapSiteUrl + '/address/index';	
	});
	
	
	//用户设置
	$('#member_account').click(function(){
		location.href=WapSiteUrl + '/security/index';
	});
	