/**
 * Created by cj on 2016/2/3.
 */

var refundInfo = function () {
    var buyerImageArray = ncGlobal.refundBuyerImageList!="" ?ncGlobal.refundBuyerImageList.split(","):[];
    var __postFlat = true;

    /**
     * 上传凭证图片
     * @private
     */
    function _buildImageList (){
        if (buyerImageArray.length <= 0 ){
            return ;
        }
        var a = buyerImageArray.map(function (n) {
            var _imageUrl = ncGlobal.uploadRoot + n;
            return '<li ><a href="' + _imageUrl + '" data-lightbox="image-1" title="买家上传凭证" ><img class="show_image" src="' + _imageUrl + '" ></a ></li >';
        }).join(",")
        $("#imageList").append(a);
    }
    /////
    function _buildElement() {
        _buildImageList();
    }

    ////
    function _bindEvents() {
        $(document).ajaxError(function () {
            //Nc.alertError("连接超时");
            __postFlat = true;
        });
        $("#confirmButton").click(function () {
            if (!$("#postForm").valid() && __postFlat == true) {
                return;
            }
            __postFlat = false;
            $.post(
                ncGlobal.sellerRoot + "refund/handle",
                function () {
                    return $("#postForm").serializeObject();
                }(),
                function (xhr) {
                    if (xhr.code == 200) {
                        Nc.alertSucceed(xhr.message,{
                            end: function () {
                                Nc.go(xhr.url);
                            }
                        });
                    }else{
                        Nc.alertError(xhr.message);
                    }
                }
            ).always(function () {
                    __postFlat = true;
                })
        });
        $('#postForm').validate({
            errorPlacement: function (error, element) {
                error.appendTo(element.parentsUntil('dl').find('span.error'));
            },
            rules: {
                sellerState: {
                    required: true
                },
                sellerMessage: {
                    required: true
                }
            },
            messages: {
                sellerState: {
                    required: '<i class="icon-exclamation-sign"></i>请选择是否同意'
                },
                sellerMessage: {
                    required: '<i class="icon-exclamation-sign"></i>备注信息不能为空'
                }
            }
        });
    }

    //////
    return {
        init: function () {
            _buildElement();
            _bindEvents();
        }
    }
}();

$(function () {
    refundInfo.init();
})