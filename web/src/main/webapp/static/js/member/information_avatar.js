var submiting = false;

//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {

        $("#avatar").fileupload({
            dataType: 'json',
            url: ncGlobal.memberRoot + "information/avatar",
            done: function (e, xhr) {
                if (xhr.result.code == 200) {
                    //暂时取消裁切图像功能
                    Nc.go(ncGlobal.memberRoot+"information/avatar");
                    return false;

                    $("#avatarCutForm").find("[name='avatarPath']").val(xhr.result.data.path);
                    $("#avatarCutForm").find("[name='avatarUrl']").val(xhr.result.data.url);
                    $("#nccropbox").attr("src",xhr.result.data.url+"?"+Math.random());
                    $("#preview").attr("src",xhr.result.data.url+"?"+Math.random());
                    $("#newAvatar").val(xhr.result.data.name);
                    $("#avatarCutForm").show();
                    $("#avatarForm").hide();
                    //预览
                    var imgWidth = xhr.result.data.imgWidth;
                    var imgHeight = xhr.result.data.imgHeight;
                    function showPreview(coords)
                    {
                        if (parseInt(coords.w) > 0){
                            var rx = 120 / coords.w;
                            var ry = 120 / coords.h;
                            $('#preview').css({
                                width: Math.round(rx * imgWidth) + 'px',
                                height: Math.round(ry * imgHeight) + 'px',
                                marginLeft: '-' + Math.round(rx * coords.x) + 'px',
                                marginTop: '-' + Math.round(ry * coords.y) + 'px'
                            });
                        }
                        $('#x1').val(coords.x);
                        $('#y1').val(coords.y);
                        $('#x2').val(coords.x2);
                        $('#y2').val(coords.y2);
                        $('#w').val(coords.w);
                        $('#h').val(coords.h);
                    }
                    //图片裁切
                    $('#nccropbox').Jcrop({
                        aspectRatio:1,
                        setSelect: [ 0, 0, 120, 120 ],
                        minSize:[50, 50],
                        allowSelect:false,
                        allowResize:true,
                        onChange: showPreview,
                        onSelect: showPreview
                    });
                }else{
                    $("#avatarCutForm").find("[name='avatarPath']").val("");
                    $("#avatarCutForm").find("[name='avatarUrl']").val("");
                    $("#avatarCutForm").hide();
                    $("#avatarForm").show();
                    Nc.alertError(xhr.result.message);
                }
                submiting = false;
            }
        });

        $("#submitCut").click(function(){
            var x1 = $('#x1').val();
            var y1 = $('#y1').val();
            var w = $('#w').val();
            var h = $('#h').val();
            var avatarPath = $("#avatarPath").val();
            var avatarName = $("#newAvatar").val();
            if(x1=="" || y1=="" || x2=="" || y2=="" || w=="" || h==""){
                Nc.alertError("您必须先选定一个区域");
                return false;
            }else{
                var params = {"avatarPath":avatarPath, "avatarName":avatarName, "width":w, "height":h, "x1":x1, "y1":y1};
                $.ajax({
                    type : "post",
                    url : ncGlobal.memberRoot + "information/avatar/cut",
                    data : params,
                    async : false,
                    success : function(xhr){
                        if (xhr.code == 200) {
                            Nc.alertSucceed("编辑成功",{end:function(){
                                window.location.href = ncGlobal.memberRoot+"information/avatar";
                            }});
                        }else{
                            Nc.alertError(xhr.message);
                        }
                        submiting = false;
                    }
                });
            }
        });
    }
    //外部可调用
    return {
        bindEvent: _bindEvent
    }
}();
//操作处理结束

$(function () {
    //页面绑定事件
    OperateHandle.bindEvent();

});