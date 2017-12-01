/**
 * Created by shopnc.feng on 2015-11-25.
 */

var dtGridColumns = [
    {
        id: 'brandLabelName',
        title: '标签名称',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'brandLabelSort',
        title: '排序',
        type: 'string',
        columnClass: 'text-center width-100',
        fastQuery: false,
        fastQueryType: 'eq',
        hideType: 'xs'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='"
                + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>"
                + "<a href='javascript:;' class='btn btn-sm btn-danger' onclick='brandLabel.delBrandLabel(" + record.brandLabelId + ",\"" + record.brandLabelName + "\")'>" +
                "<i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
        }
    },
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'brand_label/list.json',
    exportFileName: '品牌标签列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50]
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'brandLabelId';
grid.sortParameter.sortType = 1;
/**
 * bycj [ 品牌标签所使用的js ]
 */
var brandLabel = function () {
    var //标签删除地址
        delUrl = ncGlobal.adminRoot + "brand_label/delete.json";

    /**
     * 事件绑定
     * @private
     */
    function _bindEvent() {
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_brandLabelName'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
        // bycj [ 编辑对话框显示时调用 ]
        $('#editModal').on('show.bs.modal', function (event) {
            var    //获取接受事件的元素
                button = $(event.relatedTarget),
            //获取data 参数
                datano = button.data('no'),
                modal = $(this),
            //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano],
                editForm = $("#editForm")
                ;
            modal.find('input[name="brandLabelName"]').val(gridData.brandLabelName)
            modal.find('input[name="brandLabelSort"]').val(gridData.brandLabelSort)
            modal.find('input[name="brandLabelId"]').val(gridData.brandLabelId)
            //清除错误提示
            editForm.psly().reset();
            $(".alert-danger").remove()
        })
        // bycj [ 新增对话框时调用 ]
        // $('#addModal').on('show.bs.modal', function (event) {
         $('#addForm').on('nc.formSubmit.success', function (event) {
            var addForm = $("#addForm"),
                brandLabelName = addForm.find("input[name='brandLabelName']"),
                brandLabelSort = addForm.find("input[name='brandLabelSort']");

            brandLabelName.val('');
            brandLabelSort.val('0')
            //清除错误提示
            addForm.psly().reset();
            $(".alert-danger").remove()
        })
    }

    /**
     * 删除标签
     */
    function _delBrandLabel(id, brandName) {
        var tpl = '您选择对品牌标签 <strong>' + brandName + '</strong> 进行删除操作，删除标签将影响品牌设置选项及商城前台品牌展示索引。<br/>您确定要进行删除操作吗?'
        $.ncConfirm({
            url: delUrl,
            data: {
                brandLabelId: id
            },
            content: tpl
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delBrandLabel: _delBrandLabel
    }
}()
/**
 * 事件初始化
 */
$(function () {
    grid.load();
    //页面绑定事件
    brandLabel.bindEvent();
});
