package net.shopnc.b2b2c.wap.action.home;

import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.FavoritesGoodsService;
import net.shopnc.b2b2c.service.member.FavoritesStoreService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zxy on 2016-01-18
 */
@Controller
public class HomeFavoritesJsonAction extends HomeBaseJsonAction {

    @Autowired
    private FavoritesGoodsService favoritesGoodsService;
    @Autowired
    private FavoritesStoreService favoritesStoreService;

    /**
     * 关注商品
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "favorite/goods/addFavoritesGoods", method = RequestMethod.POST)
    public ResultEntity favoriteGoods(HttpServletRequest request) {
        ResultEntity resultEntity = new ResultEntity();
        Integer goodsId=Integer.valueOf(request.getParameter("goods_id"));
        String key=request.getParameter("key");
        if (goodsId==null || goodsId<=0) {
            resultEntity.setMessage("参数错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //验证是否登录
        if (key==null||key.equals("")) {
            HashMap<String, Object> returnMap = new HashMap<String, Object>();
            returnMap.put("errorType", "noLogin");
            resultEntity.setData(returnMap);
            resultEntity.setMessage("请登录");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        try{
            String favoritesId=favoritesGoodsService.addFavoritesGoods(goodsId, Integer.valueOf(key));
            resultEntity.setMessage("关注成功");
            resultEntity.setData(favoritesId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            return resultEntity;
        } catch (ShopException e){
            logger.info(e.getMessage());
            //抛出异常
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
    }
    
    /**
     * 删除关注商品
     * @param favoritesId
     * @return
     */ 
    @ResponseBody
    @RequestMapping(value = "favorites/goods/del", method = RequestMethod.POST)
    public ResultEntity delGoods(HttpServletRequest request) {
        ResultEntity resultEntity = new ResultEntity();
        try{
        	Integer favoritesId=Integer.valueOf(request.getParameter("favoritesId"));
        	Integer key=Integer.valueOf(request.getParameter("key"));
            favoritesGoodsService.delFavoritesGoods(favoritesId, key);
            resultEntity.setCode(ResultEntity.SUCCESS);
        }catch (ShopException e){
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return  resultEntity;
    }
    
    /**
     * 关注店铺
     * @param storeId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "favorite/store", method = RequestMethod.POST)
    public ResultEntity favoriteStore(@RequestParam("storeId") Integer storeId) {
        ResultEntity resultEntity = new ResultEntity();
        if (storeId==null || storeId<=0) {
            resultEntity.setMessage("参数错误");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        //验证是否登录
        if (SessionEntity.getIsLogin()==false) {
            HashMap<String, Object> returnMap = new HashMap<String, Object>();
            returnMap.put("errorType", "noLogin");
            resultEntity.setData(returnMap);
            resultEntity.setMessage("请登录");
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
        try{
            favoritesStoreService.addFavoritesStore(storeId, SessionEntity.getMemberId());
            resultEntity.setMessage("关注成功");
            resultEntity.setCode(ResultEntity.SUCCESS);
            return resultEntity;
        } catch (ShopException e){
            logger.info(e.getMessage());
            //抛出异常
            resultEntity.setMessage(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            return resultEntity;
        }
    }

    
}