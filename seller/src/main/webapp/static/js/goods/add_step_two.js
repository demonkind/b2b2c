/**
 *  添加规格值按钮组件
 */
ncDefine("nc.addSpecValue", [], function() {
    /**
     * 添加规格值组件
     */
    var addSpecValueComponent = function() {
        //合并设置
        $.extend(this, specValueDefault);
        var _tmp = '<div class="spec-value-add-btn">' +
            '<div>' +
            '<a href="javascript:void(0);" class="btn btn-xs btn-default" data-btn-add> <i class="icon-plus"></i>' +
            '添加规格值' +
            '</a>' +
            '</div>' +
            '<div style="display:none;">' +
            '<input class="text w100" type="text" placeholder="输入添加规格值" maxlength="40">' +
            '<a href="javascript:void(0);" data-btn-ok class="btn btn-sm btn-info m-l-5 m-r-5">确认</a>' +
            '<a href="javascript:void(0);" data-btn-cancel class="btn btn-sm btn-danger">取消</a>' +
            '</div>' +
            '</div>';

        this.$el = $(_tmp);
        this.bindEvents();
        //
    };
    var specValueDefault = {
        MaxAdd: 20,
        //最大添加数量
        addSpecValueUrl: ncGlobal.sellerRoot + 'spec/value/save.json'
    };
    addSpecValueComponent.prototype.bindEvents = function() {
        var that = this;
        this.$el.find("a[data-btn-add],a[data-btn-cancel]").on("click", function() {
            var $this = $(this),
                $par = $this.closest("div");
            $par.hide().siblings().show().find("input").val("");
        });
        this.$el.find("a[data-btn-ok]").on("click", function() {
            //console.log("添加规格值后显示的input 中确认事件");
            var $this = $(this),
                $par = $this.closest("div"),
                $input = $par.find("input"),
                $li = $this.closest('li'),
                $h4 = $this.closest('li').find(".spec-name"),
                _inputValue = $input.val()
                ;
            if(Nc.isEmpty(_inputValue)|| _inputValue.length > 40 ){
                Nc.alertError("请输入规格值名称");
                return ;
            }
            that.addSpecValuePost({
                specId: $h4.data().specId,
                specValueName: $input ? $input.val() : ""
            }, $li);
            $par.hide().siblings().show().find("input").val("");
        });
    };
    addSpecValueComponent.prototype.addSpecValuePost = function(postData, element) {
        var that = this;
        $.post(that.addSpecValueUrl, postData, function(data, textStatus, xhr) {
            if(data.code == "200") {
                $("#spec_" + postData.specId).find("div .spec-val:last").after(that.addNewSpecValueInput(data.data[0], postData));
            } else {
                Nc.alertError(data.message);
            }
        }, "json").error(function() {
            Nc.alertError("链接超时");
        });
    };
    //<span class="spec-val"><input type="checkbox" class="checkbox" id="spec-value-checkbox-27-71"><span id="spec-value-text-27-71">22</span></span>
    addSpecValueComponent.prototype.addNewSpecValueInput = function(specValueId, postData) {
        var $input = $("<input>", {
            type: 'checkbox',
            "class": 'checkbox',
            id: "spec-value-checkbox-" + postData.specId + '-' + specValueId
        }).data({
            specValueId: specValueId,
            specId: postData.specId,
            specValueName: postData.specValueName
        });
        return $("<span>", {
            "class": "spec-val",
            html: '<span id="spec-value-text-' + postData.specId + '-' + specValueId + '">' + postData.specValueName + '</span>'
        }).prepend($input);

    }
    addSpecValueComponent.prototype.render = function() {
        return this.$el;
    };

    return {
        build: function() {
            return new addSpecValueComponent();
        },
        MaxAdd: specValueDefault.MaxAdd
    };
});
/**
 * 规格组件
 */
