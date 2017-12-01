jQuery slimScroll插件是一个支持把内容放在一个盒子里面，固定一个高度，超出的则使用滚动。

github:https://github.com/rochal/jQuery-slimScroll

代码使用：
===============================================================================
function setScroll(){
$(".box-list").slimScroll({
height: boxHeight,
alwaysVisible: true,
});
}
setScroll();
$(window).on("resize",setScroll);
===============================================================================

参数设置：
===============================================================================
$(selector).slimScroll({
width: '300px',
height: '500px',
size: '10px',
position: 'left',
color: '#ffcc00',
alwaysVisible: true,
distance: '20px',
start: $('#child_image_element'),
railVisible: true,
railColor: '#222',
railOpacity: 0.3,
wheelStep: 10,
allowPageScroll: false,
disableFadeOut: false
});
===============================================================================