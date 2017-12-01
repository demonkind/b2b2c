/**
 * 地址模2块
 */
ncDefine("address", ["nc.eventManger"], function (ncEventsManger) {
	var __postFlat = true;
	/**
	 * 接口相关
	 */
	var URL = {
		//删除地址接口
		del: ncGlobal.webRoot + "member/address/del",
		//添加地址接口
		add: ncGlobal.webRoot + "member/address/add",
		//计算运费价格的接口
		freight: ncGlobal.webRoot + "buy/calc/freight"
	};
	/**
	 * 保存用到的jquery对象
	 */
	var $element = {
		//所有修改按钮的元素
		//buyEdit:$("a[data-buy-edit]"),
		//收货地址列表
		addrList: $("#addrList"),
		//点击修改地址的按钮
		editReciver: $("#editReciver"),
		//新增收货地址
		addReciver: $("#addReciver"),
		//已经选择的收货地址信息
		addrSelectInfo: $("#addrSelectInfo"),
		//保存地址信息按钮
		hideAddrList: $("#hideAddrList"),
		//收货地址隐藏input
		addressId: $("#addressId"),
		addressDialog: $("#addressDialog"),
		form: $("#addressForm")
	};
	/**
	 * 保存用到的class
	 */
	var elClass = {
		//选中地址后的样式
		nccSelectedItem: "ncc-selected-item",
		currentBox: "current_box"
	};
	/**
	 * 保存发送的事件的文本
	 */
	var eventText = {
		//保存收货地址事件
		hideAddrList: "address.save",
		//修改按钮隐藏事件
		buyEditHide: "buy.edit.hide",
		//修改按钮显示事件
		buyEditShow: "buy.edit.show"
	};
	/**
	 * ncData 的key
	 */
	var dataKey = {
		//运费
		freight: "storeFreight"
	}
	/**
	 * 保存的数据
	 */
	var global = {
		//
		buyData: ''
	};
	/**
	 * 清除地址上的已选择样式
	 */
	var _clearLiSelect = function () {
		$element.addrList.find('li').removeClass(elClass.nccSelectedItem);
	};
	/**
	 * 展开收货地址
	 * @param int [show] [0:隐藏收货地址列表，1：显示]editReciver
	 */
	var _openAddrList = function (show) {
		var div = $element.addrList.closest('div.ncc-receipt-info');

		if (show === 1) {
			//console.log("发送隐藏其他的修改按钮事件");
			ncEventsManger.trigger(eventText.buyEditHide);
			ncEventsManger.trigger("lockOrderSubmit");

			//$element.buyEdit.hide();
			$element.addrList.show();
			$element.addrSelectInfo.hide();
			$element.editReciver.hide();
			div.addClass(elClass.currentBox);
			$element.addReciver.show();
		} else {
			//console.log("发送显示其他的修改按钮事件");
			ncEventsManger.trigger(eventText.buyEditShow);
			ncEventsManger.trigger("unlockOrderSubmit");
			$element.addrList.hide();
			$element.addrSelectInfo.show();
			$element.editReciver.show();
			$element.addReciver.hide();
			div.removeClass(elClass.currentBox);
		}

	};
	/**
	 * 手动设置一个地址的选择
	 */
	var _setAddrSel = function (addId) {
		var addrList = $element.addrList.find("input[name='addr']");
		// debugger
		//如果不是空的话就设置radio被选中
		if (addId) {
			if (addId instanceof jQuery) {
				//如果是jqery 对象
				addId.prop('checked', true).trigger('change');
			} else {
				$("#addr_" + addId).prop('checked', true).trigger('change');
				//只是个id数值
			}
		} else {
			//如果没有指定选择就默认第一个选择
			if (addrList.length) {
				addrList.first().prop('checked', true).trigger('change');
			} else {
				//TODO：如果没有了地址列表就弹出新建地址
				AddrAdd._showNewAddressDialog(true);
			}
		}
	};

	/**
	 * 获取运费，开始和保存收货地址的时候会用
	 * @private
	 */
	var _getFreight = function (addrId) {
		//console.log('根据地址id获取运费，地址id:' + addrId);
		var a = $('tbody[data-store-id]');
		global.buyData = {
				addressId: addrId,
				storeList: $.map(a, function (n) {
					var $body = $(n);
					return {
						storeId: $body.data("storeId"),
						goodsList: $.map($body.find("tr[data-goods-id]"), function (nn) {
							return $(nn).data();
						})

					}
				})
			};
		//console.log("正在请求运费信息,锁定修改、提交按钮");
		ncEventsManger.trigger(eventText.buyEditHide);
		ncEventsManger.trigger("lockOrderSubmit");

		$.post(URL.freight, {buyData: JSON.stringify(global.buyData)}, function (data) {
			if (data.code == "200") {
				//console.log("数据是", data.data);
				//放到数据中心,后序交给事件处理
				//console.log("获取运费信息成功,解锁修改、提交按钮");
				ncData.set(dataKey.freight, data.data);
				ncEventsManger.trigger(eventText.buyEditShow);
			} else {
				Nc.alertError(data.messages);
			}
		});
	};

	/**
	 * 事件
	 * @return {[type]} [description]
	 */
	var bindEvnets = function () {
		//地址选择事件
		$element.addrList.on("change", "input[type=radio]", function (e) {
			_clearLiSelect();
			$(this).closest('li').addClass(elClass.nccSelectedItem);
			//修改当前选择的地址的数据
			Addr.curr = $(this);
		});

		//修改收货人信息事件,
		//展开所有已有的收货地址
		$element.editReciver.on("click", function (e) {
			_openAddrList(1);
		});

		//点击保存地址
		//保存后计算价格
		$element.hideAddrList.on("click", function () {
			//console.log("当前选择的数据是", Addr.curr.data());
			//console.log("当前选择的地区id", Addr.curr.val());
			//将选择的地址写到显示地址详情上面去
			if (!Addr.curr.length) {
				Nc.alertError("请选择收货地址");
				return;
			}
			//重写地址区域html
			_setAddrSelectInfoDiv(Addr.curr.data());
			//隐藏地址列表
			_openAddrList(0);
			//修改收货地址id
			$element.addressId.val(Addr.curr.val());
			//计算运费价格什么的
			_getFreight(Addr.curr.val());
		});
	};
	/**
	 * 修改选择地址后的地址显示
	 */
	var _setAddrSelectInfoDiv = function (data) {
		var tmpl = '<ul>' +
			'<li>' +
			'<span class="true-name">{realName}</span>' +
			'<span class="address">{areaInfo} {address}</span>' +
			'<span class="phone"> <i class="icon-mobile-phone"></i>{mobPhone}</span>' +
			'</li>' +
			'</ul>';
		$element.addrSelectInfo.empty().append(tmpl.ncReplaceTpl(data));
	};
	/**========================================
	 * 地址删除
	 ========================================*/
	var AddrDel = {
		__postFlat :true,
		/**
		 * 删除地址的相关事件
		 */
		_bindEvents: function () {
			//删除按钮事件
			$element.addrList.on("click", "a[data-del-address-id]", function () {
				var $this = $(this),
					addressId = $this.data("delAddressId");
				// console.log("删除按钮上的数据是:", addressId);
				if (addressId) {

					Nc.layerConfirm("是否删除地址？", {
							btn: ['确认', '取消'],
							sizeEnum: "extraSmall",
							yes: function () {

								if (AddrDel.__postFlat == false ){
								    return;
								}
								AddrDel.__postFlat = false;
								$.post(URL.del, {
									addressId: addressId
								}, function (data, textStatus, xhr) {
									if (data.code == "200") {
										Nc.alertSucceed(data.message);
											$this.closest('li').remove();
											_setAddrSel();
									} else {
										Nc.alertError(data.message);
									}
									AddrDel.__postFlat = true;
								}).error(function () {
									AddrDel.__postFlat = true;
								});
							}
						}
					);
				}

			});
		}
	};

	/**
	 * 地址验证对象
	 * @type {{}}
	 */
	var areaValidator;

	/**========================================
	 * 地址添加
	 ========================================*/

	var AddrAdd = {

		isSelectLast:false,
		/**
		 * 一个新地址的模板
		 */
		addrTmlp: '<li class="receive_add address_item">' + '<input name="addr" id="addr_{addressId}"' + 'type="radio" class="radio" value="{addressId}" ' + 'data-address="{address}" ' + 'data-real-name="{realName}" ' + 'data-area-id="{areaId}" ' + 'data-mob-phone="{mobPhone}" ' + 'data-tel-phone="{telPhone}" ' + 'data-area-info="{areaInfo}" ' + 'checked />' + '<label for="addr_{addressId}">' + '<span class="true-name">{realName}</span>' + '<span class="address">{areaInfo}  {address}</span>' + '<span class="phone"> <i class="icon-mobile-phone"></i>' + '{phone}' + '</span>' + '</label>' + '<a href="javascript:void(0);" data-del-address-id="{addressId}" class="del">[ 删除 ]</a>' + '</li>',
		/**
		 * 添加一个地址到dom上去
		 */
		_addAddrToDom: function (data) {
			data.phone = Nc.isEmpty(data.mobPhone)
				?(Nc.isEmpty(data.telPhone) ?"":data.telPhone)
				:data.mobPhone;

			var $addTmlp = $(AddrAdd.addrTmlp.ncReplaceTpl(data));
			$element.addrList.find("ul").append($addTmlp);
			$element.addrList.show();
			$element.addrSelectInfo.hide();
			//默认选中
			_setAddrSel(data.addressId);

		},
		/**
		 * 显示新增地址按钮
		 * @param [noClose] [是否不能关闭] 地址为空的时候强制写入一个新地址
		 */
		_showNewAddressDialog: function (noClose) {
			$element.addressDialog.find("input[name='realName']").val("");
			$element.addressDialog.find("input[name='address']").val("");
			$element.addressDialog.find("input[name='areaInfo']").val("");
			$element.addressDialog.find("input[name='telPhone']").val("");
			$element.addressDialog.find("input[name='mobPhone']").val("");
			$element.addressDialog.find("input[name='isDefault']").prop(false);
			$("#divAddress").data("nc.area").restart();
			areaValidator && areaValidator.resetForm();
			AddrAdd.isSelectLast = false;
			var layerOption = {
				title: "新增地址",
				content: $element.addressDialog,
				yes: function (index, layero) {
					if (!$element.form.valid() || __postFlat == false) {
						return;
					}
					__postFlat = false;
					$.post(URL.add, $element.form.serializeObject(), function (data, textStatus, xhr) {
						if (data.code == "200") {
							layer.close(index);
							AddrAdd._addAddrToDom(data.data);
						} else {
							Nc.alertError("添加地址失败");
						}
						__postFlat = true;
					}, "json").error(function () {
						Nc.alertError("添加地址失败");
						__postFlat=true;
					});

				}
			};
			if (noClose) {
				layerOption.closeBtn = 0;
				layerOption.btn = ['确认提交'];
			}
			Nc.layerOpen(layerOption);
		},
		init: function () {
			//地址初始化
			$("#divAddress").NcArea({
				hiddenInput: [{
					name: "areaId",
					value: "0"
				}, {
					name: "areaInfo",
					value: ""
				}],
				url: ncGlobal.webRoot + 'area/list.json/',
				dataHiddenName: "areaId",
				showDeep: 3,
				deepInputAddType: "clear", //mod|clear
				showLoading:$("#addressForm")
			}).on("nc.select.selected", function (e) {
				AddrAdd.isSelectLast = false;
			}).on("nc.select.last", function (e) {
				AddrAdd.isSelectLast = true;
			});
			//表单验证
			jQuery.validator.addMethod("isSelectLast", function (value, element) {
				return AddrAdd.isSelectLast;
			}, "请将地区选择完整");

			var addressGlobal = {
				//是否选择到最后一级
				isSelectLast: false,
				isInputAddPhone: function ($form) {
					return ($("#addressForm").find("input[name='telPhone']").val() == '') && ($("#addressForm").find("input[name='mobPhone']").val() == '')
				},
				isInputEditPhone: function ($form) {
					return ($("#editAddressForm").find("input[name='telPhone']").val() == '') && ($("#editAddressForm").find("input[name='mobPhone']").val() == '')
				}
			};
			areaValidator = $("#addressForm").validate({
				errorPlacement: function (error, element) {
					if (element.attr("name") == "areaId") {
						$("#divAddress").append(error);
					} else {
						error.insertAfter(element);
					}
				},
				rules: {
					realName: {
						required: true
					},
					address: {
						required: true
					},
					areaId: {
						isSelectLast: true
					},
					telPhone: {
						required: addressGlobal.isInputAddPhone
					},
					mobPhone: {
						required: addressGlobal.isInputAddPhone,
						mobile   : true
					}
				},
				messages: {
					realName: {
						required: "请填写收货人姓名"
					},
					address: {
						required: "请填写详细街道地址"
					},
					telPhone: {
						required: "电话/手机号码至少填写一项"
					},
					mobPhone: {
						required: "电话/手机号码至少填写一项",
						mobile   : '<i class="icon-exclamation-sign"></i>请输入正确的手机号',
					}
				},
				groups: {
					phone: "telPhone mobPhone"
				}
			});
			//按钮事件初始化
			$element.addReciver.on("click", function () {
				AddrAdd._showNewAddressDialog();
				return false;
			});
		}
	};


	/**
	 * 地址数据相关操作
	 * @type {Object}
	 */
	var Addr = {
		//当前选择的地址
		curr: '',
		//
	};
	/////
	return {
		init: function () {

			//地址选择事件
			bindEvnets();
			//删除地址相关
			AddrDel._bindEvents();
			AddrAdd.init();
			//新增地址相关事件

			//判断地址是否为空
			if (!$element.addrSelectInfo.find("li").length) {
				AddrAdd._showNewAddressDialog(true);
			}


			//如有默认地址
			$element.addressId.length && $element.addressId.val() != "" && function () {
				//console.log("有默认地址,获取运费信息", $element.addressId);
				//console.log("发送隐藏所有修改事件");
				ncEventsManger.trigger(eventText.buyEditHide);
				ncEventsManger.trigger("lockOrderSubmit");
				//console.log("计算运费");
				_getFreight($element.addressId.val());
				//选中默认地址
				$("#addrList :radio[value=" +$element.addressId.val()+"]").attr("checked","checked").trigger("change");
			}();
		},
		/**
		 * 输出全局对象
		 */
		global: $.extend({}, dataKey)
	};
});
/**
 * 发票模块
 */
