/**
 * Created by shopnc on 2015/12/25.
 */
//操作处理开始
var OperateHandle = function() {
    function _bindEvent() {
        //新增地址事件
        $("#addressAdd").on("click", function() {
            $("input[name='realName']").val("");
            $("input[name='addressId']").val("");
            $("input[name='address']").val("");
            $("input[name='areaInfo']").val("");
            $("input[name='telPhone']").val("");
            $("input[name='mobPhone']").val("");
            $("input[name='isDefault']").prop(false);
            Nc.layerOpen({
                title: "新增地址",
                content: $('#addressDialog'),
                $form: $("#addressForm"),
                async: true
            });
            return false;
        });
        //编辑地址事件
        $("a[ncType='edit']").on("click", function() {
            var url = ncGlobal.memberRoot + "address/edit/" + $(this).attr("dataAddressId");
            addressGlobal.isSelectLast = true;
            var setInputValue = function(data) {
                var $editAddressForm = $("#editAddressForm");
                $.each(data, function(index, el) {
                    if (index != "isDefault") {
                        var $el = $editAddressForm.find("input[name='" + index + "']");
                        $el.length && ($el.val(el))
                    }
                });
            };
            $.ajax({
                dataType: 'json',
                url: url,
                type: 'GET'
            }).success(function(result) {

                if (result.code == 200) {
                    Nc.layerOpen({
                        title: "编辑地址",
                        content: $('#editAddressDialog'),
                        $form: $("#editAddressForm"),
                        async: true
                    });
                    setInputValue(result.data);
                    $("#editAddressPanel").attr("data-area-text", result.data.areaInfo);
                    $("#editAddressPanel").attr("data-area-id", result.data.areaId);
                    $("#editAddressForm").find("input[name='isDefault']").prop("checked",result.data.isDefault == 1 ? true : false);

                    $("#editAddressPanel").data("nc.area").restart();

                } else {
                    Nc.alertError(result.message);
                }
            }).error(function() {
                NC.alertError("请求失败");
            });

            return false;
        });
        $("a[ncType='delete']").on("click",function(){
            var url = ncGlobal.memberRoot + "address/del/";
            var addressId = $(this).attr("dataAddressId");
            Nc.layerConfirm("删除后将无法恢复，确认删除吗？",{
                postUrl : url,
                postData : {addressId:addressId}

            });
        });
        //地址选择
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
            showDeep: 4,
            deepInputAddType: "clear" //mod|clear
        }).on("nc.select.selected", function(e) {
            addressGlobal.isSelectLast = false;
        }).on("nc.select.last", function(e) {
            addressGlobal.isSelectLast = true;
        });

        $("#editAddressPanel").NcArea({
            hiddenInput: [{
                name: "areaId",
                value: "0"
            }, {
                name: "areaInfo",
                value: ""
            }],
            url: ncGlobal.webRoot + 'area/list.json/',
            dataHiddenName: "areaId",
            showDeep: 4
        }).on("nc.select.selected", function(e) {
            addressGlobal.isSelectLast = false;
        }).on("nc.select.last", function(e) {
            addressGlobal.isSelectLast = true;
        });
        //表单验证
        jQuery.validator.addMethod("isSelectLast", function(value, element) {
            return addressGlobal.isSelectLast;
        }, "请将地区选择完整");

        $("#addressForm").validate({
            errorPlacement: function(error, element) {
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
                    required: addressGlobal.isInputAddPhone
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
                    required: "电话/手机号码至少填写一项"
                }
            },
            groups: {
                phone: "telPhone mobPhone"
            }
        })
        $("#editAddressForm").validate({
            errorPlacement: function(error, element) {
                if (element.attr("name") == "areaId") {
                    $("#editAddressPanel").append(error);
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
                    required: addressGlobal.isInputEditPhone
                },
                mobPhone: {
                    required: addressGlobal.isInputEditPhone
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
                    required: "电话/手机号码至少填写一项"
                }
            },
            groups: {
                phone: "telPhone mobPhone"
            }
        })
    }
    //外部可调用
    return {
        bindEvent: _bindEvent
    }
}();
//操作处理结束

var addressGlobal = {
    //是否选择到最后一级
    isSelectLast: false,
    isInputAddPhone: function($form) {
        return ($("#addressForm").find("input[name='telPhone']").val() == '') && ($("#addressForm").find("input[name='mobPhone']").val() == '')
    },
    isInputEditPhone: function($form) {
        return ($("#editAddressForm").find("input[name='telPhone']").val() == '') && ($("#editAddressForm").find("input[name='mobPhone']").val() == '')
    }
}
$(function() {
    //页面绑定事件
    OperateHandle.bindEvent();

});