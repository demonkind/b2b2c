//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {

        //删除浏览历史
        $("[data-delbrowse]").click(function () {
            var browseId = parseInt($(this).data("delbrowse"));
            if (browseId <= 0) {
                Nc.alertError("参数错误");
                return false;
            }
            Nc.layerConfirm("是否删除该记录？", {
                yes: function () {
                    var params = {"browseId": browseId};
                    $.ajax({
                        type: "post",
                        url: ncGlobal.memberRoot + "goodsbrowse/del",
                        data: params,
                        async: false,
                        success: function (xhr) {
                            if (xhr.code == 200) {
                                window.location.href = ncGlobal.memberRoot + "goodsbrowse";
                            } else {
                                Nc.alertError(xhr.message);
                            }
                        }
                    });
                }
            });
            return false;
        });

        //清空浏览历史
        $("[data-delbrowse-all]").click(function () {
            Nc.layerConfirm("是否清空记录？", {
                yes: function () {
                    $.ajax({
                        type: "post",
                        url: ncGlobal.memberRoot + "goodsbrowse/del/all",
                        async: false,
                        success: function (xhr) {
                            if (xhr.code == 200) {
                                window.location.href = ncGlobal.memberRoot + "goodsbrowse";
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
        $("[data-addcart]").click(function () {
            var goodsId = parseInt($(this).data("addcart"));
            if (goodsId <= 0) {
                Nc.alertError("参数错误");
                return false;
            }
            myCart.addCartByGoodsId(goodsId, 1);
            _flyToCart($("#browserow_"+goodsId).find("[nc_type='browserowImg']"));
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