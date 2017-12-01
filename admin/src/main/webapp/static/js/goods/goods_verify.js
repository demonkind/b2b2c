/**
 * Created by shopnc.feng on 2015-12-31.
 */
var dtGridColumns = [
    {
        id: 'commonId',
        title: 'SPU',
        type: 'string',
        columnClass: 'text-center width-50',
        hideType: 'xs',
        fastQuery: true,
        fastSort: true,
        fastQueryType: 'lk'
    },
    {
        id: 'imageSrc',
        title: '图片',
        type: 'string',
        columnClass: 'text-center width-50',
        hideType: 'xs',
        fastQuery: false,
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var tpl = "<a href='javascript:;' class='dtimg' nc-tip-pic ='{a}'><img src='{b}'/></a>";
            return $.ReplaceTpl(tpl, {
                a: value + "",
                b: value + ""
            });
        }
    },
    {
        id: 'goodsName',
        title: '商品名称',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastQuery: true,
        fastQueryType: 'eq',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
           return "<a href='" + ncGlobal.webRoot + "goods/" + record.goodsId + "' target='_blank'>" + value + "</a>";
        }
    },
    {
        id: 'createTime',
        title: '创建时间',
        type: 'date',
        headerClass: 'text-left',
        columnClass: 'text-left width-100',
		hideType: 'lg|md|sm|xs',
        fastQuery: true,
        format: 'yyyy-MM-dd'
    },
    {
        id: 'priceRange',
        title: '价格区间',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left width-150',
		hideType: 'lg|md|sm|xs',
        fastQuery: false,
        fastSort: false
    },
    {
        id: 'storage',
        title: '库存',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left width-100',
        hideType: 'lg|md|sm|xs',
        fastQuery: false,
        fastSort: false
    },
    {
        id: 'goodsState',
        title: '上架状态',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left width-100',
        hideType: 'md|sm|xs',
        fastQuery: true,
        fastSort: false,
        codeTable: {
            0: $lang.goods.goodsState0,
            1: $lang.goods.goodsState1,
            10: $lang.goods.goodsState10
        }
    },
    {
        id: 'stateRemark',
        title: '禁售原因',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left width-100',
        hideType: 'lg|md|sm|xs',
        fastQuery: false,
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return (record.goodsState == 10) ? record.stateRemark : '--';
        }
    },
    {
        id: 'goodsVerify',
        title: '审核状态',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left width-100',
        hideType: 'md|sm|xs',
        fastQuery: true,
        fastSort: false,
        codeTable: {
            0: $lang.goods.goodsVerify0,
            1: $lang.goods.goodsVerify1,
            10: $lang.goods.goodsVerify10
        }
    },
    {
        id: 'verifyRemark',
        title: '审核失败原因',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left width-100',
        hideType: 'lg|md|sm|xs',
        fastQuery: false,
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return (record.goodsVerify == 10) ? record.verifyRemark : '--';
        }
    },
    {
        id: 'storeName',
        title: '店铺名称',
        type: 'string',
        columnClass: 'text-center width-150',
        fastQuery: true,
        hideType: 'md|sm|xs'
    },
    {
        id: 'isOwnShop',
        title: '店铺类型',
        type: 'string',
        codeTable: {
            1: $lang.goods.own,
            0: $lang.goods.other
        },
        columnClass: 'text-center width-100',
        hideType: 'sm|xs',
        fastQuery: true
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-250',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a href='javascript:;' class='btn btn-sm btn-primary m-r-10' onclick='verify.passGoods(" + record.commonId + ")'>" +
            "<i class='fa fa-trash-o'></i>&nbsp;通过&nbsp;</a>" +
                "<a href='#failModal' class='btn btn-sm btn-danger m-r-10' data-toggle='modal' data-no='"
            + dataNo + "'>" + "<i class='fa fa-edit'></i>&nbsp;未通过&nbsp;</a>";
        }
    },
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'goods/list.json',
    exportFileName: '商品列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {
        int: ["commonId", "goodsState", "goodsVerify"]
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
    }
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['ne_goodsVerify'] = 1;
grid.sortParameter.columnId = 'commonId';
grid.sortParameter.sortType = 1;
var verify = function () {
    var passUrl = ncGlobal.adminRoot + "goods/pass.json"
    var _passGoods = function(id) {
        $.post(passUrl,
            {commonId:id},
            function(data) {
                if (data.code == 200) {
                    grid.reload(true);
                } else {
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: data.message
                    })
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
            grid.fastQueryParameters['lk_goodsName'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });

        /**
         * 审核失败
         */
        $("#failModal").on("show.bs.modal", function (e) {
            //获取接受事件的元素
            //获取data 参数
            var datano = $(e.relatedTarget).data('no'),
            //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];

            $('#commonId').val(gridData.commonId);
            $('#goodsName').val(gridData.goodsName);
            //清楚错误信息
            $(".alert-danger").remove();
            $("#failForm").psly().reset();
        })
    }

    return {
        init : function () {
            _bindEvent();
        },
        passGoods : _passGoods
    }
}();
$(function () {
    verify.init();
    grid.load();
})