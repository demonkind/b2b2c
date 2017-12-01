package net.shopnc.b2b2c.service.goods;

import net.shopnc.b2b2c.dao.goods.AttributeDao;
import net.shopnc.b2b2c.dao.goods.AttributeValueDao;
import net.shopnc.b2b2c.domain.goods.Attribute;
import net.shopnc.b2b2c.domain.goods.AttributeValue;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.AttributeAndValueVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-17.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class AttributeService extends BaseService {
    @Autowired
    AttributeDao attributeDao;
    @Autowired
    AttributeValueDao attributeValueDao;

    /**
     * 根据属性编号取得全部绑定品牌的名称
     * @param AttributeValueList
     * @return
     */
    private String spellAttributeValues(List<AttributeValue> AttributeValueList) {
        if (AttributeValueList.size() == 0) {
            return "-";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < AttributeValueList.size(); i++) {
            if (i > 0) {
                stringBuilder.append('，');
            }
            stringBuilder.append(AttributeValueList.get(i).getAttributeValueName());
        }
        return stringBuilder.toString();
    }
    /**
     * 根据分类编号删除属性及属性相关数据
     * @param categoryId
     * @return
     */
    public Boolean deleteByCategoryId(int categoryId) {
        List<Attribute> attributes = attributeDao.findByCategoryId(categoryId);
        for (Attribute attribute : attributes) {
            attributeValueDao.deleteByAttributeId(attribute.getAttributeId());
        }
        attributeDao.deleteAll(attributes);
        return true;
    }

    /**
     * 根据属性编号删除属性及属性值
     */
    public Boolean deleteByAttributeId(int attributeId) {
        attributeDao.delete(Attribute.class, attributeId);
        attributeValueDao.deleteByAttributeId(attributeId);
        return true;
    }


    /**
     * 保存属性与属性值
     * @param categoryId
     * @param attributes
     * @return
     */
    public Boolean saveAttributes(int categoryId, String[] attributes) {
        if (attributes == null) {
            return true;
        }
        List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
        for (String stringAttribute : attributes) {
            // 属性保存部分
            // stringAttribute 格式 0||||袖长||||无袖|||短袖|||长袖||||1
            String a[] = stringAttribute.split("\\|\\|\\|\\|");
            Attribute attribute = new Attribute();
            attribute.setAttributeSort(Integer.valueOf(a[0]));
            attribute.setAttributeName(a[1]);
            attribute.setCategoryId(categoryId);
            int isShow = Integer.valueOf(a[3]).equals(1) ? 1 : 0;
            attribute.setIsShow(isShow);

            int attributeId = (Integer) attributeDao.save(attribute);

            // 属性值保存部分
            String v[] = a[2].split("\\|\\|\\|");
            for (String string : v) {
                AttributeValue attributeValue = new AttributeValue();
                attributeValue.setAttributeId(attributeId);
                attributeValue.setAttributeValueName(string);
                attributeValues.add(attributeValue);
            }
        }
        attributeValueDao.saveAll(attributeValues);
        return true;
    }

    /**
     * 保存属性值
     * @param attributeId
     * @param attributeValueNames
     * @return
     */
    public Boolean saveAttributeValues(int attributeId, String[] attributeValueNames) {
        if (attributeValueNames == null) {
            return true;
        }
        List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
        for (String attributeValueName : attributeValueNames) {
            AttributeValue attributeValue = new AttributeValue();
            attributeValue.setAttributeId(attributeId);
            attributeValue.setAttributeValueName(attributeValueName);
            attributeValues.add(attributeValue);
        }
        attributeValueDao.saveAll(attributeValues);
        return true;
    }


    /**
     * 根据分类编号查询属性及属性值VO
     * @param categoryId
     * @return
     */
    public List<AttributeAndValueVo> getAttributeAndValueVoByCategoryId(int categoryId) {
        List<Attribute> attributes = attributeDao.findByCategoryId(categoryId, "attributeSort asc");
        List<AttributeAndValueVo> attributeAndValueVos = new ArrayList<AttributeAndValueVo>();
        for (Attribute attribute : attributes) {
            AttributeAndValueVo attributeAndValueVo = new AttributeAndValueVo();
            attributeAndValueVo.setAttributeId(attribute.getAttributeId());
            attributeAndValueVo.setAttributeName(attribute.getAttributeName());
            attributeAndValueVo.setAttributeSort(attribute.getAttributeSort());
            attributeAndValueVo.setCategoryId(attribute.getCategoryId());
            attributeAndValueVo.setIsShow(attribute.getIsShow());

            List<AttributeValue> attributeValues = attributeValueDao.findByAttributeId(attribute.getAttributeId());
            attributeAndValueVo.setAttributeValueList(attributeValues);

            String attributeValueNames = spellAttributeValues(attributeValues);
            attributeAndValueVo.setAttributeValueNames(attributeValueNames);
            attributeAndValueVos.add(attributeAndValueVo);
        }
        return attributeAndValueVos;
    }
}
