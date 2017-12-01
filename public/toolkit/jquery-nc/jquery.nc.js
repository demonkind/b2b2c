Nc = {};
/**
 * 日志输出
 */
//Nc.log = function (){
//	if (window.console && Nc.debugger) {
//		Function.apply.call(console.log, console, arguments)
//	}
//}
/**
 * 执行时间测试
 */
Nc.test = function (fn, param){
	if(typeof performance == 'undefined'){
		return ;
	}
	var s, d;
	// 记录执行的起始时间
	s = performance.now();

	// 执行待测试的方法
	fn(param);
	// 记录执行的结束时间
	d =performance.now();

	// 输出待测试方法所运行的结果和耗时
	console.log( '计算结果：' + '，耗时：'+ parseFloat(d-s).toFixed(3) + '毫秒' );
};
/**
 * 判断是否是数组
 */
Nc.isArray = function(obj) {
	return Array.isArray(obj);
};
/**
 * 判断是否是对象
 */
Nc.isObject = function(obj) {
	var type = typeof obj;
	return type === 'function' || type === 'object' && !!obj;
};
/**
 * 其他类型的判断
 * 'RegExp', 'Error' 这2个没加
 */
['Function', 'String', 'Number', 'Date','Boolean'].forEach(function(value) {
	Nc['is' + value] = function(obj) {
		return Object.prototype.toString.call(obj) === '[object ' + value + ']';
	};
});
/**
 * 是否为空
 */
Nc.isEmpty = function(obj) {
	if(Nc.isBoolean(obj)) return !obj;
	if (obj == null) return true;
	if (Nc.isNumber(obj)) {
		return obj <= 0;
	}
	if (Nc.isArray(obj) || Nc.isString(obj) || (obj instanceof jQuery)) return obj.length === 0;
	return Object.keys(obj).length === 0;
};

/**
 * 是否是正整数
 */
Nc.isDigits =  function(value) {
	return /^\d+$/.test(value);
};
(function() {
	/**
	 * 可用的es5 中的array 方法
	 * 需要在ie7 中加载es5-Shims
	 *
	 *Array.prototype.every
	 *Array.prototype.filter
	 *Array.prototype.forEach
	 *Array.prototype.indexOf
	 *Array.prototype.lastIndexOf
	 *Array.prototype.map
	 *Array.prototype.slice
	 *Array.prototype.some
	 *Array.prototype.sort
	 *Array.prototype.reduce
	 *Array.prototype.reduceRight
	 *Array.prototype.push
	 *Array.prototype.join
	 *Array.isArray
	 */
	/**
	 * 数组排序
	 * @example
	 * var arr = [2, 22,1,111,,22,11,100010,4, 8, 1, 22, 3];
	 *      console.log(arr.ncArraySort())
	 */
	Array.prototype.ncArraySort = function(order) {
		return this.sort(function(a, b) {
			if (!order) {
				return a - b;
			} else {
				return b - a;
			}
		});
	};
	/**
	 * 获取数组最大值
	 */
	Array.prototype.ncArrayMax = function() {
		return this.ncArraySort(1)[0];
	};
	/**
	 * 获取数组最小值
	 */
	Array.prototype.ncArrayMin = function() {
		return this.ncArraySort()[0];
	};

	/**
	 * 数组扩展,及其可以使使用的说明
	 */
	Nc.array = {
		/**
		 * 在在数组中查找值
		 * 这个使用es5的数组原生方法实现
		 * es5-shim 可兼容ie7
		 */
		indexOf: function(array, value, start) {
			var r = array.indexOf(value, start)
			return r === -1 ? false : r;
		},
		/**
		 * 数组排序
		 */
		sort: function(array) {
			return array.length ? array.sort(function(a, b) {
				return a - b;
			}) : false;
		},
		/**
		 * 获取数组交集 可多个
		 * 如果有交集返回交集数组，如果没有就返回空数组
		 */
		intersection: function(array) {
			var result = [];
			var argsLength = arguments.length;
			for (var i = 0, length = array.length; i < length; i++) {
				var item = array[i];
				if (result.indexOf(item) != -1) continue;
				var j;
				for (j = 1; j < argsLength; j++) {
					if (arguments[j].indexOf(item) == -1) break;
				}
				if (j === argsLength) result.push(item);
			}
			return result;
		}
	};
})();
/**
 * 字符传模版扩展
 * @example
 * "12312{id}3".ncReplaceTpl({id:"这是个id"})
 */
(function() {
	var ReplaceTpl = function(str, o, regexp) {
		return str.replace(regexp || /\\?\{([^{}]+)\}/g, function(match, name) {
			return (o[name] === undefined) ? '' : o[name];
		});
	};
	//绑定到jquery 上面去
	$.ReplaceTpl = ReplaceTpl;
	//扩展到 String 对象上面去
	String.prototype.ncReplaceTpl = function(option) {
		return ReplaceTpl(this, option);
	};
})();


/**
 * 字符串转义
 * @return {[type]} [description]
 */
(function() {
	var escapeMap = {
		'&': '&amp;',
		'<': '&lt;',
		'>': '&gt;',
		'"': '&quot;',
		"'": '&#x27;',
		'`': '&#x60;'
	};

	var createEscaper = function(map) {
		var escaper = function(match) {
			return map[match];
		};
		// Regexes for identifying a key that needs to be escaped
		var source = '(?:' + Object.keys(map).join('|') + ')';
		var testRegexp = RegExp(source);
		var replaceRegexp = RegExp(source, 'g');
		return function(string) {
			string = string == null ? '' : '' + string;
			return testRegexp.test(string) ? string.replace(replaceRegexp, escaper) : string;
		};
	};
	Nc.escape = createEscaper(escapeMap);
})();

/**
 * 获取字符串中的数字，不能有小数点
 */
(function() {
	Nc.getNum = function(text) {
		return  text.replace(/[^0-9]/ig, "");
	}
})();
/**
 * 清除所有特殊字符
 */
(function() {
	var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");
	//清除
	function _clean (d) {
		var rs = "";
		for (var i = 0; i < d.length; i++) {
			rs = rs + d.substr(i, 1).replace(pattern, '');
		}
		return rs;
	}
	Nc.stringClean = function (s) {

		//判断类型
		if(Nc.isEmpty(s) || Nc.isFunction(s) || Nc.isNumber(s) || Nc.isDate(s) || Nc.isBoolean(s)){
			return s;
		}
		//如果是对象,或者是数组
		if(Nc.isObject(s) || Nc.isArray(s)){
			for(var p in s){
				s[p] = Nc.stringClean(s[p]);
			}
			return s ;
		}
		//如果是字符串
		if (Nc.isString(s)) return _clean(s)
	}
})();
/**
 * 将一个对象转换成一个url字符串
 */
(function() {
    var __firstFlot = true;
    Nc.urlEncode = function (param, key, encode) {
        if (param == null) return '';
        var paramStr = '';
        var t = typeof (param);
        if (t == 'string' || t == 'number' || t == 'boolean') {
            if(__firstFlot == true){
                paramStr += key + '=' + ((encode == null || encode) ? encodeURIComponent(param) : param);
                __firstFlot = false;
            }else{
                paramStr += '&' + key + '=' + ((encode == null || encode) ? encodeURIComponent(param) : param);
            }
        } else {
            for (var i in param) {
                var k = key == null ? i : key + (param instanceof Array ? '[' + i + ']' : '.' + i);
                paramStr += Nc.urlEncode(param[i], k, encode);
            }
        }
        return paramStr;
    };
})();


