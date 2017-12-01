
var dtGridColumns = [
    {
        id: 'clogStorename',
        title: '店铺名称',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'md|sm|xs',
        fastSort: false
    },
    {
        id: 'clogItemname',
        title: '保障服务',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
		hideType: 'md|sm|xs',
        fastQuery: false,
        fastQueryType: 'lk'
    },
    {
        id: 'clogPrice',
        title: '金额（元）',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left f-w-600 text-warning',
        fastQuery: false,
        fastSort: true
    },
    {
        id: 'clogAddtime',
        title: '操作时间',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
		hideType: 'md|sm|xs',
        fastQuery: false,
        fastSort: true
    },
    {
        id: 'clogAdminname',
        title: '操作人',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastQuery: false,
        fastSort: false
    },
    {
        id: 'clogDesc',
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
    loadURL: ncGlobal.adminRoot + 'contract/contract_cost_list',
    exportFileName: '保证金日志列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10,20,50],
    ncColumnsType: {
        int: ["clogItemid", "clogStoreid"]
    },
    onGridComplete: function (grid) {
    	
    }
};
var grid = $.fn.DtGrid.init(dtGridOption);

$(function () {
	var itemId=$("#itemId").val();
	var storeId=$("#storeId").val();
	grid.fastQueryParameters = new Object();
    grid.fastQueryParameters['eq_clogItemid'] = itemId;
    grid.fastQueryParameters['eq_clogStoreid'] = storeId;
    grid.sortParameter.columnId = 'clogAddtime';
    grid.sortParameter.sortType = 1;
	
    grid.load();
});
