var key = getCookie('key');
// buy_stop2使用变量
var ifcart = getQueryString('ifcart');
if(ifcart==1){
    var cart_id = getQueryString('cart_id');
}else{
    var cart_id = getQueryString("goodsId")+'|'+getQueryString("buynum");
}
var pay_name = 'online';
var invoice_id = 0;
var address_id,vat_hash,offpay_hash,offpay_hash_batch,voucher,pd_pay,password,fcode='',rcb_pay,rpt,payment_code,storeList;
var message = {};
// change_address 使用变量
var freight_hash,city_id,area_id
// 其他变量
var area_info;
var goods_id;
$(function() {
    // 地址列表
    $('#list-address-valve').click(function(){
        $.ajax({
            type:'get',
            url:WapSiteUrl+"/address/list", 
            data:{key:key},
            dataType:'json',
            async:false,
            success:function(result){
                if(result.data.address_list==null){
                    return false;
                }

                checkLogin(result.data.login);
                var data = result.data;
                address_id = data.address_id;
                var html = template.render('list-address-add-list-script', data);
                $("#list-address-add-list-ul").html(html);
               
            }
        });
       
    });
    $.animationLeft({
        valve : '#list-address-valve',
        wrapper : '#list-address-wrapper',
        scroll : '#list-address-scroll'
    });
    
    // 地区选择
    $('#list-address-add-list-ul').on('click', 'li', function(){
        $(this).addClass('selected').siblings().removeClass('selected');
        eval('address_info = ' + $(this).attr('data-param'));
        _init(address_info.address_id);
        $('#list-address-wrapper').find('.header-l > a').click();
        $('#ToBuyStep2').parent().addClass('ok');
    });
    
    // 地址新增
    $.animationLeft({
        valve : '#new-address-valve',
        wrapper : '#new-address-wrapper',
        scroll : ''
    });
    // 支付方式
    $.animationLeft({
        valve : '#select-payment-valve',
        wrapper : '#select-payment-wrapper',
        scroll : ''
    });
    // 地区选择
    $('#new-address-wrapper').on('click', '#varea_info', function(){
        $.areaSelected({
            success : function(data){
                city_id = data.area_id_2 == 0 ? data.area_id_1 : data.area_id_2;
                area_id = data.area_id;
                area_info = data.area_info;
                $('#varea_info').val(data.area_info);
            }
        });
    });

    // 发票
    $.animationLeft({
        valve : '#invoice-valve',
        wrapper : '#invoice-wrapper',
        scroll : ''
    });
    
    
    template.helper('isEmpty', function(o) {
        var b = true;
        $.each(o, function(k, v) {
            b = false;
            return false;
        });
        return b;
    });
    
    template.helper('pf', function(o) {
        return parseFloat(o) || 0;
    });

    template.helper('p2f', function(o) {
        return (parseFloat(o) || 0).toFixed(2);
    });

    var _init = function (address_id) {
        var totals = 0;
        // 购买第一步 提交
        $.ajax({//提交订单信息
            type:'post',
            url:WapSiteUrl+'/buy/buyStep1',
            dataType:'json',
            data:{cart_id:cart_id,ifcart:ifcart, address_id:address_id},
            success:function(result){
            	console.log(result);
                if (result.code != 200) {
                    $.sDialog({
                        skin:"red",
                        content:result.message,
                        okBtn:false,
                        cancelBtn:false
                    });
                    return false;
                }

                checkLogin(result.data.login);
                // 商品数据
                result.data.WapSiteUrl = WapSiteUrl;
                var html = template.render('goods_list', result.data);
                $("#deposit").html(html);
                
                storeList = JSON.stringify(result.data.buyStoreVoList);
                
                //TODO  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ commet by yfb  F码商品 暂未开发          start ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//                if (fcode == '') {
//                    // F码商品
//                    for (var k in result.data.store_cart_list) {
//                        if (result.data.store_cart_list[k].goods_list[0].is_fcode == '1') {
//                            $('#container-fcode').removeClass('hide');
//                            goods_id = result.data.store_cart_list[k].goods_list[0].goods_id;
//                        }
//                        break;
//                    }
//                }
//                // 验证F码
//                $('#container-fcode').find('.submit').click(function(){
//                    fcode = $('#fcode').val();
//                    if (fcode == '') {
//                        $.sDialog({
//                            skin:"red",
//                            content:'请填写F码',
//                            okBtn:false,
//                            cancelBtn:false
//                        });
//                        return false;
//                    }
//                    $.ajax({//提交订单信息
//                        type:'post',
//                        url:ApiUrl+'/index.php?act=member_buy&op=check_fcode',
//                        dataType:'json',
//                        data:{key:key,goods_id:goods_id,fcode:fcode},
//                        success:function(result){
//                            if (result.data.error) {
//                                $.sDialog({
//                                    skin:"red",
//                                    content:result.data.error,
//                                    okBtn:false,
//                                    cancelBtn:false
//                                });
//                                return false;
//                            }
//
//                            $.sDialog({
//                                autoTime:'500',
//                                skin:"green",
//                                content:'验证成功',
//                                okBtn:false,
//                                cancelBtn:false
//                            });
//                            $('#container-fcode').addClass('hide');
//                        }
//                    });
//                });

              //TODO  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ commet by yfb  F码商品 暂未开发          end  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                
                // 默认地区相关
                if ($.isEmptyObject(result.data.addressList)) {
                    $.sDialog({
                        skin:"block",
                        content:'请添加地址',
                        okFn: function() {
                            $('#new-address-valve').click();
                        },
                        cancelFn: function() {
                            history.go(-1);
                        }
                    });
                    return false;
                }
                

             // 发票
                if (typeof(result.data.inv_info.invoiceId) != 'undefined' && result.data.inv_info.invoiceId != null && result.data.inv_info.invoiceId != '') {
                	invoice_id = result.data.inv_info.invoiceId;
                	$('#invContent').html(result.data.inv_info.content);
                }
                // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓   TODO commet by yfb ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 
                
//                vat_hash = result.data.vat_hash;
//                
//                freight_hash = result.data.freight_hash;
//                // 输入地址数据
                insertHtmlAddress(result.data.address_info, result.data.address_api);
//                
//                // 代金券
//                voucher = '';
//                voucher_temp = [];
//                for (var k in result.data.store_cart_list) {
//                    voucher_temp.push([result.data.store_cart_list[k].store_voucher_info.voucher_t_id + '|' + k + '|' + result.data.store_cart_list[k].store_voucher_info.voucher_price]);
//                }
//                voucher = voucher_temp.join(',');

             // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑  TODO commet by yfb ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 
                
                for (var k in result.data.buyStoreVoList) {
                    // 总价
                    $('#storeTotal' + k).html(result.data.buyStoreVoList[k].buyAmount.toFixed(2));
                    totals += parseFloat(result.data.buyStoreVoList[k].buyAmount);
                    // 留言
                    message[k] = '';
                    $('#storeMessage' + k).on('change', function(){
                        message[k] = $(this).val();
                    });
                }

                // 红包
                rcb_pay = 0;
                rpt = '';
                var rptPrice = 0;
                if (!$.isEmptyObject(result.data.rpt_info)) {
                    $('#rptVessel').show();
                    var rpt_info = ((parseFloat(result.data.rpt_info.rpacket_limit) > 0) ? '满' + parseFloat(result.data.rpt_info.rpacket_limit).toFixed(2) + '元，': '') + '优惠' + parseFloat(result.data.rpt_info.rpacket_price).toFixed(2) + '元'
                    $('#rptInfo').html(rpt_info);
                    rcb_pay = 1;
                } else {
                    $('#rptVessel').hide();
                }
                

                
                password = '';

                $('#useRPT').click(function(){
                    if ($(this).prop('checked')) {
                        rpt = result.data.rpt_info.rpacket_t_id+ '|' +parseFloat(result.data.rpt_info.rpacket_price);
                        rptPrice = parseFloat(result.data.rpt_info.rpacket_price);
                        var total_price = totals - rptPrice;
                    } else {
                        rpt = '';
                        var total_price = totals;
                    }
                    if (total_price <= 0) {
                        total_price = 0;
                    }
                    $('#totalPrice').html(total_price.toFixed(2));
                    $('#onlineTotal').html(total_price.toFixed(2));

                });

                // 计算总价
                var total_price = totals - rptPrice;
                if (total_price <= 0) {
                    total_price = 0;
                }
                $("#ToBuyStep2").attr("data-amout",total_price.toFixed(2))

            	$('#onlineTotal').html(total_price.toFixed(2));
                $('#totalPrice').html(total_price.toFixed(2));
            }
        });
    }
    
    rcb_pay = 0;
    pd_pay = 0;
    // 初始化
    _init();

    // 插入地址数据到html
    var insertHtmlAddress = function (address_info, address_api) {
        if($.isEmptyObject(address_info)){
        	$('#ToBuyStep2').parent().removeClass('ok');
        }else{
        	address_id = address_info.addressId;
            $('#true_name').html(address_info.realName);
            $('#mob_phone').html(address_info.mobPhone);
            $('#address').html(address_info.areaInfo + address_info.address);
            area_id = address_info.areaId;
            city_id = address_info.city_id;
        	$('#ToBuyStep2').parent().addClass('ok');
        }
     // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓   TODO commet by yfb 支持不支持线下支付 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//        if (address_api.content) {
//            for (var k in address_api.content) {
//                $('#storeFreight' + k).html(parseFloat(address_api.content[k]).toFixed(2));
//            }
//        }
//        offpay_hash = address_api.offpay_hash;
//        offpay_hash_batch = address_api.offpay_hash_batch;
//        if (address_api.allow_offpay == 1) {
//            $('#payment-offline').show();
//        }
//        if (!$.isEmptyObject(address_api.no_send_tpl_ids)) {
//            $('#').parent().removeClass('ok');
//            for (var i=0; i<address_api.no_send_tpl_ids.length; i++) {
//                $('.transportId' + address_api.no_send_tpl_ids[i]).show();
//            }
//        } else {
//            $('#ToBuyStep2').parent().addClass('ok');
//        }
     // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓   TODO commet by yfb ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    }
    
    // 支付方式选择
    // 在线支付
    $('#payment-online').click(function(){
        pay_name = 'online';
        $('#select-payment-wrapper').find('.header-l > a').click();
        $('#select-payment-valve').find('.current-con').html('在线支付');
        $(this).addClass('sel').siblings().removeClass('sel');
    })
    // 货到付款
    $('#payment-offline').click(function(){
        pay_name = 'offline';
        $('#select-payment-wrapper').find('.header-l > a').click();
        $('#select-payment-valve').find('.current-con').html('货到付款');
        $(this).addClass('sel').siblings().removeClass('sel');
    })
    
    // 地址保存
    $.sValid.init({
        rules:{
        	vtrue_name:"required",
        	vmob_phone:"required",
        	varea_info:"required",
            vaddress:"required"
        },
        messages:{
        	vtrue_name:"姓名必填！",
        	vmob_phone:"手机号必填！",
        	varea_info:"地区必填！",
            vaddress:"街道必填！"
        },
        callback:function (eId,eMsg,eRules){
            if(eId.length >0){
                var errorHtml = "";
                $.map(eMsg,function (idx,item){
                    errorHtml += "<p>"+idx+"</p>";
                });
                errorTipsShow(errorHtml);
            }else{
                errorTipsHide();
            }
        }  
    });
    $('#add_address_form').find('.btn').click(function(){
        if($.sValid()){
            var param = {};
            param.key = key;
            param.trueName = $('#vtrue_name').val();
            param.mobPhone = $('#vmob_phone').val();
            param.address = $('#vaddress').val();
            param.cityId = city_id;
            param.areaId = area_id;
            param.areaInfo = $('#varea_info').val();
            param.isDefault = 0;
            var myreg = /^[1]+[3,5,8]+\d{9}$/;
            if (!myreg.test($('#vmob_phone').val())) {
				errorTipsShow("手机号格式不正确");
				return false;
			}
            $.ajax({
                type:'post',
                url: WapSiteUrl+"/address/add",  
                data:param,
                dataType:'json',
                success:function(result){
                    if (result.code == 200) {
                        param.address_id = result.data.addressId;
                        _init(param.address_id);
                        $('#new-address-wrapper,#list-address-wrapper').find('.header-l > a').click();
                    }else{
						errorTipsShow(result.message);
					}
                }
            });
        }
    });
    // 发票选择
    $('#invoice-noneed').click(function(){
        $(this).addClass('sel').siblings().removeClass('sel');
        $('#invoice_add,#invoice-list').hide();
        invoice_id = 0;
    });
    $('#invoice-need').click(function(){
        $(this).addClass('sel').siblings().removeClass('sel');
        $('#invoice-list').show();
        // TODO comment by yfb start 发票内容暂时固定↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//        $.ajax({//获取发票内容
//            type:'post',
//            url:WapSiteUrl+'/invoice/list/json',
//            data:{key:key},
//            success:function(result){
//                var data = result.data;
//                var html = '';
//                $.each(data,function(k,v){
//                    html+='<option value="'+v.invoiceId+'">'+v.invoiceContent+'</option>';
//                });
//                $('#inc_content').append(html);
//            }
//        });
        // TODO comment by yfb end ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
        //获取自己保存过的发票列表
        $.ajax({
            type:'get',
            url: WapSiteUrl + '/buy/invoice/list',
            data:{key:key},
            dataType:'json',
            success:function(result){
                checkLogin(result.data.login);
                var html = template.render('invoice-list-script', result.data);
                $('#invoice-list').html(html)
                if (result.data.invoice_list.length > 0) {
                    invoice_id = result.data.invoice_list[0].invoiceId;
                }
                $('.del-invoice').click(function(){
                    var $this = $(this);
                    var inv_id = $(this).attr('inv_id');
                    $.ajax({
                        type:'post',
                        url: WapSiteUrl +'/buy/invoice/del',
                        data:{key:key,inv_id:inv_id},
                        success:function(result){
                            if(result.code == 200){
                                $this.parents('label').remove();
                            }
                            return false;
                        }
                    });
                });
            }
        });
    })
    // 发票类型选择
    $('input[name="inv_title_select"]').click(function(){
        if ($(this).val() == 'person') {
            $('#inv-title-li').hide();
        } else {
            $('#inv-title-li').show();
        }
    });
    $('#invoice-div').on('click', '#invoiceNew', function(){
        invoice_id = 0;
        $('#invoice_add,#invoice-list').show();
    });
    $('#invoice-list').on('click', 'label', function(){
        invoice_id = $(this).find('input').val();
    });
    // 发票添加
    $('#invoice-div').find('.btn-l').click(function(){
        if ($('#invoice-need').hasClass('sel')) {
            if (invoice_id == 0) {
                var param = {};
                param.key = key;
                param.inv_title_select = $('input[name="inv_title_select"]:checked').val();
                param.inv_title = $("input[name=inv_title]").val();
                param.inv_content = $("select[name=inv_content] ").val();
                $('#invoiceId').val(param.inv_content);
                $.ajax({
                    type:'post',
                    url: WapSiteUrl +'/buy/invoice/add',
                    data:param,
                    dataType:'json',
                    success:function(result){
                        if(result.data.inv_id>0){
                            invoice_id = result.data.inv_id;
                        }
                    }
                });
                $('#invContent').html(param.inv_title + ' ' + param.inv_content);
            } else {
                $('#invContent').html($('#inv_'+invoice_id).html());
            }
        } else {
            $('#invContent').html('不需要发票');
        }
        $('#invoice-wrapper').find('.header-l > a').click();
    });

    
    // 支付
    $('#ToBuyStep2').click(function(){   
    	  var amout= $(this).attr("data-amout");
        var msg = '';
        for (var k in message) {
            msg += k + '|' + message[k] + ',';
        }
        console.log(message);
        var key=getCookie("key");
        if(cart_id==null||cart_id==""){
        	cart_id=0;
        }
        $.ajax({
            type:'post',
            url:WapSiteUrl+'/buy/save',
            data:{
                'key':key,
                'pay_message':msg,
                'ifcart':ifcart,
                'cart_id':cart_id,
                'address_id':address_id,
                'pay_name':pay_name,
                'invoiceId':invoice_id,
                'storeList':storeList
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
                $("#orderId").val(result.data.ordersPay.payId);
                if (result.data.payment_code == 'offline') {
                    window.location.href = WapSiteUrl + '/tmpl/members/order_list.html';
                } else {
                    delCookie('cart_count');
                    toPay(result.data.pay_sn,'member_buy','pay', result.data.ordersPay.payId,amout);
                }
            },error:function(){
            	alert("请先登录！");
            }
        });
    });
    
   
    
    
    
    /*$('#toPay').click(function(){
    		location.href=WapSiteUrl+'/product/pay/alipay';
    });*/
});