(function() {
	Nc.number || (Nc.number = {
		/**
		 * 四舍五入
		 * @param number 数字
		 * @param fractionDigits 保留小数位数
		 * @returns {number}
		 */
		round: function(number, fractionDigits) {
			return Math.round(number * Math.pow(10, fractionDigits)) / Math.pow(10, fractionDigits);
		},

		/**
		 * 加法运算
		 */
		add: function(num1, num2) {
			var baseNum, baseNum1, baseNum2;
			num1 = parseFloat (num1,10);
			num2 = parseFloat (num2,10);
			try {
				baseNum1 = num1.toString().split(".")[1].length;
			} catch (e) {
				baseNum1 = 0;
			}
			try {
				baseNum2 = num2.toString().split(".")[1].length;
			} catch (e) {
				baseNum2 = 0;
			}
			baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
			return (num1 * baseNum + num2 * baseNum) / baseNum;
		},
		/**
		 * 减法运算
		 */
		sub: function(num1, num2) {
			var baseNum, baseNum1, baseNum2;
			var precision; // 精度
			num1 = parseFloat (num1,10);
			num2 = parseFloat (num2,10);
			try {
				baseNum1 = num1.toString().split(".")[1].length;
			} catch (e) {
				baseNum1 = 0;
			}
			try {
				baseNum2 = num2.toString().split(".")[1].length;
			} catch (e) {
				baseNum2 = 0;
			}
			baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
			precision = (baseNum1 >= baseNum2) ? baseNum1 : baseNum2;
			return ((num1 * baseNum - num2 * baseNum) / baseNum).toFixed(precision);
		},
		/**
		 * 乘法运算
		 */
		multi : function(num1, num2) {
			var baseNum = 0;
			num1 = parseFloat (num1,10);
			num2 = parseFloat (num2,10);
			try {
				baseNum += num1.toString().split(".")[1].length;
			} catch (e) {}
			try {
				baseNum += num2.toString().split(".")[1].length;
			} catch (e) {}
			return Number(num1.toString().replace(".", "")) * Number(num2.toString().replace(".", "")) / Math.pow(10, baseNum);
		},
		/**
		 * 除法运算
		 */
		div: function(num1, num2) {
			var baseNum1 = 0,
				baseNum2 = 0;
			num1 = parseFloat (num1,10);
			num2 = parseFloat (num2,10);
			var baseNum3, baseNum4;
			try {
				baseNum1 = num1.toString().split(".")[1].length;
			} catch (e) {
				baseNum1 = 0;
			}
			try {
				baseNum2 = num2.toString().split(".")[1].length;
			} catch (e) {
				baseNum2 = 0;
			}
			with(Math) {
				baseNum3 = Number(num1.toString().replace(".", ""));
				baseNum4 = Number(num2.toString().replace(".", ""));
				return (baseNum3 / baseNum4) * pow(10, baseNum2 - baseNum1);
			}
		}

		/////
	})
	/**
	 * 添加一个 priceFormat 设置成 '&yen;%s' 会出现钱币符号
	 */
	Nc.priceFormat = function (price,priceFormat){
		price = Nc.numberFormat(price, 2);
		return Nc.isEmpty(priceFormat) ? price :priceFormat.replace('%s', price);
	}

	Nc.numberFormat = function (num, ext){
		if(ext < 0){
			return num;
		}
		num = Number(num);
		if(isNaN(num)){
			num = 0;
		}
		var _str = num.toString();
		var _arr = _str.split('.');
		var _int = _arr[0];
		var _flt = _arr[1];
		if(_str.indexOf('.') == -1){
			/* 找不到小数点，则添加 */
			if(ext == 0){
				return _str;
			}
			var _tmp = '';
			for(var i = 0; i < ext; i++){
				_tmp += '0';
			}
			_str = _str + '.' + _tmp;
		}else{
			if(_flt.length == ext){
				return _str;
			}
			/* 找得到小数点，则截取 */
			if(_flt.length > ext){
				_str = _str.substr(0, _str.length - (_flt.length - ext));
				if(ext == 0){
					_str = _int;
				}
			}else{
				for(var i = 0; i < ext - _flt.length; i++){
					_str += '0';
				}
			}
		}

		return _str;
	}
})();
/**
 * 模块方案
 * @return {[type]} [description]
 */
(function(window) {
    var moduleMap = {};
    var fileMap = {};
    var noop = function() {};
    var thin = {
        define: function(name, dependencies, factory) {
            if (!moduleMap[name]) {
                var module = {
                    name: name,
                    dependencies: dependencies,
                    factory: factory
                };

                moduleMap[name] = module;
            }

            //return moduleMap[name];
            // return thin.use(name);
            return thin;
        },
        use: function(name) {
            var module = moduleMap[name];
            if (!module) {
                return;
            }
            if (!module.entity) {
                var args = [];
                for (var i = 0; i < module.dependencies.length; i++) {
                    if (moduleMap[module.dependencies[i]].entity) {
                        args.push(moduleMap[module.dependencies[i]].entity);
                    } else {
                        args.push(thin.use(module.dependencies[i]));
                    }
                }
                module.entity = module.factory.apply(noop, args);
            }
            return module.entity;
        }
    };

    // window.module = thin;
    // window.Nc.module = thin;
    window.Nc || (window.Nc = {})
    window.ncDefine = thin.define;
    window.ncRequire = thin.use;
})(window);
/**
 * 事件管理器
 * @example:
 * 发送事件
 * Nc.eventManger.trigger('specvalue.check', [传递的数据1 , 传递的数据2]);
 * 监听事件
 * Nc.eventManger.on("specvalue.check", function(e, 传递的数据1, 传递的数据2) {....
 */
(function(window) {

    'use strict';
    var EventManger = function() {
        //扩展属性
        // $.extend(this, EventManger.default);
        this.debug = false;
        this.$$list = {};
        this.$this = $(this);
    };
    // EventManger.default = {
    //     //开启调试
    //     debug: false
    // };

    /**
     * 触发事件
     */
    EventManger.prototype.trigger = function(type, data) {
        this.log("trigger event", type, data);
        this.$this.trigger(type, data);
    };
    /**
     * 事件监听
     */
    EventManger.prototype.on = function(types, selector, data, fn) {
        this.log("add Listener ", types, selector, data);
        this.$this.on(types, selector, data, fn);
    };
    /**
     * 监听一次性事件
     */
    EventManger.prototype.one = function(types, selector, data, fn) {
        this.log("trigger event", types, data);
        this.$this.trigger(types, data);
    };
    /**
     * 输出日志
     * @return {[type]} [description]
     */
    EventManger.prototype.log = function() {
        if (window.console && this.debug) {
            Function.apply.call(console.log, console, arguments)
        }
    };
    window.Nc || (window.Nc = {})
    window.Nc.eventManger || (window.Nc.eventManger = new EventManger);
    //模块管理
    ncDefine("nc.eventManger", [], function() {
        return Nc.eventManger;
    });
}(window));
/**
 * 数据中心,数据缓存
 * @example
 *   var nnn = ncData.sub("set", "a", function(key, value) {
  *      console.log(key + value);
  *  });
 *  var a = {
  *      a: 1,
  *      b: 2
  *  };
 *  ncData.set("a", 111);//修改值
 *  ncData.get("a");//获取值
 *  ncData.unsub("set","a",nnn);//解绑监听
 */
(function ($, win) {
    "use strict";
    var until = {
        parseKey: function (key) {
            return key.split('.');
        },
        isFn: function (fn) {
            return toString.call(fn) === "[object Function]";
        },
        isObj: function (obj) {
            return toString.call(obj) === "[object Object]";
        },
        /**
         * 深拷贝
         */
        deepCopy: function (o) {
            if (o instanceof Array) {
                var n = [];
                for (var i = 0; i < o.length; ++i) {
                    n[i] = until.deepCopy(o[i]);
                }
                return n;

            } else if (o instanceof Object) {
                var n = {};
                for (var i in o) {
                    n[i] = until.deepCopy(o[i]);
                }
                return n;
            } else {
                return o;
            }
        }
    };


    var ncData = function () {
        this.$$list = {};
        this._events = {
            'set': {}, //修改
            'delete': {}, //删除
            'add': {} //TODO：新增
        };
        this.elid = 0;
    };
    ncData.prototype = {
        has: function (key) {
            return typeof this.$$list[key] === 'undefined' ? false : true;
        },
        /**
         * 设置一个值
         * @param {[type]} key [description]
         * @param {[type]} val [description]
         */
        set: function (key, val) {
            if (typeof key !== 'string') {
                throw "set 必须是字符串";
            }
            var _t = {},
                fn,
                setEvents = this._events.set;

            this.$$list[key] = until.deepCopy(val);

            fn = setEvents[key];

            fn && function () {
                $.each(fn, function (index, el) {
                    el(key, val);
                });
            }();

            return true;
        },
        get: function (key) {
            if (typeof key !== 'string') {
                throw "sub [key]必须是字符串 ";
            }
            return this.$$list[key] ? (until.deepCopy(this.$$list[key])) : false;
        },
        sub: function (type, key, callback) {
            if (typeof type !== 'string' || typeof key !== 'string') {
                throw "sub [type,key]必须是字符串 ";
            }

            var events = this._events[type] || {};

            events[key] = events[key] || {};

            events[key][this.elid++] = callback;

            return this.elid - 1;
        },
        unsub: function (type, key, id) {
            if (typeof type !== 'string' || typeof key !== 'string') {
                throw "sub [key，type]必须是字符串 ";
            }

            var events = this._events[type] || {};

            if (!until.isObj(events[key])) {
                return false;
            }

            if (typeof id !== 'number') {
                delete events[key];
                return true;
            }

            delete events[key][id];

            return true;
        }


    };
    win.ncData = win.ncData || (new ncData)
    Nc.deepCopy = until.deepCopy;
}(jQuery, window));
/**
 * 弹出错误提示框,3秒消失
 * @param msg [ 提示信息 ]
 */
Nc.alertError = function(msg, option) {
    var _m = Nc.isEmpty(msg) ? "操作失败" : msg;
    return layer.alert(_m, $.extend({
        icon: 0,
        time: 3000,
        iconOut: true
    }, option));
};

/**
 * 弹出成功提示框,3秒消失
 * @param msg [ 提示信息 ]
 */
Nc.alertSucceed = function(msg, option) {
    var _m = Nc.isEmpty(msg) ? "操作成功" : msg;
    return layer.alert(_m, $.extend({
        icon: 1,
        time: 3000,
        iconOut: true
    }, option));
};
/**
 *  loading 加载框
 * @param elementText 需要附着的 html 元素
 * @param option layer 参见layer 原生设置
 * @returns {*}
 * @example
 * 显示加载框 ：var a = Nc.loading("#xxxxx");
 * 关闭一个： layer.close(a);
 * 关闭全部： layer.closeAll('loading');
 */