ncDefine("invoice", ["nc.eventManger"], function (ncEventManger) {
	"use strict"
	var __postFlat = true;
	//标示发票地址是否选择到了最后一位
	var invIsSelectLast = false;
	/**
	 * 所需设置
	 */
	var options = {
		$el: {
			//发票id隐藏input
			$invoiceId: $("#invoiceId"),
			//修改发票按钮
			editInvoiceBtn: $("#editInvoiceBtn"),
			//发票列表
			invoiceList: $("#invoiceList"),
			//发票最外层框
			invoiceBox: $("#invoiceBox"),
			//
			invoicePanel: "#invoicePanel",
			vatInvoicePanel: "#vatInvoicePanel",
			invTitle: "#invTitle",
			//发票保存按钮
			saveInvoice: "#saveInvoice",
			//不开发票按钮
			cancelInvoice: "#cancelInvoice",
			//新增发票区域
			addInvBox: "#addInvBox"
		},
		htmlClass: {
			//选中后边框变红的样式
			currentBox: "current_box",
			selectItem: "ncc-selected-item"
		},
		URL: {
			//
			load: ncGlobal.webRoot + "buy/invoice/list",
			//发票保存地址
			save: ncGlobal.webRoot + "member/invoice/add",

			del: ncGlobal.webRoot + "member/invoice/del"
		},
		//发票详情模板
		infoTmpl: "<ul><li>{text}</li></ul>",
		eventText: {
			buyEditHide: "buy.edit.hide",
			//修改按钮显示事件
			buyEditShow: "buy.edit.show"
		}
	};
	/**
	 * 基础小方法
	 * @type {{}}
	 */
	var until = {
		/**
		 * 获取选择的发票radio
		 */
		getSelRadio: function () {
			return options.$el.invoiceList.find("input[data-invoice-select]:checked");
		}
	};

	/**
	 * 加载html
	 * @private
	 */
	function _loadHtml() {
		var invoiceId = options.$el.$invoiceId.val();

		options.$el.invoiceList.load(options.URL.load,
			{
				invoiceId: invoiceId ? invoiceId : 0,
				allowVat: buyGlobal.allowVat == "" || buyGlobal.allowVat == 0 ? 0 : 1
			},
			function () {
				//绑定地址插件
				$("#vregion").NcArea({
					hiddenInput: [{
						name: "receiverArea",
						value: "0"
					}, {
						name: "areaInfo",
						value: ""
					}],
					url: ncGlobal.webRoot + 'area/list.json/',
					dataHiddenName: "areaId",
					showDeep: 3,
					deepInputAddType: "clear" //mod|clear
				}).on("nc.select.selected", function (e) {
					invIsSelectLast = false;
				}).on("nc.select.last", function (e) {
					invIsSelectLast = true;
				});

				//动态添加一个form
				$("#vatInvoicePanel").wrap('<form id = "vatInvoiceForm" > </form>');
				//表单验证
				jQuery.validator.addMethod("isSelectLast", function (value, element) {
					return invIsSelectLast;
				}, "请将地区选择完整");

				$('#vatInvoiceForm').validate({
					errorPlacement: function(error, element) {
						error.appendTo(element.parents("dd"));
					},
					rules : {
						company : {
							required : true
						},
						codeSn : {
							required : true
						},
						registerAddress : {
							required : true
						},
						registerPhone : {
							required : true,
							number:true
						},
						bankName : {
							required : true
						},
						bankAccount : {
							required : true
						},
						receiverName : {
							required : true
						},
						receiverPhone : {
							required : true,
							mobile : true
						},
						receiverArea : {
							isSelectLast: true
						},
						receiverAddress : {
							required : true
						}
					},
					messages : {
						company : {
							required : '<i class="icon-exclamation-sign"></i>单位名称不能为空'
						},
						codeSn : {
							required : '<i class="icon-exclamation-sign"></i>纳税人识别号不能为空'
						},
						registerAddress : {
							required : '<i class="icon-exclamation-sign"></i>注册地址不能为空'
						},
						registerPhone : {
							required : '<i class="icon-exclamation-sign"></i>注册电话不能为空',
							number:'<i class="icon-exclamation-sign"></i>请输入正确的电话号码'
						},
						bankName : {
							required : '<i class="icon-exclamation-sign"></i>开户银行不能为空'
						},
						bankAccount : {
							required : '<i class="icon-exclamation-sign"></i>银行账户不能为空'
						},
						receiverName : {
							required : '<i class="icon-exclamation-sign"></i>收票人姓名不能为空'
						},
						receiverPhone : {
							required : '<i class="icon-exclamation-sign"></i>收票人手机号不能为空',
							mobile   : '<i class="icon-exclamation-sign"></i>请输入正确的手机号',
						},
						receiverArea : {
							isSelectLast: '<i class="icon-exclamation-sign"></i>请将地区选择完整'
						},
						receiverAddress : {
							required : '<i class="icon-exclamation-sign"></i>送票地址不能为空'
						}
					}
				});
			});
	}

	/**
	 * post 保存新增的发票信息
	 */
	function _saveInvPost() {
		//获取input对象
		var inputObj = $(options.$el.addInvBox).serializeObjectByEle();
		//console.log("inputObj", inputObj);

		if(inputObj.inv_title_select == "company"){
			if(!inputObj.title){
				Nc.alertError("公司名称不能为空");
				return ;
			}
		}
		
		(inputObj.invoiceType == 1 && inputObj.inv_title_select == "person") && ( inputObj.title = "个人")
		delete inputObj.inv_title_select;
		
		if (inputObj.invoiceType == 2 && !$("#vatInvoiceForm").valid()){
		    return ;
		}
		
		
		$.post(options.URL.save, inputObj, function (data) {
			//console.log("保存post 返回数据是", data);
			if (data.code == "200") {
				_addInvInfoToPanel(data.data);
			} else {
				Nc.alertError(data.message);
			}
		});
	}

	/**
	 * 显示与隐藏元素
	 * @param show true:显示发票列表 false：隐藏发票列表
	 */
	function _togglePanel(show) {
		if (show) {
			ncEventManger.trigger(options.eventText.buyEditHide);
			ncEventManger.trigger("lockOrderSubmit");

			//修改按钮隐藏
			options.$el.editInvoiceBtn.hide();
			//边框变红
			options.$el.invoiceBox.addClass(options.htmlClass.currentBox);
			//清空内容
			options.$el.invoiceList.empty();
		} else {
			ncEventManger.trigger(options.eventText.buyEditShow);
			ncEventManger.trigger("unlockOrderSubmit");

			//显示修改按钮
			options.$el.editInvoiceBtn.show();
			//边框变红
			options.$el.invoiceBox.removeClass(options.htmlClass.currentBox);
			//清空内容
			options.$el.invoiceList.empty();
		}
	}

	/**
	 * 添加发票信息到地址发票信息上面去
	 * @private
	 */
	function _addInvInfoToPanel(data, id) {
		//console.log("_addInvInfoToPanel参数：", data, id);

		//数据模型
		var _text = []
		//_d = {
		//    invoiceType: Nc.isEmpty(data.invoiceType) ? "" : data.invoiceType,
		//    title: Nc.isEmpty(data.title) ? '不需要发票' : data.title,
		//    content: Nc.isEmpty(data.content) ? '' : data.content
		//},
		//_t = ncTemplate(options.infoTmpl)(_d)
			;
		//
		if (Nc.isEmpty(id) && !Nc.isString(data)) {
			if (data.invoiceType == 1) {
				//普票
				_text = ["普通发票", data.title, data.content];
			} else {
				_text = ["增值税发票", data.company, data.codeSn, data.registerAddress];
			}
		} else {
			_text = [data];
		}
		_togglePanel();
		//显示
		options.$el.invoiceList.html(_text.join(" "));
		//修改隐藏input
		options.$el.$invoiceId.val(
			Nc.isEmpty(id) ? (data.invoiceId != undefined ? data.invoiceId : 0) : id
		);
	}

	/**
	 * 获取选择的input radio 上的发票信息
	 * @private
	 */
	function _getInputInvInfo() {
		var $radio = until.getSelRadio()
		//value = $radio.length && $radio.val(),
		//_c = $radio &&
			;
		_addInvInfoToPanel($radio.next("label").html().trim(), $radio.val());

	}

	/**
	 * 删除
	 * @param invoiceId
	 * @private
	 */
	function _delInv(invoiceId) {
		if (Nc.isEmpty(invoiceId)) {
			return;
		}

		Nc.layerConfirm("是否删除该发票信息？", {
				btn: ['确认', '取消'],
				sizeEnum: "extraSmall",
				yes: function () {
					if (__postFlat == false) {
						return;
					}
					__postFlat = false;
					$.post(options.URL.del, {invoiceId: invoiceId}, function (data) {
						if (data.code == '200') {
							_loadHtml();
							layer.closeAll();
						} else {
							Nc.alertError(data.message);
						}
					}, 'json').always(function () {
						__postFlat = true;
					});
				}
			}
		);
	}

	/**
	 * 验证
	 * @private
	 */
	function _verify(){

	}
	/////
	return {
		init: function () {


			//点击修改事件
			options.$el.editInvoiceBtn.on("click", function () {
				_togglePanel(true);
				//加载内容
				_loadHtml();
			});
			//点击发票列表变色
			options.$el.invoiceList
				.on("change", "input[data-invoice-select]", function () {
					var $this = $(this),
						value = $this.val(),
						$li = $this.closest("li"),
						$addInvBox = $("#addInvBox")
						;
					$li.siblings("li").removeClass(options.htmlClass.selectItem);
					$li.addClass(options.htmlClass.selectItem);
					//判断是否是新增
					if (value == 0) {
						//显示新增
						$addInvBox.show();
					} else {
						$addInvBox.hide();
					}
				})
				//发票类型选择
				.on("change", "input[data-select-invoice-type]", function () {
					//console.log("发票类型选择事件触发,值是:", $(this).val());
					var $invoicePanel = $(options.$el.invoicePanel);
					var $vatInvoicePanel = $(options.$el.vatInvoicePanel);
					if ($(this).val() == 1) {
						$invoicePanel.show();
						$vatInvoicePanel.hide();
					} else {
						$invoicePanel.hide();
						$vatInvoicePanel.show();
					}
				})
				//发票抬头类型选择
				.on("change", "select[select-title-type]", function () {
					//console.log("发票抬头类型选择,值是:", $(this).val());
					var $invTitle = $(options.$el.invTitle);
					//console.log("$invTitle", $invTitle);

					if ($(this).val() == 'company') {
						$invTitle.show();
					} else {
						$invTitle.hide();
					}
				})
				//点击保存发票按钮
				.on("click", "#saveInvoice", function () {
					//console.log("点击保存发票按钮 evnets");
					var a = options.$el.invoiceList.find("input[data-invoice-select]:checked");
					//console.log("选择的发票地址id是：", a.val());
					if (a.val() == 0) {
						//新增发票
						_saveInvPost();
					} else {
						//保存原有发票信息
						_getInputInvInfo();
					}
				})
				//不开发票
				.on("click", "#cancelInvoice", function () {
					//console.log("点击不开发票按钮 evnets");
					_addInvInfoToPanel("不需要发票");
				})
				//删除一个发票信息
				.on("click", "[data-invoice-del]", function () {
					//console.log("删除发票events");
					_delInv($(this).data("invoiceDel"))
				})
		}
	}

});
/**
 * 价格计算
 */
