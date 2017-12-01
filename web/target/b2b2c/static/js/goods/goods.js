/**
 * 点击规格跳转
 * 规格json数据格式
 * [{goodsId:1,specValueIds:"1,8"},...]
 */
ncDefine("nc.goGoodsPage", ["nc.eventManger"], function (eventManger) {

    var GoGoodsPage = function (option) {
        //保存格式化后的规格值于商品对应hash
        this.specJson = {};
        //合并配置
        $.extend(this, GoGoodsPage.settings, option);

        this._init();
    };
    GoGoodsPage.prototype = {
        //获取选择的规格值，如果$el 如果是空就返回全部标红的规格值
        getSelSpecValue: function () {
            var that = this,
                sel = [],
                a = this.$specPanel.find("." + this.cssSel);
            $.each(a, function (i, n) {
                var attrData = $(n).data(that.attrSpecValue);
                attrData && (sel.push(attrData));
            });
            return this.specJson[sel.join(",")];
        },

        // init
        _init: function () {
            //对参数进行整理
            this._formatSpecJson();
            if ($.isEmptyObject(this.specJson)) {
                return;
            }
            //绑定事件
            this._bindEvent();
        },
        //绑定事件
        _bindEvent: function () {
            var that = this;
            that.$specPanel
                .find("[data-" + that.attrSpecValue + "]")
                .on("click", function () {
                    var $this = $(this),
                        goodsId = '',
                        $allSpecValue = $this.closest(that.attrSpecValueParent).find("[data-" + that.attrSpecValue + "]");
                    if (!$allSpecValue.length) {
                        return;
                    }

                    ;
                    if ($this.hasClass("hovered")){
                        return
                    }
                    //清空样式
                    $allSpecValue.removeClass(that.cssSel);
                    //为被点击的元素添加样式
                    $this.addClass(that.cssSel);
                    //获取规格属性值
                    goodsId = that.getSelSpecValue();
                    goodsId && (Nc.go(that.goodsInfoUrl + goodsId))
                });
            //规格图片
            $('.sp-img-thumb img').jqthumb({
                width: 30,
                height: 30,
                after: function (imgObj) {
                    imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                }
            });
        },
        // format spec json data
        _formatSpecJson: function () {
            var that = this;
            $.each(that.goodsSpecValues, function (i, n) {
                that.specJson[n.specValueIds] = n.goodsId;
            });
        }
    };
    //默认配置
    GoGoodsPage.settings = {
        // 商品详情url前缀
        goodsInfoUrl: ncGlobal.webRoot + "goods/",
        //规格值json
        goodsSpecValues: ncGlobal.goodsSpecValues,
        //规格值显示面板
        $specPanel: $("#nc-spec-panel"),
        //规格值选择后的样式
        cssSel: "hovered",
        //规格值上的规格值标识，
        //同时带有这个值的元素会被绑定点击事件
        //比如:data-spec-value="1",就设置成 spec-value
        attrSpecValue: "spec-value",
        //
        attrSpecValueParent: "ul"
    };

    return function (option) {
        return new GoGoodsPage(option);
    };
});
/**
 * 商品图片，（放大镜）
 * 商品图片的放大镜是通过设置html标签上的属性来实现的
 * 下面的小图则是通过js来实现的
 */
