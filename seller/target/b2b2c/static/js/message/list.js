/**
 * Created by shopnc.feng on 2016-02-05.
 */
var message = function() {
    var _bindEvent = function() {
        $("a[nc-read]").click(function(){
            var that = $(this);
            var messageId = that.data("id");
            $.post(ncGlobal.sellerRoot + "message/mark_read.json",
                {messageId : messageId}, function(){
                    that.parents("tr").removeClass("unread");
                });
        });
    }
    var _delete = function(messageId) {
        Nc.layerConfirm("是否删除该条消息", {
            postUrl: ncGlobal.sellerRoot + "message/delete.json",
            postData: {
                messageId: messageId
            }
        });
    }
    return {
        init : function () {
            _bindEvent();
        },
        delete : _delete
    }
}();

$(function(){
    message.init();
});