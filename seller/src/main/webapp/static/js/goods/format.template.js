/**
 * Created by cj on 2015/12/17.
 */

var formatTemplate = function() {
    var _addFormatTemplate = function() {
        Nc.layerOpen({
            title: "新增关联板式",
            sizeEnum: "large",
            content: $("#addModal"),
            $form: $("#addForm")
        });
    };

    var _editFormatTemplate = function(formatId) {
        $.post(ncGlobal.sellerRoot + "format_template/edit.json", {
            formatId: formatId
        }, function(data) {
            if (data.code == 200) {
                $('#formatId').val(data.data.formatId);
                $('#formatName').val(data.data.formatName);
                $('#formatSite').val(data.data.formatSite);
                Nc.layerOpen({
                    title: "编辑管理板式",
                    sizeEnum: "large",
                    content: $("#editModal"),
                    $form: $("#editForm")
                });
                setTimeout(function() {
                    var a  = UE.getEditor('editEditor', {
                        textarea: "formatContent",
                        zIndex:19891017
                    });
                    a.setContent(data.data.formatContent);
                }, 300);
            } else {
                Nc.alertError(data.message);
            }
        }, "json");
    };

    var _deleteFormatTemplate = function(id, name) {
        Nc.layerConfirm("是否进行删除“" + name + "”？ 删除后绑定该板式的商品的板式将会丢失。", {
            postUrl: ncGlobal.sellerRoot + "format_template/delete.json",
            postData: {
                formatId: id
            }
        });
    };

    var _init = function() {
        //通用验证设置
        var validateOptions = {
            invalidHandler: function(form, validator) {
                $(form).find(".alert-error").show();
            },
            submitHandler: function(form) {
                $(form).ajaxSubmit();
            },
            rules: {
                formatName: {
                    required: true
                }
            },
            messages: {
                formatName: {
                    required: '<i class="icon-exclamation-sign"></i>请填写板式名称'
                }
            }
        };
        //添加失败处理
        $("#addForm,#editForm")
            .on("nc.ajaxSubmit.error", function(e, errorMessage) {
                Nc.alertError(errorMessage);
            });
        $("#addForm").validate(validateOptions);
        $("#editForm").validate(validateOptions);
    };
    return {
        init: _init,
        addFormatTemplate: _addFormatTemplate,
        editFormatTemplate: _editFormatTemplate,
        deleteFormatTemplate: _deleteFormatTemplate
    };
}();

/* 图片上传*/
var picUpload = function() {



    var editorPicShowPanel = "#addPicShowPanel";
    var uploadProgress = "#addUploadProgress";
    var picPanel = "#addPicPanel";

    var uploadSuccessPicNum = 0;
    var progress;

    var tmplLi = '<li class="ncsc-goodspic-upload">' +
        '<div class="upload-thumb">' +
        '<img src="{url}"></div>' +
        '<div class="upload-thumb-handle">' +
        '<a href="javascript:void(0)" class="btn btn-xs btn-info m-r-5" data-nc="insert" title="插入">插入</a>' +
        '<a href="javascript:void(0)" class="btn btn-xs btn-danger" data-nc="delete" title="删除">删除</a>' +
        '</div>' +
        '</li>';
    /**
     * 图片上传成功之后
     */
    var uploadDone = function(e, data) {
        $(picPanel).show();
        // $(uploadProgress).show().html("成功上传" + uploadSuccessPicNum + "图片")
        $(editorPicShowPanel).append(tmplLi.ncReplaceTpl({
            url: data.url
        }));

        uploadSuccessPicNum && layer.msg('<div class="nc-layer-msg">已上传' + uploadSuccessPicNum + '张图片</div>')

    };
    //绑定事件
    var bindEvents = function() {
        //上传插件
        $("#addPicUploadBtn").fileupload({
            dataType: 'json',
            url: ncGlobal.sellerRoot + "image/upload.json",
            formData: {
                type: ncGlobal.filesType.goods
            },
            done: function(e, data) {
                if (data.result.code == 200) {
                    uploadSuccessPicNum++;
                    uploadDone(e, data.result.data);
                }
            },
            change: function(e, data) {
                uploadSuccessPicNum = 0;
            }
        });
        //删除事件
        $(editorPicShowPanel).on("click", "a[data-nc='delete']", function() {
            $(this).closest('li').remove();
            $(editorPicShowPanel).find("li").length || ($(picPanel).hide())
        });
        //点击图片上传到编辑器
        $(editorPicShowPanel).on("click", "a[data-nc='insert']", function() {
            UE.getEditor('addEditor').execCommand('insertHtml', $(this).parents("li:first").find('.upload-thumb').html())
        });
    };

    /////
    return {
        init: function() {
            bindEvents();
        }
    };
}();

/**
 * 编辑关联板式的图片相关
 */
var picUploadForEdit = function() {
    var editorPicShowPanel = "#editorPicShowPanel";
    var uploadProgress = "#editorUploadProgress";
    var picPanel = "#editPicPanel";

    var uploadSuccessPicNum = 0;
    var progress;

    var tmplLi = '<li class="ncsc-goodspic-upload">' +
        '<div class="upload-thumb">' +
        '<img src="{url}"></div>' +
        '<div class="upload-thumb-handle">' +
        '<a href="javascript:void(0)" class="btn btn-xs btn-info m-r-5" data-nc="insert" title="插入">插入</a>' +
        '<a href="javascript:void(0)" class="btn btn-xs btn-danger" data-nc="delete" title="删除">删除</a>' +
        '</div>' +
        '</li>';
    /**
     * 图片上传成功之后
     */
    var uploadDone = function(e, data) {
        $(picPanel).show();
        // $(uploadProgress).show().html("成功上传" + uploadSuccessPicNum + "图片")
        $(editorPicShowPanel).append(tmplLi.ncReplaceTpl({
            url: data.url
        }));
        //uploadSuccessPicNum && Nc.alertSucceed("成功上传 "+ uploadSuccessPicNum + " 张图片")
        uploadSuccessPicNum &&layer.msg('<div class="nc-layer-msg">已上传' + uploadSuccessPicNum + '张图片</div>')


        // progress && (clearTimeout(progress))
        // progress = setTimeout(function() {
        //     $(uploadProgress).hide("fast");
        // }, 2000);
    };
    //绑定事件
    var bindEvents = function() {
        //上传插件
        $("#editorPicUploadBtn").fileupload({
            dataType: 'json',
            url: ncGlobal.sellerRoot + "image/upload.json",
            formData: {
                type: ncGlobal.filesType.goods
            },
            done: function(e, data) {
                if (data.result.code == 200) {
                    uploadSuccessPicNum++;
                    uploadDone(e, data.result.data);
                }
            },
            change: function(e, data) {
                uploadSuccessPicNum = 0;
            }
        });
        //删除事件
        $(editorPicShowPanel).on("click", "a[data-nc='delete']", function() {
            $(this).closest('li').remove();
            $(editorPicShowPanel).find("li").length || ($(picPanel).hide())
        });
        //点击图片上传到编辑器
        $(editorPicShowPanel).on("click", "a[data-nc='insert']", function() {
            UE.getEditor('editEditor').execCommand('insertHtml', $(this).parents("li:first").find('.upload-thumb').html())
        });
    };

    /////
    return {
        init: function() {
            bindEvents();
        }
    };
}();

var editEditor;
$(function() {

    UE.getEditor('addEditor', {
        textarea: "formatContent",
        zIndex:19891017
    });
    UE.getEditor('editEditor', {
        textarea: "formatContent",
        zIndex:19891017
    });
    formatTemplate.init();
    picUpload.init();
    picUploadForEdit.init();
});