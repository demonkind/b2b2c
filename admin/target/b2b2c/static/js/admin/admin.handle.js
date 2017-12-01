/**
 * Created by dqw on 2015/12/30.
 */

//列表开始
var dtGridColumns = [
    {
        id: 'adminId',
        title: '编号',
        type: 'number',
        columnClass: 'text-center width-100',
        fastSort: false
    },
    {
        id: 'name',
        title: '登录名',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left width-200',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'groupName',
        title: '权限组',
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
            if(record.groupId > 0) {
                return "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='"
                + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>"
                + "<a href='javascript:;' class='btn btn-danger btn-sm' onclick='OperateHandle.delAdmin("
                + record.adminId + ")' ><i class='fa fa-edit'></i>&nbsp;删除&nbsp;</a>";
            } else {
                return ""
            }
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'admin/list.json',
    exportFileName: '管理员列表',
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

        //新增对话框初始化
        $("#addModal").on("show.bs.modal", function (event) {
            //清除错误信息
            $(".alert-danger").remove();
            $("#addForm").psly().reset();

            $("#addForm").find("[name='name']").val("");
            $("#addForm").find("[name='password']").val("");
            $("#addForm").find("[name='password2']").val("");
            $("#addForm").find("[name='groupId']").val("");
        });

        //编辑对话框初始化
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

            $("#adminId").val(gridData.adminId);
            $("#adminName").val(gridData.name);
            modal.find("[name='password']").val("");
            modal.find("[name='password2']").val("");
            modal.find("[name='groupId']").val(gridData.groupId);
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
    function _delAdmin(id) {
        var tpl = '您确定要删除该管理员吗?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "admin/del.json",
            data: {
                adminId: id
            },
            content: tpl
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delAdmin: _delAdmin
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});