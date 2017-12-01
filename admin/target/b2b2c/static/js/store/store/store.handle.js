/**
 * Created by dqw on 2015/12/25.
 */

//列表开始
var dtGridColumns = [
    {
        id: 'storeId',
        title: '编号',
        type: 'number',
        columnClass: 'text-center width-100',
        fastSort: false
    },
    {
        id: 'storeName',
        title: '店铺名称',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'sellerName',
        title: '商家用户名',
        type: 'string',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'gradeName',
        title: '店铺等级',
        type: 'string',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        hideType: 'xs|sm|md',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'className',
        title: '主营行业',
        type: 'string',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        hideType: 'xs|sm|md',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'storeCreateTime',
        title: '开店日期',
        type: 'date',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-200',
        hideType: 'xs|sm|md',
        fastQuery: true,
        fastQueryType: 'range',
        format: 'yyyy-MM-dd'
    },
    {
        id: 'storeEndTime',
        title: '过期时间',
        type: 'date',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        hideType: 'xs|sm',
        fastQuery: true,
        fastQueryType: 'range',
        format: 'yyyy-MM-dd'
    },
    {
        id: 'state',
        title: '店铺状态',
        type: 'number',
        columnClass: 'text-center width-100',
		hideType: 'xs|sm',
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.store.open,
            0: $lang.store.close
        }
    },
    {
        id: 'billCyleDescription',
        title: '结算周期',
        type: 'string',
        headerClass: 'text-left width-100',
        columnClass: 'text-left width-100',
		hideType: 'xs|sm|md|lg',
        fastSort: false,
        fastQuery: false
    },

    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-150',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a data-target='#editModal' class='btn btn-sm btn-primary' data-toggle='modal' data-no='"
                + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>"
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'store/list.json',
    exportFileName: '店铺列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    tools: 'refresh|faseQuery',
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["state"], Timestamp: ["storeCreateTime", "storeEndTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = 'storeId';
grid.sortParameter.sortType = 1;
//列表结束

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        //新增对话框初始化
        $("#editModal").on("show.bs.modal", function (event) {
            //清除错误信息
            $(".alert-danger").remove();
            $("#editForm").psly().reset();
            //获取接受事件的元素
            var button = $(event.relatedTarget);
            //获取data 参数
            var datano = button.data('no');
            var modal = $(this);
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];

            //清除错误信息
            $(".alert-danger").remove();
            $("#editForm").psly().reset();

            console.log(gridData);

            //清空控件
            $("#storeId").val(gridData.storeId);
            $("#sellerName").val(gridData.sellerName);
            $("#storeName").val(gridData.storeName);
            $("#storeCreateTime").val(gridData.storeCreateTime);
            $("#gradeId").val(gridData.gradeId);
            $("#classId").val(gridData.classId);
            $("#storeEndTime").val(gridData.storeEndTime);
            $("#storeEndTime").on("click", function () {
                WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});
            });
            if(gridData.state == 1) {
                $("#state").bootstrapSwitch('state', true);
            } else {
                $("#state").bootstrapSwitch('state', false);
            }
            // by hbj
            $("#billCycle").val(gridData.billCycle);
            if (gridData.billCycleType == 1) {
                $("#billCycleType1").prop("checked",true);
            } else {
                $("#billCycleType2").prop("checked",true);
            }
        });

        //调用checkbox方法
        $("#state").bootstrapSwitch();

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_storeName'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
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