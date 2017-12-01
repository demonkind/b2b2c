//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //加载本周上新
        $("[ncType='favNewGoodsBtn']").click(function(){
            var store_id = $(this).attr("store_id");
            $("#favHotGoodsBtn_"+store_id).removeClass("current");
            $(this).addClass("current");
            $("#favHotGoodsContent_"+store_id).hide();
            $("#favNewGoodsContent_"+store_id).show();

            if (!$.trim($("#favNewGoodsContent_"+store_id).html())) {
                if (store_id <= 0) {
                    return false;
                }
                $.ajax({
                    url: ncGlobal.memberRoot+"favorites/store/newgoods?storeid="+store_id+"&pagesize=5",
                    success: function(html){
                        $('#favNewGoodsContent_'+store_id).html(html);
                    }
                });
            }
        });
        $("[ncType='favNewGoodsBtn']").trigger("click");

        //热销商品
        $("[ncType='favHotGoodsBtn']").click(function(){
            var store_id = $(this).attr("store_id");

            $("#favNewGoodsBtn_"+store_id).removeClass("current");
            $(this).addClass("current");
            $("#favNewGoodsContent_"+store_id).hide();
            $("#favHotGoodsContent_"+store_id).show();

            if (!$.trim($("#favHotGoodsContent_"+store_id).html())) {
                if (store_id <= 0) {
                    return false;
                }
                $.ajax({
                    url: ncGlobal.memberRoot+"favorites/store/hotgoods?storeid="+store_id+"&pagesize=5",
                    success: function(html){
                        $('#favHotGoodsContent_'+store_id).html(html);
                    }
                });
            }
        });

        //删除关注店铺
        $("[nc_type='favoritesDel']").click(function () {
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
                        url: ncGlobal.memberRoot + "favorites/store/del",
                        data: params,
                        async: false,
                        success: function (xhr) {
                            if (xhr.code == 200) {
                                window.location.href = ncGlobal.memberRoot + "favorites/store";
                            } else {
                                Nc.alertError(xhr.message);
                            }
                        }
                    });
                }
            });
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