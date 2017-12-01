
var dtGridColumns = [
    {
        id: 'logStorename',
        title: '店铺名称',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'md|sm|xs',
        fastSort: false
    },
    {
        id: 'logItemname',
        title: '保障服务',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
		hideType: 'md|sm|xs',
        fastQuery: false,
        fastQueryType: 'lk',
    },
    {
        id: 'logAddtime',
        title: '添加时间',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
		hideType: 'md|sm|xs',
        fastQuery: false,
        fastSort: true
    },
    {
        id: 'operater',
        title: '操作人',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastQuery: false,
        fastSort: false
    },
    {
        id: 'logMsg',
        title: '描述',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastQuery: false,
        fastSort: false
    }
   
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'contract/contract_detail_list',
    exportFileName: '保障服务日志列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10,20,50],
    ncColumnsType: {
        int: ["logItemid", "logStoreid"]
    },
    onGridComplete: function (grid) {
    	
    }
};
var grid = $.fn.DtGrid.init(dtGridOption);

$(function () {
	var itemId=$("#itemId").val();
	var storeId=$("#storeId").val();
	grid.fastQueryParameters = new Object();
    grid.fastQueryParameters['eq_logItemid'] = itemId;
    grid.fastQueryParameters['eq_logStoreid'] = storeId;
    grid.sortParameter.columnId = 'logAddtime';
    grid.sortParameter.sortType = 1;
	
    grid.load();
});
