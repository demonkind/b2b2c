/**
 * Created by cj on 2016/2/3.
 */

var refundInfo = function () {
    var buyerImageArray = !Nc.isEmpty(ncGlobal.refundBuyerImageList) ? ncGlobal.refundBuyerImageList.split(",") : [];

    /////
    function _buildElement() {
        var a = buyerImageArray.map(function (n) {
            var _imageUrl = ncGlobal.uploadRoot + n;
            return '<li ><a href="' + _imageUrl + '" data-lightbox="image-1" title="买家上传凭证" ><img class="show_image" src="' + _imageUrl + '" ></a ></li >';
        }).join(",")
        $("#imageList").append(a);
    }

    //////
    return {
        init: function () {
            _buildElement();
        }
    }
}();

var returnShip = function () {
    var __postFlat = true;


    function _verify() {

        var a = $('#shipForm');

        a && a.validate({
            errorPlacement: function (error, element) {
                error.appendTo(element.parentsUntil('dl').find('span.error'));
            },
            rules: {
                shipId: {
                    required: true
                },
                shipSn: {
                    required: true
                }
            },
            messages: {
                shipId: {
                    required: '<i class="icon-exclamation-sign"></i>请选择物流'
                },
                shipSn: {
                    required: '<i class="icon-exclamation-sign"></i>请填写物流单号'
                }
            }
        });
    }

    //
    function _postData() {

        if (__postFlat == false)
            return
        __postFlat ==false;
        var b = $('#shipForm').serializeObject();
        b.refundId = ncGlobal.refundId;
        $.post(
            ncGlobal.webRoot + "member/return/saveship",
            b,
            function (xhr) {
                console.log("数据返回是", xhr);
                if (xhr.code == 200) {
                    Nc.alertSucceed(xhr.message,{
                        end: function () {
                            Nc.go(xhr.url);
                        }
                    })
                }else{
                    Nc.alertError(xhr.message);
                }
            },
            "json"
        )
    }
    function _bindEvents (){
        $(document).ajaxError(function () {
            //Nc.alertError("连接超时");
            __postFlat = true;
        });
        //
        $("#shipSubmitBtn").click(function () {
            if ($("#shipForm").valid()){
                _postData();
            }
        })

    }
    return {
        init: function () {
            _verify();
            _bindEvents();
        }
    }
}();

$(function () {
    refundInfo.init();
    returnShip.init();
})