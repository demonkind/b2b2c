var key = getCookie('key');
var password,rcb_pay,pd_pay,payment_code;
 // 现在支付方式
 function toPay(pay_sn,act,op,payId,amout) {
	// 从下到上动态显示隐藏内容
     $.animationUp({valve:'',scroll:''});
     
     // 需要支付金额
    //$('#onlineTotal').html(result.datas.pay_info.pay_amount);
     $('#onlineTotal').html(amout); 	
     payment_code = 'alipay';
     
     $(".nctouch-bottom-mask-bg").css("display","block")
     $(".nctouch-bottom-mask-block").css("display","block");
     
     $('#toPay').click(function(){            	
         if (payment_code == '') {
             $.sDialog({
                 skin:"red",
                 content:'请选择支付方式',
                 okBtn:false,
                 cancelBtn:false
             });
             return false;
         }
         goToPayment(pay_sn, payId);
     });
     
	 // TODO comment by yfb 暂时只有支付宝支付，所以其他的全部注释掉 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//     $.ajax({
//         type:'post',
//         url:ApiUrl+'/index.php?act='+act+'&op='+op,
//         data:{
//             key:key,
//             pay_sn:pay_sn
//             },
//         dataType:'json',
//         success: function(result){
//             checkLogin(result.login);
//             if (result.code != 200) {
//                 $.sDialog({
//                     skin:"red",
//                     content:result.datas.error,
//                     okBtn:false,
//                     cancelBtn:false
//                 });
//                 return false;
//             }
//             // 从下到上动态显示隐藏内容
//             $.animationUp({valve:'',scroll:''});
//             
//             // 需要支付金额
//             $('#onlineTotal').html(result.datas.pay_info.pay_amount);
//             
//             // 是否设置支付密码
//             if (!result.datas.pay_info.member_paypwd) {
//                 $('#wrapperPaymentPassword').find('.input-box-help').show();
//             }
//             
//             // 支付密码标记
//             var _use_password = false;
//             if (parseFloat(result.datas.pay_info.payed_amount) <= 0) {
//                 if (parseFloat(result.datas.pay_info.member_available_pd) == 0 && parseFloat(result.datas.pay_info.member_available_rcb) == 0) {
//                     $('#internalPay').hide();
//                 } else {
//                     $('#internalPay').show();
//                     // 充值卡
//                     if (parseFloat(result.datas.pay_info.member_available_rcb) != 0) {
//                         $('#wrapperUseRCBpay').show();
//                         $('#availableRcBalance').html(parseFloat(result.datas.pay_info.member_available_rcb).toFixed(2));
//                     } else {
//                         $('#wrapperUseRCBpay').hide();
//                     }
//                     
//                     // 预存款
//                     if (parseFloat(result.datas.pay_info.member_available_pd) != 0) {
//                         $('#wrapperUsePDpy').show();
//                         $('#availablePredeposit').html(parseFloat(result.datas.pay_info.member_available_pd).toFixed(2));
//                     } else {
//                         $('#wrapperUsePDpy').hide();
//                     }
//                 }
//             } else {
//                 $('#internalPay').hide();
//             }
//             
//             password = '';
//             $('#paymentPassword').on('change', function(){
//                 password = $(this).val();
//             });
//
//             rcb_pay = 0;
//             $('#useRCBpay').click(function(){
//                 if ($(this).prop('checked')) {
//                     _use_password = true;
//                     $('#wrapperPaymentPassword').show();
//                     rcb_pay = 1;
//                 } else {
//                     if (pd_pay == 1) {
//                         _use_password = true;
//                         $('#wrapperPaymentPassword').show();
//                     } else {
//                         _use_password = false;
//                         $('#wrapperPaymentPassword').hide();
//                     }
//                     rcb_pay = 0;
//                 }
//             });
//
//             pd_pay = 0;
//             $('#usePDpy').click(function(){
//                 if ($(this).prop('checked')) {
//                     _use_password = true;
//                     $('#wrapperPaymentPassword').show();
//                     pd_pay = 1;
//                 } else {
//                     if (rcb_pay == 1) {
//                         _use_password = true;
//                         $('#wrapperPaymentPassword').show();
//                     } else {
//                         _use_password = false;
//                         $('#wrapperPaymentPassword').hide();
//                     }
//                     pd_pay = 0;
//                 }
//             });
//
//             payment_code = '';
//             if (!$.isEmptyObject(result.datas.pay_info.payment_list)) {
//                 var readytoWXPay = false;
//                 var readytoAliPay = false;
//                 var m = navigator.userAgent.match(/MicroMessenger\/(\d+)\./);
//                 if (parseInt(m && m[1] || 0) >= 5) {
//                     // 微信内浏览器
//                     readytoWXPay = true;
//                 } else {
//                     readytoAliPay = true;
//                 }
//                 for (var i=0; i<result.datas.pay_info.payment_list.length; i++) {
//                     var _payment_code = result.datas.pay_info.payment_list[i].payment_code;
//                     if (_payment_code == 'alipay' && readytoAliPay) {
//                         $('#'+ _payment_code).parents('label').show();
//                         if (payment_code == '') {
//                             payment_code = _payment_code;
//                             $('#'+_payment_code).attr('checked', true).parents('label').addClass('checked');
//                         }
//                     }
//                     if (_payment_code == 'wxpay_jsapi' && readytoWXPay) {
//                         $('#'+ _payment_code).parents('label').show();
//                         if (payment_code == '') {
//                             payment_code = _payment_code;
//                             $('#'+_payment_code).attr('checked', true).parents('label').addClass('checked');
//                         }
//                     }
//                 }
//             }
//
//             $('#alipay').click(function(){
//                 payment_code = 'alipay';
//             });
//             
//             $('#wxpay_jsapi').click(function(){
//                 payment_code = 'wxpay_jsapi';
//             });
//
//             $('#toPay').click(function(){            	
//                 if (payment_code == '') {
//                     $.sDialog({
//                         skin:"red",
//                         content:'请选择支付方式',
//                         okBtn:false,
//                         cancelBtn:false
//                     });
//                     return false;
//                 }
//                 if (_use_password) {
//                     // 验证支付密码是否填写
//                     if (password == '') {
//                         $.sDialog({
//                             skin:"red",
//                             content:'请填写支付密码',
//                             okBtn:false,
//                             cancelBtn:false
//                         });
//                         return false;
//                     }
//                     // 验证支付密码是否正确
//                     $.ajax({
//                         type:'post',
//                         url:ApiUrl+'/index.php?act=member_buy&op=check_pd_pwd',
//                         dataType:'json',
//                         data:{key:key,password:password},
//                         success:function(result){
//                             if (result.datas.error) {
//                                 $.sDialog({
//                                     skin:"red",
//                                     content:result.datas.error,
//                                     okBtn:false,
//                                     cancelBtn:false
//                                 });
//                                 return false;
//                             }
//                             goToPayment(pay_sn,act == 'member_buy' ? 'pay_new' : 'vr_pay_new', payId);
//                         }
//                     });
//                 } else {
//                	 goToPayment(pay_sn,act == 'member_buy' ? 'pay_new' : 'vr_pay_new', payId);
//                 }
//             });
//         }
//     });
  // TODO comment by yfb 暂时只有支付宝支付，所以其他的全部注释掉↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
 }

 function goToPayment(pay_sn,payId) {
	 $.ajax({
         type:'post',
         url:WapSiteUrl+'/buy/pay/pay',
         data:{
             'key':key,
             'payId':payId,
             'pay_sn':pay_sn,
             'payPwd':password,
             'rcb_pay':rcb_pay,
             'pd_pay':pd_pay,
             'paymentCode':payment_code
             },
         dataType:'json',
         success: function(result){
             if (result.code != 200) {
                 $.sDialog({
                     skin:"red",
                     content:result.message,
                     okBtn:false,
                     cancelBtn:false
                 });
                 return false;
             }
             if (result.url == '') {
            	 $.sDialog({
                     skin:"red",
                     content:"支付失败。",
                     okBtn:false,
                     cancelBtn:false
                 });
                 return false;
             } else {
            	 location.href = result.url;
             }
         },error:function(){
         	alert("请先登录！");
         }
     });
//     location.href = WapSiteUrl + '/buy/pay/pay?key=' + key + '&payId=' + payId + '&pay_sn=' + pay_sn + '&payPwd=' + password + '&rcb_pay=' + rcb_pay + '&pd_pay=' + pd_pay + '&paymentCode=' + payment_code;
 }
