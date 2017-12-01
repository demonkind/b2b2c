/**
 * Created by cj on 2016/2/2.
 */

var refundAddAll = function () {
    var __postFlat = true;

    /**
     * @private
     */
    function _buildImageItem(data) {
        var tpl;

        tpl = $(
            '<li>' +
            '<div class="upload-thumb">' +
            '<img src="' + data.url + '">' +
            '<a href="javascript:;" class="del" title="移除">X</a>' +
            '</div>' +
            '</li>').data("image", data.name);
        $("#imageList").append(tpl);
    }
    function _verify(){
        $("#form").validate({
            errorPlacement: function (error, element) {
                error.appendTo(element.nextAll('span.error'));
            },
            rules: {
                buyerMessage: {
                    required: true
                }
            },
            messages: {
                buyerMessage: {
                    required: '<i class="icon-exclamation-sign"></i>请填写退货退款说明'
                }
            }
        })
    }
    return {
        init: function () {

            $(document).ajaxError(function () {
                //Nc.alertError("连接超时");
                __postFlat = true;
            });
            //上传
            $("#uploadBtn").fileupload({
                dataType: 'json',
                url: ncGlobal.webRoot + "image/upload.json",
                done: function (e, data) {
                    //console.log("done")
                    var xhr = data.result;
                    if (xhr.code == 200) {
                        _buildImageItem(xhr.data);
                    } else {
                        Nc.alertError(xhr.message);
                    }
                },
                add: function (e, data) {
                    if ($("#imageList").find("li").length >= 3) {
                        Nc.alertError("最多可以上传3张图片");
                        return;
                    }
                    data.submit();
                }
            });
            //删除图片
            $("#imageList").on("click", "a.del", function (e) {
                $(this).closest("li").remove();
            })
            //提交按钮
            $("#formSubmit").click(function () {
                if (!$("#form").valid() || __postFlat == false) {
                    return;
                }
                __postFlat = false;
                $.post(
                    ncGlobal.webRoot + 'member/refund/saveall',
                    function () {
                        var b = $.map($("#imageList li"), function (n) {
                            return $(n).data("image");
                        }).join(",");
                        return {
                            buyerMessage:  $.trim($("#buyerMessage").val()),
                            picJson: b,
                            ordersId: ncGlobal.orderId
                        }
                    }(),
                    function (xhr) {
                        if (xhr.code == 200) {
                            Nc.alertSucceed(xhr.message,{
                                end: function () {
                                    Nc.go(xhr.url);
                                }
                            })
                        } else {
                            Nc.alertError(xhr.message);
                        }

                    }
                ).always(function () {
                        __postFlat = true;

                    })

            });

            //验证
            _verify();
        }
    }
}();
$(function () {
    refundAddAll.init();
});