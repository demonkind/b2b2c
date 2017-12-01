package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.MessageTemplateSystemDao;
import net.shopnc.b2b2c.domain.MessageTemplateCommon;
import net.shopnc.b2b2c.domain.MessageTemplateSystem;
import net.shopnc.b2b2c.service.MessageTemplateService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 消息模板
 * Created by shopnc.feng on 2016-01-29.
 */
@Controller
public class MessageTemplateJsonAction extends BaseJsonAction {
    @Autowired
    MessageTemplateService messageTemplateService;
    @Autowired
    MessageTemplateSystemDao messageTemplateSystemDao;

    /**
     * 其他消息JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "message_template/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = messageTemplateService.getSystemDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.error(e.toString());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 更新其他消息
     * @param messageTemplateSystem
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "message_template/system/update.json", method = RequestMethod.POST)
    public ResultEntity save(MessageTemplateSystem messageTemplateSystem) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            messageTemplateSystemDao.update(messageTemplateSystem);
            resultEntity.setCode(ResultEntity.SUCCESS);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }

    /**
     * 会员、店铺消息JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "message_template/common/list.json", method = RequestMethod.POST)
    public DtGrid commonListJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = messageTemplateService.getCommonDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.error(e.toString());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }

    /**
     * 更新会员、店铺消息模板站内信
     * @param messageTemplateCommon
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "message_template/common/notice/update.json", method = RequestMethod.POST)
    public ResultEntity commonNoticeUpdate(MessageTemplateCommon messageTemplateCommon,
                                           BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();

        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("更新失败");
            return resultEntity;
        }

        try {
            messageTemplateService.updateCommonNotice(messageTemplateCommon);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("更新成功");
        } catch (Exception e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("更新失败");
        }

        return resultEntity;
    }

    /**
     * 更新短信模板内容
     * @param messageTemplateCommon
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "message_template/common/sms/update.json", method = RequestMethod.POST)
    public ResultEntity commonSmsUpdate(MessageTemplateCommon messageTemplateCommon,
                                        BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("更新失败");
            return resultEntity;
        }

        try {
            messageTemplateService.updateCommonSms(messageTemplateCommon);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("更新成功");
        } catch (Exception e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("更新失败");
        }

        return resultEntity;
    }

    /**
     * 更新邮件模板内容
     * @param messageTemplateCommon
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "message_template/common/email/update.json", method = RequestMethod.POST)
    public ResultEntity commonEmailUpdate(MessageTemplateCommon messageTemplateCommon,
                                          BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("更新失败");
            return resultEntity;
        }

        try {
            messageTemplateService.updateCommonEmail(messageTemplateCommon);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("更新成功");
        } catch (Exception e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("更新失败");
        }
        return resultEntity;
    }
}