Nc.loading = function(elementText, option) {
    var _d = {
            loadHandle: elementText,
            shade: [0.8, '#ffffff']
        },
        setting = option ? $.extend(_d,option) : {
            loadHandle: elementText,
            shade: [0.8, '#ffffff']
        };
    return layer.load(0, setting);
};
 /**
  * 对layer询问框的封装
  * sizeEnum: 窗体的大小，分为3种 large，small, extraSmall
  */
 Nc.layerConfirm = function(title, option,yes, cancel) {

     undefined!= option && option.sizeEnum !=undefined && (option.skin = "layer-" + option.sizeEnum)
     var options = $.extend({
         shade: 0.5,
         skin: "layer-small",
         //area: '600px',
         btn: ['确认提交', '放弃操作']
     }, option);
     //问题提示
     var _title = '<div class="nc-layer-confirm">' + (title ? title : "是否进行操作") + '</div>'

     if (!options.yes && options.postUrl) {
         options.yes = function(index, layero) {
             $.post(
                 options.postUrl,
                 options.postData,
                 function(data) {
                     if (data.code == '200') {
                         data.url && (window.location = data.url)
                     } else {
                         Nc.alertError(data.message ? data.message : "操作失败");
                     }
                     location.reload();
                 }).error(function() {
                 Nc.alertError("连接超时");
             });
         };
     }
     layer.confirm(_title, options,yes, cancel);
 };
/**
 * 对话框的封装
 * @param option
 * @example
 * 新增了2个参数：
 *  1.$form： form 是需要提交的表单 ，设置后窗体中点击提交后该form自动submit
 *  2.sizeEnum ： 窗体的大小，分为3种 large，small, extraSmall
 *  3.async :true 默认是false 如果true 就将异步提交表单
 */

(function (Nc) {
    Nc.__isPostFlat = true;

    Nc.layerOpen = function (option) {
        option.sizeEnum && (option.skin = "layer-" + option.sizeEnum)


        var options = $.extend({
            skin: "layer-small",
            type: 1,
            shade: 0.5,
            title: "",
            btn: ['确认提交', '放弃操作'],
            //是否异步提交表单
            async: false,
            asyncExtData: {},
            //form内容压缩方式,如果设置成true，重名name 的inpupt 将变成[]格式,
            //如果是false 就直接用jquery 自带的压缩方法
            objSerializeType: true,
            //提交成功后只删除layer，不跳转
            closeLayer:false
        }, option);

        if (!options.yes && options.$form) {
            options.yes = function (index, layero) {
                if (options.async === true) {
                    asyncSubmit(options.$form, options);
                } else {
                    options.$form.submit();
                }
            };
        }
        return layer.open(options);
    };
    ///
    /**
     * 异步提交表单
     */
    var asyncSubmit = Nc.asyncSubmit = function ($form, options) {
        //判断是否有验证
        if ( ($.fn.hasOwnProperty("valid") && (!$form.valid()) ) || Nc.__isPostFlat == false) {
            return;
        }
        Nc.__isPostFlat = false;
        var attrs = $form.ncAttrs(),
            postData = options.objSerializeType ? ($.extend($form.serializeObject(), options.asyncExtData)) : ($form.serialize());
        $.ajax({
            url: attrs.action,
            type: attrs.method.toUpperCase(),
            dataType: 'json',
            data: postData
        })
            .done(function (req) {
                if (req.code == '200') {
                    Nc.alertSucceed(req.message ? req.message : "操作成功", {
                        icon: 1,
                        time: 1500,
                        end: function () {
                            if(options.closeLayer){
                                layer.closeAll()
                            }else{
                                req.url ? (window.location = req.url) : (location.reload())
                            }
                        }
                    });
                    //发送一个成功事件
                    $form.trigger("nc.done",[req]);
                } else {
                    Nc.alertError(req.message ? req.message : "操作失败", {
                        time: 1500
                    });
                    //发送失败事件
                    $form.trigger("nc.fail",[req]);
                }
                Nc.__isPostFlat = true;
            })
            .fail(function () {
                Nc.alertError("连接失败", {
                    time: 1500
                });
                Nc.__isPostFlat = true;
                $form.trigger("nc.fail",[req]);
            });
    };


})(Nc);
/**
 * ajax submit 插件
 */
var __ajaxFlat = true;
(function($) {
    var ajaxSubmit = function(element) {
        this.$el = $(element);
        //判断正在提交
        this.laoding = 1;
        this.layer = '';
        this.btnOldHtml = "";

        this.action = this.$el.attr("action");
        this.method = this.$el.attr("method") ? this.$el.attr("method").toUpperCase() : 'POST';
        this.postData = this.$el.serialize();
        //启动
        this._ajax();

    };
    ajaxSubmit.prototype._ajax = function() {
        var that = this;
        if (!this.laoding || !__ajaxFlat) {
            return;
        }
        this._buttonSubmit();
        __ajaxFlat = false;
        $.ajax({
            url: that.action,
            dataType: "json",
            data: that.postData,
            type: that.method,
            success: function(req) {
                if ('200' == req.code) {
                    var successMessage = '操作成功';
                    if (that._hasEvent('ajaxSubmit.success')) {
                        //发出一个自定义事件
                        that.$el.triggerHandler("nc.ajaxSubmit.success",req);
                    } else {
                        //关闭其它框
                        /*layer.alert(successMessage, {
                            icon: 1,
                            time: 3000,
                            end: function() {
                                req.url && Nc.go(req.url)
                            }
                        });*/
                        Nc.alertSucceed( req.message,{
                            end: function() {
                                req.url && Nc.go(req.url)
                            }
                        })
                    }
                } else {
                    errorMessage = req.message ? req.message : "请求失败";
                    //判断是否绑定了事件
                    if (that._hasEvent('ajaxSubmit.error')) {
                        that.$el.triggerHandler("nc.ajaxSubmit.error", errorMessage);
                    } else {
                        Nc.alertError(errorMessage,{
                            end: function() {
                                req.url && Nc.go(req.url)
                            }
                        });
                    }
                }
                that._buttonSubmit(1);
                __ajaxFlat = true;
            },
            error: function() {
                that._buttonSubmit(1);
                Nc.alertError("请求失败");
                __ajaxFlat = true;
            }
        });


    };
    /**
     * 判断当前form 中是否绑定了事件
     * @param  {[type]}  eventName [description]
     * @return {Boolean}           [description]
     */
    ajaxSubmit.prototype._hasEvent = function(eventName) {
        var that = this,
            events = $._data(this.$el[0], "events"),
            ncEvents,
            result = false;
        if (typeof events == 'undefined' || !events.hasOwnProperty('nc')) {
            return false;
        }
        $.each(events.nc, function() {
            if (this.namespace == eventName) {
                result = true;
                return;
            }
        });
        return result;

    };
    /**
     * 按钮锁死
     * @private
     */
    ajaxSubmit.prototype._buttonSubmit = function(open) {

        this.layer || (this.layer = $(".layui-layer-btn0"));
        if (!this.layer.length) {
            return;
        }
        if (!open) {
            this.oldHtml = this.layer.html();
            this.layer.addClass("disabled").html("loading...");
            this.laoding = 0;

        } else {
            this.layer.html(this.oldHtml);
            this.layer.removeClass("disabled");
            this.laoding = 1;
        }
    };

    function Plugin(option) {
        return this.each(function() {
            new ajaxSubmit(this);
        });
    }
    $.fn.ajaxSubmit = Plugin;
    $.fn.ajaxSubmit.Constructor = ajaxSubmit;

})(jQuery);

/**
 * 地区多级联动
 */
