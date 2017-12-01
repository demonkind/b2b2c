package net.shopnc.b2b2c.wap.action.member;

import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.GoodsBrowseService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
public class MemberGoodsBrowseJsonAction extends MemberBaseJsonAction {

    @Autowired
    private GoodsBrowseService goodsBrowseService;

    /**
     * 删除商品浏览历史
     * @param browseId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goodsbrowse/del", method = RequestMethod.POST)
    public ResultEntity del(@RequestParam(value = "browseId") Integer browseId) {
        ResultEntity resultEntity = new ResultEntity();
        try{
            goodsBrowseService.delGoodsBrowse(SessionEntity.getMemberId(), browseId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            return  resultEntity;
        }catch (ShopException e){
            logger.info(e.getMessage());
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return  resultEntity;
        }
    }
    /**
     * 删除全部商品浏览历史
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goodsbrowse/del/all", method = RequestMethod.POST)
    public ResultEntity delAll() {
        ResultEntity resultEntity = new ResultEntity();
        try{
            goodsBrowseService.delGoodsBrowse(SessionEntity.getMemberId(), 0);
            resultEntity.setCode(ResultEntity.SUCCESS);
            return  resultEntity;
        }catch (ShopException e){
            logger.info(e.getMessage());
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return  resultEntity;
        }
    }
}