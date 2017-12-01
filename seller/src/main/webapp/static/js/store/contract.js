function join(itemId){
	var url=ncGlobal.sellerRoot + "contract/join";
	Nc.layerConfirm("申请加入保障服务",{
        postUrl:url,
        postData:{
        	itemId:itemId
        }
    });
}

function quit(itemId){
	var url=ncGlobal.sellerRoot + "contract/quit";
	Nc.layerConfirm("申请退出保障服务",{
        postUrl:url,
        postData:{
        	itemId:itemId
        }
    });
}