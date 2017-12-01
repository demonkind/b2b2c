/**
 * Created by shopnc.feng on 2016-02-23.
 */
var message = function() {
    var _bindEvents = function() {
        $("a[nc-read]").click(function(){
            var that = $(this);
            var messageId = that.data("id");
            $.post(ncGlobal.memberRoot + "message/mark_read.json",
                {messageId : messageId}, function(){
                    that.parents("tr").removeClass("unread");
                });
        });
    }
    var _delete = function(messageId) {
        Nc.layerConfirm("是否删除该条消息", {
            postUrl: ncGlobal.memberRoot + "message/delete.json",
            postData: {
                messageId: messageId
            }
        });
    }
    return {
        init : function () {
            _bindEvents();
        },
        delete : _delete
    }
}();

$(function(){
    message.init();
});