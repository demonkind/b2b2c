package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.dao.AreaDao;
import net.shopnc.b2b2c.domain.Area;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * 地区
 * Created by shopnc.feng on 2015-11-18.
 */
@Controller
public class AreaJsonAction extends BaseJsonAction {
    @Autowired
    AreaDao areaDao;

    /**
     * 查询下级地区数据
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "area/list.json/{id}", method = RequestMethod.GET)
    public ResultEntity listChidrenJson(@PathVariable String id) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            List<Area> areaList = areaDao.findByAreaParentId(Integer.parseInt(id));
            map.put("areaList", areaList);
            resultEntity.setData(map);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("操作失败");
        }
        return resultEntity;
    }
}
