package net.shopnc.b2b2c.service.goods;

import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.goods.FormatTemplateDao;
import net.shopnc.b2b2c.domain.goods.FormatTemplate;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.common.entity.PageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by shopnc.feng on 2015-12-17.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class FormatTemplateService extends BaseService {
    @Autowired
    FormatTemplateDao formatTemplateDao;

    /**
     * 更新消息模板
     * @param formatTemplateNew
     * @param storeId
     * @throws ShopException
     */
    public void update(FormatTemplate formatTemplateNew, int storeId) throws ShopException {
        FormatTemplate formatTemplate = formatTemplateDao.get(FormatTemplate.class, formatTemplateNew.getFormatId());
        if (formatTemplate.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }

        formatTemplate.setFormatName(formatTemplateNew.getFormatName());
        formatTemplate.setFormatSite(formatTemplateNew.getFormatSite());
        formatTemplate.setFormatContent(formatTemplateNew.getFormatContent());
        formatTemplateDao.update(formatTemplate);
    }

    /**
     * 删除消息模板
     * @param formatId
     * @param storeId
     * @throws ShopException
     */
    public void delete(int formatId, int storeId) throws ShopException {
        FormatTemplate formatTemplate = formatTemplateDao.get(FormatTemplate.class, formatId);
        if (formatTemplate.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }
        formatTemplateDao.delete(formatTemplate);
    }

    /**
     * 查询商家中心板式列表
     * @param paramMap
     * @param storeId
     * @return
     */
    public HashMap<String, Object> list(HashMap<String, Object> paramMap, int storeId) {
        List<String> whereList = new ArrayList<String>();
        HashMap<String, Object> whereMap = new HashMap<String, Object>();
        whereList.add("storeId = :storeId");
        whereMap.put("storeId", storeId);
        int formatSite = (Integer) paramMap.get("formatSite");
        if (formatSite == State.YES || formatSite == State.NO) {
            whereList.add("formatSite = :formatSite");
            whereMap.put("formatSite", formatSite);
        }
        String keyword = (String) paramMap.get("keyword");
        if (!keyword.isEmpty()) {
            whereList.add("formatName like :keyword");
            whereMap.put("keyword", "%" + keyword + "%");
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setPageNo((Integer) paramMap.get("page"));
        pageEntity.setTotal(formatTemplateDao.findCountByStoreId(whereList, whereMap));
        List<FormatTemplate> formatTemplateList = formatTemplateDao.findByStoreId(whereList, whereMap, pageEntity.getPageNo(), pageEntity.getPageSize());
        HashMap<String, Object> map = new HashMap<>();
        map.put("formatTemplateList", formatTemplateList);
        map.put("showPage", pageEntity.getPageHtml());
        return map;
    }
}
