/**
 * Created by shopnc on 2016/1/26.
 */
//定义表格
var dtGridColumns = [{
    id: 'ordersSnStr',
    title: '订单编号',
    type: 'string',
    headerClass: 'text-left width-200',
    columnClass: 'text-left width-200',
    fastSort: false,
    fastQuery: true,
    fastQueryType: 'eq',
}, {
    id: 'ordersFrom',
    title: '订单来源',
    type: 'string',
    headerClass: 'text-center width-100',
    columnClass: 'text-center width-100',
    fastSort: true,
    fastQuery: true,
    fastQueryType: 'eq',
    hideType:'md|sm|xs'
}, {
    id: 'finishTime',
    title: '订单完成时间',
    type: 'date',
    format: 'yyyy-MM-dd',
    headerClass: 'text-left width-200',
    columnClass: 'text-left width-200',
    fastQuery: true,
    fastQueryType: 'range',
    hideType:'md|sm|xs'
}, {
    id: 'ordersAmount',
    title: '订单金额（元）',
    type: 'number',
    format: '###.00',
    headerClass: 'text-left width-150',
    columnClass: 'text-left width-150',
    fastQuery: false,
    fastSort: false
}, {
    id: 'paymentCode',
    title: '支付方式',
    type: 'string',
    headerClass: 'text-left width-100',
    columnClass: 'text-left width-100',
    fastSort: false,
    fastQuery: true,
    fastQueryType: 'eq',
    codeTable: ordersGlobal.paymentList,
    hideType:'md|sm|xs'
}, {
    id: 'paySnStr',
    title: '支付单号',
    type: 'string',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastQuery: true,
    fastQueryType: 'eq',
    fastSort: false
}, {
    id: 'paymentTime',
    title: '支付时间',
    type: 'date',
    format: 'yyyy-MM-dd',
    headerClass: 'text-center width-200',
    columnClass: 'text-center width-200',
    fastQuery: true,
    fastQueryType: 'range',
    hideType:'md|sm|xs'
}, {
    id: 'commissionAmount',
    title: '收取佣金（元）',
    type: 'number',
    format: '###.00',
    headerClass: 'text-left width-120',
    columnClass: 'text-left width-120 f-w-600 text-warning',
    fastQuery: false,
    fastSort: false
}, {
    id: 'operation',
    title: '管理操作',
    type: 'string',
    columnClass: 'text-center width-100',
    fastSort: false,
    extra: false,
    resolution: function(value, record, column, grid, dataNo, columnNo) {
        var content = '<a href="' + ncGlobal.adminRoot + "orders/info/" + record.ordersId + '" class="btn btn-sm btn-info" data-dataNo="' + dataNo + '"><i class="fa fa-eye"></i>&nbsp;查看&nbsp;</a>';
        return content;
    }
}];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'orders/list.json',
    exportFileName: '商品订单列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh',
    pageSize: 20,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType:{long:["ordersSn","paySn"],int:["ordersState","storeId"],Timestamp:["createTime","paymentTime","finishTime"]}
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'ordersId';
grid.sortParameter.sortType = 1;
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_storeId'] = ordersGlobal.billinfo.storeId;
grid.fastQueryParameters['ge_finishTime'] = ordersGlobal.billinfo.startTime;
grid.fastQueryParameters['le_finishTime'] = ordersGlobal.billinfo.endTime;
grid.fastQueryParameters['eq_ordersState'] = 40;
var orders = function() {
    /**
     * 绑定事件
     * @private
     */
    function _bindEvent() {
        //模糊查询
        $("#customSearch").click(function() {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['eq_ordersSnStr'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    //外部可调用
    return {
        init: function() {
            _bindEvent();
        }
    }
}()
$(function() {
    grid.load();
    orders.init();
});