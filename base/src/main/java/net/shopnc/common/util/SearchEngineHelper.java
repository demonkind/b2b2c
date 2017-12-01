package net.shopnc.common.util;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.GoodsState;
import net.shopnc.b2b2c.constant.GoodsVerify;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.domain.goods.Category;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索<br/>
 * Created by dqw on 2016/02/17.
 */
@Component
public class SearchEngineHelper {

    protected final Logger logger = Logger.getLogger(getClass());

    public Map<String, Object> goodsQuery(String keyword, String condition, String sort, int start, int rows) throws IOException, SolrServerException {
        String url = ShopConfig.getSearchUrl() + "/b2b2c_goods";
        SolrClient solrClient = new HttpSolrClient(url);

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set("q", keyword.trim());
        logger.info(condition);
        if(condition.trim().equals("")) {
            condition = "goodsStatus:" + State.YES;
        } else {
            condition += " AND goodsStatus:" + State.YES;
        }
        solrQuery.set("fq", condition);
        if(!sort.equals("")) {
            solrQuery.set("sort", sort);
        }
        solrQuery.set("defType", "dismax");
        solrQuery.set("q.alt", "*.*");
        solrQuery.set("qf", "goodsName^0.7 jingle^0.2 categoryName^0.1");
        solrQuery.set("wt", "json");
        solrQuery.setStart(start - 1);
        solrQuery.setRows(rows);
        solrQuery.setFacet(true);
        solrQuery.addFacetField("category");

        logger.info("searchEngineQuery:" + solrQuery.toString());

        QueryResponse response = solrClient.query(solrQuery);

        SolrDocumentList list = response.getResults();
        logger.info("searchEngineTotal:" + list.getNumFound());

        List<GoodsVo> goodsVoList = response.getBeans(GoodsVo.class);
        logger.info("searchEngineCount:" + goodsVoList.size());

        List<Category> categoryList = new ArrayList<>();
        List<FacetField> facetFieldList = response.getFacetFields();
        for(FacetField facetField : facetFieldList) {
            if(facetField.getName().equals("category")) {
                for (int i = 0; i < facetField.getValueCount(); i++) {
                    if (facetField.getValues().get(i).getCount() > 0) {
                        String categoryString = facetField.getValues().get(i).getName();
                        String[] categoryArray = categoryString.split(",");
                        Category category = new Category();
                        category.setCategoryId(Integer.valueOf(categoryArray[0]));
                        category.setCategoryName(categoryArray[1]);
                        categoryList.add(category);
                    }
                }
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("goodsVoList", goodsVoList);
        resultMap.put("total", list.getNumFound());
        resultMap.put("categoryList", categoryList);
        return resultMap;
    }

    /**
     * 从搜索引擎删除商品
     * @param id
     * @throws IOException
     * @throws SolrServerException
     */
    public void goodsDelete(String id) throws IOException, SolrServerException {
        String url = ShopConfig.getSearchUrl() + "/b2b2c_goods";
        SolrClient solrClient = new HttpSolrClient(url);

        solrClient.deleteById(id);
        solrClient.commit();
    }

    /**
     * 商品搜索提示
     *
     * @return
     * @throws IOException
     * @throws SolrServerException
     */
    public List<String> goodsSuggest(String keyword) throws IOException, SolrServerException {
        SolrClient solrClient = new HttpSolrClient(ShopConfig.getSearchUrl() + "/b2b2c_goods");

        SolrQuery query = new SolrQuery();
        query.setRequestHandler("/suggest");
        query.set("q", keyword);
        logger.info(query.toString());

        QueryResponse response = solrClient.query(query);
        logger.info(response.toString());

        List<String> list = new ArrayList<>();
        List<SpellCheckResponse.Suggestion> suggestions = response.getSpellCheckResponse().getSuggestions();
        if (suggestions.size() > 0) {
            list = suggestions.get(0).getAlternatives();
        }

        return list;
    }
}


