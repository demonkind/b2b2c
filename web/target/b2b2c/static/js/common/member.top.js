/**
 * 顶部配送至
 * @type {{init, getAreaIdByCookie}}
 */
var topMyCity = function ($) {

    "use strict";

    var areaUrl = ncGlobal.webRoot + 'area/list.json/0';

    var $element = {};

    var tmpl = '<a href="javascript:;" class="area">配送至：<span id="shortCutAreaName" title="{areaName}" data-id="{areaId}">{areaName}</span> <i></i></a>'

    /**
     * 初始化元素
     * @private
     */
    function _init() {
        $element.shortCutMyCityPanel = $("#shortCutMyCityPanel");
        _getAreaList();
    }

    /**
     * 根据cookie 获取当前选择的配送地址
     * 数据格式:
     * $.cookie("_ncc",[areaId , areaName].join(","))
     * @private
     */
    function _getAreaIdByCookie() {
        return $.cookie("ncc0");
    }

    /**
     * 将选择的配送配送地址写入cookie
     * @private
     */
    function _setAreaIdByCookie(value) {
        return $.cookie("ncc0",value,{ expires: 7, path: '/' });
    }

    /**
     * 异步获取地址
     */
    function _getAreaList() {
        $.getJSON(areaUrl, function (data) {
            if (data.code == "200") {
                _buildElement(data.data.areaList)
            }
        })
    }

    /**
     * 创建元素
     */
    function _buildElement(data) {
        var myCookie = _getAreaIdByCookie(),
            myCity = !Nc.isEmpty(myCookie) ? myCookie.split(",") : ""
            ;
        var a = $.map(data, function(n) {
            return '<a href="javascript:;" data-deep="'+ n.areaDeep+'" data-id="' + n.areaId + '" class="' +
                ((myCity.length && n.areaId == myCity[0]) ? "selected": ""  ) + '">' + n.areaName + '</a>';
        }).join("");
        $element.shortCutMyCityPanel
            .append(tmpl.ncReplaceTpl(myCity ? {
                    areaId: myCity [0],
                    areaName: myCity[2]
                } : {
                    areaId: 0,
                    areaName: "全国"
                }
            ))
            .append(
            a ? '<div id="shortCutAreaList" class="area-list">' + a + '</div>' : '');
    }

    function bindEvents() {
        $element.shortCutMyCityPanel.on("click", "a[data-id]", function () {
            var $this = $(this);
            $("#shortCutAreaList a").removeClass("selected");
            $this.addClass("selected");
            $("#shortCutAreaName")
                .html($this.html())
                .attr({
                    title: $this.html(),
                    "data-id": $this.data("id") ? $this.data("id") : 0
                });
            //修改cookie
            _setAreaIdByCookie([$this.data("id"),$this.data("deep"),$this.html()].join(","));
            location.reload();
        })
    }

    return {
        init: function () {
            _init();
            bindEvents();
        },
        getAreaIdByCookie:_getAreaIdByCookie
    }
}(jQuery);
/**
 * 顶部搜索
 */
var topSearch = function ($) {
    function bindEvents() {
        //选择搜索类型
        $("#hdSearchTab").on("click", "a", function () {
            var a = $("#hdSearchTab a:first"),
                $this = $(this);
            if ($this.is(a)) {
                return;
            }
            $this.insertBefore(a);
            //修改隐藏input
            $("#searchType").val($this.data("type"));
        }).hover(function () {
            $(this).addClass("head-search-select-hover")
        }, function () {
            $(this).removeClass("head-search-select-hover")
        })
        //点击搜索按钮
        $('#button').click(function(){
            if ($('#keyword').val() == '') {
                if ($('#keyword').attr('data-value') == '') {
                    return false
                } else {
                    window.location.href="";
                    return false;
                }
            }
        });
    }

    return {
        init: function () {
            bindEvents();
        }
    }
}(jQuery);
/**
 * 右侧工具条
 */
