package net.shopnc.b2b2c.service;

import net.shopnc.b2b2c.constant.MessageTemplateCommonTplType;
import net.shopnc.b2b2c.constant.MessageTemplateSendType;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.MessageTemplateCommonDao;
import net.shopnc.b2b2c.dao.MessageTemplateSystemDao;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.member.MemberMessageDao;
import net.shopnc.b2b2c.dao.member.MemberMessageSettingDao;
import net.shopnc.b2b2c.dao.store.StoreMessageDao;
import net.shopnc.b2b2c.dao.store.StoreMessageSettingDao;
import net.shopnc.b2b2c.domain.MessageTemplateCommon;
import net.shopnc.b2b2c.domain.MessageTemplateSystem;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.MemberMessage;
import net.shopnc.b2b2c.domain.member.MemberMessageSetting;
import net.shopnc.b2b2c.domain.store.Seller;
import net.shopnc.b2b2c.domain.store.StoreMessage;
import net.shopnc.b2b2c.domain.store.StoreMessageSetting;
import net.shopnc.b2b2c.service.store.SellerService;
import net.shopnc.common.util.SendMessageHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2016-02-03.
 *
 *    发送验证码类消息
 *    sendMessageService.sendSystem("ceshi1", "13800138000", "22222");
 *    sendMessageService.sendSystem("ceshi2", "shopnc@shopnc.net", "asdfasdf");
 *
 *    发送店铺消息
 *    HashMap<String, Object> map = new HashMap<>();
 *    map.put("siteName", "B2B2C JAVA版");
 *    map.put("sendTime", "2016-11-11");
 *    map.put("verifyCode", "1212121");
 *    map.put("verifyUrl", "http://www.shopnc.net");
 *    sendMessageService.sendStore("ceshi1", 2, map, "ssssssssssss");
 *
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class SendMessageService extends BaseService {
    @Autowired
    MessageTemplateSystemDao messageTemplateSystemDao;
    @Autowired
    MessageTemplateCommonDao messageTemplateCommonDao;
    @Autowired
    SendMessageHelper sendMessageHelper;
    @Autowired
    SellerService sellerService;
    @Autowired
    StoreMessageDao storeMessageDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    SiteService siteService;
    @Autowired
    StoreMessageSettingDao storeMessageSettingDao;
    @Autowired
    MemberMessageSettingDao memberMessageSettingDao;
    @Autowired
    MemberMessageDao memberMessageDao;

    /**
     * 发送消息
     * @param tplCode 模板编号
     * @param number 号码  如：手机号或邮箱
     * @param code 代码  如：动态码、激活链接
     */
    public void sendSystem(String tplCode, String number, String code) {
        MessageTemplateSystem messageTemplateSystem = messageTemplateSystemDao.get(MessageTemplateSystem.class, tplCode);
        if (messageTemplateSystem == null) {
            logger.error("tplCode：" + tplCode + "，不存在    messageTemplateSystem");
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("siteName", siteService.getSiteInfo().get(SiteTitle.SITENAME));
        String title = sendMessageHelper.replace(messageTemplateSystem.getTplTitle(), map);
        String content = sendMessageHelper.replace(messageTemplateSystem.getTplContent(), map);
        switch (messageTemplateSystem.getSendType()) {
            case MessageTemplateSendType.EMAIL :
                sendMessageHelper.pushQueueEmail(number, title, content);
                break;
            case MessageTemplateSendType.SMS:
                sendMessageHelper.pushQueueSms(number, content);
                break;
        }
    }

    /**
     * 会员消息发送
     * @param tplCode
     * @param memberId
     * @param map
     * @param sn
     */
    public void sendMember(String tplCode, int memberId, HashMap<String, Object> map, String sn) {
        // 验证会员时候 接收该条消息
        MemberMessageSetting memberMessageSetting = memberMessageSettingDao.getByTplCodeAndMemberId(tplCode, memberId);
        if (memberMessageSetting != null && memberMessageSetting.getIsReceive() == State.NO) {
            return;
        }
        MessageTemplateCommon messageTemplateCommon = messageTemplateCommonDao.get(MessageTemplateCommon.class, tplCode);
        if (messageTemplateCommon == null) {
            logger.error("tplCode：" + tplCode + "，不存在    messageTemplateCommon");
            return;
        }
        if (messageTemplateCommon.getTplType() != MessageTemplateCommonTplType.MEMBER) {
            logger.error("不是会员的消息模板，tplCode：" + tplCode + "    messageTemplateCommon");
            return;
        }
        Member member = memberDao.get(Member.class, memberId);
        if (member == null) {
            logger.error("不存在会员账户，memberId：" + memberId + "，tplCode：" + tplCode);
            return;
        }

        map.put("siteName", siteService.getSiteInfo().get(SiteTitle.SITENAME));

        // 站内信
        String noticeContent = sendMessageHelper.replace(messageTemplateCommon.getNoticeContent(), map);
        MemberMessage memberMessage = new MemberMessage();
        memberMessage.setMemberId(memberId);
        memberMessage.setTplCode(tplCode);
        memberMessage.setAddTime(ShopHelper.getCurrentTimestamp());
        memberMessage.setMessageContent(noticeContent);
        memberMessage.setSn(sn);
        memberMessage.setTplClass(messageTemplateCommon.getTplClass());
        memberMessageDao.save(memberMessage);

        // 短信
        String smsContent = sendMessageHelper.replace(messageTemplateCommon.getSmsContent(), map);
        if (messageTemplateCommon.getSmsState() == State.YES && member.getMobile().length() > 0) {
            sendMessageHelper.pushQueueSms(member.getMobile(), smsContent);
        }

        // 邮件
        String emailTitle = sendMessageHelper.replace(messageTemplateCommon.getEmailTitle(), map);
        String emailContent = sendMessageHelper.replace(messageTemplateCommon.getEmailContent(), map);
        if (messageTemplateCommon.getEmailState() == State.YES && member.getEmail().length() > 0) {
            sendMessageHelper.pushQueueEmail(member.getEmail(), emailTitle, emailContent);
        }
    }

    /**
     * 商家消息发送
     * @param tplCode
     * @param storeId
     * @param map
     */
    public void sendStore(String tplCode, int storeId, HashMap<String, Object> map, String sn) {
        // 验证商家时候接收消息
        StoreMessageSetting storeMessageSetting = storeMessageSettingDao.getByTplCodeAndStoreId(tplCode, storeId);
        if (storeMessageSetting != null && storeMessageSetting.getIsReceive() == State.NO) {
            return;
        }

        MessageTemplateCommon messageTemplateCommon = messageTemplateCommonDao.get(MessageTemplateCommon.class, tplCode);
        if (messageTemplateCommon == null) {
            logger.error("tplCode：" + tplCode + "，不存在    messageTemplateCommon");
            return;
        }
        if (messageTemplateCommon.getTplType() != MessageTemplateCommonTplType.SELLER) {
            logger.error("不是商家的消息模板，tplCode：" + tplCode + "    messageTemplateCommon");
        }
        List<Seller> sellerList = sellerService.findSellerForMessage(tplCode, storeId);
        if (sellerList == null) {
            logger.error("不存在商家账户，storeId：" + storeId + "，tplCode：" + tplCode);
            return;
        }
        map.put("siteName", siteService.getSiteInfo().get(SiteTitle.SITENAME));
        String noticeContent = sendMessageHelper.replace(messageTemplateCommon.getNoticeContent(), map);
        String smsContent = sendMessageHelper.replace(messageTemplateCommon.getSmsContent(), map);
        String emailTitle = sendMessageHelper.replace(messageTemplateCommon.getEmailTitle(), map);
        String emailContent = sendMessageHelper.replace(messageTemplateCommon.getEmailContent(), map);

        for (Seller seller : sellerList) {
            // 发送站内信
            StoreMessage storeMessage = new StoreMessage();
            storeMessage.setMessageContent(noticeContent);
            storeMessage.setSellerId(seller.getSellerId());
            storeMessage.setAddTime(ShopHelper.getCurrentTimestamp());
            storeMessage.setTplClass(messageTemplateCommon.getTplClass());
            storeMessage.setSn(sn);
            storeMessage.setTplCode(messageTemplateCommon.getTplCode());
            storeMessageDao.save(storeMessage);

            // 发送短信
            if (messageTemplateCommon.getSmsState() == State.YES && seller.getSellerMobile().length() > 0) {
                sendMessageHelper.pushQueueSms(seller.getSellerMobile(), smsContent);
            }

            // 发送邮件
            if (messageTemplateCommon.getEmailState() == State.YES && seller.getSellerEmail().length() > 0) {
                sendMessageHelper.pushQueueEmail(seller.getSellerEmail(), emailTitle, emailContent);
            }
        }
    }
}
