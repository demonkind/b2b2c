/**
 * Created by shopnc on 2015/12/31.
 */
//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //绑定日历控件
        $('#createTimeStart').bind("focus", function () {
            WdatePicker();
        });
        $('#createTimeEnd').bind("focus", function () {
            WdatePicker();
        });
        $("#ordersState").val(orderGlobal.ordersState);

        $("a[ncType='cancel']").on("click",function(){
            var ordersSn = $(this).attr("dataOrdersSn");
            var ordersId = $(this).attr("dataOrdersId");
            Nc.layerOpen({
                title: "取消订单",
                content: $('#cancelOrdersDialog'),
                $form: $("#cancelOrdersForm"),
                async: true
            });
            $("#cancelOrdersDialogOrdersSn").html(ordersSn);
            $("#cancelOrdersForm").find("input[name='ordersId']").val(ordersId);
        });

        $("a[ncType='delayReceive']").on("click",function(){
            var ordersSn = $(this).attr("dataOrdersSn");
            var ordersId = $(this).attr("dataOrdersId");
            var autoReceiveTime = $(this).attr("dataAutoReceiveTime");

            Nc.layerOpen({
                title: "延迟收货",
                content: $('#delayReceiveOrdersDialog'),
                $form: $("#delayReceiveOrdersForm"),
                async: true
            });
            $("#delayReceiveOrdersDialogOrdersSn").html(ordersSn);
            $("#delayReceiveOrdersDialogAutoReceiveTime").html(autoReceiveTime);

            $("#delayReceiveOrdersForm").find("input[name='ordersId']").val(ordersId);
        });

        $("a[ncType='receive']").on("click",function(){
            var ordersSn = $(this).attr("dataOrdersSn");
            var ordersId = $(this).attr("dataOrdersId");
            Nc.layerOpen({
                title: "收货",
                content: $('#receiveOrdersDialog'),
                $form: $("#receiveOrdersForm"),
                async: true
            });
            $("#receiveOrdersDialogOrdersSn").html(ordersSn);
            $("#receiveOrdersForm").find("input[name='ordersId']").val(ordersId);
        });

        $("a[ncType='shipSearch']").on("click",function(){
            var ordersSn = $(this).attr("dataOrdersSn");
            var shipSn = $(this).attr("dataShipSn");
            var shipCode = $(this).attr("dataShipCode");
            var shipName = $(this).attr("dataShipName");
            var shipUrl = $(this).attr("dataShipUrl");
            Nc.layerOpen({
                btn: ['关闭'],
                title: "查看物流",
                content: $('#shipOrdersDialog')
            });
            $('#shipOrdersDialog').find('div[ncType="ordersSn"]').html(ordersSn);
            $('#shipOrdersDialog').find('a[ncType="shipName"]').html(shipName).attr('href',shipUrl);
            $('#shipOrdersDialog').find('div[ncType="shipSn"]').html(shipSn);

            $.ajax({
                type: 'GET',
                url: ncGlobal.memberRoot + "orders/ship/search/" + shipSn + "/" + shipCode,
                dataType: 'json'
            }).success(function (result) {
                if ('200' == result.code) {
                    var shipContent = $.parseJSON(result.data);
                    if ('200' == shipContent.status) {
                        $('#shipResult').empty();
                        $.each(shipContent.data, function (n, value) {
                            $('#shipResult').append("<li>"+value.time + ' ' + value.context +"</li>");
                        })
                    } else {
                        Nc.alertError("查询数据失败");
                    }
                } else {
                    $("#shipResult").html("<li>查询失败</li>");
                }
            }).error(function() {
                $("#shipResult").html("<li>请求失败</li>");
            });
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