package net.shopnc.b2b2c.constant;

import net.shopnc.b2b2c.vo.message.MessageClassVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2016-02-16.
 */
public class MessageTemplateCommonTplClass {
    /**
     * 会员交易
     */
    public static final int MEMBER_ORDER = 1001;
    public static final String MEMBER_ORDER_TEXT = "交易消息";
    /**
     * 会员退换货
     */
    public static final int MEMBER_REFUND = 1002;
    public static final String MEMBER_REFUND_TEXT = "退换货消息";
    /**
     * 会员物流
     */
    public static final int MEMBER_SHIP = 1003;
    public static final String MEMBER_SHIP_TEXT = "物流消息";
    /**
     * 会员资产
     */
    public static final int MEMBER_ASSET = 1004;
    public static final String MEMBER_ASSET_TEXT = "资产消息";

    /**
     * 商家交易
     */
    public static final int STORE_ORDER = 2001;
    public static final String STORE_ORDER_TEXT = "交易消息";
    /**
     * 商家退换货
     */
    public static final int STORE_REFUND = 2002;
    public static final String STORE_REFUND_TEXT = "退换货消息";
    /**
     * 商家商品
     */
    public static final int STORE_GOODS = 2003;
    public static final String STORE_GOODS_TEXT = "商品消息";
    /**
     * 商家运营
     */
    public static final int STORE_OPERATION = 2004;
    public static final String STORE_OPERATION_TEXT = "运营消息";

    /**
     * 会员消息模板分类
     * @return
     */
    public static List<MessageClassVo> getMemberMessageClassVoList() {
        List<MessageClassVo> messageClassList = new ArrayList<>();
        /**
         * 交易
         */
        MessageClassVo messageClass1 = new MessageClassVo();
        messageClass1.setId(MessageTemplateCommonTplClass.MEMBER_ORDER);
        messageClass1.setName(MessageTemplateCommonTplClass.MEMBER_ORDER_TEXT);
        messageClassList.add(messageClass1);
        /**
         * 退换货
         */
        MessageClassVo messageClass2 = new MessageClassVo();
        messageClass2.setId(MessageTemplateCommonTplClass.MEMBER_REFUND);
        messageClass2.setName(MessageTemplateCommonTplClass.MEMBER_REFUND_TEXT);
        messageClassList.add(messageClass2);
        /**
         * 物流
         */
        MessageClassVo messageClass3 = new MessageClassVo();
        messageClass3.setId(MessageTemplateCommonTplClass.MEMBER_SHIP);
        messageClass3.setName(MessageTemplateCommonTplClass.MEMBER_SHIP_TEXT);
        messageClassList.add(messageClass3);
        /**
         * 资产
         */
        MessageClassVo messageClass4 = new MessageClassVo();
        messageClass4.setId(MessageTemplateCommonTplClass.MEMBER_ASSET);
        messageClass4.setName(MessageTemplateCommonTplClass.MEMBER_ASSET_TEXT);
        messageClassList.add(messageClass4);

        return messageClassList;
    }

    /**
     * 会员消息模板分类
     * @return
     */
    public static HashMap<Integer, String> getMemberClassMap() {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(MessageTemplateCommonTplClass.MEMBER_ORDER, MessageTemplateCommonTplClass.MEMBER_ORDER_TEXT);
        map.put(MessageTemplateCommonTplClass.MEMBER_REFUND, MessageTemplateCommonTplClass.MEMBER_REFUND_TEXT);
        map.put(MessageTemplateCommonTplClass.MEMBER_SHIP, MessageTemplateCommonTplClass.MEMBER_SHIP_TEXT);
        map.put(MessageTemplateCommonTplClass.MEMBER_ASSET, MessageTemplateCommonTplClass.MEMBER_ASSET_TEXT);
        return map;
    }


    /**
     * 商家消息模板分类
     * @return
     */
    public static List<MessageClassVo> getStoreMessageClassVoList() {
        List<MessageClassVo> messageClassList = new ArrayList<>();
        /**
         * 交易
         */
        MessageClassVo messageClass1 = new MessageClassVo();
        messageClass1.setId(MessageTemplateCommonTplClass.STORE_ORDER);
        messageClass1.setName(MessageTemplateCommonTplClass.STORE_ORDER_TEXT);
        messageClassList.add(messageClass1);
        /**
         * 退换货
         */
        MessageClassVo messageClass2 = new MessageClassVo();
        messageClass2.setId(MessageTemplateCommonTplClass.STORE_REFUND);
        messageClass2.setName(MessageTemplateCommonTplClass.STORE_REFUND_TEXT);
        messageClassList.add(messageClass2);
        /**
         * 商品
         */
        MessageClassVo messageClass3 = new MessageClassVo();
        messageClass3.setId(MessageTemplateCommonTplClass.STORE_GOODS);
        messageClass3.setName(MessageTemplateCommonTplClass.STORE_GOODS_TEXT);
        messageClassList.add(messageClass3);
        /**
         * 运营
         */
        MessageClassVo messageClass4 = new MessageClassVo();
        messageClass4.setId(MessageTemplateCommonTplClass.STORE_OPERATION);
        messageClass4.setName(MessageTemplateCommonTplClass.STORE_OPERATION_TEXT);
        messageClassList.add(messageClass4);

        return messageClassList;
    }


    /**
     * 商家消息模板分类
     * @return
     */
    public static List<Integer> getStoreClassList() {
        List<Integer> tplClassList = new ArrayList<>();
        tplClassList.add(MessageTemplateCommonTplClass.STORE_ORDER);
        tplClassList.add(MessageTemplateCommonTplClass.STORE_REFUND);
        tplClassList.add(MessageTemplateCommonTplClass.STORE_GOODS);
        tplClassList.add(MessageTemplateCommonTplClass.STORE_OPERATION);
        return tplClassList;
    }

    /**
     * 会员消息模板分类
     * @return
     */
    public static HashMap<Integer, String> getStoreClassMap() {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(MessageTemplateCommonTplClass.STORE_ORDER, MessageTemplateCommonTplClass.STORE_ORDER_TEXT);
        map.put(MessageTemplateCommonTplClass.STORE_REFUND, MessageTemplateCommonTplClass.STORE_REFUND_TEXT);
        map.put(MessageTemplateCommonTplClass.STORE_GOODS, MessageTemplateCommonTplClass.STORE_GOODS_TEXT);
        map.put(MessageTemplateCommonTplClass.STORE_OPERATION, MessageTemplateCommonTplClass.STORE_OPERATION_TEXT);
        return map;
    }
}
