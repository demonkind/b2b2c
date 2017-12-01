//上传图片
function fileupload(){
	$(".uploadinput").fileupload({
        dataType: 'json',
        formData: {type:ncGlobal.filesType.setting},
        url: ncGlobal.sellerRoot + "image/upload.json",
        done: function (e,data) {
            if (data.result.code == 200) {
            	var html="<img src=\""+data.result.data.url+"\" style\"width:60px;height:60px\" />";
            	$("#costicon").html(html);
            	$("#ctaCostimg").val(data.result.data.name);
            } else {
				Nc.alertError(data.result.message);
            }
        }
    });
}

$(function(){
	fileupload();
	
	$("#submitbtn").on("click",function(){
		$("#form1").ajaxSubmit();
	});
});