ncDefine("amount", ["address","nc.eventManger"], function (addr,ncEventManger) {
	/**
	 * jquery 对象
	 * @type {{}}
	 */
	var $el = {
		orderTotal: $("#orderTotal"),
		eachStoreAmount: "#eachStoreAmount_",
		eachStoreFreight: "#eachStoreFreight_"
	};


	/**
	 * 初始化
	 * @param key
	 * @param value
	 */
	function boostarp(key, value) {
		//console.log("bootstrap 开始,数据:", key, value);
		if (Nc.isEmpty(value)) {
			return;
		}
		var allowSend = true;
		$.each(value.storeList, function (i, n) {
			$($el.eachStoreFreight + n.storeId).html(Nc.priceFormat (n.storeFreightAmount));
			$($el.eachStoreAmount + n.storeId).html(Nc.priceFormat (n.storeAmount));
			if (!_verifyAllowSend(n.goodsList)){
				allowSend = false;
			}
		});
		//console.log(allowSend)
		if (allowSend){
			ncEventManger.trigger("unlockOrderSubmit")
		}else{
			ncEventManger.trigger("lockOrderSubmit")
		}

		//console.log("设置订单总金额");
		$el.orderTotal.html(Nc.priceFormat (value.buyAmount) );

	}

	/**
	 * 验证是否有货
	 * @private
	 */
	function _verifyAllowSend(goodsList){
		//console.log(goodsList)
		var result = true;
		goodsList.forEach(function(value) {
			var $element = $("#goodsBuyNum_" + value.goodsId);
			if (value.allowSend <= 0) {
				$element.html('<em class="goods-subtotal">( 无货 )</em>');
				result = false;
			} else {
				$element.html($element.data("buyNum"));
			}
		});
		return result;
	}
	/**
	 * 事件
	 * @private
	 */
	function _bindEvents() {

		//console.log("监听运费数据变化:", addr.global.freight);
		ncData.sub("set", addr.global.freight, boostarp);
	}

	////
	return {
		init: function () {
			//console.log("init 事件");
			_bindEvents();
		}
	}
}).use("amount").init();
/**
 * 货到付款
 */
