/**
 * Created by shopnc on 2016/1/8.
 */
//操作处理开始
var OperateHandle = function () {
    //列出物流公司
    var shipList = function(callback) {
        $.ajax({
            type: 'GET',
            url: ncGlobal.sellerRoot + "ship/company/list/json",
            dataType: 'json'
        }).success(function (result) {
            if ('200' == result.code) {
                var $shipCode = $('select[name="shipCode"]');
                $shipCode.empty();
                $.each(result.data, function (n, value) {
                    $shipCode.append("<option value='" + value.shipCode + "'>" + value.shipName + "</option>");
                })
                $shipCode.append('<option value="" selected>不使用物流公司</option>');
                if (typeof callback == "function") {
                    callback();
                }
            }
        }).error(function() {
            Nc.alertError("请求失败");
        });
    }
    function _bindEvent() {
        //绑定日历控件
        $('#createTimeStart').bind("focus", function () {
            WdatePicker();
        });
        $('#createTimeEnd').bind("focus", function () {
            WdatePicker();
        });
        $("#ordersState").val(ordersGlobal.ordersState);
        $("#searchType").val(ordersGlobal.searchType);

        $("a[ncType='cancel']").on("click",function(){
            var ordersSn = $(this).attr("dataOrdersSn");
            var ordersId = $(this).attr("dataOrdersId");
            var memberName = $(this).attr("memberName");
            Nc.layerOpen({
                title: "取消订单",
                content: $('#cancelOrdersDialog'),
                $form: $("#cancelOrdersForm"),
                async: true
            });
            $("#cancelOrdersDialog").find('input[name="ordersId"]').val(ordersId);
            $("#cancelOrdersDialog").find('span[name="ordersSn"]').html(ordersSn);
        });
        $("a[ncType='modifyFreight']").on("click",function(){
            var ordersSn = $(this).attr("dataOrdersSn");
            var ordersId = $(this).attr("dataOrdersId");
            var memberName = $(this).attr("dataMemberName");
            var freightAmount = $(this).attr("datafreightAmount");
            Nc.layerOpen({
                title: "修改运费",
                content: $('#modifyFreightDialog'),
                $form: $("#modifyFreightForm"),
                async: true
            });
            $("#modifyFreightDialog").find('input[name="ordersId"]').val(ordersId);
            $("#modifyFreightDialog").find('span[name="ordersSn"]').html(ordersSn);
            $("#modifyFreightDialog").find('dd[name="memberName"]').html(memberName);
            $("#modifyFreightDialog").find('input[name="freightAmount"]').val(freightAmount);
        });

        $("a[ncType='send']").on("click",function(){
            var ordersId = $(this).attr("dataOrdersId");
            var sendModify = $(this).attr("dataSendModify");
            ordersGlobal.isSelectLast = true;
            $("#sendDialog").find("div[ncType='modifyReceiver']").hide();
            $.ajax({
                type: 'GET',
                url: ncGlobal.sellerRoot + "orders/send/info/json/" + ordersId,
                dataType: 'json'
            }).success(function (result) {
                if ('200' == result.code) {
                    $("#sendDialog").find('input[name="ordersId"]').val(ordersId);
                    $("#sendDialog").find('span[name="ordersSn"]').html(result.data.ordersSn);
                    $("#sendDialog").find('input[name="shipSn"]').val(result.data.shipSn);
                    $("#sendDialog").find('textarea[name="shipNote"]').val(result.data.shipNote);
                    $("#editAddressPanel").attr("data-area-text", result.data.receiverAreaInfo);
                    $("#sendDialog").find('input[name="areaId1"]').val(result.data.receiverAreaId1);
                    $("#sendDialog").find('input[name="areaId2"]').val(result.data.receiverAreaId2);
                    $("#sendDialog").find('input[name="areaId3"]').val(result.data.receiverAreaId3);
                    $("#sendDialog").find('input[name="areaId4"]').val(result.data.receiverAreaId4);
                    $("#sendDialog").find('input[name="receiverAddress"]').val(result.data.receiverAddress);
                    $("#sendDialog").find('input[name="receiverName"]').val(result.data.receiverName);
                    $("#sendDialog").find('input[name="receiverPhone"]').val(result.data.receiverPhone);
                    $("#sendDialog").find('span[name="receiver"]').html(result.data.receiverName + "，" + result.data.receiverPhone + "，" + result.data.receiverAreaInfo + result.data.receiverAddress);
                    Nc.layerOpen({
                        title: sendModify == "1" ? "查看/编辑发货信息" : "发货",
                        content: $('#sendDialog'),
                        $form: $("#sendForm"),
                        async: true
                    });
                    $("#editAddressPanel").data("nc.area").restart();
                    if (sendModify == "1") {
                        shipList(function(){
                            $('select[name="shipCode"]').val(result.data.shipCode);
                        });
                    } else {
                        shipList();
                    }
                }
            }).error(function() {
                Nc.alertError("请求失败");
            });
        });
        $("#editAddressPanel").NcArea({
            hiddenInput: [{
                name: "areaId",
                value: "0"
            }, {
                name: "areaInfo",
                value: ""
            }],
            url: ncGlobal.sellerRoot + 'area/list.json/',
            dataHiddenName: "areaId",
            showDeep: 4
        }).on("nc.select.selected", function(e) {
            ordersGlobal.isSelectLast = false;
        }).on("nc.select.last", function(e) {
            ordersGlobal.isSelectLast = true;
        });
        $("#modifyReceiver").on("click",function(){
            $("#sendDialog").find("div[ncType='modifyReceiver']").show();
        })
        $("#submitModifyReceiver").on("click",function(){
            ordersGlobal.validShip = false;
            if(!$("#sendForm").valid()){
                ordersGlobal.validShip = true;
                return false;
            }
            ordersGlobal.validShip = true;
            if ($("#sendDialog").find("input[name='areaInfo']").val() == "") {
                $("#sendDialog").find("input[name='areaInfo']").val($("#editAddressPanel").attr("data-area-text"));
            }
            $("#sendDialog").find('span[name="receiver"]').html($("#sendDialog").find("input[name='receiverName']").val() + "，" + $("#sendDialog").find("input[name='receiverPhone']").val() + "，" + $("#sendDialog").find("input[name='areaInfo']").val() + $("#sendDialog").find("input[name='receiverAddress']").val());
            $("#sendDialog").find("div[ncType='modifyReceiver']").hide();
        })
        //表单验证
        jQuery.validator.addMethod("isSelectLast", function(value, element) {
            return ordersGlobal.isSelectLast;
        }, "请将地区选择完整");
        $("#sendForm").validate({
            errorPlacement: function(error, element) {
                if (element.attr("name") == "areaId") {
                    $("#editAddressPanel").append(error);
                } else {
                    error.insertAfter(element);
                }
            },
            rules: {
                shipSn: {
                    required: function(){
                        return $("#sendForm").find("select[name='shipCode']").val() != '' && $("#sendForm").find("input[name='shipSn']").val() == '' && ordersGlobal.validShip;
                    }
                },
                receiverName: {
                    required: true
                },
                receiverAddress: {
                    required: true
                },
                areaId: {
                    isSelectLast: true
                },
                receiverPhone: {
                    required: true
                }
            },
            messages: {
                shipSn: {
                    required: "请填写快递单号"
                },
                receiverName: {
                    required: "请填写收货人姓名"
                },
                receiverAddress: {
                    required: "请填写详细街道地址"
                },
                receiverPhone: {
                    required: "请填写联系电话"
                }
            }
        })

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
                url: ncGlobal.sellerRoot + "orders/ship/search/" + shipSn + "/" + shipCode,
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