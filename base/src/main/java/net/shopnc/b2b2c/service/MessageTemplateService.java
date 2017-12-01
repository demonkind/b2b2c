package net.shopnc.b2b2c.service;


import net.shopnc.b2b2c.dao.MessageTemplateCommonDao;
import net.shopnc.b2b2c.dao.MessageTemplateSystemDao;
import net.shopnc.b2b2c.domain.MessageTemplateCommon;
import net.shopnc.b2b2c.domain.MessageTemplateSystem;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zxy on 2016-01-07.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class MessageTemplateService extends BaseService {
    @Autowired
    MessageTemplateSystemDao messageTemplateSystemDao;
    @Autowired
    MessageTemplateCommonDao messageTemplateCommonDao;

    /**
     * 获取后台系统消息数据表格
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getSystemDtGridList(String dtGridPager) throws Exception {
        return messageTemplateSystemDao.getDtGridList(dtGridPager, MessageTemplateSystem.class);
    }

    /**
     * 获取后台用户、店铺消息数据表格
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getCommonDtGridList(String dtGridPager) throws Exception {
        return messageTemplateCommonDao.getDtGridList(dtGridPager, MessageTemplateCommon.class);
    }

    /**
     * 后台更新站内消息
     * @param messageTemplateCommonNew
     */
    public void updateCommonNotice(MessageTemplateCommon messageTemplateCommonNew) {
        MessageTemplateCommon messageTemplateCommon = messageTemplateCommonDao.get(MessageTemplateCommon.class, messageTemplateCommonNew.getTplCode());
        messageTemplateCommon.setNoticeContent(messageTemplateCommonNew.getNoticeContent());
        messageTemplateCommonDao.update(messageTemplateCommon);
    }

    /**
     * 后台更新短信消息
     * @param messageTemplateCommonNew
     */
    public void updateCommonSms(MessageTemplateCommon messageTemplateCommonNew) {
        MessageTemplateCommon messageTemplateCommon = messageTemplateCommonDao.get(MessageTemplateCommon.class, messageTemplateCommonNew.getTplCode());
        messageTemplateCommon.setSmsState(messageTemplateCommonNew.getSmsState());
        messageTemplateCommon.setSmsContent(messageTemplateCommonNew.getSmsContent());
        messageTemplateCommonDao.update(messageTemplateCommon);
    }

    /**
     * 后台更新邮件消息
     * @param messageTemplateCommonNew
     */
    public void updateCommonEmail(MessageTemplateCommon messageTemplateCommonNew) {
        MessageTemplateCommon messageTemplateCommon = messageTemplateCommonDao.get(MessageTemplateCommon.class, messageTemplateCommonNew.getTplCode());
        messageTemplateCommon.setEmailState(messageTemplateCommonNew.getEmailState());
        messageTemplateCommon.setEmailTitle(messageTemplateCommonNew.getEmailTitle());
        messageTemplateCommon.setEmailContent(messageTemplateCommonNew.getEmailContent());
        messageTemplateCommonDao.update(messageTemplateCommon);
    }
}