var rightToolBar = function ($) {
    "use strict";

    var $element;
    var className = {
        variation: "variation"
    };

    /**
     * 根据条件是否显示右侧工具条
     */
    function isShowToolBar() {
        var a = $element.appBarTabs.find("." + className.variation);
        $(window).width() >= 1240 ? a.show() : a.hide();
    }


    function _bindEvents() {
        //浏览器改变大小的时候重新验证是否显示全部右侧工具条
        $(window).resize(function () {
            isShowToolBar();
        });

        //浏览器缩小的时候计算显示还是不显示
        $element.appBarTabs
            .hover(function () {
                $(this).find("." + className.variation).show();
            },
            function () {
                isShowToolBar
            })
        //点击显示会员信息
        $element.barUserInfoBtn.on("click",function() {
            $element.barUserInfo.toggle();
        })
    }

    return {
        init: function () {
            $element = {
                appBarTabs: $("#appBarTabs"),
                barUserInfoBtn:$("#barUserInfoBtn"),
                barUserInfo:$("#barUserInfo")
            };
            //是否显示右侧工具条
            isShowToolBar();
            //绑定事件
            _bindEvents();
        }
    }
}(jQuery);
/**
 * 点击会员登录1
 */
var popupLoging = function($) {
    "use strict";
    var $element;
    //重复标示
    var __showFlat;
    var URL = ncGlobal.webRoot + "login/popuplogin";

    function _bindEvent(){
        $element.popupLoginBtn.on("click",function() {
            showPopupLogin();
        })
    }
    /**
     * 显示登录对话框
     */
	 
    function showPopupLogin(){
        if(__showFlat){
            return ;
        }
        var __showFlat = Nc.layerOpen({
            type: 2,
            title: '您尚未登录',
            content: URL,
            skin:"default",
            area: ['410px', '425px'],
            btn:''
        })
    }
    return {
        init:function() {
            $element= {
                popupLoginBtn:$("#popupLoginBtn")
            };
            _bindEvent()
        },
        showLoginDialog:showPopupLogin
    }
}(jQuery);
//popupLoging.showPopupLogin()
/**
 * 首页左侧分类菜单
 */
var topCategroy = function($) {
    "use strict";
    function _showMenu($menu,$item,top){
        var a = $("#topCategoryMenu").offset(), b = $(window).scrollTop(),
            c = $item.offset().top;

        if(b >= a.top){
            $menu.css({top:b- a.top});
        }else{
            $menu.css({top:2});
        }
    }

    function _bindEvents() {
        $(".category ul.menu").find("li").each(
            function() {
                $(this).hover(
                    function() {

                        var cat_id = $(this).attr("cat_menu_id");
                        var menu = $(this).find("div[cat_menu_id='"+cat_id+"']");
                        menu.show();
                        $(this).addClass("hover");
                        var menu_height = menu.height();
                        if (menu_height < 60) menu.height(80);
                        menu_height = menu.height();
                        var li_top = $(this).position().top;
                        //bycj
                        _showMenu($(menu),$(this) , li_top);
                    },
                    function() {
                        $(this).removeClass("hover");
                        var cat_id = $(this).attr("cat_menu_id");
                        $(this).find("div[cat_menu_id='"+cat_id+"']").hide();
                    }
                );
            }
        );
    }
    return {
        init:function() {
          _bindEvents();
        }
    }
}(jQuery);
/**
 * 购物车相关
 */
