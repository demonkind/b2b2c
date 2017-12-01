/**
 * Created by cj on 2015/11/24.
 */
var handleBootstrapWizardsValidation = function() {
    "use strict";
    $("#wizardEdit").bwizard({
        validating: function(e, t) {
            if (t.index == 0) {
                if (false === $('form[name="form-wizard-edit"]').parsley().validate("wizard-step-1")) {
                    return false
                }
            } else if (t.index == 1) {
                if (false === $('form[name="form-wizard-edit"]').parsley().validate("wizard-step-2")) {
                    return false
                }
            } else if (t.index == 2) {
                if (false === $('form[name="form-wizard-edit"]').parsley().validate("wizard-step-3")) {
                    return false
                }
            }
        }
    });
};
var FormWizardValidation = function() {
    "use strict";
    return {
        init: function() {
            handleBootstrapWizardsValidation()
        }
    }
}();

//列表开始
var dtGridColumns = [
    {
        id: 'memberId',
        title: '会员ID',
        type: 'number',
        columnClass: 'text-center width-100'
    },
    {
        id: 'memberName',
        title: '会员名称',
        type: 'string',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'email',
        title: '会员邮箱',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        hideType: 'xs|sm|md',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'mobile',
        title: '会员手机',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left width-150',
		hideType: 'xs|sm|md',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'trueName',
        title: '真实姓名',
        type: 'string',
        columnClass: 'text-center',
        hideType: 'xs|sm|md|lg',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'memberSex',
        title: '性别',
        type: 'number',
        columnClass: 'text-center',
        hideType: 'xs|sm|md|lg',
        codeTable: {
            0: $lang.member.memberSex0,
            1: $lang.member.memberSex1,
            2: $lang.member.memberSex2
        }
    },
    {
        id: 'addressAreaInfo',
        title: '所在地',
        type: 'string',
        hideType: 'xs|sm|md|lg'
    },
    {
        id: 'birthday',
        title: '出生日期',
        type: 'date',
        columnClass: 'text-center',
        hideType: 'xs|sm|md|lg',
        fastQuery: true,
        fastQueryType: 'range',
        format: 'yyyy-MM-dd'
    },
    {
        id: 'memberQQ',
        title: 'QQ',
        type: 'string',
        hideType: 'xs|sm|md|lg',
        fastSort: false
    },
    {
        id: 'memberWW',
        title: '旺旺',
        type: 'string',
        hideType: 'xs|sm|md|lg',
        fastSort: false
    },
    {
        id: 'predepositFreeze',
        title: '冻结预存款',
        type: 'number',
        hideType: 'xs|sm|md|lg',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return Nc.priceFormat(value,'%s')+"元";
        }
    },
    {
        id: 'registerTime',
        title: '注册时间',
        type: 'date',
        columnClass: 'text-center',
        hideType: 'xs|sm|md|lg',
        fastQuery: true,
        fastQueryType: 'range',
        format: 'yyyy-MM-dd'
    },
    {
        id: 'memberPoints',
        title: '会员积分',
        type: 'number',
		headerClass: 'text-center width-100',
        columnClass: 'text-center width-100',
        codeTable: {"": "0"}
    },
    {
        id: 'experiencePoints',
        title: '会员经验',
        type: 'number',
		headerClass: 'text-center width-100',
        columnClass: 'text-center width-100',
        codeTable: {"": "0"}
    },
    {
        id: 'predepositAvailable',
        title: '可用预存款（元）',
        type: 'number',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150 f-w-600 text-warning',
        format: '###.00'
    },
    {
        id: 'allowBuy',
        title: '允许购买',
        type: 'number',
        columnClass: 'text-center width-100',
        codeTable: {
            1: $lang.member.allowBuy1,
            0: $lang.member.allowBuy0
        }
    },
    {
        id: 'allowTalk',
        title: '允许发言',
        type: 'number',
        columnClass: 'text-center width-100',
        codeTable: {
            1: $lang.member.allowTalk1,
            0: $lang.member.allowTalk0
        }
    },
    {
        id: 'state',
        title: '会员状态',
        type: 'string',
        columnClass: 'text-center width-100',
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.member.memberStateOpen,
            0: $lang.member.memberStateClose
        }
    },
    {
        id: 'loginTime',
        title: '最后登录时间',
        type: 'date',
        hideType: 'xs|sm|md|lg'
    },
    {
        id: 'loginIp',
        title: '最后登录IP',
        type: 'string',
        hideType: 'xs|sm|md|lg',
        fastSort: false
    },
    {
        id: 'loginNum',
        title: '登录次数',
        type: 'number',
        hideType: 'xs|sm|md|lg',
        codeTable: {"": "0"}
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-100',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='"
                + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
        }
    }
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'member/list.json',
    exportFileName: '会员列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["state"], Timestamp: ["birthday", "registerTime"]}
};
var grid = $.fn.DtGrid.init(dtGridOption);
//排序
grid.sortParameter.columnId = 'memberId';
grid.sortParameter.sortType = 1;
//列表结束
//操作处理开始
var OperateHandle = function () {



    function _bindEvent() {
        /****** 编辑三步效验开始 ******/
        FormWizardValidation.init();

        //新增对话框初始化
        $("#addModal").on("show.bs.modal", function (e) {
            //清除错误信息
            $(".alert-danger").remove();
            $("#addForm").psly().reset();
            //清空控件
            $("#memberId").val(0);
            $("#addForm").find("[name='memberName']").val("");
            $("#addForm").find("[name='memberPwd']").val("");
            $("#addForm").find("[name='repeatMemberPwd']").val("");
            $("#addForm").find("[name='trueName']").val("");
            $("#addForm").find("[name='mobile']").val("");
            $("#addForm").find("[name='email']").val("");
        });

        //编辑对话框初始化
        $('#editModal').on('show.bs.modal', function (event) {
            //图片同比例缩放-默认
            $('#avatarThumb img').jqthumb({
                width: 100,
                height: 100,
                after: function (imgObj) {
                    imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                }
            });

            //获取接受事件的元素
            var button = $(event.relatedTarget);
            //获取data 参数
            var datano = button.data('no');
            var modal = $(this);
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];
            var editForm = $("#editForm");
            modal.find('input[name="memberId"]').val(gridData.memberId);
            modal.find('input[id="memberName"]').val(gridData.memberName);
            modal.find('input[id="memberNameHidden"]').val(gridData.memberName);
            modal.find('input[name="trueName"]').val(gridData.trueName);
            modal.find('input[name="mobile"]').val(gridData.mobile);
            modal.find('input[name="email"]').val(gridData.email);
            modal.find('input[name="birthday"]').val(gridData.birthday);
            //密码
            modal.find('input[name="memberPwd"]').val("");
            modal.find('input[name="repeatMemberPwd"]').val("");
            //绑定日历控件
            modal.find('input[name="birthday"]').bind("focus", function () {
                WdatePicker({maxDate: '%y-%M-%d'})
            });
            modal.find('input[name="memberQQ"]').val(gridData.memberQQ);
            modal.find('input[name="memberWW"]').val(gridData.memberWW);
            //头像
            $('#avatarFileImage').val(gridData.avatar);
            $('#avatarFileImageImg').attr('src', ncImage(gridData.avatarUrl, 120, 120));
            $("#allowBuy").bootstrapSwitch('state', gridData.allowBuy == 1 ? true : false);
            $("#allowTalk").bootstrapSwitch('state', gridData.allowTalk == 1 ? true : false);
            $("#state").bootstrapSwitch('state', gridData.state == 1 ? true : false);

            //性别
            var memberSexHtml = '<label class="radio-inline">' +
                '<input type="radio" value="0" name="memberSex" id="memberSex_0" data-parsley-multiple="memberSex" />'+$lang.member.memberSex0+
                '</label>'+
                '<label class="radio-inline">' +
                '<input type="radio" value="1" name="memberSex" id="memberSex_1" data-parsley-multiple="memberSex" />'+$lang.member.memberSex1+
                '</label>'+
                '<label class="radio-inline">' +
                '<input type="radio" value="2" name="memberSex" id="memberSex_2" data-parsley-multiple="memberSex" />'+$lang.member.memberSex2+
                '</label>';
            $("#memberSexModule").html(memberSexHtml);
            modal.find("[id='memberSex_"+gridData.memberSex+"']").attr("checked", true);

            //所在地
            $("#divAddress").remove();
            $("#divAddressParent").append($("<div>",{
                id:"divAddress",
                "data-area-text":'',
                "data-area-id":''
            }));
            if (gridData.addressAreaInfo) {
                $("#divAddress").attr("data-area-text",gridData.addressAreaInfo);
            }else{
                $("#divAddress").attr("data-area-text","");
            }
            if (gridData.addressAreaId) {
                $("#divAddress").attr("data-area-id",gridData.addressAreaId);
            }else{
                $("#divAddress").attr("data-area-id",0);
            }
            $("#divAddress").append('<input type="hidden" value="'+ gridData.addressProvinceId +'" name="memberAddress_1" data-deep="1">');
            $("#divAddress").append('<input type="hidden" value="'+ gridData.addressCityId +'" name="memberAddress_2" data-deep="2">');
            $("#divAddress").NcSimpleArea({
                hiddenInput: [
                    {name: "addressAreaId", value: 0},
                    {name: "addressAreaInfo", value: ""}
                ],
                url: ncGlobal.adminRoot + "area/list.json/",
                showDeep: 3,
                dataFormat: "areaList",
                dataHiddenName:"memberAddress_"
            });

            //清除错误提示
            editForm.psly().reset();
            $(".alert-danger").remove();

            $("#avatarFileImageDel").click(function () {
                $('#avatarFileImageImg').attr('src', ncGlobal.publicRoot +"img/avatar.gif");
                $('#avatarFileImage').val("");

                //图片同比例缩放-默认
                $('#avatarThumb img').jqthumb({
                    width: 100,
                    height: 100,
                    after: function (imgObj) {
                        imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                    }
                });
            });
        });


        //调用checkbox方法
        $("[name='allowBuy']").bootstrapSwitch();
        $("[name='allowTalk']").bootstrapSwitch();
        $("[name='state']").bootstrapSwitch();

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_memberName'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });


    }

    function _buildHTML() {
        /**
         * 上传插件绑定
         */
        $("#avatarFile").fileupload({
            dataType: 'json',
            url: ncGlobal.adminRoot+"image/upload.json",
            done: function (e, data) {
                if (data.result.code == 200) {
                    $('#avatarFileImage').val(data.result.data.name);
                    $('#avatarFileImageImg').attr('src', data.result.data.url);
                    //图片同比例缩放-新增
                    $('#avatarThumb img').jqthumb({
                        width: 100,
                        height: 100,
                        after: function (imgObj) {
                            imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                        }
                    });
                } else {
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: data.result.message
                    });
                }
            }
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        buildHTML: _buildHTML
    }
}();
//操作处理结束

