package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.admin.util.AdminSessionHelper;
import net.shopnc.b2b2c.dao.store.StoreJoininDao;
import net.shopnc.b2b2c.domain.admin.AdminMenu;
import net.shopnc.b2b2c.service.ContractService;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.admin.AdminMenuService;
import net.shopnc.b2b2c.service.bill.BillService;
import net.shopnc.b2b2c.service.goods.BrandService;
import net.shopnc.b2b2c.service.goods.GoodsService;
import net.shopnc.b2b2c.service.member.PredepositService;
import net.shopnc.b2b2c.service.refund.AdminRefundService;
import net.shopnc.b2b2c.vo.admin.AdminMenuStateVo;
import net.shopnc.common.util.MyWebBindingInitializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class BaseAction {
    @Autowired
    SiteService siteService;
    @Autowired
    AdminMenuService adminMenuService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    BrandService brandService;
    @Autowired
    ContractService contractService;
    @Autowired
    PredepositService predepositService;
    @Autowired
    AdminRefundService adminRefundService;
    @Autowired
    StoreJoininDao storeJoininDao;
    @Autowired
    BillService billService;

    protected final Logger logger = Logger.getLogger(getClass());

    protected static final String ADMIN_TEMPLATE_ROOT = "";

    @Autowired
    protected MyWebBindingInitializer myWebBindingInitializer;

    private String path = "";

    /**
     * 过滤表单
     */
    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        myWebBindingInitializer.initBinder(binder, request);
    }

    protected String getAdminTemplate(String template) {
        return ADMIN_TEMPLATE_ROOT + template;
    }

    /**
     * freemarker 中获取全局配置
     * @return
     */
    @ModelAttribute("config")
    public Map<String, String> getConfig() {
        return siteService.getSiteInfo();
    }
    /**
     * 手工设置选中菜单
     * @param path
     */
    public void setMenuPath(String path) {
        this.path = path;
    }

    /**
     * 管理员用户名
     * @return
     */
    @ModelAttribute("adminName")
    public String getAdminName() {
        return AdminSessionHelper.getAdminName();
    }

    /**
     * 管理员组
     * @return
     */
    @ModelAttribute("adminGroupName")
    public String getAdminGroupName() {
        return AdminSessionHelper.getAdminGroup();
    }

    /**
     * 管理员头像URl
     * @return
     */
    @ModelAttribute("adminAvatarUrl")
    public String getAdminAvatarUrl() {
        return AdminSessionHelper.getAdminAvatarUrl();
    }

    /**
     * 后台菜单
     * @return
     */
    @ModelAttribute("adminMainMenu")
    public List<AdminMenu> getAdminMenu() {
        List<AdminMenu> adminMenuList = AdminSessionHelper.getAdminMenu();
        return adminMenuList;
    }

    /**
     * 后台菜单选中状态
     * @return
     */
    @ModelAttribute("adminMenuState")
    public AdminMenuStateVo getAdminMenuStateVo() {
        return adminMenuService.getAdminMenuState(path);
    }

    /**
     * 后台顶部代办事项提醒
     * @return
     */
    @ModelAttribute("countPrompt")
    public HashMap<String, Long> countPrompt() {
        HashMap<String, Long> map = new HashMap<>();
        // 等待审核商品
        long goodsWaitCount = goodsService.findWaitCount();
        if (goodsWaitCount > 0) {
            map.put("goodsWaitCount", goodsWaitCount);
        }
        // 等待审核品牌
        long brandWaitCount = brandService.findWaitCount();
        if (brandWaitCount > 0) {
            map.put("brandWaitCount", brandWaitCount);
        }
        // 消保申请
        Map<String, Long> contractMap = contractService.countApply();
        if (contractMap.get("countApply") > 0) {
            map.put("contractApply", contractMap.get("countApply"));
        }
        // 消保支付保证金
        if (contractMap.get("countCostApply") > 0) {
            map.put("contractCostApply", contractMap.get("countCostApply"));
        }
        // 消保保证金退款
        if (contractMap.get("countQuitApply") > 0) {
            map.put("contractQuitApply", contractMap.get("countQuitApply"));
        }
        // 会员提现申请
        long predepositCash = predepositService.getNotDealwithCashCount();
        if (predepositCash > 0) {
            map.put("predepositCash", predepositCash);
        }
        // 退款待确认
        long handleRefund = adminRefundService.getHandleRefundCount();
        if (handleRefund > 0) {
            map.put("handleRefund", handleRefund);
        }
        // 退货带确认
        long handleReturn = adminRefundService.getHandleReturnCount();
        if (handleReturn > 0) {
            map.put("handleReturn", handleReturn);
        }
        //　开店申请
        long storeJoin = storeJoininDao.getAdminCheckCount();
        if (storeJoin > 0) {
            map.put("storeJoin", storeJoin);
        }
        // 结算确认
        long billConfirmCount = billService.getBillConfirmCount();
        if (billConfirmCount > 0) {
            map.put("billConfirmCount", billConfirmCount);
        }
        // 结算付款
        long billAccessCount = billService.getBillAccessCount();
        if (billAccessCount > 0) {
            map.put("billAccessCount", billAccessCount);
        }
        return map;
    }
}