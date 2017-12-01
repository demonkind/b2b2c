/**
 * Created by shopnc.feng on 2015-12-31.
 */
var dtGridColumns = [
    {
        id: 'activityId',
        title: '活动id',
        type: 'string',
        columnClass: 'text-center width-50',
        hideType: 'xs',
        fastQuery: true,
        fastSort: true,
        fastQueryType: 'lk'
    },{
        id: 'activityName',
        title: '活动名称',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastQuery: true,
        fastQueryType: 'eq',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
        	console.log(value)
            return "<a href='javascript:;'>" + value + "</a>";
        }
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var str= "<a href='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='"
                + dataNo + "'>" + "<i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>" +
                "<a href='javascript:;' class='btn btn-sm btn-danger' onclick='goods.delGoods(\"" +record.activityId+ "\")'>" +
                "<i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>" ;
            return str;
        }
    },
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot+'findActivityList.json',
    exportFileName: '活动列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {
    	string: ["activityId", "activityName"]
    },
    onGridComplete: function (grid) {
        //图片同比例缩放-列表

    }
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'id';
grid.sortParameter.sortType = 1;
var goods = function () {
    var delUrl = ncGlobal.adminRoot + "delActivity"
    var _delGoods = function(id) {
        $.ncConfirm({
            url: delUrl,
            data: {
            	activityId: id
            }
        });
    }

    /**
     * 事件绑定
     * @private
     */
    function _bindEvent() {
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['activityName'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
        /**
         * 编辑活动
         */
        $("#editModal").on("show.bs.modal", function (e) {
            //获取接受事件的元素
            //获取data 参数
            var datano = $(e.relatedTarget).data('no'),
            //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];
    		console.log(gridData);
            $('#id_id').val(gridData.activityId);
            $('#id_name').val(gridData.activityName);
            //清楚错误信息
            $(".alert-danger").remove();
            $("#editFrom").psly().reset();
        })
    }

    return {
        init : function () {
            _bindEvent();
        },
        delGoods : _delGoods
    }
}();
$(function () {
    goods.init();
    grid.load();
})

function getCookie(name){
	var strcookie=document.cookie;
	var arrcookie=strcookie.split("; ");
	for(var i=0;i<arrcookie.length;i++){
	var arr=arrcookie[i].split("=");
	if(arr[0]==name)return unescape(arr[1]);
	}
	return null;
}

