/**
 * Created by zxy on 2015-12-28
 */
//列表开始
var dtGridColumns = [
    {
        id: 'logId',
        title: '日志ID',
        type: 'number',
        columnClass: 'text-center width-100'
    },
    {
        id: 'memberId',
        title: '会员ID',
        type: 'number',
		headerClass: 'text-left width-100',
        columnClass: 'text-left width-100',
		hideType:'lg|md|sm|xs'
    },
    {
        id: 'memberName',
        title: '会员名称',
        type: 'string',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        fastSort: false
    },
    {
        id: 'availableAmount',
        title: '可用金额（元）',
        type: 'number',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150 f-w-600 text-warning',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var tpl = "<span class='{a}'>{b}</span>";
            var className = "";
            var valueText = Nc.priceFormat(value,'%s');
            if (value<0) {
                valueText = Nc.priceFormat(value,'%s');
                className = "text-success";
            }else if(value>0){
                valueText = Nc.priceFormat(value,'%s');
                className = "text-danger";
            }
            return $.ReplaceTpl(tpl, {
                a: className + "",
                b: valueText + ""
            });
        }
    },
    {
        id: 'freezeAmount',
        title: '冻结金额（元）',
        type: 'number',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150 f-w-600 text-warning',
        format: '###.00',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var tpl = "<span class='{a}'>{b}</span>";
            var className = "";
            var valueText = Nc.priceFormat(value,'%s');
            if (value<0) {
                valueText = Nc.priceFormat(value,'%s');
                className = "text-success";
            }else if(value>0){
                valueText = Nc.priceFormat(value,'%s');
                className = "text-danger";
            }
            return $.ReplaceTpl(tpl, {
                a: className + "",
                b: valueText + ""
            });
        }
    },
    {
        id: 'addTime',
        title: '添加时间',
        type: 'date',
		headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        fastQuery: true,
        fastQueryType: 'range',
        format: 'yyyy-MM-dd',
        hideType: 'xs'
    },
    {
        id: 'description',
        title: '日志描述',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'adminId',
        title: '管理员编号',
        type: 'number',
        columnClass: 'text-center width-100',
        hideType:'lg|md|sm|xs',
        fastSort: false
    },
    {
        id: 'adminName',
        title: '管理员',
        type: 'string',
        columnClass: 'text-center width-100',
		hideType:'lg|md|sm|xs',
        fastSort: false
    }
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'predeposit/log.json',
    exportFileName: '预存款明细',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["memberId"],Timestamp: ["addTime"]}
};
var grid = $.fn.DtGrid.init(dtGridOption);
//排序
grid.sortParameter.columnId = 'logId';
grid.sortParameter.sortType = 1;
//列表结束
//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            //查询会员详情，构造参数
            var memberName = $('#keyword').val();
            if (memberName) {
                var memberId = 0;
                var params = {"memberName":memberName};
                $.ajax({
                    type : "get",
                    url : ncGlobal.adminRoot + "common/member/getid",
                    data : params,
                    async : false,
                    success : function(xhr){
                        if(xhr.code == 200) {
                            memberId = xhr.data;
                        }
                    }
                });
                grid.fastQueryParameters['eq_memberId'] = memberId;
            }
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});