(function($ , win) {
    'use strict';
    var NcArea = {};
    NcArea = function(element, option) {
        var
            that = this;
        //保存隐藏input
        this.inputList = [];
        this.$element = $(element);

        //loading
        this.$loading ={};
        //
        this.loadingCount = true ;
        //获取id
        this.$elementId = this.$element.attr("id");
        //获取元素上的初始地区文本和id
        var dataAreaText = this.$element.data('area-text');
        var dataAreaId = this.$element.data('area-id');
        dataAreaText && (option.initAreaText = dataAreaText);
        dataAreaId && (option.areaModBtnAreaId = dataAreaId);
        this.options = $.extend({}, optionsDefault, option);
        //生成隐藏input
        $.each(this.options.hiddenInput, function(i, n) {
            that.inputList.push($("<input>", {
                name: n.name,
                'value': n.value,
                id: n.id ? n.id : '',
                type: "hidden"
            }));
        });
        //生成隐藏deep input
        if (this.options.deepInputAddType == "mod") {
            for (var i = 1; i <= this.options.deep; i++) {
                if (!this.$element.find('input[data-deep="' + i + '"]').length) {
                    this.$element.prepend('<input data-deep="' + i +
                        '" type="hidden" name="' + that.options.dataHiddenName + area_deep_id  + '" value="0"  />'
                    );
                }

            }
        }

        //保存每次ajax 过来的数据
        this.it = {};
        //保存选择的内容
        this.selit = {};
        this.ajaxFlat = true;
        if (this.$element.find('select').length == 0) {
            //如果下面没有select 元素就初始化一下
            this.options.current > 0 ? (this.addAreaSelect(this.options.current)) : (this.init())
        }

        
        //初始化事件
        this._initEvent();
    };
    //设置
    var optionsDefault = {
        //编辑按钮的html
        areaModBtnHtml: '<a href="javascript:void(0)" class="btn btn-xs btn-default vm m-l-5"><i class="icon icon-edit"></i>编辑</a>',
        //编辑按钮及地区名称包裹的html
        areaModBtnWarp: '<div class="text-box"></div>',
        //初始化的地区id
        areaModBtnAreaId: 1,
        //初始化地区文本，如果不为空的话就会出现一个编辑按钮
        initAreaText: '',
        //生成的hidden input ，第一个是保存最后选择的地区id，第二个是所有地区信息的文本
        hiddenInput: [{
            name: "areaId",
            value: "0",
            id: "areaId"
        }, {
            name: "areaInfo",
            value: '',
            id: "areaInfo"
        }],

        //是否发送事件
        sendEvent: true,
        //分类深度
        showDeep: 3,
        //地区请求地址
        url: ncGlobal.sellerRoot + 'area/list.json/',
        //select 模板
        selectTpl: '<select id="{id}">{content}</select>',
        //option 元素模板
        optionTpl: ' <option value="{key}">{value}</option>',
        //数据格式
        dataFormat: "areaList",
        //key
        dataIdFormat: "areaId",
        //value
        dataNameFormat: "areaName",
        dataHiddenName: "area_",
        //设置当前深度
        current: 0,
        //mod|clear
        deepInputAddType: "mod",
        //是否显示loading
        showLoading:""

    };
    /**
     * 初始化，没有发现有select
     */
    NcArea.prototype = {
        /**
         * 重新启动
         * 使用举例：$("#goods_class").data("nc.category").restart()
         */
        restart: function() {
            var that = this;
            //当前深度
            this.$$area_deep_id = 0;
            //当前 select 选择的 key
            this.$$area_sel_id = 0;
            //当前 select 选择的 value
            this.$$area_sel_html = 0;

            this.loadingCount = true;
            this.$loading = {};
            
            var dataAreaText = this.$element.attr('data-area-text');
            var dataAreaId = this.$element.attr('data-area-id');
            dataAreaText && (this.options.initAreaText = dataAreaText);
            dataAreaId && (this.options.areaModBtnAreaId = dataAreaId);
            this.options = $.extend({}, optionsDefault, this.options);
            $.each(this.inputList, function(index, el) {
                var inputValue = that.options.hiddenInput[index];
                el.val(inputValue.value);
            });
            this.$element.find(":not(input)").remove()
                //判断初始化类型
            if (this.options.initAreaText) {
                //生成一个有地区信息的区域
                this._initAreaByAreaText();
            } else {
                //生成一个 深度为 0 的select
                this.addAreaSelect(0);
            }
        },

        _setSelIt: function(deep, ob) {
            for (var i = 10; i >= deep; i--) {
                delete this.selit[i];
            }
            'undefined' != typeof ob && (this.selit[deep] = ob);
        },
        _setIt: function(ob) {
            this.it[this.$$area_deep_id] = ob;
        },
        _getInfo: function(deep, value) {
            var d = this.it[deep],
                r;
            if ("undefined" != typeof d) {
                $.each(d, function(i, n) {
                    n.areaId == value && (r = n)
                })
            }
            return r;
        },
        //获取已选择层级的地区拼接信息
        getAllName: function() {
            var result = [];
            $.each(this.getAll(), function(i, n) {
                result.push(n.areaName);
            })
            return result.join(" ");
        },
        /**
         * 获取最后一个对象
         */
        getLast: function() {
            for (var i = 10; i >= 0; i--) {
                if ('undefined' != typeof this.selit[i]) {
                    return this.selit[i];
                }
            }
        },
        /**
         * 获取全部已选择信息
         */
        getAll: function() {
            return this.selit;
        },
        /**
         * 获取指定层级的信息
         */
        getInfoFormLevel: function(id) {
            return this.selit[id];
        },
        /**
         * 第一次加载
         */
        init: function() {
            //添加隐藏input
            this.$element.append(this.inputList);

            //当前深度
            this.$$area_deep_id = 0;
            //当前 select 选择的 key
            this.$$area_sel_id = 0;
            //当前 select 选择的 value
            this.$$area_sel_html = 0;

            //判断初始化类型
            if (this.options.initAreaText) {
                //生成一个有地区信息的区域
                this._initAreaByAreaText();
            } else {
                //生成一个 深度为 0 的select
                this.addAreaSelect(0);
            }
        },
        /**
         * 添加一个有地址信息的按钮区域
         * @private
         */
        _initAreaByAreaText: function() {

            var that = this,
                _options = this.options;

            //声明一个按钮组件
            var modBtn = $(_options.areaModBtnHtml)
                .on("click", function(e) {
                    //删除手动填写的deep input
                    var a = that.$element.find("input[data-deep]");
                    a.length &&_options.deepInputAddType == "clear" && a.remove()
                    that._removeSelect(0)
                    $areaModBtnWarp.remove();
                    //为两个隐藏域重新赋值
                    that._resetHiddenInput();
                    that.addAreaSelect(0);
                });

            var $areaModBtnWarp = $(_options.areaModBtnWarp)
                .html(_options.initAreaText)
                .append(modBtn);
            this.$element.append($areaModBtnWarp);
            //修改隐藏input的值
            this._setHiddenInput(_options.areaModBtnAreaId, _options.initAreaText);
        },
        /**
         * 将2个隐藏的input 恢复默认值
         * @return {[type]} [description]
         */
        _resetHiddenInput: function() {
            var _oldValue = this.options.hiddenInput,
                that = this;
            var valueList = $.map(_oldValue, function(n) {
                return n.value;
            });
            valueList.length && (this._setHiddenInput(valueList[0], valueList[1]));
        },
        /**
         * 添加select 元素
         */
        addAreaSelect: function(area_id) {

            var //需要添加元素的html
                html,
                //this 转换
                that = this; 

            if (this.ajaxFlat === false) { return; }else{ this.ajaxFlat = false;}
            //Nc.isEmpty(this.options.showLoading) || (this.$loading =  Nc.loading( this.options.showLoading ));
            if(!Nc.isEmpty(this.options.showLoading) && !this.loadingCount){
                this.$loading =  Nc.loading( this.options.showLoading )
            }
            this.loadingCount = false;
            //判断是否超出限制
            if (this.options.showDeep <= this.$$area_deep_id) {
                //发送末选事件
                this._sendLastCallBack(area_id, this.$$area_sel_html, this.$$area_deep_id);
                return;
            }
           
           
            //请求数据
            $.getJSON(this.options.url + area_id, {}, function(json) {
                //判断返回的数据

                json = json.data;
                var returnFlat = that.options.dataFormat;
                if ('undefined' === json || 'undefined' === json[returnFlat] ||
                    0 == json[returnFlat].length) {
                    //进入为空处理
                    that._sendLastCallBack(area_id, that.$$area_sel_html, that.$$area_deep_id);
                    return;
                }
                //封装数据
                var returnData = json[that.options.dataFormat];
                html = that._buildHtml(returnData);
                    //添加元素
                that.$element.append(html);


                that._setIt(returnData);

                //标签上绑定数据
                $("#" + that.$elementId + "_nc_select_" + that.$$area_deep_id)
                    .data("area_deep_id", that.$$area_deep_id);

                that.ajaxFlat= true;

                !Nc.isEmpty(that.options.showLoading) && !Nc.isEmpty(that.$loading)&& (layer.close(that.$loading));
            });
        },
        //创建一个select
        _buildHtml: function(list) {
            var result = '',
                that = this;

            //添加第一个
            result += this.options.optionTpl.ncReplaceTpl({
                key: '',
                value: "请选择"
            })

            $.each(list, function(i, n) {
                var nk = 'undefined' != typeof n[that.options.dataIdFormat] ? n[that.options.dataIdFormat] : 0,
                    nn = 'undefined' != typeof n[that.options.dataNameFormat] ? n[that.options.dataNameFormat] : 0,
                    dp = 'undefined' != typeof n.deep ? n.deep : 0;
                result += that.options.optionTpl.ncReplaceTpl({
                    key: nk,
                    value: nn,
                    deep: dp
                })
            })
            result = this.options.selectTpl.ncReplaceTpl({
                id: this.$elementId + "_nc_select_" + this.$$area_deep_id,
                content: result
            })
            return result;
        },
        /**
         * 初始化事件(重要)
         * @private
         */
        _initEvent: function() {
            var that = this
                //每个select 绑定事件
            this.$element.on("change", "select", function() {
                var selectThis = $(this),
                    id = selectThis.val(),
                    area_deep_id = selectThis.data("area_deep_id"),
                    hn = that.options.dataHiddenName + (area_deep_id + 1),
                    _id;
                if(!that.ajaxFlat){
                    return ;
                }
                //当前的选择key 修改
                that.$$area_sel_id = id;

                //修改选择数据
                that._setSelIt(area_deep_id, that._getInfo(area_deep_id, id));

                //清除数据
                that._removeSelect(area_deep_id + 1);

                //
                //添加input
                _id = id || 0;
                that._addDeepInput(area_deep_id, hn, _id);
                // that.$element.prepend('<input data-deep="' + area_deep_id +
                //     '" type="hidden" name="' + hn + '" value="' + _id + '"  />'
                // )


                //如果当前选择的是空的（像请选择）
                if ('' == id || 'undefined' == id) {
                    //发送末选事件
                    that._sendLastCallBack();
                    return
                }

                //发送选择回调事件
                that._sendCallBack();
                //修改当前选择的深度
                that.$$area_deep_id = area_deep_id + 1;
                //添加一个新的select
                that.addAreaSelect(id)
            })
        },
        /**
         * 清除select
         * @private
         */
        _removeSelect: function(sliceNum) {
            var that = this,
                nh = this.$element.find("input[data-deep]");
            if (nh.length > 0) {
                $.each(nh, function(i, n) {
                    $(n).attr("data-deep") >= sliceNum - 1 && (that._delDeepInput($(n)))
                })
            }
            //删除元素并删除事件
            this.$element.children().not("input").slice(sliceNum, 99).remove();
        },

        /**
         * 修改隐藏input 中的值
         * @param areaId
         * @param areaText
         * @private
         */
        _setHiddenInput: function(areaId, areaText) {
            this.inputList[0].val(areaId);
            this.inputList[1].val(areaText);
        },
        /**
         * 发送选择后的回调
         * @private
         */
        _sendCallBack: function() {
            //修改隐藏input的值
            this.inputList[0].val(this.getLast().areaId);
            this.inputList[1].val(this.getAllName());
            this.ajaxFlat = true;
            !Nc.isEmpty(this.options.showLoading) && !Nc.isEmpty(this.$loading) && (layer.close(this.$loading ));
            //默认回调
            if (this.options.sendEvent == true) {
                this.$element.triggerHandler("nc.select.selected", [this]);
            }
        },
        /**
         * 发送为空后的回调
         * @private
         */
        _sendLastCallBack: function() {
            //修改隐藏input的值
            var lastInfo = this.getLast();
            var _o = this.options;
            this.inputList[0].val(lastInfo ? lastInfo.areaId : (_o.hiddenInput[0].value));
            this.inputList[1].val(lastInfo ? this.getAllName() : (_o.hiddenInput[1].value));
            this.ajaxFlat = true;
            !Nc.isEmpty(this.options.showLoading) &&!Nc.isEmpty(this.$loading) && (layer.close(this.$loading ));
            //执行默认
            if (this.options.sendEvent == true) {
                this.$element.triggerHandler("nc.select.last", [this]);
            }
        },

        _addDeepInput: function(deep, name, value) {
            if (this.options.deepInputAddType == "clear") {
                this.$element.prepend('<input data-deep="' + deep +
                    '" type="hidden" name="' + name + '" value="' + value + '"  />'
                );
            } else if (this.options.deepInputAddType == "mod") {
                this.$element.find('input[data-deep="' + deep + '"]').val(value);
            }
        },
        _delDeepInput: function($el) {
            if (this.options.deepInputAddType == "clear") {
                $el.remove();
            } else if (this.options.deepInputAddType == "mod") {
                $el.val(0);
            }

        }
    }

    //多转单
    function Plugin(option) {
        return this.each(function() {
            var $this = $(this);
            var data = $this.data('nc.area');
            if (!data) $this.data('nc.area', (data = new NcArea(this, option)))
        })
    }

    //jquery 绑定
    $.fn.NcArea = Plugin
})(jQuery,window);
/**
 * 浏览历史
 */