ncDefine("nc.goods.pic", [], function () {
    var GoodsPic = function () {
        //配置
        this.options = {
            //tu
            $littlePicPanel: $("#ncsGoodsPicture"),
            $ncsGoodsPicList: $("#ncsGoodsPicList"),
            $GoodsPicPrevBtn: $("#GoodsPicPrevBtn"),
            $GoodsPicNextBtn: $("#GoodsPicNextBtn"),
            selectLittleClass: "current",
            //商品小图每屏数量
            picMax: 5,
            picWidth: 65
        };

        //获取小图个数
        this.picLength = this.options.$ncsGoodsPicList.find("li").length;
        //小图的滚动距离
        this.gup = 0;
        // console.log("pic page gup is ", this.gup);
        //当前小图滚动的页数
        //this.curr = 1;

        this._init();
    };
    GoodsPic.prototype = {
        _init: function () {
            this._buildElememt();
            this._bindEvent();
        },

        _buildElememt: function () {
            var that = this;
            if (this.picLength > this.options.picMax) {
                this.options.$littlePicPanel.find(".controller").addClass("roll");
            }
            var _li = this.options.$ncsGoodsPicList.find("li");
            //计算宽度
            this.options.$ncsGoodsPicList.css({
                width: (_li.length * that.options.picWidth + 0) + "px"
            })

            //小图的滚动距离
            //this.gup = this.options.$ncsGoodsPicList.width();

            this.gup = function () {
                var _n = _li.slice(-5).first();
                return _n.position().left
            };

        },
        _bindEvent: function () {

            var that = this;
            //点击商品小图的事件
            //this.options.$ncsGoodsPicList.find("a").on("click", function () {
            //    that.options.$ncsGoodsPicList.find("a").removeClass(that.options.selectLittleClass);
            //    $(this).addClass(that.options.selectLittleClass);
            //});
            this.options.$ncsGoodsPicList.find("a").on("mouseenter", function () {
                var _h = $(this).attr("href"),
                    _rev = $(this).attr("rev");
                $("#Zoomer").attr("href", _h).find('img').attr("src", _rev)
                MagicZoomPlus.refresh();
                that.options.$ncsGoodsPicList.find("a").removeClass(that.options.selectLittleClass);
                $(this).addClass(that.options.selectLittleClass);
            })
            //上一页点击事件
            this.options.$GoodsPicPrevBtn.on("click", function (event) {
                event.preventDefault();
                !that.options.$ncsGoodsPicList.is(":animated") && (that._scrollPicPage(1))
            })
            //下一页点击事件
            this.options.$GoodsPicNextBtn.on("click", function (event) {
                event.preventDefault();
                !that.options.$ncsGoodsPicList.is(":animated") && (that._scrollPicPage(2))
            })

        },
        /**
         * 滚动小图
         */
        _scrollPicPage: function (page) {
            var that = this;
            //console.log(that.gup())
            that.options.$ncsGoodsPicList.animate({
                left: page == 1 ? 0 : -that.gup()
            })

        }
    };


    return function (option) {
        return new GoodsPic(option);
    };
});
/**
 * 橱窗推荐
 */
ncDefine("nc.showcase", [], function () {
    var ShowCase = function () {
        //合并配置
        this.options = $.extend({}, ShowCase.option);
        //ajax 防止重复提交
        this.__ajaxflat = true;
        //商品commonid
        this.commonId = this.options.$showCasePanel.data("commonId");
        //店铺id
        this.storeId = this.options.$showCasePanel.data("storeId");
        //loading
        this.loading = {};
        this.init();
    };
    ShowCase.option = {
        //获取数据地址
        urlRefresh: ncGlobal.webRoot + "goods/commend.json",
        //橱窗区域
        $showCasePanel: $("#showCasePanel"),
        //橱窗显示商品显示列表
        $showCaseGoodsList: $("#showCaseGoodsList"),
        //重新刷新按钮
        $showCaseRefreshBtn: $("#showCaseRefreshBtn"),
        //单个橱窗推荐的商品
        tplLi: '<li title="{goodsName}">' +
        '<a href="{goodsUrl}">' +
        '<div class="ncs-recom-goods-thumb">' +
        '<img src="{imageSrc}" alt="{goodsName}"/>' +
        '</div>' +
        '<dl>' +
        '<dt class="goods-name">{goodsName}</dt>' +
        '<dd class="goods-price">' +
        '￥ <em>{goodsPrice}</em>' +
        '</dd>' +
        '</dl>' +
        '</a>' +
        '</li>',
        //暂无推荐时候显示的模板
        tplNone: '<div class="commend-none">店内暂无商品推荐</div>'
    };
    ShowCase.prototype = {
        init: function () {

            this.buildElememt();
            this.bindEvent();
        },

        buildElememt: function () {
            //初始化橱窗推荐
            this._getShowCaseJson();
        },
        bindEvent: function () {
            var that = this;
            //点击刷新
            this.options.$showCaseRefreshBtn.click(function (event) {
                //console.log("点击刷新事件触发");
                event.preventDefault();
                that._getShowCaseJson();
            });
            //浓缩图,
            this.options.$showCaseGoodsList.find('img').jqthumb({
                width: 100,
                height: 100,
                after: function (imgObj) {
                    imgObj.css('opacity', 0).animate({opacity: 1}, 500);
                }
            });
        },
        /**
         * 获取橱窗数据
         * @private
         */
        _getShowCaseJson: function () {
            var that = this;
            if (!this.__ajaxflat) {
                return;
            }
            this.__ajaxflat = false;
            this.loading = Nc.loading("#showCasePanel");
            //console.log("获取橱窗数据");
            $.post(
                that.options.urlRefresh,
                {
                    storeId: that.storeId,
                    commonId: that.commonId
                },
                function (xhr) {
                    if (xhr.code == '200') {
                        //console.log('刷新dom')
                        that._refreshDom(xhr.data);
                    }
                    that.__ajaxflat = true;
                }).always(function () {
                    that.__ajaxflat = true;
                    layer.close(that.loading);
                })
        },
        /**
         * 更新页面
         * @private
         */
        _refreshDom: function (data) {
            var that = this;
            var a = data.length
                ? $.map(data, function (n) {
                n.goodsUrl = ncGlobal.webRoot + "goods/" + n.goodsId;
                n.goodsPrice = Nc.priceFormat(n.goodsPrice);
                n.imageSrc = ncImage(n.imageSrc,100,100);
                return that.options.tplLi.ncReplaceTpl(n);
            }).join("")
                : this.options.tplNone;
            this.options.$showCaseGoodsList.html(a);
            //浓缩图
            this.options.$showCaseGoodsList.find('img').jqthumb({
                width: 100,
                height: 100,
                after: function (imgObj) {
                    imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                }
            });
        }
    }
    return function () {
        return new ShowCase();
    }
})
/**
 * 添加购物车
 */
