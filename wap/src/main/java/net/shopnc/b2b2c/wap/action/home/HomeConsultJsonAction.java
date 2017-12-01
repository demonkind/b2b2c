package net.shopnc.b2b2c.wap.action.home;

import net.shopnc.b2b2c.domain.member.Consult;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.ConsultService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Created by zxy on 2016-03-14
 */
@Controller
public class HomeConsultJsonAction extends HomeBaseJsonAction {
    @Autowired
    private ConsultService consultService;

    /**
     * 保存咨询
     * @param consult
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "consult/add", method = RequestMethod.POST)
    public ResultEntity consultAdd(@Valid Consult consult,
                                   BindingResult bindingResult) {
        ResultEntity resultEntity = new ResultEntity();
        if (bindingResult.hasErrors()) {
            //获取所有错误信息
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.info(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("提交失败");
            return resultEntity;
        }
        consult.setMemberId(SessionEntity.getMemberId());
        try {
            consultService.addConsult(consult);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("提交成功");
            return resultEntity;
        } catch (ShopException e) {
            logger.warn(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
    }
}
