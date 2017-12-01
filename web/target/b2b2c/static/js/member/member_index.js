/**
 * 会员购物车
 * @type {{init}}
 */
var memberCartHandle = function() {
    //缓存几个
    var setting = {
        //购物车数据url
        urlCart: ncGlobal.webRoot + "cart/json",
        //会员中心购物车显示区域
        $shopping: $("#shopping"),
        //最大显示数量
        itemMax : 3
    };
    /**
     * 创建单挑
     */
    var _getItem = function(data) {
        return '<li>'
            + '<div class="ncm-goods-thumb">' +
            '<a target="_blank" href="' + ncGlobal.webRoot + 'goods/' + data.goodsId + '">' +
            '<img src="' + ncImage(data.imageSrc, 60, 60) + '"></a>' +
            '</div>'
            + '<dl class="ncm-goods-info">'
            + '<dt>' +
            '<a href="' + ncGlobal.webRoot + 'goods/' + data.goodsId + '">' + data.goodsName + '</a>' +
            '</dt>'
            + '<dd><span class="ncm-order-price">商城价：<em>￥' + Nc.priceFormat(data.goodsPrice) + '</em></span></dd>'
            + '</dl>'
            + '</li>'
    };
    /*
     *事件绑定
     */
    function eventBind() {
        //监听购物车创建成功事件
        Nc.eventManger.on("nc.cart.build.success", function(event, data) {
            buildCart(data);
        })
        //监听购物车为空事件
        Nc.eventManger.on("nc.cart.build.empty",function(){
            buildEmptyCart();
        })
    }

    /**
     * 初始化购物车
     */
    function buildCart(data) {
        var a = '';
        $.each(data.goodsList ,function(i,n) {
            if(i >= (setting.itemMax)){
                return ;
            }
            a += _getItem(n);
        })
        var cartList = setting.$shopping.find("[data-cart-list]");
        cartList.find("ul").html(a);
        cartList.show();
        setting.$shopping.find ("[data-empty-cart]").hide();
    };
    /**
     * 购物车中没有商品的时候
     */
    function buildEmptyCart() {
        var cartList = setting.$shopping.find("[data-cart-list]");
        cartList.find("ul").html();
        cartList.hide();
        setting.$shopping.find ("[data-empty-cart]").show();
    }

    return {
        init: function() {
            eventBind();
        }
    }

}();

/**
 * 收藏
 */
var memberFavorites = function() {

    return {
        /**
         * 商品收藏
         */
        goods:function(){
            var a = $("#favoritesGoods").find("li").length;
            (a > 0 && a > 4)&&  $('#favoritesGoodsList').bxSlider({
                infiniteLoop: false,
                hideControlOnEnd: true,
                hideHoverControls:true,
                minSlides: 0,
                maxSlides: 4,
                slideWidth: 163,
                slideMargin: 0
            });
        },
        /**
         * 店铺收藏
         */
        store:function() {
            var a = $("#favoritesStore").find("li").length;
            (a > 0 && a > 3)&&  $('#favoritesStoreList').bxSlider({
                infiniteLoop: false,
                hideControlOnEnd: true,
                hideHoverControls:true,
                minSlides: 0,
                maxSlides: 3,
                slideWidth: 110,
                slideMargin: 0
            });
        }
    }
}();

//操作处理开始
var OperateHandle = function(memberFavoritesGoods) {
    function _bindEvent() {
        //load模块
        var INFO_TYPE = ['transaction','favoritesGoods', 'favoritesStore'];

        function _ajax_load(type) {
            var url = ncGlobal.memberRoot + "index/";
            switch(type) {
                case "favoritesGoods":
                    url += "favorites/goods";
                    break;
                case "favoritesStore":
                    url += "favorites/store";
                    break;
                case "transaction":
                    url += "orders/list";
                    break;
            }
            $.ajax({
                url: url,
                success: function(html) {
                    INFO_TYPE.shift();
                    if(INFO_TYPE[0]) {
                        _ajax_load(INFO_TYPE[0]);
                    }
                    $('#' + type).html(html);
                    //bycj 加载商品收藏
                    type == "favoritesGoods"
                        ? memberFavorites.goods()
                        :memberFavorites.store()

                }
            });
        }

        _ajax_load(INFO_TYPE[0]);


    }

    //外部可调用
    return {
        bindEvent: _bindEvent
    }
}(memberFavorites);
//操作处理结束

$(function() {
    //页面绑定事件
    OperateHandle.bindEvent()

    memberCartHandle.init();
});