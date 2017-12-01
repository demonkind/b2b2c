
/**
 * 定义购物车参数设置
 * 目前购物车价格等都是前台计算出来的
 */
ncDefine("cartConfig", [], function() {
	return {
		event: {
			//刷新购物车事件文本
			cartRefresh: "cart.list.refresh"
		}
	};
});
/**
 * 购物车中的商品增减
 */
ncDefine("goodsNumBtn", ["cartConfig", "nc.eventManger"], function(cc, em) {
    "use strict";
    var settings = {
        minValue: 1,
        goodsCutFlat: "btn-goods-cut",
        goodsAddFlat: "btn-goods-add",
        goodsModFlat: "btn-goods-mod",
        //每个商品的购买数量减少按钮
        $btnCut: $("a[btn-goods-cut]"),
        //每个商品的购买数量添加按钮
        $btnAdd: $("a[btn-goods-add]"),
        //每个商品的购买数量input
        $btnMod: $("input[btn-goods-mod]"),
        //商品全面的选择checkbox
        $goodsCheckList: $("input[data-goods-checkbox]"),
        //全选按钮
        $selectAll: $("#selectAll"),
        //确认订单按钮
        $nextSubmit: $("#next_submit"),
        //表单
        $formBuy: $("#form_buy"),
        inputPrefix: "input_item_",
        //检验是否登录链接
        urlLoginState : ncGlobal.webRoot + 'login/status'
    };
    var URL = {
        add: ncGlobal.webRoot + "cart/edit",
        del: ncGlobal.webRoot + "cart/del",
    };
    // 异步提交标示
    var __postFlat = true;

    /**
     * 获取数量显示区域
     */
    function _getNumInput(goodsId) {
        return $("#" + settings.inputPrefix + goodsId);
    }

    /**
     * 修改购物车数量
     * @param cartId
     * @param buyNum
     * @private
     */
    function _cartEditNum(cartId, buyNum) {
        //console.log("edit cart goods num set ,post data is :", cartId, buyNum);
        if(!__postFlat) {
            return;
        }
        __postFlat = false;
        $.post(URL.add, {
            cartId: cartId, buyNum: buyNum
        }, function(data) {
            __postFlat = true;
            //console.log("edit num cart goods is Ok ,req data is ", data);
            if (data.code == "200") {
                _getNumInput(cartId).val(buyNum);
                //是否锁定减号
                _showStorageError(cartId, buyNum);
                //_unlockCartGoods(cartId);
                em.trigger(cc.event.cartRefresh);
            } else {

                if (data.data.goodsStorage > 0) {
                    _getNumInput(cartId).val(data.data.goodsStorage);
                    _showStorageError(cartId, buyNum, data.data.goodsStorage);
                    //_lockCartGoods(cartId);


                    em.trigger(cc.event.cartRefresh);
                } else {
                    //_lockCartGoods(cartId);
                    _showStorageError(cartId, buyNum, data.data.goodsStorage);
                    //Nc.alertError("该商品库存不足");
                }
            }
            _lockSub(cartId,_getNumInput(cartId).val());
        }, "json");
    }

    /**
     * 显示库存超出错误提示
     * @private
     */
    function _showStorageError(cartId , buyNum , goodsStorage){
        var $errorPanel = $("#storageErrorPanel" + cartId),
            //$goodsStorageError = $("#goodsStorageError" + cartId),
            tpl = '<em class="error-msg"></em>';
            ;
        if (goodsStorage == undefined){
            $errorPanel.hide()
            return
        }
        if (goodsStorage == 0){
            //$errorPanel.hide()
            $errorPanel.html($(tpl).html("无库存"))
            return
        }
        if (buyNum > goodsStorage) {
            //console.log("显示错误提示");
            $errorPanel.html($(tpl).html("当前库存" + goodsStorage + "件"));
            $errorPanel.show();
        } else {
            //console.log("隐藏错误提示");
            $errorPanel.hide();
        }
    }

    /**
     * 是否锁定商品选择框
     * @param cartId
     * @param buyNum
     * @param goodsStorage
     * @private
     */
    function _lockCartGoods(cartId, buyNum, goodsStorage) {
        var $cartItem = $("#cart_item_" + cartId),
            $inputItem = $("#cart_id" + cartId);
        $inputItem.length && $inputItem.attr("disabled","disabled");

    }

    /**
     *是否锁定减号
     * @private
     */
    function _lockSub(cartId,num){
        var a = $("a[btn-goods-cut="+cartId+"]");
        a.length && (num <= 1 ? a.addClass("sub-disable") : a.removeClass("sub-disable"))
    }
    /**
     * 解锁商品选择框
     * @param cartId
     * @private
     */
    function _unlockCartGoods(cartId){
        var $cartItem = $("#cart_item_" + cartId),
            $inputItem = $("#cart_id" + cartId)
            ;
        $inputItem.length && $inputItem.removeAttr("disabled");
    }
    /**
     * 根据cartid 删除购物车中的一个商品
     * @param cartId
     * @private
     */
    function _delCart(cartId) {
        if(!__postFlat) {
            return
        }
        __postFlat = false;
        $.post(URL.del,
            {
                cartId: cartId
            },
            function(xhr) {
                if(xhr.code == 200) {
                    //删除
                    var $item = $("#cart_item_" + cartId),
                        $itemTr = $("#cart_item_tr_" + cartId),
                        $store = $item.closest("tbody[data-cart-store]"),
                        _cartLength = $('tr[data-cart-id]').not($item).length
                        ;
                    //判断是否最后一个了
                    if(_cartLength == 0) {
                        Nc.go();
                        return;
                    }
                    !$store.find("tr[data-cart-id]").not($item).length
                        ? $store.remove()
                        : $item.length && ($item.remove(),$itemTr.remove() )

                    _cartLength = $('tr[data-cart-id]').length;

                    //刷新小红点事件
                    em.trigger("nc.cart.redpoint", [_cartLength]);
                    //刷新购物车价格
                    em.trigger(cc.event.cartRefresh);
                    //layer.closeAll();
                    __postFlat = true;
                } else {
                    Nc.alertError(xhr.message, { offset: "15%" });
                    __postFlat = true;
                }
            }
            , "json"
        ).always(function() {
                //layer.closeAll();
                __postFlat = true;
            })
    }

    //绑定事件
    var _bindEvents = function() {
        $(document).ajaxError(function() {
            //Nc.alertError("连接超时", { offset: "15%" });
            __postFlat = true;
        });
        $("input[data-store-all-check]").change(function() {
            //console.log("店铺所有商品选择事件");
            var $this = $(this),
                storeId = $this.data("storeAllCheck"),
                a = $("tbody[data-cart-store=" + storeId + "]")
                ;
            if( $this.is(":checked") && a.length){
                a.find("input[data-goods-checkbox]").not(":disabled").attr("checked", "checked");
                $("input[data-store-all-check]:not(:checked)").length || settings.$selectAll.attr("checked","checked");
            }else{
                a.find("input[data-goods-checkbox]").removeAttr("checked");
                settings.$selectAll.removeAttr("checked");
            }
            a.length && em.trigger(cc.event.cartRefresh);
        })
        /**
         * 减少商品数量
         */
        settings.$btnCut.on("click", function() {
            //console.log("点击商品减少事件");
            var $this = $(this),
                goodsId = $this.attr(settings.goodsCutFlat),
                $input = _getNumInput(goodsId);
            if(Nc.isEmpty($input) || ($input.val() <= settings.minValue)) {
                return;
            }
            _cartEditNum(goodsId, Nc.number.sub($input.val(), 1));
        });
        /**
         * 添加商品数量
         */
        settings.$btnAdd.on("click", function() {
            //console.log("点击商品添加事件");
            var $this = $(this),
                goodsId = $this.attr(settings.goodsAddFlat),
                $input = _getNumInput(goodsId);
            if(Nc.isEmpty($input)) {
                return;
            }
            _cartEditNum(goodsId, Nc.number.add($input.val(), 1));
        });
        /**
         * 直接修改数量
         */
        settings.$btnMod.on("focusout", function() {
            var $this = $(this),
                goodsId = $this.attr(settings.goodsModFlat),
                _buyNum = Nc.getNum($this.val())
                ;
            $this.val( _buyNum != ''&& _buyNum != 0 ? _buyNum : 1);
            _cartEditNum(goodsId, $this.val());
        });
        /**
         * 商品前面的勾选按钮事件
         */
        settings.$goodsCheckList.change(function() {
            //已经选择的商品列表
            var $this = $(this),
                selectGoodsLen = settings.$goodsCheckList.filter(":checked").length,
                _storeid = $this.data("storeId"),
                _selStoreGoodsLen = settings.$goodsCheckList.filter("[data-store-id=" + _storeid+"]:not(:checked)").length,
                goodsLen = settings.$goodsCheckList.length
                ;
            //发送购物车刷新事件
            em.trigger(cc.event.cartRefresh);
            //显示提交按钮

            //如果不是全都选中的全选按钮不勾选
            selectGoodsLen < goodsLen
                ? settings.$selectAll.removeAttr("checked")
                : settings.$selectAll.attr("checked", "checked")

            _selStoreGoodsLen > 0
                ? $("input[data-store-all-check="+_storeid+"]").removeAttr("checked")
                :$("input[data-store-all-check="+_storeid+"]").attr("checked", "checked")

        });

        /**
         *  全选事件
         */
        settings.$selectAll.on("change", function() {
            //$(this).is(":checked")
            //    ? settings.$goodsCheckList.attr("checked", "checked")
            //    : settings.$goodsCheckList.removeAttr("checked")
            if($(this).is(":checked")){
                settings.$goodsCheckList.not(":disabled").attr("checked", "checked")
                $("input[data-store-all-check]").attr("checked", "checked")
            }else{
                settings.$goodsCheckList.removeAttr("checked")
                $("input[data-store-all-check]").removeAttr("checked")
            }
            //发送购物车刷新事件
            em.trigger(cc.event.cartRefresh);
        });
        /**
         * 下一步按钮
         */
        settings.$nextSubmit.click(function() {
            if(settings.$goodsCheckList.filter(":checked").length) {
                $.getJSON(ncGlobal.webRoot + 'login/status',function(xhr){

                    if (xhr.code == 200 && xhr.data.status == true){
                        settings.$formBuy.submit();
                    }else{
                        //调用member_top中的显示对话框函数
                        popupLoging.showLoginDialog();
                    }
                })

            }
        })
        /**
         * 购物车删除
         */
        $("a[data-del-cart-id]").click(function() {
            var $this = $(this),
                cartId = $this.data("delCartId"),
                _goodsId = $this.data("goodsId");

            Nc.layerConfirm("您可以选择移到关注，或删除商品。",
                {
                    title: "删除商品？",
                    btn: ["删除", "移到我的关注"],
                    btn1: function() {
                        _delCart(cartId);
                    },
                    btn2: function() {
                        if(!_goodsId) {
                            Nc.alertError("参数错误");
                            return
                        }
                        if (__postFlat == false ){
                            return;
                        }
                        __postFlat == false;
                        //验证是否登录
                        $.getJSON(settings.urlLoginState, function (xhr) {
                            if (xhr.code == 200){
                                __postFlat = true;
                                if ( xhr.data.status){
                                    favorites.goods(_goodsId, cartId);
                                }else{
                                    popupLoging.showLoginDialog();
                                }
                            }
                            __postFlat == true;
                        });
                    }
                }
            )
        })
        /**
         * 移入关注
         */
        $("a[data-goods-favorites]").click(function () {
            var $this = $(this),
                _goodsId = $this.data("goodsFavorites"),
                _cartId = $this.data("cartId"),
                _layerConfirm = function () {
                    Nc.layerConfirm("移动后选中商品将不在购物车中显示。",
                        {
                            title: "移到关注？"
                        },
                        function () {
                            favorites.goods(_goodsId, _cartId);
                        })
                }
                ;
            if (!_goodsId || !_cartId) {
                Nc.alertError("参数错误");
                return
            }
            console.log("移入关注 button is click");
            if (__postFlat == false ){
                return;
            }
            __postFlat == false;
            console.log("移入关注 button is click");
            $.getJSON(settings.urlLoginState, function (xhr) {
                if (xhr.code == 200){
                    __postFlat == true;
                    if ( xhr.data.status){
                        _layerConfirm()
                    }else{
                        popupLoging.showLoginDialog();
                    }
                }
            });

        });
        /**
         * 监听移入关注成功事件，删除购物车中的商品
         */
        Nc.eventManger.on("goods.favorites.end", function(event, goodsId, cartId) {
            _delCart(cartId);
        })

    };
    ///
    _bindEvents();

});
/**
 * 刷新购物车价格
 * 这里主要监听
 */