var myCart = function ($) {

    "use strict";

    var addCartUrl = ncGlobal.webRoot + "cart/add";
    //购物车post 标示
    var addCartFlat = true;
    var $element;

    /**
     * 添加购物车
     * @param goodsId
     */
    function addCartByGoodsId(goodsId, buyNum) {
        $(document).ajaxError(function (event, request, settings) {
            //Nc.alertError("TODO:连接超时");
            addCartFlat = true;
        });
        if (!addCartFlat) {
            return;
        }
        addCartFlat = false;
        $.get(
            addCartUrl,
            {
                goodsId: goodsId,
                buyNum: buyNum
            },
            function (xhr) {
                if (xhr.code == "200") {
                    Nc.eventManger.trigger("add.cart.succeed");
                    //$("body").trigger("add.cart.succeed");
                } else {
                    Nc.eventManger.trigger("add.cart.error");
                    Nc.alertError(xhr.message ? xhr.message : "连接超时");
                }
                addCartFlat = true;
            },
            'json'
        )
    }


    /**
     * 全局事件
     */
    var bindCommonEvents = function () {
    };
    //
    return {
        //添加购物车
        addCartByGoodsId: addCartByGoodsId,
        init: function () {
            //$element.topMyCart.find(".incart-goods-box").perfectScrollbar({suppressScrollX: true});

            //绑定全局事件
            bindCommonEvents();
        }
    }
}(jQuery);
/**
 *顶部我的商城、我的订单什么的
 */
var topQuickMenu = function() {


    var $element = {
        //quickmenu显示区域
        quickMenu:""
    };
    var className = {
        hover:"hover"
    };


    function _bindEvents() {
        var hoverElement = [".my-mall",".my-order",".mobile-mall",".call-center",".mobile-mall",".my-favorite"];
        //我的商城鼠标滑过
        $element.quickMenu.find(hoverElement.join(",")).hover(function(e) {
            $(this).addClass(className.hover);
        },function(e) {
            $(this).removeClass(className.hover);
        })
    }


    return {
        init:function() {

            $element.quickMenu = $("#quickMenu");
            _bindEvents();
        }
    }
}();
//分享商品
(function($) {
    var ShareGoods = function(element, option) {
        //console.log(option);


        /*初始化元素*/
        this.$element = $(element);
        this.options = $.extend({}, ShareGoods.setting, option);
        this.init();
    };
    /**
     * 默认设置
     * @type {{}}
     */
    ShareGoods.setting = {};
    ShareGoods.prototype = {
        init: function() {
            var that = this;
            //显示登录对话框
            that._showShareGoodsDialog(true);
        },
        /**
         * 显示登录对话框
         */
        _showShareGoodsDialog: function(){
            if(__showFlat){
                return ;
            }
            var __showFlat = Nc.layerOpen({
                type: 2,
                title: '分享商品',
                content: ncGlobal.webRoot + "share/goods?goodsid="+this.options.goodsId,
                skin:"default",
                area: ['500px', '440px'],
                btn:''
            });
        }
    };
    //多转单
    function Plugin(option) {
        return new ShareGoods(this, option);
    }
    //jquery 绑定
    $.fn.ncShareGoods = Plugin;
})(jQuery);
/**
 * 关注
 */
