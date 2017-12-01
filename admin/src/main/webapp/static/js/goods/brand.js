/**
 * Created by shopnc.feng on 2015-11-25.
 */
var dtGridColumns = [
    {
        id: 'brandInitial',
        title: '首字母',
        type: 'string',
        columnClass: 'text-center width-100',
        hideType: 'md|sm|xs',
        fastQuery: true,
        fastQueryType: 'eq'
    },
    {
        id: 'brandName',
        title: '品牌名称',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'brandEnglish',
        title: '英文名称',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'brandImage',
        title: '品牌LOGO',
        type: 'string',
        columnClass: 'text-center width-100',
        hideType: 'xs',
        fastQuery: false,
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var tpl = "<a href='javascript:;' class='dtimg' nc-tip-pic ='{a}'><img src='{b}'/></a>";
            return $.ReplaceTpl(tpl, {
                a: ncGlobal.uploadRoot + value + "",
                b: ncGlobal.uploadRoot + value + ""
            });
        }
    },
    {
        id: 'brandLabelNames',
        title: '品牌标签',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'lg|md|sm|xs',
        fastQuery: true,
        fastSort: false
    },
    {
        id: 'showType',
        title: '展示方式',
        type: 'string',
        codeTable: {
            0: $lang.brand.showType0,
            1: $lang.brand.showType1
        },
        columnClass: 'text-center width-100',
        hideType: 'sm|xs',
        fastQuery: true
    },
    {
        id: 'isRecommend',
        title: '品牌推荐',
        type: 'string',
        columnClass: 'text-center width-100',
        fastQuery: true,
        type: "string",
        codeTable: {
            0: $lang.brand.isRecommend0,
            1: $lang.brand.isRecommend1
        },
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var content = '';
            content += '<input type="checkbox" value="1" name="dtGridIsRecommend" data-size="small" data-on-color="primary" data-on-text="是" data-off-text="否"';
            content += 'data-dataNo="' + dataNo + '"';
            if (value == 1) {
                content += ' checked ';
            }
            content += '/>';
            return content;
        }
    },{
        id: 'applyState',
        title: '审核状态',
        type: 'string',
        codeTable: {
            0: $lang.brand.applyState0,
            1: $lang.brand.applyState1,
            10: $lang.brand.applyState10
        },
        columnClass: 'text-center width-100',
        hideType: 'md|sm|xs',
        fastQuery: true
    },
    {
        id: 'brandSort',
        title: '排序',
        type: 'string',
        columnClass: 'text-center width-100',
        hideType: 'md|sm|xs',
        fastQuery: false
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a href='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='"
                + dataNo + "'>" + "<i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>" +
                "<a href='javascript:;' class='btn btn-sm btn-danger' onclick='brand.delBrand(" + record.brandId + ")'>" +
                "<i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
        }
    },
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'brand/list.json',
    exportFileName: '品牌列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {
        int: ["applyState", "showType", "isRecommend", "brandSort"]

    },
    onGridComplete: function (grid) {
        //图片同比例缩放-列表

        $('.dtimg img').jqthumb({
            width: 90,
            height: 30,
            after: function (imgObj) {
                imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
            }
        });
        $("[name='dtGridIsRecommend']").bootstrapSwitch('onSwitchChange', function (event, state) {
            var dataNo = $(this).attr("data-dataNo");
            $.ajax({
                type: 'POST',
                url: ncGlobal.adminRoot + 'brand/is_recommend/update.json',
                dataType: 'json',
                data: {
                    isRecommend: state ? 1 : 0,
                    brandId: grid.exhibitDatas[dataNo].brandId
                }
            }).success(function (data) {
                grid.exhibitDatas[dataNo].isRecommend = state ? 1 : 0;
            });
        });
    }
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'brandId';
grid.sortParameter.sortType = 1;

