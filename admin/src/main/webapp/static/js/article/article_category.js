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
    title: '文章分类',
    type: 'string',
    headerClass: 'text-left width-250',
    columnClass: 'text-left width-250',
    fastSort: false,
    fastQuery: true,
    fastQueryType: 'lk',
}, {
    id: 'positionTitle',
    title: '显示位置',
    type: 'string',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastSort: false,
    hideType: 'md|sm|xs'
},
    {
        id: 'type',
        title: '是否可删除',
        type: 'string',
		 headerClass: 'text-center width-150',
        columnClass: 'text-center width-150',
        fastSort: false,
        fastQuery: false,
        fastQueryType: 'eq',
        codeTable:{
            1:$lang.article.allowDelete0,
            2:$lang.article.allowDelete0,
            3:$lang.article.allowDelete1
        },
        hideType:'md|sm|xs'
    },
    {
    id: 'operation',
    title: '管理操作',
    type: 'string',
    columnClass: 'text-center width-200',
    fastSort: false,
    resolution: function(value, record, column, grid, dataNo, columnNo) {
        content = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
        content += "<a href='javascript:;' class='btn btn-danger btn-sm' onclick='articleCategory.delArticleCategory(" + record.categoryId + ",\"" + record.title + "\")'>" + "<i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
        return content;
    }
}];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'article_category/list.json',
    exportFileName: '管理文章分类列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50]
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'categoryId';
grid.sortParameter.sortType = 1;
var articleCategory = function() {
    //删除URL
    var delUrl = ncGlobal.adminRoot + "article_category/delete";

    /**
     * 删除
     */
    function _delArticleCategory(id, content) {
        var tpl = '您选择对文章分类 <strong>' + content + '</strong> 进行删除操作，删除后将无法恢复。<br/>您确定要进行删除操作吗?'
        $.ncConfirm({
            url: delUrl,
            data: {
                categoryId: id
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
                modal = $(this),
                    modal.find('input[name="title"]').val("")
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
            modal.find('input[name="title"]').val(gridData.title)
            modal.find('input[name="sort"]').val(gridData.sort)
            modal.find('input[name="categoryId"]').val(gridData.categoryId),
                modal.find('select[name="positionId"]').val(gridData.positionId),
                modal.find('input[name="parentTitle"]').val(gridData.parentTitle)
                //清除错误提示
            editForm.psly().reset();
            $(".alert-danger").remove()
        })
    }

    //外部可调用
    return {
        init: function() {
            _bindEvent();
        },
        delArticleCategory: _delArticleCategory
    }
}()
$(function() {
    grid.load();
    articleCategory.init();
});