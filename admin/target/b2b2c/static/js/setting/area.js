/**
 * Created by cj on 2015/11/21.
 */

var dtGridColumns = [{
    id: 'areaId',
    title: '地区ID',
    type: 'number',
    columnClass: 'text-center',
    fastQuery: false,
    fastQueryType: 'eq',
    hideType:'md|sm|xs'
}, {
    id: 'areaName',
    title: '地区名称',
    type: 'string',
    columnClass: 'text-center',
    fastQuery: true,
    fastQueryType: 'lk',
}, {
    id: 'areaRegion',
    title: '所属大区',
    type: 'string',
    columnClass: 'text-center',
    fastQuery: true,
    fastQueryType: 'eq',
    fastSort: false,
    resolution: function (value, record, column, grid, dataNo, columnNo) {
        return value.length > 0 ? value : "--";
    },
    hideType:'md|sm|xs'
}, {
    id: 'areaDeep',
    title: '所在层级',
    type: 'number',
    columnClass: 'text-center'
}, {
    id: 'areaParentName',
    title: '上级',
    type: 'string',
    columnClass: 'text-center',
    fastSort: false,
    hideType:'md|sm|xs',
    resolution: function (value, record, column, grid, dataNo, columnNo) {
        return value != '' ? value : "--";
    },
}, {
    id: 'operation',
    title: '管理操作',
    type: 'string',
    columnClass: 'text-center width-300',
    fastSort: false,
    extra: false,
    resolution: function (value, record, column, grid, dataNo, columnNo) {
        var content = "";
        content += '<div class="btn-group">' +
            '<a href="javascript:;" data-toggle="dropdown" aria-expanded="false" class="btn btn-primary btn-sm dropdown-toggle m-r-10">' +
            '<i class="fa fa-gears"></i>&nbsp;编辑&nbsp;' +
            '<span class="caret"></span></a><ul class="dropdown-menu dropdown-menu-left">';
        content += "<li><a href='#editModal' data-no='" + dataNo + "' data-toggle='modal'><i class='fa fa-lg fa-pencil-square m-r-10' ></i>&nbsp;编辑地区信息</a></li>";
        if(record.areaDeep < 4 ){
            content += '<li><a href="#addChildModal" data-toggle="modal" title="新增下级地区" data-no="' + dataNo + '"><i class="fa fa-lg fa-plus m-r-10"></i>新增下级地区</a></li>';
        }
        content += '</ul></div>';
        content += '<a href="javascript:;" class="btn btn-danger btn-sm m-r-10" name="delete" data-dataNo="' + dataNo + '"><i class="fa fa-lg fa-trash-o m-r-10"></i>删除</a>';
        if(record.areaDeep < 4 ) {
            content += '<a href="javascript:;" name="showChildren" data-dataNo="' + dataNo + '" class="btn btn-white btn-sm"><i class="fa fa-level-down"></i>下级地区</a>';
        }
        return content;
    }
},];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'area/list.json',
    exportFileName: '地区列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 50,
    pageSizeLimit: [50],
    ncColumnsType: {
        int: ["areaId", "areaParentId"]
    },
    onGridComplete: function (grid) {
        var returnBtn = $("#returnParent"),
            lastArea = area.current.getLastInfo()
            ;
        //是否显示上一级
        if (lastArea !== '') {
            //重新绑定事件
            returnBtn.removeClass("hidden").off("click")
                .on("click", function () {
                    area.current.gridGoFormParentId(lastArea.areaParentId)
                    //bycj[ 设置地区信息 ]
                    area.current.delList(lastArea.areaDeep);
                });
        } else {
            returnBtn.addClass("hidden");
        }

        $("a[name='edit']").on('click', function () {
            var dataNo = $(this).attr("data-dataNo");
            $('a[data-toggle="modal"]').click();
        });

        /**
         * 查看下一级地区按钮事件
         */
        $("a[name='showChildren']").on('click', function () {
            var dataNo = $(this).attr("data-dataNo");
            areaInfo = grid.exhibitDatas[dataNo]
            ;
            //bycj[ 设置地区信息 ]
            area.current.setList(areaInfo.areaDeep, areaInfo);
            area.current.gridGoFormParentId(areaInfo.areaId);

        });
        /**
         * 列表中的删除事件
         */
        $("[name='delete']").on('click', function () {
            var dataNo = $(this).attr("data-dataNo"),
                ed = grid.exhibitDatas[dataNo],
                tpl = '您选择对地区 <strong>' + ed.areaName + '</strong> 进行删除操作，系统将会把选中地区及其所有子地区删除。<br/>您确定要进行删除操作吗?';
            $.ncConfirm({
                url: ncGlobal.adminRoot + 'area/del',
                data: {
                    areaId: ed.areaId
                },
                content: tpl
            });
        });
    }
};
var grid = $.fn.DtGrid.init(dtGridOption);


//自定义查询
function customSearch() {
    grid.fastQueryParameters = new Object();
    grid.fastQueryParameters['lk_areaName'] = $('#keyword').val();
    grid.pager.startRecord= 0;
    grid.pager.nowPage = 1;
    grid.pager.recordCount = -1;
    grid.pager.pageCount = -1;
    grid.refresh(true);
}

/**
 * 自定义相关
 */