//验证用户名是否存在
window.Parsley.addAsyncValidator('membernameisexist', function (xhr) {
    var data;
    eval("data="+xhr.responseText);
    if (data.code == 400) {
        return false;
    }else{
        return true;
    }
}, ncGlobal.adminRoot+"member/membernameexist", {"type": "GET", "dataType": "json"});
//验证邮箱是否存在
window.Parsley.addAsyncValidator('emailisexist', function (xhr) {
    var data;
    eval("data="+xhr.responseText);
    if (data.code == 400) {
        return false;
    }else{
        return true;
    }
}, ncGlobal.adminRoot+"member/emailexist", {"type": "GET", "dataType": "json", "data": {
    memberId:function () {
        var memberId = $("#memberId").val();
        if (memberId > 0) {
            return memberId;
        }else{
            return 0;
        }
    }
}});
//验证手机是否存在
window.Parsley.addAsyncValidator('mobileisexist', function (xhr) {
    var data;
    eval("data="+xhr.responseText);
    if (data.code == 400) {
        return false;
    }else{
        return true;
    }
}, ncGlobal.adminRoot+"member/mobileexist", {"type": "GET", "dataType": "json", "data": {
    memberId:function () {
        var memberId = $("#memberId").val();
        if (memberId > 0) {
            return memberId;
        }else{
            return 0;
        }
    }
}});

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
    OperateHandle.buildHTML();

});