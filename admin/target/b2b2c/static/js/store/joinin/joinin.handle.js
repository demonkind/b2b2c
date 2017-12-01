/**
 * Created by dqw on 2015/12/11.
 */

//列表开始
var dtGridColumns = [
    {
        id: 'sellerName',
        title: '商家用户名',
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
        columnClass: 'text-left',
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'joininSubmitTime',
        title: '申请时间',
        type: 'date',
        format: 'yyyy-MM-dd',
		headerClass: 'text-left',
        columnClass: 'text-left width-200',		
        hideType: 'xs|sm|md',
        fastSort: false
    },
    {
        id: 'state',
        title: '申请状态',
        type: 'number',
        columnClass: 'text-center width-150',
		hideType: 'xs|sm',
        codeTable: {
            10: $lang.storeJoinin.s10,
            15: $lang.storeJoinin.s15,
            20: $lang.storeJoinin.s20,
            30: $lang.storeJoinin.s30,
            35: $lang.storeJoinin.s35,
            90: $lang.storeJoinin.s90
        }
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var btnText = "<i class='fa fa-eye'></i>&nbsp;查看";
            var url = ncGlobal.adminRoot + 'store_joinin/detail/' + record.sellerId
            if(record.state == 10 || record.state == 30) {
              btnText = "<i class='fa fa-check'></i>&nbsp;审核";
            }
            var btn = "<a href='" + url + "' class='btn btn-sm btn-primary m-r-10' >" + btnText + "&nbsp;</a>";
            if(record.state != 90) {
              btn +="<a href='javascript:;' class='btn btn-danger btn-sm' onclick='OperateHandle.delStoreJoinin(" + record.sellerId+ ")' ><i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
            }
            return btn;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'store_joinin/list.json',
    exportFileName: '入驻申请列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {Timestamp:["joininSubmitTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = 'id';
grid.sortParameter.sortType = 1;
//列表结束

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_storeName'] = $('#keyword').val();
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
    function _delStoreJoinin(id) {
        var tpl = '您确定要删除该入驻申请吗?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "store_joinin/delete.json",
            data: {
                sellerId: id
            },
            content: tpl
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delStoreJoinin: _delStoreJoinin
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});