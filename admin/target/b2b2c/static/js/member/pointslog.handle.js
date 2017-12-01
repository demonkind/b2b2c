/**
 * Created by zxy on 2015-12-01
 */
//列表开始
var dtGridColumns = [
    {
        id: 'logId',
        title: '日志ID',
        type: 'number',
        columnClass: 'text-center width-100'
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
        id: 'points',
        title: '积分',
        type: 'string',
        columnClass: 'text-center width-100'
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
        hideType: 'xs'
    },
    {
        id: 'operationStage',
        title: '操作阶段',
        type: 'string',
        columnClass: 'text-center width-150',
        fastQuery: true,
        fastQueryType:"eq",
        codeTable: {
            login: $lang.pointslog.operationStageLogin,
            comments: $lang.pointslog.operationStageComments,
            orders: $lang.pointslog.operationStageOrders,
            register: $lang.pointslog.operationStageRegister,
            admin: $lang.pointslog.operationStageAdmin
        },
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var content = "";
            switch (value){
                case "login":
                    content = $lang.pointslog.operationStageLogin;
                    break;
                case "comments":
                    content = $lang.pointslog.operationStageComments;
                    break;
                case "orders":
                    content = $lang.pointslog.operationStageOrders;
                    break;
                case "register":
                    content = $lang.pointslog.operationStageRegister;
                    break;
                case "admin":
                    content = $lang.pointslog.operationStageAdmin;
                    break;
            }
            return content;
        }
    },
    {
        id: 'description',
        title: '操作描述',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk',
        hideType: 'xs|sm'
    },
    {
        id: 'adminName',
        title: '管理员',
        type: 'string',
        columnClass: 'text-center width-100'
    }
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'points/pointslog.json',
    exportFileName: '积分明细',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {Timestamp: ["addTime"]}
};
var grid = $.fn.DtGrid.init(dtGridOption);
//排序
grid.sortParameter.columnId = 'logId';
grid.sortParameter.sortType = 1;
//列表结束
//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_description'] = $('#keyword').val();
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