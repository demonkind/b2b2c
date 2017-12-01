//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //绑定日历控件
        $('#addTimeStart').bind("focus", function () {
            WdatePicker();
        });
        $('#addTimeEnd').bind("focus", function () {
            WdatePicker();
        });

        //操作阶段
        var operationStageHtml = '<option value="">---请选择---</option>'+
            '<option value="login">'+$lang.pointslog.operationStageLogin+'</option>'+
            '<option value="register">'+$lang.pointslog.operationStageRegister+'</option>'+
            '<option value="comments">'+$lang.pointslog.operationStageComments+'</option>'+
            '<option value="orders">'+$lang.pointslog.operationStageOrders+'</option>'+
            '<option value="admin">'+$lang.pointslog.operationStageAdmin+'</option>';
        $("#operationStage").html(operationStageHtml);
        switch ($("#operationStageValue").val()){
            case "login":
                $("#operationStage").val("login");
                break;
            case "comments":
                $("#operationStage").val("comments");
                break;
            case "orders":
                $("#operationStage").val("orders");
                break;
            case "register":
                $("#operationStage").val("register");
                break;
            case "admin":
                $("#operationStage").val("admin");
                break;
        }


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