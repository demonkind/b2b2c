/**
 * Created by shopnc.feng on 2016-02-05.
 */
var setting = function(){

    var _bindEvent = function() {
    }

    var _messageSetting = function(tplCode) {
        $('#tplCode').val(tplCode);
        $.post(ncGlobal.sellerRoot + "message/get/seller/list.json",
            {tplCode:tplCode},
            function(data){
                if (data.code == 200) {
                    if (data.data.isReceive == 1) {
                        $("#isReceive1").click();
                    } else {
                        $("#isReceive0").click();
                    }

                    $('#subAccountUl').html("");
                    for (var i=0; i < data.data.sellerList.length; i++) {
                        var seller = data.data.sellerList[i];
                        if (seller.isStoreOwner == 1) {
                            var _li = "<li><input class='checkbox m-r-5' type='checkbox' name='sellerId' value='" + seller.sellerId + "' readonly disabled checked>" + seller.sellerName + "</li>";
                        } else {
                            var _checked = seller.isReceive == 1 ? "checked" : "";
                            var _li = "<li><input type='checkbox' name='sellerId' value='" + seller.sellerId + "' " + _checked + ">" + seller.sellerName + "</li>";
                        }
                        $('#subAccountUl').append(_li);
                    }
                    Nc.layerOpen({
                        title: "消息接收设置",
                        content: $('#messageSettingModal'),
                        $form: $("#messageSettingForm"),
                        async: true
                    });
                } else {
                    Nc.alertError(data.message);
                }
            },
            "json");
    }

    return {
        init : function() {
            _bindEvent();
        },
        messageSetting : _messageSetting
    }
}();

$(function(){
   setting.init();
});