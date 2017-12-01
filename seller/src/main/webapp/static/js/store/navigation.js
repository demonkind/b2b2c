/**
 * Created by dqw on 2016/1/22.
 */
var list = function() {

   var _delete = function(id) {
       Nc.layerConfirm("确认删除该导航？", {
           postUrl: ncGlobal.sellerRoot + "store/navigation/del.json",
           postData: {
               id: id
           }
       });
   }

   var bindEvents = function() {
   };

   var _init = function() {};

   return {
   	  init: function() {
        _init();
        bindEvents();
      },
   	  delete: _delete
   };

}();

$(function() {
    list.init();
});


