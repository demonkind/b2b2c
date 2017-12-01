package net.shopnc.b2b2c.web.action.buy;

import net.shopnc.b2b2c.config.ShopConfig;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

/**
 * 购买基类
 * Created by hou on 2016/3/14.
 */
@Controller
public class BuyBaseJsonAction {
    protected final Logger logger = Logger.getLogger(getClass());
    protected String getMemberBuyRoot() {
        return ShopConfig.getMemberRoot() + "buy/";
    }
}
