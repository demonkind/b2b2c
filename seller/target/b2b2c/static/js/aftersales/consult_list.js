/**
 * Created by zxy on 2016-01-14
 */
var submiting = false;

//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //回复咨询
        $("[nc_type='editReply']").on("click",function(){
            //加载中
            if (submiting == true) {
                return false;
            }
            submiting = true;

            var data_str = $(this).attr('data-param');
            if(!data_str){
                submiting = false;
                Nc.alertError("参数错误");
                return false;
            }
            eval("data_str = "+data_str);
            var consultId = data_str.consultId;
            $("#replyForm").find("[name='consultId']").val(consultId);
            $("#replyForm").find("[name='consultContent']").html($("#consultContent"+consultId).html());
            $("#replyForm").find("[name='consultReply']").val($("#replyContent"+consultId).html());
            Nc.layerOpen({
                title: "咨询回复",
                content: $('#replyDialog'),
                $form: $("#replyForm"),
                async: true
            });
            submiting = false;
            return false;
        });

        //删除咨询
        $("[nc_type='delConsult']").on("click",function(){
            //加载中
            if (submiting == true) {
                return false;
            }
            submiting = true;
            var data_str = $(this).attr('data-param');
            if(!data_str){
                submiting = false;
                Nc.alertError("参数错误");
                return false;
            }
            eval("data_str = "+data_str);
            var consultId = data_str.consultId;

            Nc.layerConfirm("是否删除该记录？", {
                    yes: function () {
                        var params = {"consultId": consultId};
                        $.ajax({
                            type: "post",
                            url: ncGlobal.sellerRoot + "consult/del",
                            data: params,
                            async: false,
                            success: function (xhr) {
                                if (xhr.code == 200) {
                                    window.location.href = ncGlobal.sellerRoot + "consult/list/all";
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
            submiting = false;
            return false;
        });

        //批量删除咨询
        $("[nc_type='batchDel']").on("click",function(){
            if($('[nc_type="checkitem"]:checked').length == 0){
                Nc.alertError("请选择需要操作的记录");
                return false;
            }
            //添加失败处理
            $("#listForm").on("nc.ajaxSubmit.success", function(e, errorMessage) {
                window.location.href = ncGlobal.sellerRoot + "consult/list/all";
            });
            Nc.layerConfirm("是否批量删除所选记录？", {
                    yes: function () {
                        $("#listForm").ajaxSubmit();
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