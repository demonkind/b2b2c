package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.member.ConsultService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-03-14
 */
@Controller
public class ConsultJsonAction extends BaseJsonAction {

    @Autowired
    private ConsultService consultService;

    /**
     * 回复咨询
     * @param consultReply
     * @param consultId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "consult/reply", method = RequestMethod.POST)
    public ResultEntity consultAdd(@RequestParam(value = "consultReply") String consultReply,
                                   @RequestParam(value = "consultId") Integer consultId) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            consultService.replyConsult(consultReply, consultId, SellerSessionHelper.getStoreId());
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("回复成功");
            return resultEntity;
        } catch (ShopException e) {
            logger.warn(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
    }
    /**
     * 删除单条咨询
     * @param consultId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "consult/del", method = RequestMethod.POST)
    public ResultEntity consultDel(@RequestParam(value = "consultId") Integer consultId) {
        ResultEntity resultEntity = new ResultEntity();
        if (consultId==null || consultId<=0) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("删除失败");
            return resultEntity;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("consultId", consultId);
        params.put("storeId", SellerSessionHelper.getStoreId());
        try {
            consultService.deleteConsult(params);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("删除成功");
            return resultEntity;
        } catch (ShopException e) {
            logger.warn(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
    }
    /**
     * 批量删除咨询
     * @param consultId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "consult/delbatch", method = RequestMethod.POST)
    public ResultEntity consultDelBatch(@RequestParam(value = "consultId") List<Integer> consultId) {
        ResultEntity resultEntity = new ResultEntity();
        if (consultId==null) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("请选择操作项");
            return resultEntity;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("consultIdIn", consultId);
        params.put("storeId", SellerSessionHelper.getStoreId());
        try {
            consultService.deleteConsult(params);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("删除成功");
            return resultEntity;
        } catch (ShopException e) {
            logger.warn(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
    }
}