ncDefine("nc.spec", ['nc.addSpecValue'], function(addSpecValueComp) {

    /**
     * 构造函数
     */
    var GoodsSpecComponent = function(option) {
        //合并配置
        $.extend(this, GoodsSpecComponent.setting, option);

        //规格以及规格值列表，ajax 获取
        this.specList = '';

        this._init();
    };

    /**
     * 设置
     * @type {Object}
     */
    GoodsSpecComponent.setting = {
        //规格显示的panel
        $el: $("#specPanel"),
        //库存配置的div
        $specPanel: $("#spec_div"),
        //库存配置的头部
        $specDivTh: $("#spec-div-th"),
        //库存详情列表
        $specDivBody: $("#spec-div-body"),
        specUrl: "",
        //库存配置列表模板
        trTmpl: '<td><input class="text price" name="noneMarketPrice{id}" data-marketprice type="text" data-rule-required="true" data-rule-number="number" data-rule-min="0.01"><em class="add-on"><i class="icon-renminbi"></i></em></td>' +
        '<td><input name="nonePrice{id}" data-rule-required="true" data-rule-number="number" data-rule-min="0.01" class="text price" data-price type="text"><em class="add-on"><i class="icon-renminbi"></i></em></td>' +
        '<td><input name="noneStock{id}" data-rule-required="true" data-rule-digits="true" class="text stock" data-stock type="text"></td>' +
        '<td><input name="nonealarm{id}" class="text stock" data-rule-digits="true" data-alarm type="text"></td>' +
        '<td><input name="nonesku{id}" class="text sku" data-sku type="text"></td>' +
        '<td><input  name="nonebarcode{id}" class="text sku" data-barcode type="text"></td>'

    };

    GoodsSpecComponent.prototype = {
        /**
         * 初始化
         * @return {[type]} [description]
         */
        _init: function() {
            //异步获取规格及规格值列表
            this._ajaxGetSpec();
            this._bindEvents();
        },
        /**
         * 添加事件监听
         * @return {[type]} [description]
         */
        _bindEvents: function() {
            var that = this;
            //当选中了规格值的时候
            Nc.eventManger.on("specvalue.check", function(e, isCheck, data, $el) {
                //console.log("准备刷新spec事件",isCheck);
                var keyA, keyB, key = that._getCacheKey($el);
                keyA = Nc.deepCopy(key);
                keyB = Nc.deepCopy(key);
                if(!isCheck) {
                    keyA.push(data.specId + "_" + data.specValueId);
                }
                //console.log("已选规格信息key： ",key);
                Nc.eventManger.trigger('refreshSKU.refresh',
                    [
                        that.$specDivBody.clone(),
                        keyA.length ? keyA.join(",") : "no-spec"
                    ]);

                that._refreshSKU();
                //触发一个规格值选改变完成事件
                if(isCheck) {
                    keyB.push(data.specId + "_" + data.specValueId);
                }
                Nc.eventManger.trigger('refreshSKU.finish',
                    [
                        keyB.length ? keyB.join(",") : "no-spec",
                        keyA.length ? keyA.join(",") : "no-spec"
                    ]
                );
            });
            //
            $("#specPanel").on("change", "input:checkbox", function(event) {
                var $this = $(this),
                    thisData = $this.data(),
                    $h4 = $("#spec-h4-" + thisData.specId),
                    $li = $this.closest("li"),
                    prefix = thisData.specId + "-" + thisData.specValueId,
                    specValueInputId = "spec-value-input-" + prefix,
                    specValueTextId = 'spec-value-text-' + prefix;
                isCheck = false;
                var h4_font = $("<font>", {
                    html: $h4.data().specName
                });

                //生成一个 input 组件
                var h4_input = $("<input>", {
                    'class': 'text w70',
                    'title': '修改规格别名',
                    type: 'text',
                    value: $h4.data().specName
                }).on("focusout", function() {
                    var $this = $(this);
                    $h4.data('specName', $(this).val());
                    h4_font.html($this.val());
                }).on("change", function() {

                    var $this = $(this),
                        $specTitle,
                        data = $h4.data();
                    //如果修改后值是空的
                    if($this.val() == '') {
                        $this.val(data.specName);
                        return;
                    }
                    $specTitle = that.$specDivTh.find('th[data-spec-title="' + data.specId + '"]');
                    //table th 上的显示值和 h4 上的 data的值
                    if($specTitle.length) {
                        $specTitle.html($this.val());
                        data.specName = $this.val();
                        //发送规格修改事件
                        Nc.eventManger.trigger('spec.update', [data]);
                    }
                });
                if(typeof $this.attr("checked") != "undefined") {
                    /*规格值编辑框*/
                    $("<input>", {
                        "value": thisData.specValueName,
                        "class": "text w80",
                        id: specValueInputId
                    })
                        .insertAfter($this)
                        .on('focusout', function() {
                            $this.data("specValueName", $(this).val());
                        })
                        .on("change", function(e) {
                            var $this = $(this),
                                data = thisData,
                                tdEl = "td[data-spec-td='{specId}-{specValueId}']".ncReplaceTpl(data),
                                $specTd = that.$specDivBody.find(tdEl);
                            //如果是空就使用原来的值
                            if($this.val() == '') {
                                $this.val(data.specValueName);
                                return;
                            }
                            //修改库存框上的td 的值
                            if($specTd.length) {
                                $specTd.html($this.val());
                                data.specValueName = $this.val();
                                //发送一个修改specvalue 事件
                                Nc.eventManger.trigger('specvalue.update', [data]);
                            }
                            ;
                        });
                    $("#" + specValueTextId).remove();

                    if($h4.find("font").length) {
                        $h4.find("font").remove();
                        h4_input.appendTo($h4);
                    }
                    isCheck = true;

                } else {
                    //
                    $("#" + specValueTextId).remove();

                    $("<span>", {
                        id: specValueTextId,
                        html: $("#" + specValueInputId).val()
                    }).insertAfter($this);

                    $("#" + specValueInputId).remove();
                    if($li.find("input:checked").length == 0) {
                        h4_font.appendTo($h4);
                        $h4.find("input").remove();
                    }
                }

                //发送消息
                Nc.eventManger.trigger('specvalue.check', [isCheck, thisData, $this]);


            })

        },
        /**
         * 根据已选规格值id获取缓存的key
         * @private
         */
        _getCacheKey: function($el) {
            return $.map(this.$el.find(":checkbox:checked").not($el), function(n) {
                var a = $(n);
                return a.data("specId") + "_" + a.data("specValueId");
            });
        },
        /**
         * 获取spec
         * @return {[type]} [description]
         */
        _ajaxGetSpec: function() {
            var that = this;
            $.getJSON(this.specUrl, function(data) {
                if(data.code == 200) {

                    that.specList = data.data;
                    //显示规格以及规格值列表
                    that._buildElement();
                } else {
                    Nc.alertError(data.message);
                }
            }, 'json');
        },
        /**
         * 创建元素
         * @return {[type]} [description]
         */
        _buildElement: function() {
            var goodsSpecThis = this,
            //规格行
                b_li = function(data) {

                    var li = $("<li>", {
                        id: "spec_" + data.specId
                    });

                    var h4_font = $("<font>", {
                        html: data.specName
                    });

                    /*选中的后规格的 input*/
                    var h4_input = $("<input>", {
                        'class': 'text w70',
                        'title': '修改规格别名',
                        type: 'text',
                        value: data.specName
                    })

                    var h4 = $("<h4>", {
                        "class": "spec-name",
                        id: "spec-h4-" + data.specId
                    })
                        .append(h4_font)
                        .appendTo(li)
                        .data(data);

                    li.append("<div class='spec-value-box'></div>");

                    $.each(data.specValueList, function() {
                        //标示用于生成id
                        var that = this,
                            specValueId = this.specValueId,
                            prefix = data.specId + "-" + that.specValueId,
                            specValueInputId = "spec-value-input-" + prefix,
                            specValueTextId = 'spec-value-text-' + prefix;

                        var input = $("<input>", {
                            'type': "checkbox",
                            'class': 'checkbox',
                            id: 'spec-value-checkbox-' + prefix
                        })
                            .data(this);

                        var span = $("<span>", {
                            "class": 'spec-val',
                            html: '<span id="' + specValueTextId + '">' + that.specValueName + '</span>'
                        })
                            .prepend(input)
                            .appendTo(li.find("div"));

                        //li.find("div").append(span)

                    });
                    //添加规格值编辑框组件
                    if(data.specValueList.length < addSpecValueComp.MaxAdd) {
                        //如果店铺的id 是 0 的话就不显示添加规格值按钮
                        data.storeId != 0 && li.append(addSpecValueComp.build().render())

                    }
                    return li;
                };

            //遍历规格列表并添加到显示区域
            $.each(this.specList, function(index, el) {
                b_li(this).appendTo(goodsSpecThis.$el);
            });
        },
        /**
         * 刷新sku
         * @return {[type]} [description]
         */
        _refreshSKU: function() {
            var that = this,
                dataList = [],
                specNameList = [],
                liList = this.$el.find("li"),
                variation_set = [];
            //获取选择的input
            $.each(liList, function(i, n) {
                var $n = $(n),
                    $h4 = $n.find('h4'),
                    check = $n.find("input:checked");
                if(check.length > 0) {
                    dataList.push($(n).find("input:checked"));
                    specNameList.push($h4.data());
                }
            });


            /*for (var i = liList.length - 1; i >= 0; i--) {
             var $li = $(liList[i]),
             $h4 = $li.find('h4'),
             $check = $li.find("input:checked");
             if ($check.length > 0) {
             dataList.push($check);
             specNameList.push($h4.data());
             }
             }*/

            if(liList.length > 0) {
                if(dataList.length) {
                    //如果有选择的规格值
                    getMatrix(dataList, 0, []);
                    specNameList && variation_set && (that._buildSKUHtml(specNameList, variation_set))
                } else {
                    //如果没有规格被选择就初始化
                    that.resetSKUDiv();
                }
            }

            function getMatrix(arr, index, value) {

                $.each(arr[index], function(i, n) {
                    $this = $(n);
                    value.splice(index, 9);
                    value.push({
                        specId: $this.data().specId,
                        specValueId: $this.data().specValueId,
                        specValueName: $this.data().specValueName
                    });
                    if(index == arr.length - 1) {
                        // variation_set.push(value.slice(0));
                        variation_set.unshift(value.slice(0));
                    } else {
                        var _index = index + 1;
                        getMatrix(arr, _index, value);
                    }
                });
            }

        },
        /**
         * sku 框框
         * @param data
         * @private
         */
        _buildSKUHtml: function(specNameList, data) {
            var that = this;
            //生成 sku 列表的头部
            //删除
            that.$specDivTh.find("tr th[data-spec-title]").remove();
            $.each(specNameList, function(i, n) {
                var $th = $("<th>", {
                        html: n.specName,
                        "data-spec-title": n.specId
                    }),
                    $tr = that.$specDivTh.find("tr"),
                    $tdLast = $tr.find("th[data-spec-title]:last");
                //添加，如果已经有规格的话就添加到后面去
                $tdLast.length ? $tdLast.after($th) : $tr.prepend($th);
            });

            //生成sku表格数据
            var a = 0,
                l = data.length - 1;
            that.$specDivBody.empty();
            //反向输出一下，
            //for (; a < l; a++) {
            for(; l >= 0; l--) {

                var $tr = $("<tr>", {
                    "data-goods-id": 0
                });
                $.each(data[l], function(i, n) {
                    $("<td>", {
                        html: n.specValueName,
                        "data-spec-td": n.specId + '-' + n.specValueId,
                        "data-spec-id": n.specId,
                        "data-spec-value-id": n.specValueId
                    }).appendTo($tr);
                })
                that.$specDivBody.append($tr.append(that.trTmpl.ncReplaceTpl({
                    id: Nc.random(1, 999999999)
                })));
            }
        },

        /**
         * 初始化库存配置列表
         */
        resetSKUDiv: function() {
            var that = this;
            //清除列表上的th
            that.$specDivBody.empty().append($("<tr>", {
                "data-no-spec": "",
                html: that.trTmpl.ncReplaceTpl({
                    id: Nc.random(1, 999999999)
                })
            }));
            //清除表头上的规格
            that.$specDivTh.find("th[data-spec-title]").remove();
        },
        /**
         * 获取已经选择的规格信息
         * @return {[type]} [description]
         */
        getSpecPostData: function() {
            var that = this,
                liList = that.$el.find("li"),
                dataList = [],
                result;

            //获取选择的inpu
            $.each(liList, function(i, n) {
                var $n = $(n),
                    $h4 = $n.find('h4'),
                    check = $n.find("input:checked");
                if(check.length > 0) {
                    var _t = {
                        specId: $h4.data().specId,
                        specName: $h4.data().specName,
                        specValueList: []
                    };
                    $.each(check, function(index, el) {
                        var $el = $(el),
                            _data = $el.data();
                        _t.specValueList.push({
                            specValueId: _data.specValueId,
                            specValueName: _data.specValueName
                        });
                    });
                    dataList.push(_t);
                }
            });
            return dataList;
        },
        /**
         * 获取要发送的库存相关
         * @return {[type]} [description]
         */
        getSpecValuePostData: function() {

            var that = this,
                specText = that.$specDivTh.find("th[data-spec-title]"),
                specTextList = {},
                specDivTr = that.$specDivBody.find("tr"),
                result = [],
            //根据规格id获取规格名称
                getSpecName = function(specId) {
                    return specTextList[specId];
                },
                getGoodsFullSpecs = function(element) {
                    var
                        _goodsSpecs = '',
                        _goodsSpecValueIds = [],
                        _goodsFullSpecs = [],
                        colorId = 0;

                    $.each(element, function(index, el) {
                        var $el = $(el);
                        _goodsSpecs += $el.html() + ' ';
                        //获取规格id
                        specId = $el.data("spec-id") && $el.data("spec-id");
                        _goodsFullSpecs.push(getSpecName($el.data("spec-id")) + "：" + $el.html());
                        //获取规格值id列表
                        _goodsSpecValueIds.push($el.data("spec-value-id"));
                        if($el.data("spec-id") == '1') {
                            colorId = $el.data("spec-value-id");
                        }
                        ;
                    });
                    return {
                        colorId: colorId,
                        goodsSpecs: _goodsSpecs,
                        goodsFullSpecs: _goodsFullSpecs.length ? _goodsFullSpecs.join("，") : "",
                        specValueIds: _goodsSpecValueIds.length ? _goodsSpecValueIds.join(",") : ""
                    };
                };
            //获取已填写的规格id 和规格文字相对应的数据
            specText && $.each(specText, function(index, el) {
                var $el = $(el);
                specTextList[$el.attr("data-spec-title")] = $el.html();
            })

            // if ($.isEmptyObject(specText) || !specDivTr.length) {
            // 	return;
            // }

            $.each(specDivTr, function(index, el) {
                var $el = $(el),
                    specList = $el.find('td[data-spec-td]'),
                    goodsSpecs = getGoodsFullSpecs(specList);

                result.push({
                    goodsId: $el.attr("data-goods-id") ? ($el.attr("data-goods-id")) : 0,
                    goodsSpecs: $.trim(goodsSpecs.goodsSpecs),
                    goodsFullSpecs: goodsSpecs.goodsFullSpecs,
                    specValueIds: goodsSpecs.specValueIds,
                    markerPrice: $el.find('input[data-marketprice]').val(),
                    goodsPrice: $el.find('input[data-price]').val(),
                    goodsSerial: $el.find('input[data-sku]').val(),
                    goodsStorage: $el.find('input[data-stock]').val(),
                    goodsStorageAlarm: $el.find('input[data-alarm]').val(),
                    goodsBarcode: $el.find('input[data-barcode]').val(),
                    colorId: goodsSpecs.colorId
                });
            });

            return result;
        }

    };
    return function(option) {
        return new GoodsSpecComponent(option);
    };
});
/**
 * 商品上传图片
 * 依赖jquery，jquery.nc
 */
