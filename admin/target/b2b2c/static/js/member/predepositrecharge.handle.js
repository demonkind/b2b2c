/**
 * Created by zxy on 2015-12-28
 */
//列表开始
var dtGridColumns = [
    {
        id: 'rechargeId',
        title: '充值ID',
        type: 'number',
        columnClass: 'text-center width-100'
    },
    {
        id: 'rechargeSn',
        title: '充值编号',
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
		hideType: 'xs|sm|md',
        fastSort: false
    },
    {
        id: 'amount',
        title: '充值金额（元）',
        type: 'number',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150 f-w-600 text-warning',
        format: '###.00'
    },
    {
        id: 'addTime',
        title: '添加时间',
        type: 'date',
		headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: true,
        fastQueryType: 'range',
        format: 'yyyy-MM-dd',
        hideType: 'xs|sm|md'
    },
    {
        id: 'paymentName',
        title: '支付方式',
        type: 'string',
        columnClass: 'text-center width-100',
        fastSort: false,
        hideType: 'xs|sm|md'
    },
    {
        id: 'tradeSn',
        title: '交易号',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastSort: false,
        hideType: 'xs|sm|md|lg'
    },
    {
        id: 'payState',
        title: '支付状态',
        type: 'string',
        headerClass: 'text-center width-150',
        columnClass: 'text-center width-150',
        fastQuery: true,
        fastQueryType: 'eq',
		hideType: 'xs|sm',
        codeTable: {
            1: $lang.predeposit.rechargeStatePaid,
            0: $lang.predeposit.rechargeStateNotpay
        }
    },
    {
        id: 'payTime',
        title: '支付时间',
        type: 'date',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        hideType: 'xs|sm|md'
    },
    {
        id: 'adminId',
        title: '管理员编号',
        type: 'number',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        fastSort: false,
        hideType: 'xs|sm|md|lg'
    },
    {
        id: 'adminName',
        title: '管理员名称',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        hideType: 'xs|sm|md|lg'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        headerClass: 'text-center width-200',
        columnClass: 'text-center width-200',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html = "";
            if (record.payState == 0) {
                html += "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='"
                    + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;审核&nbsp;</a>";
                html += "<a href='javascript:;' class='btn btn-sm btn-danger' onclick='OperateHandle.delRecharge(" + record.rechargeId + ")'>" +
                    "<i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
            }
            return html;
        }
    }
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'predeposit/recharge.json',
    exportFileName: '充值列表',
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
grid.sortParameter.columnId = 'rechargeId';
grid.sortParameter.sortType = 1;
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
            }
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });

        //获取支付方式
        $.ajax({
            type : "get",
            url : ncGlobal.adminRoot + "predeposit/payment",
            async : false,
            success : function(xhr){
                if(xhr.code == 200) {
                    var html = '<option value="">- 请选择 -</option>';
                    for (key in xhr.data) {
                        html += '<option value="'+key+'">'+xhr.data[key]['paymentName']+'</option>';
                    }
                    $("#paymentCodeSelect").html(html);
                }
            }
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
            modal.find('input[name="rechargeSn"]').val(gridData.rechargeSn);
            modal.find('input[name="rechargeId"]').val(gridData.rechargeId);
            modal.find('input[name="amount"]').val(gridData.amount.toString());
            modal.find('input[name="memberName"]').val(gridData.memberName);
            //绑定日历控件
            modal.find('input[name="payTime"]').bind("focus", function () {
                WdatePicker({maxDate: '%y-%M-%d'})
            });
            //清除数据
            modal.find('input[name="payTime"]').val("");
            $('#paymentCodeSelect').find("option:first").attr("selected","selected");
            modal.find('input[name="tradeSn"]').val("");
            //清除错误提示
            editForm.psly().reset();
            $(".alert-danger").remove();
        });
    }

    //删除记录
    function _delRecharge(rechargeId){
        $.ncConfirm({
            url: ncGlobal.adminRoot + "predeposit/recharge/del",
            data: {
                rechargeId: rechargeId
            }
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delRecharge:_delRecharge
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});