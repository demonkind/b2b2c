package net.shopnc.b2b2c.wap.action.member;

import net.shopnc.b2b2c.service.member.MemberMessageService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 会员消息
 * Created by shopnc.feng on 2016-02-18.
 */
@Controller
public class MemberMessageJsonAction extends MemberBaseJsonAction {
    @Autowired
    MemberMessageService memberMessageService;

    /**
     * 消息删除
     * @param messageId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "message/delete.json", method = RequestMethod.POST)
    public ResultEntity delete(Integer[] messageId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            memberMessageService.deleteMessage(messageId, SessionEntity.getMemberId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("删除成功");
        } catch (Exception e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("删除失败");
        }
        return resultEntity;
    }

    /**
     * 消息标记已读
     * @param messageId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "message/mark_read.json", method = RequestMethod.POST)
    public ResultEntity markRead(Integer[] messageId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            memberMessageService.markReadByMemberId(SessionEntity.getMemberId(), messageId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }

        return resultEntity;
    }

    /**
     * 消息接收设置
     * @param tplCode
     * @param isReceive
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "message/setting/save.json", method = RequestMethod.POST)
    public ResultEntity save(String tplCode,
                             int isReceive) {
        ResultEntity resultEntity = new ResultEntity();
        try{
            memberMessageService.save(tplCode, isReceive, SessionEntity.getMemberId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("设置成功");
        } catch (Exception e) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("设置失败");
        }
        return resultEntity;
    }
}
