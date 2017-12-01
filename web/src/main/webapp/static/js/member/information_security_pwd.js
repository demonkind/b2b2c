var submiting = false;

//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        $('#password_form').validate({
            rules : {
                password : {
                    required   : true,
                    minlength  : 6,
                    maxlength  : 20
                },
                confirm_password : {
                    required   : true,
                    equalTo    : '#password'
                }
            },
            messages : {
                password  : {
                    required  : '<i class="icon-exclamation-sign"></i>请正确输入密码',
                    minlength : '<i class="icon-exclamation-sign"></i>请正确输入密码',
                    maxlength : '<i class="icon-exclamation-sign"></i>请正确输入密码'
                },
                confirm_password : {
                    required   : '<i class="icon-exclamation-sign"></i>请正确输入密码',
                    equalTo    : '<i class="icon-exclamation-sign"></i>两次密码输入不一致'
                }
            }
        });

        $('#submitButton').click(function () {
            //加载中
            if (submiting == true) {
                return false;
            }
            submiting = true;
            if($("#password_form").valid()){
                var params = {"memberPwd":$('#password').val(),"repeatMemberPwd":$('#confirm_password').val()};
                $.ajax({
                    type : "post",
                    url : ncGlobal.memberRoot + "security/pwd",
                    data : params,
                    async : false,
                    success : function(xhr){
                        if (xhr.code == 200) {
                            Nc.alertSucceed("密码修改成功",{end:function(){
                                window.location.href = ncGlobal.memberRoot+"security";
                            }});
                        }else{
                            if (xhr.url) {
                                window.location.href = xhr.url;
                            }else{
                                Nc.alertError(xhr.message);
                            }
                        }
                        submiting = false;
                    }
                });
            }else{
                submiting = false;
            }
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