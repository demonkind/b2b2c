/**
 * Created by shopnc.feng on 2015-12-01.
 */

var site = function () {
    /**
     * 绑定事件
     * @private
     */
    function _bindEvent() {
        //调用checkbox方法
        $("#goodsVerify").bootstrapSwitch('state', goodsSite.goodsVerify);
    }

    return {
        init: function () {
            _bindEvent();
        }
    }

}()


$(function () {
    site.init();
});