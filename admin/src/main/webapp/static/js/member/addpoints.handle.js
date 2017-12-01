/**
 * Created by zxy on 2015-12-01
 */
//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //搜索会员
        $('#memberName').blur(function () {
            $("#memberInfo").html("");
            $("#memberId").val("");
            var memberName = $(this).val();
            $.getJSON(ncGlobal.adminRoot + "points/memberinfo",{"memberName":memberName},function(data){
                if (data.code == 200) {
                    $("#memberInfo").html(data.data.memberName+"，当前积分数为"+data.data.memberPoints);
                    $("#memberId").val(data.data.memberId);
                }else{
                    $("#memberInfo").html(data.message);
                    $("#memberId").val(0);
                }
                $("#memberInfoModule").show();
            });
        });


        //
        $("#addForm").on("nc.qSubmit.success",function(){
            window.location.reload();
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent
    }
}();
//操作处理结束

$(function () {
    $("#memberName").val("");
    $("#memberId").val("");
    $("#operateType").val(1);
    $("#points").val("");
    $("#description").val("");


    //页面绑定事件
    OperateHandle.bindEvent();
});