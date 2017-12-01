/**
 * Created by zxy on 2016-01-05
 */
//列表开始
var dtGridColumns = [
    {
        id: 'paymentName',
        title: '支付方式',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'paymentState',
        title: '状态',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        fastQuery: false,
        codeTable: {
            1: $lang.paymentState.open,
            0: $lang.paymentState.close
        }
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-150',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='"
                + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
        }
    }   
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'payment/list.json',
    exportFileName: '支付方式',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: '',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50]
};
var grid = $.fn.DtGrid.init(dtGridOption);
//排序
grid.sortParameter.columnId = 'paymentCode';
grid.sortParameter.sortType = 1;
//列表结束

//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_paymentName'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });

        //编辑对话框初始化
        $('#editModal').on('show.bs.modal', function (event) {
            //获取接受事件的元素
            var button = $(event.relatedTarget);
            //获取data 参数
            var datano = button.data('no');
            //var modal = $(this);
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];
            if (gridData.paymentCode == "alipay") {
                $("[nc_type='paymentForm']").hide();
                $("#alipayContent").show();
                var editForm = $("#alipayEditForm");
                editForm.find('input[name="paymentName"]').val(gridData.paymentName);
                editForm.find('input[name="paymentCode"]').val(gridData.paymentCode);
                if (gridData.paymentInfo) {
                    var paymentInfo = eval("paymentInfo="+gridData.paymentInfo);
                    editForm.find('input[name="alipayAccount"]').val(paymentInfo.alipayAccount);
                    editForm.find('input[name="alipayKey"]').val(paymentInfo.alipayKey);
                    editForm.find('input[name="alipayPartner"]').val(paymentInfo.alipayPartner);
                }else{
                    editForm.find('input[name="alipayAccount"]').val("");
                    editForm.find('input[name="alipayKey"]').val("");
                    editForm.find('input[name="alipayPartner"]').val("");
                }
                editForm.find('input[name="state"]').bootstrapSwitch('state', gridData.paymentState == 1 ? true : false);
            }else if(gridData.paymentCode == "wxpay"){
                $("[nc_type='paymentForm']").hide();
                $("#wxpayContent").show();
                var editForm = $("#wxpayEditForm");
                editForm.find('input[name="paymentName"]').val(gridData.paymentName);
                editForm.find('input[name="paymentCode"]').val(gridData.paymentCode);
                if (gridData.paymentInfo) {
                    var paymentInfo = eval("paymentInfo="+gridData.paymentInfo);
                    editForm.find('input[name="wxAppId"]').val(paymentInfo.wxAppId);
                    editForm.find('input[name="wxMchid"]').val(paymentInfo.wxMchid);
                    editForm.find('input[name="wxKey"]').val(paymentInfo.wxKey);
                }else{
                    editForm.find('input[name="wxAppId"]').val("");
                    editForm.find('input[name="wxMchid"]').val("");
                    editForm.find('input[name="wxKey"]').val("");
                }
                editForm.find('input[name="state"]').bootstrapSwitch('state', gridData.paymentState == 1 ? true : false);
            }else if(gridData.paymentCode == "offline"){
                $("[nc_type='paymentForm']").hide();
                $("#offlineContent").show();
                var editForm = $("#offlineEditForm");
                editForm.find('input[name="paymentName"]').val(gridData.paymentName);
                editForm.find('input[name="paymentCode"]').val(gridData.paymentCode);
                editForm.find('input[name="state"]').bootstrapSwitch('state', gridData.paymentState == 1 ? true : false);
            }
            //清除错误提示
            editForm.psly().reset();
            $(".alert-danger").remove();
        });

        $("[name='state']").bootstrapSwitch();
    }

    //外部可调用
    return {
        bindEvent: _bindEvent
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});