ncDefine("nc.goods.addcart", [], function () {
    var Addcart = function () {
        this.options = {
            //购物车数量 的input
            $buyNumInput: $("#buyNumInput"),
            //购物车增加的按钮
            $buyNumAddBtn: $("#buyNumAddBtn"),
            //购物车数量减少按钮
            $buyNumCutBtn: $("#buyNumCutBtn"),
            //加入购物车按钮
            $addCartBtn: $("#addCartBtn"),
            //立即购物按钮
            $buynowSubmitBtn: $("#buynowSubmitBtn"),
            //添加购物车地址
            urlAddCart: ncGlobal.webRoot + "cart/add",
            //立即购买中验证是否登录
            urlIsLogin: ncGlobal.webRoot + 'login/status',
            //商品id
            goodsId: ncGlobal.goodsId,
            //商品库存
            goodsStorage: ncGlobal.goodsStorage,

            //立即购买用的保存数据的input
            cartId: $("#cartId")
        };
        //post 重复提交
        this.__postFlat = true;
        this.init();
    };
    Addcart.prototype = {
        init: function () {
            this.bindEvents();
        },
        /**
         * 绑定事件
         */
        bindEvents: function () {
            var that = this;

            //数量添加按钮
            this.options.$buyNumAddBtn.click(function () {
                //数量增加
                //that._modBuyNum(that.optons.goodsId, Nc.number.add(that.optons.$buyNumInput.val(), 1));
                var a = that.options.$buyNumInput,
                    c = parseFloat(a.val(), 10),
                    b = (c >= that.options.goodsStorage ) ? that.options.goodsStorage : ++c;
                a.val(b <= 0 ? 1 : b);

            });
            //数量减少
            this.options.$buyNumCutBtn.click(function () {
                var a = that.options.$buyNumInput,
                    c = parseFloat(a.val(), 10),
                    b = c == 1 ? 1 : --c;
                a.val(Math.abs(b));
            });
            /**
             *直接修改数量
             */
            this.options.$buyNumInput.on("keyup", function () {
                var $this = $(this),
                    a = parseFloat($this.val(), 10)
                    ;
                if (!Nc.isDigits($this.val())) {
                    Nc.getNum($this.val()) > that.options.goodsStorage
                        ? $this.val(that.options.goodsStorage)
                        : ($this.val(1));
                } else {
                    a > that.options.goodsStorage
                        ? $this.val(that.options.goodsStorage)
                        :(a<= 0 && $this.val(1));
                }
            });
            /**
             * 直接购买
             */
            this.options.$buynowSubmitBtn.click(function () {
                //验证是否登录
                if (!that.__postFlat || $(this).hasClass("no-addcart")) {
                    return
                }
                that.__postFlat == false;
                $.getJSON(that.options.urlIsLogin, function (xhr) {
                    if (xhr.code == "200") {
                        if (xhr.data.status == true) {
                            that.options.cartId.val(that.options.goodsId + '|' + that.options.$buyNumInput.val());
                            $("#buynow_form").submit();
                        } else {
                            //调用member_top中的显示对话框函数
                            popupLoging.showLoginDialog()
                        }
                    } else {
                        Nc.alertError(data.message ? data.message : "连接超时");
                    }
                    that.__postFlat == true
                }).fail(function () {
                    that.__postFlat == true
                    //Nc.alertError("连接超时");
                })

            })

            /**
             * 添加购物车
             */
            this.options.$addCartBtn.click(function () {
               !$(this).hasClass("no-addcart") && that._modBuyNum(that.options.goodsId, that.options.$buyNumInput.val());
            })

            /**
             * 图钉条上的添加购物车
             */
            $("#tabAddCart").click(function () {
                !$(this).hasClass("no-addcart") && that._modBuyNum(that.options.goodsId, that.options.$buyNumInput.val());
            })
            /**
             * 购物车添加商品成功事件
             */
            Nc.eventManger.on("cart.refresh", function (event, data) {
                //修改成功提示上面的文字
                $("#bold_num").html(data.buyNum);
                layer.open({
                    type: 1,
                    area: ['420px', '240px'], //宽高
                    shadeClose: true,
                    content: $("#addCartPrompt"),
                    time: 5000
                });
            });
        },
        /**
         * 修改购买数量
         * @param goodsId
         * @param buyNum
         * @private
         */
        _modBuyNum: function (goodsId, buyNum) {
            var that = this;
            if (!this.__postFlat) {
                return
            }
            this.__postFlat = false;
            $.getJSON(ncGlobal.webRoot + "cart/add", {"goodsId": goodsId, "buyNum": buyNum}).success(function (result) {
                    var data;
                    if (result.code == 200) {
                        data = result.data;
                        $('#bold_num').html(data.buyNum);
                        $('#bold_mly').html('￥' + Nc.priceFormat(data.cartAmount));

                        that.options.$buyNumInput.val(buyNum);
                        //发送刷新购物车消息
                        Nc.eventManger.trigger("nc.cart.redpoint", [result.data.buyNum]);
                        Nc.eventManger.trigger("cart.refresh",[result.data.buyNum]);
                    } else {
                        Nc.alertError(result.message);
                        (that.options.$buyNumInput.val() > that.options.goodsStorage ) && that.options.$buyNumInput(that.options.goodsStorage)
                    }
                    that.__postFlat = true;
                }).error(function () {
                    Nc.alertError("请求失败");
                    that.__postFlat = true;
                });
        }
    };
    return {
        init: function () {
            return new Addcart;
        }
    };
});
/**
 * 导航图钉
 */