var favorites = function ( $ ) {
    //重复提交标示
    var __postFlat = true ;
    /**
     * 商品关注
     * @param goodsId
     * @returns {boolean}
     */
    function favoritesGoods( goodsId ,cartId) {
        if ( goodsId <= 0 ) {
            Nc.alertError( "参数错误" );
            return false;
        }
        if(!__postFlat){
            return ;
        }
        __postFlat = false;

        var params = { "goodsId": goodsId };
        $.post( ncGlobal.webRoot + "favorite/goods", params, function ( xhr ) {
            if ( xhr.code == 200 ) {
                //显示数量累计
                var currNum = parseInt( $( "[nc_type='goodsFavoritesNum']" ).html() );
                if ( currNum <= 0 ) {
                    currNum = 0;
                }
                $( "[nc_type='goodsFavoritesNum']" ).html( currNum + 1 );
                Nc.alertSucceed( "关注成功" );
                //console.log("发送关注成功事件");

            } else {
                if ( xhr.data && xhr.data.errorType && xhr.data.errorType == "noLogin" ) {
                    popupLoging.showLoginDialog();
                } else {
                    Nc.alertError( xhr.message );
                }
            }
            __postFlat = true;
        } ).always(function() {
            __postFlat = true;
            //发送关注商品完成事件
            Nc.eventManger.trigger("goods.favorites.end",[goodsId,cartId]);
        });
        return false;
    }


    /**
     * 关注店铺
     * @param storeId
     * @returns {boolean}
     */
    function favoritesStore(storeId){
        if (storeId <= 0) {
            Nc.alertError("参数错误");
            return false;
        }
        if(!__postFlat){
            return ;
        }
        __postFlat = false;
        var params = {"storeId":storeId};
        $.post(ncGlobal.webRoot + "favorite/store", params, function(xhr){
            if (xhr.code == 200) {
                //显示数量累计
                var currNum = parseInt($("[nc_type='storeFavoritesNum']").html());
                if (currNum <= 0) {
                    currNum = 0;
                }
                $("[nc_type='storeFavoritesNum']").html(currNum+1);
                Nc.alertSucceed("店铺关注成功");

            }else{
                if (xhr.data && xhr.data.errorType && xhr.data.errorType == "noLogin") {
                    popupLoging.showLoginDialog();
                }else{
                    Nc.alertError(xhr.message);
                }
            }
            __postFlat = true;
        }).fail(function() {
            __postFlat = true;
        });
        return false;
    }
    ////
    return {
        goods: favoritesGoods,
        store:favoritesStore
    }
}( jQuery )

/**
 * 顶部搜索
 */
var topMemberRelatedDate = function ($) {
    function bindEvents() {
        $.getJSON(ncGlobal.webRoot+"index/member/relateddate", function (data) {
            if (data.ordersCount && data.ordersCount > 0) {
                $("#topMemberOrders").html("(" + data.ordersCount + ")");
            }
            if (data.consultNoReadCount && data.consultNoReadCount > 0) {
                $("#topMemberConsult").html("(" + data.consultNoReadCount + ")");
            }
            if (data.favoritesGoodsCount && data.favoritesGoodsCount > 0) {
                $("#topMemberFavoritesGoods").html("(" + data.favoritesGoodsCount + ")");
            }
            if (data.goodsBrowseCount && data.goodsBrowseCount > 0) {
                $("#topMemberGoodsBrowse").html("(" + data.goodsBrowseCount + ")");
            }
            if (data.messageUnreadCount && data.messageUnreadCount > 0) {
                $("#topMessageUnreadCount").html("(" + data.messageUnreadCount + ")");
                $("<sub><a href='"+ ncGlobal.memberRoot +"message/list' title='您有" + data.messageUnreadCount + "条未读消息'>" + data.messageUnreadCount + "</a></sub>").appendTo("#memberLayoutMessageUnreadCount");
            }
            return false;
        });
    }
    return {
        init: function () {
            bindEvents();
        }
    }
}(jQuery);

$( function () {
    //顶部配送至
    topMyCity.init();
    //顶部搜索
    topSearch.init();
    popupLoging.init();
    //首页左侧分类菜单
    topCategroy.init();
    //购物车
    $("#topMyCart").ncCart();
    Nc.eventManger.trigger("nc.cart.init");
    topQuickMenu.init();
    //查询顶部会员相关数据
    topMemberRelatedDate.init();

    //关注商品
    $( "[nc_type='goodsFavoritesBtn']" ).click( function () {
        var data_str = $( this ).attr( 'data-param' );
        if ( !data_str ) {
            Nc.alertError( "参数错误" );
            return false;
        }
        eval( "data_str = " + data_str );
        favorites.goods( data_str.goodsId );
    } );

    //关注店铺
    $("[nc_type='storeFavoritesBtn']").click(function(){
        var data_str = $(this).attr('data-param');
        if(!data_str){
            Nc.alertError("参数错误");
            return false;
        }
        eval("data_str = "+data_str);
        favorites.store(data_str.storeId);
    });
} )