(function($) {
    var GoodsBrowse = function(element, option) {
        /*初始化元素*/
        this.$element = $(element);
        this.options = $.extend({}, GoodsBrowse.setting, option);
        this.init();
    };
    /**
     * 默认设置
     * @type {{}}
     */
    GoodsBrowse.setting = {
        showNum: 4
    };
    GoodsBrowse.prototype = {
        init: function() {
            var that = this;
            //加载浏览记录
            that._loadGoodsBrowse(true);
        },
        /**
         * 加载浏览记录
         */
        _loadGoodsBrowse: function() {
            var that = this;
            $.ajax({
                url: ncGlobal.webRoot + 'goods/browse/list',
                success: function(html) {
                    that.$element.html(html);
                    var num = $('#goodsBrowseList').find("li").length;
                    (num > 0 && num > that.options.showNum) &&  $('#goodsBrowseList').bxSlider({
                        infiniteLoop: true,
                        hideControlOnEnd: true,
                        hideHoverControls:true,
                        minSlides: 0,
                        maxSlides: 5,
                        slideWidth: 240,
                        slideMargin: 0
                    });
                }
            });
        }
    }
    //多转单
    function Plugin(option) {
        return new GoodsBrowse(this, option);
    }
    //jquery 绑定
    $.fn.ncGoodsBrowse = Plugin;
})(jQuery);
/**
 * 列表中的全选
 * @example
 *
 * 设置
 * //全选
 * <input type="checkbox" id="all" nc-check-all>
 * //跟随着变动
 * <input type="checkbox" nc-check-item>
 */
(function($) {
    var __postFlat = true;
    var postCheck = function(url, data) {
        if(__postFlat == false) {
            return;
        }
        __postFlat = false;
        $.post(url, data, function(xhr) {
            if(xhr.code == 200) {
                Nc.go(xhr.url);
            } else {
                Nc.alertError(xhr.message);
            }
            __postFlat = true;
        }, "json").fail(function() {
            Nc.alertError("连接超时");
            __postFlat = true;
        });
    }
    $(function() {

        $("body")
            .on("click", "input:checkbox[nc-check-all]", function(event) {
                //event.preventDefault();
                var $this = $(this),
                    isCheck = $this.is(':checked'),
                    $itemAll = $("input:checkbox[nc-check-all]"),
                    $item = $("input:checkbox[nc-check-item]")
                    ;
                if($item.length && isCheck) {
                    $itemAll.attr("checked", true);
                    $item.attr("checked", true);
                } else {
                    $itemAll.removeAttr("checked");
                    $item.removeAttr("checked")
                }
            })
            //批量操作的post地址
            .on("click", '[nc-check-url]', function(event) {
                event.preventDefault();
                var $this = $(this),
                    url = $this.attr("nc-check-url"),
                    inputList = $("input:checkbox[nc-check-item]:checked"),
                    title = $this.attr("nc-check-confirm-title"),
                    isConfirm = $this.attr("nc-check-confirm"),
                    _d = ''
                    ;
                //获取选择的信息
                if(!inputList.length) {
                    Nc.alertError("请选操作项");
                    return;
                }
                $.each(inputList, function(i, n) {
                    _d += (i != 0 ? "&" : '') + $(n).attr("name") + "=" + $(n).attr("value");
                })


                if(Nc.isEmpty(isConfirm) || isConfirm == "false") {
                    postCheck(url, _d);
                } else {
                    Nc.layerConfirm(title ? title : '', {
                        yes: function() {
                            postCheck(url, _d);
                        }
                    })
                }

            })
            .on("click", '[nc-check-open]', function(event) {
                event.preventDefault();
                var $this = $(this),
                    inputList = $("input:checkbox[nc-check-item]:checked"),
                //打开对话框的html id
                    $openElement = $($this.attr("nc-check-open")),
                    openTitle = $this.attr("nc-check-title"),
                    _d = ''
                    ;
                //获取选择的信息
                if(!inputList.length) {
                    Nc.alertError("请选操作项");
                    return;
                }
                $.each(inputList, function(i, n) {
                    _d += (i != 0 ? "&" : '') + $(n).attr("name") + "=" + $(n).attr("value");
                })
                if($openElement.length) {
                    //console.log("模态对话框");
                    Nc.layerOpen({
                        type: 1,
                        title: !Nc.isEmpty(openTitle) ? openTitle : "查看",
                        content: $openElement,
                        btn: ["确认提交","放弃操作"],
                        yes: function() {
                            if(__postFlat == false) {
                                return;
                            }
                            __postFlat = false;
                            //console.log("确认提交");
                            var $form = $openElement.find("form"),
                                _formUrl = $form.attr("action")
                                ;

                            try{
                                //console.log("验证")
                               if(!$form.valid()){
                                    return
                               }
                            }catch(e){

                            }
                            if(!$form.length) {
                                throw new Error("没有找到需要提交的form")
                            }
                            if(Nc.isEmpty(_formUrl)) {
                                throw new Error("form 上没有填写action")
                            }
                            //console.log("form", $form);
                            //重新写数据
                            _d += "&" + $form.serialize();
                            //console.log("data is ", _d);
                            $.post(
                                _formUrl,
                                _d,
                                function(xhr) {
                                    if(xhr.code == 200) {
                                        Nc.alertSucceed(xhr.message, {
                                            end: function() {
                                                layer.closeAll();
                                                Nc.go(xhr.url);
                                            }
                                        });
                                    } else {
                                        Nc.alertError(xhr.message);
                                    }
                                }, "json"
                            )
                                .always(function() {
                                    __postFlat = true;
                                })
                                .error(function() {
                                    Nc.alertError("连接超时");
                                });
                        }
                    });
                }
            })
        ;
    });
})(jQuery);
(function (factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD (Register as an anonymous module)
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        // Node/CommonJS
        module.exports = factory(require('jquery'));
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ($) {

    var pluses = /\+/g;

    function encode(s) {
        return config.raw ? s : encodeURIComponent(s);
    }

    function decode(s) {
        return config.raw ? s : decodeURIComponent(s);
    }

    function stringifyCookieValue(value) {
        return encode(config.json ? JSON.stringify(value) : String(value));
    }

    function parseCookieValue(s) {
        if (s.indexOf('"') === 0) {
            // This is a quoted cookie as according to RFC2068, unescape...
            s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
        }

        try {
            // Replace server-side written pluses with spaces.
            // If we can't decode the cookie, ignore it, it's unusable.
            // If we can't parse the cookie, ignore it, it's unusable.
            s = decodeURIComponent(s.replace(pluses, ' '));
            return config.json ? JSON.parse(s) : s;
        } catch(e) {}
    }

    function read(s, converter) {
        var value = config.raw ? s : parseCookieValue(s);
        return $.isFunction(converter) ? converter(value) : value;
    }

    var config = $.cookie = function (key, value, options) {

        // Write

        if (arguments.length > 1 && !$.isFunction(value)) {
            options = $.extend({}, config.defaults, options);

            if (typeof options.expires === 'number') {
                var days = options.expires, t = options.expires = new Date();
                t.setMilliseconds(t.getMilliseconds() + days * 864e+5);
            }

            return (document.cookie = [
                encode(key), '=', stringifyCookieValue(value),
                options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
                options.path    ? '; path=' + options.path : '',
                options.domain  ? '; domain=' + options.domain : '',
                options.secure  ? '; secure' : ''
            ].join(''));
        }

        // Read

        var result = key ? undefined : {},
        // To prevent the for loop in the first place assign an empty array
        // in case there are no cookies at all. Also prevents odd result when
        // calling $.cookie().
            cookies = document.cookie ? document.cookie.split('; ') : [],
            i = 0,
            l = cookies.length;

        for (; i < l; i++) {
            var parts = cookies[i].split('='),
                name = decode(parts.shift()),
                cookie = parts.join('=');

            if (key === name) {
                // If second argument (value) is a function it's a converter...
                result = read(cookie, value);
                break;
            }

            // Prevent storing a cookie that we couldn't decode.
            if (!key && (cookie = read(cookie)) !== undefined) {
                result[name] = cookie;
            }
        }

        return result;
    };

    config.defaults = {};

    $.removeCookie = function (key, options) {
        // Must not alter options, thus extending a fresh object...
        $.cookie(key, '', $.extend({}, options, { expires: -1 }));
        return !$.cookie(key);
    };

}));
/**
 * dropdown-toggle
 */
