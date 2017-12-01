/**
 * Created by shopnc on 2016/1/26.
 */
//定义表格
var dtGridColumns = [{
    id: 'ordersSnStr',
    title: '订单编号',
    type: 'string',
    headerClass: 'text-left width-150',
    columnClass: 'text-left width-150',
    fastSort: false,
    fastQuery: true,
    fastQueryType: 'eq',
}, {
    id: 'ordersFrom',
    title: '来源',
    type: 'string',
    headerClass: 'text-center width-50',
    columnClass: 'text-center width-50',
    fastSort: true,
    fastQuery: true,
    fastQueryType: 'eq',
    hideType:'lg|md|sm|xs'
}, {
    id: 'createTime',
    title: '下单时间',
    type: 'date',
    format: 'yyyy-MM-dd',
    headerClass: 'text-left width-150',
    columnClass: 'text-left width-150',
    fastQuery: true,
    fastQueryType: 'range'
}, {
    id: 'ordersAmount',
    title: '订单金额（元）',
    type: 'number',
    format: '###.00',
    headerClass: 'text-left width-150',
    columnClass: 'text-left width-150 f-w-600 text-warning',
	hideType: 'md|sm|xs',
    fastQuery: false,
    fastSort: false
}, {
    id: 'freightAmount',
    title: '运费（元）',
    type: 'number',
    format: '###.00',
    headerClass: 'text-left',
    columnClass: 'text-left',
	hideType: 'lg|md|sm|xs',
    fastQuery: false,
    fastSort: false
}, {
    id: 'ordersState',
    title: '订单状态',
    type: 'number',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastQuery: true,
    fastSort: true,
    fastQueryType: 'eq',
    codeTable: ordersGlobal.ordersStateList
}, {
    id: 'paymentCode',
    title: '支付方式',
    type: 'string',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastSort: false,
    fastQuery: true,
    fastQueryType: 'eq',
    hideType: 'lg|md|sm|xs',
    codeTable: ordersGlobal.paymentList
}, {
    id: 'paySnStr',
    title: '支付单号',
    type: 'string',
    headerClass: 'text-left width-150',
    columnClass: 'text-left width-150',
    fastQuery: true,
    fastQueryType: 'eq',
    fastSort: false
}, {
    id: 'paymentTime',
    title: '支付时间',
    type: 'date',
    format: 'yyyy-MM-dd',
    headerClass: 'text-left width-150',
    columnClass: 'text-left width-150',
    fastQuery: true,
    fastQueryType: 'range',
    hideType: 'md|sm|xs'
}, {
    id: 'predepositAmount',
    title: '预存款支付（元）',
    type: 'number',
    format: '###.00',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastQuery: false,
    fastSort: false,
    hideType: 'lg|md|sm|xs'
}, {
    id: 'finishTime',
    title: '完成时间',
    type: 'date',
    format: 'yyyy-MM-dd',
    headerClass: 'text-left width-150',
    columnClass: 'text-left width-150',
    fastQuery: true,
    fastQueryType: 'range',
    hideType: 'md|sm|xs'
}, {
    id: 'operation',
    title: '管理操作',
    type: 'string',
    columnClass: 'text-center width-200',
    fastSort: false,
    extra: false,
    resolution: function(value, record, column, grid, dataNo, columnNo) {
        var content = "";
        content += '<div class="btn-group">';
        if (record.showAdminCancel != 1 && record.showAdminPay != 1) {
            //无操作时
            content += '<a href="javascript:;" data-toggle="dropdown" aria-expanded="false" class="btn btn-default btn-sm dropdown-toggle m-r-10" style="cursor: not-allowed;" title="不可编辑">' +
                '<i class="fa fa-gears"></i>&nbsp;编辑&nbsp;&nbsp;&nbsp;&nbsp;</a>';
        } else {
            //有操作时
            content += '<a href="javascript:;" data-toggle="dropdown" aria-expanded="false" class="btn btn-primary btn-sm dropdown-toggle m-r-10">' +
            '<i class="fa fa-gears"></i>&nbsp;编辑&nbsp;' +
            '<span class="caret"></span></a><ul class="dropdown-menu dropdown-menu-left">';
            if (record.showAdminCancel == 1) {
                content += "<li><a href='javascript:;' onclick='orders.cancel("+record.ordersId+",\""+record.ordersSn+"\")' data-no='" + dataNo + "' ><i class='fa fa-lg fa-ban m-r-10' ></i>取消订单</a></li>";
            }
            if(record.showAdminPay == 1 ){
                content += '<li><a href="#payModal" data-toggle="modal" data-no="' + dataNo + '"><i class="fa fa-lg fa-jpy m-r-10"></i>&nbsp;收到货款</a></li>';
            }
            content += '</ul>';
        }

        content += '</div>';
        content += '<a href="' + ncGlobal.adminRoot + "orders/info/" + record.ordersId + '" class="btn btn-sm btn-info" data-dataNo="' + dataNo + '"><i class="fa fa-eye"></i>&nbsp;查看&nbsp;</a>';
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
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType:{long:["ordersSn","paySn"],int:["ordersState"],Timestamp:["createTime","paymentTime","finishTime"]}
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'ordersId';
grid.sortParameter.sortType = 1;
var orders = function() {
    //取消URL
    var cancelUrl = ncGlobal.adminRoot + "orders/cancel";

    /**
     * 取消
     */
    function _cancel(id, content) {
        var tpl = '您选择对订单 <strong>' + content + '</strong> 进行取消操作，取消后将无法恢复。<br/>您确定要进行取消操作吗?'
        $.ncConfirm({
            url: cancelUrl,
            data: {
                ordersId: id
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
            grid.fastQueryParameters['eq_ordersSnStr'] = $('#keyword').val();
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
            modal.find('div[nctype="ordersSn"]').html(gridData.ordersSn);
            modal.find('div[nctype="paySn"]').html(gridData.paySn);
            modal.find('div[nctype="ordersAmount"]').html(Nc.priceFormat(gridData.ordersAmount));
            modal.find('input[name="ordersId"]').val(gridData.ordersId);
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
        cancel: _cancel
    }
}()
$(function() {
    grid.load();
    orders.init();
});