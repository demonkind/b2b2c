/**
 * Created by shopnc.feng on 2016-01-25.
 */

var storeLabel = function () {
    var _addLabel = function(id){
        $('#addParentId').val(id);

        Nc.layerOpen({
            title: "新增分类",
            content: $('#addModal'),
            $form:$("#addForm")
        })
    }

    var _editLabel = function(ele) {

        var storeLabelId = $(ele).data("id");
        var storeLabelName = $(ele).data("name");
        var parentId = $(ele).data("pid");
        var storeLabelSort = $(ele).data("sort");
        var isFold = $(ele).data('fold');
        $('#storeLabelId').val(storeLabelId);
        $('#storeLabelName').val(storeLabelName);
        $('#editParentId').val(parentId).prop("disabled", true);
        $('#storeLabelSort').val(storeLabelSort);
        if (parentId == 0) {
            $('#isFold1').parents("dl:first").show();
            if (isFold == 1) {
                $('#isFold1').prop('checked', true);
            } else {
                $('#isFold0').prop('checked', true);
            }
        } else {
            $('#isFold1').parents("dl:first").hide();
            $('#isFold0').prop('checked', true);
        }

        Nc.layerOpen({
            title: "编辑分类",
            content: $('#editModal'),
            $form:$("#editForm")
        })


    }

    var _deleteLabel = function(storeLabelId) {
        Nc.layerConfirm("是否进行删除", {
            postUrl: ncGlobal.sellerRoot + 'label/delete',
            postData: {storeLabelId: storeLabelId}
        });
    }

    var _init = function() {
        //通用验证设置
        var validateOptions = {
            invalidHandler: function (form, validator) {
                $(form).find('.alert-error').show();
            },
            submitHandler: function (form) {
                $(form).ajaxSubmit();
            },
            rules: {
                storeLabelName: {
                    required: true
                },
                storeLabelSort: {
                    number: true
                }
            },
            messages: {
                storeLabelName: {
                    required: '<i class="icon-exclamation-sign"></i>请填写分类名称'
                },
                storeLabelSort: {
                    number: '<i class="icon-exclamation-sign"></i>排序只能为数字'
                }
            }
        }
        //添加失败处理
        $("#addForm,#editForm")
            .on("nc.ajaxSubmit.error", function (e, errorMessage) {
                changeCaptcha();
                Nc.alertError(errorMessage);
            })
        $('#addForm').validate(validateOptions);
        $('#editForm').validate(validateOptions);
    }
    return {
        init:_init,
        addLabel:_addLabel,
        editLabel:_editLabel,
        deleteLabel:_deleteLabel
    }
}();
$(function(){
    storeLabel.init();
})