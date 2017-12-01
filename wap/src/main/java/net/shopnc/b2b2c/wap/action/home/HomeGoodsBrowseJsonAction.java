package net.shopnc.b2b2c.wap.action.home;

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
 * Created by zxy on 2016-03-14
 */
@Controller
public class HomeGoodsBrowseJsonAction extends HomeBaseJsonAction {

    @Autowired
    private GoodsBrowseService goodsBrowseService;

    /**
     * 添加商品浏览历史
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "goods/browse/add", method = RequestMethod.POST)
    public ResultEntity addBrowse(@RequestParam(value = "goods_id") Integer goodsId) {
        ResultEntity resultEntity = new ResultEntity();
        if (goodsId==null || goodsId<=0) {
            resultEntity.setMessage("参数错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        if (SessionEntity.getIsLogin()==true && SessionEntity.getMemberId()>0) {
            //加入数据库
            try{
                goodsBrowseService.addGoodsBrowseToDatabase(goodsId, SessionEntity.getMemberId());
                resultEntity.setCode(ResultEntity.SUCCESS);
                return resultEntity;
            }catch (ShopException e){
                logger.error(e.getMessage());

                resultEntity.setMessage(e.getMessage());
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }
        }else{
            //加入cookie
            try {
                goodsBrowseService.addGoodsBrowseToCookie(goodsId);
                resultEntity.setCode(ResultEntity.SUCCESS);
                return resultEntity;
            }catch (ShopException e){
                logger.error(e.getMessage());

                resultEntity.setMessage(e.getMessage());
                resultEntity.setCode(ResultEntity.FAIL);
                return resultEntity;
            }
        }
    }
}