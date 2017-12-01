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

    /**
     * 物流跟踪
     * @private
     */
    function _buildExpressList(){
        if (Nc.isEmpty(ncGlobal.shipSn)){
            return
        }
        $.ajax({
            type: 'GET',
            url: ncGlobal.sellerRoot + "orders/ship/search/" + ncGlobal.shipSn + "/" + ncGlobal.shipCode,
            dataType: 'json'
        }).success(function (result) {
            if ('200' == result.code) {
                var shipContent = $.parseJSON(result.data);
                if ('200' == shipContent.status) {
                    $('#shipResult').empty();
                    $.each(shipContent.data, function (n, value) {
                        $('#shipResult').append("<li>"+value.time + ' ' + value.context +"</li>");
                    })
                } else {
                    $("#shipResult").html("<li>没有相关物流信息数据</li>");
                }
            } else {
                $("#shipResult").html("<li>没有相关物流信息数据</li>");
            }
        }).error(function() {
            $("#shipResult").html("<li>请求失败</li>");
        });

    }
    /////
    function _buildElement() {
        _buildImageList();
        _buildExpressList();
    }

    ////
    function _bindEvents() {
        $(document).ajaxError(function () {
            //Nc.alertError("连接超时");
            __postFlat = true;
        });
        $("#confirmButton").click(function () {
            if (!$("#postForm").valid()){
                return;
            }

            if ( __postFlat == false) {
                return;
            }
            __postFlat = false;
            $.post(
                ncGlobal.sellerRoot + "return/handle",
                function () {
                    var a = $("#postForm").serializeObject();
                    a.refundId = ncGlobal.refundId;
                    return a;
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