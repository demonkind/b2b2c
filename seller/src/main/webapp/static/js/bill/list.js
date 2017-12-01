/**
 * Created by shopnc on 2016/1/25.
 */
//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        $("a[ncType='confirm']").on("click",function(){
            var billId = $(this).attr("dataBillId");
            Nc.layerConfirm("您确认账单数据计算无误吗？",{area:"400px"},function(e){
                layer.closeAll();
                $.ajax({
                    type: 'POST',
                    url: ncGlobal.sellerRoot + "bill/confirm",
                    data : {"billId" : billId},
                    dataType: 'json'
                }).success(function (result) {
                    if ('200' == result.code) {
                        window.location.reload();
                    } else {
                        Nc.alertError(result.message);
                    }
                }).error(function() {
                    Nc.alertError("请求失败");
                });
            })
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