/**
 * Created by cj on 2016/2/2.
 */

var refundAddGoods = function () {
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

    function _verfiy() {
        var _r = $("#refundAmount").data("default"),
            _n = $("#goodsNum").data("default");
        $("#form").validate({
            errorPlacement: function (error, element) {
                error.appendTo(element.nextAll('span.error'));
            },
            rules: {
                reasonId: {
                    required: true
                },
                refundAmount: {
                    required: true,
                    number: true,
                    min: 0.01,
                    max: function () {
                        return $("#refundAmount").data("default");
                    }()
                },
                goodsNum: {
                    required: true,
                    digits: true,
                    min: 1,
                    max: function () {
                        return $("#goodsNum").data("default");
                    }()
                },
                buyerMessage: {
                    required: true
                }
            },
            messages: {
                buyerMessage: {
                    required: '<i class="icon-exclamation-sign"></i>请填写退货退款说明'
                },
                reasonId: {
                    required: '<i class="icon-exclamation-sign"></i>请选择退货退款原因'
                },
                refundAmount: {
                    required: '<i class="icon-exclamation-sign"></i>可退金额'+_r,
                    number: '<i class="icon-exclamation-sign"></i>可退金额' +_r,
                    min: '<i class="icon-exclamation-sign"></i>最小金额0.01',
                    max: '<i class="icon-exclamation-sign"></i>可退金额' +_r
                },
                goodsNum: {
                    required: '<i class="icon-exclamation-sign"></i>可退数量' + _n,
                    digits: '<i class="icon-exclamation-sign"></i>可退数量' + _n,
                    min: '<i class="icon-exclamation-sign"></i>最小数量 1',
                    max: '<i class="icon-exclamation-sign"></i>最大数量 ' + _n
                }
            }
        });
    }

    /**
     * 验证
     * @private
     */

    return {
        init: function () {

            $(document).ajaxError(function () {
                //Nc.alertError("连接超时");
                __postFlat = true;
            });
            _verfiy();
            //上传
            $("#uploadBtn").fileupload({
                dataType: 'json',
                url: ncGlobal.webRoot + "image/upload.json",
                done: function (e, data) {
                    console.log("done")
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
            $("#imageList").on("click", "a.del", function () {
                $(this).closest("li").remove();
            });
            //提交按钮
            $("#formSubmit").click(function () {
                if (!$("#form").valid()) {
                    return;
                }
                if (__postFlat == false) {
                    return;
                }
                __postFlat = false;

                $.post(
                    ncGlobal.webRoot + 'member/return/save',
                    function () {
                        var b = $.map($("#imageList li"), function (n) {
                                return $(n).data("image");
                            }).join(","),
                            a = $.trim($("#buyerMessage").val()),
                            refundAmount = $.trim($("#refundAmount").val()),
                            reasonId = $.trim($("#reasonId").val()),
                            goodsNum = $.trim($("#goodsNum").val())
                            ;
                        return {
                            refundAmount: refundAmount,
                            buyerMessage: a,
                            picJson: b,
                            goodsNum:goodsNum,
                            ordersId: ncGlobal.ordersId,
                            ordersGoodsId: ncGlobal.ordersGoodsId,
                            reasonId: reasonId
                        }
                    }(),
                    function (xhr) {
                        if (xhr.code == 200) {
                            Nc.alertSucceed(xhr.message, {
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

        }
    }
}();
$(function () {
    refundAddGoods.init();
});