/**
 * Created by dqw on 2015/12/11.
 */

//列表开始
var dtGridColumns = [
    {
        id: 'id',
        title: '编号',
        type: 'number',
        columnClass: 'text-center width-100'
    },
    {
        id: 'name',
        title: '等级名称',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left width-200',
        fastSort: false
    },
	{
        id: 'recommendLimit',
        title: '可推荐商品数',
        type: 'number',
        columnClass: 'text-center width-100',
		hideType: 'xs|sm|md',
        fastSort: false
    },
    {
        id: 'goodsLimit',
        title: '可发布商品数',
        type: 'number',
        columnClass: 'text-center width-100',
		hideType: 'xs|sm',
        fastSort: false
    },
    {
        id: 'albumLimit',
        title: '可上传图片数',
        type: 'number',
        columnClass: 'text-center width-100',
		hideType: 'xs|sm',
        fastSort: false
    },    
	{
        id: 'price',
        title: '收费标准（元）',
        type: 'number',
        columnClass: 'text-center width-150',
		hideType: 'xs|sm|md',
        fastSort: false
    },
    {
        id: 'description',
        title: '申请说明',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'xs|sm|md',
        fastSort: false
    },
    {
        id: 'sort',
        title: '排序',
        type: 'number',
        columnClass: 'text-center width-100',
		hideType: 'xs|sm|md',
        fastSort: false
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a data-target='#addModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-type='edit' data-no='"
                + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>"
                + "<a href='javascript:;' class='btn btn-danger btn-sm' onclick='OperateHandle.delGrade("
                + record.id + ")' ><i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'store_grade/list.json',
    exportFileName: '店铺等级列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["goodsLimit", "albumLimit", "recommendLimit", "sort"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = 'id';
grid.sortParameter.sortType = 0;
//列表结束

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        //新增对话框初始化
        $("#addModal").on("show.bs.modal", function (event) {
            //清除错误信息
            $(".alert-danger").remove();
            $("#addForm").psly().reset();

            //获取接受事件的元素
            var button = $(event.relatedTarget);

            //获取data 参数
            var datano = button.data('no');
            var dataType = button.data('type');
            if(dataType) {
                $("#modalTitle").text("编辑等级");
                var gridData = grid.sortOriginalDatas[datano];
                $("#gradeId").val(gridData.id);
                $("#addForm").find("[name='name']").val(gridData.name);
                $("#addForm").find("[name='goodsLimit']").val(gridData.goodsLimit);
                $("#addForm").find("[name='albumLimit']").val(gridData.albumLimit);
                $("#addForm").find("[name='recommendLimit']").val(gridData.recommendLimit);
                $("#addForm").find("[name='price']").val(gridData.price);
                $("#addForm").find("[name='description']").val(gridData.description);
                $("#addForm").find("[name='sort']").val(gridData.sort);
            } else {
                $("#modalTitle").text("新增等级");
                $("#gradeId").val("0");
                $("#addForm").find("[name='name']").val("");
                $("#addForm").find("[name='goodsLimit']").val("");
                $("#addForm").find("[name='albumLimit']").val("");
                $("#addForm").find("[name='recommendLimit']").val("");
                $("#addForm").find("[name='price']").val("");
                $("#addForm").find("[name='description']").val("");
                $("#addForm").find("[name='sort']").val("");
            }
        });

        //调用checkbox方法
        $("#allowLogin").bootstrapSwitch();

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_name'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    /**
     * 删除
     */
    function _delGrade(id) {
        var tpl = '您确定要删除该等级吗?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "store_grade/del.json",
            data: {
                gradeId: id
            },
            content: tpl
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delGrade: _delGrade
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});