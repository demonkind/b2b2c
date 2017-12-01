/**
 * Created by cj on 2016/2/3.
 */

var refundList = function() {

    return {
        init:function() {
          //bind Events
            //绑定日历控件
            $('#addTimeStart').bind("focus", function () {
                WdatePicker();
            });
            $('#addTimeEnd').bind("focus", function () {
                WdatePicker();
            });
        }
    }
}();

$(function() {
    refundList.init();
})