ncDefine("nc.goods.pic", [], function() {

    var GoodPic = function(option) {
        var that = this;
        //图片上传layer对象
        this.imageLayerObj = {};
        // 商品图片最大上传数量
        var MaxPic;

        //标识当前上传图片的个数
        var currentUploadPic = 0;

        this.uploadPicNum = 0;
        //合并配置
        $.extend(this, GoodPic.setting, option);

        //外部可以访问的方法
        this.getPicListData = GoodPic.getPicListData;

        //添加批量上传时的验证图片数量
        Maxpic = this.MaxPic;
        this.uploadOption.add = function(e, data) {
            //console.log("图片上传add事件");
            if(currentUploadPic >= Maxpic) {
                Nc.alertError("商品图片最多只能上传" + Maxpic + "张");
                return false;
            }
            data.submit();
            currentUploadPic++;
            that.uploadPicNum++;
        };

        this.uploadOption.change = function(e, data) {
            //console.log("图片上传change 事件");
            var a = $(e.target).closest(".ncsc-goodspic-list");
            currentUploadPic = a && a.find('ul li:not(.ui-state-disabled)').length;
            that.uploadPicNum = 0;
            that.imageLayerObj = {};
        };
        //初始化的时候使用默认的规格图片
        this._defaultShow();
        //绑定事件
        this._bindEvents();
    };
    /**
     * 获取数据
     */
    GoodPic.getPicListData = function() {
        var that = this,
            list = this.$goodsPicListPanel.find(".ncsc-goodspic-list"),
            postData = [];

        list && $.each(list, function(i, n) {
            var specInfo = $(n).data();
            $.each($(n).find("li.ncsc-goodspic-upload"), function(i, n) {
                var $n = $(n),
                    d = $n.data();
                postData.push({
                    colorId: specInfo.specValueId,
                    imageName: d.name,
                    imageSort: i,
                    isDefault: i == 0 ? 1 : 0
                });
            });
        })
        //修改input
        //this.$picJson.val(postData.length ? JSON.stringify(postData):'');
        return postData;
    };

    GoodPic.prototype = {
        /**
         * 默认显示的上传图片
         * @private
         */
        _defaultShow: function() {
            var that = this,
                a = [{
                    specId: 0,
                    specName: "默认",
                    specValueId: 0,
                    specValueName: "",
                    specValueList: []
                }];

            that.$goodsPicListPanel.empty();
            $.each(a, function(index, el) {
                that.$goodsPicListPanel.append(that._buildItem(el));
            });
        },
        /**
         * 绑定事件
         * @private
         */
        _bindEvents: function() {
            var that = this;

            //监听规格名称修改事件
            Nc.eventManger.on("spec.update", function(e, data) {
                if(that.showSpecId != data.specId) {
                    return;
                }
                var list = that.$goodsPicListPanel.find("span[data-spec-name]");
                list.length && list.html(data.specName)
            });
            //监听规格值名称的改变事件
            Nc.eventManger.on("specvalue.update", function(e, data) {
                if(that.showSpecId != data.specId) {
                    return;
                }

                var specValueId = data.specValueId,
                    list = that.$goodsPicListPanel.find("span[data-spec-value-name='" + specValueId + "']");
                list.length && list.html(data.specValueName)
            });

            //当选中了规格值的时候,才用
            //显示图片的时候
            Nc.eventManger.on("specvalue.check", function(e, isCheck, data) {

                if(data.specId == that.showSpecId) {
                    //获取规格相关数据
                    var a = that._domToAdapter(that._getSpecData(data.specId));
                    if(a.length) {
                        that.$goodsPicListPanel.empty();
                        $.each(a, function(index, el) {
                            that.$goodsPicListPanel.append(that._buildItem(el))
                        });
                    } else {
                        //如果没有规格值被选中,则添加一个初始值
                        that._defaultShow();
                    }

                }
            });

            //监听图片上传成功事件
            Nc.eventManger.on("nc.imageupload.succeed", function(e, uploadEvent, data) {

                var a = $(uploadEvent.target).closest(".ncsc-goodspic-list"),
                    d = a && a.data(),
                    c = a && a.find('ul'),
                //是否隐藏上传按钮
                    btnFn = function() {
                        var btn = a.find(".ncsc-upload-btn"),
                            count = c.find("li").length;
                        count >= that.MaxPic ? btn.addClass('noupload') : btn.removeClass('noupload');
                        count || c.append(that.goodsDefaultPicTmpl);
                    };
                if(a.length == 0 || $.isEmptyObject(d)) {
                    return;
                }
                //删除默认图片
                a.find(".ui-state-disabled").remove();

                var b = $(that.goodSpicUploadTmpl.ncReplaceTpl({
                    goodPicUrl: data.url
                })).data(data);


                //绑定删除事件
                b.find(".del").on("click", function() {
                    $(this).closest("li").remove();
                    btnFn();
                });
                c.append(b);
                btnFn();

                that._imageUploadSucessMsg(that.uploadPicNum, a);
                //商品图片排序
                $("#goods-pic-list ul").sortable(that.sortTableOption).disableSelection();
            });
            //图像上传
            that.$goodsPicListPanel.find("input[type='file']").fileupload(that.uploadOption);
            //排序
            $("#goods-pic-list ul").sortable(that.sortTableOption).disableSelection();
        },
        /**
         * 适配器
         * [{
		 * 		specId: 123123,
		 *  	specName: "fdfdf",
		 *		specValueId:1,
		 *		specValueName:"fddddd",
		 *		specValueList: []
		 *  }]
         *
         * @return {[type]} [description]
         */
        _domToAdapter: function(data) {
            return data;
        },
        /**
         * 构建一条图片上传列表
         */
        _buildItem: function(data) {
            var that = this,
            //build color element
                buildColorValue = function(specName, specValueId, specValueName) {
                    var a = $(that.colorValueTmpl.ncReplaceTpl({
                        specName: specName,
                        specValueId: specValueId,
                        specValueName: specValueName
                    }));
                    //绑定上传插件
                    a.find("input[type='file']").fileupload(that.uploadOption);
                    return a;
                };

            return $("<div>", {
                "class": "ncsc-goodspic-list",
                "data-spec-id": data.specId,
                "data-spce-value-id": data.specValueId
            })
                .append(buildColorValue(data.specName, data.specValueId, data.specValueName))
                .append(that._buildGoodsPicList(data.specValueList))
                .data(data);
        },

        // build goods-pic-list div
        _buildGoodsPicList: function(specValueList) {
            var that = this,
                a = $("<ul>", {
                    "class": "goods-pic-list"
                });
            specValueList && ($.each(specValueList, function (i, n) {
                $(that.goodSpicUploadTmpl.ncReplaceTpl({
                    goodPicUrl: n
                })).appendTo(a);
            }))
            //如果没有li就添加一个默认的数据
            a.find("li").length || a.append(that.goodsDefaultPicTmpl);
            //a.sortable(that.sortTableOption).disableSelection();
            //$("#goods-pic-list ul").sortable(that.sortTableOption).disableSelection()
            return a;
        },

        /**
         * 根据规格id 获取规格值等信息
         * @param  {[type]} specId [description]
         * @return {[type]}        [description]
         */
        _getSpecData: function(specId) {
            var specH4Data = $("#spec-h4-" + specId).data(),
                inputList = [];

            $.each($("#spec_" + specId).find("input:checked"), function(i, n) {
                var $n = $(n).data();
                inputList.push({
                    specId: specH4Data.specId,
                    specName: specH4Data.specName,
                    specValueId: $n.specValueId,
                    specValueName: $n.specValueName,
                    picList: []
                });
            });
            return inputList;
        },
        /**
         * 验证颜色图片是否填全
         */
        verfiySpecGoodsPic: function() {
            var that = this,
                list = this.$goodsPicListPanel.find(".ncsc-goodspic-list");
            return list.every(function(index, element, self) {
                return $(element).find("li.ncsc-goodspic-upload").length;
            })
        },
        /**
         * 图片上传提示
         * @private
         */
        _imageUploadSucessMsg: function(num, $element) {
            //console.log("图片上传",num);
            if(num !== 0 && Nc.isEmpty(this.imageLayerObj)) {
                //console.log("图片上传成功提示框");
                this.imageLayerObj = layer.msg('<div class="nc-layer-msg">已上传' + num + '张图片</div>');
            }
        }
    };


    GoodPic.setting = {
        //最大传图数量
        MaxPic: 8,
        //显示商品图片的规格id
        showSpecId: 1,

        //图片拖拽区域
        goodsPicList: ".goods-pic-list",

        //图片上传地址
        picUploadUrl: ncGlobal.sellerRoot + "image/upload.json",

        //列表添加区域
        $goodsPicListPanel: $("#goods-pic-list"),

        //填写图片数据的input
        $picJson: $("input[name='picJson']"),

        //每行左边
        colorValueTmpl: '<div class="color-value">' +
        '<h4><span data-spec-name>{specName}</span>：<span data-spec-value-name="{specValueId}">{specValueName}</span></h4>' +
        '<div class="ncsc-upload-btn"> <a href="javascript:void(0);"> <span>' +
        '<input type="file" hidefocus="true" size="1" class="input-file" name="file" multiple>' +
        '</span>' +
        '<p><i class="icon-picture"></i>多图上传</p>' +
        '</a> </div>' +
        '</div>',

        //单个图片的li
        goodSpicUploadTmpl: '<li class="ncsc-goodspic-upload">' +
        '<div class="upload-thumb"><img src="{goodPicUrl}"> </div>' +
        '<div class="show-default"> <a href="javascript:void(0)" class="del" title="移除">X</a> </div>' +
        '</li>',

        //默认的一张图片
        goodsDefaultPicTmpl: '<li class="ui-state-disabled">' +
        '<div class="upload-thumb">' +
        '<img src="' + ncGlobal.imgRoot + '/default_goods_image_240.gif"></div></li>',


        //上传设置,其他设置在 Goodpic 中
        uploadOption: {
            dataType: 'json',
            url: ncGlobal.sellerRoot + "image/upload.json",
            formData: {
                type: ncGlobal.filesType.goods
            },
            done: function(e, data) {
                if(data.result.code == 200) {
                    //发送上传成功事件
                    Nc.eventManger.trigger("nc.imageupload.succeed", [e, data.result.data]);
                } else {
                    Nc.alertError("文件上传失败");
                }

                //try{
                //    $("#goods-pic-list ul").sortable( 'destroy' );
                //}catch(e){
                //
                //}
                //$("#goods-pic-list ul").sortable(GoodPic.setting.sortTableOption).disableSelection();
                //try{
                //    debugger
                //    $("#goods-pic-list ul").sortable( 'refresh' );
                //}catch(e){
                //
                //}
            }
        },

        //jquery ui sorttable 设置
        sortTableOption: {
            revert: true,
            items: "li:not(.ui-state-disabled)"
        }
    };

    return function(option) {
        return new GoodPic(option);
    };
});
/**
 * 添加规则窗口
 * 依赖jquery，jquery.nc
 */
