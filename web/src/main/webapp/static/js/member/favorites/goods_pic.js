//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //瀑布流
        $('#favoritesGoodsList').infinitescroll({
            navSelector : '#page-more',
            nextSelector : '#page-more a',
            itemSelector : '.favorite-goods-list',
            loading: {
                selector:'#page-nav',
                img: ncGlobal.imgRoot+'transparent.gif',
                msgText:'努力加载中...',
                maxPage : 2,
                finishedMsg : '没有记录了'
            }
        });

        //删除关注
        $("#favoritesGoodsList").on("click", "[nc_type='favoritesDel']", function () {
            var favoritesId = parseInt($(this).attr("favorites_id"));
            if (favoritesId <= 0) {
                Nc.alertError("参数错误");
                return false;
            }
            Nc.layerConfirm("是否删除该记录？", {
                yes: function () {
                    var params = {"favid": favoritesId};
                    $.ajax({
                        type: "post",
                        url: ncGlobal.memberRoot + "favorites/goods/del",
                        data: params,
                        async: false,
                        success: function (xhr) {
                            if (xhr.code == 200) {
                                window.location.href = ncGlobal.memberRoot + "favorites/goods";
                            } else {
                                Nc.alertError(xhr.message);
                            }
                        }
                    });
                }
            });
            return false;
        });

        //加入购物车
        $("#favoritesGoodsList").on("click", "[nc_type='favoritesAddCart']", function () {
            var goodsId = parseInt($(this).attr("goods_id"));
            if (goodsId <= 0) {
                Nc.alertError("参数错误");
                return false;
            }
            myCart.addCartByGoodsId(goodsId, 1);

            _flyToCart($("#favoriteGoodsItem"+goodsId).find("[nc_type='favoriteGoodsItemImg']"));
            return false;
        });
        //分享商品
        $("#favoritesGoodsList").on("click", "[nc_type='favoritesSharegoods']", function () {
            var goodsId = parseInt($(this).attr("goods_id"));
            if (goodsId <= 0) {
                Nc.alertError("参数错误");
                return false;
            }
            $(this).ncShareGoods({"goodsId":goodsId});
            return false;
        });

    }
    /*
     *飞入购物车动画
     */
    function _flyToCart($img){
        var $element = $("#topMyCart");
        var rtoolbar_offset_left = $element.offset().left;
        var rtoolbar_offset_top = $element.offset().top - $(document).scrollTop();
        var img = $img.attr('src');
        var flyer = $('<img id="fly"class="u-flyer" src="' + img + '" style="z-index:198910151;width:' + $img.width() + 'px;height:' + $img.height() + 'px;">');
        flyer.fly({
            start: {
                left: $img.offset().left,
                top: $img.offset().top - $(document).scrollTop()
            },
            end: {
                left: rtoolbar_offset_left,
                top: rtoolbar_offset_top,
                width: 0,
                height: 0
            },
            onEnd: function() {
                flyer.remove();
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