/**
 * Created by shopnc on 2016/1/25.
 */
//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        $("a[ncType='clone']").on("click",function(){
            var freightId = $(this).attr("dataFreightId");
            $.ajax({
                type: 'POST',
                url: ncGlobal.sellerRoot + "freight/template/clone",
                data : {"freightId" : freightId},
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
        });
        $("a[ncType='delete']").on("click",function(){
            var freightId = $(this).attr("dataFreightId");
            Nc.layerConfirm("删除将影响所有使用该运费模板的商品的运费计算，确定继续删除吗？",{area:"400px"},function(e){
                layer.closeAll();
                $.ajax({
                    type: 'POST',
                    url: ncGlobal.sellerRoot + "freight/template/del",
                    data : {"freightId" : freightId},
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