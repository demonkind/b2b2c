var submiting = false;

//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        $("[nc_type='delBtn']").click(function(){
            var data_str = $(this).attr('data-param');
            if(!data_str){
                Nc.alertError("参数错误");
                return false;
            }
            eval("data_str = "+data_str);

            //加载中
            if (submiting == true) {
                return false;
            }
            submiting = true;
            Nc.layerConfirm("是否删除该记录？", {
                    yes: function () {
                        var params = {"rechargeId": data_str.rechargeId};
                        $.ajax({
                            type: "post",
                            url: ncGlobal.memberRoot + "predeposit/recharge/del",
                            data: params,
                            async: false,
                            success: function (xhr) {
                                if (xhr.code == 200) {
                                    window.location.href = ncGlobal.memberRoot + "predeposit/recharge/list";
                                } else {
                                    Nc.alertError(xhr.message);
                                }
                                submiting = false;
                            }
                        });
                    },
                    cancel:function(){
                        submiting = false;
                    }
                }
            );
            return false;
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