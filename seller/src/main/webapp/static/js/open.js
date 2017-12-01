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
        errorPlacement: function(error, element) {
            var inputName = element.attr("name");
            if (inputName == "companyAddress" || inputName == "bankAddress"  || inputName == "settlementBankAddress" ){
                error.appendTo(element.closest("dd"));
            }else{
                error.appendTo(element.parent());
            }

        },
        rules: {
            //组1 里面的验证
            companyName: {
                required: true,
                rangelength:[2,50]
            },
            companyAddress: {
                required: true
            },
            companyAddressDetail: {
                required: true,
            rangelength:[2,50]
            },
            companyPhone: {
                required: true,
            rangelength:[2,20]
            },
            companyEmployeeCount: {
                required: true,
                digits: true
            },
            companyRegisteredCapital: {
                required: true,
                number: true
            },
            contactsName: {
                required: true,
                rangelength:[2,50]
            },
            contactsPhone: {
                required: true,
                rangelength:[2,20]
            },
            contactsEmail: {
                required: true,
                email: true
            },
            businessLicenceNumber: {
                required: true,
                rangelength:[2,50]
            },
            businessSphere: {
                required: true
            },
            businessLicenceImage: {
                required: true
            },
            organizationCode: {
                required: true,
                rangelength:[2,50]
            },
            organizationImage: {
                required: true
            },
            generalTaxpayer: {
                required: true
            },
            bankAccountName: {
                required: true,
                rangelength:[2,50]
            },
            bankAccountNumber: {
                required: true,
                rangelength:[2,50]
            },
            bankName: {
                required: true,
                rangelength:[2,50]
            },
            bankCode: {
                required: true,
                rangelength:[2,50]
            },
            bankAddress: {
                required: true
            },
            bankLicenceImage: {
                required: true
            },
            settlementBankAccountName: {
                required: true,
                rangelength:[2,50]
            },
            settlementBankAccountNumber: {
                required: true,
                rangelength:[2,50]
            },
            settlementBankName: {
                required: true,
                rangelength:[2,50]
            },
            settlementBankCode: {
                required: true,
                rangelength:[2,50]
            },
            settlementBankAddress: {
                required: true
            },
            taxRegistrationCertificate: {
                required: true,
                rangelength:[2,50]
            },
            taxpayerId: {
                required: true,
                rangelength:[2,50]
            },
            taxRegistrationImage: {
                required: true
            },
            storeName: {
                required: true,
                rangelength:[2,50],
                remote   : {
                    url :ncGlobal.sellerRoot +'open/is_store_name_exist',
                    type:'get',
                    data:{
                        storeName: function() {
                                return $('#storeName').val();
                            }
                        }
                    }
            },
            storeGrade: {
                required: true
            },
            storeClass: {
                required: true
            },
            bindCategory: {
                required: true
            }
        },
        messages: {
            companyName:{
                required: '<i class="icon-exclamation-sign"></i>公司名称不能为空'
            },
            companyAddress: {
                required: '<i class="icon-exclamation-sign"></i>请选择公司所在地'
            },
            companyAddressDetail: {
                required: '<i class="icon-exclamation-sign"></i>公司详细地址不能为空'
            },
            companyPhone: {
                required: '<i class="icon-exclamation-sign"></i>公司电话不能为空'
            },
            companyEmployeeCount: {
                required: '<i class="icon-exclamation-sign"></i>公司员工总数不能为空'
            },
            companyRegisteredCapital: {
                required: '<i class="icon-exclamation-sign"></i>注册资金不能为空'
            },
            contactsName: {
                required: '<i class="icon-exclamation-sign"></i>联系人姓名不能为空'
            },
            contactsPhone: {
                required: '<i class="icon-exclamation-sign"></i>联系人电话不能为空'
            },
            contactsEmail: {
                required: '<i class="icon-exclamation-sign"></i>电子邮箱不能为空'
            },
            businessLicenceNumber: {
                required: '<i class="icon-exclamation-sign"></i>营业执照号不能为空'
            },
            businessSphere: {
                required: '<i class="icon-exclamation-sign"></i>法定经营范围不能为空'
            },
            businessLicenceImage: {
                required: '<i class="icon-exclamation-sign"></i>营业执照电子版不能为空'
            },
            organizationCode: {
                required: '<i class="icon-exclamation-sign"></i>组织机构代码不能为空'
            },
            organizationImage: {
                required: '<i class="icon-exclamation-sign"></i>组织机构代码证电子版不能为空'
            },
            generalTaxpayer: {
                required: '<i class="icon-exclamation-sign"></i>一般纳税人证明不能为空'
            },
            bankAccountName: {
                required: '<i class="icon-exclamation-sign"></i>银行开户名不能为空'
            },
            bankAccountNumber: {
                required: '<i class="icon-exclamation-sign"></i>银行账号不能为空'
            },
            bankName: {
                required: '<i class="icon-exclamation-sign"></i>开户银行支行名称不能为空'
            },
            bankCode: {
                required: '<i class="icon-exclamation-sign"></i>支行联行号不能为空'
            },
            bankAddress: {
                required: '<i class="icon-exclamation-sign"></i>开户银行所在地不能为空'
            },
            bankLicenceImage: {
                required: '<i class="icon-exclamation-sign"></i>开户银行许可证电子版不能为空'
            },
            settlementBankAccountName: {
                required: '<i class="icon-exclamation-sign"></i>银行开户名不能为空'
            },
            settlementBankAccountNumber: {
                required: '<i class="icon-exclamation-sign"></i>银行账号不能为空'
            },
            settlementBankName: {
                required: '<i class="icon-exclamation-sign"></i>开户银行支行名称不能为空'
            },
            settlementBankCode: {
                required: '<i class="icon-exclamation-sign"></i>支行联行号不能为空'
            },
            settlementBankAddress: {
                required: '<i class="icon-exclamation-sign"></i>开户银行所在地不能为空'
            },
            taxRegistrationCertificate: {
                required: '<i class="icon-exclamation-sign"></i>税务登记证号不能为空'
            },
            taxpayerId: {
                required: '<i class="icon-exclamation-sign"></i>纳税人识别号不能为空'
            },
            taxRegistrationImage: {
                required: '<i class="icon-exclamation-sign"></i>税务登记证号电子版'
            },
            storeName: {
                required: '<i class="icon-exclamation-sign"></i>店铺名称不能为空',
                remote: '店铺名称已存在'
            },
            storeGrade: {
                required: '<i class="icon-exclamation-sign"></i>请选择店铺等级'
            },
            storeClass: {
                required: '<i class="icon-exclamation-sign"></i>请选择主营行业'
            },
            bindCategory: {
                required: '<i class="icon-exclamation-sign"></i>请选择经营分类'
            }
        }
    });

    /**
     * 手动添加验证分组，原生没有验证分组
     * 需要填写里面的verifyGroup对象
     */
    var verifyGroupByid = function(groupId){
        var errorArray = [],
            //验证数组保存input id
            verifyGroup = {
                1:['companyName', 'companyAddress', 'companyAddressDetail', 'companyPhone',
                'companyEmployeeCount', 'companyRegisteredCapital', 'contactsName', 'contactsPhone',
                'contactsEmail', 'businessLicenceNumber', 'businessSphere', 'businessLicenceImage',
                'organizationCode', 'organizationImage', 'generalTaxpayer'],
                2:['bankAccountName', 'bankAccountNumber', 'bankName', 'bankCode', 'bankAddress',
                'bankLicenceImage', 'settlementBankAccountName', 'settlementBankAccountNumber',
                'settlementBankName', 'settlementBankCode', 'settlementBankAddress',
                'taxRegistrationCertificate', 'taxpayerId', 'taxRegistrationImage'],
                3:['storeName', 'storeGrade', 'storeClass', 'bindCategory']
            };
        errorArray = verifyGroup [groupId] && ($.map(verifyGroup [groupId],function(n){
            if (! v.element("#"+n)){
                return n ;
            }
        }));
        return errorArray;
    }

    //绑定方法
    var _bindEvent = function () {
        var changeStep = function(step) {
            //控制标签
            $("#sf1").hide();
            $("#sf2").hide();
            $("#sf3").hide();
            $("#sf" + step).show();
            //控制tab状态
            $tab = $("#tabStep" + step);
            $tab.addClass("current");
            $tab.prev().addClass("current");
            $tab.next().removeClass("current");
            //返回顶部
            $('body,html').animate({
                scrollTop: 0
            }, 100)
        };
        //第一步Next按钮
        $("#btnStep1Next").on("click", function () {
            if(verifyGroupByid(1).length == 0 ){
                current = 1;
                changeStep("2");
                $('dt[dataType="leftStep"]').removeClass("current");
                $("#leftStep3").addClass("current");
            }
        });

        //第二步Pre按钮
        $("#btnStep2Pre").click(function () {
            current = 0;
            changeStep("1");
            $('dt[dataType="leftStep"]').removeClass("current");
            $("#leftStep2").addClass("current");
        });

        //第二步Next按钮，到第三步
        $("#btnStep2Next").click(function () {
            if (verifyGroupByid(2).length == 0 ) {
                current = 2;
                changeStep("3");
                $('dt[dataType="leftStep"]').removeClass("current");
                $("#leftStep4").addClass("current");
            }
        });

        //第三步Pre按钮，返回第二步
        $("#btnStep3Pre").click(function () {
            current = 1;
            changeStep("2");
            $('dt[dataType="leftStep"]').removeClass("current");
            $("#leftStep3").addClass("current");
        });

        //第三步提交按钮
        $("#btnSubmit").click(function () {
            if (verifyGroupByid(2).length == 0 ) {
                $("#joinForm").submit();
            }
        });
    };

    _bindEvent();

    //同意协议按钮
    $("#btnAgreement").on("click", function() {
        if($('#chkAgreement').prop('checked')) {
            $("#divAgreement").hide();
            $("#divJoinForm").show();
            $("#tabStep1").addClass("current");
            $('dt[dataType="leftStep"]').removeClass("current");
            $("#leftStep2").addClass("current");
        } else {
            Nc.alertError("请阅读并同意商家入驻协议");
        }
    });

    //公司地址选择
    $("#divCompanyAddress").NcArea({
        hiddenInput: [
        {
            id: "companyAddressId",
            name: "companyAddressId",
            value: "0"
        },
        {
            id: "companyAddress",
            name: "companyAddress",
            value: ""
        }]
    })
    .on("nc.select.last", function (e, element) {
        v.element("#companyAddress");
    });

    //银行所在地
    $("#divBankAddress").NcArea({
        hiddenInput: [
        {
            id: "bankAddressId",
            name: "bankAddressId",
            value: "0"
        },
        {
            id: "bankAddress",
            name: "bankAddress",
            value: ""
        }]
    })
    .on("nc.select.last", function (e, element) {
        v.element("#bankAddress");
    });

    //开户银行所在地
    $("#divSettlementBankAddress").NcArea({
        hiddenInput: [
        {
            id: "settlementBankAddressId",
            name: "settlementBankAddressId",
            value: "0"
        },
        {
            id: "settlementBankAddress",
            name: "settlementBankAddress",
            value: ""
        }]
    })
    .on("nc.select.last", function (e, element) {
        v.element("#settlementBankAddress");
    });

    //营业执照电子版上传
    $("#btnUploadBusinessLicenceImage").fileupload({
        dataType: 'json',
        url: ncGlobal.sellerRoot + "image/upload.json",
        formData: {type:ncGlobal.filesType.setting},
        done: function (e, data) {
            if (data.result.code == 200) {
                var _img = $('<a href="' + data.result.data.url + '" data-lightbox="img-PayingCertificate" data-item="image" class="upload-file-thumb" title="营业执照电子版"><img src="'+data.result.data.url+'" alt=""></a>');
                $("#divUploadBusinessLicenceImage").nextAll().remove("[data-item='image']");
                $("#divUploadBusinessLicenceImage").after(_img);
                $("#businessLicenceImage").val(data.result.data.name);
                v.element("#businessLicenceImage");
				//图片同比例缩放-默认
                _img.find("img").jqthumb({
        		// $('#divUploadBusinessLicenceImage img').jqthumb({
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
	
    //组织机构代码证电子版上传
    $("#btnUploadOrganizationImage").fileupload({
        dataType: 'json',
        url: ncGlobal.sellerRoot + "image/upload.json",
        formData: {type:ncGlobal.filesType.setting},
        done: function (e, data) {
            if (data.result.code == 200) {
                console.log(data.result.data.name);
                console.log(data.result.data.url);

                var _img = $('<a href="' + data.result.data.url + '" data-lightbox="img-PayingCertificate" data-item="image" class="upload-file-thumb" title="组织机构代码证电子版"><img src="'+data.result.data.url+'" alt=""></a>');
                $("#divUploadOrganizationImage").nextAll().remove("[data-item='image']");
                $("#divUploadOrganizationImage").after(_img);
                $("#organizationImage").val(data.result.data.name);
                v.element("#organizationImage");
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

    //一般纳税人证明上传
    $("#btnUploadGeneralTaxpayer").fileupload({
        dataType: 'json',
        url: ncGlobal.sellerRoot + "image/upload.json",
        formData: {type:ncGlobal.filesType.setting},
        done: function (e, data) {
            if (data.result.code == 200) {
                console.log(data.result.data.name);
                console.log(data.result.data.url);

                var _img = $('<a href="' + data.result.data.url + '" data-lightbox="img-PayingCertificate" data-item="image" class="upload-file-thumb" title="一般纳税人证明"><img src="'+data.result.data.url+'" alt=""></a>');
                $("#divUploadGeneralTaxpayer").nextAll().remove("[data-item='image']");
                $("#divUploadGeneralTaxpayer").after(_img);
                $("#generalTaxpayer").val(data.result.data.name);
                v.element("#generalTaxpayer");

				_img.find('img').jqthumb({
					width: 120,
					height: 150,
					after: function (imgObj) {
					imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
					}
				});
            } else {
                Nc.alertError("文件上传失败");
            }
        }
    });

    //开户银行许可证电子版
    $("#btnUploadBankLicenceImage").fileupload({
        dataType: 'json',
        url: ncGlobal.sellerRoot + "image/upload.json",
        formData: {type:ncGlobal.filesType.setting},
        done: function (e, data) {
            if (data.result.code == 200) {
                console.log(data.result.data.name);
                console.log(data.result.data.url);
                var _img = $('<a href="' + data.result.data.url + '" data-lightbox="img-PayingCertificate" data-item="image" class="upload-file-thumb" title="开户银行许可证电子版"><img src="'+data.result.data.url+'" alt=""></a>');
                $("#divUploadBankLicenceImage").nextAll().remove("[data-item='image']");
                $("#divUploadBankLicenceImage").after(_img);
                $("#bankLicenceImage").val(data.result.data.name);
                v.element("#bankLicenceImage");
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

    //税务登记证号电子版
    $("#btnUploadTaxRegistrationImage").fileupload({
        dataType: 'json',
        url: ncGlobal.sellerRoot + "image/upload.json",
        formData: {type:ncGlobal.filesType.setting},
        done: function (e, data) {
            if (data.result.code == 200) {
                console.log(data.result.data.name);
                console.log(data.result.data.url);
                var _img = $('<a href="' + data.result.data.url + '" data-lightbox="img-PayingCertificate" data-item="image" class="upload-file-thumb" title="税务登记证号电子版"><img src="'+data.result.data.url+'" alt=""></a>');
                $("#divUploadTaxRegistrationImage").nextAll().remove("[data-item='image']");
                $("#divUploadTaxRegistrationImage").after(_img);
                $("#taxRegistrationImage").val(data.result.data.name);
                v.element("#taxRegistrationImage");
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
})();

