package net.shopnc.b2b2c.web.action.buy;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.AddressDao;
import net.shopnc.b2b2c.dao.orders.InvoiceDao;
import net.shopnc.b2b2c.domain.Address;
import net.shopnc.b2b2c.domain.orders.Invoice;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.buy.BuyService;
import net.shopnc.b2b2c.vo.buy.BuyGoodsItemVo;
import net.shopnc.b2b2c.vo.buy.BuyStoreVo;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import net.shopnc.common.entity.buy.BuyFirstPostDataEntity;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

/**
 * 购买
 * Created by hbj on 2015/12/11.
 */
@Controller
public class BuyAction extends BuyBaseAction {
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private BuyService buyService;
    @Autowired
    private AddressDao addressDao;

    /**
     * 购买第一步[设置收货地址、发票等信息]
     *
     * @param buyItemList
     * @param isCart
     * @return
     */
    @RequestMapping(value = "buy", method = RequestMethod.POST)
    public String buyFirst(@RequestParam(value = "cartId", required = true) List<String> buyItemList,
                           @RequestParam(value = "isCart", required = false) Integer isCart, ModelMap modelMap) {
        logger.info("购买第一步[设置收货地址、发票等信息]");
        try {
            if (SessionEntity.getAllowBuy() == State.NO) {
                throw new ShopException("您的账户被禁止购买商品");
            }
            //提交数据保存入实体
            logger.info("处理提交数据");
            BuyFirstPostDataEntity buyFirstPostDataEntity = new BuyFirstPostDataEntity(
                    buyItemList,
                    isCart,
                    SessionEntity.getMemberId());

            //得到商品列表
            logger.info("得到商品列表");
            List<BuyGoodsItemVo> buyGoodsItemVoList = buyService.buyFirstGetGoodsList(buyFirstPostDataEntity,SessionEntity.getMemberId());

            //得到店铺列表
            logger.info("得到店铺列表");
            List<BuyStoreVo> buyStoreVoList = buyService.buyFirstGetStoreList(buyGoodsItemVoList);

            //得到平台促销
            logger.info("得到平台促销");
            buyService.buyFirstGetPlatFormPromotionsList();

            // bycj [ 添加收货地址信息 ]
            logger.info("得到收货地址");
            List<Address> addressList = addressDao.getAddressList(SessionEntity.getMemberId());

            //发票 :只有所有商品都支持增值税发票才提供增值税发票 0否/1是
            logger.info("得到发票");
            int allowVat = buyService.getAllowVat(buyGoodsItemVoList);

            //选择货到付款时分开显示(可以货到付款和线上支付的商品的)商品列表
            logger.info("得到货到付款和线上支付的商品");
            List<BuyGoodsItemVo> buyGoodsItemVoOffLineList = buyService.getBuyGoodsItemVoOffLineList(buyStoreVoList);
            List<BuyGoodsItemVo> buyGoodsItemVoOnLineList = buyService.getBuyGoodsItemVoOnLineList(buyStoreVoList);

            //输出
            modelMap.put("buyStoreVoList", buyStoreVoList);
            modelMap.put("isCart", buyFirstPostDataEntity.getIsCart());
            modelMap.put("addressList", addressList);
            modelMap.put("allowVat",allowVat);

            //是否出现货到付款选项
            modelMap.put("allowOffline",buyGoodsItemVoOffLineList.size() > 0 ? State.YES : State.NO);

            //货到付款与在线支付商品列表
            modelMap.put("buyGoodsItemVoOffLineList",buyGoodsItemVoOffLineList);
            modelMap.put("buyGoodsItemVoOnLineList",buyGoodsItemVoOnLineList);

            //购买进度坐标
            modelMap.put("buyStep","2");
        } catch (ShopException e) {
            HashMap<String, String> hashMapMessage = new HashMap<>();
            hashMapMessage.put("message", e.getMessage());
            hashMapMessage.put("url", ShopConfig.getMemberRoot());
            return "redirect:/message?" + ShopHelper.buildQueryString(hashMapMessage);
        } catch (Exception e) {
            logger.error(e.toString());
            return "redirect:/error";
        }
        return getBuyTemplate("buy");
    }

    /**
     * 发票列表 [ 发票信息 ]
     * @param invoiceId
     * @param allowVat
     * @param modelMap
     * by cj
     * @return
     */
    @RequestMapping(value = "buy/invoice/list", method = RequestMethod.POST)
    public String invoicelist(
            @RequestParam(value = "invoiceId", required = false) Integer invoiceId ,
            @RequestParam(value = "allowVat",required=false) Integer allowVat ,
                              ModelMap modelMap
    ) {
        List<Invoice> invoiceList = invoiceDao.getInvoiceList(SessionEntity.getMemberId());
        modelMap.put("invoiceList", invoiceList);
        modelMap.put("invoiceId", invoiceId);
        modelMap.put("allowVat", allowVat);
        return getBuyTemplate("buy_inc_invoice_load");
    }
}
