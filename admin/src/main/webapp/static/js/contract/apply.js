
var dtGridColumns = [
    {
        id: 'ctaStorename',
        title: '店铺名称',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        hideType: 'xs',
        fastSort: false
    },
    {
        id: 'ctaItemname',
        title: '保障服务',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: false,
        fastQueryType: 'lk',
        hideType: 'xs'
    },
    {
        id: 'ctaAddtime',
        title: '添加时间',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: false,
        fastSort: false
    },
    {
        id: 'ctaState',
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
	    	if(record.ctaAuditstate!="0" && record.ctaAuditstate!="3"){
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
    loadURL: ncGlobal.adminRoot + 'contract/apply_list',
    exportFileName: '服务加入申请列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10,20,50],
    onGridComplete: function (grid) {
    	$("#customSearch").click(function(){
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_ctaStorename'] = $('#keyword').val();
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
            $("#ctaId").val(gridData.ctaId);
            $("#ctaItemname").text(gridData.ctaItemname);
            $("#ctaAddtime").text(gridData.ctaAddtime);
            $("#ctaCostimg").attr("src",ncGlobal.uploadRoot+gridData.ctaCostimg);
            $("#storeName").text(gridData.ctaStorename);
            var ctaAuditstate=gridData.ctaAuditstate;
            if(ctaAuditstate=="0"){//审核加入
            	$("#ctaCostimgDiv").hide();
            	var html="<label class='radio-inline'><input type='radio' value='0' name='ctaAuditstate' checked='checked' />等待审核</label>";
            	html+="<label class='radio-inline'><input type='radio' value='1' name='ctaAuditstate' />审核通过，待支付保证金</label>";
            	html+="<label class='radio-inline'><input type='radio' value='2' name='ctaAuditstate' />审核未通过</label>";
            	$("#state").html(html);
            	showTextarea();
            	$(".modal-footer").show();
            }else if(ctaAuditstate=="3"){//审核保证金
            	$("#ctaCostimgDiv").show();
            	var html="<label class='radio-inline'><input type='radio' value='3' name='ctaAuditstate' checked='checked' />保证金待审核</label>";
            	html+="<label class='radio-inline'><input type='radio' value='4' name='ctaAuditstate' />保证金审核通过</label>";
            	html+="<label class='radio-inline'><input type='radio' value='5' name='ctaAuditstate' />保证金审核失败</label>";
            	$("#state").html(html);
            	showTextarea();
            	$(".modal-footer").show();
            }else{
            	$("#ctaCostimgDiv").hide();
            	$("#state").html(gridData.ctaState);
            	$("#cause").hide();
            	$(".modal-footer").hide();
            }
        });

        //调用checkbox方法
        //$("#ctiState").bootstrapSwitch();

    }

    //外部可调用
    return {
        bindEvent: _bindEvent
    };
}();

//上传图片
function fileupload(){
	$(".uploadinput").fileupload({
        dataType: 'json',
        url: ncGlobal.adminRoot + "image/upload.json",
        done: function (e,data) {
            if (data.result.code == 200) {
            	var html="<img src=\""+data.result.data.url+"\" style=\"width:50px;height:50px\" />";
            	$("#itemIcon").html(html);
            	$("#ctiIcon").val(data.result.data.name);
            } else {
				Nc.alertError(data.result.message);
            }
        }
    });
}

function showTextarea(){
	var v=$("#state input[name='ctaAuditstate']:checked").val();
	if(v=="2" || v=="5"){
		$("#cause").show();
	}else{
		$("#cause").hide();
	}
	$("#state input[name='ctaAuditstate']").on("click",function(){
		var val=$(this).val();
		if(val=="2" || val=="5"){
			$("#cause").show();
		}else{
			$("#cause").hide();
		}
	});
}

$(function () {
	grid.sortParameter.columnId = 'ctaAddtime';
    grid.sortParameter.sortType = 1;
    grid.load();
    
    OperateHandle.bindEvent();
    
    
    
    //fileupload();
});
