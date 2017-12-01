/**
 * Created by shopnc.feng on 2015-12-24.
 */

/**
 * 编辑关联板式的图片相关
 */
var picUploadForEdit = function() {
    var editorPicShowPanel = "#editorPicShowPanel";
    var picPanel = "#editPicPanel";

    var uploadSuccessPicNum = 0;

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
        $(editorPicShowPanel).append(tmplLi.ncReplaceTpl({
            url: data.url
        }));
        layer.msg('<div class="nc-layer-msg">已上传' + uploadSuccessPicNum + '张图片</div>');
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
            UE.getEditor('editor').execCommand('insertHtml', $(this).parents("li:first").find('.upload-thumb').html())
        });
    };

    /////
    return {
        init: function() {
            bindEvents();
        }
    };
}();



var editSku = function() {
    var
        $skuForm = $("#skuForm"),
        $formSubmit = $("#formSubmit");

	var bindEvents = function() {
		//编辑器
		UE.getEditor('editor', {
			textarea: "goodsBody",
            zIndex:19891016
		});
        $formSubmit.click(function(){
            if (!$skuForm.valid()) {
                return;
            }
            $formSubmit.attr("disabled", "disabled");
            $.post(ncGlobal.sellerRoot + "goods/save/sku.json",
                $skuForm.serialize(),
                function(data){
                    if (data.code == 200) {
                        var successMessage = data.message ? data.message : '操作成功';
                        Nc.alertSucceed(successMessage, {
                            icon: 1,
                            time: 3000,
                            end: function() {
                                data.url && Nc.go(data.url)
                            }
                        })
                        $formSubmit.removeAttr("disabled");
                    } else {
                        Nc.alertError("请求失败");
                        $formSubmit.removeAttr("disabled");
                    }
            })
        });
	};
    var formVerify = function () {
        $("#skuForm").validate({
            errorPlacement: function(error, element) {
                error.appendTo(element.parent());
            },
            rules: {
                goodsPrice: {
                    required : true,
                    number : true,
                    min : 0.01,
                    max : 9999999
                },
                markerPrice: {
                    required : true,
                    number : true,
                    min : 0.01,
                    max : 9999999
                },
                goodsStorage: {
                    required : true,
                    digits : true,
                    min : 0,
                    max : 999999999
                },
                godosStorageAlarm: {
                    required : true,
                    digits : true,
                    min : 0,
                    max : 999999999
                }
            }
        });
    };
	return {
		init: function() {
			bindEvents();
		}
	};
}();

$(function() {
	editSku.init();
	picUploadForEdit.init();

});