ncDefine("cartRefresh",["cartConfig", "nc.eventManger"],function(cartConfig,eventManger) {

    var CartRefresh = function(){
        //购物车刷新事件文本
        this.cartRefreshEvent = cartConfig.event.cartRefresh;
        //购物车中每个店铺的商品区域
        this.$storeCartList = $("tbody[data-cart-store]");
        //订单价格显示区域
        this.$cartTotal = $("#cartTotal");

        //console.log("设置完成后,",this);
        //购物车内商品总价格
        this.orderAmount = 0 ;
        this.init();
    };
    CartRefresh.prototype = {
        /**
         * 根据cartid 获取商品小计区域
         */
        _getGoodsSubtotalElement:function(cartId) {
            return $("#item_subtotal_"+ cartId);
        },
        init :function() {
            this.bindEvent();
            //eventManger.trigger("nc.cart.redpoint", [$("input[data-goods-checkbox]").length]);
        },
        bindEvent:function() {
            var that = this ;
            //console.log("监听购物车重新刷新价格事件");
            eventManger.on(this.cartRefreshEvent ,function(){
                that._refreshCart();
            });
            //顶部小红点监听事件
            eventManger.on("nc.cart.redpoint", function(event, num) {

                //console.log(num)
                var $shortCutCartCount = $("#shortCutCartCount");
                if(num > 0) {
                    $shortCutCartCount.show().html(num);
                } else {
                    $shortCutCartCount.hide();
                }
            })
        },
        /**
         * 刷新购物车全部价格
         * @private
         */
        _refreshCart:function() {
            //console.log("购物车价格全部刷新");
            var that = this;

            that.orderAmount = 0;
            //running ...........
             $.each(this.$storeCartList,function(i,n) {
                 $.each($(n).find('tr[data-cart-id]'),function(ii,nn) {

                     var $this = $(nn),
                         cartId = $this.data("cartId"),
                         goodsPrice = $this.data("goodsPrice"),
                         _buyNum = that._getBuyNumByCartId(cartId),
                         _amount =  Nc.number.multi(goodsPrice,_buyNum),
                         $goodsSubtotal =  that._getGoodsSubtotalElement(cartId)
                         ;
                     if($this.find("input[data-goods-checkbox]:checked").length){
                        that.orderAmount =  Nc.number.add(that.orderAmount,_amount);
                     }
                     //修改商品小计
                     $goodsSubtotal.length && $goodsSubtotal.html(Nc.priceFormat(_amount))
                 })
             })
            //console.log(that.orderAmount)
            that.$cartTotal.html(Nc.priceFormat(that.orderAmount))
            //刷新是否可以下单按钮
            if($("input[data-goods-checkbox]:checked").length) {
                //可以下单，添加样式
                $("#next_submit").addClass("ok");
            } else {
                //不可以下单，移除样式
                $("#next_submit").removeClass("ok");
            }
        },
        /**
         * 根据cartid 获取商品数量
         * @private
         */
        _getBuyNumByCartId:function(cartId){
            var a = $("#input_item_" + cartId);
            return a.length ? $.trim (a.val()) :0;
        }
    };
    //
    return new CartRefresh;
});


$(function() {
    ncRequire("goodsNumBtn");
    ncRequire("cartRefresh");
    ncRequire("cartGoPay");
    //初始化购物车小红点
    Nc.eventManger.trigger("nc.cart.redpoint", [$("input[data-goods-checkbox]").length]);

    //最近浏览
    $('#goodsbrowse_div').ncGoodsBrowse();
});