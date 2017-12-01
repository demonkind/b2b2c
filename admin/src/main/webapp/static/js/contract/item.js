
var dtGridColumns = [
    {
        id: 'ctiSort',
        title: '排序',
        type: 'string',
        columnClass: 'text-center width-50',
        hideType: 'xs',
        fastSort: false
    },
    {
        id: 'ctiName',
        title: '项目名称',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastQuery: true,
        fastQueryType: 'lk',
        hideType: 'xs'
    },
    {
        id: 'ctiCost',
        title: '保证金(元)',
        type: 'string',
		headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
		hideType: 'sm|xs',
        fastQuery: false,
        fastSort: false
    },
    {
        id: 'ctiState',
        title: '状态',
        type: '',
        headerClass: 'text-center',
        columnClass: 'text-center width-150',
        fastQuery: false,
        fastSort: true,
        hideType: 'sm|xs',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
        	var content="关闭";
        	if(value=="1"){
        		content="开启";
        	}
            return content;
        }
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
	    	return "<a data-target='#editModal' class='btn btn-sm btn-primary' data-toggle='modal' data-no='"
            + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
        }
	}
   
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'contract/item_list',
    exportFileName: '保障服务管理列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10],
    onGridComplete: function (grid) {
    	
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
            $("#ctiId").val(gridData.ctiId);
            $("#ctiName").val(gridData.ctiName);
            $("#ctiCost").val(gridData.ctiCost);
            $("#ctiDescribe").val(gridData.ctiDescribe);
            $("#ctiDescurl").val(gridData.ctiDescurl);
            $("#ctiSort").val(gridData.ctiSort);
            var ctiIcon=gridData.ctiIcon;
            var ctiIconUrl=gridData.ctiIconUrl;
            if(ctiIconUrl!=""){
            	//$("#itemIcon").html("<img src=\""+ncGlobal.uploadRoot+ctiIcon+"\" style=\"width:50px;height:50px\" />");
            	$("#brand-logo").html("<img src=\""+ctiIconUrl+"\" id=\"editFormBrandImageImg\" alt=\"\" class=\"media-object\">");
            	$("#ctiIcon").val(ctiIcon);
            }else{
            	//$("#itemIcon").html("");
            	$("#brand-logo").html("");
            	$("#ctiIcon").val("");
            }
            if(gridData.ctiState == 1) {
                $("#ctiState").bootstrapSwitch('state', true);
            } else {
                $("#ctiState").bootstrapSwitch('state', false);
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

//上传图片
function fileupload(){
	$("#ctiIconFile").fileupload({
        dataType: 'json',
        url: ncGlobal.adminRoot + "image/upload.json",
        done: function (e,data) {
            if (data.result.code == 200) {
            	var html="<img src=\""+data.result.data.url+"\" id=\"editFormBrandImageImg\" alt=\"\" class=\"media-object\">";
            	$("#brand-logo").html(html);
            	$("#ctiIcon").val(data.result.data.name);
            } else {
				Nc.alertError(data.result.message);
            }
        }
    });
}

$(function () {
	grid.sortParameter.columnId = 'ctiState';
    grid.sortParameter.sortType = 1;
    grid.load();
    
    OperateHandle.bindEvent();
    
    $('.brand-logo img').jqthumb({
        width: 150,
        height: 50,
        after: function (imgObj) {
            imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
        }
    });
    fileupload();
});
