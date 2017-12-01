/**
 * Created by dqw on 2016/1/22.
 */
var add = function() {

   var bindEvents = function() {
        var ue = UE.getEditor('editor', {
            // scaleEnabled:true,
            initialFrameHeight: 400,
            textarea: "content"
        });

       $("#addForm").validate({
          submitHandler: function(form) {
              $(form).ajaxSubmit();
          },
          onkeyup: false,
          rules:{
              title:{
                  required:true,
                  rangelength : [2,10]
              },
              sort:{
                  required: true,
                  min: 0,
                  max: 999
              }
          },
          messages:{
              groupName:{
                  required	: '<i class="icon-exclamation-sign"></i>导航名称不能为空'
              },
              sort:{
                  required	: '<i class="icon-exclamation-sign"></i>排序不能为空'
              }
          }
      });

       $("#formSubmit").on("click", function() {
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


