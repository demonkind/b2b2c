/**
 * Created by cj on 2016/1/5.
 */
var groupAdd = function ($) {

    "use strict";
    var $elemnt = {
        form: $("#form"),
        menuPanel: $("#menuPanel")
    };

    /**
     * 获取已经选择的input的权限id
     * @private
     */
    function _getPostData() {
        return $.map($("input[data-menu2]:checked"), function (n) {
            return $(n).val();
        });
    }

    function _bindEvent() {
        $("#adminMenuAll").on("change", function(e) {
            var $this = $(this),
                menu1Id = $this.val(),
                isCheck = $this.is(':checked'),
                checkList = $("#divCheck").find(":checkbox")
                ;
            if (isCheck) {
                checkList.attr("checked", true);
            } else {
                checkList.removeAttr("checked");
            }
        });

        $("input[data-menu1]").on("change", function (e) {
            var $this = $(this),
                menu1Id = $this.val(),
                isCheck = $this.is(':checked'),
                menu2List = $("input[data-menu1-id='" + menu1Id + "']")
                ;
            if (isCheck) {
                menu2List.attr("checked", true);
            } else {
                menu2List.removeAttr("checked");
            }
        });

        //表单提交按钮
        $("#formSubmit").on('click', function () {
            var a = _getPostData();
            if(a.length == 0){
                $.ncAlert({
                    content: '<div class="alert alert-danger m-b-0"><h4><i class="fa fa-info-circle"></i>&nbsp;请选择权限</h4></div>',
                    autoCloseTime: 2
                });
                return;
            }
            $(this).attr("nc-data-permission",JSON.stringify(a));
            $(this).qSubmit();
        });
    }

    return {
        init: function () {
            _bindEvent();
        }
    }
}(jQuery);
$(function () {
    groupAdd.init();
});
