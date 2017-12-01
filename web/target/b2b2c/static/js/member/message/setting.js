/**
 * Created by shopnc.feng on 2016-02-19.
 */
var setting = function() {
    var __postFlat = true;
    var _bindEvents = function() {
        $('div[data-input-name="isReceive"]').ncSwitch();
    }
    var _submit = function() {
        $(document).ajaxError(function () {
            //Nc.alertError("连接超时");
            __postFlat = true;
        });

        if (__postFlat == false) {
            return;
        }
        __postFlat = false;
        $.post(
            ncGlobal.memberRoot + "/message/setting/save.json",
            function() {
                return $("#settingForm").serializeObject();
            }(),
            function (data) {
                if (data.code == 200) {
                    location.reload();
                } else {
                    Nc.alertError(data.message);
                }
            }
        ).always(function () {
            __postFlat = true;
        })
    }
    return {
        init : function() {
            _bindEvents();
        },
        submit : _submit
    }
}();

$(function(){
    setting.init();
})