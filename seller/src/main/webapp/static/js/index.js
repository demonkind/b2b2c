/**
 * Created by zxy on 2016-02-23
 */
//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //30天热销商品排行Top10
        $.ajax({
            url: ncGlobal.sellerRoot+"index/goodshotsale",
            success: function(html) {
                $('#goodsHotSale').html(html);
                return false;
            }
        });
        //昨日今日销售走势
        $.ajax({
            url: ncGlobal.sellerRoot + 'index/hourtrend',
            success: function(xhr) {
                var data = xhr;
                data.element = 'hourTrend';
                data.parseTime = false;
                Morris.Line(data);
                return false;
            }
        });
        //查询商品相关统计数字
        var statTagGoods1 = {
            "ncGoodsCount":"goodsCommonCount",
            "ncGoodsCommendCount":"goodsCommendCount",
            "ncImageCount":"imageCount"};
        var statTagGoods2 = {
            "ncGoodsOnSale":"goodsCommonOnSaleCount",
            "ncGoodsWaitverify":"goodsCommonWaitCount",
            "ncGoodsVerifyfail":"goodsCommonVerifyFailCount",
            "ncGoodsOffline":"goodsCommonOfflineAndPassCount",
            "ncGoodsBan":"goodsCommonBanCount",
            "ncConsultNoReply":"consultNoReplyCount"};
        $.ajax({
            url: ncGlobal.sellerRoot + 'index/stat/goods',
            success: function(xhr) {
                $.each(statTagGoods1, function(name,value) {
                    if (xhr[value] && xhr[value]>0) {
                        $("#"+name).html(xhr[value]);
                    }else{
                        $("#"+name).html("0");
                    }
                });
                $.each(statTagGoods2, function(name,value) {
                    if (xhr[value] && xhr[value]>0) {
                        $("#"+name).html(xhr[value]);
                        $("#"+name).parents('a').addClass('num');
                    }
                });
                return false;
            }
        });
        $.ajax({
            url: ncGlobal.sellerRoot + 'index/stat/evaluate',
            success: function(xhr) {
                $("#ncEvaluateDes").addClass(xhr.desEvalArrow);
                $("#ncEvaluateDes").find("span").append(xhr.desEvalTitle);
                $("#ncEvaluateDes").append(xhr.desEvalRate);

                $("#ncEvaluateService").addClass(xhr.desEvalArrow);
                $("#ncEvaluateService").find("span").append(xhr.desEvalTitle);
                $("#ncEvaluateService").append(xhr.desEvalRate);

                $("#ncEvaluateDelivery").addClass(xhr.desEvalArrow);
                $("#ncEvaluateDelivery").find("span").append(xhr.desEvalTitle);
                $("#ncEvaluateDelivery").append(xhr.desEvalRate);
                return false;
            }
        });

        var statTagOrders = {
            "ncOrdersProgressing":"ordersProgressingCount",
            "ncOrdersRefundAndReturn":"refundAndReturnCount",
            "ncOrdersWaitPay":"ordersWaitPayCount",
            "ncOrdersWaitSend":"ordersWaitSendCount",
            "ncRefundWaiting":"refundWaitingCount",
            "ncReturnWaiting":"returnWaitingCount",
            "ncBillNew":"billNewCount"};
        $.ajax({
            url: ncGlobal.sellerRoot + 'index/stat/orders',
            success: function(xhr) {
                $.each(statTagOrders, function(name,value) {
                    if (xhr[value] && xhr[value]>0) {
                        $("#"+name).html(xhr[value]);
                        $("#"+name).parents('a').addClass('num');
                    }
                });
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