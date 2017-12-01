/**
 * Created by dqw on 2015/12/30.
 */

//列表开始
var dtGridColumns = [
    {
        id: 'groupId',
        title: '编号',
        type: 'number',
        columnClass: 'text-center width-100',
        fastSort: false
    },
    {
        id: 'groupName',
        title: '权限组名称',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a href='" + ncGlobal.adminRoot + "admin_group/edit/?groupId=" + record.groupId + "' class='btn btn-sm btn-primary m-r-10' data-toggle='modal'><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>"
            + "<a href='javascript:;' class='btn btn-danger btn-sm' onclick='OperateHandle.delGroup("
            + record.groupId + ")' ><i class='fa fa-edit'></i>&nbsp;删除&nbsp;</a>";
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'admin_group/list.json',
    exportFileName: '权限组列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = 'adminId';
grid.sortParameter.sortType = 0;
//列表结束

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        //新增
        $("#addModal").on("show.bs.modal", function (event) {
            Nc.go(ncGlobal.adminRoot + "admin_group/add");
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_name'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    /**
     * 删除入驻申请
     */
    function _delGroup(id) {
        var tpl = '您确定要删除该权限组吗?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "admin_group/del.json",
            data: {
                groupId: id
            },
            content: tpl
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delGroup: _delGroup
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});