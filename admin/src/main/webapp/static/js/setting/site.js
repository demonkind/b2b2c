/**
 * Created by cj on 2015/11/23.
 */


var site = function () {


    /**
     * 绑定事件
     * @private
     */
    function _bindEvent() {
        $("#siteForm").on("nc.qSubmit.success",function(e,result){
            if (result.code == 200) {
                window.location.href = ncGlobal.adminRoot+"site/edit#baseSet";
            } else {
                Nc.alertError(result.message);
            }
        });

        $("#siteForm1").on("nc.qSubmit.success",function(e,result){
            if (result.code == 200) {
                window.location.href = ncGlobal.adminRoot+"site/edit#otherSet";
            } else {
                Nc.alertError(result.message);
            }
        });
        //调用checkbox方法
        $("[name='siteState']").bootstrapSwitch('state', siteGlobal.siteState);


        /**
         * 上传站点LOGO
         */
        $('#previewSiteLogo').jqthumb({
            width: 200,
            height: 60,
            after: function (imgObj) {
                imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
            }
        });
        $('#previewSellerLogo').jqthumb({
            width: 220,
            height: 50,
            after: function (imgObj) {
                imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
            }
        });
        $("#addSiteLogo").fileupload({
            dataType: 'json',
            url: ncGlobal.adminRoot + "image/upload.json",
            done: function (e, data) {
                if (data.result.code == 200) {
                    $('#siteLogo').val(data.result.data.name);
                    $('#previewSiteLogo').attr('src', data.result.data.url);
                    //图片同比例缩放-新增
                    $('#previewSiteLogo').jqthumb({
                        width: 200,
                        height: 60,
                        after: function (imgObj) {
                            imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                        }
                    });
                } else {
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: data.result.message
                    })
                }
            }
        });
        /**
         * 上传商家中心LOGO
         */
        $("#addSellerLogo").fileupload({
            dataType: 'json',
            url: ncGlobal.adminRoot + "image/upload.json",
            done: function (e, data) {
                if (data.result.code == 200) {
                    $('#sellerLogo').val(data.result.data.name);
                    $('#previewSellerLogo').attr('src', data.result.data.url);
                    //图片同比例缩放-新增
                    $('#previewSellerLogo').jqthumb({
                        width: 220,
                        height: 50,
                        after: function (imgObj) {
                            imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                        }
                    });
                } else {
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: data.result.message
                    })
                }
            }
        });
    }

    return {
        init: function () {
            _bindEvent();
        }
    }
}()
$(document).ready(function () {
    site.init();
});
