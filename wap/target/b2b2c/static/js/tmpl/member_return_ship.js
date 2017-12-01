$(function(){
    var key = getCookie('key');
    var return_id = getQueryString("refund_id");
//    $.getJSON(ApiUrl+'/index.php?act=member_return&op=ship_form', {key:key,return_id:return_id}, function(result){
//        checkLogin(result.login);
//        $('#delayDay').html(result.datas.return_delay);
//        $('#confirmDay').html(result.datas.return_confirm);
//        for (var i=0; i<result.datas.express_list.length; i++) {
//            $('#express').append('<option value="'+result.datas.express_list[i].express_id+'">'+result.datas.express_list[i].express_name+'</option>');
//        }
//        
//
//        
//    });
});

$('.btn-l').click(function(){
    var refundId = $(this).attr("refundId");
    var shipId = $('#express').val();
    var shipSn = $('#shipSn').val();
    
    var data = {refundId:refundId,shipId:shipId,shipSn:shipSn};
    if (shipSn == '') {
        $.sDialog({
            skin:"red",
            content:'请填写快递单号',
            okBtn:false,
            cancelBtn:false
        });
        return false;
        
    }
    // 发货表单提交
    $.ajax({
        type:'post',
        url:WapSiteUrl+'/member/return/saveship',
        data:data,
        dataType:'json',
        async:false,
        success:function(result){
            checkLogin(result.login);
            if (result.code == 200) {
                $.sDialog({
                    skin:"red",
                    content:result.message,
                    okBtn:false,
                    cancelBtn:false
                });
                window.location.href = WapSiteUrl + '/member/return/list';
            }else{
            	$.sDialog({
                    skin:"red",
                    content:result.message,
                    okBtn:false,
                    cancelBtn:false
                });
            	return false;
            }
        }
    });
});