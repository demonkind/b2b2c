package net.shopnc.b2b2c.wap.action.home;

import net.shopnc.b2b2c.dao.goods.GoodsDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by zxy on 2016-02-19
 */
@Controller
public class HomeShareAction extends HomeBaseAction {

    @Autowired
    private GoodsDao goodsDao;

    /**
     * 分享商品页面
     * @param goodsId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "share/goods", method = RequestMethod.GET)
    public String shareGoods(@RequestParam(value = "goodsid", required = false) Integer goodsId,
                             ModelMap modelMap) {
        //会员详情
        Member member = null;
        if (SessionEntity.getIsLogin()== true) {
            member = memberDao.get(Member.class, SessionEntity.getMemberId());
        }
        modelMap.put("member", member);
        //查询商品详情
        GoodsVo goodsVo = goodsDao.findGoodsAndCommonByGoodsId(goodsId);
        modelMap.put("goodsDetail", goodsVo);
        return getHomeTemplate("share/goods");
    }
}