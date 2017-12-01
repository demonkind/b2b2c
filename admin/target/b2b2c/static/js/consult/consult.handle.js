/**
 * Created by zxy on 2016-01-12.
 */
//列表开始
var dtGridColumns = [
    {
        id: 'memberName',
        title: '咨询人',
        type: 'string',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            if (!value) {
                return "游客";
            }else{
                return value;
            }
        }
    },
    {
        id: 'consultContent',
        title: '咨询内容',
        type: 'string',
        columnClass: 'text-left',
        fastSort: false
    },
    {
        id: 'consultReply',
        title: '回复内容',
        type: 'string',
        columnClass: 'text-left',
        fastSort: false,
        hideType: 'xs|sm|md|lg'
    },
    {
        id: 'goodsName',
        title: '咨询商品',
        type: 'string',
        columnClass: 'text-left',
        fastSort: false,
        hideType: 'xs|sm|md|lg',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a href='"+ncGlobal.webRoot+"goods/"+record.goodsId+"' target='_blank'>"+value+"</a>";
        }
    },
    {
        id: 'addTime',
        title: '咨询时间',
        type: 'date',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
		hideType: 'xs|sm|md',
        fastSort: false
    },
    {
        id: 'replyTime',
        title: '回复时间',
        type: 'date',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
		hideType: 'xs|sm|md',
        fastSort: false
    },
    {
        id: 'storeName',
        title: '商家名称',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
		hideType: 'xs|sm',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a href='"+ncGlobal.webRoot+"store/"+record.storeId+"' target='_blank'>"+value+"</a>";
        }
    },
    {
        id: 'className',
        title: '咨询类型',
        type: 'string',
        columnClass: 'text-left',
        fastSort: false,
        hideType: 'xs|sm|md|lg'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-100',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html = "";
            html += "<a href='javascript:;' class='btn btn-sm btn-danger' onclick='OperateHandle.delConsult(" + record.consultId + ")'>" + "<i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
            return html;
        }
    }
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'consult/list.json',
    exportFileName: '咨询列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50]
};
var grid = $.fn.DtGrid.init(dtGridOption);
//排序
grid.sortParameter.columnId = 'consultId';
grid.sortParameter.sortType = 1;
//列表结束
//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_consultContent'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    //删除记录
    function _delConsult(consultId){
        $.ncConfirm({
            url: ncGlobal.adminRoot + "consult/del",
            data: {
                consultId: consultId
            }
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delConsult: _delConsult
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});