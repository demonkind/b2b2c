package net.shopnc.b2b2c.web.action.home;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.BrandShowType;
import net.shopnc.b2b2c.service.SearchService;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.SearchEngineHelper;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品列表页
 * Created by shopnc.feng on 2015-12-10.
 */
@Controller
public class HomeSearchJsonAction extends HomeBaseJsonAction {
    @Autowired
    SearchService searchService;
    @Autowired
    SearchEngineHelper searchEngineHelper;

    /**
     * 搜索关键词联想提示
     * @param keyword
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "search/suggest.json", method = RequestMethod.GET)
    public List<String> suggestJson(@RequestParam(name = "term",defaultValue = "") String keyword) {
        List<String> suggestList = new ArrayList<>();

        if(!keyword.equals("") && ShopConfig.getSearchOpen()) {
            try {
                suggestList = searchEngineHelper.goodsSuggest(keyword);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.toString());
            } catch (SolrServerException e) {
                e.printStackTrace();
                logger.error(e.toString());
            }
        }

        return suggestList;
    }

    /**
     * 查询搜索页商品SKU数据
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "get/sku.json", method = RequestMethod.POST)
    public ResultEntity getSku(Integer goodsId) {
        ResultEntity resultEntity = new ResultEntity();

        try {
            HashMap<String, Object> map = searchService.getSku(goodsId);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setData(map);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }

        return resultEntity;
    }
}
