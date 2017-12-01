
var dtGridColumns = [
    {
        id: 'ctqStorename',
        title: '店铺名称',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        hideType: 'xs',
        fastSort: false
    },
    {
        id: 'ctqItemname',
        title: '保障服务',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: false,
        fastQueryType: 'lk',
        hideType: 'xs'
    },
    {
        id: 'ctqAddtime',
        title: '添加时间',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: false,
        fastSort: false
    },
    {
        id: 'ctqState',
        title: '状态',
        type: '',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastQuery: false,
        fastSort: true,
        hideType: 'xs'
    },
    {
	    id: 'update',
	    title: '操作',
	    type: 'string',
	    columnClass: 'text-center width-150',
	    headerClass: 'text-center width-150',
	    fastQuery: false,
	    fastSort: false,
	    resolution: function (value, record, column, grid, dataNo, columnNo) {
	    	var html="";
	    	if(record.ctqAuditstate!="0"){
	    		html+="<a data-target='#editModal' class='btn btn-sm btn-info' data-toggle='modal' data-no='"+ dataNo + "' ><i class='fa fa-eye'></i>&nbsp;查看&nbsp;</a>";
	    	}else{
	    		html+="<a data-target='#editModal' class='btn btn-sm btn-success' data-toggle='modal' data-no='"+ dataNo + "' ><i class='fa fa-edit'></i>&nbsp;审核&nbsp;</a>";
	    	}
	    	return html;
        }
	}
   
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'contract/quitapply_list',
    exportFileName: '服务退出申请列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10,20,50],
    onGridComplete: function (grid) {
    	$("#customSearch").click(function(){
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_ctqStorename'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }
};
var grid = $.fn.DtGrid.init(dtGridOption);


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
            //var modal = $(this);
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];

            //清除错误信息
            $(".alert-danger").remove();
            $("#editForm").psly().reset();

            //填入内容
            $("#ctqId").val(gridData.ctqId);
            $("#ctqItemname").text(gridData.ctqItemname);
            $("#ctqAddtime").text(gridData.ctqAddtime);
            //$("#ctqCostimg").attr("src",gridData.ctqCostimg);
            $("#storeName").text(gridData.ctqStorename);
            var ctqAuditstate=gridData.ctqAuditstate;
            if(ctqAuditstate=="0"){//审核加入
            	$("#ctqCostimgDiv").hide();
            	var html="<input type='radio' value='0' name='ctqAuditstate' checked='checked' />等待审核";
            	html+="<input type='radio' value='1' name='ctqAuditstate' />审核通过";
            	html+="<input type='radio' value='2' name='ctqAuditstate' />审核失败";
            	$("#state").html(html);
            	showTextarea();
            	$(".modal-footer").show();
            }else{
            	$("#ctqCostimgDiv").hide();
            	$("#state").html(gridData.ctqState);
            	$("#cause").hide();
            	$(".modal-footer").hide();
            }
        });

        //调用checkbox方法
        $("#ctiState").bootstrapSwitch();

    }

    //外部可调用
    return {
        bindEvent: _bindEvent
    };
}();

function showTextarea(){
	var v=$("#state input[name='ctqAuditstate']:checked").val();
	if(v=="2"){
		$("#cause").show();
	}else{
		$("#cause").hide();
	}
	$("#state input[name='ctqAuditstate']").on("click",function(){
		var val=$(this).val();
		if(val=="2"){
			$("#cause").show();
		}else{
			$("#cause").hide();
		}
	});
}

$(function () {
	grid.sortParameter.columnId = 'ctqAddtime';
    grid.sortParameter.sortType = 1;
    grid.load();
    
    OperateHandle.bindEvent();
});
