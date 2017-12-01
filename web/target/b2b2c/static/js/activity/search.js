(function($, mycart) {
    /**
     * 商品详情
     * @constructor
     */
    var GoodsItem=function(element, option) {

        this.options=_default;
        this.$element=$(element);
        //规格多图相关
        this.goodsPicImg=this.$element.find(".goods-pic img");
        this.goodsPicA=this.$element.find(".goods-pic a");
        this.goodsPicScrollShow=this.$element.find(".goods-pic-scroll-show");
        this.goodsPicScrollShowUl=this.goodsPicScrollShow && this.goodsPicScrollShow.find(".show-box ul")
        this.moveNum=0;
        this.moveMax=0;

        //购物车
        this.$goodsInfo=this.$element.find(".goods-info");
        this.$shoppingBox=this.$goodsInfo.find("div[nc-shopping-box]");
        this.$addCartBtn=this.$element.find("[nc-add-cart]");
        //购买数量显示区域
        //this.$shoppingNum = this.$element.find("[data-shopping-num]");
        //goods id
        this.goodsId=this.$addCartBtn.attr("nc-add-cart");
        //goods common id
        this.commonId=this.$addCartBtn.data("commonId");
        //购物车添加类型
        this.addCartType=this.$addCartBtn.data('addCartType');
        //购物车显示数量的input
        this.$cartNumInput=$("#cartNumInput_" + this.goodsId);
        //缓存商品信息
        this.goodsInfo = {};
        //商品主图
        this.$bigImage=this.$element.find("img[nc-goods-pic]");


        /*显示商品规格弹出框*/
        //商品规格弹出框id
        this.$seriesProBuy=$("#seriesProBuy");

        //post 重复提交标示
        this.__postFlat=true;
        this.init();
    };
    //cn 是className 的简写
    //tpl 是模板的
    _default={
        cnHandle: "handle",
        //规格对话框上的li 选中样式
        cnHovered: "hovered",

        cnNOAddCart: "no-addcart",
        picMax: 7,
        picPx: 25,
        /*关于添加购物车*/
        //数量不能减少时的css
        cnDisDecrease: "dis_decrease",
        //数量能够减少时的css
        cnDecrease: "decrease",
        urlAddCart: ncGlobal.webRoot + "cart/add",

        //弹出框中的规格图片模板
        tplSpTxt: '<li><a href="javascript:void(0)" data-spec-value="{specValueId}" class="{cnHovered}">{specValueName}<sub></sub></a></li>',

        tplSpImg: '<li class="<% if(colorImgSrc != undefined ) {%>sp-img<% } %>"><a href="javascript:void(0);" data-spec-value="<%=specValueId %>" class="<%=cnHovered%>" title="<%=specValueName%>"><% if(colorImgSrc != undefined ) {%><i class="sp-img-thumb"><img src="<%=colorImgSrc%>"/></i> <% } %><%=specValueName%><sub></sub></a></li>'

    }


    GoodsItem.prototype={
        init: function() {
            this.buildElememt();
            this.bindEvents();
        },
        buildElememt: function() {
            if(this.goodsPicScrollShow.length == 0) {
                return
            }
            var a=this.goodsPicScrollShow.find("li").length;
            if(a > this.options.picMax) {
                this.moveMax=a - (this.options.picMax - 1);
                this.goodsPicScrollShow.addClass(this.options.cnHandle).find(".cBtn").show();
            }
        },
        bindEvents: function() {
            var that=this;
            //商品规格多图事件
            this.goodsPicScrollShow
                .on("click", "li", function(event) {

                    var $this=$(this),
                        showPicUrl=$this.data("showPic")
                        ;
                    //显示图片
                    that.goodsPicImg.attr("src", showPicUrl);
                    //修改商品id
                    $(this).siblings("li").removeClass("selected").end().addClass("selected");
                })
                .on("click", ".prev", function(event) {
                    event.preventDefault();
                    if(that.moveNum == 0 || (that.goodsPicScrollShowUl.is(":animated"))) {
                        return;
                    }

                    that.goodsPicScrollShowUl.animate({
                        left: that.goodsPicScrollShowUl.position().left + that.options.picPx
                    }, "fast")
                    that.moveNum--;
                })
                .on("click", ".next", function(event) {
                    event.preventDefault();
                    if((that.moveNum >= that.moveMax - 1) || (that.goodsPicScrollShowUl.is(":animated"))) {
                        return;
                    }
                    that.goodsPicScrollShowUl.animate({
                        left: that.goodsPicScrollShowUl.position().left - that.options.picPx
                    }, "fast")
                    that.moveNum++;
                });
            //购物车中的数量修改
            this.$shoppingBox
                .on("click", "[nc-cart-add]", function(event) {
                    event.preventDefault();
                    //数量增加
                    that.$cartNumInput.val(Nc.number.add(Math.abs(that.$cartNumInput.val()), 1));
                    $(this).siblings("[nc-cart-cut]").removeClass(that.options.cnDisDecrease).addClass(that.options.cnDecrease)
                })
                .on("click", "[nc-cart-cut]", function(event) {
                    event.preventDefault();
                    if(that.$cartNumInput.val() == 1) {
                        return;
                    }
                    var $this=$(this), _num=Nc.number.sub(Math.abs(that.$cartNumInput.val() ), 1)
                    //console.log("购物车数量减少", _num)
                    that.$cartNumInput.val(_num);
                    _num == 1 && ($this.removeClass(that.options.cnDecrease).addClass(that.options.cnDisDecrease))
                });
            //购物车商品数量 input 文字修改
            this.$cartNumInput.on("keyup", function() {
                var $this=$(this);
                //console.log("购物车商品数量 input 文字修改");
                if(!Nc.isDigits($this.val()) || $this.val() == 0 || $this.val() == '' ) {
                    $this.val(1);
                }

            });
            //点击添加购物车按钮的事件
            this.$addCartBtn.on("click", function() {
                if(that.addCartType == 'nospec') {
                    mycart.addCartByGoodsId(that.goodsId, that.$cartNumInput.val());
                    //console.log("飞入购物车特效")
                    that._flyToCart(that.$bigImage);
                } else {
                    that._showSeriesProBuy(that.goodsId)
                }
            })
        },

        /*
         *飞入购物车动画
         */
        _flyToCart: function($img){
            var $element = $("#nav-cart").is(":hidden") ? $("#topMyCart"): $("#nav-cart");
            var rtoolbar_offset_left=$element.offset().left;
            var rtoolbar_offset_top=$element.offset().top - $(document).scrollTop();
            var img=$img.attr('src');
            var flyer=$('<img id="fly"class="u-flyer" src="' + img + '" style="z-index:198910151;width:' + $img.width() + 'px;height:' + $img.height() + 'px;">');
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


    };
    /*关于弹出规格框*/
    $.extend(GoodsItem.prototype, {
        /**
         *获取规格列表
         */
        _getSpecPanel: function() {
            return this.commonId ? $("#nc-spec-panel-" + this.commonId) : [];
        },
        /**
         * 获取对话框上的+号
         */
        _getModalCartAdd: function() {
            return $("#modalCartAdd_" + this.goodsId);
        },
        /**
         * 获取对话框上的 - 号
         */
        _getModalCartCut: function() {
            return $("#modalCartCut_" + this.goodsId);
        },

        _getModelCartInput: function() {
            return $("#modelCartInput_" + this.goodsId);
        },
        /**
         * 获取对话框上的购物车数量
         */
        _getModalAddCartBtn: function() {
            return $("#modalAddCartBtn_" + this.goodsId);
        },
        /**
         * 根据id获取图片地址
         * @param specId
         * @private
         */
        _getSpecImgBySpId: function(specId, imgLength) {
            //console.log("根据id获取图片地址", specId);
            var a=imgLength != 1
                ? this.goodsPicScrollShow.find('img[data-color-img-id="' + specId + '"]')
                : this.$element.find("img[nc-goods-pic]");
            return a.length != 0 ? a.data("colorImgSrc") : '';
        },
        /**
         * 显示，通过这个函数启动
         * @private
         */
        _showSeriesProBuy: function() {
            var that=this;
            //初始化数据
            this._initSpecValue();

            //弹出显示规格的对话框
            layer.open({
                area: ["auto"],
                type: 1,
                title: "请选择规格",
                content: that._buildSpecHtml()
            });
            //弹出框规格缩略图片
            $('.sp-img-thumb img').jqthumb({
                width: 30,
                height: 30,
                after: function(imgObj) {
                    imgObj.css('opacity', 0).animate({ opacity: 1 }, 1000);
                }
            });

            //绑定事件
            this._bindEventsToModel();

            //初始化数据
            this._getModalInfo(this.goodsId);
        },
        /**
         * 根据按钮上的信息初始化规格数据
         * 单独写了一下，方便以后改变数据
         * @private
         */
        _initSpecValue: function() {
            //this.specValue = this.$addCartBtn.data("specValue");
            this.specValue=this._formatSpecValueJson(this.$addCartBtn.data("specValue"));
            this.specJson=this.$addCartBtn.data("specJson");

            //默认数据
            this.defaultGoodsPrice=this.$goodsInfo.find(".goods-price");
            this.defaultGoodsName=this.$goodsInfo.find(".goods-name");
            this.defaultGoodsJingle=this.$goodsInfo.find(".goods-jingle");
            //this.defaultGoodspic = this.

            //console.log("this.specValue", this.specValue)
            //console.log("this.specJson", this.specJson)
        },
        /**
         * 格式化规格值列表
         */
        _formatSpecValueJson: function(data) {
            var that=this, _r={};
            $.each(data, function(i, n) {
                _r[n.specValueIds]=n.goodsId;
            });
            return _r;
        },
        /**
         * 刷新价格等信息
         */
        _refreshPrice: function() {

            var _panel=this._getSpecPanel(), a, b;
            if(_panel.length == 0) {
                return;
            }
            a=$.map(_panel.find("a.hovered"), function(n, i) {
                return $(n).data("specValue");
            });
            //如果正向没有就反向
            b=a.join(",");
            a=a.ncArraySort().join(",");
            //post获取数据
            this._getModalInfo(this.specValue[a] ? this.specValue[a] : this.specValue[b]);
        },

        /**
         *
         * @private
         */
        _getModalInfo: function(goodsId) {
            var that=this;
            //显示遮罩层
            var a = Nc.loading("#popNcsInfoModal");
            $.post(
                ncGlobal.webRoot + "get/sku.json",
                {
                    goodsId: goodsId
                },
                function(xhr) {
                    if(xhr.code == '200') {
                        var data=xhr.data,
                            _$buyNum = $("#shoppingNumPanel");
                            ;
                        //缓存商品信息
                        that.goodsInfo = data;
                        //console.log(xhr)
                        $("#modalImg").attr("src", data.imageSrc);
                        $("#modalGoodsName").html(data.goodsName);
                        $("#modalJingle").html(that.defaultGoodsJingle.find("a").html())
                        $("#modelPrice").html(Nc.priceFormat(data.goodsPrice)).closest(".goods-price").show();
                        that.modalGoodsMax=data.goodsStorage;
                        //改变商品详情的href
                        $("#goGoodsInfo").attr("href",ncGlobal.webRoot + "goods/"+ goodsId);
                        // console.log("修改加入购物车goodsId");
                        that._getModalAddCartBtn().attr("data-add-cart",goodsId);

                        if(data.goodsStorage < 1) {
                            that._getModalAddCartBtn().addClass(that.options.cnNOAddCart);
                            _$buyNum.hide();

                        } else {
                            that._getModalAddCartBtn().removeClass(that.options.cnNOAddCart);
                            _$buyNum.show();
                            that._getModelCartInput().triggerHandler("keyup");
                        }
                    }
                }
            ).always(function() {
                    that.__postFlat=true;
                    layer.close(a);
                })
        },
        /**
         * 生产规格弹出框htmlthat.options.tplSpImg.ncReplaceTpl
         */
        _buildSpecHtml: function() {
            var that=this;
            var a=$.map(this.specJson, function(n) {
                var b=$.map(n.specValueList, function(nn, i) {
                    return n.specId != 1
                        ? that.options.tplSpTxt.ncReplaceTpl({
                        specValueId: nn.specValueId,
                        cnHovered: i == 0 ? that.options.cnHovered : "",
                        specValueName: nn.specValueName
                    })
                        :  ncTemplate(that.options.tplSpImg)({
                        specValueId: nn.specValueId,
                        cnHovered: i == 0 ? that.options.cnHovered : "",
                        specValueName: nn.specValueName,
                        colorImgSrc: that._getSpecImgBySpId(nn.specValueId, n.specValueList.length)
                    })
                });
                return '<dl><dt>' + n.specName + '</dt><dd> <ul>' + b.join('') + '</ul></dd></dl>'
            });

            return '<div class="pop-ncs-info-model" id="popNcsInfoModal">'
                + '<div class="goods-pic"><img id="modalImg" src="" alt=""/></div>' +
                '<div class="goods-info"><h3 id="modalGoodsName"></h3><h4 id="modalJingle"></h4>' +
                '<div class="goods-price" style="display: none">￥ <em id="modelPrice"></em></div>' +
                '</div></div>' +
                '<div class="pop-ncs-spec-model" id="nc-spec-panel-' + that.commonId + '">' + a.join("") + '</div>' +
                '<div class="ncs-pop-buy-model">' +
                '<div class="shopping-num" id="shoppingNumPanel">' +
                '<h4>购买数量</h4>' +
                '<input id="modelCartInput_' + this.goodsId + '" type="text" value="1" maxlength="3">' +
                '<span><a class="add" id="modalCartAdd_' + this.goodsId + '" href="javascript:;">加</a>' +
                '<a class="dis_decrease" id="modalCartCut_' + this.goodsId + '" href="javascript:;">减</a>' +
                '</span>' +
                '</div>' +
                '<div class="shopping-btns">' +
                '<a href="javascript:;" id="modalAddCartBtn_' + this.goodsId + '" class="cart" data-add-cart="'+this.goodsId+'"><i></i>加入购物车</a>' +
                '<a href="' + ncGlobal.webRoot + 'goods/' + this.goodsId + '" target="_blank" class="url" id="goGoodsInfo">查看商品详情</a>' +
                '</div>' +
                '</div>';
        },
        /**
         * 在model生成后绑定事件
         */
        _bindEventsToModel: function() {
            var that=this;
            //点击选择规格弹出框中的规格小图片
            this._getSpecPanel().on("click", "a", function(e) {
                var $this=$(this);
                if(!that.__postFlat) {
                    return;
                }
                ;
                that.__postFlat=false;

                $this.closest("ul").find("a").removeClass(that.options.cnHovered)
                $this.addClass(that.options.cnHovered);
                //刷新价格区域
                that._refreshPrice();
            });
            //modal 购物车数量增加
            this._getModalCartAdd().on("click", function(event) {
                event.preventDefault();
                //console.log("购物车数量增加")
                //数量增加
                var a=that._getModelCartInput();
                a.val(Nc.number.add(a.val(), 1));
                that._getModalCartCut().removeClass(that.options.cnDisDecrease).addClass(that.options.cnDecrease);
                that._getModelCartInput().triggerHandler("keyup");
            });
            //modal 购物车数量减少
            this._getModalCartCut().on("click", function(event) {
                event.preventDefault();
                var a=that._getModelCartInput();
                if(a.val() == 1) {
                    return;
                }
                var $this=$(this), _num=Nc.number.sub(a.val(), 1)
                //console.log("购物车数量减少", _num)
                a.val(_num);
                _num == 1 && ($this.removeClass(that.options.cnDecrease).addClass(that.options.cnDisDecrease))
            });
            //购物车商品数量 input 文字修改
            this._getModelCartInput().on("keyup", function() {
                var $this=$(this);
                ////console.log("购物车商品数量 input 文字修改");
                //console.log("最大库存", that.modalGoodsMax);
                if(!Nc.isDigits($this.val())) {
                    Nc.getNum($this.val()) > that.modalGoodsMax
                        ? $this.val(that.modalGoodsMax)
                        : ($this.val(1), that._getModalCartCut.removeClass(that.options.cnDecrease).addClass(that.options.cnDisDecrease));
                } else {
                    $this.val() > that.modalGoodsMax ? $this.val(that.modalGoodsMax) : ($this.val() <= 0 && $this.val(1))
                }
            });
            //点击添加购物车按钮的事件
            this._getModalAddCartBtn().on("click", function() {
                var a=that._getModelCartInput();
                if(that.modalGoodsMax < a.val()) {
                    return;
                }
                //console.log("添加购物车");
                var attrGoodsId = $(this).attr("data-add-cart");
                mycart.addCartByGoodsId(!Nc.isEmpty(attrGoodsId) ?attrGoodsId :that.goodsId, a.val());
                that._flyToCart($("#modalImg"));
            })
        }
    })

    function Plugin(option) {
        return this.each(function() {
            var $this=$(this);
            var data=$this.data('nc.item');
            if(!data) $this.data('nc.item', (data=new GoodsItem(this, option)))
        })
    }

    $.fn.GoodsItem=Plugin;
})(jQuery, myCart);


/**
 * 搜索
 */
var homeSearch=function() {
    return {
        init: function() {
            $("div[goods-item]").GoodsItem();
        }
    };
}();

/**
 * 品牌
 */
var brandShow=function() {
    var options={
        //品牌首字母搜索
        $nchBrandTab: $("#nchBrandTab"),
        //配牌显示区域
        $ncBrandlist: $("#ncBrandlist"),
        //显示更多的按钮
        $nchBrandMoreBtn: $("#nchBrandMoreBtn"),

        cnCurrent: "current",

        tplShow: '更多<i class="drop-arrow"></i>',
        tplHide: '收起<i class="up-arrow"></i>'

    };
    var moreOpen=false;

    /**
     * 事件
     */
    function bindEvents() {


        //点击显示更多按钮
        options.$nchBrandMoreBtn.click(function(xhr) {
            if(moreOpen == false) {
                options.$nchBrandTab.show();
                options.$ncBrandlist.find("li").show();
                options.$nchBrandMoreBtn.html(options.tplHide)
                //添加滚动条
                options.$ncBrandlist.perfectScrollbar();
            } else {
                //归位
                options.$nchBrandTab.find("li").removeClass(options.cnCurrent).show().first().addClass(options.cnCurrent);
                options.$nchBrandTab.hide();
                options.$ncBrandlist.find("li").show().filter(":gt(15)").hide();
                options.$nchBrandMoreBtn.html(options.tplShow)
                options.$ncBrandlist.perfectScrollbar('destroy');
            }
            moreOpen= !moreOpen;
            //动态修改触发位置
            Nc.eventManger.trigger("nc.navPin.offset");
        })
        //大写
        options.$nchBrandTab.on("mouseover", "a[data-letter]", function() {
            var $this=$(this),
                a=$this.data("letter"),
                _list=options.$ncBrandlist.find("li")
                ;
            //改变样式
            $this.closest("li").siblings("li").removeClass(options.cnCurrent).end().addClass(options.cnCurrent);
            if(a == 'all') {
                _list.show();
                return
            }
            _list.hide().filter("[data-initial='" + a + "']").show();
            //动态修改触发位置
            Nc.eventManger.trigger("nc.navPin.offset");
        });
    }

    ///
    return {
        init: function() {

            bindEvents();
        }
    }


}();

/**
 * 导航滚动固定特效
 */
var navPin=function() {

    var $mainNav=$("#main-nav"),
        //最开始的坐标
        offsetTop = 0
        ;

    var classNamePin="nav-pin";

    /**
     * 修改位置
     * @private
     */
    function _setOffSet(){
        offsetTop = $mainNav.offset().top;
        _scroll();
    }
    function _scroll() {
        if( $(window).scrollTop()  >= offsetTop){
            $mainNav.addClass(classNamePin);
            //显示图钉上的购物车
            $("#nav-cart").show();
        }else{
            $mainNav.removeClass(classNamePin);
            $("#nav-cart").hide();
        }
    }
    return {
        init: function() {
            _setOffSet();
            $(window).scroll(function() {
                //console.log("window top is ",$(window).scrollTop())
                //console.log("mainnav top is ",$mainNav.offset().top)
                _scroll();
            })
            //监听事件
            Nc.eventManger.on("nc.navPin.offset",function(){
                _setOffSet();
            })
        },
        /**
         * 修改位置
         */
        setOffSet:_setOffSet
    }
}();
/**
 * 属性相关
 */
var goodsAttrItem=function(navPin) {
    var options={
        //显示更多属性按钮
        $showAttrMoreBtn: $("#showAttrMoreBtn"),
        //属性item
        $goodsAttrList: $("dl[goods-attr-item]"),
        //收起、放下的样式
        cnExpand: "expand",
        //属性更多按钮显示样式
        cnExtMore: "ext-more",
        //属性更多按钮 不显示样式
        cnAttrItem: "attr-item"
    };
    var __showFlat=true;
    var __showMore=true;

    function _buildElement() {
        //是否显示属性右侧的更多按钮
        _showExtMore();
    }

    function _bindEvents() {

        //点击显示更多属性按钮事件
        options.$showAttrMoreBtn.click(function(event) {
            event.preventDefault();
            //console.log("点击显示更多属性按钮事件");
            if(__showFlat == true) {
                options.$goodsAttrList.show();
                options.$showAttrMoreBtn.addClass(options.cnExpand).find("span").html("收起")

            } else {
                options.$goodsAttrList.slice(3).hide()
                options.$showAttrMoreBtn.removeClass(options.cnExpand).find("span").html("更多选项");
            }
            __showFlat= !__showFlat;
            _showExtMore();
            //动态修改触发位置
           Nc.eventManger.trigger("nc.navPin.offset");
        });
        //规格右边点击更多的事件
        options.$goodsAttrList.find("dd[data-ext-more]").click(function() {
            var _dd=$(this).siblings("dd"), $this=$(this);
            //console.log("more点击事件");
            if(__showMore) {
                _dd.removeClass(options.cnAttrItem).addClass(options.cnExtMore);
                $this.find("span").html("收起");
            } else {
                _dd.removeClass(options.cnExtMore).addClass(options.cnAttrItem);
                $this.find("span").html("更多");
            }
            __showMore= !__showMore;

            //动态修改触发位置
            Nc.eventManger.trigger("nc.navPin.offset");
        });
        //窗体改变之后是否显示属性右侧的更多按钮
        $(window).resize(function() {
            //console.log("窗体改变之后是否显示属性右侧的更多按钮")
            _showExtMore();
        });
    }

    /**
     * 是否显示属性右侧的更多按钮
     * @private
     */
    function _showExtMore() {
        //属性值列表是否显示更多
        $.each(options.$goodsAttrList, function(i, n) {
            var $n=$(n),
                _ul=$n.find(".list ul"),
                _li=_ul.find("li"),
                maxWidth=0
                ;
            $.each(_li, function(ii, nn) {
                maxWidth+=($(nn).outerWidth(true) + 0)
            })
            if(maxWidth > _ul.width()) {
                $n.find("dd[data-ext-more]").show();
                //
            } else {
                $n.find("dd[data-ext-more]").hide();
                //$n.closest("dd").removeClass(options.cnExtMore).addClass(options.cnAttrItem)
            }

        })
    }

    return {
        init: function() {
            _buildElement();
            _bindEvents();
        }
    }
}(navPin);

$(function() {
    homeSearch.init();
    brandShow.init();
    goodsAttrItem.init();
    //加载滚动固定特效
    navPin.init();
    //购物车插件
    $("#nav-cart").ncCart();
    //最近浏览
    $('#goodsbrowse_div').ncGoodsBrowse();
    countdown();
})
var countdown = function(){
	var $tims = $(".nch-g-list ul li .Countdown");
	$.each($tims,function(i,v){
		var starttime = $(this).attr("data-startTime"); //开始时间
		var endtime = $(this).attr("data-endTime");
		var type= $(this).attr("data-type");  //是否是开始
		var atype = $(this).attr("data-atype");
		var _this = $(this);
		var ss ; // 时间戳
		if(type == 0){ 
			if(starttime){
				ss = TimeObjectUtil.timeStamp("",starttime); //活动未开始   开始时间-当前时间 
			}
			
		}else{
			if(endtime){
				ss = TimeObjectUtil.timeStamp("",endtime);//活动已开始  结束时间-当前时间
			}
		}
		
		var t;
		if(ss < 0){
			ss = TimeObjectUtil.timeStamp("",endtime);//活动已开始  结束时间-当前时间
			type =1;
		}
		if(ss !=undefined){
			 t = setInterval(function(){
				var d = TimeObjectUtil.getTime(ss);
				//活动未开始
				if(type ==0){
					$(_this).text("距抢购开始:"+d.toString);
					//$("#buynowSubmitBtn").addClass("no-addcart");
					$(_this).parents(".goods-content").find(".favorite-btn a").attr("href","javascript:;").addClass("disabled");
					//预售
//					if(atype==2){
//						$(_this).parents(".goods-content").find(".favorite-btn a").attr("href","javascript:;").addClass("disabled");
//						$(_this).parents(".goods-content").find(".goods-pic >a").attr("href","javascript:;").removeAttr("target");
//					}
					if(ss <= 0){
						ss = TimeObjectUtil.timeStamp("",endtime);//改变为活动已开始
						type = 1;
//						if(atype==2){
//							var href = $(_this).parents(".goods-content").find(".favorite-btn a").attr("data-href");
//							$(_this).parents(".goods-content").find(".favorite-btn a").attr("href",href).removeClass("disabled");
//							$(_this).parents(".goods-content").find(".goods-pic >a").attr("href",href).attr("target","_blank");
//						}
					}
				}else{
					$(_this).text("距活动结束:"+d.toString);
					//$("#buynowSubmitBtn").removeClass("no-addcart");
					if(ss <= 0){
						$(_this).text("活动已结束");
						clearInterval(t);
//						$(_this).parents(".goods-content").find(".grab_btn").attr("disabled",true);
//						$(_this).parents(".goods-content").find(".favorite-btn a").removeAttr("href").addClass("disabled");
//						$(_this).parents(".goods-content").find(".goods-pic >a").attr("href","javascript:;").removeAttr("target");
					}
				}
				ss -=1000;
			},1000)
		}
	})
	}