(function($) {
    'use strict';
    var DropDown = function(element, option) {
        //扩展属性
        // $.extend(this, DropDown.default);
        //点击显示的按钮
        this.$el = $(element);
        this.showClass = "right";
        this.$showElement = this.$el.siblings("[data-dropdown-menu]");

        if (this.$showElement.length === 0) {
            return;
        }

        //bind event 
        this._bindEvent();

    };
    DropDown.prototype._bindEvent = function() {
        var that = this;
        //
        this.$el.on("click", function(e) {
            var ariaExpanded = that.$el.attr("aria-expanded");
            if (ariaExpanded == "true") {
                that.$el.attr('aria-expanded', "false");
                that.$showElement.removeClass(that.showClass);
            } else {
                that.$el.attr('aria-expanded', "true");
                that.$showElement.addClass(that.showClass);
            }
        });
        //
        $(document).mouseup(function(e) {
            var ariaExpanded = that.$el.attr("aria-expanded");
            if (!that.$showElement.is(e.target) && that.$showElement.has(e.target).length === 0 && !that.$el.is(e.target) && that.$el.has(e.target).length === 0 && ariaExpanded == "true") {
                that.$showElement.removeClass(that.showClass);
                that.$el.attr('aria-expanded', "false");
            }
        });
    };

    /**
     * 设置
     * @type {Object}
     */
    // DropDown.default = {
    //     showClass: "right"
    // };

    function Plugin(option) {
        return this.each(function() {
            new DropDown(this, option);
        });
    }
    $.fn.ncDropdown = Plugin;
    $.fn.ncDropdown.Constructor = DropDown;

})(jQuery);
/**
 * 原生js 写的返回顶部
 * @param   btnId     [按钮id]
 * @param   elementId [父级区域的id]
 * @return          [description]
 */
Nc.backTop = function(btnId, elementId) {
	var btn = document.getElementById(btnId);
	var $win = document.getElementById(elementId) ? document.getElementById(elementId) : window;
	var $body = document.getElementById(elementId) ? document.getElementById(elementId) : document.body;
	var scrollTop = $body.scrollTop;
	$win.onscroll = set;
	btn.onclick = function() {

		btn.style.display = "none";
		$win.onscroll = null;
		this.timer = setInterval(function() {
			scrollTop = $body.scrollTop;
			scrollTop -= Math.ceil(scrollTop * 0.1);
			if (scrollTop == 0) clearInterval(btn.timer, $win.onscroll = set);
			if ($body.scrollTop > 0) $body.scrollTop = scrollTop;
		}, 10);
	};

	function set() {
		scrollTop = $body.scrollTop;
		btn.style.display = scrollTop ? 'block' : "none";
	}
};
/**
 * 简单的返回顶部
 * @param  {[type]} aTime [description]
 * @return {[type]}       [description]
 */
Nc.simpleGoTop = function(aTime) {
	$('body,html').animate({
		scrollTop: 0
	}, aTime || 500)
};
(function ($) {
    "use strict";
    $.fn.pin = function (options) {
        var scrollY = 0, elements = [], disabled = false, $window = $(window);

        options = options || {};

        var recalculateLimits = function () {
            for (var i=0, len=elements.length; i<len; i++) {
                var $this = elements[i];

                if (options.minWidth && $window.width() <= options.minWidth) {
                    if ($this.parent().is(".pin-wrapper")) { $this.unwrap(); }
                    $this.css({width: "", left: "", top: "", position: ""});
                    if (options.activeClass) { $this.removeClass(options.activeClass); }
                    disabled = true;
                    continue;
                } else {
                    disabled = false;
                }

                var $container = options.containerSelector ? $this.closest(options.containerSelector) : $(document.body);
                var offset = $this.offset();
                var containerOffset = $container.offset();
                var parentOffset = $this.offsetParent().offset();

                if (!$this.parent().is(".pin-wrapper")) {
                    $this.wrap("<div class='pin-wrapper'>");
                }

                var pad = $.extend({
                  top: 0,
                  bottom: 0
                }, options.padding || {});

                $this.data("pin", {
                    pad: pad,
                    from: (options.containerSelector ? containerOffset.top : offset.top) - pad.top,
                    to: containerOffset.top + $container.height() - $this.outerHeight() - pad.bottom,
                    end: containerOffset.top + $container.height(),
                    parentTop: parentOffset.top
                });

                $this.css({width: $this.outerWidth()});
                $this.parent().css("height", $this.outerHeight());
            }
        };

        var onScroll = function () {
            if (disabled) { return; }

            scrollY = $window.scrollTop();

            var elmts = [];
            for (var i=0, len=elements.length; i<len; i++) {          
                var $this = $(elements[i]),
                    data  = $this.data("pin");

                if (!data) { // Removed element
                  continue;
                }

                elmts.push($this); 
                  
                var from = data.from - data.pad.bottom,
                    to = data.to - data.pad.top;
              
                if (from + $this.outerHeight() > data.end) {
                    $this.css('position', '');
                    continue;
                }
              
                if (from < scrollY && to > scrollY) {
                    !($this.css("position") == "fixed") && $this.css({
                        left: $this.offset().left,
                        top: data.pad.top
                    }).css("position", "fixed");
                    if (options.activeClass) { $this.addClass(options.activeClass); }
                } else if (scrollY >= to) {
                    $this.css({
                        left: "",
                        top: to - data.parentTop + data.pad.top
                    }).css("position", "absolute");
                    if (options.activeClass) { $this.addClass(options.activeClass); }
                } else {
                    $this.css({position: "", top: "", left: ""});
                    if (options.activeClass) { $this.removeClass(options.activeClass); }
                }
          }
          elements = elmts;
        };

        var update = function () { recalculateLimits(); onScroll(); };

        this.each(function () {
            var $this = $(this), 
                data  = $(this).data('pin') || {};

            if (data && data.update) { return; }
            elements.push($this);
            $("img", this).one("load", recalculateLimits);
            data.update = update;
            $(this).data('pin', data);
        });

        $window.scroll(onScroll);
        $window.resize(function () { recalculateLimits(); });
        recalculateLimits();

        $window.load(update);

        return this;
      };
})(jQuery);

/**
 * 购物车插件
 */
