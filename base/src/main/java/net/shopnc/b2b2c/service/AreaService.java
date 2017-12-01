package net.shopnc.b2b2c.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shopnc.b2b2c.dao.AreaDao;
import net.shopnc.b2b2c.domain.Area;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.vo.AreaVo;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.entity.dtgrid.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 地区
 * Created by shopnc on 2015/11/25.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class AreaService {
    @Autowired
    private AreaDao areaDao;

    /**
     * 后台地区列表
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getArticleCategoryDtGridList(String dtGridPager) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DtGrid dtGrid = mapper.readValue(dtGridPager, DtGrid.class);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (dtGrid.getNcColumnsType() != null && dtGrid.getNcColumnsType().size() > 0) {
            for (String key : dtGrid.getNcColumnsType().keySet()) {
                for (int i = 0; i< dtGrid.getNcColumnsType().get(key).size(); i++) {
                    hashMap.put((String) dtGrid.getNcColumnsType().get(key).get(i), key);
                }
                dtGrid.setNcColumnsTypeList(hashMap);
            }
        }
        QueryUtils.parseDtGridHql(dtGrid);
        dtGrid = areaDao.getDtGridList(dtGrid,Area.class);
        List<Object> exhibitDatas = dtGrid.getExhibitDatas();
        List<Object> articleCategoryVoList = new ArrayList<Object>();
        for (int i = 0; i < exhibitDatas.size(); i++) {
            Area area = (Area)exhibitDatas.get(i);
            AreaVo areaVo = new AreaVo();
            areaVo.setAreaId(area.getAreaId());
            areaVo.setAreaName(area.getAreaName());
            areaVo.setAreaDeep(area.getAreaDeep());
            areaVo.setAreaParentId(area.getAreaParentId());
            areaVo.setAreaRegion(area.getAreaRegion());
            if (area.getAreaParentId() > 0) {
                Area areaParent = areaDao.get(Area.class,area.getAreaParentId());
                if (areaParent != null) {
                    areaVo.setAreaParentName(areaParent.getAreaName());
                }
            }
            articleCategoryVoList.add(areaVo);
        }
        dtGrid.setExhibitDatas(articleCategoryVoList);
        return dtGrid;
    }

    /**
     * 删除地区及子地区
     * @param areaId
     * @throws ShopException
     */
    public void delArea(int areaId) throws ShopException {
        if (areaId <= 0) {
            throw new ShopException("参数错误");
        }
        List<Integer> areaIdList = getAllChilren(areaId);
        areaDao.delArea(areaIdList);
    }

    /**
     * 取得地区ID及所有子地区ID
     * @param areaId
     * @return
     */
    public List<Integer> getAllChilren(int areaId) {
        List<Integer> areaIdList = new ArrayList<Integer>();
        areaIdList.add(areaId);
        List<Area> areaChildrenList = areaDao.findByAreaParentId(areaId);
            for(int i=0; i<areaChildrenList.size(); i++) {
                areaIdList.addAll(getAllChilren(areaChildrenList.get(i).getAreaId()));
            }
        return areaIdList;
    }
}
