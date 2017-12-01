var submiting = false;

//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //咨询列表切换卡
        $("#consult_tab").find("li").removeClass("current");
        $("#classTab"+$("#currClassId").val()).addClass("current");

        //显示隐藏咨询类型信息
        $("[nc_type='consultClassRadio']").first().attr("checked","checked");
        $("[nc_type='consultClassIntroduce']").hide();
        $("[nc_type='consultClassIntroduce']").first().show();

        $("[nc_type='consultClassRadio']").click(function () {
            $("[nc_type='consultClassIntroduce']").hide();
            $("#consultClassIntroduce"+$(this).val()).show();
        });

        //验证码
        $("#consultCaptchaHide").click(function(){
            $(".code").fadeOut("slow");
        });
        $("#consultCaptcha").focus(function(){
            $(".code").fadeIn("fast");
        });

        $("#consultSubmit").click(function(){
            //加载中
            if (submiting == true) {
                return false;
            }
            submiting = true;
            //获取input对象
            var inputObj = $("#consultForm").serializeObjectByEle();
            if ($('#consultForm').valid()) {
                $.post(ncGlobal.webRoot+"consult/add", inputObj, function (xhr) {
                    if (xhr.code == "200") {
                        Nc.alertSucceed("提交成功",{time:1000,end:function(){
                            window.location.href = ncGlobal.webRoot+"consult?gid="+$("#goodsId").val();
                        }});
                    }else{
                        Nc.alertError(xhr.message);
                        submiting = false;
                    }
                });
            }else{
                submiting = false;
            }
            return false;
        });

        //表单验证
        $('#consultForm').validate({
            errorPlacement: function(error, element){
                $(element).parent("dd").find("[nc_type='error_msg']").html(error);
            },
            onkeyup: false,
            rules : {
                consultContent : {
                    required : true,
                    maxlength : 120
                },
                consultCaptcha: {
                    required : true,
                        remote   : {
                        url : ncGlobal.webRoot+'captcha/check',
                            type:'get',
                            data:{
                                captcha : function(){
                                    return $('#consultCaptcha').val();
                                }
                            },
                            complete: function(data) {
                                if(data.responseText == 'false') {
                                    $('#consultCaptchaImage').attr("src",ncGlobal.webRoot+"captcha/getcaptcha?t="+Math.random());
                                }
                            }
                    }
                }
            },
            messages : {
                consultContent : {
                    required : '咨询内容不能为空',
                    maxlength: '咨询内容不能超过120个字符'
                },
                consultCaptcha: {
                    required : '请填写验证码',
                    remote   : '验证码错误'
                }
            }
        });

        //更换验证码
        $('[nc_type="consultCaptchaChange"]').click(function(){
            changeCaptcha();
        });

        //字符个数动态计算
        $("#consultContent").charCount({
            allowed: 120,
            warning: 10,
            counterContainerID:'consultCharCount',
            firstCounterText:'还可以输入',
            endCounterText:'字',
            errorCounterText:'已经超出'
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent
    }
}();
//操作处理结束

//更换验证码
function changeCaptcha() {
    $('#consultCaptchaImage').attr('src', ncGlobal.webRoot + 'captcha/getcaptcha?t=' + Math.random());
    $('#consultCaptcha').select();
}
/**
 * 小星星
 */
function initRaty() {
    $('.raty').raty({
        path: ncGlobal.publicRoot + "toolkit/jquery.raty/img",
        readOnly: true,
        width: 80,
        hints:['很不满意', '不满意', '一般', '满意', '很满意'],
        score: function () {
            return $(this).attr('data-score');
        }
    });
}
$(function () {
    //页面绑定事件
    OperateHandle.bindEvent();
    initRaty();

});