var brand = function () {
    var delUrl = ncGlobal.adminRoot + "brand/delete.json";
    /**
     * 添加品牌相关
     */
    var addBrand = {
        //初始化添加品牌对话框
        initAddModel: function () {
            var $addForm = $("#addForm"),
                $addFormBrandName = $("#addFormBrandName"),
                $addFormBrandImage = $('#addFormBrandImage'),
                $addFormBrandEnglish = $('#addFormBrandEnglish'),
                $addFormBrandImageImg = $('#addFormBrandImageImg'),
                $addFormBrandSort = $("#addFormBrandSort"),
                $addFormIsRecommend = $("#addFormIsRecommend"),
                $addFormBrandLabelsChecked = $("#addFormBrandLabelsChecked"),
                $addFormBrandLabelsAll = $("#addFormBrandLabelsAll")
                ;
            //清楚错误信息
            $(".alert-danger").remove();
            $addForm.psly().reset();
            //
            $addFormBrandName.val("");
            $addFormBrandImage.val("");
            $addFormBrandImageImg.attr('src', ncGlobal.imgRoot + "default_image.gif");
            $addFormBrandSort.val("0");
            $addFormBrandEnglish.val("");
            //switch 的重置
            $addFormIsRecommend.bootstrapSwitch('state', false);
            //已选标签重置
            $addFormBrandLabelsChecked.empty()
            //未选标签重置
            $addFormBrandLabelsAll.find("div[data-id]").show();

            //图片同比例缩放-默认
            $('.brand-logo img').jqthumb({
                width: 150,
                height: 50,
                after: function (imgObj) {
                    imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                }
            });
        }
    }

    function _buildHTML() {
        /**
         * 上传插件绑定
         */
            //图片同比例缩放-默认
        $('.brand-logo img').jqthumb({
            width: 150,
            height: 50,
            after: function (imgObj) {
                imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
            }
        });
        $("#addFormFile").fileupload({
            dataType: 'json',
            url: ncGlobal.adminRoot + "image/upload.json",
            done: function (e, data) {
                if (data.result.code == 200) {
                    $('#addFormBrandImage').val(data.result.data.name);
                    $('#addFormBrandImageImg').attr('src', data.result.data.url);
                    //图片同比例缩放-新增
                    $('.brand-logo img').jqthumb({
                        width: 150,
                        height: 50,
                        after: function (imgObj) {
                            imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                        }
                    });
                } else {
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: data.result.message
                    })
                }
            }
        });
        /**
         * 上传插件绑定
         */
        $("#editFormFile").fileupload({
            dataType: 'json',
            url: ncGlobal.adminRoot + "image/upload.json",
            done: function (e, data) {
                if (data.result.code == 200) {
                    $('#editFormBrandImage').val(data.result.data.name);
                    $('#editFormBrandImageImg').attr('src', data.result.data.url);
                    //图片同比例缩放-编辑
                    $('.brand-logo img').jqthumb({
                        width: 150,
                        height: 50,
                        after: function (imgObj) {
                            imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                        }
                    });
                } else {
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: data.result.message
                    })
                }
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
            grid.fastQueryParameters['lk_brandName'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
        //调用checkbox方法
        $("[name='isRecommend']").bootstrapSwitch();

        // 添加选择品牌标签
        $("#addFormBrandLabelsAll").on('click', 'div', function () {
            if ($(this).hasClass("checked")) {
                return false;
            }
            $('<div class="btn btn-xs btn-default m-r-5" >'
                + $(this).text()
                + '<input type="hidden" name="brandLabelId" value="' + $(this).attr("data-id") + '" />'
                + '<i class="fa fa-times m-l-10"></i></div>')
                .appendTo($("#addFormBrandLabelsChecked"));
            $(this).hide();
        })
        $("#addFormBrandLabelsChecked").on('click', 'div', function () {
            var _val = $(this).find('input').val();
            $(this).remove();
            $('div[data-id="' + _val + '"]').show();
        })
        // 编辑选择品牌标签
        $("#editFormBrandLabelsAll").on('click', 'div', function () {
            if ($(this).hasClass("checked")) {
                return false;
            }
            $('<div class="btn btn-xs btn-white m-r-5" >'
                + $(this).text()
                + '<input type="hidden" name="brandLabelId" value="' + $(this).attr("data-id") + '" />'
                + '<i class="fa fa-times m-l-10"></i></div>')
                .appendTo($("#editFormBrandLabelsChecked"));
            $(this).hide();
        })
        $("#editFormBrandLabelsChecked").on('click', 'div', function () {
            var _val = $(this).find('input').val();
            $(this).remove();
            $('div[data-id="' + _val + '"]').show();
        })

        /**
         * 新增品牌
         */
        // $("#addModal").on("show.bs.modal", function (e) {
        $("#addForm").on("nc.formSubmit.success", function (e) {
            //重新
            addBrand.initAddModel();
        })

        /**
         * 编辑品牌
         */
        $("#editModal").on("show.bs.modal", function (e) {
            //获取接受事件的元素
            //获取data 参数
            var datano = $(e.relatedTarget).data('no'),
            //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];

            $('#editFormBrandId').val(gridData.brandId);
            $('#editFormApplyState').val(gridData.applyState);
            $('#editFormBrandName').val(gridData.brandName);
            $('#editFormBrandEnglish').val(gridData.brandEnglish);
            $('#editFormBrandImage').val(gridData.brandImage);
            $('#editFormBrandImageImg').attr('src', ncGlobal.uploadRoot + gridData.brandImage);
            if (gridData.isRecommend) {
                $('#editFormIsRecommend').bootstrapSwitch('state', true);
            } else {
                $('#editFormIsRecommend').bootstrapSwitch('state', false);
            }
            if (gridData.showType) {
                $('#editFormShowType1').prop('checked', true);
            } else {
                $('#editFormShowType0').prop('checked', true);
            }
            $('#editFormBrandSort').val(gridData.brandSort);

            //图片同比例缩放-默认
            $('.brand-logo img').jqthumb({
                width: 150,
                height: 50,
                after: function (imgObj) {
                    imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                }
            });
            //已选标签重置
            $("#editFormBrandLabelsChecked").empty()
            //未选标签重置
            $("#editFormBrandLabelsAll").find("div[data-id]").show();

            if (gridData.brandLabelIds != null) {
                $.each(gridData.brandLabelIds.split(','), function (i, e) {
                    $("#editFormBrandLabelsAll").find('div[data-id="' + e + '"]').click();
                })
            }

            //清楚错误信息
            $(".alert-danger").remove();
            $("#editForm").psly().reset();
        })
    }

    /**
     * 删除品牌
     */
    function _delBrand(id) {
        $.ncConfirm({
            url: delUrl,
            data: {
                brandId: id
            }
        });
    }

    /**
     * 删除品牌图片
     */
    function _delBrandImg() {
        $('#editFormBrandImageImg').attr('src', ncGlobal.imgRoot + "default_image.gif");
        $('#editFormBrandImage').val("");

        //图片同比例缩放-默认
        $('.brand-logo img').jqthumb({
            width: 150,
            height: 50,
            after: function (imgObj) {
                imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
            }
        });
    }

    return {
        init: function () {
            _buildHTML();
            _bindEvent();
        },
        delBrand: _delBrand,
        delBrandImg: _delBrandImg,
        tipShowPic:function(){

        }
    }
}()

$(function () {
    grid.load();
    // 页面时间绑定
    brand.init();
})