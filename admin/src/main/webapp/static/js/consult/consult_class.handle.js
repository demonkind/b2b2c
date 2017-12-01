/**
 * Created by zxy on 2016-01-12.
 */
//列表开始
var dtGridColumns = [
    {
        id: 'className',
        title: '类型名称',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: false
    },
    {
        id: 'classSort',
        title: '排序',
        type: 'number',
        columnClass: 'text-center width-100',
        fastSort: false
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html = "";
            html += "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            html += "<a href='javascript:;' class='btn btn-sm btn-danger' onclick='OperateHandle.delConsultClass(" + record.classId + ")'>" + "<i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
            return html;
        }
    }
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'consult/class.json',
    exportFileName: '类型列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50]
};
var grid = $.fn.DtGrid.init(dtGridOption);
//排序
grid.sortParameter.columnId = 'classSort';
grid.sortParameter.sortType = 2;
//列表结束
//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {
        //新增对话框初始化
        $("#addModal").on("show.bs.modal", function (event) {
            //清除错误信息
            $(".alert-danger").remove();
            $("#addForm").psly().reset();
            //清空控件
            $("#addForm").find("[name='className']").val("");
            $("#addForm").find("[name='classSort']").val("");
            ueEditorAdd.execCommand('cleardoc');
        });
        //编辑对话框初始化
        $("#editModal").on("show.bs.modal", function (event) {
            //获取接受事件的元素
            var button = $(event.relatedTarget);
            //获取data 参数
            var datano = button.data('no');
            var modal = $(this);
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];
            var editForm = $("#editForm");
            modal.find('input[name="classId"]').val(gridData.classId);
            modal.find('input[name="className"]').val(gridData.className);
            modal.find('input[name="classSort"]').val(gridData.classSort);
            ueEditorEdit.execCommand('cleardoc');
            ueEditorEdit.execCommand('insertHtml', gridData.introduce);
            //清除错误信息
            $(".alert-danger").remove();
            $("#editForm").psly().reset();
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_className'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    //删除记录
    function _delConsultClass(classId){
        var tpl = '您确定要删除该咨询类型吗?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "consult/class/del",
            data: {
                classId: classId
            },
            content: tpl
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delConsultClass: _delConsultClass
    }
}();
//操作处理结束
var ueEditorAdd,ueEditorEdit;
$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
    //编辑器
    ueEditorAdd = UE.getEditor('contentEditorAdd',{textarea:'introduce'});
    ueEditorEdit = UE.getEditor('contentEditorEdit',{textarea:'introduce'});
});