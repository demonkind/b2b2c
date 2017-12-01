/**
 * Created by zxy on 2015-12-30
 */
//列表开始
var dtGridColumns = [
    {
        id: 'cashId',
        title: '提现ID',
        type: 'number',
        columnClass: 'text-center width-100'
    },
    {
        id: 'cashSn',
        title: '提现编号',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left',
        fastQuery: true,
        fastQueryType: 'eq'
    },
    {
        id: 'memberName',
        title: '会员名称',
        type: 'string',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        fastSort: false
    },
    {
        id: 'amount',
        title: '提现金额（元）',
        type: 'number',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150 f-w-600 text-warning',
        format: '###.00'
    },
    {
        id: 'addTime',
        title: '申请时间',
        type: 'date',
		headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: true,
        fastQueryType: 'range',
        format: 'yyyy-MM-dd',
        hideType: 'xs|sm|md'
    },
    {
        id: 'receiveCompany',
        title: '收款方式',
        type: 'string',
		headerClass: 'text-left width-100',
        columnClass: 'text-left width-100',
        fastSort: false,
        hideType: 'xs|sm'
    },
    {
        id: 'receiveAccount',
        title: '收款账号',
        type: 'string',
		headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastSort: false,
        hideType: 'xs|sm|md'
    },
    {
        id: 'receiveUser',
        title: '开户名',
        type: 'string',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        fastSort: false,
        hideType: 'xs|sm|md|lg'
    },
    {
        id: 'state',
        title: '状态',
        type: 'string',
		headerClass: 'text-center width-100',
        columnClass: 'text-center width-100',
		hideType: 'xs|sm|md',
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            2: $lang.predeposit.cashStateFail,
            1: $lang.predeposit.cashStateSuccess,
            0: $lang.predeposit.cashStateNotDealwith
        }
    },
    {
        id: 'payTime',
        title: '支付时间',
        type: 'date',
        columnClass: 'text-center',
        hideType: 'xs|sm|md|lg'
    },
    {
        id: 'adminName',
        title: '管理员',
        type: 'string',
        columnClass: 'text-center',
        fastSort: false,
        hideType: 'xs|sm|md|lg'
    },
    {
        id: 'adminId',
        title: '管理员编号',
        type: 'number',
        columnClass: 'text-center',
        fastSort: false,
        hideType: 'xs|sm|md|lg'
    },
    {
        id: 'refuseReason',
        title: '拒绝提现理由',
        type: 'string',
        columnClass: 'text-center',
        fastSort: false,
        hideType: 'xs|sm|md|lg'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html = "";
            if (record.state == 0) {
                html += "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='"
                    + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;审核&nbsp;</a>";
                html += "<a href='javascript:;' class='btn btn-sm btn-danger' onclick='OperateHandle.delCash(" + record.cashId + ")'>" +
                    "<i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
            }
            return html;
        }
    }
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'predeposit/cash.json',
    exportFileName: '提现列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["memberId","payState"],Timestamp: ["addTime"]}
};
var grid = $.fn.DtGrid.init(dtGridOption);
//排序
grid.sortParameter.columnId = 'state';
grid.sortParameter.sortType = 2;
//列表结束

//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            //查询会员详情，构造参数
            var memberName = $('#keyword').val();
            if (memberName) {
                var memberId = 0;
                var params = {"memberName": memberName};
                $.ajax({
                    type : "get",
                    url : ncGlobal.adminRoot + "common/member/getid",
                    data : params,
                    async : false,
                    success : function(xhr){
                        if(xhr.code == 200) {
                            memberId = xhr.data;
                        }
                    }
                });
                grid.fastQueryParameters['eq_memberId'] = memberId;
                grid.pager.startRecord= 0;
                grid.pager.nowPage = 1;
                grid.pager.recordCount = -1;
                grid.pager.pageCount = -1;
            }
            grid.refresh(true);
        });

        //编辑对话框初始化
        $('#editModal').on('show.bs.modal', function (event) {
            //获取接受事件的元素
            var button = $(event.relatedTarget);
            //获取data 参数
            var datano = button.data('no');
            var modal = $(this);
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];
            var editForm = $("#editForm");
            modal.find('input[name="cashSn"]').val(gridData.cashSn);
            modal.find('input[name="cashId"]').val(gridData.cashId);
            modal.find('input[name="amount"]').val(gridData.amount);
            modal.find('input[name="memberName"]').val(gridData.memberName);
            modal.find('input[name="receiveCompany"]').val(gridData.receiveCompany);
            modal.find('input[name="receiveAccount"]').val(gridData.receiveAccount);
            modal.find('input[name="receiveUser"]').val(gridData.receiveUser);
            //绑定日历控件
            modal.find('input[name="payTime"]').bind("focus", function () {
                WdatePicker({maxDate: '%y-%M-%d'})
            });
            //状态
            var stateHtml = '<label class="radio-inline">' +
                '<input type="radio" value="1" name="state" id="state_1" data-parsley-multiple="state" data-parsley-required="true" data-parsley-errors-container="#stateErrorContent" data-parsley-required-message="请选择状态" onclick="OperateHandle.showContent(1);"/>'+$lang.predeposit.cashStateSuccess+'</label>'+
                '<label class="radio-inline">' +
                '<input type="radio" value="2" name="state" id="state_2" data-parsley-multiple="state" data-parsley-required="true" data-parsley-errors-container="#stateErrorContent" data-parsley-required-message="请选择状态" onclick="OperateHandle.showContent(2);"/>'+$lang.predeposit.cashStateFail+
                '</label>';
            $("#stateModule").html(stateHtml);
            //清除数据
            modal.find('input[name="payTime"]').val("");
            //清除错误提示
            editForm.psly().reset();
            $(".alert-danger").remove();
        });
    }

    //删除记录
    function _delCash(cashId){
        $.ncConfirm({
            url: ncGlobal.adminRoot + "predeposit/cash/del",
            data: {
                cashId: cashId
            }
        });
    }
    //根据状态显示内容
    function _showContent(state){
        if (state == 1) {
            $("#payTimeDiv").show();
            $("#refuseReasonDiv").hide();
        }else{
            $("#payTimeDiv").hide();
            $("#refuseReasonDiv").show();
        }
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delCash:_delCash,
        showContent:_showContent
    }
}();
//操作处理结束
window.Parsley.addValidator('checkpaytime', {
    validateString: function(value) {
        if ($("#state_1").attr("checked") == "checked") {
            if (value.length > 0) {
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    },
    messages: {
        en: '请选择付款时间 '
    }
});

window.Parsley.addValidator('checkrefusereason', {
    validateString : function(value) {
        if ($("#state_2").attr("checked") == "checked") {
            if (value.length > 0) {
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    },
    messages: {
        en: '请填写拒绝理由'
    }
});

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});