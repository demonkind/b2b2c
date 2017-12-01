package net.shopnc.b2b2c.service.goods;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.dao.goods.SpecDao;
import net.shopnc.b2b2c.dao.goods.SpecValueDao;
import net.shopnc.b2b2c.domain.goods.Spec;
import net.shopnc.b2b2c.domain.goods.SpecValue;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.vo.SpecAndValueVo;
import net.shopnc.common.entity.dtgrid.DtGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by shopnc.feng on 2015-11-06.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class SpecService extends BaseService {
    @Autowired
    SpecDao specDao;
    @Autowired
    SpecValueDao specValueDao;

    /**
     * 验证规格数量
     * @param storeId
     * @throws ShopException
     */
    private void checkSpecCountByStoreId(Object storeId) throws ShopException {
        long count = specDao.findCountByStoreId(storeId);
        if (count >= Common.SPEC_MAX_NUM) {
            throw new ShopException("规格已经添加到最大数量");
        }
    }

    /**
     * 验证规格值数量
     * @param specId
     * @return
     * @throws ShopException
     */
    private long checkSpecValueCountBySpecId(int specId) throws ShopException {
        long count = specValueDao.findCountBySpecId(specId);
        if (count >= Common.SPEC_VALUE_MAX_NUM) {
            throw new ShopException("规格值已经添加到最大数量");
        }
        return count;
    }

    /**
     * 根据Spec实体查询SpecAndValueVo
     * @param spec
     * @return
     */
    public SpecAndValueVo getSpecAndValueBySpec(Spec spec) {
        SpecAndValueVo specAndValue = new SpecAndValueVo();
        specAndValue.setSpecId(spec.getSpecId());
        specAndValue.setSpecName(spec.getSpecName());
        specAndValue.setSpecSort(spec.getSpecSort());
        specAndValue.setStoreId(spec.getStoreId());
        List<SpecValue> specValues = specValueDao.findBySpecId(spec.getSpecId());

        specAndValue.setSpecValueList(specValues);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < specValues.size(); i++) {
            if (i > 0) {
                stringBuilder.append("，");
            }
            stringBuilder.append(specValues.get(i).getSpecValueName());
        }
        specAndValue.setSpecValueNames(stringBuilder.toString());
        return specAndValue;
    }

    /**
     * 保存规格以及规格值
     *
     * @param spec
     * @param specValueNames
     * @return
     */
    public Serializable saveSpec(Spec spec, String[] specValueNames) throws ShopException {
        // 验证数量
        List<Integer> list  = new ArrayList<Integer>();
        list.add(0);
        if (spec.getStoreId() > 0) {
            list.add(spec.getStoreId());
        }
        this.checkSpecCountByStoreId(list);

        int specId = (Integer) specDao.save(spec);
        if (specValueNames == null) {
            return true;
        }
        saveSpecValue(specId, specValueNames);
        return specId;
    }

    /**
     * 保存店铺规格
     * @param specName
     * @param specValueNames
     * @param storeId
     * @return
     */
    public SpecAndValueVo saveStoreSpec(String specName, String[] specValueNames, int storeId) throws ShopException {
        Spec spec = new Spec();
        spec.setStoreId(storeId);
        spec.setSpecName(specName);
        spec.setSpecSort(0);

        int specId = (Integer) saveSpec(spec, specValueNames);
        spec.setSpecId(specId);
        SpecAndValueVo specAndValueVo = getSpecAndValueBySpec(spec);
        return specAndValueVo;
    }

    /**
     * 保存规格值
     * @param specId
     * @param specValueNames
     * @return
     * @throws ShopException
     */
    public List<Serializable> saveSpecValue(int specId, String[] specValueNames) throws ShopException {
        // 验证已添加规格值数量
        long count = checkSpecValueCountBySpecId(specId);

        List<SpecValue> specValues = new ArrayList<SpecValue>();
        for (String specValueName : specValueNames) {
            SpecValue specValue = new SpecValue();
            specValue.setSpecId(specId);
            specValue.setSpecValueName(specValueName);
            specValues.add(specValue);

            // 验证原规格值加新增规格值的数量不超过最大限制数量
            if ((count + specValues.size()) == Common.SPEC_VALUE_MAX_NUM) break;
        }
        return specValueDao.saveAll(specValues);
    }

    /**
     * 保存规格值
     * @param specId
     * @param specValueName
     * @return
     * @throws ShopException
     */
    public Serializable saveSpecValue(int specId, String specValueName) throws ShopException {
        // 验证已添加规格值数量
        checkSpecValueCountBySpecId(specId);

        SpecValue specValue = new SpecValue();
        specValue.setSpecId(specId);
        specValue.setSpecValueName(specValueName);
        return specValueDao.save(specValue);
    }

    public Serializable saveSpecValue(int storeId, SpecValue specValue) throws ShopException {
        // 验证已添加规格值数量
        checkSpecValueCountBySpecId(specValue.getSpecId());

        Spec spec = specDao.get(Spec.class, specValue.getSpecId());
        if (spec.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }
        return specValueDao.save(specValue);
    }

    /**
     * 后台规格列表JSON数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getSpecDtGridListForAdmin(String dtGridPager) throws Exception {
        DtGrid dtGrid = specDao.getDtGridList(dtGridPager, Spec.class);

        List<Object> specs = dtGrid.getExhibitDatas();
        List<Object> specAndValues = new ArrayList<Object>();
        for (int j = 0; j < specs.size(); j++) {
            Spec spec = (Spec) specs.get(j);
            SpecAndValueVo specAndValue = getSpecAndValueBySpec(spec);
            specAndValues.add(specAndValue);
        }
        dtGrid.setExhibitDatas(specAndValues);
        return dtGrid;
    }

    public SpecAndValueVo getSpecAndValue(int specId) {
        Spec spec = specDao.get(Spec.class, specId);
        return getSpecAndValueBySpec(spec);
    }

    /**
     * 删除规格及其相关数据
     * @param specId
     * @return
     */
    public Boolean deleteBySpecId(int specId) {
        specDao.delete(Spec.class, specId);
        specValueDao.deleteBySpecId(specId);
        return true;
    }

    /**
     * 删除规格及其相关数据
     * @param specId
     * @param storeId
     * @throws ShopException
     */
    public void deleteBySpecId(int specId, int storeId) throws ShopException {
        Spec spec = specDao.get(Spec.class, specId);
        if (spec == null || spec.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }
        specDao.delete(spec);
        specValueDao.deleteBySpecId(specId);
    }

    /**
     * 根据店铺编号查询可用规格
     * @param storeId
     * @return
     */
    public List<SpecAndValueVo> findSpecAndValueVoByStoreId(int storeId) {
        List<Integer> storeList = new LinkedList<Integer>();
        storeList.add(storeId);
        storeList.add(0);
        List<Spec> specList = specDao.findByStoreId(storeList, "storeId asc,specSort asc");
        List<SpecAndValueVo> specAndValueVoList = new ArrayList<SpecAndValueVo>();
        for (Spec spec : specList) {
            SpecAndValueVo specAndValueVo = this.getSpecAndValueBySpec(spec);
            specAndValueVoList.add(specAndValueVo);
        }
        return specAndValueVoList;
    }

    /**
     * 根据店铺编号取得SpecAndValueVo
     * @param specId
     * @param storeId
     * @return
     * @throws ShopException
     */
    public SpecAndValueVo getSpecAndValueVoBySpecId(int specId, int storeId) throws ShopException {
        Spec spec = specDao.get(Spec.class, specId);
        if (spec == null || spec.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }
        SpecAndValueVo specAndValueVo = this.getSpecAndValueBySpec(spec);
        return specAndValueVo;
    }

    /**
     * 更新规格名称
     * @param specNew
     * @param storeId
     * @throws ShopException
     */
    public void updateSpecName(Spec specNew, int storeId) throws ShopException {
        Spec spec = specDao.get(Spec.class, specNew.getSpecId());
        if (spec == null || spec.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }
        spec.setSpecName(specNew.getSpecName());
        specDao.update(spec);
    }

    /**
     * 更新规格值名称
     * @param specValueNew
     * @param storeId
     * @throws ShopException
     */
    public void updateSpecValueName(SpecValue specValueNew, int storeId) throws ShopException {
        SpecValue specValue = specValueDao.get(SpecValue.class, specValueNew.getSpecValueId());
        if (specValue == null) {
            throw new ShopException("参数错误");
        }
        Spec spec = specDao.get(Spec.class, specValue.getSpecId());
        if (spec == null || spec.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }
        specValue.setSpecValueName(specValueNew.getSpecValueName());
        specValueDao.update(specValue);
    }

    public void deleteSpecValueBySpecValueId(int specValueId, int storeId) throws ShopException {
        SpecValue specValue = specValueDao.get(SpecValue.class, specValueId);
        if (specValue == null) {
            return;
        }
        Spec spec = specDao.get(Spec.class, specValue.getSpecId());
        if (spec == null || spec.getStoreId() != storeId) {
            throw new ShopException("参数错误");
        }
        specValueDao.delete(specValue);
    }
}