var area = function ($) {
    /**
     *
     * @type {{}}
     */
    var current = {
        /**
         * 储存地区列表
         */
        $$list: {},
        /**
         * 修改选择的地区列表
         * @param deep
         * @param obj
         */
        setList: function (deep, obj) {
            deep && obj && (this.$$list [deep] = obj)
        },
        /**
         * 返回列表中指定深度的地区
         * @param deep
         * @returns {*}
         */
        getList: function (deep) {
            return this.$$list [deep];
        },
        /**
         * 删除
         * @param deep
         */
        delList: function (deep) {
            delete this.$$list[deep];
        },
        /**
         * 获取当前所有 的地区名字
         */
        getAreaInfo: function () {
            var r = '', n;
            for (n in this.$$list) {
                r += this.$$list[n].areaName + ' ';
            }
            return r;
        },
        /**
         * 获取上一级信息
         */
        getLastInfo: function () {
            var i = 5;
            while (i) {
                if (this.$$list.hasOwnProperty(i)) {
                    return this.$$list[i];
                }
                i--;
            }
            return '';
        },
        /**
         * 根据父级地区id设置grid
         * @param parentId
         */
        gridGoFormParentId:function(parentId){
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['eq_areaParentId'] = parentId;
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        },
        /**
         * 有上级地区的新增地区
         */
        addNewArea: function (areaRegionPanel, gridData) {

            var //地区内容
                areaInfo = current.getAreaInfo(),
                //地区文字
                areaText = areaInfo == '' ? gridData.areaName: areaInfo;

            var areaDeep = $("<input>", {
                name: "areaDeep",
                type: "hidden",
                value: gridData.areaDeep + 1
            });
            var areaParentId = $("<input>", {
                name: "areaParentId",
                type: "hidden",
                value: gridData.areaId
            })
            var i = $("<input>", {
                "class": "form-control",
                disabled: true,
                value: areaText
            });
            var a = $("<div>", {
                "class": "input-group-btn",
                html: '<button type="button" class="btn btn-success">编辑</button>'
            }).on("click", function () {
                areaRegionPanel.removeClass("input-group").empty().data("nc.area", '').off("change")
                    .NcArea({
                        url: ncGlobal.adminRoot + "area/list.json/",
                        showDeep: 3,
                        dataFormat: "areaList"
                    });
            });
            areaRegionPanel
                .empty().append(areaParentId, areaDeep, i, a)
                .addClass(function () {
                    if (!$(this).hasClass("input-group")) {
                        return "input-group";
                    }
                });
        }


    };

    /**
     * 绑定事件
     */
    function bindEvent() {
        /**
         * 地区联动
         */
        $("#add_area_panel").NcArea({
            url: ncGlobal.adminRoot + "area/list.json/",
            showDeep: 3,
            dataFormat: "areaList"
        });
        //调用select方法
        $(".selectpicker").selectpicker("render");
        /**
        * 表单提交成功后的事件
         */
        $("#addForm").on("nc.formSubmit.success",function(){
            //重新
            var $addForm = $("#addForm"),
                $areaName = $addForm.find("input[name='areaName']"),
                $add_area_panel = $("#add_area_panel"),
                lastArea = current.getLastInfo(),
                $areaRegion = $addForm.find("input[name='areaRegion']");

            $(".alert-danger").remove();
            formPsly = $addForm.psly();
            formPsly.reset();
            $areaName.val('');
            $areaRegion.val('');

            lastArea == ''
                ?($add_area_panel.removeClass("input-group").data('nc.area').restart())
                :(current.addNewArea($add_area_panel,lastArea));
        });


        /**
         * 编辑地区modal 显示事件
         */
        $("#editModal").on("show.bs.modal", function (e) {

            var $editForm = $("#editForm"),
                $areaId = $editForm.find("input[name='areaId']"),
                $areaName = $editForm.find("input[name='areaName']"),
                $areaRegion = $editForm.find("input[name='areaRegion']"),
                datano = $(e.relatedTarget).data('no'),
            //获取列表框中的原始数据
                gridData = grid.exhibitDatas[datano];
            //删除错误信息
            $(".alert-danger").remove();
            formPsly = $editForm.psly().reset();
            //修改数值
            $areaId.val(gridData.areaId);
            $areaName.val(gridData.areaName);
            $areaRegion.val(gridData.areaRegion);
            $("#editFormAreaDeep").val(gridData.areaDeep);
            $("#editFormAreaParentId").val(gridData.areaParentId);
            $editForm.find('input[name="areaDeep"]').val(gridData.areaDeep);
            $editForm.find('input[name="areaParentId"]').val(gridData.areaParentId);

        });

        /**
         * 新增下级地区对话框显示事件
         */
        $("#addChildModal").on("show.bs.modal", function (e) {
            var datano = $(e.relatedTarget).data('no'),
                areaRegionPanel = $("#areaRegionPanel"),
                gridData = grid.exhibitDatas[datano];


            //清空数据
            $("#addChildForm").psly().reset();
            areaRegionPanel.find(".alert-danger").remove();
            $("#addChildAreaName").val("");
            $("#addChildAreaRegion").val("");
            //添加编辑按钮
            current.addNewArea(areaRegionPanel,gridData);

        });
    }

    /**
     * 返回
     */
    return {
        init: function () {
            bindEvent();
        },
        current: current
    };
}(jQuery);


$(function () {
    grid.fastQueryParameters = new Object();
    grid.fastQueryParameters['eq_areaParentId'] = 0;
    grid.sortParameter.columnId = 'areaId';
    grid.sortParameter.sortType = 1;
    grid.load();
    //绑定方法
    $('#customSearch').click(customSearch);
    area.init();
});