$(function(){
		var key = getCookie('key');
		if(!key){
			location.href = WapSiteUrl+'/login/popuplogin';
		}
		
		//点击删除地址
		$('.deladdress').click(function(){
		    var address_id = $(this).attr('address_id');
            $.sDialog({
                skin:"block",
                content:'确认删除吗？',
                okBtn:true,
                cancelBtn:true,
                okFn: function() {
                    delAddress(address_id);
                }
            });
		});
	
		
		//initPage();
		//点击删除地址
		function delAddress(address_id){
			$.ajax({
				type:'post',
				url:WapSiteUrl + '/address/del',
				data:{addressId:address_id},
				dataType:'json',
				success:function(result){
					if(result.code == 200){
						location.href = WapSiteUrl+'/address/index';
					}
				}
			});
		}
});
//我的订单
$('#header-nav').click(function(){
	window.location.href= WapSiteUrl + '/address/addAddress';	
});



