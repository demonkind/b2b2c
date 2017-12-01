/**
 * Created by dqw on 2016/1/8.
 */
var add = function() {

    /**
     * 获取已经选择的input的权限id
     * @private
     */
   function _getPostData() {
       return $.map($("input[data-menu2]:checked"), function (n) {
           return $(n).val();
       });
   }

   var bindEvents = function() {
       $("#checkAll").on("change", function(e) {
           var $this = $(this),
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
               menu2List = $("input[data-menu1-name='" + menu1Id + "']")
               ;
           if (isCheck) {
               menu2List.attr("checked", true);
           } else {
               menu2List.removeAttr("checked");
           }
       });

       $("input[data-menu2]").on("change", function (e) {
           var $this = $(this),
               groupId = $this.data("groupId"),
               isCheck = $this.is(':checked'),
               menu2List = $("input[data-group-id='" + groupId + "']")
               ;
           if (isCheck) {
               menu2List.attr("checked", true);
           } else {
               menu2List.removeAttr("checked");
           }
       });

       $("#addForm").validate({
          submitHandler: function(form) {
              $(form).ajaxSubmit();
          },
          onkeyup: false,
          rules:{
              groupName:{
                  required:true,
                  rangelength : [3,20]
              },
              groupPermission:{
                  required:true
              }
          },
          messages:{
              groupName:{
                  required	: '<i class="icon-exclamation-sign"></i>组名称不能为空'
              },
              groupPermission:{
                  required	: '<i class="icon-exclamation-sign"></i>请选择权限'
              }
          }
      });

       $("#formSubmit").on("click", function() {
           $("#groupPermission").val(JSON.stringify(_getPostData()));
           $("#addForm").submit();
       });
   };

   var _init = function() {};

   return {
   	  init: function() {
        _init();
        bindEvents();
      }
   };

}();

$(function() {
    add.init();
});


