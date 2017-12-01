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
        columnClass: 'text-center width-250',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var _content = '<a href="#editModalNotice" class="btn btn-sm btn-primary m-r-10" data-toggle="modal" data-no="' + dataNo + '"><i class="fa fa-volume-down"></i>&nbsp;站内信&nbsp;</a><a href="#editModalSms" class="btn btn-sm btn-success m-r-10" data-toggle="modal" data-no="' + dataNo + '"><i class="fa fa-mobile"></i>&nbsp;短信&nbsp;</a><a href="#editModalEmail" class="btn btn-warning btn-sm" data-toggle="modal" data-no="' + dataNo + '"><i class="fa fa-envelope-o"></i>&nbsp;邮件&nbsp;</a>';
            return _content;
        }
    },
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL : ncGlobal.adminRoot + 'message_template/common/list.json',
    exportFileName: '商品规格列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools : 'refresh|fastQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {
        int: ["tplType", "smsState", "emailState"]

    },
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_tplType'] = 2;
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
        /**
         * 编辑站内信
         */
        $("#editModalNotice").on("show.bs.modal", function (e) {
            //获取接受事件的元素
            //获取data 参数
            var datano = $(e.relatedTarget).data('no'),
            //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];
            $('#tplCodeNotice').val(gridData.tplCode);
            $('#tplNameNotice').val(gridData.tplName);
            $('#noticeContent').val(gridData.noticeContent);
        });

        /**
         * 编辑短信
         */
        $("#editModalSms").on("show.bs.modal", function(e){
            var dataNo = $(e.relatedTarget).data('no'),
                gridData = grid.sortOriginalDatas[dataNo];
            $('#tplCodeSms').val(gridData.tplCode);
            $('#tplNameSms').val(gridData.tplName);
            if (gridData.smsState == 1) {
                $("#smsState").bootstrapSwitch('state', true);
            } else {
                $("#smsState").bootstrapSwitch('state', false);
            }
            $('#smsContent').val(gridData.smsContent);
        });

        /**
         * 编辑短信
         */
        $("#editModalEmail").on("show.bs.modal", function(e){
            var dataNo = $(e.relatedTarget).data('no'),
                gridData = grid.sortOriginalDatas[dataNo];
            $('#tplCodeEmail').val(gridData.tplCode);
            $('#tplNameEmail').val(gridData.tplName);
            if (gridData.emailState == 1) {
                $("#emailState").bootstrapSwitch('state', true);
            } else {
                $("#emailState").bootstrapSwitch('state', false);
            }
            $('#emailTitle').val(gridData.emailTitle);
            message_template.ueEdit.setContent(gridData.emailContent);
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
    message_template.ueEdit= UE.getEditor('contentEdit',{textarea:'emailContent'});
    message_template.init();
});