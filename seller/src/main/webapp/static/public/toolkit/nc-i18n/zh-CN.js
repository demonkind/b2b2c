/**
 * 语言文件
 * Created by cj on 2015/11/20.
 * 使用方法举例：
 * $lang.login.username
 * $lang['login']['username']
 */
(function (window) {
    window.$lang = window.$lang || {
            login: {
                "username": "用戶名",
                "password": "密码"
            },
            brand: {
                "applyState1": "审核通过",
                "applyState0": "等待审核",
                "applyState10": "审核失败",
                "showType0": "文字",
                "showType1": "图片",
                "isRecommend1":"是",
                "isRecommend0":"否"

            },
            member: {
                "memberStateOpen": "开启",
                "memberStateClose": "关闭",
                "allowBuy1": "允许",
                "allowBuy0": "禁止",
                "allowTalk1": "允许",
                "allowTalk0": "禁止",
                "memberSex0": "保密",
                "memberSex1": "男",
                "memberSex2": "女"
            },
            seller: {
                "allowLogin":"允许登录",
                "notAllowLogin":"禁止登录",
            },
			shopCompany: {
                "shipTypeState1": "是",
                "shipTypeState0": "否",
                "shipStateState1": "开启",
                "shipStateState0": "关闭"
            },
            article: {
                "recommendState1": "是",
                "recommendState0": "否",
				"allowDelete1": "是",
                "allowDelete0": "否"
            },
            explog: {
                "operationStageLogin": "会员登录",
                "operationStageRegister": "会员注册",
                "operationStageComments": "商品评论",
                "operationStageOrders": "订单消费"
            },
            pointslog: {
                "operationStageLogin": "会员登录",
                "operationStageRegister": "会员注册",
                "operationStageComments": "商品评论",
                "operationStageOrders": "订单消费",
                "operationStageAdmin": "管理员增减"
            },
            storeJoinin: {
                "s10": "新申请",
                "s15": "初审失败",
                "s20": "初审成功",
                "s30": "缴费完成",
                "s35": "缴费审核失败",
                "s90": "审核成功"
            },
            predeposit: {
                "rechargeStateNotpay": "未支付",
                "rechargeStatePaid": "已支付",
                "cashStateNotDealwith": "未处理",
                "cashStateSuccess": "已支付",
                "cashStateFail": "拒绝提现"
            },
            store: {
                "open": "开启",
                "close": "关闭"
            },
            goods: {
                "own": "自营",
                "other": "三方",
                "goodsState0": "仓库中",
                "goodsState1": "出售中",
                "goodsState10": "违规禁售",
                "goodsVerify0": "等待审核",
                "goodsVerify1": "审核通过",
                "goodsVerify10": "审核失败"
            },
            paymentState: {
                "open": "开启",
                "close": "关闭"
            },
            messageTemplateState: {
                "open": "开启",
                "close": "关闭"
            }
    };
})(window);
