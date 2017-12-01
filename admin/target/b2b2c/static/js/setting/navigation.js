/**
 * Created by shopnc on 2015/11/26.
 */
//定义表格
var dtGridColumns = [{
    id: 'sort',
    title: '排序',
    type: 'number',
    headerClass: 'text-center width-100',
    columnClass: 'text-center width-100',
    hideType: 'md|sm|xs'
}, {
    id: 'title',
    title: '导航标题',
    type: 'string',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastSort: false,
    fastQuery: true,
    fastQueryType: 'lk',
}, {
    id: 'positionId',
    title: '显示位置',
    type: 'string',
    headerClass: 'text-center width-150',
    columnClass: 'text-center width-150',
    fastSort: false,
    fastQuery: true,
    fastQueryType: 'eq',
    hideType: 'md|sm|xs',
    codeTable: {1:"顶部导航",2:"中部主导航",3:"底部导航"}
}, {
    id: 'openType',
    title: '新窗口打开',
    type: 'string',
    headerClass: 'text-center width-100',
    columnClass: 'text-center width-100',
    fastSort: false,
    hideType: 'md|sm|xs',
    codeTable: {0:"否",1:"是"}
},{
    id: 'operation',
    title: '管理操作',
    type: 'string',
    columnClass: 'text-center width-200',
    fastSort: false,
    extra: false,
    resolution: function(value, record, column, grid, dataNo, columnNo) {
        content = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            content += "<a href='javascript:;' class='btn btn-danger btn-sm' onclick='navigation.delNavigation(" + record.navId + ",\"" + record.title + "\")'>" + "<i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
        return content;
    }
}];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'navigation/list.json',
    exportFileName: '管理导航列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType:{int:["positionId"]}
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'navId';
grid.sortParameter.sortType = 1;
var navigation = function() {
    //删除URL
    var delUrl = ncGlobal.adminRoot + "navigation/del";

    /**
     * 删除
     */
    function _delNavigation(id, content) {
        var tpl = '您选择对导航 <strong>' + content + '</strong> 进行删除操作，删除后将无法恢复。<br/>您确定要进行删除操作吗?'
        $.ncConfirm({
            url: delUrl,
            data: {
                navId: id
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
            grid.fastQueryParameters['lk_title'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
        //新增分类
        $("#addForm").on("nc.formSubmit.success", function(e) {
                $("#addForm").psly().reset();
                modal = $(this);
                modal.find('input[name="title"]').val("");
                modal.find('input[name="url"]').val("");
                modal.find('input[name="openType"]').bootstrapSwitch('state', true);
                modal.find('select[name="positionId"]').val("1");
            })
            //编辑对话框显示时调用
        $('#editModal').on('show.bs.modal', function(event) {
            var //获取接受事件的元素
                button = $(event.relatedTarget),
                //获取data 参数
                datano = button.data('no'),
                modal = $(this),

                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano],
                editForm = $("#editForm");
                modal.find('input[name="title"]').val(gridData.title);
                modal.find('input[name="sort"]').val(gridData.sort);
                modal.find('input[name="url"]').val(gridData.url);
                modal.find('input[name="navId"]').val(gridData.navId);
                modal.find('select[name="positionId"]').val(gridData.positionId);
                if (gridData.openType) {
                    modal.find('input[name="openType"]').bootstrapSwitch('state', true);
                } else {
                    modal.find('input[name="openType"]').bootstrapSwitch('state', false);
                }
                //清除错误提示
            editForm.psly().reset();
            $(".alert-danger").remove()
        })
        //调用checkbox方法
        $("input[name='openType']").bootstrapSwitch('state', true);
    }

    //外部可调用
    return {
        init: function() {
            _bindEvent();
        },
        delNavigation: _delNavigation
    }
}()
$(function() {
    grid.load();
    navigation.init();
});