package net.shopnc.b2b2c.service.goods;

import net.shopnc.b2b2c.dao.goods.CustomDao;
import net.shopnc.b2b2c.domain.goods.Custom;
import net.shopnc.b2b2c.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-24.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class CustomService extends BaseService {
    @Autowired
    CustomDao customDao;

    /**
     * 保存自定义属性
     * @param categoryId
     * @param customValues
     * @return
     */
    public Boolean saveCustom(int categoryId, String[] customValues) {
        if (customValues == null) {
            return true;
        }
        List<Custom> customs = new ArrayList<Custom>();
        for (String customValue : customValues) {
            Custom custom = new Custom();
            custom.setCategoryId(categoryId);
            custom.setCustomName(customValue);
            customs.add(custom);
        }
        customDao.saveAll(customs);
        return true;
    }
}