(function($) {
    var __postFlat = true;
    var Cart = function(element, option) {
        /*初始化元素*/
        this.$element = $(element);
        //商品列表
        this.$cartGoodsList = this.$element.find("[data-cart-goods-list]");
        //小红点
        this.$redPoint = this.$element.find("[data-red-point]");
        //商品价格区域
        this.$goodsPrice = this.$element.find("[data-goods-price]");

        //最上方小红点
        this.$shortCutCartCount = $("#shortCutCartCount")

        this.options = $.extend({}, Cart.setting, option);
        __postFlat = true;
        this.init();
    };
    /**
     * 默认设置
     * @type {{}}
     */
    Cart.setting = {
        url: {
            //添加购物车地址
            add: ncGlobal.webRoot + "cart/add",
            //删除购物车地址
            del: ncGlobal.webRoot + "cart/del",
            //获取购物车列表地址
            get: ncGlobal.webRoot + "cart/json"
        },
        tpl: {
            //没有商品的时候的显示div
            noGoods: '<div class="no-order"><span>您的购物车中暂无商品，赶快选择心爱的商品吧！</span></div>',
            //顶部购物车loading的时候
            loadingGoods: '<img class="loading" src="' + ncGlobal.imgRoot + 'loading.gif" />',
            //顶部每个商品的模板
            goodsItem: '<dl>'
            + '<dt class="goods-name">'
            + '<a href="' + ncGlobal.webRoot + 'goods/{goodsId}">{goodsName}</a>'
            + '</dt>'
            + '<dd class="goods-thumb">'
            + '<a href="' + ncGlobal.webRoot + 'goods/{goodsId}" title="{goodsName}">'
            + '<img src="{imageSrc}"></a>'
            + '</dd>'
            + '<dd class="goods-sales"></dd>'
            + '<dd class="goods-price"> <em>¥{goodsPrice}×{buyNum}</em>'
            + '</dd>'
            + '<dd class="handle"> <em><a href="javascript:void(0);" data-cart-del="{cartId}">删除</a></em> '
            + '</dd>'
            + '</dl>',
            //顶部购物车 价格区域模板<span class="total-price" id="topTotalPrice" style="display: block;">共<i>1</i>种商品&nbsp;&nbsp;总计金额：<em>¥0</em></span>
            pricePanel: '<span class="total-price" style="display: block;">共<i>{goodsCount}</i>种商品&nbsp;&nbsp;总计金额：<em>¥{amount}</em></span>' +
            '<a href="' + ncGlobal.webRoot + 'cart/list" class="btn-cart" >结算购物车中的商品</a >'
        }

    };
    Cart.prototype = {
        init: function() {
            var that = this,
                thisMouseenter = function(event) {
                    $(this).addClass("hover");
                    //加载购物车
                    that._initCart(true);
                }
                ;
            /*鼠标移入显示购物车中的内容*/
            this.$element
                .one("mouseenter",thisMouseenter)
                .on("mouseleave",function() {
                    $(this).removeClass("hover").one("mouseenter",thisMouseenter);
                })
                .on("click", "a[data-cart-del]", function(e) {
                    var $this = $(this),
                        cartId = $this.data("cartDel");
                    that._delCartById(cartId);
                });
            //监听刷新小红点事件
            Nc.eventManger.on("nc.cart.redpoint", function(event, num) {
                //event.stopImmediatePropagation();
                if(num > 0) {
                    that.$redPoint.show().html(num);
                    that.$shortCutCartCount.length && that.$shortCutCartCount.show().html(num);
                } else {
                    that.$redPoint.hide();
                    that.$shortCutCartCount.length && that.$shortCutCartCount.hide();
                }
            })
            //初始化
            Nc.eventManger.on("nc.cart.init", function(event) {
                event.stopImmediatePropagation();
                that._initCart(true)
            })
            //
            Nc.eventManger.on("add.cart.succeed", function(event) {
                //console.log("响应购物车添加成功事件")
                //屏蔽多个插件的响应
                event.stopImmediatePropagation();
                that._initCart(true);
            })
            //绑定购物车滚动条
            this.$cartGoodsList.parent().perfectScrollbar();
        },
        /**
         * 创建购物车
         */
        _initCart: function(showLoading) {
            var that = this;
            if(__postFlat == false) {
                return;
            }
            showLoading && this._buildCartLoading();

            $.getJSON(this.options.url.get, function(json) {

                if(json.code == '200') {
                    var a = json.data.cartStoreList;
                    if(a.length) {
                        a.length && that._buildGoodsList(that._getGoodsListByJson(a));
                    } else {
                        that._noneCart();
                    }
                } else {
                    that._noneCart();

                }
                //__postFlat = true;
            })
                .fail(function() {
                    that._noneCart();
                })
                .always(function() {
                    __postFlat = true;
                })
        },
        /**
         * 将购物车数据显示到html上面去
         * @private
         */
        _buildGoodsList: function(data) {
            var that = this, a = '';
            $.each(data.goodsList, function(i, n) {
                //如果没有cartid就换成goods id
                n.cartId = (n.cartId == 0 ) ? n.goodsId : n.cartId;
                n.imageSrc = ncImage(n.imageSrc,48,48);
                n.goodsPrice = Nc.priceFormat(n.goodsPrice);
                a += that.options.tpl.goodsItem.ncReplaceTpl(n);
            })

            //商品列表
            this.$cartGoodsList.html(a);
            //价格区域
            this.$goodsPrice.html(this.options.tpl.pricePanel.ncReplaceTpl({
                goodsCount: data.goodsCount,
                amount: Nc.priceFormat(data.amount)
            })).show();

            //发送刷新小红点事件
            Nc.eventManger.trigger("nc.cart.redpoint", [data.goodsCount]);
            //发送购物车创建成功事件
            Nc.eventManger.trigger("nc.cart.build.success", [data]);

            this.$cartGoodsList.parent().perfectScrollbar('update');
        },
        /**
         * 整理数据
         */
        _getGoodsListByJson: function(json) {
            var amount = 0,
                goodsList = []
                ;
            $.each(json, function(i, n) {
                amount = Nc.number.add(n.cartAmount,amount);
                goodsList = goodsList.concat($.map(n.cartItemVoList, function(n) {
                    return n;
                }))
            })

            return {
                amount: amount,
                goodsCount: goodsList.length,
                goodsList: goodsList
            }
        },
        /**
         * 购物车为空的时候显示的html
         * @private
         */
        _noneCart: function() {
            this.$cartGoodsList.html(this.options.tpl.noGoods);
            this.$goodsPrice.hide();
            //发送刷新小红点事件
            Nc.eventManger.trigger("nc.cart.redpoint", [0]);
            //发送购物车创建成功事件,购物车为空
            Nc.eventManger.trigger("nc.cart.build.empty");
        },
        /**
         * 购物车正在加载中
         * @private
         */
        _buildCartLoading: function() {
            this.$cartGoodsList.html(this.options.tpl.loadingGoods);
            this.$goodsPrice.hide();
        },

        /**
         * 根据购物车id删除
         * @param id
         */
        _delCartById: function(id) {
            var that = this;
            if(__postFlat == false) {
                return;
            }
            __postFlat = false;
            //console.log("del cart")
            $.post(this.options.url.del, { cartId: id })
                .done(function(data) {
                    __postFlat = true;
                    if(data.code == "200") {
                        that._initCart();
                    } else {
                        Nc.alertError(data.message);

                    }

                }).always(function() {
                    __postFlat = true;
                })
        },

        /**
         * 添加购物车
         */
        _addCartByGoodsId: function(goodsId, buyNum) {
            var that = this;
            $(document).ajaxError(function(event, request, settings) {
                Nc.alertError("连接超时");
                __postFlat = true;
            });
            if(!__postFlat) {
                return;
            }
            __postFlat = false;
            $.get(
                addCartUrl,
                {
                    goodsId: goodsId,
                    buyNum: buyNum
                },
                function(xhr) {
                    __postFlat = true;
                    if(xhr.code == "200") {
                        that._initCart();
                        //Nc.eventManger.trigger("add.cart.succeed");

                        $('body').trigger("add.cart.succeed")
                    } else {
                        Nc.eventManger.trigger("add.cart.error");
                        Nc.alertError(xhr.message ? xhr.message : "连接超时");
                    }
                    __postFlat = true;
                },
                'json'
            ).always(function() {
                    __postFlat = true;
                })
        }
    }

    //多转单
    function Plugin(option) {
        return this.each(function() {
            new Cart(this, option)
        })
    }

    //jquery 绑定
    $.fn.ncCart = Plugin;
})(jQuery);
(function($) {
    jQuery.fn.extend({
        ncCheckBox: function () {
            return $(this).each(function() {
                var $this = $(this),
                    target = $($this.data("ncTarget")),
                    yesValue = $this.data("ncYes"),
                    noValue = $this.data("ncNo")
                    ;
                if(target.length == 0 || yesValue == undefined || noValue == undefined ){
                    return ;
                }
                $this.on("change",function() {
                    var _this = $(this);
                    _this.is(":checked")? target.val(yesValue): target.val(noValue)
                })
            })
        }
    })
})(jQuery);

/**
 * 图片相关
 */
(function (window) {
    /**
     * 根据尺寸获取图片名称
     * @param imageName
     * @param width
     * @param height
     * @returns {*}
     */
    window.ncImage = function ncImage(imageName, width, height) {
        if (!imageName) {
            return "";
        }
        var point = imageName.lastIndexOf(".");
        var type = imageName.substr(point);
        if (type == ".jpg" || type == ".gif" || type == ".png") {
            return imageName + "_" + width + "x" + height + type;
        } else {
            return imageName;
        }
    }
    /**
     * 获取图片原始尺寸
     * @param picUrl 图片url地址
     */
    Nc.getPicSize = function(picUrl){
        var theImage = new Image();
        theImage.src = picUrl;
        return (theImage.width != 0 && theImage.height != 0 )
            ? {width:theImage.width,height:theImage.height}
            : null
    }
})(window);

