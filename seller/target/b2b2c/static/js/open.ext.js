/**
 * Created by cj on 2015/12/18.
 */
var Category = function() {
	/**
	 * element
	 */
	var $ncsFreightSelector = $("#ncscOpenSelector"),
		$categoryBox = $("#categoryBox"),
		$BindCategoryContent = $("#BindCategoryContent"),
		$btnBindCategory = $("#btnBindCategory"),
		$categoryTab = $("#categoryTab"),
		//item 显示区域
		$categoryItemPanel = $("#categoryItemPanel"),
		//添加按钮
		$addBtn = $ncsFreightSelector.find("a[data-add]"),
		$tableCategoryBody = $("#tableCategoryBody");

	/**
	 * 用到的几个模板
	 */
	var tmpl = {
		//正常未选中
		iconCheckEmpty: '<i class="icon icon-check-empty"></i>',
		//已选中时
		iconCheck: '<i class="icon icon-check"></i>'
	};

	/**
	 * 动态添加的样式什么的
	 * @type {Object}
	 */
	var elClass = {
		curr: "curr",
		selected: "selected"
	};
	/**
	 * 内置变量
	 */
	var _deep = 0,
		_CList = [],
		_current,
		_selectedCategory = [];
	/**
	 * hidden Input value refresh
	 */
	var refreshInput = function() {
		var $input = $("#bindCategory"),
			d = tc.__postList,
			r = [];
		r = $.map(d, function(item, index) {
			var _r = {
				categoryId1: 0,
				categoryId2: 0,
				categoryId3: 0,
				commissionRate: 0
			};

			for (var i = item.length ; i >= 0; i--) {
				i == 0 && (_r.categoryId1 = item[i].categoryId, _r.categoryName1 = item[i].categoryName)
				i == 1 && (_r.categoryId2 = item[i].categoryId, _r.categoryName2 = item[i].categoryName)
				i == 2 && (_r.categoryId3 = item[i].categoryId, _r.categoryName3 = item[i].categoryName)
				i == item.length && (_r.commissionRate = item[i-1].commissionRate)
			};
			return _r;
		});
		// console.log("$input", $input);
		// console.log("d", d);
		// console.log("d to string ", JSON.stringify(d));
		// console.log("r is : ", r);
		// console.log("return  is : ", JSON.stringify(r));
		$input.val(r.length ? JSON.stringify(r) : '');
		//刷新验证
		v.element("#bindCategory");
	};

	/**
	 * 根据深度获取需要显示的ul元素
	 */
	var _getUlByDeep = function(deep) {
		return $categoryBox.find("div[data-category-item='" + deep + "']");
	};
	/**
	 * [_getLast description]
	 * @return {[type]} [description]
	 */
	var _getLast = function() {

	};

	/**
	 * [_getAll description]
	 * @return {[type]} [description]
	 */
	var _getAllName = function() {
		var a = $.map($categoryTab.find("li"), function(item, index) {
			return $(item).data("info");
		});

		// a.push(_selectedCategory.data("info"));
		return $.map(_selectedCategory, function(item, index) {
			return new Array(a.concat($(item).data("info")));
		});
	};
	/**
	 * 切换选项卡
	 */
	var _selTab = function(deep, curr) {

		_selectedCategory = [];
		$addBtn.hide();
		var tmpl = '<li data-index="{deep}" data-widget="tab-item"><a class="hover" href="#none"><em>请选择</em><i> ∨</i></a></li>';
		//删除，并添加新的选项卡
		$categoryTab.find("[data-index]").slice(deep).remove();
		$categoryTab.append(tmpl.ncReplaceTpl({
			deep: deep
		}));
		curr && ($categoryTab.find("[data-index='" + (deep - 1) + "']").data('info', curr).find("em").html(curr.categoryName))
		//隐藏元素
		var a = $categoryItemPanel.find("div[data-category-item!='" + deep + "']");
		a.hide().find("a.curr").removeClass('curr').find("i").removeClass('icon-check').addClass('icon-check-empty');
		$categoryItemPanel.find("div[data-category-item='" + deep + "']").show();

	};
	/**
	 * [renderLi description]
	 * @return {[type]} [description]
	 */
	var renderLi = function(deep, data, curr) {
		var $ul = _getUlByDeep(deep);
		$ul && ($ul.show().find("ul").empty())
		$.each(data, function(i, n) {
			var isHas = tc.has(n.categoryId);
			var $a = $("<a>", {
				"href": "javascript:;",
				id: "categoryItem" + n.categoryId,
				"class": isHas ? elClass.selected : "",
				html: (isHas ? tmpl.iconCheck : tmpl.iconCheckEmpty) + n.categoryName,
			}).data("info", n);
			$ul.find("ul").append($("<li>").append($a));
		});
		_selTab(deep, curr);
	};

	/**
	 * 选择一个
	 * @return {[type]} [description]
	 */
	var selCategory = function($element) {
		// $element.closest('ul').find('.' + elClass.curr).removeClass(elClass.curr);
		//再次点击删除选择
		$element.find('i').remove();
		if ($element.hasClass("curr")) {
			_delSelCategory($element);
			$element.prepend(tmpl.iconCheckEmpty).removeClass('curr');
			return;
		}
		$element.addClass(elClass.curr).prepend(tmpl.iconCheck);

		$addBtn.show();
		//可多选
		_selectedCategory.push($element);
		// console.log("当前已选择",_selectedCategory);
	};
	/**
	 * 删除一个已经选择的
	 */
	var _delSelCategory = function($element) {
		if (!_selectedCategory.length) {
			return;
		}
		$.each(_selectedCategory, function(index, val) {
			if ($(val).is($element)) {
				_selectedCategory.splice(index, 1);
				return;
			}
		});
		if (_selectedCategory.length ===0 ) {
			$addBtn.hide();
		}
		// console.log(_selectedCategory)
	};

	/**
	 * 获取分类数据
	 */
	var getDataJson = function(deep, parentId, curr, $element) {
		$.getJSON(
			ncGlobal.sellerRoot + "category/commission/list.json/" + parentId,
			function(data) {
				if (data.code == "200") {
					data.data.categoryList.length ? (renderLi(deep, data.data.categoryList, curr)) : (selCategory($element))
				}
			}
		);
	};
	/**
	 * 事件
	 * @return {[type]} [description]
	 */
	var bindEvents = function() {
		//点击展开
		$btnBindCategory.click(function(event) {
			event.preventDefault();
			$ncsFreightSelector.addClass('hover');
			_current = $(this).data("info");
		});
		//点击分类后的事件
		$categoryBox.on('click', 'ul a', function(event) {
			event.preventDefault();
			var $this = $(this),
				d = $this.data("info");
			if ($this.hasClass(elClass.selected)) {
				return;
			}
			d && (getDataJson(d.deep, d.categoryId, d, $this))
		});
		//点击切换选项卡
		$categoryTab.on('click', 'li', function(event) {
			var d = $(this).data("info");
			d && (_selTab(d.deep - 1))
		});
		//添加事件
		//editdata 是编辑时的数据
		$addBtn.on('click', function(event, editData) {

			event.preventDefault();
			// var a = tc.add(_getAllName());
			var allData = editData ? editData : _getAllName();
			// console.log("editData is ", editData);
			// console.log('allData',allData);

			for (var i = allData.length - 1; i >= 0; i--) {
				$("#tableCategoryBody").append(tc.add(allData[i]));
			}
			for (var i = _selectedCategory.length - 1; i >= 0; i--) {
				_selectedCategory[i].removeClass(elClass.curr).addClass(elClass.selected);
			}
			_selectedCategory = []; //清空选择的
			$addBtn.hide();
			refreshInput();
		});
		//删除
		$("#tableCategoryBody").on('click', 'a[data-id]', function(event) {
			event.preventDefault();
			var $this = $(this),
				id = $this.data("id"),
				$categoryItem = $("#categoryItem" + id);
			tc.del(id);

			if ($categoryItem.length) {
				$("#categoryItem" + id).removeClass(elClass.selected).find("i").remove();
				$("#categoryItem" + id).prepend(tmpl.iconCheckEmpty);
			}
			$this.closest('tr').remove();
			refreshInput();
		});
	};
	/**
	 * [init description]
	 * @return {[type]} [description]
	 */
	var init = function() {
		getDataJson(0, 0);
	};
	/**
	 * 如果是编辑的时候
	 */
	var bootstarp = function(data) {

		var JsonData = $.parseJSON(data);
		// console.log("原始数据是：", JsonData);
		if (!JsonData.length) {
			return;
		}
		var cData = $.map(JsonData, function(item, index) {
			var r = [];
			$.each(["1", "2", "3"], function(index, val) {
				if (item["categoryId" + val] != '0') {
					r.push({
						categoryId: item["categoryId" + val],
						categoryName: item["categoryName" + val],
						commissionRate: item["commissionRate"]
					});
				}
			});
			// console.log('r array is  ', r);
			return new Array(r);
		});
		$addBtn.trigger('click', [cData]);
		// console.log(cData);
	};
	/**
	 * 选择列表相关
	 * @type {Object}
	 */
	var tc = {
		__selectedList: [],
		__postList: {},
		/**
		 * 是否重复
		 */
		has: function(value) {
			for (var i = tc.__selectedList.length - 1; i >= 0; i--) {
				if (tc.__selectedList[i] == value) {
					return true;
				}
			}
			return false;
		},
		add: function(data) {
			var r = '',
				delBtn;
			for (var i = 0; i <= 2; i++) {
				if (typeof data[i] != "undefined") {
					if (i == data.length - 1) {
						tc.__selectedList.push(data[i].categoryId);
						tc.__postList[data[i].categoryId] = data;
						r += "<td>{categoryName} (分佣比例：{commissionRate}%)</td>".ncReplaceTpl(data[i]);
						delBtn || (delBtn = '<td><a data-id ="{categoryId}" href="javascript:;" class="btn btn-xs btn-danger">删除</a></td>'.ncReplaceTpl(data[i]))
					} else {
						r += "<td>{categoryName} </td>".ncReplaceTpl(data[i]);
					}
				} else {
					r += '<td></td>';
				}
			}

			return '<tr class="store-class-item">' + r + delBtn + '</tr>';
		},
		del: function(categoryId) {
			tc.__postList.hasOwnProperty(categoryId) && (delete tc.__postList[categoryId])
			for (var i = tc.__selectedList.length - 1; i >= 0; i--) {
				if (tc.__selectedList[i] == categoryId) {
					tc.__selectedList.splice(i, 1);
					break;
				}
			}
		}
	};
	//////////////////
	return {
		//初始化
		init: function() {
			init();
			bindEvents();
			//判断是否是编辑
			var $bindCategory = $("#bindCategory");
			if ($bindCategory.length && ($bindCategory.val() != "")) {
				bootstarp($bindCategory.val());
			}
			return this;
		},
		//获取发送的数据
		getPostData: function() {
			return tc.__postList;
		}
	};
}();

//经营类目
var category = Category.init();