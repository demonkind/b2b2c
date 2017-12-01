var page = pagesize;
var curpage = 1;
var hasMore = true;
var footer = false;
var reset = true;
var orderKey = '';
	
$(function(){
	var key = getCookie('key');
	if(!key){
		window.location.href = WapSiteUrl;
	}

	if (getQueryString('data-state') != '') {
	    $('#filtrate_ul').find('li').has('a[data-state="' + getQueryString('data-state')  + '"]').addClass('selected').siblings().removeClass("selected");
	}

	//查找订单
    $('#search_btn').click(function(){
        reset = true;
        curpage = 1;
        var formSearch = $("#searchForm");
        formSearch.submit();
    });

    $('#fixed_nav').waypoint(function() {
        $('#fixed_nav').toggleClass('fixed');
    }, {
        offset: '50'
    });

	function initPage(){
	    if (reset) {
	        curpage = 1;
	        hasMore = true;
	    }
        $('.loading').remove();
        if (!hasMore) {
            return false;
        }
        hasMore = false;
        var page = $("#page").val();
	    var ordersState = $('#ordersState').val();
	    var keyword = $('#order_key').val();
	    var url = WapSiteUrl+'/orders/list?';
	    if(page){
	    	url = url + "page=" + page;
	    }else{
	    	url = url + "page=" + 1;
	    }
	    if(ordersState){
	    	url = url + "&ordersState=" + ordersState;
	    }
	    if(keyword){
	    	url = url + "&keyword=" + keyword;
	    }
	    location.href = url;
	
//		$.ajax({
//			type:'get',
//			url:WapSiteUrl+'/orders/list',
//			data:{page:page,ordersState:ordersState,keyword:orderKey},
//			dataType:'json',
//			success:function(result){
//				curpage++;
//                hasMore = result.hasmore;
//                if (!hasMore) {
//                    get_footer();
//                }
//                if (result.datas.order_group_list.length <= 0) {
//                    $('#footer').addClass('posa');
//                } else {
//                    $('#footer').removeClass('posa');
//                }
//				var data = result;
//				data.WapSiteUrl = WapSiteUrl;//页面地址
//				data.ApiUrl = ApiUrl;
//				data.key = getCookie('key');
//				template.helper('$getLocalTime', function (nS) {
//                    var d = new Date(parseInt(nS) * 1000);
//                    var s = '';
//                    s += d.getFullYear() + '年';
//                    s += (d.getMonth() + 1) + '月';
//                    s += d.getDate() + '日 ';
//                    s += d.getHours() + ':';
//                    s += d.getMinutes();
//                    return s;
//				});
//                template.helper('p2f', function(s) {
//                    return (parseFloat(s) || 0).toFixed(2);
//                });
//                template.helper('parseInt', function(s) {
//                    return parseInt(s);
//                });
//				var html = template.render('order-list-tmpl', data);
//				if (reset) {
//				    reset = false;
//				    $("#order-list").html(html);
//				} else {
//                    $("#order-list").append(html);
//                }
//			}
//		});

	}
});	

    // 取消订单
    $('#order-list').on('click','.cancel-order', cancelOrder);
    // 订单退款
    $('#order-list').on('click','.all_refund_order',refunOrder);
    // 收货
    $('#order-list').on('click','.sure-order',sureOrder);
    // 我要退货
    $('#order-list').on('click','.return-order',returnOrder);
    // 我要退款
    $('#order-list').on('click','.refund-order',refundOrder);
    // 评价
    $('#order-list').on('click','.evaluation-order', evaluationOrder);
    // 追评
    $('#order-list').on('click','.evaluation-again-order', evaluationAgainOrder);
    //查看物流
    $('#order-list').on('click','.viewdelivery-order',viewOrderDelivery);
    //订单支付
    $('#order-list').on('click','.check-payment',function() {
        var pay_sn = $(this).attr('data-paySn');
        var amout= $(this).attr("data-amout");
        var payId=$(this).attr("payId");
        toPay(pay_sn,'member_buy','pay',payId,amout);
        return false;
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
            url:WapSiteUrl+"/orders/cancel",
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

    //收货
    function sureOrder(){
        var order_id = $(this).attr("order_id");

        $.sDialog({
            content: '确定收到了货物吗？',
            okFn: function() { sureOrderId(order_id); }
        });
    }

    function sureOrderId(order_id,activitiesGoods) {
        $.ajax({
            type:"post",
            url:WapSiteUrl+"/orders/receive",
            data:{ordersId:order_id},
            dataType:"json",
            success:function(result){
                if(result.code == 200){
                    reset = true;
                    $.sDialog({
                        skin:"red",
                        content:result.message,
                        okBtn:false,
                        cancelBtn:false
                    });
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
    //订单评价
    function evaluationOrder(){
    	var orderId = $(this).attr('order_id');
        location.href = WapSiteUrl + '/member/evaluate/evaluateAdd?orderId=' + orderId;
    }
    // 追加评价
    function evaluationAgainOrder() {
        var orderId = $(this).attr('order_id');
        location.href = WapSiteUrl + '/member/evaluate/evaluateAddAain?orderId=' + orderId;
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
    
    $('#filtrate_ul').find('a').click(function(){
        $('#filtrate_ul').find('li').removeClass('selected');
        $(this).parent().addClass('selected').siblings().removeClass("selected");
        reset = true;
        window.scrollTo(0,0);
        initPage();
    });
                    
    //初始化页面
    $(window).scroll(function(){
        if(($(window).scrollTop() + $(window).height() > $(document).height()-1)){
            initPage();
        }
    });

function get_footer() {
    if (!footer) {
        footer = true;
        $.ajax({
            url: WapSiteUrl+'/js/tmpl/footer.js',
            dataType: "script"
          });
    }
}