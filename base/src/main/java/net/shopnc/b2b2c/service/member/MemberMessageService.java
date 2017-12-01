package net.shopnc.b2b2c.service.member;

import net.shopnc.b2b2c.constant.MessageTemplateCommonTplClass;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.MessageTemplateCommonDao;
import net.shopnc.b2b2c.dao.member.MemberMessageDao;
import net.shopnc.b2b2c.dao.member.MemberMessageSettingDao;
import net.shopnc.b2b2c.domain.MessageTemplateCommon;
import net.shopnc.b2b2c.domain.member.MemberMessage;
import net.shopnc.b2b2c.domain.member.MemberMessageSetting;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.message.MessageClassVo;
import net.shopnc.common.entity.PageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shopnc.feng on 2016-02-19.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class MemberMessageService extends BaseService {
    @Autowired
    MemberMessageDao memberMessageDao;
    @Autowired
    MessageTemplateCommonDao messageTemplateCommonDao;
    @Autowired
    MemberMessageSettingDao memberMessageSettingDao;

    /**
     * 查询会员消息列表
     * @param memberId
     * @param tplClass
     * @param page
     * @return
     */
    public HashMap<String, Object> findList(int memberId, int tplClass, int page) {

        PageEntity pageEntity = new PageEntity();
        pageEntity.setPageNo(page);
        List<MemberMessage> memberMessageList;
        if (tplClass == 1) {
            pageEntity.setTotal(memberMessageDao.findCountByMemberId(memberId));
            memberMessageList = memberMessageDao.findByMemberId(memberId, pageEntity.getPageNo(), pageEntity.getPageSize());
        } else {
            pageEntity.setTotal(memberMessageDao.findCountByMemberId(memberId, tplClass));
            memberMessageList = memberMessageDao.findByMemberId(memberId, tplClass, pageEntity.getPageNo(), pageEntity.getPageSize());
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("memberMessageList", memberMessageList);
        map.put("showPage", pageEntity.getPageHtml());

        return map;
    }

    /**
     * 删除消息
     * @param messageIds
     * @param memberId
     * @throws Exception
     */
    public void deleteMessage(Integer[] messageIds, int memberId) throws Exception {
        if (messageIds.length <= 0) {
            return;
        }
        for (int i = 0; i<messageIds.length; i++) {
            MemberMessage memberMessage = memberMessageDao.get(MemberMessage.class, messageIds[i]);
            if (memberMessage != null && memberMessage.getMemberId() != memberId) {
                throw new Exception("参数错误");
            }
            memberMessageDao.delete(memberMessage);
        }
    }

    public void markReadByMemberId(int memberId, Integer[] messageId) {
        memberMessageDao.updateIsReadByMemberId(State.YES, memberId, messageId);
    }

    /**
     * 会员消息模板
     * @param memberId
     * @return
     */
    public List<MessageClassVo> findMessageTemplateMember(int memberId) {
        List<MessageClassVo> messageClassList = new MessageTemplateCommonTplClass().getMemberMessageClassVoList();
        for (MessageClassVo messageClass : messageClassList) {
            List<MessageTemplateCommon> messageTemplateCommonList = messageTemplateCommonDao.findMessageTemplateMemberByTplClass(messageClass.getId());
            if (messageTemplateCommonList != null) {
                for (MessageTemplateCommon messageTemplateCommon : messageTemplateCommonList) {
                    if (messageClass.getId() == messageTemplateCommon.getTplClass()) {
                        MemberMessageSetting MemberMessageSetting = memberMessageSettingDao.getByTplCodeAndMemberId(messageTemplateCommon.getTplCode(), memberId);
                        if (MemberMessageSetting == null || MemberMessageSetting.getIsReceive() == State.YES) {
                            messageTemplateCommon.setIsReceive(State.YES);
                        }
                    }
                }
            }
            messageClass.setMessageTemplateCommonList(messageTemplateCommonList);
        }
        return messageClassList;
    }

    /**
     * 消息接收设置
     * @param tplCode
     * @param isReceive
     * @param memberId
     */
    public void save(String tplCode,int isReceive, int memberId) {
        // 删除原有设置
        memberMessageSettingDao.deleteByTplCodeAndMemberId(tplCode, memberId);
        if (isReceive == State.YES) {
            return;
        }
        MemberMessageSetting memberMessageSetting = new MemberMessageSetting();
        memberMessageSetting.setIsReceive(isReceive == State.YES ? State.YES : State.NO);
        memberMessageSetting.setMemberId(memberId);
        memberMessageSetting.setTplCode(tplCode);
        memberMessageSettingDao.save(memberMessageSetting);
    }
}