ncDefine("nc.addspec", ['nc.addSpecValue'], function(addSpecValue) {
    var AddSpec = function(option) {
        //合并配置
        $.extend(this, AddSpec.setting, option);
        //保存最初的窗口内容
        this.firstModal = this.$addSpecModal.html();

        //事件绑定
        this._init();
    };
    //设置
    AddSpec.setting = {

        specValueMax: 5,
        //库存配置的div
        $specPanel: $("#spec_div"),
        //库存配置的头部
        $specDivTh: $("#spec-div-th"),
        //库存详情列表
        $specDivBody: $("#spec-div-body"),
        $addSpecForm: $("#addSpecForm"),
        //添加规格按钮，点击后显示添加规格对话框
        $addSpecBtn: $("#addSpecBtn"),
        //添加规格对话框
        $addSpecModal: $("#addSpecModal"),
        //添加规格值按钮
        $addSpecValueBtn: $("#addSpecValueBtn"),

        $specInputName: $("#spec-input-name"),
        //
        $specValueInputGroup: $("#spec-value-input-group"),
        //保存新增的sepc的url
        saveSpecUrl: ncGlobal.sellerRoot + "spec/save.json",
        //几个常用的模板
        tpl: {}

    };
    AddSpec.prototype = {
        _init: function() {
            this._bindEvents();
        },
        _bindEvents: function() {
            var that = this;
            //当"添加规格"button 点击事件
            that.$addSpecBtn.on("click", function() {
                //还原对话框
                $("#addSpecForm").validate().resetForm();
                that.$specValueInputGroup.find("div:not(:first)").remove();
                that.$addSpecModal.find("input").val("");

                Nc.layerOpen({
                    title: "新增商品规格",
                    content: that.$addSpecModal,
                    yes: function(index, layero) {
                        that._addSpecAndSpecValue();
                    }
                });
            });

            that.$addSpecValueBtn.on("click", function() {
                //console.log('"添加规格值"按钮的点击事件');
                var $thisBtn = $(this),
                    length = that.$specValueInputGroup.find("input").length,
                    _div = '<div class="m-t-5" style="height: 30px;overflow: hidden"><input type="text" class="text w300 m-r-10" name="specValueName' +Nc.randomString(32)+ '" data-rule-required="true"><a href="javascript:;" class="btn btn-sm btn-danger"><i class="icon icon-trash"></i>删除</a></div>',
                    $div = $(_div);


                $div.find("a").on('click', function(event) {
                    var length;
                    $(this).closest('div').remove();
                    that.$specValueInputGroup.find("input").length >= that.specValueMax
                        ? $thisBtn.hide()
                        : $thisBtn.show();
                });
                that.$specValueInputGroup.append($div);
                //如果超出限制就隐藏按钮
                that.$specValueInputGroup.find("input").length >= that.specValueMax ? $thisBtn.hide() : $thisBtn.show();
            });
            //


        },
        /**
         * 添加一个新的规格和规格值
         */
        _addSpecAndSpecValue: function() {
            var that = this;
            //console.log("验证需要添加的规格名称和各个规格值");

            if(!$("#addSpecForm").valid()){
                return;
            }
            //改变input的name值
            this.$specValueInputGroup.find("input").attr("name","specValueName");

            $.post(
                this.saveSpecUrl,
                this.$addSpecForm.serialize(),
                function(data, textStatus, xhr) {
                    if(data.code == '200') {
                        that._addNewSpecToPanel(data.data);
                        layer.closeAll();
                        //删除
                        that.$specValueInputGroup.find("div:not(:first)").remove();
                        that.$addSpecModal.find("input").val("")
                    } else {
                        Nc.alertError(data.message);
                    }
                },
                "json"
            ).error(function() {
                    Nc.alertError("添加失败")
                });
        },
        //添加一个新的规格值
        _addNewSpecToPanel: function(data) {
            var goodsSpecThis = this;
            var li = $("<li>", {
                id: "spec_" + data.specId
            });

            var h4_font = $("<font>", {
                html: data.specName
            });

            /*选中的后规格的 input*/
            var h4_input = $("<input>", {
                'class': 'text w70',
                'title': '修改规格别名',
                type: 'text',
                value: data.specName
            });

            var h4 = $("<h4>", {
                "class": "spec-name",
                id: "spec-h4-" + data.specId
            })
                .append(h4_font)
                .appendTo(li)
                .data(data);

            li.append("<div class='spec-value-box'></div>");
            $.each(data.specValueList, function() {
                //标示用于生成id
                var that = this,
                    specValueId = this.specValueId,
                    prefix = data.specId + "-" + that.specValueId,
                    specValueInputId = "spec-value-input-" + prefix,
                    specValueTextId = 'spec-value-text-' + prefix;

                var input = $("<input>", {
                    'type': "checkbox",
                    'class': 'checkbox',
                    id: 'spec-value-checkbox-' + prefix
                })
                    .data(this);

                var span = $("<span>", {
                    "class": 'spec-val',
                    html: '<span id="' + specValueTextId + '">' + that.specValueName + '</span>'
                })
                    .prepend(input)
                    .appendTo(li.find("div"));
            });
            var addSpecValueBtn = addSpecValue.build().render()
            addSpecValueBtn.appendTo(li)
            $("#specPanel").append(li)
        }

    }
    return function(option) {
        return new AddSpec(option);
    };
})

