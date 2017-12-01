/**
 * Created by cj on 2016/2/3.
 */

var refundInfo = function() {
    var buyerImageArray = !Nc.isEmpty(ncGlobal.refundBuyerImageList) ?ncGlobal.refundBuyerImageList.split(",") :[];

    /////
    function _buildElement(){
        var a  = buyerImageArray.map(function(n) {
            var _imageUrl = ncGlobal.uploadRoot + n;
            return '<li ><a href="'+_imageUrl+'" data-lightbox="image-1" title="买家上传凭证" ><img class="show_image" src="'+_imageUrl+'" ></a ></li >';
        }).join(",")
        $("#imageList").append(a);
    }
    //////
    return {
        init:function() {
            _buildElement();

        }
    }
}();

$(function() {
    refundInfo.init();
})