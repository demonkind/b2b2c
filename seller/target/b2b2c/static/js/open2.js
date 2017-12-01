var v;
(function() {
  
    // 初始选项卡的值
    var current = 0;
    //获取验证实例
    v = $("#joinForm").validate({
        errorClass: "error",  
        submitHandler: function (form) {
            $(form).ajaxSubmit();
        },
        rules: {
            payingCertificate: {
                required: true,
                rangelength:[2,50]
            },
            payingCertificateExp: {
                maxlength: 100
            }
        },
        messages: {
            payingCertificate: {
                required: "请上传付款凭证"
            }
        }
    });

    $("#btnSubmit").click(function () {
        $("#joinForm").submit();
    });


    //营业执照电子版上传
    $("#btnUploadPayingCertificate").fileupload({
        dataType: 'json',
        url: ncGlobal.sellerRoot + "image/upload.json",
        formData: {type:ncGlobal.filesType.setting},
        done: function (e, data) {
            if (data.result.code == 200) {
                console.log(data.result.data.name);
                console.log(data.result.data.url);
                var _img = $('<a href="' + data.result.data.url + '" data-lightbox="img-PayingCertificate" data-item="image" class="upload-file-thumb" title="付款凭证"><img src="'+data.result.data.url+'" alt=""></a>');
                $("#divUploadPayingCertificate").nextAll().remove("[data-item='image']");
                $("#divUploadPayingCertificate").after(_img);
                $("#payingCertificate").val(data.result.data.name);
                v.element("#payingCertificate");
				//图片同比例缩放-默认
                _img.find("img").jqthumb({
					width: 120,
					height: 120,
					after: function (imgObj) {
					imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
					}
				});
            } else {
                Nc.alertError("文件上传失败");
            }
        }
    });
	//上传证照缩略
	$('.upload-file-thumb img').jqthumb({
		width: 120,
		height: 120,
		after: function (imgObj) {
		imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
		}
	});

    //左侧开店进度高亮
    $('dt[dataType="leftStep"]').removeClass("current");
    $("#leftStep5").addClass("current");
})();

