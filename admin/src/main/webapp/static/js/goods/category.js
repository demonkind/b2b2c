/**
 * Created by shopnc.feng on 2015-11-24.
 */
var handleBootstrapWizardsValidation = function() {
    "use strict";
    $("#wizard").bwizard({
        validating: function(e, t) {
            if (t.index == 0) {
                if (false === $('form[name="form-wizard"]').parsley().validate("wizard-step-1")) {
                    return false
                    maxlength
                }
            } else if (t.index == 1) {
                if (false === $('form[name="form-wizard"]').parsley().validate("wizard-step-2")) {
                    return false
                }
            } else if (t.index == 2) {
                if (false === $('form[name="form-wizard"]').parsley().validate("wizard-step-3")) {
                    return false
                }
            }
        }
    })
};
var FormWizardValidation = function() {
    "use strict";
    return {
        init: function() {
            handleBootstrapWizardsValidation()
        }
    }
}()

var dtGridColumns = [{
    id: 'categorySort',
    title: '排序',
    type: 'string',
    headerClass: 'text-center width-50',
    columnClass: 'text-center width-50',
    hideType: 'sm|xs',
    fastQuery: true,
    fastQueryType: 'eq'
}, {
    id: 'categoryName',
    title: '分类名称',
    type: 'string',
    headerClass: 'text-left width-150',
    columnClass: 'text-left width-150',
    fastQuery: true,
    fastQueryType: 'lk'
}, {
    id: 'parentName',
    title: '上级分类',
    type: 'string',
    headerClass: 'text-left width-150',
    columnClass: 'text-left width-150',
    fastQuery: false,
    fastSort: false
}, {
    id: 'brandNames',
    title: '绑定品牌',
    type: 'string',
    headerClass: 'text-left',
    columnClass: 'text-left',
    hideType: 'lg|md|sm|xs',
    fastQuery: false,
    fastSort: false
}, {
    id: 'attributeNames',
    title: '分类属性',
    type: 'string',
    headerClass: 'text-left',
    columnClass: 'text-left',
    hideType: 'md|sm|xs',
    fastQuery: false,
    fastSort: false
}, {
    id: 'customsNames',
    title: '自定义属性',
    type: 'string',
    headerClass: 'text-left',
    columnClass: 'text-left',
    hideType: 'md|sm|xs',
    fastQuery: false,
    fastSort: false
}, {
    id: 'operation',
    title: '操作管理',
    type: 'string',
    columnClass: 'text-center width-300',
    fastSort: false,
    extra: false,
    resolution: function(value, record, column, grid, dataNo, columnNo) {
        var content = '';
        content += '<div class="btn-group">' +
            '<a data-toggle="dropdown" href="javascript:;" aria-expanded="false" class="btn btn-primary btn-sm dropdown-toggle m-r-10"><i class="fa fa-gears"></i>&nbsp;编辑&nbsp;<span class="caret"></span></a>' +
            '<ul class="dropdown-menu dropdown-menu-left">' +
            '<li><a href="#editModalInfo" data-toggle="modal" data-no="' + dataNo + '"><i class="fa fa-lg fa-pencil-square m-r-10"></i>编辑分类信息</a></li>' +
            '<li><a href="#editModalBrand" data-toggle="modal" data-no="' + dataNo + '"><i class="fa fa-lg fa-tags m-r-10"></i>编辑绑定品牌</a></li>' +
            '<li><a href="#editModalATE" data-toggle="modal" data-no="' + dataNo + '"><i class="fa fa-lg fa-th-list m-r-10"></i>编辑分类属性</a></li>' +
            '<li><a href="#addModalATE" data-toggle="modal" data-id="' + record.categoryId + '"><i class="fa fa-lg fa-plus m-r-10"></i>添加分类属性</a></li>' +
            '</ul>' +
            '</div>';
        content += '<a href="javascript:;" class="btn btn-danger btn-sm" onclick="category.delCategory(' + record.categoryId + ',\'' + record.categoryName + '\')"><i class="fa fa-trash-o"></i>&nbsp;删除&nbsp;</a>';
        if (record.deep < 3) {
            content += '<a href="javascript:;" name="showChildren" data-dataNo="' + dataNo + '" class="btn btn-white btn-sm m-l-10"><i class="fa fa-level-down"></i>&nbsp;下级分类&nbsp;</a>';
        }
        return content;
    }
}];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'category/list.json',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 50,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {
        int: ["categorySort", "parentId"]
    },
    onGridComplete: function(grid) {
        var returnBtn = $("#returnParent"),
            lastCategory = category.current.getLastInfo();
        //是否显示上一级
        if (lastCategory !== '') {
            //重新绑定事件
            returnBtn.removeClass("hidden").off("click")
                .on("click", function() {
                    category.current.gridGoFormParentId(lastCategory.parentId);
                    category.current.delList(lastCategory.deep);
                });
        } else {
            returnBtn.addClass("hidden");
        }

        $("a[name='edit']").on('click', function() {
            var dataNo = $(this).attr("data-dataNo");
            $('a[data-toggle="modal"]').click();
        });

        /**
         * 查看下一级按钮事件
         */
        $("a[name='showChildren']").on('click', function() {
            var dataNo = $(this).attr("data-dataNo"),
                categoryInfo = grid.exhibitDatas[dataNo];

            category.current.setList(categoryInfo.deep, categoryInfo);
            category.current.gridGoFormParentId(categoryInfo.categoryId);

        });
    }
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_parentId'] = 0;
grid.sortParameter.columnId = 'categorySort';
grid.sortParameter.sortType = 1;

