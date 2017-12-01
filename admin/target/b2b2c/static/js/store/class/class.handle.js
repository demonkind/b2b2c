/**
 * Created by dqw on 2015/12/11.
 */

//列表开始
var dtGridColumns = [
    {
        id: 'id',
        title: '编号',
        type: 'number',
        columnClass: 'text-center width-100'
    },
    {
        id: 'name',
        title: '主营行业名称',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: false
    },
    {
        id: 'bail',
        title: '保证金',
        type: 'number',
        columnClass: 'text-center width-200',
        hideType: 'xs|sm',
        fastSort: false
    },
    {
        id: 'sort',
        title: '排序',
        type: 'number',
        columnClass: 'text-center width-100',
        hideType: 'xs|sm|md',
        fastSort: false
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a data-target='#addModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-type='edit' data-no='"
                + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>"
                + "<a href='javascript:;' class='btn btn-danger btn-sm' onclick='OperateHandle.delClass("
                + record.id + ")' ><i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'store_class/list.json',
    exportFileName: '主营行业列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["bail", "sort"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = 'id';
grid.sortParameter.sortType = 0;
//列表结束

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        //新增对话框初始化
        $("#addModal").on("show.bs.modal", function (event) {
            console.log("aaa");
            //清除错误信息
            $(".alert-danger").remove();
            $("#addForm").psly().reset();

            //获取接受事件的元素
            var button = $(event.relatedTarget);

            //获取data 参数
            var datano = button.data('no');
            var dataType = button.data('type');
            if(dataType) {
                $("#modalTitle").text("编辑主营行业");
                var gridData = grid.sortOriginalDatas[datano];
                $("#classId").val(gridData.id);
                $("#addForm").find("[name='name']").val(gridData.name);
                $("#addForm").find("[name='bail']").val(gridData.bail);
                $("#addForm").find("[name='sort']").val(gridData.sort);
            } else {
                $("#modalTitle").text("新增主营行业");
                $("#classId").val("0");
                $("#addForm").find("[name='name']").val("");
                $("#addForm").find("[name='bail']").val("");
                $("#addForm").find("[name='sort']").val("");
            }
        });

        //调用checkbox方法
        $("#allowLogin").bootstrapSwitch();

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
     * 删除
     */
    function _delClass(id) {
        var tpl = '您确定要删除该主营行业吗?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "store_class/del.json",
            data: {
                classId: id
            },
            content: tpl
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delClass: _delClass
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});