ncDefine("payment", ["nc.eventManger"], function (ncEventManger) {
	var Payment = function () {
		this.$editPaymentBtn = $("#editPaymentBtn");
		this.$paymentList = $("#paymentList");
		this.$paymentCon = $("#paymentCon");
		this.$showOfflineGoods = $("#showOfflineGoods");
		this.$nccPaymentShowgoodsList = $("#nccPaymentShowgoodsList");
		this.$paymentTypeOnline = $("#paymentTypeOnline");
	};
	var that = {};
	Payment.prototype = {
		init: function () {
			that = this;
			this.$editPaymentBtn.click(function () {
				//console.log("修改支付方式点击");
				that._showPaymentPanel();
			});
			this.$paymentList.on("change","input[name=payment_type]", function () {
				//console.log("选择支付方式事件");
				var payType = $(this).val();
				if(payType == "offline"){
					//console.log("支付方式选择，到付");
					Nc.layerOpen({
						title: "请确认支付方式",
						content:  $("#confirmOffAndOnGodosList"),
						yes: function (index, layero) {
							//console.log("确认支付方式");
							that.$showOfflineGoods.show();
							layer.close(index);
						},
						cancel : function () {
							//console.log("取消货到付款");
							that.$nccPaymentShowgoodsList.hide();
							that.$paymentTypeOnline.attr("checked","checked");
						}
					});
				}else{
					//console.log("支付方式选择，在线");
					that.$showOfflineGoods.hide();
				}
			});
			this.$showOfflineGoods.hover(function () {
					//console.log("货到付款移动到")
					that.$nccPaymentShowgoodsList.show();
				},
				function () {
					//console.log("货到付款移出")
					that.$nccPaymentShowgoodsList.hide();
				}
			);
			$("#hidePaymentListBtn").click(function () {
				//console.log("保存支付方式按钮点击事件")
				var a = that.$paymentList.find("input[name=payment_type]:checked");
				$("#paymentTypeCode").val(a.val())
				that._hidePaymentPanel();
				$("#nccCandidateItems").html("<ul><li>" +(a.val() == "online" ? "在线支付":"货到付款")+"</li></ul>")
			})


		},
		_showPaymentPanel: function () {
			ncEventManger.trigger("buy.edit.hide");
			this.$editPaymentBtn.hide()
			this.$paymentList.show();
			this.$paymentCon.addClass("current_box");
		},
		_hidePaymentPanel:function(){
			ncEventManger.trigger("buy.edit.show");
			this.$editPaymentBtn.show()
			this.$paymentList.hide();
			this.$paymentCon.removeClass("current_box");
		}
	}
	return new Payment();
});
/**
 * 表单提交
 */
