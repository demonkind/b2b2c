package net.shopnc.b2b2c.web.action.member;

import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.FavoritesGoodsService;
import net.shopnc.b2b2c.service.member.FavoritesStoreService;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
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
public class MemberFavoritesJsonAction extends MemberBaseJsonAction {

    @Autowired
    private FavoritesGoodsService favoritesGoodsService;
    @Autowired
    private FavoritesStoreService favoritesStoreService;

    /**
     * 删除关注商品
     * @param favoritesId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "favorites/goods/del", method = RequestMethod.POST)
    public ResultEntity delGoods(@RequestParam(value = "favid") Integer favoritesId) {
        ResultEntity resultEntity = new ResultEntity();
        try{
            favoritesGoodsService.delFavoritesGoods(favoritesId, SessionEntity.getMemberId());
            resultEntity.setCode(ResultEntity.SUCCESS);
        }catch (ShopException e){
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return  resultEntity;
    }
    /**
     * 删除关注店铺
     * @param favoritesId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "favorites/store/del", method = RequestMethod.POST)
    public ResultEntity delStore(@RequestParam(value = "favid") Integer favoritesId) {
        ResultEntity resultEntity = new ResultEntity();
        try{
            favoritesStoreService.delFavoritesStore(favoritesId, SessionEntity.getMemberId());
            resultEntity.setCode(ResultEntity.SUCCESS);
        }catch (ShopException e){
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return  resultEntity;
    }
}