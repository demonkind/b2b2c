var adminRefundInfo = function () {
    var __postFlat = true;
    var buyerImageArray = ncGlobal.refundBuyerImageList.split(",");
    function _buildImageList (){
        if (ncGlobal.refundBuyerImageList == ""){
            return;
        }
        var a = buyerImageArray.map(function (n) {
            var _imageUrl = ncGlobal.uploadRoot + n;
            return '<a href="' + _imageUrl + '" data-lightbox="image-1" title="买家上传凭证" ><img class="show_image" src="' + _imageUrl + '" ></a >';
        }).join(",");
        ////
        $("#imageList").append(a).find("img").jqthumb({
            width: 60,
            height: 60,
            after: function (imgObj) {
                imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
            }
        });
    }

    function _bindEvents(){
        $(document).ajaxError(function () {

            __postFlat = true;
        });
        //按钮提交事件
        $("#formSubmit").click(function (event) {
            event.preventDefault();
            var $adminMessage = $("#adminMessage");
            if ($.trim($adminMessage.val()) == "") {
                $.ncAlert({
                    content: '<div class="alert alert-danger m-b-0"><i class="fa fa-info-circle m-r-10"></i><h4>' + "请填写操作备注" + '</h4></div>',
                    autoCloseTime: 3
                });
                return;
            }

            $.ncConfirm({
                url: ncGlobal.adminRoot + "return/save",
                data: {
                    adminMessage: $.trim($adminMessage.val()),
                    refundId: ncGlobal.refundId
                },
                content: "如有在线退款，请确认成功后再确认提交。提交后将不能恢复，确认吗？",
                alertTitle:"平台退款审核",
                callBack:function(xhr){
                    Nc.go(xhr.url);
                }
            });

        });
        /**
         * 支付宝退款按钮点击后锁定
         */
        $("#alipayRefundBtn").one("click",function(){
            //console.log("支付宝退款按钮点击后变灰");
            var $this = $(this)
                ;
            $this.removeClass("btn-warning").addClass("btn-default").attr("style","cursor: not-allowed;")
        })
    }
    ////
    return {
        init: function () {
            _buildImageList();
            _bindEvents();
        }
    }
}();

$(function () {
    adminRefundInfo.init();
})