/**
 * Created by zxy on 2016-02-04
 */
//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        $.ajax({
            url: ncGlobal.adminRoot + 'index/stat/goodscategory',
            success: function(xhr) {
                var data = xhr;
                data.element = 'container_goodscategory';
                data.parseTime = false;
                Morris.Line(data);
                return false;
            }
        });
        $.ajax({
            url: ncGlobal.adminRoot + 'index/stat/hourtrend',
            success: function(xhr) {
                var data = xhr;
                data.element = 'container_hourtrend';
                data.parseTime = false;
                Morris.Line(data);
                return false;
            }
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