/**
 *  店铺分类组件
 *    var a = $("#ncsc-store-label").find("dl dd input");
 */
ncDefine("nc.storeClass", [], function() {

    var StoreClass = function() {
        $.extend(this, StoreClass.setting);
        //绑定插件
        //this.$storeClass.perfectScrollbar();
        this._bindEvents();
    };
    StoreClass.setting = {
        $storeClass: $("#ncsc-store-label")
    };
    StoreClass.prototype = {
        /**
         * 绑定事件
         * @private
         */
        _bindEvents: function() {
            //店铺分类选择事件
            this.$storeClass
                .find("dl dd input")
                .click(function() {
                    var $this = $(this),
                    //获取1级分类
                        $dl = $this.closest("dl"),
                        $par = $dl.find("[data-store-parent]"),
                        $ddInput = $dl.find("dd input:checked");
                    //如果有一个2级分类被选择那么它的1级分类也要被选择
                    if($ddInput.length) {
                        $ddInput.length && $par.attr("checked", "true");
                    } else {
                        $par.length && $par.removeAttr('checked');
                    }
                });
        },
        /**
         * 获取数据
         */
        getDataPost: function() {
            var result = [];
            result = $.map($("#ncsc-store-label").find("input:checked"), function(n) {
                return ("storeLabelId=" + $(n).val());
            });
            return result ? result.join("&") : '';
        }
    };

    return new StoreClass();
});
/**
 * 商品品牌组件
 * 目前还是按照原有封装
 */