var category = function() {
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
        setList: function(deep, obj) {
            deep && obj && (this.$$list[deep] = obj)
        },
        /**
         * 返回列表中指定深度的地区
         * @param deep
         * @returns {*}
         */
        getList: function(deep) {
            return this.$$list[deep];
        },
        /**
         * 删除
         * @param deep
         */
        delList: function(deep) {
            delete this.$$list[deep];
        },
        /**
         * 获取当前所有 的地区名字
         */
        getCategoryInfo: function() {
            var r = '',
                n;
            for (n in this.$$list) {
                r += this.$$list[n].areaName + ' ';
            }
            return r;
        },
        /**
         * 获取上一级信息
         */
        getLastInfo: function() {
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
        gridGoFormParentId: function(parentId) {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['eq_parentId'] = parentId;
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        }

    };

    // 分类删除地址
    var delUrl = ncGlobal.adminRoot + "category/delete.json",
        delAttributeValueUrl = ncGlobal.adminRoot + "category/attribute_value/delete.json",
        delCustomUrl = ncGlobal.adminRoot + "category/custom/delete.json";

    function _getBrandAll() {
        if (typeof(_getBrandAllParam) != 'undefined') {
            return _getBrandAllParam;
        } else {
            var _getBrandAllParam = [];
            $.ajax({
                url: ncGlobal.adminRoot + "category/brand_all.json",
                async: false,
                success: function(data) {
                    if (data.code == 200) {
                        _getBrandAllParam.push({
                            id: 0,
                            text: "快速搜索品牌"
                        })
                        $.each(data.data.brands, function(i, e) {
                            _getBrandAllParam.push({
                                id: e.brandId,
                                text: e.brandName
                            })
                        })
                    }
                }
            })

        }
        return _getBrandAllParam;
    }

    function _bindEvent() {
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_categoryName'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
        /******  级联开始 开始  ******/
        $("#goods_class").ncCategory({
                url: ncGlobal.adminRoot + "category/list.json/",
                showDeep: 2
            })
            /******  级联开始 结束  ******/

        /******  新增分类三步效验 开始  ******/
        FormWizardValidation.init();
        /******  checkbox按钮开关方法 开始  ******/
        $("#attributeState").bootstrapSwitch();
        /******  checkbox按钮开关方法 结束  ******/

        /******  表单输入项标签化 开始  ******/
        // 属性值
        $("#jquery-tagIt-success").tagit({
            availableTags: ["c++", "java", "php", "javascript", "ruby", "python", "c"],
            fieldName: "attributeValues"
        });
        // 自定义属性
        $("#jquery-tagIt-success2").tagit({
            availableTags: ["c++", "java", "php", "javascript", "ruby", "python", "c"],
            fieldName: "custom"
        });
        /******  表单输入项标签化 结束  ******/

        /******  品牌选择 开始  *****/
        // 添加品牌选择
        $("#addFormBrandAll").on('click', 'div', function() {
            if ($(this).hasClass("checked")) {
                return false;
            }
            $('<div class="btn btn-xs btn-default m-r-5" >' + $(this).text() + '<input type="hidden" name="brandId" value="' + $(this).attr("data-id") + '" />' + '<i class="fa fa-times m-l-10"></i></div>')
                .appendTo($("#addFormBrandChecked"));
            $(this).addClass("checked").hide();
        });
        $("#addFormBrandChecked").on('click', 'div', function() {
            var _val = $(this).find('input').val();
            $(this).remove();
            $('div[data-id="' + _val + '"]').removeClass("checked").show();
        });

        // 编辑品牌选择
        $("#editFormBrandAll").on('click', 'div', function() {
            if ($(this).hasClass("checked")) {
                return false;
            }
            $('<div class="btn btn-xs btn-default m-r-5" >' + $(this).text() + '<input type="hidden" name="brandId" value="' + $(this).attr("data-id") + '" />' + '<i class="fa fa-times m-l-10"></i></div>')
                .appendTo($("#editFormBrandChecked"));
            $(this).addClass("checked").hide();
        });
        $("#editFormBrandChecked").on('click', 'div', function() {
            var _val = $(this).find('input').val();
            $(this).remove();
            $('div[data-id="' + _val + '"]').removeClass("checked").show();
        });
        /******  品牌选择 结束  *****/
        _buildAttribute();

        // 新增数据成功后调用
        $('#addModal').on('show.bs.modal', function(event) {
            var t = _getBrandAll();
            $("#addFormBrandSearch").editable({
                mode: "inline",
                inputclass: "form-control input-sm",
                source: t,
                select2: {
                    width: 200,
                    placeholder: "请输入一个品牌关键字",
                    allowClear: true
                },
                type: "select",
                display: true,
                success: function(response, newValue) {
                    $('div[data-id="' + newValue + '"]').click();
                }
            });
        })
        $('#addForm').on('nc.formSubmit.success', function(event) {
            var addForm = $("#addForm"),
                categoryName = addForm.find("input[name='categoryName']"),
                categorySort = addForm.find("input[name='categorySort']");

            categoryName.val('');
            categorySort.val('0');
            $("#goods_class").data("nc.category").restart()

            $('#addFormBrandChecked').find('div').click();
            // 初始化bwizard
            $("#wizard").find('li:first').click();
            //清除错误提示
            addForm.psly().reset();
            $(".alert-danger").remove();
        });



        // 编辑分类信息时调用
        $('#editModalInfo').on('show.bs.modal', function(event) {
            var //获取接受事件的元素
                datano = $(event.relatedTarget).data('no'),
                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano],
                editForm = $("#editFormInfo");

            $(this).find('input[name="categoryId"]').val(gridData.categoryId)
            $(this).find('input[name="categoryName"]').val(gridData.categoryName)
            $(this).find('input[name="parentId"]').val(gridData.parentId)
            $(this).find('input[name="categorySort"]').val(gridData.categorySort)
            $(this).find('input[name="deep"]').val(gridData.deep)
            $(this).find('input[name="parentName"]').val(gridData.parentName)
                //清除错误提示
            editForm.psly().reset();
            $(".alert-danger").remove()
        })

        $('#editModalBrand').on('show.bs.modal', function(event) {
            var //获取接受事件的元素
                datano = $(event.relatedTarget).data('no'),
                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];

            $(this).find('[name="categoryId"]').val(gridData.categoryId);

            var t = _getBrandAll();
            $("#editFormBrandSearch").editable({
                mode: "inline",
                inputclass: "form-control input-sm",
                source: t,
                select2: {
                    width: 200,
                    placeholder: "Select editFormBrandSearch",
                    allowClear: true
                },
                type: "select",
                display: true,
                success: function(response, newValue) {
                    $('div[data-id="' + newValue + '"]').click();
                }
            });

            $('#editFormBrandChecked').find('div').click();

            if (gridData.brandIds != null) {
                $.each(gridData.brandIds.split(','), function(i, e) {
                    $("#editFormBrandAll").find('div[data-id="' + e + '"]').click();
                })
            }
        })


        $('#addModalATE').on('show.bs.modal', function(event) {
            var _categoryId = $(event.relatedTarget).data('id');
            $(this).find("[name='categoryId']").val(_categoryId);


            $("#editATEVessel").html('');
            $("#editATECustom").find('a').click();
            /******  表单输入项标签化 开始  ******/
            // 属性值
            $("#editATEAttributeValues").tagit({
                availableTags: ["c++", "java", "php", "javascript", "ruby", "python", "c"],
                fieldName: "attributeValues"
            });
            // 自定义属性值
            $("#editATECustom").tagit({
                availableTags: ["c++", "java", "php", "javascript", "ruby", "python", "c"],
                fieldName: "custom"
            });
            /******  表单输入项标签化 结束  ******/


            _buildAttribute({
                addButton: "#editATEButton",
                sort: "#editATEAttributeSort",
                name: "#editATEAttributeName",
                values: "#editATEAttributeValues",
                state: "#editATEAttributeState",
                vessel: "#editATEVessel"
            })


            /******  checkbox按钮开关方法 开始  ******/
            $("#editATEAttributeState").bootstrapSwitch();
            /******  checkbox按钮开关方法 结束  ******/
        })

        $('#editModalATE').on('show.bs.modal', function(event) {
            var //获取接受事件的元素
                datano = $(event.relatedTarget).data('no'),
                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];
            // 分类编号
            $(this).find("[name='categoryId']").val(gridData.categoryId);
            $(this).find("a[href='#addModalATE']").data('id', gridData.categoryId);
            // 原自定义属性值
            $("#editATECustomOld").html(gridData.customsNames).data('id', gridData.categoryId);


            // 异步取得属性数据
            $.getJSON(ncGlobal.adminRoot + "category/attribute/list.json/" + gridData.categoryId, function(data) {
                if (data.code != 200) {
                    return false
                }
                $('#editATEDiv').html('');
                $.each(data.data.attributeAndValueVos, function(i, e) {
                    var _content = '<div class="row p-b-10 m-b-10 border-bottom-1"><div class="col-sm-1">' +
                        '<a href="javascript:;" class="editATESort" data-type="text" data-pk="' + e.attributeId + '" data-title="编辑分类属性排序">' +
                        e.attributeSort + '</a></div><div class="col-sm-2 p-l-0">' +
                        '<a href="javascript:;" class="editATEName" data-type="text" data-pk="' + e.attributeId + '" data-title="编辑分类属性名称">' +
                        e.attributeName + '</a></div><div class="col-sm-6 p-l-0">' +
                        '<a href="#addAttributeValueModalATE" data-id="' + e.attributeId + '" data-name="' + e.attributeName + '" data-values="' + e.attributeValueNames + '" class="m-r-10 btn btn-xs btn-white" data-toggle="modal" data-dismiss="modal" aria-hidden="true"> <i class="fa fa-plus"></i> &nbsp;新增 </a>' +
                        '<a href="#editAttributeValueModelATE" data-id="' + e.attributeId + '" data-name="' + e.attributeName + '" data-toggle="modal" class="editable editable-click" data-dismiss="modal" aria-hidden="true">' +
                        e.attributeValueNames + '</a></div>' +
                        '<div class="col-sm-3 p-l-0">' +
                        '<input type="checkbox" value="1" class="attributeState" autocomplete="off" data-id="' + e.attributeId + '" data-size="mini" data-on-color="primary" data-on-text="显示" data-off-text="隐藏" ' +
                        (e.isShow ? 'checked' : '') + '/>' +
                        '<a href="javascript:;" class="btn btn-danger btn-xs m-l-20" onclick="category.delAttribute(' + e.attributeId + ', \'' + e.attributeName + '\', this)"><i class="fa fa-trash-o"></i>&nbsp;删除&nbsp;</a></div>' +
                        '</div>';
                    $('#editATEDiv').append(_content);
                })

                //编辑分类属性排序
                $(".editATESort").editable({
                    mode: "popup",
                    url: ncGlobal.adminRoot + "category/attribute/sort/update.json",
                    params: function(params) {
                        return {
                            attributeSort: params.value,
                            attributeId: params.pk
                        }
                    },
                    validate: function(e) {
                        if ($.trim(e) === "") {
                            return "属性排序为必填项"
                        }
                    },
                    success: function(response, newValue) {
                        if (response.code == 400) {
                            // 模态框报错
                            $.ncAlert({
                                closeButtonText: "关闭",
                                autoCloseTime: 3,
                                content: response.message
                            })
                        }
                    }
                });
                //编辑分类属性名称
                $(".editATEName").editable({
                    mode: "popup",
                    url: ncGlobal.adminRoot + "category/attribute/name/update.json",
                    params: function(params) {
                        return {
                            attributeName: params.value,
                            attributeId: params.pk
                        }
                    },
                    validate: function(e) {
                        if ($.trim(e) === "") {
                            return "属性名称为必填项"
                        }
                    },
                    success: function(response, newValue) {
                        if (response.code == 400) {
                            // 模态框报错
                            $.ncAlert({
                                closeButtonText: "关闭",
                                autoCloseTime: 3,
                                content: response.message
                            })
                        }
                    }
                });

                /******  checkbox按钮开关方法 开始  ******/
                $(".attributeState").bootstrapSwitch('onSwitchChange', function(event, state) {
                    var attributeId = $(this).data('id');
                    $.ajax({
                        type: 'POST',
                        url: ncGlobal.adminRoot + 'category/attribute/is_show/update.json',
                        dataType: 'json',
                        data: {
                            isShow: state ? 1 : 0,
                            attributeId: attributeId
                        }
                    });
                });
                /******  checkbox按钮开关方法 结束  ******/
            })

        })

        $('#editAttributeValueModelATE').on('show.bs.modal', function(event) {
            var attributeId = $(event.relatedTarget).data('id'),
                attributeName = $(event.relatedTarget).data('name');

            $('#editAttributeValueModelATESpan').html(attributeName);
            $.getJSON(ncGlobal.adminRoot + "category/attribute_value/list.json/" + attributeId, function(data) {
                if (data.code == 400) {
                    $('#editModal').modal('hide');
                    // 模态框报错
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: data.message
                    })
                }
                $("#editAttributeValueDiv").html('');
                var attributeValues = data.data.attributeValues;
                $.each(attributeValues, function(i, e) {
                    $('<div class="row p-b-10 m-b-10 border-bottom-1"><div class="col-sm-8 col-xs-9">' +
                            '<a href="javascript:;" class="editATEValue editable editable-click" data-type="text" data-pk="' + e.attributeValueId + '" data-title="编辑分类属性值">' +
                            e.attributeValueName + '</a></div>' +
                            '<div class="col-sm-2 col-xs-1">&nbsp;</div>' +
                            '<div class="col-sm-2 col-xs-2">' +
                            '<a href="javascript:;" class="btn btn-danger btn-sm" onclick="category.delAttributeValue(' + e.attributeValueId + ', this)">' +
                            '<i class="fa fa-trash-o"></i>&nbsp;删除&nbsp;</a></div></div>')
                        .appendTo($("#editAttributeValueDiv"));
                })

                //编辑分类属性值
                $(".editATEValue").editable({
                    mode: "inline",
                    url: ncGlobal.adminRoot + "category/attribute_value/name/update.json",
                    params: function(params) {
                        return {
                            attributeValueName: params.value,
                            attributeValueId: params.pk
                        }
                    },
                    validate: function(e) {
                        if ($.trim(e) === "") {
                            return "属性值不能为空"
                        }
                    },
                    success: function(response, newValue) {
                        if (response.code == 400) {
                            // 模态框报错
                            $.ncAlert({
                                closeButtonText: "关闭",
                                autoCloseTime: 3,
                                content: response.message
                            })
                        }
                    }
                });
            });
        })

        $('#addAttributeValueModalATE').on('show.bs.modal', function(event) {
            var attributeId = $(event.relatedTarget).data('id'),
                attributeName = $(event.relatedTarget).data('name'),
                attributeValueNames = $(event.relatedTarget).data('values');

            // 属性id
            $('#addAttributeValueAttributeId').val(attributeId);
            // 属性值名称
            $('#addAttributeValueSpan').html(attributeName);
            // 原属性值
            $('#addAttributeValueOld').html(attributeValueNames);

            $('#addAttributeValueNew').find('a').click();

            $("#addAttributeValueNew").tagit({
                availableTags: ["c++", "java", "php", "javascript", "ruby", "python", "c"],
                fieldName: "attributeValueNames"
            });

        })
        $('#editATECustomOldModel').on('show.bs.modal', function(event) {
            var categoryId = $(event.relatedTarget).data('id');
            $.getJSON(ncGlobal.adminRoot + "category/custom/list.json/" + categoryId, function(data) {
                if (data.code == 400) {
                    $('#editModal').modal('hide');
                    // 模态框报错
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: data.message
                    })
                }

                var customs = data.data.customs;
                $.each(customs, function(i, e) {
                    $('<div class="row p-b-10 m-b-10 border-bottom-1"><div class="col-sm-8 col-xs-9">' +
                            '<a href="javascript:;" class="editCustomATE editATEValue editable editable-click" data-type="text" data-pk="' + e.customId + '" data-title="编辑自定义属性">' +
                            e.customName + '</a></div>' +
                            '<div class="col-sm-2 col-xs-1">&nbsp;</div>' +
                            '<div class="col-sm-2 col-xs-2">' +
                            '<a href="javascript:;" class="btn btn-danger btn-sm" onclick="category.delCustom(' + e.customId + ', this)">' +
                            '<i class="fa fa-trash-o"></i>&nbsp;删除&nbsp;</a></div></div>')
                        .appendTo($("#editCustomDiv"));
                })


                //编辑分类属性值
                $(".editCustomATE").editable({
                    mode: "inline",
                    url: ncGlobal.adminRoot + "category/custom_name/update.json",
                    params: function(params) {
                        return {
                            customName: params.value,
                            customId: params.pk
                        }
                    },
                    validate: function(e) {
                        if ($.trim(e) === "") {
                            return "属性值不能为空"
                        }
                    },
                    success: function(response, newValue) {
                        if (response.code == 400) {
                            // 模态框报错
                            $.ncAlert({
                                closeButtonText: "关闭",
                                autoCloseTime: 3,
                                content: response.message
                            })
                        }
                        grid && grid.reload(true);
                    }
                });
            })

        })
    }

    /**
     * 添加属性
     */
    function _buildAttribute(options) {
        var defaults = {
            addButton: "#attributeAdd",
            sort: "#attributeSort",
            name: "#attributeName",
            values: "#jquery-tagIt-success",
            state: "#attributeState",
            vessel: "#attributeVessel"
        }

        var options = $.extend({}, defaults, options);
        $(options.addButton).off('click').on('click', function() {
            var _attribute = '';
            var _sort = parseInt($(options.sort).val());
            if (isNaN(_sort)) {
                _sort = 0;
            }
            _attribute += _sort;
            var _name = $.trim($(options.name).val());
            if (_name == '') {
                _attributeError("请填写属性名称", options.vessel);
                return false;
            }
            _attribute += "||||" + _name;
            var _value = "";
            var _values = "";
            if ($(options.values).find('[name="attributeValues"]').length == 0) {
                _attributeError("请填写属性值", options.vessel);
                return false;
            }
            $(options.values).find('[name="attributeValues"]').each(function(i, e) {
                if (i > 0) {
                    _value += ',';
                    _values += '|||';
                }
                _value += $.trim($(e).val());
                _values += $.trim($(e).val());
            });
            _attribute += "||||" + _values;
            var _state = $(options.state).prop('checked');
            if (_state) {
                _state = "显示";
                _attribute += "||||1";
            } else {
                _state = "隐藏";
                _attribute += "||||0";
            }
            $('<div class="row p-b-10 m-b-10 border-bottom-1">' +
                    '<input type="hidden" name="attribute" value="' + _attribute + '" />' +
                    '<div class="col-sm-1">' + _sort + '</div>' +
                    '<div class="col-sm-2 p-l-0">' + _name + '</div>' +
                    '<div class="col-sm-6 p-l-0">' + _value + '</div>' +
                    '<div class="col-sm-3 p-l-0">' +
                    '<span style="display:inline-block; width:60px; padding-left:8px;">' + _state + '</span>' +
                    '<a href="javascript:;" class="btn btn-xs btn-danger m-l-30"><i class="fa fa-trash-o m-r-5"></i>删除</a></div>' +
                    '</div>')
                .on('click', 'a', function() {
                    $(this).parents('.row:first').remove();
                }).appendTo(options.vessel);
            $(options.sort).val("0");
            $(options.name).val("");
            $(options.values).find('a').click();
        })
    }

    /**
     * 删除分类
     */
    function _delCategory(id, categoryName) {
        var tpl = '您选择对分类 ' + categoryName + ' 进行删除操作,您确定要进行删除操作吗?'
        $.ncConfirm({
            url: delUrl,
            data: {
                categoryId: id
            },
            content: tpl
        });
    }

    /**
     * 删除属性
     */
    function _delAttribute(attributeId, attributeName, o) {
        var tpl = '您选择对属性 ' + attributeName + ' 进行删除操作，您确定要进行删除操作吗？'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "category/attribute/delete.json",
            data: {
                attributeId: attributeId
            },
            content: tpl,
            callBack: function() {
                grid && grid.reload(true);
                $(o).parents('.row:first').remove();
            }
        })
    }

    /**
     * 属性与错误提示
     * @param msg
     */
    function _attributeError(msg, vessel) {
        var _error = '<div class="alert alert-danger fade in m-b-15"><i class="fa fa-ban fa-2x pull-left"></i>' +
            msg + '<span class="close" data-dismiss="alert">×</span></div>';
        $(_error).insertAfter(vessel);
        setTimeout(function() {
            $(vessel).nextAll().find('span.close').click();
        }, 3000)
    }

    /**
     * 删除属性值
     */
    function _delAttributeValue(attributeValueId, o) {
        $.post(delAttributeValueUrl, {
            attributeValueId: attributeValueId
        }, function(data) {
            if (data.code == 200)
                $(o).parents('.row:first').remove();
        }, 'json');
    }

    /**
     * 删除自定义属性
     */
    function _delCustom(customId, o) {
        $.post(delCustomUrl, {
            customId: customId
        }, function(data) {
            if (data.code == 200) {
                $(o).parents('.row:first').remove();
                grid && grid.reload(true);
            }
        }, 'json');

    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delCategory: _delCategory,
        delAttribute: _delAttribute,
        delAttributeValue: _delAttributeValue,
        delCustom: _delCustom,
        current: current
    }
}()

$(function() {
    grid.load();
    //页面绑定事件
    category.bindEvent()
})