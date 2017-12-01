$(function(){
	var key = getCookie('key');
	if(!key){
		window.location.href = WapSiteUrl+'/login/popuplogin/';
	}
	
	// 取消订单
    $(".cancel-order").click(cancelOrder);
    // 订单退款
    $(".all_refund_order").click(refunOrder);
    // 收货
    $(".sure-order").click(sureOrder);
    // 我要退货
    $(".return-order").click(returnOrder);
    // 我要退款
    $(".refund-order").click(refundOrder);
    // 评价
    $(".evaluation-order").click(evaluationOrder);
    // 追评
    $('.evaluation-again-order').click(evaluationAgainOrder);
    // 部分退款
    $('.goods-refund').click(refunOrder);
    // 部分退货
    $('.goods-return').click(goodsReturn);
    // 查看物流
    $('.viewdelivery-order').click(viewOrderDelivery);

    $.ajax({
        type: 'get',
        url: ApiUrl + "/index.php?act=member_order&op=get_current_deliver",
        data:{key:key,order_id:getQueryString("order_id")},
        dataType:'json',
        success:function(result) {
            //检测是否登录了
            checkLogin(result.login);

            var data = result && result.datas;
            if (data.deliver_info) {
                $("#delivery_content").html(data.deliver_info.context);
                $("#delivery_time").html(data.deliver_info.time);               	
            }
        }
    });
    
    
    //取消订单
    function cancelOrder(){
        var order_id = $(this).attr("order_id");
        $.sDialog({
            content: '确定取消订单？',
            okFn: function() { cancelOrderId(order_id); }
        });
    }
    function cancelOrderId(order_id) {
        $.ajax({
            type:"post",
            url: WapSiteUrl+"/orders/cancel",
            data:{ordersId:order_id,cancelReason:1},
            dataType:"json",
            success:function(result){
            	if(result.code == 200){
                    reset = true;
                    window.location.reload();
                } else {
                    $.sDialog({
                        skin:"red",
                        content:result.message,
                        okBtn:false,
                        cancelBtn:false
                    });
                }
            }
        });
    }
  //订单退款
    function refunOrder() {
    	var ordersId = $(this).attr("order_id");
    	var orderGoodsId = $("#order_goods_id").text();
    	location.href = WapSiteUrl+"/member/refund/addall?ordersId=" + ordersId;
    }

    //确认订单
    function sureOrder(){
        var order_id = $(this).attr("order_id");

        $.sDialog({
            content: '确定收到了货物吗？',
            okFn: function() { sureOrderId(order_id); }
        });
    }

    function sureOrderId(order_id) {
        $.ajax({
            type:"post",
            url:WapSiteUrl+"/orders/receive",
            data:{ordersId:order_id},
            dataType:"json",
            success:function(result){
            	if(result.code == 200){
                    setTimeout(function(){
                    	window.location.reload();
                    }, 3000);
                } else {
                    $.sDialog({
                        skin:"red",
                        content:result.message,
                        okBtn:false,
                        cancelBtn:false
                    });
                }
            }
        });
    }
 // 我要退货
    function returnOrder() {
        var ordersId = $(this).attr('order_id');
        var orderGoodsId = $(this).attr('order_good_id');
        location.href = WapSiteUrl + '/member/return/add?ordersId=' + ordersId + '&orderGoodsId=' +orderGoodsId;
    }
    // 我要退款
    function refundOrder() {
        var ordersId = $(this).attr('order_id');
        var orderGoodsId = $(this).attr('order_good_id');
        location.href = WapSiteUrl + '/member/refund/addgoods?ordersId=' + ordersId + '&orderGoodsId=' +orderGoodsId;
    }
    // 评价
    function evaluationOrder(){
    	var orderId = $(this).attr('order_id');
        location.href = WapSiteUrl + '/member/evaluate/evaluateAdd?orderId=' + orderId;
    }
 // 追加评价
    function evaluationAgainOrder() {
        var orderId = $(this).attr('order_id');
        location.href = WapSiteUrl + '/member/evaluate/evaluateAddAain?orderId=' + orderId;
    }
    // 全部退款
    function allRefundOrder() {
        var orderId = $(this).attr('order_id');
        location.href = WapSiteUrl + '/member/refund/addall?orderId=' + orderId;
    }
    // 退款
    function goodsRefund() {
        var orderId = $(this).attr('order_id');
        var orderGoodsId = $(this).attr('order_goods_id');
        location.href = WapSiteUrl + '/member/refund/addall?orderId=' + orderId;
    }
    // 退款
    function goodsRefund() {
        var orderId = $(this).attr('order_id');
        var orderGoodsId = $(this).attr('order_goods_id');
        location.href = WapSiteUrl + '/member/refund/addall?orderId=' + orderId +'&order_goods_id=' + orderGoodsId;
    }
    // 退货
    function goodsReturn() {
        var orderId = $(this).attr('order_id');
        var orderGoodsId = $(this).attr('order_goods_id');
        location.href = WapSiteUrl + '/member/refund/addgoods?orderId=' + orderId +'&order_goods_id=' + orderGoodsId;
    }
    
    
    //查看物流
    function viewOrderDelivery() {
        var orderId = $(this).attr('order_id');
        var shipSn = $(this).attr('shipSn');
        var shipName = $(this).attr('shipName');
        var shipCode = $(this).attr('shipCode');
        var shipUrl = $(this).attr('shipUrl');
        var shipNote = $(this).attr('shipNote');
        location.href = "http://m.kuaidi100.com/index_all.html?type=" + shipCode + "&postid=" + shipSn + "&callbackurl=" + WapSiteUrl + "/orders/list?ordersState=send";
    }
});