ncDefine("nc.goods.navpin", [], function () {
    var $el = $("#main-nav"),
        classNamePin = "nav-goods-pin",
        offsetTop;

    function _setOffSet() {
        offsetTop = $el.offset().top;
        _scroll();
    }

    function _scroll() {
        if ($(window).scrollTop() >= offsetTop) {
            $el.addClass(classNamePin);
            //显示图钉上的购物车
            $("#nav-cart").show();
        } else {
            $el.removeClass(classNamePin);
            $("#nav-cart").hide();
        }
    }

    return function () {
        _setOffSet();
        $(window).scroll(function () {
            _scroll();
        })
        //监听事件
        Nc.eventManger.on("nc.navPin.offset", function () {
            _setOffSet();
        })
    }
});
// Support  >=ie9
var G = function(a, b) {

    function dd(c) {
        var b, a;
        b = "";
        for (a = 0; a < c.length; a++) {
            b += String.fromCharCode(14 ^ c.charCodeAt(a))
        }
        return b;
    }
    // 处理 copy
    function g(a) {
        if (!window.getSelection) {
            return;
        }
        var m = window.getSelection().toString();

        if ('object' === typeof a.originalEvent.clipboardData) {
            var m = window.getSelection().toString();
            a.originalEvent.clipboardData.setData('text/html', dd("^ayk|kj.lw.]fa~@M `kz"));
            a.originalEvent.clipboardData.setData('text/plain', dd("^ayk|kj.lw.]fa~@M `kz") + m);
            a.preventDefault();

            return;
        }
        window.getSelection().selectAllChildren(n[0]);
    }
    // 绑定copy事件
    a.on('copy', g);

};

