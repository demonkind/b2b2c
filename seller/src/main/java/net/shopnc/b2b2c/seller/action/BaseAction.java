package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.FilesType;
import net.shopnc.b2b2c.domain.store.SellerMenu;
import net.shopnc.b2b2c.domain.store.StoreMessage;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.store.SellerMenuService;
import net.shopnc.b2b2c.service.store.SellerService;
import net.shopnc.b2b2c.service.store.StoreMessageService;
import net.shopnc.b2b2c.vo.message.MessageClassVo;
import net.shopnc.b2b2c.vo.seller.SellerMenuStateVo;
import net.shopnc.common.util.MyWebBindingInitializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

/**
 * Created by shopnc.feng on 2015-11-06.
 */
@Controller
@RequestMapping("")
public class BaseAction {
    protected final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private SiteService siteService;

    @Autowired
    SellerService sellerService;

    @Autowired
    SellerMenuService sellerMenuService;
    @Autowired
    StoreMessageService storeMessageService;
    @Autowired
    protected MyWebBindingInitializer myWebBindingInitializer;

    private String menuPath = "";

    protected static final String SELLER_TEMPLATE_ROOT = "";

    private HashMap<String, String> sellerTabMenuMap = new LinkedHashMap<String, String>();

    protected String getSellerTemplate(String template) {
        return SELLER_TEMPLATE_ROOT + template;
    }

    /**
     * 过滤表单
     */
    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        myWebBindingInitializer.initBinder(binder, request);
    }
    public String getMenuPath() {
        return menuPath;
    }

    protected void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    @ModelAttribute("storeId")
    public int getStoreId() {
        return SellerSessionHelper.getStoreId();
    }

    @ModelAttribute("sellerName")
    public String getSellerName() {
        return SellerSessionHelper.getSellerName();
    }

    @ModelAttribute("sellerAvatarUrl")
    public String getAdminAvatarUrl() {
        return SellerSessionHelper.getSellerAvatarUrl();
    }

    @ModelAttribute("sellerMenu")
    public Map<String, List<SellerMenu>> getSellerMenu() {
        return SellerSessionHelper.getSellerMenu();
    }

    @ModelAttribute("sellerMenuState")
    public SellerMenuStateVo getSellerMenuStateVo() {
        return sellerMenuService.getSellerMenuState(menuPath);
    }

    @ModelAttribute("sellerTabMenuMap")
    public HashMap<String, String> getSellerTabMenuMap() {
        return sellerTabMenuMap;
    }

    protected void setSellerTabMenu(HashMap<String, String> sellerTabMenuMap) {
        this.sellerTabMenuMap = sellerTabMenuMap;
    }

    @ModelAttribute("filesType")
    public HashMap<String, Object> getAlbumType() {
        return new FilesType().get();
    }

    /**
     * freemarker 中获取全局配置
     *
     * @return
     */
    @ModelAttribute("config")
    public Map<String, String> getConfig() {
        return siteService.getSiteInfo();
    }

    @ModelAttribute("messagePrompt")
    public HashMap<String, Object> getMessagePrompt() {
        HashMap<String, Object> map = new HashMap<>();
        long count = storeMessageService.getUnreadStoreMessageCountBySellerId(SellerSessionHelper.getSellerId());
        map.put("count", count);
        if (count > 0) {
            List<StoreMessage> storeMessageList = storeMessageService.getUnreadStoreMessageBySellerId(SellerSessionHelper.getSellerId(), 5);
            map.put("storeMessageList", storeMessageList);
        }
        return map;
    }
}