ncDefine("nc.goodsBrand", [], function() {
    var GoodsBrand = function() {

        //合并配置
        $.extend(this, GoodsBrand.setting);

        //绑定插件
        this.$brandListDiv.perfectScrollbar();

        this._bindEvents();
    };
    GoodsBrand.setting = {
        $bName: $('#b_name'),
        $bId: $('#b_Id'),
        $ncscBrandSelect : $("#ncscBrandSelect"),
        $ncscBrandSelectContainer: $('#ncsc-brand-select-container'),
        $bSearchBrandPanel: $('#b_search_brand_panel'),
        $bSearchBrandKeyword: $('#b_search_brand_keyword'),
        $bSearchBrandGo: $('#b_search_brand_go'),
        $brandListDiv: $("#brand-list-div"),
        $brandListUl: $('#brand-list-ul'),
        $bSearchNoResult: $('#b-search-no-result')
    };
    GoodsBrand.prototype = {
        //绑定事件
        _bindEvents: function() {
            var that = this;
            //点击input出现选择商品品牌列表
            //this.$bName.click(function() {
            //    that.$ncscBrandSelectContainer.show();
            //    that._reset();
            //});
            this. $ncscBrandSelect.click(function(){
                    that.$ncscBrandSelectContainer.show();
                    that._reset();
            })
            //点击空白关闭品牌选择
            $(document).mouseup(function(e) {
                if (!that.$ncscBrandSelect.is(e.target) && that.$ncscBrandSelect.has(e.target).length === 0) {
                    //console.log("点击空白关闭品牌选择")
                    that.$ncscBrandSelectContainer.hide()
                }
            })
            //首字母搜索点击事件
            this.$ncscBrandSelectContainer
                .find("a[data-letter]")
                .click(function(e) {
                    var $this = $(this),
                        letterValue = $this.data("letter");
                    that._showLiByLetter(letterValue);
                })
            //搜索关键字点击事件
            this.$bSearchBrandGo.click(function() {
                var keyWord = that.$bSearchBrandKeyword.val();
                //如果为空就不搜索
                if(!$.trim(keyWord)) {
                    that.$brandListUl.find("li").show();
                    return;
                }

                that._showLiByKeyword(keyWord);
            })
            //搜索后的品牌点击事件
            that.$brandListUl
                .find("li")
                .click(function() {
                    var $this = $(this);
                    that.$bName.val($this.data("brand-name"));
                    that.$bId.val($this.data("id"));
                    that.$ncscBrandSelectContainer.hide()
                });
            //点击删除品牌
            $("#ncsc-brand-select-del").click(function() {
                //console.log("删除品牌");
                that.$bName.val("");
                that.$bId.val("0");
                that.$ncscBrandSelectContainer.hide()
            });

        },
        /**
         * 根据首字母显示列表框
         * @param letter
         * @private
         */
        _showLiByLetter: function(letter) {
            var that = this,
                showList,
                hideList;
            showList = this.$brandListUl.find("li").show();
            this.$bSearchNoResult.hide();
            if(letter != "all") {
                hideList = this.$brandListUl.find("li[data-letter!='" + letter + "']").hide();

                hideList.length >= showList.length
                    ? this.$bSearchNoResult.show().find("strong").html(letter)
                    : this.$bSearchNoResult.hide();
            }
        },
        /**
         * 根据关键字搜索品牌框
         * @private
         */
        _showLiByKeyword: function(keyword) {
            var that = this,
                showList,
                hideList,
                rk
                ;
            this.$bSearchNoResult.hide();
            keyword = $.trim(keyword);

            rk = new RegExp(keyword, "i");
            this.$brandListUl.find("li").hide();
            //hideList = this.$brandListUl.find("li[data-brand-name*='" + keyword.toUpperCase()+ "']").show();
            hideList = $.grep(this.$brandListUl.find("li[data-brand-name]"), function (n, i) {
                var $this = $(n);
                if ($this.attr("data-brand-name").search(rk) >= 0){
                    $this.show();
                    return $this;
                }
            });
            hideList.length ? this.$bSearchNoResult.hide() : this.$bSearchNoResult.show().find("strong").html(keyword);
        },
        /**
         * 重置品牌框
         * @private
         */
        _reset: function() {
            this.$bSearchNoResult.hide();
            //初始化品牌选择框
            this.$brandListUl.find("li").show();
            this.$bSearchBrandKeyword.val('');
        }
    }

    return new GoodsBrand;
});
/**
 * 商品自定义属性模块
 */
ncDefine("nc.custom", [], function() {
    var GoodsCustom = function() {
        //合并配置
        $.extend(this, GoodsCustom.setting);
    };
    GoodsCustom.setting = { //自定义品牌区域
        $customPanel: $("#customPanel")
    };
    GoodsCustom.prototype = {
        /**
         * 获取要发送的数据
         */
        getPostData: function() {
            var inputList = this.$customPanel.find("input"),
                result = [];
            $.each(inputList, function(i, n) {
                var $n = $(n);
                if($.trim($n.val()) != '') {
                    result.push({
                        customId: $n.data("custom-id"),
                        customValue: $n.val()
                    })
                }
            })
            return result.length ? JSON.stringify(result) : '';
        }
    }
    return new GoodsCustom;
});
/**
 * 富文本编辑器图片组件
 * 图片展示区域用show 和hide 控制
 */
ncDefine("nc.editorPic", [], function() {
    var EditorPic = function() {
        var that = this;
        this.currPic = 0;
        //合并配置
        $.extend(this, EditorPic.setting);
        this.$parDiv = this.$editorPicShowPanel.closest("div");
        //上传图片提示
        this.uploadOption.add = function(e, data) {
            that.currPic++;
            data.submit();
        };
        this.uploadOption.change = function(e, data) {
            that.currPic = 0;
        }

        this._bindEvents();
    };
    EditorPic.setting = {
        //图片上传按钮
        $editorPicUploadBtn: $("#editorPicUploadBtn"),
        //上传后图片展示区域
        $editorPicShowPanel: $("#editorPicShowPanel"),

        //富文本编辑器id
        editorId: "editor",
        //上传设置,其他设置在 Goodpic 中
        uploadOption: {
            dataType: 'json',
            url: ncGlobal.sellerRoot + "image/upload.json",
            formData: {
                type: ncGlobal.filesType.goods
            },
            done: function(e, data) {
                if(data.result.code == 200) {
                    //发送上传成功事件
                    EditorPic.setting.$editorPicShowPanel.triggerHandler("imageupload.succeed", [data.result.data]);
                } else {
                    Nc.alertError("文件上传失败");
                }
            }
        },
        tmplLi: '<li class="ncsc-goodspic-upload">' +
        '<div class="upload-thumb">' +
        '<img src="{url}">' +
        '</div>' +
        '<div class="upload-thumb-handle">' +
        '<a href="javascript:void(0)" data-btn-insert class="btn btn-xs btn-info m-r-5" title="插入">插入</a>' +
        '<a href="javascript:void(0)" data-btn-del class="btn btn-xs btn-danger" title="删除">删除</a>' +
        '</div>' +
        '</li>'
    };
    EditorPic.prototype = {
        _bindEvents: function() {

            var that = this;
            //图像上传
            this.$editorPicUploadBtn.fileupload(this.uploadOption);
            //图片上传成功后的事件
            this.$editorPicShowPanel.on("imageupload.succeed", function(e, data) {
                var liHtml = that.tmplLi.replace("{url}", data.url);
                that.$parDiv.show();
                that.$editorPicShowPanel.append(liHtml);
                that.currPic !== 0 && layer.msg('<div class="nc-layer-msg">已上传' + that.currPic  + '张图片</div>')
            });
            //点击删除图片事件,和点击图片插入到编辑器中
            this.$editorPicShowPanel
                .on("click", "a[data-btn-del]", function() {
                    $(this).closest("li").remove();
                    that.$editorPicShowPanel.find("li").length || that.$parDiv.hide();
                })
                .on("click", "a[data-btn-insert]", function() {
                    var $this = $(this),
                        $img = $this.parents("li:first").find(".upload-thumb");
                    $img.length && UE.getEditor(that.editorId).execCommand('insertHtml', $img.html());
                })
        }
    }
    return new EditorPic;
});
/**
 * 商品所在地模块,
 *
 */
