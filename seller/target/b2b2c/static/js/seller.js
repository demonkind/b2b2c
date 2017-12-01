//头部导航下拉菜单相关
$(function () {
    $(".ncsc-nav dl").hover(function () {
        $(this).addClass("hover");
    },
    function () {
        $(this).removeClass("hover");
    });

	//表格列表图片缩略
	$('.table-list-thumb img').jqthumb({
		width: 60,
		height: 60,
		after: function (imgObj) {
		imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
		}
	});
	
	
	//顶部下拉
	$("#member-top-show").ncDropdown();
	$("#store-message-top-show").ncDropdown();


});