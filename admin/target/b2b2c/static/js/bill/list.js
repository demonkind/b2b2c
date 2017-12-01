/**
 * Created by shopnc on 2016/1/26.
 */
//定义表格
var dtGridColumns = [{
    id: 'billSn',
    title: '账单编号',
    type: 'number',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastSort: false,
    fastQuery: true,
    fastQueryType: 'eq',
}, {
    id: 'ordersAmount',
    title: '订单金额（元）',
    type: 'number',
    format: '###.00',
    headerClass: 'text-left',
    columnClass: 'text-left text-warning',
    fastQuery: false,
    fastSort: false,
    hideType:'lg|md|sm|xs'
}, {
    id: 'commissionAmount',
    title: '收取佣金（元）',
    type: 'number',
    format: '###.00',
    headerClass: 'text-left',
    columnClass: 'text-left text-warning',
    fastQuery: false,
    fastSort: false,
    hideType:'lg|md|sm|xs'
}, {
    id: 'refundAmount',
    title: '退单金额（元）',
    type: 'number',
    format: '###.00',
    headerClass: 'text-left',
    columnClass: 'text-left text-success',
    fastQuery: false,
    fastSort: false,
    hideType:'lg|md|sm|xs'
}, {
    id: 'refundCommissionAmount',
    title: '退还佣金（元）',
    type: 'number',
    format: '###.00',
    headerClass: 'text-left',
    columnClass: 'text-left text-success ',
    fastQuery: false,
    fastSort: false,
    hideType:'lg|md|sm|xs'
}, {
    id: 'billAmount',
    title: '本期应结（元）',
    type: 'number',
    format: '###.00',
    headerClass: 'text-left',
    columnClass: 'text-left f-w-600 text-danger ',
    fastQuery: false,
    fastSort: false
}, {
    id: 'createTime',
    title: '出账日期',
    type: 'date',
    format: 'yyyy-MM-dd',
    headerClass: 'text-center',
    columnClass: 'text-center',
    fastQuery: true,
    fastQueryType: 'range',
    hideType:'lg|md|sm|xs'
}, {
    id: 'billState',
    title: '账单状态',
    type: 'number',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastQuery: true,
    fastSort: true,
    fastQueryType: 'eq',
    codeTable: billGlobal.billStateList
}, {
    id: 'storeName',
    title: '商家',
    type: 'string',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastSort: false,
    fastQuery: true,
    fastQueryType: 'lk',
    hideType: 'md|sm|xs'
}, {
    id: 'startTime',
    title: '开始日期',
    type: 'date',
    format: 'yyyy-MM-dd',
    headerClass: 'text-center',
    columnClass: 'text-center',
    fastQuery: false,
    fastSort: true,
    hideType:'lg|md|sm|xs'
}, {
    id: 'endTime',
    title: '结束日期',
    type: 'date',
    format: 'yyyy-MM-dd',
    headerClass: 'text-center',
    columnClass: 'text-center',
    fastQuery: false,
    fastSort: true,
    hideType:'lg|md|sm|xs'
}, {
    id: 'storeId',
    title: '商家Id',
    type: 'string',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastQuery: false,
    fastSort: false,
    hideType: 'lg|md|sm|xs'
}, {
    id: 'paymentTime',
    title: '付款时间',
    type: 'date',
    format: 'yyyy-MM-dd',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastQuery: false,
    fastSort: false,
    hideType: 'lg|md|sm|xs'
}, {
    id: 'paymentNote',
    title: '支付备注',
    type: 'string',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastSort: false,
    fastQuery: false,
    hideType: 'lg|md|sm|xs'
}, {
    id: 'operation',
    title: '管理操作',
    type: 'string',
    columnClass: 'text-center width-200',
    fastSort: false,
    extra: false,
    resolution: function(value, record, column, grid, dataNo, columnNo) {
        var content = "";
        if (record.billStateConfirm == 1) {
            content += '<a href="javascript:void(0);" onclick="bill.access('+ record.billId+ "," + record.billSn+')" class="btn btn-sm btn-primary m-r-10" data-dataNo="' + dataNo + '"><i class="fa fa-check-square-o"></i> 审核</a>';
        }
        if (record.billStateAccess == 1) {
            content += '<a href="#payModal" data-toggle="modal" class="btn btn-sm btn-success m-r-10" data-no="' + dataNo + '"><i class="fa fa-credit-card"></i> 支付</a>';
        }
        content += '<a href="' + ncGlobal.adminRoot + "bill/orders/list/" + record.billId +  '" class="btn btn-sm btn-info" data-dataNo="' + dataNo + '"><i class="fa fa-eye"></i> 查看</a>';
        return content;
    }
}];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'bill/list.json',
    exportFileName: '商品订单结算列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType:{int:["billSn","billState"],Timestamp:["createTime"]}
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'billId';
grid.sortParameter.sortType = 1;
if (billGlobal.billStateType == "confirm") {
    grid.fastQueryParameters = new Object();
    grid.fastQueryParameters['eq_billState'] = 20;
} else if (billGlobal.billStateType == "access") {
    grid.fastQueryParameters = new Object();
    grid.fastQueryParameters['eq_billState'] = 30;
}
var bill = function() {
    //商家确认URL
    var accessUrl = ncGlobal.adminRoot + "bill/access";

    /**
     * 平台审核账单
     */
    function _access(id,sn) {
        var tpl = '您选择对结算单 <strong>' + sn + '</strong> 进行确认操作，操作后将无法恢复。<br/>您确定要进行操作吗?'
        $.ncConfirm({
            alertTitle: "平台审核账单",
            url: accessUrl,
            data: {
                billId: id
            },
            content: tpl
        });
    }

    /**
     * 绑定事件
     * @private
     */
    function _bindEvent() {
        //模糊查询
        $("#customSearch").click(function() {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['eq_billSn'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
        //付款对话框显示时调用
        $('#payModal').on('show.bs.modal', function(event) {
            var //获取接受事件的元素
            button = $(event.relatedTarget),
            //获取data 参数
            datano = button.data('no'),
            modal = $(this),

            //获取列表框中的原始数据
            gridData = grid.sortOriginalDatas[datano],
            payForm = $("#payForm");
            modal.find('div[nctype="billSn"]').html(gridData.billSn);
            modal.find('div[nctype="storeName"]').html(gridData.storeName);
            modal.find('div[nctype="billAmount"]').html(Nc.priceFormat(gridData.ordersAmount));
            modal.find('input[name="billId"]').val(gridData.billId);
            //清除错误提示
            payForm.psly().reset();
            $(".alert-danger").remove();
            payForm.get(0).reset();
        })
    }

    //外部可调用
    return {
        init: function() {
            _bindEvent();
        },
        access: _access
    }
}()
$(function() {
    grid.load();
    bill.init();
});