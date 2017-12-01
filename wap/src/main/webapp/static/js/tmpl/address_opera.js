$(function(){
    var key = getCookie('key');
	$.sValid.init({
		rules:{
			true_name:"required",
			mob_phone:"required",
			area_info:"required",
			address:"required"
		},
		messages:{
			true_name:"姓名必填！",
			mob_phone:"手机号必填！",
			area_info:"地区必填！",
			address:"街道必填！"
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
	$('#header-nav').click(function(){
		$('#save_address').click();
	});
	$('#save_address').click(function(){
		if($.sValid()){
			var true_name = $('#true_name').val();
			var mob_phone = $('#mob_phone').val();
			var area_info = $('#area_info').val();
			var area_id = $('#area_info').attr('data-areaid');
			var city_id = $('#area_info').attr('data-areaid2');
			var address = $('#address').val();
			var is_default = $('#is_default').attr("checked") ? 1 : 0;
			var myreg = /^[1]+[3,5,8]+\d{9}$/;
			if (!myreg.test(mob_phone)) {
				errorTipsShow("手机号格式不正确");
				return false;
			}
			$.ajax({
				type:'post',
				url:WapSiteUrl + '/address/add',	
				data:{
					key:key,
					trueName:true_name,
				    mobPhone:mob_phone,
				    areaId:area_id,
				    cityId:city_id,
				    address:address,
				    areaInfo:area_info,
				    isDefault:is_default
				},
				dataType:'json',
				success:function(result){
					if(result.code == 200){
						location.href = WapSiteUrl+'/address/index';
					}else{
						errorTipsShow(result.message);
					}
				}
			});
		}
	});

    // 选择地区
    $('#area_info').on('click', function(){
        $.areaSelected({
            success : function(data){
                $('#area_info').val(data.area_info).attr({'data-areaid':data.area_id, 'data-areaid2':(data.area_id_2 == 0 ? data.area_id_1 : data.area_id_2)});
            }
        });
    });
});