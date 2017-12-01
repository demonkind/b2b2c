$(function() {
    var key = getCookie('key');
    if (!key) {
        window.location.href = WapSiteUrl+'/login/popuplogin';
        return;
    }
  
    $.ajax({
        type:'get',
        url:ApiUrl+"/index.php?act=member_account&op=get_mobile_info",
        data:{key:key},
        dataType:'json',
        success:function(result){
            if(result.code == 200){
            	if (result.datas.state) {
            		$('#mobile_link').attr('href','member_mobile_modify.html');
            		$('#mobile_value').html(result.datas.mobile);
            	}
            }else{
            }
        }
    });
    $.ajax({
        type:'get',
        url:ApiUrl+"/index.php?act=member_account&op=get_paypwd_info",
        data:{key:key},
        dataType:'json',
        success:function(result){
            if(result.code == 200){
            	if (!result.datas.state) {
            		$('#paypwd_tips').html('未设置');
            	}
            }else{
            }
        }
    });
});

//注销
$('#logoutbtn').click(function(){
	var username = getCookie('username');
	var key = getCookie('key');
	var client = 'wap';
	$.ajax({
		type:'get',
		url:WapSiteUrl+'/login/logout',
		data:{username:username,key:key,client:client},
		success:function(result){
			if(result){
				delCookie('username');
				delCookie('key');
				location.href = WapSiteUrl;
			}
		}
	});
});