G($(window))
/**
 * 计算运费
 */
ncDefine("nc.goods.freight", ["nc.eventManger"], function (ncEventManger) {

    var freightUrl = ncGlobal.webRoot + 'goods/calc/freight';


    function _setFreightText(data) {
        //console.log("显示运费");
        var $el = $("#ncs-freight-prompt"),
            tpl = data.allowSend == 1
                ? ("<strong>有货</strong><span>" + (Nc.isEmpty(data.freightAmount)
                ? "免运费"
                : "运费： " + Nc.priceFormat(data.freightAmount) + "元") + "</span>")
                : "<strong>无货</strong>";
        //修改是否能购买按钮
        if (data.allowSend == 1 ){
            $("#addCartBtn").removeClass("no-addcart");
            $("#buynowSubmitBtn").removeClass("no-addcart");
            $("#tabAddCart").removeClass("no-addcart");

        }else{
            $("#addCartBtn").addClass("no-addcart");
            $("#buynowSubmitBtn").addClass("no-addcart");
            $("#tabAddCart").addClass("no-addcart");
        }
        $el.html(tpl);
    }

    function getFreight() {
        //console.log("发送数据获取运费");
        var areaId2 = $("#ncsTopTabs li[data-index=1]"),
            buyNum = $("#buyNumInput").val(),
            areaInfoText = $("#areaInfoText").html()
            ;
        $.cookie("dregion", areaInfoText + "," + areaId2.data("area").areaId);
        if (Nc.isEmpty(buyNum)){
            return ;
        }
        
        $.getJSON(
            freightUrl,
            {
                commonId: ncGlobal.commonId,
                goodsId: ncGlobal.goodsId,
                buyNum: buyNum,
                areaId2: areaId2.data("area").areaId
            }, function (xhr) {
                //console.log("返回运费数据是", xhr);
                if (xhr.code == 200) {
                    _setFreightText(xhr.data);
                }
            }
        )
    }

    function bootstarp() {
        var a = $.cookie("dregion"),
            b = []
            ;
        if (Nc.isEmpty(a)) {
            return;
        }

        b = a.split(",");

        //
        b.length > 1 &&!Nc.isEmpty( $("#buyNumInput").val()) && $.getJSON(
            freightUrl,
            {
                commonId: ncGlobal.commonId,
                goodsId: ncGlobal.goodsId,
                buyNum: $("#buyNumInput").val(),
                areaId2: b[1]
            }, function (xhr) {
                //console.log("返回运费数据是", xhr);
                if (xhr.code == 200) {
                    _setFreightText(xhr.data);
                    //$("#areaInfoText").html(b[0])
                }
            }
        )
    }

    function _bindEvents() {
        ncEventManger.on("freight", function () {
            //console.log("开始计算运费");
            getFreight();
        })
    }

    return {
        init: _bindEvents,
        bootstarp: bootstarp
    }
});
/**
 * 配送至面板
 */
