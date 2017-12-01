$(function() {
    $("#formSubmit").on("click", function() {
        $("#settingForm").submit();
    });

    $("#settingForm").validate({
        submitHandler: function(form) {
            $(form).ajaxSubmit();
        },
        onkeyup: false,
        rules:{
            companyName:{
               maxlength: 20
            },
            phone:{
               maxlength: 20
            },
            storeZy:{
                maxlength: 50
            },
            storeQq:{
                maxlength: 20
            },
            storeWw:{
                maxlength: 20
            },
            storeWw:{
                maxlength: 20
            },
            storePhone:{
                maxlength: 20
            },
            storeSeoKeyword:{
                maxlength: 50
            },
            storeSeoDescription:{
                maxlength: 120
            }
        }
    });

    //地区选择
    $("#divCompanyArea").NcArea({
        showDeep: 1,
        hiddenInput: [
        {
            id: "companyAreaId",
            name: "companyAreaId",
            value: "0"
        },
        {
            id: "companyArea",
            name: "companyArea",
            value: ""
        }]
    });

	//卖家修改资料上传头像图片缩略
	$('.seller-avatar-thumb img').jqthumb({
		width: 60,
		height: 60,
		after: function (imgObj) {
		    imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
		}
	});

    /**
     * 店铺Logo上传
     */
	$('#divImgStoreLogo img').jqthumb({
		width: 200,
		height: 60,
		after: function (imgObj) {
		imgObj.css('opacity', 0).animate({opacity: 1}, 500);
		}
	});
    $("#fileStoreLogo").fileupload({
        dataType: 'json',
        url: ncGlobal.sellerRoot + "image/upload.json",
        formData: {
		    type: ncGlobal.filesType.setting
		},
        done: function (e, data) {
            if (data.result.code == 200) {
                $('#storeLogo').val(data.result.data.name);
                $('#imgStoreLogo').attr('src', data.result.data.url);
                $('#divImgStoreLogo img').jqthumb({
                    width: 200,
                    height: 60,
                    after: function (imgObj) {
                        imgObj.css('opacity', 0).animate({opacity: 1}, 500);
                    }
                });
            } else {
                Nc.alertError(data.result.message);
            }
        }
    });

    /**
     * 店铺条幅上传
     */
	$('#divImgStoreBanner img').jqthumb({
		width: 672,
		height: 70,
		after: function (imgObj) {
		imgObj.css('opacity', 0).animate({opacity: 1}, 500);
		}
	});
    $("#fileStoreBanner").fileupload({
        dataType: 'json',
        url: ncGlobal.sellerRoot + "image/upload.json",
        formData: {
		    type: ncGlobal.filesType.setting
		},
        done: function (e, data) {
            if (data.result.code == 200) {
                $('#storeBanner').val(data.result.data.name);
                $('#imgStoreBanner').attr('src', data.result.data.url);
                $('#divImgStoreBanner img').jqthumb({
                    width: 672,
                    height: 70,
                    after: function (imgObj) {
                        imgObj.css('opacity', 0).animate({opacity: 1}, 500);
                    }
                });
            } else {
                Nc.alertError(data.result.message);
            }
        }
    });

    /**
     * 店铺头像上传
     */
	$('#divImgStoreAvatar img').jqthumb({
    	width: 60,
		height: 60,
		after: function (imgObj) {
			imgObj.css('opacity', 0).animate({opacity: 1}, 500);
		}
	});
    $("#fileStoreAvatar").fileupload({
        dataType: 'json',
        url: ncGlobal.sellerRoot + "image/upload.json",
        formData: {
		    type: ncGlobal.filesType.setting
		},
        done: function (e, data) {
            if (data.result.code == 200) {
                $('#storeAvatar').val(data.result.data.name);
                $('#imgStoreAvatar').attr('src', data.result.data.url);
                $('#divImgStoreAvatar img').jqthumb({
                    width: 60,
                    height: 60,
                    after: function (imgObj) {
                        imgObj.css('opacity', 0).animate({opacity: 1}, 500);
                    }
                });
            } else {
                Nc.alertError(data.result.message);
            }
        }
    });
});
