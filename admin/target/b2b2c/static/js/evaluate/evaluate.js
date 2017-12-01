
var dtGridColumns = [
   
    {
        id: 'evaluateId',
        title: 'evaluateId',
        type: 'string',
        columnClass: 'text-center width-50',
        hideType: 'xs',
        fastQuery: false,
        fastSort: false,
        hide:true
    },
    {
        id: 'memberName',
        title: '评价人',
        type: 'string',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        hideType: 'md|sm|xs',
        fastQuery: true,
        fastSort: false,
        fastQueryType: 'lk'
    },
	 {
        id: 'evaluateTimeStr',
        title: '评价时间',
        type: 'string',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
		hideType: 'md|sm|xs',
        fastQuery: false,
        fastSort: false,
    },
    {
        id: 'scores',
        title: '评分',
        type: 'string',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        fastQuery: true,
        fastQueryType: 'eq',
        hideType: 'md|sm|xs',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
        	var content="<em class=\"raty\" data-score=\""+value+"\" style=\"width: 100px;\"></em>";
        	return content;
        }
    },
    {
        id: 'evaluateContent1',
        title: '评价内容',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastQuery: false,
        fastSort: false
    },
    {
        id: 'image1List',
        title: '晒单图片',
        type: '',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: false,
        fastSort: false,
        hideType: 'md|sm|xs',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
        	var content="";
        	if(value!=""){
        		$.each(value,function(i){
        			//content+="<img  src=\""+ncGlobal.uploadRoot+value[i]+"\" style=\"width:30px;height:30px;\" />";
        			var tpl = "<a href='javascript:;' class='dtimg' nc-tip-pic ='{a}'><img src='{b}'/></a>";
        			content+=$.ReplaceTpl(tpl, {
                        a: ncGlobal.uploadRoot+value[i] + "",
                        b: ncGlobal.uploadRoot+value[i] + ""
                    });
        		});
        	}
            return content;
        }
    },
   {
        id: 'ordersSn',
        title: '订单编号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-left',
        hideType: 'lg|md|sm|xs',
        fastQuery: false,
        fastSort: false
    },
    {
        id: 'goodsName',
        title: '被评商品',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-left',
		hideType: 'lg|md|sm|xs',
        fastQuery: true,
        fastSort: false,
        fastQueryType: 'lk'
    },
    {
        id: 'storeName',
        title: '所属商家',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-left',
        hideType: 'lg|md|sm|xs',
        fastQuery: true,
        fastSort: false,
        fastQueryType: 'lk'
    },
    {
        id: 'evaluateContent2',
        title: '追评内容',
        type: 'string',
        headerClass: 'text-left width-400',
        columnClass: 'text-left width-400',
        hideType: 'lg|md|sm|xs',
        fastQuery: false,
        fastSort: false
    },
    {
        id: 'image2List',
        title: '追评晒单',
        type: '',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: false,
        fastSort: false,
        hideType: 'lg|md|sm|xs',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
        	var content="";
        	if(value!=""){
        		$.each(value,function(i){
        			//content+="<img  src=\""+ncGlobal.uploadRoot+value[i]+"\" style=\"width:30px;height:30px;\" />";
        			var tpl = "<a href='javascript:;' class='dtimg' nc-tip-pic ='{a}'><img src='{b}'/></a>";
        			content+=$.ReplaceTpl(tpl, {
                        a: ncGlobal.uploadRoot+value[i] + "",
                        b: ncGlobal.uploadRoot+value[i] + ""
                    });
        		});
        	}
            return content;
        }
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
    loadURL: ncGlobal.adminRoot + 'evaluate/list',
    exportFileName: '评价列表',
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
    	
    	//图片同比例缩放-列表

        $('.dtimg img').jqthumb({
            width: 30,
            height: 30,
            after: function (imgObj) {
                imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
            }
        });
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
	var url=ncGlobal.adminRoot + "evaluate/delEval";
	var tpl = '您选择评价ID为 <strong>'+evalId+'</strong>的数据 进行删除操作，删除后将无法恢复。<br/>您确定要进行删除操作吗?';
    $.ncConfirm({
        url:url,
        data:{
            evaluateId:evalId
        },
        content:tpl
    });
}