/**
 * Created by shopnc on 2016/1/14.
 */
//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        $("#shipSubmit").on("click",function(){
            $("#shipForm").ajaxSubmit();
        });
    }
    //外部可调用
    return {
        bindEvent: _bindEvent
    }
}();
//操作处理结束

$(function () {
    //页面绑定事件
    OperateHandle.bindEvent();
});