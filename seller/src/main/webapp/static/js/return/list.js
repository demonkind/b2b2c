var __postFlat = true;
var sellerRefundList = function () {
    function _bindEvents() {

        $(document).ajaxError(function () {
            //Nc.alertError("连接超时");
            __postFlat = true;
        });
        //绑定日历控件
        $('#addTimeStart').bind("focus", function () {
            WdatePicker();
        });
        $('#addTimeEnd').bind("focus", function () {
            WdatePicker();
        });
    }

    return {
        init: function () {
            _bindEvents();
        }
    }
}();
var sellerReturnReceive = function () {

    function _showReceiveModal() {
        console.log("显示收货modal");
        var $this = $(this),
            _refundId = $this.data("receiveId"),
            u = ncGlobal.sellerRoot + "return/receive/" + _refundId;
        ;
        Nc.layerOpen({
            type: 2,
            title: '编辑',
            content: u,
            skin: "default",
            area: ['600px', '315px'],

            success: function (layero, index) {
                var $sendForm = layer.getChildFrame("#sendForm", index),
                    $sendFormError =layer.getChildFrame("#warning", index);
                $sendForm.validate({
                    rules: {
                        goodsState: {
                            required: true
                        }
                    },
                    messages: {
                        goodsState: {
                            required: '<i class="icon-exclamation-sign"></i>请选择收货情况'
                        }
                    }
                });
            },
            yes: function (index, layero) {
                var $sendForm = layer.getChildFrame("#sendForm", index),
                    a = $sendForm.serializeObject()
                u = ncGlobal.sellerRoot + "return/receivesave"
                ;
                if (!$sendForm.valid()){
                    return ;
                }
                if (__postFlat == false) {
                    return;
                }
                __postFlat = false;
                $.post(
                    u, a, function (xhr) {
                        if (xhr.code == 200) {
                            layer.close("all");
                            Nc.alertSucceed(xhr.message,{end: function () {
                                Nc.go(xhr.url);
                            }});
                        } else {
                            Nc.alertError(xhr.message);
                        }
                        __postFlat = true;
                    }, "json"
                )
            }
        })

    }

    return {
        init: function () {
            //
            $("a[data-receive-id]").click(_showReceiveModal);

        }
    }
}();
$(function () {
    sellerRefundList.init();
    sellerReturnReceive.init();
})