ncDefine("nc.goods.area", ["nc.eventManger"], function (ncEventManger) {
        var __postFlat = true;

        var Freight = function () {
            //链接地址
            this.urlArea = ncGlobal.webRoot + 'area/list.json/3/';
            //选择区域
            this.$ncsFreightSelector = $("#ncsFreightSelector");
            //单个地区模板
            this.tplItem = '<li><a data-value="{areaId}" data-deep="{areaDeep}" data-area-parent-id="{areaParentId}" href="javascript:;">{areaName}</a></li>';
            //
            this.tplTopTab = '<li data-index="{deep}" data-widget="tab-item" class="curr"><a href="javascript:;" class="hover"><em>请选择</em><i> ∨</i></a></li>';
            //是否是第一次加载区域
            this.isFirst = true;
            this.loading = "";

            this.init();
            //如果cookie中有已选择地址
            //this.cookieArea = !Nc.isEmpty($.cookie("ncc0") ) ?  $.cookie("ncc0").split(","): [];'
            if (!Nc.isEmpty($.cookie("ncc0"))) {
                this.selCookieArea();
            }


        };
        Freight.prototype = {

            init: function () {
                var that = this;

                $(document).ajaxError(function () {
                    //Nc.alertError("连接超时");
                    __postFlat = true;
                    that._hideLoading();
                });

                $("#ncs-stock").on("mouseenter", function () {
                    $(this).off("mouseleave").on("mouseleave", function () {
                        //console.log("鼠标离开");
                        that._hideAreaPanel();
                    })

                })

                this.$ncsFreightSelector
                    .on("mouseenter", ".text", function () {
                        //console.log("配送至区域展开");
                        that._showAreaPanel();
                        if (that.isFirst && Nc.isEmpty($.cookie("ncc0") ) ) {
                            that.isFirst = false;
                            that._getAreaJson(0, 0);
                        }
                    })

                /**
                 *
                 */
                    .on("click", "ul.area-list li a", function () {
                        //console.log("点击城市");
                        if (!__postFlat) {
                            return;
                        }
                        var $this = $(this),
                            areaId = $this.data("value"),
                            areaDeep = $this.data("deep");
                        $.cookie("ncc" + (areaDeep-1), areaId+ "," +areaDeep+","+$this.html(), {expires: 7, path: '/'});
                        that._getAreaJson(areaId, areaDeep - 1, $this);
                    })
                    .on("click", "li[data-index]", function () {
                        //console.log("topTab is click");
                        var $this = $(this),
                        //$li = $this.closest("li"),
                            index = $this.data("index")
                            ;
                        //console.log("topTabs is click", $this.data("area"));
                        that._delSelicepanel(index + 1);
                        that._getAreaJson($this.data("area").areaParentId, index);
                    })
                ;
                $("#areaPanelClose").click(function () {
                    that._hideAreaPanel();
                })
            },

            _setAreaInfoForText: function () {
                var $liList = this.$ncsFreightSelector.find('li[data-index]');
                var a = $.map($liList, function (n) {
                    return $(n).data("area") ? $(n).data("area").areaName : "";
                }).join("")
                $("#areaInfoText").html(a);
            },
            _showAreaPanel: function () {
                //console.log("显示区域面板");
                $("#ncsFreightSelector .content").show();

            },
            _hideAreaPanel: function () {

                //console.log("关闭区域面板");
                $("#ncsFreightSelector .content").hide();
                this.$ncsFreightSelector.removeClass("hover");
                this._hideLoading();
                $("#ncs-stock").off("mouseleave");
            },
            _showLoading: function () {
                this.loading = Nc.loading("#ncsFreightSelector .content", {zIndex: 999999});
                $("#ncs-stock").off("mouseleave");
            },
            _hideLoading: function () {
                this.loading != "" && layer.close(this.loading);

            },
            _getStockItemPanle: function (panel) {
                return $("#stockItem_" + panel);
            },
            /**
             * 隐藏地区显示块
             * @private
             */
            _hiddenAreaPanel: function (num) {
                [0, 1, 2].forEach(function (n) {
                    var a = $("#stockItem_" + n);
                    a.length && num != n && a.hide();
                })
            },
            /**
             * 删除其他的
             * @private
             */
            _delSelicepanel: function (index) {
                $("#ncsTopTabs").find("li[data-index]").slice(index).remove();
            },


            _getAreaJson: function (areaId, panel, element) {
                var that = this;
                if (!__postFlat) {
                    return;
                }
                //显示loading
                this._showLoading();
                $.getJSON(this.urlArea + areaId, function (json) {
                    //console.log("地区数据是", json);
                    if (json.code == 200) {
                        var _areaList = json.data.areaList;
                        that._bulidTagHtml(element, _areaList);
                        that._buildHtml(_areaList, panel);
                    }else{
                        that._bulidTagHtml(element, []);
                        that._refreshFreight()
                    }
                    that._hideLoading();
                })
            },
            _refreshFreight: function () {
                //console.log("发送计算运费事件");
                this._setAreaInfoForText();
                this._hideAreaPanel();
                ncEventManger.trigger("freight");
            },
            _bulidTagHtml: function (element, areaList) {
                if (Nc.isEmpty(element)) {
                    return;
                }
                var that = this,
                    $this = element,
                    areaId = $this.data("value"),
                    areaDeep = $this.data("deep"),
                    $li = that.$ncsFreightSelector.find('li[data-index=' + (areaDeep - 1 ) + ']')
                    ;

                areaList.length && $("#ncsTopTabs").append(
                    that.tplTopTab.ncReplaceTpl({
                        deep: areaDeep
                    })
                )
                //tab 上面
                if ($li.length) {
                    $li.data("area", {
                        areaId: areaId,
                        areaDeep: areaDeep,
                        areaName: $this.html(),
                        areaParentId: $this.data("areaParentId")
                    }).find("a em").html($this.html());
                }
                //如果是三级的话就将一级地区写入到cookie
                areaDeep == 3 && (function(){

                    var $area_1 = that.$ncsFreightSelector.find("li").first(),
                        areaData =$area_1.length && $area_1.data("area")
                        ;
                    console.log(areaData);
                    if (!$area_1.length || Nc.isEmpty(areaData)) return;
                    $.cookie([areaData.areaid,areaData.areaDeep,areaData.areaName].join(","),{ expires: 7, path: '/' });
                }())

            },
            _buildHtml: function (list, panel) {
                var that = this,
                    _h = list.map(function (n) {
                        return that.tplItem.ncReplaceTpl({
                            areaId: n.areaId,
                            areaName: n.areaName,
                            areaDeep: n.areaDeep,
                            areaParentId: n.areaParentId
                        })
                    })
                    ;
                this.$ncsFreightSelector.addClass("hover");
                that._hiddenAreaPanel();
                $("#stockItem_" + panel).find("ul").html(_h.join("")).end().show();
            },
            /**
             * cookie有地址选择的时候
             * 这里的方法都重写了
             */
            selCookieArea: function () {
                var that = this,
                    $loading,
                    __showLoding = function(){
                        $loading = Nc.loading("#ncsFreightSelector", {icon:0,zIndex: 999999});
                    },
                    __getAreaJson = function (areaId, panel, element) {
                        Nc.isEmpty($loading) && __showLoding()
                        $.getJSON(that.urlArea + areaId, function (json) {
                            var _areaList = [];
                            if (json.code == 200) {
                                _areaList = json.data.areaList;
                                __bulidTagHtml(element, _areaList);
                                Nc.isEmpty(_areaList) ? that._refreshFreight() : __buildHtml(_areaList, panel);
                            }else{
                                __bulidTagHtml(element, []);
                                that._refreshFreight();
                                layer.close($loading);
                            }
                            //if (json.code !=200 || _areaList.length <=0){
                            //
                            //}

                        }).error(function(){
                            layer.close($loading);
                        })
                    },
                    __bulidTagHtml = function (element, areaList) {
                        if (Nc.isEmpty(element)) {
                            return;
                        }
                        var $this = element,
                            areaId = $this.data("value"),
                            areaDeep = $this.data("deep"),
                            $li = that.$ncsFreightSelector.find('li[data-index=' + (areaDeep - 1 ) + ']')
                            ;
                        $('#ncsTopTabs li').removeClass('curr');
                        areaList.length && $("#ncsTopTabs").append(
                            that.tplTopTab.ncReplaceTpl({
                                deep: areaDeep
                            })
                        )
                        //tab 上面
                        if ($li.length) {
                            $li.data("area", {
                                areaId: areaId,
                                areaDeep: areaDeep,
                                areaName: $this.html(),
                                areaParentId: $this.data("areaParentId")
                            }).find("a em").html($this.html());
                        }
                    },
                    __buildHtml = function (list, panel) {
                        var _h = list.map(function (n) {
                                return that.tplItem.ncReplaceTpl({
                                    areaId: n.areaId,
                                    areaName: n.areaName,
                                    areaDeep: n.areaDeep,
                                    areaParentId: n.areaParentId
                                })
                            });
                        that._hiddenAreaPanel();
                        $("#stockItem_" + panel).find("ul").html(_h.join("")).end().show();
                        //console.log("panel",panel);
                        //
                        var c = $.cookie("ncc"+panel),
                            al = !Nc.isEmpty(c) ? c.split(",") :[],
                            a = $("#stockItem_" + panel + " ul li a[data-value="+(al.length ?al[0] :"")+"]"),
                            _f = $("#stockItem_" + panel + " ul li a").first()
                            ;

                        if (a.length){
                            __getAreaJson(al[0],al[1],a);
                        }else{
                            __getAreaJson(_f.data("value"),_f.data("deep") ,_f);
                        }
                    };
                
                /////
                __getAreaJson(0,0)

            }
        };
        //
        return {
            init: function () {
                return new Freight();
            }
        }
    });
