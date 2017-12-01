var submiting = false;

//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //绑定日历控件
        $('#birthday').bind("focus", function () {
            WdatePicker();
        });

        $("#submitBtn").click(function(){
            //加载中
            if (submiting == true) {
                return false;
            }
            submiting = true;
            if($("#infoForm").valid()){
                var params = {"memberName":$("[name='memberName']").val(),
                              "trueName":$("#trueName").val(),
                              "memberSex":$("input[name='memberSex']:checked").val(),
                              "birthday":$("#birthday").val(),
                              "memberAddress_1":$("[name='memberAddress_1']").val(),
                              "memberAddress_2":$("[name='memberAddress_2']").val(),
                              "addressAreaId":$("[name='addressAreaId']").val(),
                              "addressAreaInfo":$("[name='addressAreaInfo']").val(),
                              "memberQQ":$("#memberQQ").val(),
                              "memberWW":$("#memberWW").val()};
                $.ajax({
                    type : "post",
                    url : ncGlobal.memberRoot + "information",
                    data : params,
                    async : false,
                    success : function(xhr){
                        if (xhr.code == 200) {
                            Nc.alertSucceed("编辑成功",{end:function(){
                                window.location.href = ncGlobal.memberRoot+"information";
                            }});
                        }else{
                            Nc.alertError(xhr.message);
                        }
                        submiting = false;
                    }
                });
            }else{
                submiting = false;
            }
            return false;
        });

        jQuery.validator.addMethod("nccheckname", function(value, element) {
            return this.optional(element) || (/^[A-Za-z0-9\u4e00-\u9fa5_-]+$/i.test(value) && !/^\d+$/.test(value));
        }, "Letters only please");

        $("#infoForm").validate({
            onkeyup: false,
            rules : {
                memberName : {
                    required : true,
                    rangelength : [6,20],
                    nccheckname : true,
                    remote   : {
                        url : ncGlobal.memberRoot + 'information/membernameexist',
                        type:'get',
                        data:{
                            memberName : function(){
                                return $('[name="memberName"]').val();
                            }
                        }
                    }
                }
            },
            messages : {
                memberName : {
                    required : "用户名不能为空",
                    rangelength : "用户名必须在6-20个字符之间",
                    nccheckname: "请使用6-20个中、英文、数字及“-”符号，且不能全为数字",
                    remote	 : "用户名已存在"
                }
            }
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

    //地址选择
    $("#divAddress").NcArea({
        hiddenInput: [
            {name: "addressAreaId", value: "0"},
            {name: "addressAreaInfo", value: ""}
        ],
        url:ncGlobal.webRoot + 'area/list.json/',
        dataHiddenName:"memberAddress_"
    });
});