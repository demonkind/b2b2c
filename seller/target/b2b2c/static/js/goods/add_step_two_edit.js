/**
 * 加载规格
 */
ncDefine("goodsEdit.spec", [], function() {
	"use strict";
	var Spec = function() {


		var that = this;
		$.extend(this, Spec.setting);
		//获取goodscommondid
		this.commonId = this.$goodsCommonInput.length && this.$goodsCommonInput.val();
		//如果common id 为空就直接退出
		if (!this.commonId) {
			return;
		}
		//已选规格
		this.specList = $.parseJSON(goodsSpecValueJson);
		//库存、价格等信息
		this.goodsList = [];
		//图片列表
		this.goodsPic = [];

		this._cache = {};

		//异步加载已经选择的spec
		$.when(this.getSpecFormPost())
			.done(function() {
				//向页面中写入数据
				that.render();
			});
	};
	/**
	 * 设置
	 */
	Spec.setting = {
		//存放goodsid 的input
		$goodsCommonInput: $("input[name='commonId']"),
		//获取spec 的地址
		postUrl: ncGlobal.sellerRoot + "goods/edit/get/goods.json",
		//规格标示
		specFlat: "spec-h4-{specId}",
		//用于定位规格值的标示
		specValueIdTmpl: "spec-value-checkbox-{specId}-{specValueId}",

		specValueTextTmpl: "spec-value-text-{specId}-{specValueId}",

		$specDivBody: $("#spec-div-body"),

		$goodsPicList: $("#goods-pic-list"),
		//单个图片的li
		goodSpicUploadTmpl: '<li class="ncsc-goodspic-upload">' +
			'<div class="upload-thumb"><img src="{goodPicUrl}"> </div>' +
			'<div class="show-default"> <a href="javascript:void(0)" class="del" title="移除">X</a> </div>' +
			'</li>'
	};

	Spec.prototype = {
		/**
		 * 根据post地址获取已选择的规格信息
		 */
		getSpecFormPost: function() {
			var that = this,
				dtd = $.Deferred();
			$.post(this.postUrl, {
				commonId: that.commonId
			}, function(data) {
				if (data.code == 200) {
					that.goodsList = data.data.goodsJson;
					that.goodsPic = data.data.goodsPic;
					that.specJson = !Nc.isEmpty (data.data.specJson)? $.parseJSON(data.data.specJson):[];
					dtd.resolve();
				} else {
					Nc.alertError(data.message);
				}

			}).error(function() {
				//Nc.alertError("连接超时");
			});
			return dtd.promise();
		},
		/**
		 * 
		 */
		render: function() {

			var that = this;
			//如果goods列表是空的就弹出报错
			if (!this.goodsList.length) {
				return;
			}
			//修改已经选择的规格值
			this._modSpecValue();
			//勾选规格值
			this.specList && this._selSpecValue(this.specList);
			//写库存等信息
			this._renderGoodsInput(this.goodsList);


			//写图片信息
			if (this.goodsPic) {
				this._renderPic(this.goodsPic);
			}
			//发送加载完成事件
			Nc.eventManger.trigger('specValueUpload.finish',[this.goodsPic]);
		},
		/**
		 * 加载图片,通过事件触发
		 */
		_renderPic: function(goodsPic) {

			//排列数据
			var that = this;
			$.each(goodsPic, function(index, el) {
				var $ul = that.$goodsPicList.find("div[data-spce-value-id='" + el.colorId + "']").find("ul");
				el.url = ncGlobal.uploadRoot + el.imageName;
				el.name = el.imageName;

				Nc.eventManger.trigger("nc.imageupload.succeed", [{
					target: $ul
				}, el]);
			});
		},

		/**
		 * 修改已经选择的规格值
		 * @private
		 */
		_modSpecValue:function(){
			//console.log("修改已经选择的规格值",this.specJson);
			if (Nc.isEmpty(this.specJson)){
			   return ;
			}
			$.each(this.specJson,function(i,n){
				var $spec = n,
					specPanel = $("#spec_"),
					specH4 = $("#spec-h4-"+ n.specId)
					;
				specH4.data("specName" , $spec.specName);
				$.each($spec.specValueList , function(ii,nn){
					var $specValue = $("#spec-value-checkbox-" + n.specId + "-" + nn.specValueId);
					$specValue.data("specValueName",nn.specValueName);
				})
			})
			
		},
		/**
		 * 根据数据规格 勾选
		 */
		_selSpecValue: function(specList) {

			var that = this;
			$.each(specList, function(i, n) {
				//修改规格名称
				var h4 = $("#" + that.specFlat.ncReplaceTpl({
					specId: n.specId
				}));
				if (h4.length) {
					h4.find("font").html(n.specName);
					//规格值
					$.each(n.specValueList, function(ii, specValue) {
						var specValueCheck = $("#" + that.specValueIdTmpl.ncReplaceTpl({
								specId: n.specId,
								specValueId: specValue.specValueId
							})),
							specValueText = $("#" + that.specValueTextTmpl.ncReplaceTpl({
								specId: n.specId,
								specValueId: specValue.specValueId
							}));
						//改变规格值
						specValueText.lenght && specValueText.html(specValue.specValueName)
						specValueCheck && (specValueCheck.attr("checked", "checked").trigger("change"))
					});
				}

			});
		},
		/**
		 * 填写库存等信息
		 */
		_renderGoodsInput: function() {
			//根据goods规格排序
			if (this.$specDivBody.find("td[data-spec-value-id]").length) {
				//有规格值的
				this._renderHaveSpecValue(this.goodsList);
			} else {
				//没有规格时
				this._renderNoSpecValue(this.goodsList);
			}
		},
		/**
		 * 有规格值时写库存等信息
		 * @return {[type]} [description]
		 */
		_renderHaveSpecValue: function(goodsList) {
			var that = this;
			//循环已生成的tr
			$.each(this.$specDivBody.find("tr"), function(index, el) {
				var $tr = $(el),
					a = that.getSpecValueIdFormTr($(el)),
					b = that._getGoodsInfo(a);
				b && (that._setTdPrice(b, $tr))
			});
		},
		//获取tr上的specvalueid数组，排序好了之后的
		getSpecValueIdFormTr: function($el) {
			var a = $.map($el.find("[data-spec-value-id]"), function(n) {
				return $(n).attr("data-spec-value-id");
			});
			return a.ncArraySort();
		},
		/**
		 * 没有规格值时写库存
		 * @return {[type]} [description]
		 */
		_renderNoSpecValue: function(goodsList) {
			var $tr = this.$specDivBody.find("tr[data-no-spec]");
			$tr && (this._setTdPrice(goodsList [0], $tr))
		},
		/**
		 *根据tr 上的specvalueid数组 查找post过来的数据
		 */
		_getGoodsInfo: function(specValueArray) {
			var that = this,
				result = '';
			//将规格值信息排序后输出
			var formatData = function(specString) {
				return specString.split(",").ncArraySort().toString();
			};
			//specValueArray 这个是排序后的数组
			$.each(this.goodsList, function(i, n) {
				//获取goods信息上的specValueIds
				var a = formatData(n.specValueIds);
				if (specValueArray.toString() == a) {
					result = n;
					return;
				}
			});
			return result;
		},
		/**
		 * 将库存等信息写入到 td 中的input中去
		 */
		_setTdPrice: function(goodsInfo, $el) {
			var $dataMarketprice = $el.find("input[data-marketprice]"),
				$dataPrice = $el.find("input[data-price]"),
				$dataStock = $el.find("input[data-stock]"),
				$dataAlarm = $el.find("input[data-alarm]"),
				$dataSku = $el.find("input[data-sku]"),
				$dataBarcode = $el.find("input[data-barcode]");

			//修改goodsid
			goodsInfo.goodsId && ($el.attr("data-goods-id",goodsInfo.goodsId))

			$dataMarketprice.val(goodsInfo.markerPrice);
			$dataPrice.val(goodsInfo.goodsPrice);
			$dataStock.val(goodsInfo.goodsStorage);
			$dataAlarm.val(goodsInfo.goodsStorageAlarm);
			$dataSku.val(goodsInfo.goodsSerial);
			$dataBarcode.val(goodsInfo.goodsBarcode);
		}
	};
	return {
		factory: function() {
			new Spec();
		}
	};
});
/**
 * 地区模块
 * 这个只将地区id
 */
ncDefine("goodsEdit.brand", [], function() {
	var b_name = "#b_name";
	var b_Id = "#b_Id";
	var brandListUl = "#brand-list-ul";
	//获取品牌值
	return {
		factory: function() {
			var $b_Id = $(b_Id),
				$li = $(brandListUl).find("li[data-id='" + $b_Id.val() + "']");
			$li.length && ($(b_name).val($li.data('brand-name')));
		}
	}
});
/**
 * 编辑商品初始化组件
 */
ncDefine(
	"goodsEdit.bootstarp", ['goodsEdit.spec',  'goodsEdit.brand'],
	function(specModule, brandModule) {
		//获取goodscommonid
		var goodCommonId = $("input[name='commonId']");

		/**
		 * 将信息填到元素上去
		 * 这个主要是加载其他组件
		 */
		var buildElement = function() {
			//生成规格
			specModule.factory();
			brandModule.factory()
		};

		//初始化
		var init = function() {
			//如果goodsCommon id 是空就返回
			if (!goodCommonId.length || goodCommonId == '') {
				return;
			}
			//加载其他组件
			buildElement();
		};
		//直接初始化
		init();
	});