
var dtGridColumns = [
   
	 {
        id: 'storeName',
        title: '被评商家',
        type: 'string',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        hideType: 'sm|xs',
        fastQuery: false,
        fastSort: false,
        fastQueryType: 'lk'
    },
    {
        id: 'memberName',
        title: '评价人',
        type: 'string',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        hideType: 'sm|xs',
        fastQuery: true,
        fastSort: false,
        fastQueryType: 'lk'
    },
    {
        id: 'descriptionCredit',
        title: '描述相符',
        type: 'string',
        headerClass: 'text-center width-150',
        columnClass: 'text-center width-150',
        fastQuery: false,
        fastQueryType: 'eq',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
        	var content="<em class=\"raty\" data-score=\""+value+"\" style=\"width: 100px;\"></em>";
        	return content;
        }
    },
    {
        id: 'serviceCredit',
        title: '服务态度',
        type: 'string',
        headerClass: 'text-center width-150',
        columnClass: 'text-center width-150',
        fastQuery: false,
        fastQueryType: 'eq',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
        	var content="<em class=\"raty\" data-score=\""+value+"\" style=\"width: 100px;\"></em>";
        	return content;
        }
    },
    {
        id: 'deliveryCredit',
        title: '发货速度',
        type: 'string',
        headerClass: 'text-center width-150',
        columnClass: 'text-center width-150',
        fastQuery: false,
        fastQueryType: 'eq',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
        	var content="<em class=\"raty\" data-score=\""+value+"\" style=\"width: 100px;\"></em>";
        	return content;
        }
    },
    {
        id: 'evaluateTimeStr',
        title: '评价时间',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
		hideType: 'md|sm|xs',
        fastQuery: false,
        fastSort: false,
    },
    {
        id: 'ordersSn',
        title: '订单编号',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
		hideType: 'md|sm|xs',
        fastQuery: false,
        fastSort: false
    },
    {
	    id: 'del',
	    title: '操作',
	    type: 'int',
	    columnClass: 'text-center width-100',
	    headerClass: 'text-center width-100',
	    fastQuery: false,
	    fastSort: false,
	    resolution: function (value, record, column, grid, dataNo, columnNo) {
	    	var content='<a href="javascript:;" class="btn btn-danger btn-sm m-r-10" name="delete" data-datano="0" onclick="del(\''+record.evaluateId+'\')">';
	    		content+='<i class="fa fa-lg fa-trash-o m-r-10"></i>删除</a>';
            return content;
        }
	}
   
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'evaluate/list_store',
    exportFileName: '店铺评分列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    onGridComplete: function (grid) {
    	$("#customSearch").click(function(){
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_m.memberName'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    	initRaty();
    }
};
var grid = $.fn.DtGrid.init(dtGridOption);

$(function () {
    grid.load();
});

/**
 * 小星星
 */
function initRaty(){
	$('.raty').raty({
        path: ncGlobal.publicRoot + "toolkit/jquery.raty/img",
        readOnly: true,
        width: 80,
        hints:['很不满意', '不满意', '一般', '满意', '很满意'],
        score: function() {
          return $(this).attr('data-score');
        }
    });
}

function del(evalId){
	var url=ncGlobal.adminRoot + "evaluate/delEvalStore";
	var tpl = '您选择评价ID为 <strong>'+evalId+'</strong>的数据 进行删除操作，删除后将无法恢复。<br/>您确定要进行删除操作吗?';
    $.ncConfirm({
        url:url,
        data:{
            evaluateId:evalId
        },
        content:tpl
    });
}