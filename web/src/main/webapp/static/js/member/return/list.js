/**
 * Created by cj on 2016/2/3.
 */

var refundList = function () {

    return {
        init: function () {
            //bind Events
            //绑定日历控件
            $('#addTimeStart').bind("focus", function () {
                WdatePicker();
            });
            $('#addTimeEnd').bind("focus", function () {
                WdatePicker();
            });
            $("a[data-delay-btn]").click(function () {
                var $this = $(this),
                    refundId = $this.data("delayBtn");
                console.log("延迟对话框");
                Nc.layerConfirm("商家选择没收到已经发货的商品，请联系物流进行确认，提交后将重新计时，商家可以再次确认收货。",
                    {
                        title: "延迟时间",
                        postUrl:ncGlobal.webRoot + "member/return/savedelay",
                        postData:{
                            refundId:refundId
                        }

                    }
                );

            })
        }
    }
}();

$(function () {
    refundList.init();
})