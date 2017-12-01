/**
 * Created by shopnc on 2016/1/26.
 */
//定义表格
var dtGridColumns = [{
    id: 'reasonSort',
    title: '排序',
    type: 'string',
    headerClass: 'text-center width-100',
    columnClass: 'text-center width-100',
    fastSort: true
}, {
    id: 'reasonInfo',
    title: '原因',
    type: 'string',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastSort: true,
    fastQuery: true,
    fastQueryType: 'eq'
}, {
    id: 'operation',
    title: '管理操作',
    type: 'string',
    columnClass: 'text-center width-200',
    fastSort: false,
    resolution: function (value, record, column, grid, dataNo, columnNo) {
        var content = '';
        content +=
            '<a data-toggle="modal" href="#editModal" class="btn btn-primary btn-sm m-r-10" data-no="' + dataNo + '" ><i class="fa fa-gears"></i>&nbsp;编辑&nbsp;</a>' + '</div>';
        if (record.reasonId > 5) {
            content += "<a href='javascript:;' class='btn btn-sm btn-danger' onclick='reason.del(" + record.reasonId + ")'><i class='fa fa-trash-o'></i>删除</a>";
        }
        return content;
    }
}];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'refund/reason/list.json',
    exportFileName: '退款退货原因列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: '',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    onGridComplete: function (grid) {

    }
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.sortType = 1;

var reason = function () {
    /**
     * 绑定事件
     * @private
     */
    function _bindEvent() {
        //模糊查询
        $("#customSearch").click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_reasonInfo'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
        //添加 modal显示时
        $('#addModal').on('show.bs.modal', function (event) {
            $("#addForm").psly().reset();
            $(".alert-danger").remove()
        });
        //编辑modal 显示时
        $('#editModal').on('show.bs.modal', function (event) {
            var
                button = $(event.relatedTarget),
                datano = button.data('no'),
                gridData = grid.sortOriginalDatas[datano],
                editForm = $("#editForm");
            $("#editReasonId").val(gridData.reasonId);
            $("#editReasonInfo").val(gridData.reasonInfo);
            $("#editReasonSort").val(gridData.reasonSort);
            editForm.psly().reset();
            $(".alert-danger").remove()
        })
        //添加的form 成功返回时
        $("#addForm").on("nc.formSubmit.success", function () {
            $("#addReasonInfo").val('');
            $("#addReasonSort").val('');
            $("#addForm").psly().reset();
            $(".alert-danger").remove()
        })


    }

    //外部可调用
    return {
        init: function () {
            _bindEvent();
        },
        //删除
        del: function (reasonId) {

            $.ncConfirm({
                url: ncGlobal.adminRoot + "refund/reason/del",
                data: {
                    reasonId: reasonId
                }
            });

        }
    }
}()
$(function () {
    grid.load();
    reason.init();
});