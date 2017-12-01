/**
 * Created by shopnc.feng on 2016-01-29.
 */
var dtGridColumns = [
    {
        id: 'tplCode',
        title: '模板编号',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'tplName',
        title: '模板名称',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'sm|xs',
        fastQuery: false,
        fastQueryType: 'eq',
        fastSort: false
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-100',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var _dataTarget = "#editModal";
            if (record.sendType == 1) {
                _dataTarget = "#editModal1";
            }
            var _content = "<a data-target='" + _dataTarget + "' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='"
                + dataNo + "'><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            return _content;
        }
    },
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL : ncGlobal.adminRoot + 'message_template/list.json',
    exportFileName: '商品规格列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools : 'refresh|fastQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50]
};
var grid = $.fn.DtGrid.init(dtGridOption);
var message_template = function() {
    //编辑器对象
    var _ueEdit;
    var _bindEven = function() {
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_tplName'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
        $("#editModal").on("show.bs.modal", function (e) {
            //获取接受事件的元素
            //获取data 参数
            var datano = $(e.relatedTarget).data('no'),
            //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];
            $('#sendType').val(gridData.sendType);
            $('#tplCode').val(gridData.tplCode);
            $('#tplName').val(gridData.tplName);
            $('#tplTitle').val(gridData.tplTitle);
            message_template.ueEdit.setContent(gridData.tplContent);
        });

        $("#editModal1").on("show.bs.modal", function (e) {
            //获取接受事件的元素
            //获取data 参数
            var datano = $(e.relatedTarget).data('no'),
            //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];
            $('#sendType1').val(gridData.sendType);
            $('#tplCode1').val(gridData.tplCode);
            $('#tplName1').val(gridData.tplName);
            $('#tplContent1').val(gridData.tplContent);
        });
    }
    return {
        init : function() {
            _bindEven();
        },
        ueEdit : _ueEdit
    }
}();
$(function() {
    grid.load();
    message_template.ueEdit= UE.getEditor('contentEdit',{textarea:'tplContent'});
    message_template.init();
});