ncDefine("dataSubmit", ["nc.eventManger"], function (ncEventManger) {
	var URL = {
		submitOrder: ncGlobal.webRoot + "buy/save",
	};
	/**
	 * 所用的元素定义
	 */
	var $element = {
		//提交表单按钮
		submitOrder: $("#submitOrder"),
		//所有修改按钮的元素
		buyEdit: $("a[data-buy-edit]"),
		//保存支付方式的input
		paymentTypeCode: $("#paymentTypeCode"),
		//保存默认地址
		addressId: $("#addressId"),
		//发票id隐藏input
		invoiceId: $("#invoiceId"),
		//购物车标志
		isCart: $("#isCart")
	};

	/**
	 * 样式列表
	 * @type {{ok: string}}
	 */
	var elementClass = {
		//提交按钮可以点击的样式
		ok: "ok"
	};

	/**
	 * 保存发送的事件的文本
	 */
	var eventText = {
		//修改按钮隐藏事件
		buyEditHide: "buy.edit.hide",
		//修改按钮显示事件
		buyEditShow: "buy.edit.show"
	};

	/**
	 * 显示或隐藏修改按钮
	 * @param toggle true:显示 false：隐藏
	 * @private
	 */
	function _toggleBuyEditBtn(toggle) {
		if (toggle) {
			$element.buyEdit.show();
		} else {
			$element.buyEdit.hide();
		}
	}

	/**
	 * 提交按钮锁定
	 * @param lock true：锁定不能点击 false：能点击
	 * @private
	 */
	function _btnLock(lock) {
		if (lock) {
			$element.submitOrder.removeClass(elementClass.ok);

		} else {
			$element.submitOrder.addClass(elementClass.ok);
		}
	}

	/**
	 * 提交数据
	 * @private
	 */
	function _postData() {
		var a = $('tbody[data-store-id]');
		var _d = {
			"addressId": $element.addressId.val(),
			"paymentTypeCode": $element.paymentTypeCode.val(),
			"invoiceId": $element.invoiceId.val(),
			"isCart":$element.isCart.val(),
			"storeList": $.map(a, function (n) {
				var $body = $(n);
				return {
					storeId: $body.data("storeId"),
					"receiverMessage": $body.find("textarea[data-receiver-messagemax]").val(),
					goodsList: $.map($body.find("tr[data-goods-id]"), function (nn) {
						return $(nn).data();
					})

				}
			})
		};
		//console.log("发送生成订单数据", _d);
		$.post(URL.submitOrder, {buyData: JSON.stringify(_d)}, function (data) {
			//console.log("发送生成订单数据成功,返回信息:", data)
			if (data.code == "200") {
						Nc.go(data.url);
			} else {
				Nc.alertError(data.message);
			}
		}).error(function () {
			//Nc.alertError("连接超时");
		})
	}


	function _bindEvents() {

		$element.submitOrder.on("click", function (e) {
			//console.log("提交订单按钮点击");
			if ($(this).hasClass("ok")){
				_postData();
			}
		});

		ncEventManger
			.on(eventText.buyEditHide, function () {
				//console.log("收到隐藏修改按钮事件");
				//console.log("隐藏所有修改按钮");
				_toggleBuyEditBtn(false);
				//console.log("锁定提交订单按钮");
				//_btnLock(true);

			});
		ncEventManger.on(eventText.buyEditShow, function () {
			//console.log("收到显示修改按钮事件");
			//console.log("显示所有修改按钮");
			_toggleBuyEditBtn(true);
			//console.log("解锁订单提交按钮");
			//_btnLock(false);
		});
		/**
		 * 监听锁定提交按钮事件
		 */
		ncEventManger.on("lockOrderSubmit",function(event){
			//console.log("监听锁定提交按钮事件");
			event.stopImmediatePropagation();
			_btnLock(true)
		});
		/**
		 * 监听解锁订单提交按钮事件
		 */
		ncEventManger.on("unlockOrderSubmit",function(event){
			//event.stopImmediatePropagation();
			_btnLock(false)
		})

	}

	///
	return {
		init: function () {
			_bindEvents();
		}
	}

}).use("dataSubmit").init();

$(function () {

	if (!Nc.isEmpty(ncGlobal.freightTemplateId)) {
		ncRequire("nc.goosFreight").ajaxLoadFreightTemplate();
	}
	ncRequire("address").init();
	ncRequire("invoice").init();
	ncRequire("payment").init();
});