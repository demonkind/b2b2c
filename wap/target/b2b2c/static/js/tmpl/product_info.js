$(function() {
    var goods_id = getQueryString("goods_id");
    $.ajax({
        url: ApiUrl + "/index.php?act=goods&op=goods_body",
        data: {goods_id: goods_id},
        type: "get",
        success: function(result) {
            $(".fixed-tab-pannel").html(result);
        }
    });

    $('#goodsDetail').click(function(){
        window.location.href = WapSiteUrl+'/product/productDetail';
    });
    $('#goodsBody').click(function(){
        window.location.href = WapSiteUrl+'/product/productInfo';
    });
    $('#goodsEvaluation').click(function(){
        window.location.href = WapSiteUrl+'/product/productEvalList';
    });
});