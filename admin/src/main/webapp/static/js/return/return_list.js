/**
 * Created by shopnc on 2016/1/26.
 */
//定义表格
var dtGridColumns = [{
    id: 'refundSnStr',
    title: '退单单号',
    type: 'string',
    headerClass: 'text-left',
    columnClass: 'text-left',
    fastSort: true,
    fastQuery: true,
    fastQueryType: 'eq'
},
    {
        id: 'refundAmount',
        title: '退款金额（元）',
        type: 'string',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150 f-w-600 text-warning',
        format: '###.00',
        //fastSort: true,
        //fastQuery: true,
        //fastQueryType: 'eq',
        hideType: 'sm|xs',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return Nc.priceFormat(value)
        }
    },
	{
        id: 'addTime',
        title: '申请时间',
        type: 'date',
        format: 'yyyy-MM-dd',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
		hideType: 'md|sm|xs',
        fastQuery: true,
        fastQueryType: 'range'
    },
	{
        id: 'memberName',
        title: '申请人（买家）',
        type: 'string',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        fastSort: true,
        fastQuery: true,
        fastQueryType: 'eq',
        hideType: 'sm|xs'
    },
    {
        id: 'buyerMessage',
        title: '申请原因',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        /*fastSort: true,
        fastQuery: true,
        fastQueryType: 'eq',*/
        hideType: 'lg|md|sm|xs'
    },
    {
        id: 'picJson',
        title: '申请举证',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'md|sm|xs|lg',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var _l = value.split(",");
            var tpl = "<a href='javascript:;' class='dtimg' style='display: inline-block; vertical-align: middle;' nc-tip-pic ='{a}'><img src='{b}'/></a>";
            return _l.map(function (n) {
                return $.ReplaceTpl(tpl, {
                    a: ncGlobal.uploadRoot + n,
                    b: ncGlobal.uploadRoot + n
                });
            }).join(",");

        }
    },
    {
        id: 'goodsName',
        title: '涉及商品',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: true,
        fastQuery: true,
        fastQueryType: 'eq',
        hideType: 'md|sm|xs|lg'
    },
	{
        id: 'goodsId',
        title: '商品ID',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: true,
        fastQuery: true,
        fastQueryType: 'eq',
        hideType: 'lg|md|sm|xs'
    },
	{
        id: 'goodsImage',
        title: '商品图片',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        //fastSort: true,
        //fastQuery: true,
        //fastQueryType: 'eq',
        hideType: 'lg|md|sm|xs',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var tpl = "<a href='javascript:;' class='dtimg' nc-tip-pic ='{a}'><img src='{b}'/></a>";
            return $.ReplaceTpl(tpl, {
                a: ncGlobal.uploadRoot + value,
                b: ncGlobal.uploadRoot + value
            });
        }
    },
    {
        id: 'sellerState',
        title: '商家处理',
        type: 'number',
        headerClass: 'text-center width-100',
        columnClass: 'text-center width-100',
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: "待审核",
            2: "同意",
            3: "不同意"
        }
    }, 
	{
        id: 'sellerTime',
        title: '商家处理时间',
        type: 'date',
        format: 'yyyy-MM-dd',
        headerClass: 'text-left width-150',
        columnClass: 'text-left width-150',
        fastSort: true,
        fastQuery: true,
        fastQueryType: 'range',
        hideType: 'md|sm|xs'
    },
	 {
        id: 'storeName',
        title: '商家名称',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: true,
        fastQuery: true,
        fastQueryType: 'eq',
        hideType: 'md|sm|xs|lg'
    },
    {
        id: 'sellerMessage',
        title: '商家处理备注',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        //fastSort: true,
        //fastQuery: true,
        //fastQueryType: 'eq',
        hideType: 'lg|md|sm|xs'
    },
	{
        id: 'refundState',
        title: '平台处理',
        type: 'number',
        headerClass: 'text-center width-150',
        columnClass: 'text-center width-150',
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: "处理中",
            2: "待管理员处理",
            3: "已完成"
        }
    },      
        {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {

            var content = '',
                handleUrl =ncGlobal.adminRoot + 'return/info/' + record.refundId+ '?type=handle',
                lookUrl = ncGlobal.adminRoot + 'return/info/' + record.refundId+ '?type=look'
                ;
            if (record.showAdminReturnHandle == 1) {
                content +=
                    '<a href="'+handleUrl+'" class="btn btn-primary btn-sm m-r-10" ><i class="fa fa-gears"></i>&nbsp;处理&nbsp;</a>' + '</div>';
            }else{
                content += '<a href="javascript:;" data-toggle="dropdown" aria-expanded="false" class="btn btn-default btn-sm dropdown-toggle m-r-10" style="cursor: not-allowed;">' +
                    '<i class="fa fa-gears"></i>&nbsp;处理&nbsp;</a>';
            }
            content += "<a href='" +lookUrl+ "' class='btn btn-sm btn-info'><i class='fa fa-eye'></i>查看</a>";
            return content;
        }
    }];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'return/list.json',
    exportFileName: '退货列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {
        Timestamp: ["addTime", "sellerTime"],
        long: ["refundSn","ordersSn"],
        int: ["refundType", "sellerState", "refundState"]
    },
    onGridComplete: function (grid) {
        //图片同比例缩放-列表
        $('.dtimg img').jqthumb({
            width: 30,
            height: 30,
            after: function (imgObj) {
                imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
            }
        });

    },
    ncDefaultFastQueryParameters:{
        eq_refundType:2
    }
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'addTime';
grid.sortParameter.sortType = 1;
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_refundType'] = 2;


var reason = function () {
    /**
     * 绑定事件
     * @private
     */
    function _bindEvent() {
        //模糊查询
        $("#customSearch").click(function () {
            var a = $.trim( $('#keyword').val());
            if (!Nc.isDigits(a)){
                $('#keyword').val("");
            }
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['eq_refundSn'] = a;
            grid.fastQueryParameters['eq_refundType'] = 2;
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;

            grid.refresh(true);
        });
        //添加 modal显示时
        $('#addModal').on('show.bs.modal', function (event) {
            $("#addForm").psly().reset();
            $(".alert-danger").remove()
        });
        //编辑modal 显示时
        $('#editModal').on('show.bs.modal', function (event) {
            var
                button = $(event.relatedTarget),
                datano = button.data('no'),
                gridData = grid.sortOriginalDatas[datano],
                editForm = $("#editForm");
            $("#editReasonId").val(gridData.reasonId);
            $("#editReasonInfo").val(gridData.reasonInfo);
            $("#editReasonSort").val(gridData.reasonSort);
            editForm.psly().reset();
            $(".alert-danger").remove()
        })
        //添加的form 成功返回时
        $("#addForm").on("nc.formSubmit.success", function () {
            $("#addReasonInfo").val('');
            $("#addReasonSort").val('');
            $("#addForm").psly().reset();
            $(".alert-danger").remove()
        })


    }

    //外部可调用
    return {
        init: function () {
            _bindEvent();
        },
        //删除
        del: function (reasonId) {
            console.log("删除按钮点击");
            $.ncConfirm({
                url: ncGlobal.adminRoot + "refund/reason/del",
                data: {
                    reasonId: reasonId
                }
            });

        }
    }
}()
$(function () {
    grid.load();
    reason.init();
});