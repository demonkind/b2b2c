/**
 * Created by dqw on 2015/12/01.
 */

//列表开始
var dtGridColumns = [
    {
        id: 'sellerId',
        title: '商家ID',
        type: 'number',
        columnClass: 'text-center width-100'		
    },
    {
        id: 'sellerName',
        title: '商家名称',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left width-150',
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'storeName',
        title: '店铺名称',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left width-200 ',
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'sellerEmail',
        title: '商家会员邮箱',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'xs|sm',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'joinDate',
        title: '注册日期',
        type: 'date',
		headerClass: 'text-left',
        columnClass: 'text-left width-150',
        hideType: 'xs|sm|md',
        fastQuery: true,
        fastQueryType: 'range',
        format: 'yyyy-MM-dd'
    },
    {
        id: 'lastLoginTime',
        title: '最后登录日期',
        type: 'date',
		headerClass: 'text-left',
        columnClass: 'text-left width-150',
        hideType: 'xs|sm|md',
        fastQuery: true,
        fastQueryType: 'range',
        format: 'yyyy-MM-dd'
    },
    {
        id: 'allowLogin',
        title: '登录状态',
        type: 'string',
        columnClass: 'text-center width-100',
		hideType: 'xs|sm',
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.seller.allowLogin,
            0: $lang.seller.notAllowLogin
        }
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-150',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a data-target='#editModal' class='btn btn-sm btn-primary' data-toggle='modal' data-no='"
                + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>"
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'seller/list.json',
    exportFileName: '商家列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["allowLogin"], Timestamp: ["joinDate", "lastLoginTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = 'sellerId';
grid.sortParameter.sortType = 1;
//列表结束

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        //新增对话框初始化
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

            //清除错误信息
            $(".alert-danger").remove();
            $("#editForm").psly().reset();
            //清空控件
            $("#sellerId").val(gridData.sellerId);
            $("#editForm").find("#sellerName").val(gridData.sellerName);
            $("#editForm").find("[name='sellerPassword']").val("");
            $("#editForm").find("[name='sellerPassword2']").val("");
            $("#editForm").find("[name='sellerEmail']").val(gridData.sellerEmail);
            if(gridData.allowLogin == 1) {
                $("#allowLogin").bootstrapSwitch('state', true);
            } else {
                $("#allowLogin").bootstrapSwitch('state', false);
            }
        });

        //调用checkbox方法
        $("#allowLogin").bootstrapSwitch();

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_sellerName'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    function _buildHTML() {
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        buildHTML: _buildHTML
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
    OperateHandle.buildHTML();

});