$(function(){
	initRaty();
});

/**
 * 小星星
 */
function initRaty(){
	$('.raty').raty({
        path: ncGlobal.publicRoot + "toolkit/jquery.raty/img",
        readOnly: true,
        width: 80,
        hints:['很不满意', '不满意', '一般', '满意', '很满意'],
        score: function() {
          return $(this).attr('data-score');
        }
    });
}