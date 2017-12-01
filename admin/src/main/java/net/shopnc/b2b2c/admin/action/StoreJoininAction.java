package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.constant.StoreJoininState;
import net.shopnc.b2b2c.dao.goods.StoreBindCategoryDao;
import net.shopnc.b2b2c.dao.store.StoreCertificateDao;
import net.shopnc.b2b2c.dao.store.StoreClassDao;
import net.shopnc.b2b2c.dao.store.StoreGradeDao;
import net.shopnc.b2b2c.dao.store.StoreJoininDao;
import net.shopnc.b2b2c.domain.goods.StoreBindCategory;
import net.shopnc.b2b2c.domain.store.StoreCertificate;
import net.shopnc.b2b2c.domain.store.StoreClass;
import net.shopnc.b2b2c.domain.store.StoreGrade;
import net.shopnc.b2b2c.domain.store.StoreJoinin;
import net.shopnc.common.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 店铺入驻管理
 * Created by dqw on 2015-12-21.
 */
@Controller
public class StoreJoininAction extends BaseAction {

    @Autowired
    private StoreJoininDao storeJoininDao;

    @Autowired
    private StoreCertificateDao storeCertificateDao;

    @Autowired
    private StoreBindCategoryDao storeBindCategoryDao;

    @Autowired
    private StoreGradeDao storeGradeDao;

    @Autowired
    private StoreClassDao storeClassDao;

    public StoreJoininAction() {
        setMenuPath("store_joinin/list");
    }

    /**
     * 商家入驻申请列表
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store_joinin/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        return getAdminTemplate("store/joinin/list");
    }

    /**
     * 商家入驻申请详细信息
     * @param sellerId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "store_joinin/detail/{sellerId}", method = RequestMethod.GET)
    public String detail(@PathVariable int sellerId, ModelMap modelMap) {
        StoreJoinin storeJoinin = storeJoininDao.get(StoreJoinin.class, sellerId);
        modelMap.addAttribute("storeJoinin", storeJoinin);

        StoreCertificate storeCertificate = storeCertificateDao.get(StoreCertificate.class, sellerId);
        modelMap.addAttribute("storeCertificate", storeCertificate);

        StoreGrade storeGrade = storeGradeDao.get(StoreGrade.class, storeJoinin.getGradeId());
        modelMap.addAttribute("storeGrade", storeGrade);

        StoreClass storeClass = storeClassDao.get(StoreClass.class, storeJoinin.getClassId());
        modelMap.addAttribute("storeClass", storeClass);

        modelMap.addAttribute("payingAmount", storeGrade.getPrice() + storeClass.getBail());

        List<StoreBindCategory> storeBindCategoryList = storeBindCategoryDao.getListByStoreId(storeJoinin.getSellerId());
        modelMap.addAttribute("storeBindCategoryList", storeBindCategoryList);
        modelMap.addAttribute("storeBindCategoryListJson", JsonHelper.toJson(storeBindCategoryList));

        if(storeJoinin.getState() == StoreJoininState.SUBMIT) {
            modelMap.addAttribute("isVerify", true);
        }

        if(storeJoinin.getState() == StoreJoininState.PAYED) {
            modelMap.addAttribute("isVerifyPay", true);
        }

        return getAdminTemplate("store/joinin/detail");
    }
}


