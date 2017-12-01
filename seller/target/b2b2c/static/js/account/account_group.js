/**
 * Created by dqw on 2016/1/8.
 */
var list = function() {

   var _delete = function(groupId) {
       Nc.layerConfirm("确认删除该账号组？", {
           postUrl: ncGlobal.sellerRoot + "account_group/del.json",
           postData: {
               groupId: groupId
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


