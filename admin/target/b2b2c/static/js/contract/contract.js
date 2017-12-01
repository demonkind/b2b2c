
var dtGridColumns = [
    {
        id: 'ctId',
        title: 'ctId',
        type: 'string',
        columnClass: 'text-center width-50',
        hideType: 'xs',
        fastQuery: false,
        fastSort: false,
        hide:true
    },
    {
        id: 'ctStorename',
        title: '店铺名称',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        hideType: 'xs',
        fastSort: false
    },
    {
        id: 'ctItemname',
        title: '保障服务',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: false,
        fastQueryType: 'lk',
        hideType: 'xs'
    },
    {
        id: 'ctCost',
        title: '保证金余额（元）',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: false,
        fastSort: true
    },
    {
        id: 'ctState',
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
	    columnClass: 'text-center width-200',
	    headerClass: 'text-center width-200',
	    fastQuery: false,
	    fastSort: false,
	    resolution: function (value, record, column, grid, dataNo, columnNo) {
	    	var html="<div class='btn-group'>";
				html+="<a href='javascript:;' data-toggle='dropdown' aria-expanded='false' class='btn btn-primary btn-sm dropdown-toggle m-r-10'><i class='fa fa-gears'></i>&nbsp;设置&nbsp;<span class='caret'></span></a>";
				html+="<ul class='dropdown-menu dropdown-menu-left'>";
				html+="<li><a href='javascript:;' data-target='#editModal' data-toggle='modal' data-no='"+ dataNo + "'><i class='fa fa-lg fa-edit m-r-10'></i>编辑状态</a></li>";
				html+="<li><a href='javascript:;' data-target='#editModal2' data-toggle='modal' data-no='" + dataNo + "'><i class='fa fa-lg fa-database m-r-10'></i>保证金管理</a></li>";
	    		html+="</ul>";
				html+="</div>";
				
	    		html+="<div class='btn-group'>";
				html+="<a href='javascript:;' data-toggle='dropdown' aria-expanded='false' class='btn btn-info btn-sm dropdown-toggle'><i class='fa fa-eye'></i>&nbsp;查看&nbsp;<span class='caret'></span></a>";
				html+="<ul class='dropdown-menu dropdown-menu-left'>";
				html+="<li><a  href='"+ncGlobal.adminRoot+"contract/contract_detail?ctid="+record.ctId+"'><i class='fa fa-lg fa-file-o m-r-10'></i>保障服务日志</a></li>";
				html+="<li><a href='"+ncGlobal.adminRoot+"contract/contract_cost?ctid="+record.ctId+"'><i class='fa fa-lg fa-file-zip-o m-r-10'></i>保证金管理日志</a></li>";
	    		html+="</ul>";
				html+="</div>";	    	
	    	return html;
        }
	}
   
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'contract/contract_list',
    exportFileName: '店铺保障服务列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10,20,50],
    onGridComplete: function (grid) {
    	$("#customSearch").click(function(){
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_ctStorename'] = $('#keyword').val();
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
            $("#ctId").val(gridData.ctId);
            $("#ctItemname").text(gridData.ctItemname);
            $("#storeName").text(gridData.ctStorename);
            $("#state").text(gridData.ctState);
            var ctClosestate=gridData.ctClosestate;
            $("input[name='ctClosestate'][value='"+ctClosestate+"']").attr("checked","checked");
            showTextarea();
        });
        
        
        $("#editModal2").on("show.bs.modal", function (event) {
            //清除错误信息
            $(".alert-danger").remove();
            $("#editForm2").psly().reset();
            //获取接受事件的元素
            var button = $(event.relatedTarget);
            //获取data 参数
            var datano = button.data('no');
            //var modal = $(this);
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];

            //填入内容
            $("#editForm2 input[name='ctId']").val(gridData.ctId);
            $("#editForm2 span[name='ctItemname']").text(gridData.ctItemname);
            $("#editForm2 span[name='ctCost']").text(gridData.ctCost);
            $("#editForm2 span[name='storeName']").text(gridData.ctStorename);
        });

        //调用checkbox方法
        //$("#ctiState").bootstrapSwitch();

    }

    //外部可调用
    return {
        bindEvent: _bindEvent
    };
}();

function showTextarea(){
	$("#closestate input[name='ctClosestate']").on("click",function(){
		var val=$(this).val();
		if(val=="0"){
			$("#cause").show();
		}else{
			$("#cause").hide();
		}
	});
}

$(function () {
	grid.sortParameter.columnId = 'ctStorename';
    grid.sortParameter.sortType = 2;
    grid.load();
    
    OperateHandle.bindEvent();
});
