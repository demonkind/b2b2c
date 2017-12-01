var sellerRefundList = function () {
    function _bindEvents (){
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
$(function () {
    sellerRefundList.init();
})