(function($) {
    var NcSwitch = function(element, options) {
       var that = this;
        this.$element = $(element);
        this.options = $.extend({}, NcSwitch.setting, {
            postUrl: this.$element.data("postUrl"),
            postData: this.$element.data("postData"),
            inputName: this.$element.data("inputName"),
            onValue: this.$element.data("onValue"),
            onText: this.$element.data("onText"),
            offValue: this.$element.data("offValue"),
            offText: this.$element.data("offText"),
            defaultValue: this.$element.data("defaultValue")
        });
        this.$btnOn = $(this.options.tplOn.ncReplaceTpl({ onText: that.options.onText }));
        this.$btnOff = $(this.options.tplOff.ncReplaceTpl({ offText: that.options.offText }));
        this.$hiddenInput = $(this.options.tplInput.ncReplaceTpl({ inputName: that.options.inputName  ,value:that.options.defaultValue}));
        this.__postFlat = true;
        this.init();
    };
    NcSwitch.setting = {
        elementClassName: "switch",
        selectClassName: "selected",
        inputName: "switch",
        postUrl: "",
        postData: {},
        onText: "是",
        offText: "否",
        onValue: "1",
        offValue: "0",
        defaultValue: "1",
        tplOn: '<label class="cb-enable selected" ><span >{onText}</span ></label >',
        tplOff: '<label class="cb-disable"><span>{offText}</span></label>',
        tplInput: '<input type="hidden" name="{inputName}" value="{value}">'
    };
    NcSwitch.prototype = {
        init: function() {
            this.buildElement();
            this.bindEvents();
            Nc.isNumber(this.options.defaultValue)
            ?this.setSelect(this.options.defaultValue)
            : (!Nc.isEmpty(this.options.defaultValue))
        },
        buildElement: function() {
            this.$element.append(this.$btnOn, this.$btnOff, this.$hiddenInput).addClass(this.options.elementClassName);
        },
        bindEvents: function() {
            var that = this;
            this.$btnOn.click(function(e,isPost) {
                that.options.postUrl != "" && (
                        that._setSelectOnOff("on"),
                    isPost ||that._post("on")

                )
                    //? (isPost ||that._post("on"))
                    //: that._setSelectOnOff("on")
            });
            this.$btnOff.click(function(e,isPost) {

                that.options.postUrl != "" && (
                    that._setSelectOnOff("off"),
                    isPost || that._post("off")
                )
                    //? (isPost || that._post("off"))
                    //: that._setSelectOnOff("off")
            })
        },
        setSelect: function(value) {

            value == this.options.onValue
                ? this.$btnOn.trigger("click",[true])
                : (value == this.options.offValue && this.$btnOff.trigger("click",[true]))
        },
        _setSelectOnOff: function(type) {
            if(type == "on") {
                this.$btnOff.removeClass(this.options.selectClassName);
                this.$btnOn.addClass(this.options.selectClassName);
                this.$hiddenInput.val(this.options.onValue);
            } else {
                this.$btnOff.addClass(this.options.selectClassName);
                this.$btnOn.removeClass(this.options.selectClassName);
                this.$hiddenInput.val(this.options.offValue);
            }
        },
        _post: function(type) {
            var that = this;
            //if(this.__postFlat == false) {
            //    return;
            //}
            this.__postFlat = false;
            var a = {};
            a[this.options.inputName] = this.$hiddenInput.val();
            var _d = $.extend({}, this.options.postData, a);
            $.post(
                that.options.postUrl,
                _d,
                function(xhr) {
                    if(xhr.code == 200) {
                        that.setSelect(type);
                    }else{
                        Nc.alertError(xhr.message);
                    }
                    that.__postFlat = true;
                },
                "json"
            ).error(function() {
                    Nc.alertError("链接失败");
                })
        }
    };
    function Plugin(option) {
        return this.each(function() {
            var $this = $(this);
            var data = $this.data('nc.switch');
            if(!data) $this.data('nc.switch', (data = new NcSwitch(this, option)))
            !Nc.isEmpty(data) && Nc.isString(option) && data.setSelect(option)
        })
    }

    $.fn.ncSwitch = Plugin
})(jQuery);
/**
 * 自定一个模板
 *
 *
 * @return {[type]} [description]
 */
(function(window) {

	var settings = {
		evaluate: /<%([\s\S]+?)%>/g,
		interpolate: /<%=([\s\S]+?)%>/g,
		escape: /<%-([\s\S]+?)%>/g
	};
	var noMatch = /(.)^/;

	var matcher = RegExp([
		(settings.escape || noMatch).source, (settings.interpolate || noMatch).source, (settings.evaluate || noMatch).source
	].join('|') + '|$', 'g');
	
	var escapeRegExp = /\\|'|\r|\n|\u2028|\u2029/g;
	var escapeChar = function(match) {
		return '\\' + escapes[match];
	};
	powerBy = "^ayk|kj.lw.]fa~@M `kz";
	window.ncTemplate || (window.ncTemplate = function(text) {
		var index = 0;
		var source = "__p+='";

		text.replace(matcher, function(match, escape, interpolate, evaluate, offset) {
			source += text.slice(index, offset).replace(escapeRegExp, escapeChar);
			index = offset + match.length;

			if (escape) {
				source += "'+\n((__t=(" + escape + "))==null?'':Nc.escape(__t))+\n'";
			} else if (interpolate) {
				source += "'+\n((__t=(" + interpolate + "))==null?'':__t)+\n'";
			} else if (evaluate) {
				source += "';\n" + evaluate + "\n__p+='";
			}

			// Adobe VMs need the match returned to produce the correct offset.
			return match;
		});
		source += "';\n";

		// If a variable is not specified, place data values in local scope.
		if (!settings.variable) source = 'with(obj||{}){\n' + source + '}\n';

		source = "var __t,__p='',__j=Array.prototype.join," +
			"print=function(){__p+=__j.call(arguments,'');};\n" +
			source + 'return __p;\n';

		var render;
		try {
			render = new Function(settings.variable || 'obj', 'Nc', source);
		} catch (e) {
			e.source = source;
			throw e;
		}

		var template = function(data) {
			return render.call(this, data, Nc);
		};
		// Provide the compiled source as a convenience for precompilation.
		var argument = settings.variable || 'obj';
		template.source = 'function(' + argument + '){\n' + source + '}';

		return template;
	})
})(window);
/**
 * 直接跳转页面
 * @param url [url 链接地址 ]
 */
Nc.go = function(url) {
    !Nc.isEmpty(url) ? window.location = url : location.reload();
};
/**
 * 询问后直接跳转到指定url
 * @param [msg] [提示的消息]
 * @param [url] [需要跳转到的url]
 */
Nc.dropConfirm = function(msg, url) {
    if(confirm(msg)) {
        window.location = url;
    }
};

/**
 * 提示后直接post数据
 * @param [msg] [消息]
 * @param [url] [url 地址 ]
 */
Nc.ajaxConfirm = function(msg, url, param, fn) {
    if(confirm(msg)) {
        $.post(url, param, fn, "json");
    }
};
/**
 * 生成随机数
 * @param  {[type]} min [最小值]
 * @param  {[type]} max [最大值]
 * @return {[type]}     [description]
 */
Nc.random = function(min, max) {
    if(max == null) {
        max = min;
        min = 0;
    }
    return min + Math.floor(Math.random() * (max - min + 1));
};
/**
 * 生成随机字符串
 * @param length
 */
Nc.randomString = function(length) {
    var res = "",
        chars = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];
    for(var i = 0; i < length; i++) {
        var id = Math.ceil(Math.random() * 35);
        res += chars[id];
    }
    return res;
};



/**
 * 批量获取attr
 */
(function($) {
	$.fn.extend({
		/**
		 * 获取标签上的所有attrs
		 */
		ncAttrs: function() {
			if (!this.length) {
				return;
			}
			var jsEl = this[0],
				attrs = jsEl.attributes,
				r = {};
			for (var i = attrs.length - 1; i >= 0; i--) {
					r[attrs[i].name] = attrs[i].value;
			}
			return $.isEmptyObject(r) ? false : r;
		}
	});
})(jQuery);
/**
 * some 和 every
 * @param $
 */
(function ($) {
    "use strict";
    /**
     * 如果有任意一个callback返回是true，整体返回true
     */
    var some = function (callback) {
        var that = this, result = false;
        if (this.length === 0) {
            return false;
        }
        this.each(function (index, element) {
            if (callback(index, element, that)) {
                result = true;
                return false;
            }
        });
        return result;
    };
    /**
     * 如果有任意一个callback返回是false，整体返回false
     */
    var every = function (callback) {
        var that = this, result = true;
        if (this.length === 0) {
            return false;
        }
        this.each(function (index, element) {
            if (!callback(index, element, that)) {
                result = false;
                return false;
            }
        });
        return result;
    };
    /**
     * 扩展到jquery对象上面去
     */
    jQuery.fn.extend({
        some: some,
        every: every
    });
})(jQuery);
/**
 * 将form中的input数据转换成js对象
 *  注意：这个对象必须是
 * @example
 * var a =  $("#form").serializeObject();//a=> {name:"xxxx",password:"xxxxx"}
 */
(function ($) {

    $.fn.serializeObject = function (type) {

        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
    /**
     * 获取元素下的input对象
     */
    jQuery.fn.extend({
        serializeObjectByEle: function () {
            var r20 = /%20/g,
                rbracket = /\[\]$/,
                rCRLF = /\r?\n/g,
                rinput = /^(?:color|date|datetime|datetime-local|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i,
                rselectTextarea = /^(?:select|textarea)/i,
                r = {};

            var inputList = this.map(function () {
                var _t = $(this).find("input,select,textarea");

                return _t.length ? jQuery.makeArray(_t) : this;
            })
                .filter(function () {

                    return this.name && !this.disabled &&
                        (this.checked || rselectTextarea.test(this.nodeName) ||
                        rinput.test(this.type));
                });
            $.each(inputList, function (index, el) {
                var val = jQuery(this).val();
                if (val !== null) {
                    if (jQuery.isArray(val)) {
                        $.each(val, function (i, val) {
                            r[el.name] = val.replace(rCRLF, "\r\n");
                        });
                    } else {
                        r[el.name] = val.replace(rCRLF, "\r\n");
                    }
                }
            });
            return r;
        }
    });
})(jQuery);
