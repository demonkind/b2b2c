package net.shopnc.b2b2c.constant;

import net.shopnc.b2b2c.config.ShopConfig;

import java.util.HashMap;

/**
 * Created by shopnc.feng on 2016/3/10.
 */
public class MessageTemplateCommonUrl {
    public static final String MEMBER_ORDERS_CANCEL = ShopConfig.getMemberRoot() + "orders/info/{$sn}";
    public static final String MEMBER_ORDERS_EVALUATE_EXPLAIN = ShopConfig.getWebRoot()+"member/evaluate";
    public static final String MEMBER_ORDERS_MODIFY_FREIGHT = ShopConfig.getMemberRoot() + "orders/info/{$sn}";
    public static final String MEMBER_ORDERS_PAY = ShopConfig.getMemberRoot() + "orders/info/{$sn}";
    public static final String MEMBER_ORDERS_RECEIVE = ShopConfig.getMemberRoot() + "orders/info/{$sn}";
    public static final String MEMBER_ORDERS_SEND = ShopConfig.getMemberRoot() + "orders/info/{$sn}";
    public static final String MEMBER_PREDEPOSIT_CASH_FAIL = ShopConfig.getMemberRoot() + "predeposit/cash/info/{$sn}";
    public static final String MEMBER_PREDEPOSIT_CHANGE = ShopConfig.getMemberRoot() + "predeposit/log";
    public static final String MEMBER_REFUND_UPDATE = ShopConfig.getMemberRoot() + "refund/info/{$sn}";
    public static final String MEMBER_RETURN_UPDATE = ShopConfig.getMemberRoot() + "return/info/{$sn}";
    public static final String STORE_CONTRACT_APPLY = ShopConfig.getSellerRoot()+"contract/list";
    public static final String STORE_CONTRACT_COST_APPLY = ShopConfig.getSellerRoot()+"contract/list";
    public static final String STORE_CONTRACT_QUIT_APPLY = ShopConfig.getSellerRoot()+"contract/list";
    public static final String STORE_GOODS_STORAGE_ALARM =  ShopConfig.getSellerRoot() + "goods/online/list";
    public static final String STORE_GOODS_VERIFY =  ShopConfig.getSellerRoot() + "goods/offline/list";
    public static final String STORE_GOODS_VIOLATION =  ShopConfig.getSellerRoot() + "goods/offline/list";
    public static final String STORE_ORDERS_CANCEL = ShopConfig.getSellerRoot() + "orders/info/{$sn}";
    public static final String STORE_ORDERS_DELAY = ShopConfig.getSellerRoot() + "orders/info/{$sn}";
    public static final String STORE_ORDERS_EVALUATE = ShopConfig.getSellerRoot()+"evaluate";
    public static final String STORE_ORDERS_NEW = ShopConfig.getSellerRoot() + "orders/info/{$sn}";
    public static final String STORE_ORDERS_PAY = ShopConfig.getSellerRoot() + "orders/info/{$sn}";
    public static final String STORE_ORDERS_RECEIVE = ShopConfig.getSellerRoot() + "orders/info/{$sn}";
    public static final String STORE_REFUND = ShopConfig.getSellerRoot() + "refund/info/{$sn}";
    public static final String STORE_REFUND_AUTO_PROCESS = ShopConfig.getSellerRoot() + "refund/info/{$sn}";
    public static final String STORE_RETURN = ShopConfig.getSellerRoot() + "return/info/{$sn}";
    public static final String STORE_RETURN_AUTO_PROCESS = ShopConfig.getSellerRoot() + "return/info/{$sn}";
    public static final String STORE_RETURN_AUTO_RECEIPT = ShopConfig.getSellerRoot() + "return/info/{$sn}";

    public static HashMap<String, String> getHash() {
        HashMap<String, String> map = new HashMap<>();
        map.put("memberOrdersCancel", MEMBER_ORDERS_CANCEL);
        map.put("memberOrdersEvaluateExplain", MEMBER_ORDERS_EVALUATE_EXPLAIN);
        map.put("memberOrdersModifyFreight", MEMBER_ORDERS_MODIFY_FREIGHT);
        map.put("memberOrdersPay", MEMBER_ORDERS_PAY);
        map.put("memberOrdersReceive", MEMBER_ORDERS_RECEIVE);
        map.put("memberOrdersSend", MEMBER_ORDERS_SEND);
        map.put("memberPredepositCashFail", MEMBER_PREDEPOSIT_CASH_FAIL);
        map.put("memberPredepositChange", MEMBER_PREDEPOSIT_CHANGE);
        map.put("memberRefundUpdate", MEMBER_REFUND_UPDATE);
        map.put("memberReturnUpdate", MEMBER_RETURN_UPDATE);
        map.put("storeContractApply", STORE_CONTRACT_APPLY);
        map.put("storeContractCostApply", STORE_CONTRACT_COST_APPLY);
        map.put("storeContractQuitApply", STORE_CONTRACT_QUIT_APPLY);
        map.put("storeGoodsStorageAlarm", STORE_GOODS_STORAGE_ALARM);
        map.put("storeGoodsVerify", STORE_GOODS_VERIFY);
        map.put("storeGoodsViolation", STORE_GOODS_VIOLATION);
        map.put("storeOrdersCancel", STORE_ORDERS_CANCEL);
        map.put("storeOrdersDelay", STORE_ORDERS_DELAY);
        map.put("storeOrdersEvaluate", STORE_ORDERS_EVALUATE);
        map.put("storeOrdersNew", STORE_ORDERS_NEW);
        map.put("storeOrdersPay", STORE_ORDERS_PAY);
        map.put("storeOrdersReceive", STORE_ORDERS_RECEIVE);
        map.put("storeRefund", STORE_REFUND);
        map.put("storeRefundAutoProcess", STORE_REFUND_AUTO_PROCESS);
        map.put("storeReturn", STORE_RETURN);
        map.put("storeReturnAutoProcess", STORE_RETURN_AUTO_PROCESS);
        map.put("storeReturnAutoReceipt", STORE_RETURN_AUTO_RECEIPT);
        return map;
    }

    public static String getUrl(String tplCode) {
        HashMap<String, String> map = MessageTemplateCommonUrl.getHash();
        return map.get(tplCode);
    }
}
