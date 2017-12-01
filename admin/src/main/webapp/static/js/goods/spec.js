/**
 * Created by shopnc.feng on 2015-12-01.
 */
var dtGridColumns = [
    {
        id: 'specName',
        title: '规格名称',
        type: 'string',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'specValueNames',
        title: '规格值',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'sm|xs',
        fastQuery: false,
        fastQueryType: 'eq',
        fastSort: false
    },
    {
        id: 'specSort',
        title: '排序',
        type: 'number',
        columnClass: 'text-center width-100',
        hideType: 'md|sm|xs',
        fastQuery: true,
        fastQueryType: 'eq'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-250',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var _content = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-id='" + record.specId + "'>" +
                "<i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            if (record.specId != 1) {
                _content += "<a href='javascript:;' class='btn btn-sm btn-danger' onclick='spec.delSpec(" + record.specId + ",\"" + record.specName + "\")'>" +
                "<i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
            }
            return _content;
        }
    },
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL : ncGlobal.adminRoot + 'spec/list.json',
    exportFileName: '商品规格列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools : 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50]
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'specId';
grid.sortParameter.sortType = 1;

var spec = function () {
    // 规格删除地址
    var delUrl = ncGlobal.adminRoot + "spec/delete.json",
    //  规格值删除地址
        delValueUrl = ncGlobal.adminRoot + "spec/value/delete.json";

    function _bindEvent () {
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_specName'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });

        //表单输入项标签化实例化
        $(".bootstrap-tagsinput input").focus(function() {
            $(this).closest(".bootstrap-tagsinput").addClass("bootstrap-tagsinput-focus");
        });
        $(".bootstrap-tagsinput input").focusout(function() {
            $(this).closest(".bootstrap-tagsinput").removeClass("bootstrap-tagsinput-focus");
        });

        $("#jquery-tagIt-success").tagit({
            availableTags: ["c++", "java", "php", "javascript", "ruby", "python", "c"],
            fieldName: "specValueNames"
        });
        $("#jquery-tagIt-success2").tagit({
            availableTags: ["c++", "java", "php", "javascript", "ruby", "python", "c"],
            fieldName: "specValueNames"
        });

        // 编辑规格信息时调用
        $('#editModal').on('show.bs.modal', function(event) {
            var    //获取接受事件的元素
                specId = $(event.relatedTarget).data('id'),
            //获取列表框中的原始数据
                editForm = $("#editForm")
                ;
            $('[href="#editModal"]').data('id', specId);
            $('[href="#addValueModal"]').data('id', specId);
            $.getJSON(ncGlobal.adminRoot + "spec/info_value.json/" + specId, function(data){
                if (data.code == 400) {
                    $('#editModal').modal('hide');
                    // 模态框报错
                    $.ncAlert({
                        closeButtonText:"关闭",
                        autoCloseTime:3,
                        content:data.message
                    })
                }
                var specAndValue = data.data.specAndValue;
                $('#specId1').val(specAndValue.specId);
                $('#specName1').val(specAndValue.specName);
                $('#specValueNames1').data('id', specAndValue.specId).data('name', specAndValue.specName).html(specAndValue.specValueNames);
                $('#specSort1').val(specAndValue.specSort);
            });

            //清除错误提示
            editForm.psly().reset();
            $(".alert-danger").remove()
        })



        // 编辑规格值是调用
        $('#editValueModal').on('show.bs.modal', function(event){
            var specId = $(event.relatedTarget).data('id'),
                specName = $(event.relatedTarget).data('name'),
                editValueForm = $('#editValueForm');
            $.getJSON(ncGlobal.adminRoot + "spec/values.json/" + specId, function (data) {
                if (data.code == 400) {
                    $('editValueModel').modal('hide');
                    // 模态框报错
                    $.ncAlert({
                        closeButtonText:"关闭",
                        autoCloseTime:3,
                        content:data.message
                    })
                }
                var specValues = data.data.specValues;
                $('#editValueTitleSpan').html('编辑“' + specName + '”的规格值');
                $('#editValueDiv').html('');
                $.each(specValues, function(i, e){
                    var editValueTemplate = '<div class="row p-b-10 m-b-10 border-bottom-1">' +
                        '<div class="col-sm-8 col-xs-9">' +
                        '<a href="javascript:;" class="editATEValue" data-type="text" data-pk="' + e.specValueId +  '" data-title="编辑规格值">' + e.specValueName + '</a>' +
                        '</div>' +
                        '<div class="col-sm-2 col-xs-1">&nbsp;</div>' +
                        '<div class="col-sm-2 col-xs-2">' +
                        '<a href="javascript:;" class="btn btn-danger btn-sm" onclick="spec.delSpecValue('+ e.specValueId +', this)"><i class="fa fa-trash-o"></i>&nbsp;删除&nbsp;</a>' +
                        '</div>' +

                        '</div>';
                    $('#editValueDiv').append($(editValueTemplate));
                })

                //编辑分类属性值
                $(".editATEValue").editable({
                    mode: "inline",
                    url:ncGlobal.adminRoot + "spec/value/update.json",
                    params: function(params) {
                        return {
                            specValueName:params.value,
                            specValueId:params.pk
                        }
                    },
                    validate: function(e) {
                        if ($.trim(e) === "") {
                            return "规格值不能为空"
                        }
                    },
                    success: function(response, newValue) {
                        if (response.code == 400) {
                            // 模态框报错
                            $.ncAlert({
                                closeButtonText:"关闭",
                                autoCloseTime:3,
                                content:response.message
                            })
                        }
                    }
                });
            })
            //清除错误提示
            editValueForm.psly().reset();
            $(".alert-danger").remove()
        })

        $('#addValueModal').on('show.bs.modal', function(event){
            var specId = $(event.relatedTarget).data('id')
            $.getJSON(ncGlobal.adminRoot + "spec/info_value.json/" + specId, function(data){
                if (data.code == 400) {
                    $('#editModal').modal('hide');
                    // 模态框报错
                    $.ncAlert({
                        closeButtonText:"关闭",
                        autoCloseTime:3,
                        content:data.message
                    })
                }
                var specAndValue = data.data.specAndValue;
                $('#addSpecValueSpan').html(specAndValue.specName);
                $('#addSpecValueOldValue').html(specAndValue.specValueNames);
                $('#addSpecValueSpecId').val(specAndValue.specId);
            });

        })

        $('#addForm').on('nc.formSubmit.success', function() {

            $('#specName').val('');
            $('#specSort').val(0);
            $('#jquery-tagIt-success').find('a').click();
            //清除错误提示
            $('#addForm').psly().reset();
            $(".alert-danger").remove()
        })

        $('#addValueForm').on('nc.formSubmit.success', function(){
            $('#jquery-tagIt-success2').find('a').click();
        })
    }

    /**
     * 删除规格
     */
    function _delSpec(specId,specName){
        var tpl = '您选择对规格 <strong>'+specName+'</strong> 进行删除操作, 删除规格将影响前台商家选择及商品发布设置与显示。<br/>您确定要进行删除操作吗?'
        $.ncConfirm({
            url:delUrl,
            data:{
                specId:specId
            },
            content:tpl
        });
    }

    /**
     * 删除规格值
     * @private
     */
    function _delSpecValue(specValueId, o) {
        $.post(delValueUrl, {specValueId:specValueId}, function(data) {
            if (data.code == 200)
                $(o).parents('.row:first').remove();
        }, 'json');
    }
    return {
        bindEvent:_bindEvent,
        delSpec:_delSpec,
        delSpecValue:_delSpecValue
    }
}()

$(function () {
    grid.load();
    //页面绑定事件
    spec.bindEvent()
});