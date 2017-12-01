/**
 * Created by shopnc on 2016/1/26.
 */
//定义表格
var dtGridColumns = [{
    id: 'refundSnStr',
    title: '退单单号',
    type: 'string',
    headerClass: 'text-left width-200',
    columnClass: 'text-left width-200',
    fastSort: false
},
    {
        id: 'ordersSn',
        title: '订单编号',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastSort: true,
        fastQuery: true,
        fastQueryType: 'eq',
        hideType:'md|sm|xs'
    },
    {
        id: 'refundAmount',
        title: '退款金额（元）',
        type: 'number',
        format: '###.00',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        fastSort: true,
        fastQuery: true,
        fastQueryType: 'eq'
    },
    {
        id: 'refundCommissionAmount',
        title: '退还佣金（元）',
        type: 'number',
        format: '###.00',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150 f-w-600 text-warning',
        fastSort: true,
        fastQuery: true,
        fastQueryType: 'eq'
    },
    {
        id: 'buyerMessage',
        title: '申请原因',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: true,
        fastQuery: true,
        fastQueryType: 'eq',
        hideType:'md|sm|xs'
    },
    {
        id: 'adminTime',
        title: '平台处理时间',
        type: 'date',
        format: 'yyyy-MM-dd',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: true,
        fastQueryType: 'range'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-100',
        fastSort: false,
        extra: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var content = '';
            if (record.refundType == 1) {
                content += '<a href="'+ncGlobal.adminRoot + 'refund/info/'+ record.refundId+'?type=look" class="btn btn-sm btn-info" ><i class="fa fa-eye"></i>&nbsp;查看&nbsp;</a>' + '</div>';
            } else {
                content += '<a href="'+ncGlobal.adminRoot + 'return/info/'+ record.refundId+'?type=look" class="btn btn-sm btn-info" ><i class="fa fa-eye"></i>&nbsp;查看&nbsp;</a>' + '</div>';
            }
            return content;
        }
    }];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'refund/list.json',
    exportFileName: '退单列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh',
    pageSize: 20,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {Timestamp: ["addTime", "adminTime"],int:["sellerState","storeId","goodsId","refundId","refundType"],long:["refundSn"]}
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'refundId';
grid.sortParameter.sortType = 1;
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_storeId'] = ordersGlobal.billinfo.storeId;
grid.fastQueryParameters['ge_adminTime'] = ordersGlobal.billinfo.startTime;
grid.fastQueryParameters['le_adminTime'] = ordersGlobal.billinfo.endTime;
grid.fastQueryParameters['eq_sellerState'] = 2;
grid.fastQueryParameters['gt_goodsId'] = 0;
var reason = function () {
    /**
     * 绑定事件
     * @private
     */
    function _bindEvent() {
        //模糊查询
        $("#customSearch").click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['eq_refundSnStr'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    //外部可调用
    return {
        init: function () {
            _bindEvent();
        }
    }
}()
$(function () {
    grid.load();
    reason.init();
});