ncDefine("nc.goodsArea", [], function() {
    var options = {
        $goodsArea: $("#goodsArea")
    }
    /**
     * 直接输出一个自运行函数
     */
    return function() {
        options.$goodsArea.NcArea({
            showDeep: 2,
            dataHiddenName: "areaId"

        })
            .on("nc.select.last", function(e, element) {

            })
            .on("nc.select.selected", function(e, element) {

            });
    }()

});
/**
 * 运费组件
 */
ncDefine("nc.goosFreight", [], function() {
    /**
     * by hbj
     */
    var ajaxLoadFreightTemplate = function(freightId) {
        $.ajax({
            type: 'GET',
            url: ncGlobal.sellerRoot + "freight/template/info/json/" + freightId,
            dataType: 'json'
        }).success(function(result) {
            if('200' == result.code) {
                $("#freightTemplateInfo").show();
                $("#freightTemplateInfo").find("a:first").attr("href", ncGlobal.sellerRoot + "freight/template/add?calcType=" + result.data.calcType + "&freightId=" + freightId);
                $("#freightInfoArea").html(result.data.area);
                $("#freightInfoCalcRule").html(result.data.calcRule);
            } else {
                $("#freightTemplateInfo").hide();
                Nc.alertError(result.message);
            }
        }).error(function() {
            $("#freightTemplateInfo").hide();
            Nc.alertError("请求失败");
        });
    };

    var Freight = function() {
        $.extend(this, Freight.setting);
        this.$radio = this.$FreightDl.find("input:radio");
        this.$div = this.$FreightDl.find("span");
        this._bindEvents();
    };
    Freight.setting = {
        //最外层
        $FreightDl: $("#freightDl")

    }
    Freight.prototype = {
        _bindEvents: function() {
            var that = this;
            this.$radio.click(function() {
                var $this = $(this);
                that.$div.hide();
                $this.siblings("span").show();
                if($this.val() == 1) {
                    that.$FreightDl.find("input[name=goodsFreight]").val(0)
                    $("#freightTemplateInfo").hide();
                } else {
                    that.$FreightDl.find("select[name=freightTemplateId]").val(0);
                }
            });
            that.$FreightDl.find("select[name=freightTemplateId]").on("click", function() {
                if($(this).val() == "0") {
                    return;
                }
                var freightId = $(this).val();
                ajaxLoadFreightTemplate(freightId);
            });

        },
        /**
         * 获取post数据
         */
        getPostData: function() {
            var that = this;

        }
    }
    //return new Freight;
    return {
        init: function() {
            if (typeof ncGlobal.freightTemplateId != "undefined" && ncGlobal.freightTemplateId != "0") {
                ajaxLoadFreightTemplate(ncGlobal.freightTemplateId);
            }
            return new Freight;
        },

        ajaxLoadFreightTemplate: ajaxLoadFreightTemplate
    }
});

/**
 * 根据原列表，改变sku goods 列表上的值
 */
ncDefine("goodsEdit.cache", [], function() {
    var _cache = {};
    var $specDivBody = $("#spec-div-body");

    /**
     * 私有方法
     * @type {Object}
     */
    var __ = {
        /**
         * 根据td获取specvalueid数组
         */
        getSpecValueArray: function($el) {

            return $.map($el.find("[data-spec-value-id]"), function(n) {
                return $(n).attr("data-spec-value-id");
            });
        },
        /**
         *
         * @return {[type]} [description]
         */
        getSameData: function(specValueArray) {
            var result;
            $.each($specDivBody.find("tr"), function(index, el) {
                var $el = $(el),
                    a = __.getSpecValueArray($el),
                //数组交集
                    b = Nc.array.intersection(a, specValueArray);
                if(a && b && (a.toString() == b.toString())) {
                    result = $el;
                }
            });
            return result;
        },
        setTr: function(newTr, oldTr) {
            var $newTr = $(newTr),
                $oldTr = $(oldTr);
            $newTr.find("input[data-marketprice]").val($oldTr.find("input[data-marketprice]").val());
            $newTr.find("input[data-price]").val($oldTr.find("input[data-price]").val());
            $newTr.find("input[data-stock]").val($oldTr.find("input[data-stock]").val());
            $newTr.find("input[data-alarm]").val($oldTr.find("input[data-alarm]").val());
            $newTr.find("input[data-sku]").val($oldTr.find("input[data-sku]").val());
            $newTr.find("input[data-barcode]").val($oldTr.find("input[data-barcode]").val());
            $newTr.attr("data-goods-id", $oldTr.attr("data-goods-id"));
        }
    };
    /**
     * 监听事件
     */
    var listerEvents = function() {

        //监听sku准备刷新事件
        Nc.eventManger.on("refreshSKU.refresh", function(e, $el, key) {
            //console.log('sku准备刷新事件', $el, " key is", key);
            _cache[key] = $el;
            //console.log("_cache", _cache);
        });
        //sku框刷新完成事件
        Nc.eventManger.on("refreshSKU.finish", function(e, key,keyOld) {
            //console.log("sku框刷新完成事件,key is ", key);
            var a = _cache[key] ? _cache[key] :(_cache[keyOld] ? _cache[keyOld]:'')
            _refreshSkuFormCache(a);
        });
    };
    /**
     * 刷新数据根据原数据
     */
    var _refreshSkuFormCache = function(_cache) {
        _cache&&$.each(_cache.find("tr"), function(index, el) {
            var $tr = $(el),
                a = __.getSpecValueArray($tr),
                b = __.getSameData(a);
            if(b && b.length) {
                __.setTr(b, el);
            }
            ;
        });
    };

    ///////////////////////////////
    return {
        factory: function() {
            listerEvents();
        }
    };
})
/**
 * 商品图片缓存
 */
ncDefine("goodsEdit.colorPic", [], function() {
    "use strict";
    var colorPicList = {};
    var goodsPicList = "#goods-pic-list";

    /**
     * 保存colorpiclist
     * @return {[type]} [description]
     */
    var saveColorPicList = function() {
        $.each($(goodsPicList).find(".ncsc-goodspic-list"), function(i, n) {
            var $n = $(n), colorId = $n.data("spce-value-id");
            colorPicList[colorId] = $n.find("ul").clone(true, true);
        });
    };
    /**
     * 渲染goods 列表框
     * @return {[type]} [description]
     */
    var renderGoodsPanel = function() {

        //查找goods 框上规格相等的
        $.each($(goodsPicList).find(".ncsc-goodspic-list"), function(i, n) {
            var $n = $(n), colorId = $n.data("spce-value-id");
            if(colorPicList.hasOwnProperty(colorId)) {
                var a = $n.find('.goods-pic-list').replaceWith(colorPicList [colorId]);
            }
            ;
        });
    };

    /**
     * 监听事件
     */
    function listenEvents() {
        var r_sku = function(){
            Nc.eventManger.on("refreshSKU.refresh", function(e, $el) {
                saveColorPicList();
            });
            //sku框刷新完成事件
            Nc.eventManger.on("refreshSKU.finish", function() {
                setTimeout(renderGoodsPanel, 0);
            });
        }
        //判断是添加还是编辑
        if (Nc.isEmpty(ncGlobal.isEdit)) {
            r_sku();
        } else {
            Nc.eventManger.on("specValueUpload.finish", function () {
                r_sku();
            });
        }
    }

    return {
        factory: function() {
            listenEvents();
        }
    };
});


