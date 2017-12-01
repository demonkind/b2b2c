/**
 * Created by zxy on 2015-12-10
 */
//操作处理开始
var OperateHandle = function () {
    $("img[data-previewimage]").jqthumb({
        width: 193,
        height: 150,
        after: function (imgObj) {
            imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
        }
    });

    function _bindEvent() {
        if (window.location.hash == "#mobile") {
            $('#tabsTitle li:eq(1) a').tab('show');
        }
        $("#mobileForm").on("nc.qSubmit.success",function(e,result){
            if (result.code == 200) {
                window.location.href = ncGlobal.adminRoot+"site/login#mobile";
            } else {
                Nc.alertError(result.message);
            }
        });

        $("#loginImageForm").on("nc.qSubmit.success",function(e,result){
            if (result.code == 200) {
                window.location.href = ncGlobal.adminRoot+"site/login#loginImage";
            } else {
                Nc.alertError(result.message);
            }
        });

        $("input[data-addloginimage]").fileupload({
            dataType: 'json',
            url: ncGlobal.adminRoot+"image/upload.json",
            done: function (e, data) {
                var $this = $(this), _id =$this.data("addloginimage");
                if (data.result.code == 200) {
                    $('#loginImage'+_id).val(data.result.data.name);
                    $('#previewLoginImage'+_id).attr('src', data.result.data.url);
                    $("#previewLoginImage"+_id).parent("a").attr('href', data.result.data.url);
                    //图片同比例缩放-新增
                    $('#previewLoginImage'+_id).jqthumb({
                        width: 193,
                        height: 150,
                        after: function (imgObj) {
                            imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                        }
                    });
                    $("#previewLink"+_id).find(".jqthumb").remove();
                } else {
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: data.result.message
                    })
                }
            }
        });

        $("a[data-delloginimage]").click(function(){
            var $this = $(this), _id =$this.data("delloginimage");
            $("#loginImage"+_id).val("");
            $("#previewLoginImage"+_id).attr('src', ncGlobal.publicRoot +"img/login_img.jpg");
            $("#previewLoginImage"+_id).parent("a").attr('href', ncGlobal.publicRoot +"img/login_img.jpg");
            //图片同比例缩放
            $('#previewLoginImage'+_id).jqthumb({
                width: 193,
                height: 150,
                after: function (imgObj) {
                    imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                }
            });
            //删除图片展示代码
            $("#previewLink"+_id).find(".jqthumb").remove();
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