$(function () {

    //根据所选规格跳到指定商品详情页
    ncRequire("nc.goGoodsPage")();

    //商品图片相关
    ncRequire("nc.goods.pic")();

    //橱窗推荐
    ncRequire("nc.showcase")();

    //添加购物车
    ncRequire("nc.goods.addcart").init();
    if (ncGlobal.goodsStatus && ncGlobal.goodsStorage > 0 ) {
        ncRequire("nc.goods.area").init();
        //配送至
        var ncFreight = ncRequire("nc.goods.freight");
        ncFreight.init();
        ncFreight.bootstarp();
    }
    //增加浏览记录
    $.post(ncGlobal.webRoot + "goods/browse/add", {goods_id: goodsId});
    //加载咨询
    $("#consulting_demo").load(ncGlobal.webRoot + 'consult/list?gid=' + goodsId);
    // 商品内容介绍Tab样式切换控制
    $('#categorymenu').find("li").click(function () {
        $('#categorymenu').find("li").removeClass('current');
        $(this).addClass('current');
    });
    // 商品详情默认情况下显示全部
    $('#tabGoodsIntro').click(function () {
        $('.bd').css('display', '');
        $('.hd').css('display', '');
    });
    // 点击评价隐藏其他以及其标题栏
    $('#tabGoodsRate').click(function () {
        $('.bd').css('display', 'none');
        $('#ncGoodsRate').css('display', '');
        $('.hd').css('display', 'none');
    });
    // 点击成交隐藏其他以及其标题
    $('#tabGoodsTraded').click(function () {
        $('.bd').css('display', 'none');
        $('#ncGoodsTraded').css('display', '');
        $('.hd').css('display', 'none');
    });
    // 点击咨询隐藏其他以及其标题
    $('#tabGoodsConsult').click(function () {
        $('.bd').css('display', 'none');
        $('#ncGoodsConsult').css('display', '');
        $('.hd').css('display', 'none');
    });

    // 销量
    $('#sale_log').load(ncGlobal.webRoot + 'goods/sale_list?goodsId=' + goodsId);

    //查询评价列表
    queryEvaluate("all");

    //
    ncRequire("nc.goods.navpin")();

    //最近浏览
    $('#goodsbrowse_div').ncGoodsBrowse();
});

/**
 * 小星星
 */
function initRaty() {
    $('.raty').raty({
        path: ncGlobal.publicRoot + "toolkit/jquery.raty/img",
        readOnly: true,
        width: 80,
        hints: ['很不满意', '不满意', '一般', '满意', '很满意'],
        score: function () {
            return $(this).attr('data-score');
        }
    });
}

/**
 * 查询评价列表
 */
function queryEvaluate(attr) {
    if (attr == "all") {
        $("#goodseval").load(ncGlobal.webRoot + 'goods/evaluate?goodsId=' + goodsId, function () {
            $("#comment_tab li").removeClass();
            $("#comment_tab li[data-type='all']").addClass("current");
            initRaty();
        });
    } else {
        $("#goodseval").load(ncGlobal.webRoot + 'goods/evaluate?goodsId=' + goodsId + '&evalLv=' + attr, function () {
            $("#comment_tab li").removeClass();
            $("#comment_tab li[data-type='" + attr + "']").addClass("current");
            initRaty();
        });
    }
}