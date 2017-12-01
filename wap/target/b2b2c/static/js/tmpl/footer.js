$(function (){
    if (getQueryString('key') != '') {
        var key = getQueryString('key');
        var username = getQueryString('username');
        addCookie('key', key);
        addCookie('username', username);
    } else {
        var key = getCookie('key');
    }
    var html = '<div class="nctouch-footer-wrap posr">'
        +'<div class="nav-text">'
        +'<form id="loginForm" action=' + WapSiteUrl + '/login/popuplogin/>'
        +'<form id="registerForm" action=' + WapSiteUrl + '/register/popupRegister/>'       

/*    if(key){
        html += '<a id="memberBtn" href="javascript:void(0);">我的商城</a>'
            + '<a id="logoutbtn" href="javascript:void(0);">注销</a>'
            + '<a id="memberFeedBackBtn" href="javascript:void(0);">反馈</a>';
            
    } else {
        html += '<a id="loginBtn" href="javascript:void(0);">登录</a>'
            + '<a id="registerBtn" href="javascript:void(0);">注册</a>'
            + '<a id="loginBtn" href="javascript:void(0);">反馈</a>';
    }*/
    html += '<a href="javascript:void(0);" class="gotop">返回顶部</a>'
        +'</div>'
        +'<div class="nav-pic">'
			+'<a href="'+SiteUrl+'/index.php?act=mb_app" class="app"><span><i></i></span><p>客户端</p></a>'
			+'<a href="javascript:void(0);" class="touch"><span><i></i></span><p>触屏版</p></a>'
            +'<a href="'+SiteUrl+'" class="pc"><span><i></i></span><p>电脑版</p></a>'
         +'</div>'
		 +'<div class="copyright">'
		 +'<span  style="font-family:Verdana, Arial, Helvetica, sans-serif;font-size:0.7rem">©</span> 2016-2019 智慧仓 版权所有'
    	 +'</div>';
	$("#footer").html(html);
    var key = getCookie('key');
   
})
	
	//首页
	$('#page').click(function(){
		location.href=WapSiteUrl;

	});
	
	//我的
	$('#mine').click(function(){
			location.href=WapSiteUrl+'/members/index';
	});
	//商品详情
	$('.product_info').click(function(){
		var div=$(this).prev($("input[name=text]").val());
		var goodsId=div.context.childNodes[1].defaultValue;
		var key = getCookie('key');
		window.location.href= WapSiteUrl + '/product/productDetail?goodsId='+goodsId+'&key='+key;
		var a= WapSiteUrl + '/product/productDetail?goodsId='+goodsId+'&key='+key;
		
	});
	
	//详情页面的立即购买按钮跳转
	/*$('#buy-now').click(function(){
		
		var key = getCookie('key');
		if(key == null  || key == undefined || "NULL" == key.toUpperCase()){
	    	 key = ""
	     }
		if(!key){
			window.location.href= WapSiteUrl + '/login/popuplogin';
		}else{
			var saleNum=$('#goodsSaleNum').text();
			var buynum=$('#buynum').val();
			if(parseInt(saleNum)<parseInt(buynum)){
                alert("库存不足");
                return;
			}
			var goodsId=$('#goodsId').val();
			var goodsColorId=$('#goodsColorId').val();
			var buynum=$('#buynum').val();
			window.location.href= WapSiteUrl + '/product/goToBuy?goods_id='+goodsId +'&goodsColorId='+16+'&buynum='+buynum+'&key='+key;	
		}
	});*/
	
	$('.cart').click(function(){
		window.location.href= WapSiteUrl + '/cart/list'
	})
	
	
	
	
	
	
	
	
	