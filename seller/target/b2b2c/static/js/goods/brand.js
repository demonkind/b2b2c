/**
 * Created by shopnc.feng on 2016-01-25.
 */
 //列表品牌缩略图
$('.ncs-brand-list-thumb img').jqthumb({
		width: 90,
		height: 30,
		after: function (imgObj) {
		imgObj.css('opacity', 0).animate({opacity: 1}, 500);
		}
	});
 
var brand = function () {
    var _addBrand = function(){
        Nc.layerOpen({
            title: "新增品牌",
            content: $('#addModal'),
            $form:$("#addForm")
        })
    }

    var _editBrand = function(id) {
        $.post(ncGlobal.sellerRoot + 'brand/edit.json',
            {brandId: id},
            function(data){
                if (data.code == 200) {
                    var brand = data.data.brandAndLabel;
                    var brandApply = data.data.brandApply;
                    $('#brandId').val(brand.brandId);
                    $('#brandName').val(brand.brandName);
                    $('#brandEnglish').val(brand.brandEnglish);
                    $('#editImgBrand').attr('src', brand.brandImageSrc);
                    $('#editBrandImageInput').val(brand.brandImage);

                    $('#registerNumber').val(brandApply.registerNumber);
                    $('#editImage1').attr('src', brandApply.image1Src);
                    $('#editImage1Input').val(brandApply.image1);
                    $('#editImage2').attr('src', brandApply.image2Src);
                    $('#editImage2Input').val(brandApply.image2);
                    $('#owner').val(brandApply.owner);
                    if (brand.brandLabelIds.length > 0) {
                        var brandLabelIds = brand.brandLabelIds.split(",");
                        for (i in brandLabelIds) {
                            $('#editForm').find('input[value="' + brandLabelIds[i] + '"]').prop("checked", true);
                        }
                    }
                    $('#editImgBrand').jqthumb({
                        width: 150,
                        height: 50,
                        after: function (imgObj) {
                            imgObj.css('opacity', 0).animate({opacity: 1}, 500);
                        }
                    });

                    $('#editImage1,#editImage2').jqthumb({
                        width: 110,
                        height: 150,
                        after: function (imgObj) {
                            imgObj.css('opacity', 0).animate({opacity: 1}, 500);
                        }
                    });
                    Nc.layerOpen({
                        title: "编辑品牌",
                        content: $('#editModal'),
                        $form:$("#editForm")
                    });
                    // 审核失败原因
                    if (brand.applyState == 10) {
                        $('#stateRemark').html(brandApply.stateRemark).parents("dl:first").show();
                    } else {
                        $('#stateRemark').html('').parents("dl:first").hide();
                    }
                } else {
                    Nc.alertError(data.message);
                }
            }, 'json');
    }

    var _deleteBrand = function(brandId) {
        Nc.layerConfirm("是否进行删除", {
            postUrl: ncGlobal.sellerRoot + 'brand/delete.json',
            postData: {brandId: brandId}
        });
    }

    var _init = function() {
        //通用验证设置
        var validateOptions = {
            invalidHandler: function (form, validator) {
                $(form).find('.alert-error').show();
            },
            submitHandler: function (form) {
                $(form).ajaxSubmit();
            },
            rules: {
                brandName: {
                    required: true
                },
                registerNumber: {
                    required: true
                },
                owner: {
                    required: true
                }
            },
            messages: {
                brandName: {
                    required: '<i class="icon-exclamation-sign"></i>请填写品牌中文名称'
                },
                registerNumber: {
                    required: '<i class="icon-exclamation-sign"></i>请填写品牌注册号/申请号'
                },
                owner: {
                    required: '<i class="icon-exclamation-sign"></i>请填写品牌所有人'
                }
            }
        }
        //添加失败处理
        $("#addForm,#editForm")
            .on("nc.ajaxSubmit.error", function (e, errorMessage) {
                Nc.alertError(errorMessage);
            })
        $('#addForm').validate(validateOptions);
        $('#editForm').validate(validateOptions);

        /**
         * 新增品牌图片上传
         */
        $("#addFileBrandImage").fileupload({
            dataType: 'json',
            url: ncGlobal.sellerRoot + "image/upload.json",
            formData: {
                type: ncGlobal.filesType.setting
            },
            done: function (e, data) {
                if (data.result.code == 200) {
                    $('#addBrandImageInput').val(data.result.data.name);
                    $('#addImgBrand').attr('src', data.result.data.url);
                    $('#addDivBrandImage img').jqthumb({
                        width: 150,
                        height: 50,
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
         * 注册证/受理书1
         */
        $("#addFileImage1").fileupload({
            dataType: 'json',
            url: ncGlobal.sellerRoot + "image/upload.json",
            formData: {
                type: ncGlobal.filesType.setting
            },
            done: function (e, data) {
                if (data.result.code == 200) {
                    $('#addImage1Input').val(data.result.data.name);
                    $('#addImage1').attr('src', data.result.data.url);
                    $('#addDivImage1 img').jqthumb({
                        width: 110,
                        height: 150,
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
         * 注册证/受理书1
         */
        $("#addFileImage1").fileupload({
            dataType: 'json',
            url: ncGlobal.sellerRoot + "image/upload.json",
            formData: {
                type: ncGlobal.filesType.setting
            },
            done: function (e, data) {
                if (data.result.code == 200) {
                    $('#addImage1Input').val(data.result.data.name);
                    $('#addImage1').attr('src', data.result.data.url);
                    $('#addDivImage1 img').jqthumb({
                        width: 110,
                        height: 150,
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
         * 注册证/受理书2
         */
        $("#addFileImage2").fileupload({
            dataType: 'json',
            url: ncGlobal.sellerRoot + "image/upload.json",
            formData: {
                type: ncGlobal.filesType.setting
            },
            done: function (e, data) {
                if (data.result.code == 200) {
                    $('#addImage2Input').val(data.result.data.name);
                    $('#addImage2').attr('src', data.result.data.url);
                    $('#addDivImage2 img').jqthumb({
                        width: 110,
                        height: 150,
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
         * 编辑注册证/受理书1
         */
        $("#editFileImage1").fileupload({
            dataType: 'json',
            url: ncGlobal.sellerRoot + "image/upload.json",
            formData: {
                type: ncGlobal.filesType.setting
            },
            done: function (e, data) {
                if (data.result.code == 200) {
                    $('#editImage1Input').val(data.result.data.name);
                    $('#editImage1').attr('src', data.result.data.url);
                    $('#editDivImage1 img').jqthumb({
                        width: 110,
                        height: 150,
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
         * 编辑注册证/受理书2
         */
        $("#editFileImage2").fileupload({
            dataType: 'json',
            url: ncGlobal.sellerRoot + "image/upload.json",
            formData: {
                type: ncGlobal.filesType.setting
            },
            done: function (e, data) {
                if (data.result.code == 200) {
                    $('#editImage2Input').val(data.result.data.name);
                    $('#editImage2').attr('src', data.result.data.url);
                    $('#editDivImage2 img').jqthumb({
                        width: 110,
                        height: 150,
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
         * 编辑品牌图片上传
         */
        $("#editFileBrandImage").fileupload({
            dataType: 'json',
            url: ncGlobal.sellerRoot + "image/upload.json",
            formData: {
                type: ncGlobal.filesType.setting
            },
            done: function (e, data) {
                if (data.result.code == 200) {
                    $('#editBrandImageInput').val(data.result.data.name);
                    $('#editImgBrand').attr('src', data.result.data.url);
                    $('#editDivBrandImage img').jqthumb({
                        width: 150,
                        height: 50,
                        after: function (imgObj) {
                            imgObj.css('opacity', 0).animate({opacity: 1}, 500);
                        }
                    });
                } else {
                    Nc.alertError(data.result.message);
                }
            }
        });
    }
    return {
        init:_init,
        addBrand:_addBrand,
        editBrand:_editBrand,
        deleteBrand:_deleteBrand
    }
}();
$(function(){
    brand.init();
})