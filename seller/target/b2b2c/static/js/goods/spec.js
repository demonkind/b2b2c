/**
 * Created by shopnc.feng on 2015-12-28.
 */
var spec = function() {
    var _editSpecId = "#editSpecId",
        _editSpecName = "#editSpecName",
        _ddEditSpecValue = "#ddEditSpecValue",
        _addSpecValue = "#addSpecValue";
    var _init = function() {
        $(_ddEditSpecValue)
            .on("click", "input[data-spec-value]", function() {
                $(this).parent().addClass("focus");
            })
            .on("click", "a[data-spec-value-ok]", function() {
                console.log("修改规格值modal 上的-确定-");
                var specValueId = $(this).parent().data("spec-value-id");
                var specValueName = $(this).parent().find("input[data-spec-value]").val();
                if(Nc.isEmpty($.trim(specValueName))) {
                    Nc.alertError("规格值不能为空");
                    return
                }
                _submitSpecValueName(specValueId, specValueName);
            })
            .on("click", "a[data-spec-value-cancel]", function() {
                var specValueId = $(this).parent().data("spec-value-id");
                _deleteSpecValue(specValueId);
            })
            .on("focusout", "input[data-spec-value]", function(event) {
                var $this = $(this),
                    $div = $this.parent(),
                    okBtn = $div.find('a[data-spec-value-ok]'),
                    cancelBtn = $div.find("a[data-spec-value-cancel]")
                    ;
                if(!$(event.relatedTarget).is(okBtn) && !$(event.relatedTarget).is(cancelBtn)) {
                    $this.closest(".spec-item").removeClass("focus");
                }
            });
        //
        $(_addSpecValue).on("focus", function() {
            $(this).parent().addClass("focus");
        });
        //bycj 编辑规格名称，获取焦点后显示修改按钮
        $(_editSpecName)
            .focus(function() {
                $(this).parent().addClass("focus");
            })
            .focusout(function(event) {
                if(!$(event.relatedTarget).is($("#editSpecNameBtn"))) {
                    $(this).parent().removeClass("focus");
                }
            });
    }
    // 编辑规格
    var _edit = function(specId) {
        $.post(ncGlobal.sellerRoot + "spec/edit.json",
            { specId: specId },
            function(data) {
                if(data.code == 200) {
                    $(_editSpecId).val(data.data.specId);
                    $(_editSpecName).val(data.data.specName).parent().removeClass("focus");
                    $(_ddEditSpecValue).html("");
                    for(var i = 0; i < data.data.specValueList.length; i++) {
                        var specValue = data.data.specValueList[i];
                        _html = '<div data-spec-value-id="' + specValue.specValueId + '" class="spec-item">' +
                            '<input data-spec-value type="text" class="text w300 m-r-10" value="' + specValue.specValueName + '"/>' +
                            '<a href="javascript:;" data-spec-value-ok class="btn btn-sm btn-primary m-r-5"><i class="icon icon-ok-sign"></i>确定</a>' +
                            '<a href="javascript:;" data-spec-value-cancel class="btn btn-sm btn-danger"><i class="icon icon-trash"></i>删除</a>' +
                            '</div>';
                        $(_ddEditSpecValue).append(_html);
                    }
                    //Nc.layerOpen({
                    //    type: 1,
                    //    title: "编辑规格“" + data.data.specName + "”",
                    //    content: $('#editModal'),
                    //    end: function () {
                    //        location.reload();
                    //    }
                    //});
                    layer.open({
                        type: 1,
                        title: "编辑规格“" + data.data.specName + "”",
                        content: $('#editModal'),
                        skin: "layer-small",
                        end: function() {
                            location.reload();
                        }
                    });
                } else {
                    Nc.alertError(data.message);
                }
            }, "json");
    }
    // 删除规格
    var _delete = function(name, specId) {
        Nc.layerConfirm("是否删除“" + name + "”规格", {
            postUrl: ncGlobal.sellerRoot + "spec/delete.json",
            postData: {
                specId: specId
            }
        });
    }
    // 修改规格名
    var _submitSpecName = function() {
        var specId = $(_editSpecId).val(),
            specName = $(_editSpecName).val();
        $.post(ncGlobal.sellerRoot + "spec/update/spec_name.json",
            { specId: specId, specName: specName },
            function(data) {
                if(data.code == 200) {
                    $(_editSpecName).parent().removeClass("focus");
                } else {
                    Nc.alertError(data.message);
                }
            }, "json");
    }
    // 修改规格值名
    var _submitSpecValueName = function(specValueId, specValueName) {
        $.post(ncGlobal.sellerRoot + "spec/update/spec_value_name.json",
            { specValueId: specValueId, specValueName: specValueName },
            function(data) {
                if(data.code == 200) {
                    $("div[data-spec-value-id='" + specValueId + "']").removeClass("focus");
                } else {
                    Nc.alertError(data.message);
                }
            }, "json");
    }
    // 删除规格值
    var _deleteSpecValue = function(specValueId) {
        $.post(ncGlobal.sellerRoot + "spec/delete/spec_value.json",
            { specValueId: specValueId },
            function(data) {
                if(data.code == 200) {
                    $("div[data-spec-value-id='" + specValueId + "']").remove();
                } else {
                    Nc.alertError(data.message);
                }
            }, "json")
    };
    // 添加规格值
    var _saveSpecValue = function() {
        var specId = $(_editSpecId).val(),
            specValueName = $(_addSpecValue).val();
        //bycj不能添加一个空的
        if(Nc.isEmpty($.trim(specValueName))) {
            return;
        }
        $.post(ncGlobal.sellerRoot + "spec/save/spec_value_name.json",
            { specId: specId, specValueName: specValueName },
            function(data) {
                if(data.code == 200) {
                    var specValue = data.data;
                    _html = '<div class="spec-item" data-spec-value-id="' + specValue.specValueId + '">' +
                        '<input data-spec-value type="text" class="text w300 m-r-10" value="' + specValue.specValueName + '"/>' +
                        '<a href="javascript:;" data-spec-value-ok class="btn btn-sm btn-primary m-r-5"><i class="icon icon-ok-sign"></i>确定</a>' +
                        '<a href="javascript:;" data-spec-value-cancel class="btn btn-sm btn-danger"><i class="icon icon-trash"></i>删除</a>' +
                        '</div>';
                    $(_ddEditSpecValue).append(_html);
                    specValueName = $(_addSpecValue).val("");
                    $(_addSpecValue).parent().removeClass("focus");
                } else {
                    Nc.alertError(data.message);
                }
            }, "json")
    };
    return {
        init: _init,
        edit: _edit,
        delete: _delete,
        submitSpecName: _submitSpecName,
        saveSpecValue: _saveSpecValue
    };
}()
/**
 * 添加一个规格
 */
