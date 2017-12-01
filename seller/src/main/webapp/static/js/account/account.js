/**
 * Created by dqw on 2016/1/7.
 */
var list = function() {
	var $el;
	var bindEvents = function() {
	    $("#addForm").validate({
            submitHandler: function(form) {
                $(form).ajaxSubmit();
            },
            onkeyup: false,
            rules:{
                sellerName :{
                    required:true,
                    rangelength : [3,20],
                    remote   : {
                        url :ncGlobal.sellerRoot +'register/is_seller_exist',
                        type:'get',
                        data:{
                            sellerName : function() {
                                return $('#sellerName').val();
                            }
                        }
                    }
                },
                password : {
                    required: true
                },
                password2 : {
                    required: true,
                    equalTo: "#password"
                },
                groupId : {
                    required: true
                }
            },
            messages:{
                sellerName :{
                    required	: '<i class="icon-exclamation-sign"></i>账号名不能为空',
                    remote		:	'<i class="icon-exclamation-sign"></i>账号已存在'
                },
                password : {
                    required	: '<i class="icon-exclamation-sign"></i>账号密码不能为空'
                },
                password2 : {
                    required	: '<i class="icon-exclamation-sign"></i>重复密码不能为空',
                    equalTo: "两次输入的密码不一致"
                },
                groupId : {
                    required	: '<i class="icon-exclamation-sign"></i>请选择账号组'
                }
            }
        });

        $("#editForm").validate({
            submitHandler: function(form) {
                $(form).ajaxSubmit();
            },
            onkeyup: false,
            rules:{
                password2 : {
                    equalTo: "#editPassword"
                },
                groupId : {
                    required: true
                }
            },
            messages:{
                password2 : {
                    equalTo: "两次输入的密码不一致"
                },
                groupId : {
                    required	: '<i class="icon-exclamation-sign"></i>请选择账号组'
                }
            }
        });

		$el.addBtn.on('click', function(event) {
			event.preventDefault();
			$("#sellerName").val("");
			$("#password").val("");
			$("#password2").val("");
			$("#groupId").val("");
			Nc.layerOpen({
				title: "添加账号",
				content: $el.addModal,
				$form: $el.addForm,
                async: true,
                objSerializeType:false
			});
		});
	};

	var _init = function() {
		$el = {
			addBtn: $("#addBtn"),
			addForm: $("#addForm"),
			addModal: $("#addModal"),
            editOwnerForm: $("#editOwnerForm"),
            editOwnerModal: $("#editOwnerModal"),
			editForm: $("#editForm"),
			editModal: $("#editModal")
		};
	};

    var _editOwner = function(item) {
        $item = $(item);
        $("#editOwnerSellerId").val($item.data("seller-id"));
        $("#editOwnerSellerName").val($item.data("seller-name"));
        $("#editOwnerSellerEmail").val($item.data("email"));
        $("#editOwnerSellerMobile").val($item.data("mobile"));
        Nc.layerOpen({
            title: "编辑账号",
            content: $el.editOwnerModal,
            $form: $el.editOwnerForm,
            async: true,
            objSerializeType:false
        });
    }

	var _edit = function(item) {
	    $item = $(item);
	    $("#editSellerId").val($item.data("seller-id"));
	    $("#editSellerName").val($item.data("seller-name"));
        $("#editSellerEmail").val($item.data("email"));
        $("#editSellerMobile").val($item.data("mobile"));
	    $("#editGroupId").val($item.data("groupId"));
		Nc.layerOpen({
		    title: "编辑账号",
		    content: $el.editModal,
		    $form: $el.editForm,
            async: true,
            objSerializeType:false
		});
	};

    var _delete = function(sellerId) {
        Nc.layerConfirm("确认删除该账号？", {
            postUrl: ncGlobal.sellerRoot + "account/del.json",
            postData: {
                sellerId: sellerId
            }
        });
    }

	return {
		init: function() {
			_init();
			bindEvents();
		},
        editOwner: _editOwner,
		edit: _edit,
		delete: _delete
	};
}();

$(function() {
	list.init();
});