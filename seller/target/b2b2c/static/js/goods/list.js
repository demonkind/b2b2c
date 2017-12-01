/**
 * Created by shopnc.feng on 2015-12-22.
 */
var list = function() {
	var $el;
    var _getGoods = function(commonId, name, type) {
        $.post(ncGlobal.sellerRoot + "goods/get/sku.json", {commonId: commonId}, function(data){
            if (data.code == 200) {
                $('#skuTable').find('tbody').html('');
                for (var i = 0; i < data.data.length; i++) {
                    var goods = data.data[i];
                    var _html  = "<tr>" +
                        "<td></td>" +
                        "<td class='tl'>"+
						"<div class='table-list-thumb'><img src='" + goods.imageSrc + "' / style='height: 32px;'></div>"+
						"<dl class='goods-sku'><dt><a href='javascript:;'>" + goods.goodsFullSpecs + "</a></dt>"+
						"<dd><span class='m-r-30'>SKU:" + goods.goodsId + "</span>"+
						"<span class='QRcode-block'>二维码:<i class='icon icon-qrcode vm m-l-5'></i><div class='QRcode'><img src='" + goods.goodsQRCode + "'/></div></span>"+
						"</dd></dl></td>" +
                        "<td>" + Nc.priceFormat(goods.goodsPrice) + "</td>" +
                        "<td>" + goods.goodsStorage + "</td>" +
						"<td>" + goods.goodsStorageAlarm + "</td>" +
						"<td>" + goods.goodsSerial + "</td>";
                        if (type == "online") {
                            _html += "<td><a href='" + ncGlobal.sellerRoot + "goods/edit/sku/" + goods.goodsId + "' target='_blank' class='btn btn-xs btn-primary'><i class='icon-edit'></i>编辑</a></td>";
                        } else {
                            _html += "<td><a href='" + ncGlobal.sellerRoot + "goods/offline/edit/sku/" + goods.goodsId + "' target='_blank' class='btn btn-xs btn-primary'><i class='icon-edit'></i>编辑</a></td>";
                        }
                        _html += "</tr>";
                    $('#skuTable').find('tbody').append(_html);

                }
                Nc.layerOpen({
                    title: "查看<span class='spu-name'>“" + name + "”</span>的规格",
                    sizeEnum: "large",
                    content: $("#skuTable"),
                    btn:[]
                });

                $('.table-list-thumb a img').jqthumb({
					width: 32,
					height: 32,
					after: function (imgObj) {
					imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
					}
				});

				
            } else {
                Nc.alertError(data.message);
            }
			
			
        }, "json");
    }

    var _deleteGoods = function(commonId, name) {
        Nc.layerConfirm("是否删除“" + name + "”商品", {
            postUrl: ncGlobal.sellerRoot + "goods/delete.json",
            postData: {
                commonId: commonId
            }
        });
    }
    var _init = function(){
    	$el = {
    		freeShop : $("#freeShop"),
    		freeShopFrom : $("#freeShopForm"),
    		
    		preSale : $("#preSale"),
    		preSaleFrom : $("#preSaleFrom"),
    		
    		freeSale : $("#freeSale"),
    		freeSaleFrom : $("#freeSaleFrom"),
    	}
    	 //绑定日历控件
        $('input.datePiceker').bind("focus", function () {
        	WdatePicker({
        		dateFmt:'yyyy-MM-dd HH:mm:ss',
        		realDateFmt:"yyyy-MM-dd HH-mm-ss",  
        		realTimeFmt:"HH:mm:ss HH-mm-ss", 
        	});
        });
    }
    var bindEvents = function(){
    	//0 元购 表单验证
//    	$("#addForm").validate({
//            submitHandler: function(form) {
//                $(form).ajaxSubmit();
//            },
//            onkeyup: false,
//            rules:{
//                startTime :{
//                    required:true,
//                },
//                endTime : {
//                    required: true
//                },
//                //份数
//                share : {
//                    required: true,
//                    number:true
//                },
//                //限购数量
//                purchase : {
//                    required: true,
//                    digits:true
//                },
//                //返还金额
//                refund : {
//                    required: true,
//                    number:true
//                }
//            },
//            messages:{
//            	startTime :{
//                    required	: '<i class="icon-exclamation-sign"></i>活动开始时间不能为空',
//                },
//                endTime : {
//                    required	: '<i class="icon-exclamation-sign"></i>活动结束时间不能为空'
//                },
//                share : {
//                    required	: '<i class="icon-exclamation-sign"></i>份数不能为空',
//                },
//                purchase : {
//                    required	: '<i class="icon-exclamation-sign"></i>份数不能为空',
//                    digits : "必须为正整数"
//                },
//                refund : {
//                    required	: '<i class="icon-exclamation-sign"></i>返还金额不能为空'
//                }
//            }
//        });
    	
    }
    //0元购
    var _freeShop = function(goodsId,type,gid,commonId){
    	console.log(1);
    	$("#id_freeShopFormGoodActivityId").val(gid);
    	$("#id_freeShopFormCommonId").val(commonId);
    	//清空表单
    	$.post(ncGlobal.sellerRoot + "goodsActivityByGoodsId", {goodsId: commonId}, function(data){
    		if(data.code == 0){
    			data= data.data;
				$($el.freeShopFrom)[0].reset();
    			if(data){
    				$("#id_freeShopFormGoodActivityId").val(gid);
    				$("#id_freeShopFormCommonId").val(commonId);
    				$("#id_freeShopFormActivityId").val(data.activityId);
    				if(data.startTime.toString().indexOf('-')<=0 ){
    					data.startTime = formatterDateTime(data.startTime);
    				}
            		$el.freeShopFrom.find("input[name=startTime]").val(data.startTime);
            		$el.freeShopFrom.find("input[name=endTime]").val(data.endTime);
            		$el.freeShopFrom.find("input[name=returnAmount]").val(data.returnAmount);
            		$el.freeShopFrom.find("input[name=weight]").val(data.weight);
            		$el.freeShopFrom.find("input[name=maxNum]").val(data.maxNum);
            		$el.freeShopFrom.find("textarea").val(data.description);
            		$el.freeShopFrom.find("select[name='cartType'] option[value='"+data.cartType+"']").attr("selected",true).prop("selected",true);
            		$el.freeShopFrom.find("select[name='sendKCodeType'] option[value='"+data.sendKCodeType+"']").attr("selected",true).prop("selected",true);
    			}
    		}
    	});
    	if(type == 3){
    		$el.freeShopFrom.find("input").attr("disabled",true).attr("readonly",true)
    		$el.freeShopFrom.find("textarea").attr("disabled",true).attr("readonly",true)
    		$el.freeShopFrom.find("select").attr("disabled",true).attr("readonly",true);
    	}else{
    		$el.freeShopFrom.find("input").attr("disabled",false).attr("readonly",false)
    		$el.freeShopFrom.find("textarea").attr("disabled",false).attr("readonly",false)
    		$el.freeShopFrom.find("select").attr("disabled",false).attr("readonly",false);
    	}
		Nc.layerOpen({
			title: "设置活动",
			content: $el.freeShop,
			$form: $el.freeShopFrom,
            async: true,
            objSerializeType:false
		});
		//阻止表单提交?
		if(type ==3 ){
    		$(".layui-layer-btn0").hide();
		}else{
			$(".layui-layer-btn0").show();
		}
	}
    //预售
    var _preSale = function(goodsId,type,gid,commonId,event){
    	//清空表单
    	$("#id_preSaleFormGoodActivityId").val(gid);
    	$("#id_preSaleFormCommonId").val(commonId);
    	$.post(ncGlobal.sellerRoot + "goodsActivityByGoodsId", {goodsId: commonId}, function(data){
    		if(data.code == 0){
    			data= data.data;
    			$($el.preSaleFrom)[0].reset();
    			if(data){
    				$("#id_preSaleFormActivityId").val(data.activityId);
    				$("#id_preSaleFormCommonId").val(commonId);
    				$("#id_preSaleFormGoodActivityId").val(gid);
    				if(data.startTime.toString().indexOf('-')<=0 ){
    					data.startTime = formatterDateTime(data.startTime);
    				}
            		$el.preSaleFrom.find("input[name=startTime]").val(data.startTime);
            		$el.preSaleFrom.find("input[name=endTime]").val(data.endTime);
            		$el.preSaleFrom.find("input[name=weight]").val(data.weight);
            		$el.preSaleFrom.find("input[name=maxNum]").val(data.maxNum);
            		$el.preSaleFrom.find("textarea").val(data.description);
    			}
    		}
    	});
    	if(type == 3){
    		$el.preSaleFrom.find("input").attr("disabled",true).attr("readonly",true)
    		$el.preSaleFrom.find("textarea").attr("disabled",true).attr("readonly",true)
    	}else{
    		$el.preSaleFrom.find("input").attr("disabled",false).attr("readonly",false)
    		$el.preSaleFrom.find("textarea").attr("disabled",false).attr("readonly",false)
    	}
		Nc.layerOpen({
			title: "设置活动",
			content: $el.preSale,
			$form: $el.preSaleFrom,
            async: true,
            objSerializeType:false
		});
		//阻止表单提交?
		if(type ==3 ){
    		$(".layui-layer-btn0").hide();
		}else{
			$(".layui-layer-btn0").show();
		}
	}
    //预售+0元购
    var _freeSale = function(goodsId,type,gid,commonId,event){
    	$("#id_freeSaleFormGoodActivityId").val(gid);
    	$("#id_freeSaleFormCommonId").val(commonId);
    	//清空表单
    	$.post(ncGlobal.sellerRoot + "goodsActivityByGoodsId", {goodsId: commonId}, function(data){
    		if(data.code == 0){
    			data= data.data;
    			$($el.freeSaleFrom)[0].reset();
    			if(data){
    				$("#id_freeSaleFormCommonId").val(commonId);
    				$("#id_freeSaleFormGoodActivityId").val(gid);
    				$("#id_freeSaleFormActivityId").val(data.activityId);
    				if(data.startTime.toString().indexOf('-')<=0 ){
    					data.startTime = formatterDateTime(data.startTime);
    				}
            		$el.freeSaleFrom.find("input[name=startTime]").val(data.startTime);
            		$el.freeSaleFrom.find("input[name=endTime]").val(data.endTime);
            		$el.freeSaleFrom.find("input[name=returnAmount]").val(data.returnAmount);
            		$el.freeSaleFrom.find("input[name=weight]").val(data.weight);
            		$el.freeSaleFrom.find("input[name=maxNum]").val(data.maxNum);
            		$el.freeSaleFrom.find("textarea").val(data.description);
            		$el.freeSaleFrom.find("select[name='cartType'] option[value='"+data.cartType+"']").attr("selected",true).prop("selected",true);
            		$el.freeSaleFrom.find("select[name='sendKCodeType'] option[value='"+data.sendKCodeType+"']").attr("selected",true).prop("selected",true);
    			}
    		}
    	});
    	if(type == 3){
    		$el.freeSaleFrom.find("input").attr("disabled",true).attr("readonly",true)
    		$el.freeSaleFrom.find("textarea").attr("disabled",true).attr("readonly",true)
    		$el.freeSaleFrom.find("select").attr("disabled",true).attr("readonly",true);
    	}else{
    		$el.freeSaleFrom.find("input").attr("disabled",false).attr("readonly",false)
    		$el.freeSaleFrom.find("textarea").attr("disabled",false).attr("readonly",false)
    		$el.freeSaleFrom.find("select").attr("disabled",false).attr("readonly",false);
    	}
		Nc.layerOpen({
			title: "设置活动",
			content: $el.freeSale,
			$form: $el.freeSaleFrom,
            async: true,
            objSerializeType:false
		});
		//阻止表单提交?
		if(type ==3 ){
    		$(".layui-layer-btn0").hide();
		}else{
			$(".layui-layer-btn0").show();
		}
	}
    return {
        getGoods : _getGoods,
        deleteGoods : _deleteGoods,
        freeShop :_freeShop,
        preSale : _preSale,
        freeSale : _freeSale,
        init : function(){
        	_init();
        	bindEvents();
        }
        
    }
}();
$(function(){
	list.init();
});
var formatterDateTime = function(time){
	var date = new Date(time);
    var datetime = date.getFullYear()
            + "-"// "年"
            + ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0"
                    + (date.getMonth() + 1))
            + "-"// "月"
            + (date.getDate() < 10 ? "0" + date.getDate() : date
                    .getDate())
            + " "
            + (date.getHours() < 10 ? "0" + date.getHours() : date
                    .getHours())
            + ":"
            + (date.getMinutes() < 10 ? "0" + date.getMinutes() : date
                    .getMinutes())
            + ":"
            + (date.getSeconds() < 10 ? "0" + date.getSeconds() : date
                    .getSeconds());
    return datetime;
}