var addSpecModel = function() {
    var $el;
    var specValueMax = 20;

    /**
     * 添加一个新增商品规格
     * @private
     */
    function _addSpecAndSpecValue() {
        console.log("验证需要添加的规格名称和各个规格值");

        if(!$el.addSpecForm.valid()) {
            return;
        }
        //改变input的name值
        $el.specValueInputGroup.find("input").attr("name", "specValueName");

        $.post(
            ncGlobal.sellerRoot + "spec/save.json",
            $el.addSpecForm.serialize(),
            function(data) {
                if(data.code == '200') {
                    layer.closeAll();
                    Nc.alertSucceed(data.message, {
                        end: function() {
                            Nc.go();
                        }
                    });

                } else {
                    Nc.alertError(data.message);
                }
            },
            "json"
        ).error(function() {
                Nc.alertError("添加失败")
            });
    };
    var bindEvents = function() {
        $el.addSpecBtn.on('click', function(event) {
            event.preventDefault();
            //还原对话框
            $el.addSpecForm.validate().resetForm();
            $el.specValueInputGroup.find("div:not(:first)").remove();
            $("#spec-input-name").find("input").val("");

            Nc.layerOpen({
                title: "新增商品规格",
                content: $el.addSpecModal,
                yes: function(index, layero) {
                    _addSpecAndSpecValue();
                }
                //$form: $el.addSpecForm,
                //async: true,
                //objSerializeType: false
            });
        });
        //点击添加规格值
        $el.addSpecValueBtn.on('click', function(event) {
            var $thisBtn = $(this),
                length = $el.specValueInputGroup.find("input").length,
                _div = '<div class="m-t-5" style="height: 30px;overflow: hidden;"><input type="text" class="text w300 m-r-10" data-rule-required="true" name="specValueName' +
                    Nc.randomString(16) + '"><a href="javascript:;" class="btn btn-sm btn-danger"><i class="icon icon-trash"></i>删除</a></div>',
                $div = $(_div);
            $div.find("a").on('click', function(event) {
                var length;
                $(this).closest('div').remove();
                $el.specValueInputGroup.find("input").length >= specValueMax ? $thisBtn.hide() : $thisBtn.show();
            });
            $el.specValueInputGroup.append($div);
            //如果超出限制就隐藏按钮
            $el.specValueInputGroup.find("input").length >= specValueMax ? $thisBtn.hide() : $thisBtn.show();
        });
    };
    var _init = function() {
        $el = {
            addSpecBtn: $("#addSpecBtn"),
            addSpecForm: $("#addSpecForm"),
            addSpecModal: $("#addSpecModal"),
            addSpecValueBtn: $("#addSpecValueBtn"),
            specValueInputGroup: $("#specValueInputGroup")
        };
    };
    //////
    return {
        init: function() {
            _init();
            bindEvents();
        }
    };
}();
$(function() {
    spec.init();
    addSpecModel.init();
});