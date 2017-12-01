$(document).ready(function() {

    //回车后登陆
    $(document).keyup(function(event){
      if(event.keyCode ==13){
        $("#submit").trigger("click");
      }
    });

    //更换验证码
    function changeCaptcha() {
        $('#captcha_image').attr('src', ncGlobal.adminRoot + 'captcha/captcha?t=' + Math.random());
        $('#captcha').select();
    }

    //点击验证码图片更换验证码
    $('#captchaImageChange').on('click', function() {
        changeCaptcha();
    });

    //登陆失败后更新验证码
    $('#loginForm').on("nc.qSubmit.error", function() {
        changeCaptcha();
    });
});