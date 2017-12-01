/**
 * Created by dqw on 2016/1/7.
 */
var serviceList = function() {
    var itemString;
    var $preList;
    var $afterList;

    //添加客服
    var addService = function($list, service) {
        $item = $($.parseHTML(itemString));
        if(!Nc.isEmpty(service)) {
            $item.find('[data-shop-nc="name"]').val(service.name);
            $item.find('[data-shop-nc="type"]').val(service.type);
            $item.find('[data-shop-nc="num"]').val(service.num);
        }
        $item.find('[data-shop-nc="btnDel"]').on("click", function() {
            $(this).parents('[data-shop-nc="item"]').remove();
        });
        $list.append($item);
    }

    //初始化加载现有客服
    var initService = function(json, $list) {
	    if(json == "" || json == "[]") {
	        addService($list);
	    } else {
	      var serviceList = $.parseJSON(json);
	      $.each(serviceList, function(index, service) {
	          addService($list, service);
          });
	    }
    }

    //设置Post数据
    var getPostData = function($list, $input) {
        var serviceList = [];
        $list.find('[data-shop-nc="item"]').each(function(index) {
            var $this = $(this);
            var service = {};
            service.name = $this.find('[data-shop-nc="name"]').val();
            service.type= $this.find('[data-shop-nc="type"]').val();
            service.num= $this.find('[data-shop-nc="num"]').val();
            if(service.name != "" && service.num != "") {
                serviceList.push(service);
            }
        });
        $input.val(JSON.stringify(serviceList));
    }

	var _init = function() {
	    itemString = $("#_serviceItem").html();
	    $preList = $("#preList");
	    $afterList = $("#afterList");
	    $storePresales = $("#storePresales");
	    $storeAftersales = $("#storeAftersales");

	    initService($storePresales.val(), $preList);
	    initService($storeAftersales.val(), $afterList);
	};

	var bindEvents = function() {

        //添加售前客服
	    $('[data-shop-nc="btnAddPre"]').on("click", function() {
	        addService($preList);
	    });

        //添加售后客服
	    $('[data-shop-nc="btnAddAfter"]').on("click", function() {
	        addService($afterList);
	    });

	    //提交表单
	    $("#formSubmit").on("click", function() {
	        getPostData($preList, $storePresales);
	        getPostData($afterList, $storeAftersales);
	        $("#addForm").ajaxSubmit();
	    });
	};

	return {
		init: function() {
			_init();
			bindEvents();
		}
	};
}();

$(function() {
	serviceList.init();
});