/**
 * by shopnc.feng
 * input 验证
 */
ncDefine("nc.verify", [], function() {

    $('#goodsForm').validate({
        // invalidHandler: function(form, validator) {
        // 	$(form).find('.alert-error').show();
        // },
        errorPlacement: function(error, element) {
            error.appendTo(element.parent());
            $('#layoutRight').is(":animated") || $('#layoutRight').animate({
                scrollTop: element.parent().scrollTop()
            }, 200)
        },
        // errorLabelContainer: $("#goodsForm div.error"),
        // submitHandler: function (form) {
        // 	$(form).ajaxSubmit();
        // },
        rules: {
            goodsName: {
                required: true,
                rangelength: [3, 50]
            },
            jingle: {
                maxlength: 140
            },
            freightVolume: {
                required: false,
                number: true
            },
            freightWeight: {
                required: false,
                number: true
            }
        },
        messages: {
            goodsName: {
                required: '<i class="icon-exclamation-sign"></i>请填写商品名称',
                rangelength: '<i class="icon-exclamation-sign"></i>商品名称长度在3到50字符之间'
            },
            jingle: {
                maxlength: '<i class="icon-exclamation-sign"></i>广告词最大长度不能超过140个字符'
            }
        }
    });
});

/**
 * 批量设置库存
 */
ncDefine("nc.batch", [], function() {
    var $specDiv = $("#spec_div");
    //绑定事件
    var bindEvents = function() {
        //显示
        $specDiv.on('click', '[data-batch] i', function(event) {
            event.preventDefault();
            var $this = $(this),
                $div = $this.closest("div[data-batch]").find("div:first");
            $specDiv.find("div[data-batch-panel]").hide();
            $div.show().find("input").val("");
        }).on('click', 'a[data-batch-close]', function(event) {
            event.preventDefault();
            $(this).closest('div').hide()
        }).on('click', 'a[data-batch-mod]', function(event) {
            event.preventDefault();
            var $this = $(this),
                $div = $this.closest('div[data-batch]'),
                type = $div.data("batch"),
                value = $div.find("input").val(),
                goal = $("#spec-div-body").find("input[data-" + type + "]");

            if(type == 'price' || type == 'marketprice') {
                value = Nc.number.round(value, 2);
            } else {
                value = parseInt(value);
            }
            if(type == 'alarm' && value > 255) {
                value = 255;
            }
            if(isNaN(value)) {
                value = 0;
            }
            goal.val(value);
            $this.closest("div").hide()
        });
        //设置库存
    }
    ////////
    bindEvents();
});
/**
 * 购买第二步
 * @param {[type]} $ [description]
 */



//TODO:先放到外面方便测试
var ncGoodsCustomModule;
var ncEditorPicModule;
var ncGoodsClassModule;
var addStepTwo = function($) {

    var
    //表单框
        $GoodsForm = $("#goodsForm"),
    //商品发布提交按钮
        $formSubmit = $("#formSubmit"),
        ncSpecmodule,
        ncGoodsPicModule,
        ncAddSpecModule,
        ncGoodsBrandModule,

        ncGoodsAreaModule,
        ncGoosFreight;


    function _buildElement() {
        //生成规格组件
        ncSpecmodule = ncRequire("nc.spec")({
            specUrl: ncGlobal.sellerRoot + "goods/get/spec.json",
            $el: $("#specPanel")
        });
        //商品图片模块
        ncGoodsPicModule = ncRequire("nc.goods.pic")();
        //添加规格对话框组件
        ncAddSpecModule = ncRequire("nc.addspec")();
        //商品品牌组件
        ncGoodsBrandModule = ncRequire("nc.goodsBrand");
        //店铺分类组件
        ncGoodsClassModule = ncRequire("nc.storeClass");
        //自定义shuxing组件
        ncGoodsCustomModule = ncRequire("nc.custom");
        //编辑器图片组件
        ncEditorPicModule = ncRequire("nc.editorPic");
        //地区
        ncGoodsAreaModule = ncRequire("nc.goodsArea");

        ncGoosFreight = ncRequire("nc.goosFreight").init();
        //input验证
        ncRequire("nc.verify");
        //
        ncRequire("nc.batch");
    }

    function _bindEvent() {
        /**
         * 商品发布提交
         */
        $("#formSubmit").click(function(event) {
            var specData = ncSpecmodule.getSpecPostData(),
                specValueData = ncSpecmodule.getSpecValuePostData(),
                picData = ncGoodsPicModule.getPicListData(),
            //店铺分类数据
                storeClassData = ncGoodsClassModule.getDataPost(),
            // 自定义属性
                goodsCustomData = ncGoodsCustomModule.getPostData(),
                isPost = $formSubmit.attr("disabled"),
                paramPost = [];

            if(isPost || !$('#goodsForm').valid()) {
                return;
            }

            paramPost.push("specJson=" + (specData.length ? encodeURI(JSON.stringify(specData)) : ""))

            paramPost.push("goodsJson=" + (specValueData.length ? encodeURI(JSON.stringify(specValueData)) : ""));

            //验证商品图片不能为空
            if(!ncGoodsPicModule.verfiySpecGoodsPic()) {
                Nc.alertError("请上传商品图片");
                return;
            }
            paramPost.push("goodsPic=" + (picData.length ? encodeURI(JSON.stringify(picData)) : ""));

            storeClassData && (paramPost.push(encodeURI(storeClassData)));

            goodsCustomData.length && (paramPost.push("custom=" + encodeURI(goodsCustomData)))

            //按钮变灰
            $formSubmit.attr("disabled", "disabled");

            paramPost = paramPost.length ? ("&" + paramPost.join("&")) : '';
            //发送post 请求

            $.post(
                ncGlobal.sellerRoot + ($("#goodsCommonId").length ? "goods/update.json" : "goods/add/save.json"),
                $GoodsForm.serialize() + paramPost,
                function(data) {
                    if('200' == data.code) {
                        var successMessage = data.message ? data.message : '操作成功';
                        $("#goodsCommonId").length
                            ? (Nc.alertSucceed(successMessage, {
                            icon: 1,
                            time: 3000,
                            end: function() {
                                Nc.go(data.url);
                            }
                        }))
                            : (Nc.go(_getExtUrl(data)));


                    } else {
                        var
                            errorMessage = data.message ? data.message : "请求失败";
                        //判断是否绑定了事件
                        Nc.alertError(errorMessage);
                    }

                    //按钮变色
                    $formSubmit.removeAttr("disabled");
                }, "json"
            ).error(function() {
                    Nc.alertError("请求失败");
                    $formSubmit.removeAttr("disabled");
                });
        });
    }

    ///

    /**
     * 获取
     * @param data
     * @private
     */
    function _getExtUrl(data) {
        return data.url + "?" + Nc.urlEncode(data.data);
    }

    ///
    return {
        init: function() {
            _buildElement();
            _bindEvent();

            ncRequire("goodsEdit.cache").factory();
            ncRequire("goodsEdit.colorPic").factory();
            //
        }
    };
}(jQuery);
/**
 *
 * @param  {[type]} ) {	var        ue [description]
 * @return {[type]}   [description]
 */
$(function() {
    var ue = UE.getEditor('editor', {
        // scaleEnabled:true,
        initialFrameHeight: 400,
        textarea: "goodsBody",
        zIndex:1
    });
    //$("#spec-div-table").perfectScrollbar();
    addStepTwo.init();

    //应用编辑商品初始化组件,直接执行没有返回值
    ncRequire("goodsEdit.bootstarp");
});