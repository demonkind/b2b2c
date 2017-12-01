package net.shopnc.b2b2c.wap.action.buy;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.OrdersOrdersFrom;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.AddressDao;
import net.shopnc.b2b2c.dao.member.FavoritesGoodsDao;
import net.shopnc.b2b2c.dao.orders.InvoiceDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.Address;
import net.shopnc.b2b2c.domain.orders.Invoice;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.buy.BuyFreightService;
import net.shopnc.b2b2c.service.buy.BuyService;
import net.shopnc.b2b2c.service.goods.GoodsService;
import net.shopnc.b2b2c.vo.buy.BuyGoodsItemVo;
import net.shopnc.b2b2c.vo.buy.BuyStoreVo;
import net.shopnc.b2b2c.vo.buy.freight.BuyStoreFreightListVo;
import net.shopnc.b2b2c.vo.goods.GoodsDetailVo;
import net.shopnc.b2b2c.vo.goods.GoodsSpecValueJsonVo;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.buy.BuyFirstPostDataEntity;
import net.shopnc.common.entity.buy.BuySecondPostDataEntity;
import net.shopnc.common.entity.buy.OrdersEntity;
import net.shopnc.common.entity.buy.postjson.PostJsonGoodsEntity;
import net.shopnc.common.entity.buy.postjson.PostJsonStoreEntity;
import net.shopnc.common.entity.buy.postjson.PostJsonStoreListEntity;
import net.shopnc.common.util.UtilsHelper;

/**
 * 购买
 * Created by hou on 2016/3/14.
 */
@Controller
public class BuyJsonAction extends BuyBaseJsonAction {
    @Autowired
    private BuyService buyService;
    @Autowired
    private BuyFreightService buyFreightService;
    
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    GoodsService goodsService;
    @Autowired
    StoreDao storeDao;
    @Autowired
    private FavoritesGoodsDao favoritesGoodsDao;

    /**
     * 购买第一步[设置收货地址、发票等信息]
     *
     * @param buyItemList
     * @param isCart
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "buy/buyStep1", method = RequestMethod.POST)
    public ResultEntity buyFirst(HttpServletRequest request) {
    	String cart_id = request.getParameter("cart_id");
    	String isCartStr = request.getParameter("ifcart");
    	String address_id = request.getParameter("address_id");
    	Integer isCart = null;
    	if (!UtilsHelper.isEmpty(isCartStr)){
    		isCart = Integer.parseInt(isCartStr);
    	}
    	
        logger.info("购买第一步[设置收货地址、发票等信息]");
        ResultEntity resultEntity = new ResultEntity();
        try {
        	List<String> buyItemList = new ArrayList<>();
        	String[] cartIds = cart_id.split(",");
        	for(int i =0 ; i < cartIds.length; i++){
        		buyItemList.add(cartIds[i]);
        	}
        	
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
            
            // 获取默认地址
            Address defaultAddress = null;
            for(Address address : addressList){
            	if (UtilsHelper.isEmpty(address_id)){
            		if (address.getIsDefault() == 1){
            			defaultAddress = address;
            			break;
            		}
            	} else {
            		if (Integer.parseInt(address_id) == address.getAddressId().intValue()){
            			defaultAddress = address;
            			break;
            		}
            		
            	}
        		
        	}

            //发票 :只有所有商品都支持增值税发票才提供增值税发票 0否/1是
            logger.info("得到发票");
            int allowVat = buyService.getAllowVat(buyGoodsItemVoList);

            //选择货到付款时分开显示(可以货到付款和线上支付的商品的)商品列表
            logger.info("得到货到付款和线上支付的商品");
            List<BuyGoodsItemVo> buyGoodsItemVoOffLineList = buyService.getBuyGoodsItemVoOffLineList(buyStoreVoList);
            List<BuyGoodsItemVo> buyGoodsItemVoOnLineList = buyService.getBuyGoodsItemVoOnLineList(buyStoreVoList);

            //输出
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("buyStoreVoList", buyStoreVoList);
            // 每个店铺总金额的设定
            for (BuyStoreVo bsv : buyStoreVoList){
            	if(bsv.getStoreFreightAmount() == null) {
            		bsv.setStoreFreightAmount(new BigDecimal(0));
            	}
            	BigDecimal storeTotalAmount = bsv.getBuyItemAmount().add(bsv.getStoreFreightAmount());
            	bsv.setBuyAmount(storeTotalAmount);
            }
            
            
            resultMap.put("isCart", buyFirstPostDataEntity.getIsCart());
            resultMap.put("addressList", addressList);
            resultMap.put("address_info", defaultAddress);
            resultMap.put("allowVat",allowVat);
            
            // 获取发票信息
            List<Invoice> invoiceList = invoiceDao.getInvoiceList(SessionEntity.getMemberId());
            Invoice invoice = new Invoice();
            if (invoiceList!= null && !invoiceList.isEmpty()){
            	invoice = invoiceList.get(0);
            }
            resultMap.put("inv_info", invoice);

            //是否出现货到付款选项
            resultMap.put("allowOffline",buyGoodsItemVoOffLineList.size() > 0 ? State.YES : State.NO);

            //货到付款与在线支付商品列表
            resultMap.put("buyGoodsItemVoOffLineList",buyGoodsItemVoOffLineList);
            resultMap.put("buyGoodsItemVoOnLineList",buyGoodsItemVoOnLineList);

            //购买进度坐标
            resultMap.put("buyStep","2");
            resultMap.put("login", SessionEntity.getIsLogin());
            resultEntity.setData(resultMap);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("订单生成功");
        } catch (ShopException e) {
        	e.printStackTrace();
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            logger.error(e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("订单生成失败");
            logger.error(e.getMessage());
        }
        return resultEntity;
    }
    
    /**
     * 购买第二步[保存生成订单]
     * @param buyData
     * @param modelMap
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "buy/save", method = RequestMethod.POST)
    public ResultEntity buySecond(HttpServletRequest request) {
    	String buyData=null;
        logger.info("购买第二步[保存生成订单]");
        ResultEntity resultEntity = new ResultEntity();
        try {
            if (SessionEntity.getAllowBuy() == State.NO) {
                throw new ShopException("您的账户被禁止购买商品");
            }
            //解析提交json数据
            logger.info("解析提交数据");
            logger.info(buyData);
            String addressId=request.getParameter("address_id");
            String invoiceId=request.getParameter("invoice_id");
//            String paymentTypeCode=request.getParameter("paymentTypeCode");
            String storeList=request.getParameter("storeList");
            String ifcart=request.getParameter("ifcart");
            String key=request.getParameter("key");
            String payName=request.getParameter("pay_name");
            String payMessage=request.getParameter("pay_message");
            String cartId="";
            cartId=request.getParameter("cart_id");

            
            
          //解析提交json数据
            logger.info("解析提交数据");
            logger.info(buyData);
            ObjectMapper mapper = new ObjectMapper();
            List<BuyStoreVo> buyStoreVoList = mapper.readValue(storeList, new TypeReference<List<BuyStoreVo>>() {});
            List<PostJsonStoreEntity> postJsonList=new ArrayList<PostJsonStoreEntity>();
            for(BuyStoreVo buyStoreVo : buyStoreVoList){
			   	PostJsonStoreEntity postjsonstore=new PostJsonStoreEntity();
			   	postjsonstore.setStoreId(buyStoreVo.getStoreId());
			   	
			   	List<PostJsonGoodsEntity> goodsList=new ArrayList<PostJsonGoodsEntity>();
			   	List<BuyGoodsItemVo> buyGoodsItemVoList = buyStoreVo.getBuyGoodsItemVoList();
			   	for(BuyGoodsItemVo buyGoodsItemVo : buyGoodsItemVoList){
			   		PostJsonGoodsEntity goodsEntity=new PostJsonGoodsEntity();
			   		goodsEntity.setBuyNum(buyGoodsItemVo.getBuyNum());
				   	goodsEntity.setCartId(buyGoodsItemVo.getCartId());
				   	goodsEntity.setGoodsId(buyGoodsItemVo.getGoodsId());
				   	goodsList.add(goodsEntity);
			   	}
			   	postjsonstore.setGoodsList(goodsList);
			   	postJsonList.add(postjsonstore);
			}
          if(UtilsHelper.isEmpty(invoiceId)){
          	invoiceId="0";
          }
          PostJsonStoreListEntity postJsonStoreListEntity =new PostJsonStoreListEntity();
          postJsonStoreListEntity.setAddressId(Integer.valueOf(addressId));
          postJsonStoreListEntity.setInvoiceId(Integer.valueOf(invoiceId));
          if(UtilsHelper.isEmpty(ifcart)){
        	  ifcart = "0";
          }
          postJsonStoreListEntity.setIsCart(Integer.valueOf(ifcart));
          postJsonStoreListEntity.setPaymentTypeCode(payName);
          postJsonStoreListEntity.setStoreList(postJsonList);
          BuySecondPostDataEntity buySecondPostDataEntity = new BuySecondPostDataEntity(
                  postJsonStoreListEntity,
                  Integer.valueOf(key),
                  SessionEntity.getMemberName(),
                  OrdersOrdersFrom.WAP);

            //数据验证
            logger.info("数据验证");
            buySecondPostDataEntity = buyService.buySecondValidatePostDataEntity(buySecondPostDataEntity);

            //得到商品列表
            logger.info("得到商品列表");
            List<BuyGoodsItemVo> buyGoodsItemVoList = buyService.buySecondGetGoodsList(buySecondPostDataEntity);

            //锁定并获取商品最新库存
            logger.info("锁定并获取商品最新库存");
            buyGoodsItemVoList = buyService.buySecondGetLockGoodsList(buyGoodsItemVoList);

            //得到店铺列表
            logger.info("得到店铺列表");
            buyStoreVoList = buyService.buySecondGetStoreList(buyGoodsItemVoList);

            //计算店铺运费
            logger.info("计算店铺运费");
            buyStoreVoList = buyService.buySecondCalcStoreFreight(buySecondPostDataEntity, buyStoreVoList);

            logger.info("是否存在不支持配送的商品");
            buyService.buySecondIsExitsNoAllowSendGoods(buyStoreVoList);

            //验证店铺促销
            logger.info("验证店铺促销");
            buyService.buySecondParseStorePromotions();

            //验证平台促销
            logger.info("验证平台促销");
            buyService.buySecondParsePlatFormPromotions();

            //其它验证
            logger.info("其它验证");
            buyService.buySecondValidateCommon(buySecondPostDataEntity, buyGoodsItemVoList);

            //生成订单
            logger.info("生成订单");
            OrdersEntity ordersEntity = buyService.buySecondeCreateOrders(buySecondPostDataEntity, buyStoreVoList);

            logger.info("消息通知");
//            buyService.buySecondOrdersAfterCommon(ordersEntity, buyGoodsItemVoList);

            //返回操作结果
            resultEntity.setUrl(ShopConfig.getWebRoot() + "buy/pay/payment/list/" + ordersEntity.getOrdersPay().getPayId());
            resultEntity.setData(ordersEntity);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("订单生成功");
        } catch (ShopException e) {
        	e.printStackTrace();
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            logger.error(e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("订单生成失败");
            logger.error(e.getMessage());
        }

        return resultEntity;
    }

    /**
     * 异步运费计算
     * @param buyData
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "calc/freight", method = RequestMethod.POST)
    public ResultEntity calcFreight(@RequestParam(value = "buyData", required = true) String buyData) {
        logger.info("下单时根据地区异步计算运费");
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setCode(ResultEntity.FAIL);
        try {
        	
            ObjectMapper mapper = new ObjectMapper();
            PostJsonStoreListEntity postJsonStoreListEntity = mapper.readValue(buyData,PostJsonStoreListEntity.class);
            BuySecondPostDataEntity buySecondPostDataEntity = new BuySecondPostDataEntity(
                    postJsonStoreListEntity,
                    SessionEntity.getMemberId(),
                    SessionEntity.getMemberName(),
                    OrdersOrdersFrom.WEB);
            //得到商品列表
            logger.info("得到商品列表");
            List<BuyGoodsItemVo> buyGoodsItemVoList = buyService.buySecondGetGoodsList(buySecondPostDataEntity);

            //得到店铺列表
            logger.info("商品按店铺分组");
            List<BuyStoreVo> buyStoreVoList = buyService.buySecondGetStoreList(buyGoodsItemVoList);

            //计算店铺运费
            logger.info("计算店铺运费");
            buyStoreVoList = buyService.buySecondCalcStoreFreight(buySecondPostDataEntity, buyStoreVoList);

            logger.info("生成返回格式Vo");
            BuyStoreFreightListVo buyStoreFreightListVo = buyFreightService.getFreightListVo(buyStoreVoList);

            resultEntity.setData(buyStoreFreightListVo);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("获取运费信息成功");
        } catch (ShopException e) {
            logger.error(e.getMessage());
            resultEntity.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setMessage("获取运费信息失败");
        }
        return resultEntity;
    }
    
    
    /**
     * 发票列表 [ 发票信息 ]
     * @param invoiceId
     * @param allowVat
     * @param modelMap
     * by cj
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "buy/invoice/list", method = RequestMethod.GET)
    public ResultEntity invoicelist(HttpServletRequest request) {
    	ResultEntity resultEntity = new ResultEntity();
        List<Invoice> invoiceList = invoiceDao.getInvoiceList(SessionEntity.getMemberId());
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("login", SessionEntity.getIsLogin());
        resultMap.put("invoice_list", invoiceList);
        resultEntity.setData(resultMap);
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("获取运费信息成功");
        
        return resultEntity;
    }
    
    /**
     * 新增发票 [ 发票信息 ]
     * @param invoiceId
     * @param allowVat
     * @param modelMap
     * by cj
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "buy/invoice/add", method = RequestMethod.POST)
    public ResultEntity addInvoice(HttpServletRequest request) {
    	ResultEntity resultEntity = new ResultEntity();

    	String inv_title_select = request.getParameter("inv_title_select");
    	String inv_title = request.getParameter("inv_title");
    	String inv_content = request.getParameter("inv_content");
    	Invoice invoice = new Invoice();
    	invoice.setMemberId(SessionEntity.getMemberId());
    	invoice.setTitle(inv_title);
    	invoice.setContent(inv_content);
    	if("person".equals(inv_title_select)){
    		invoice.setInvoiceType(Short.parseShort("1"));
    	}else{
    		invoice.setInvoiceType(Short.parseShort("2"));
    	}
    	int invoiceId = (Integer) invoiceDao.save(invoice);
    	
    	
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("login", SessionEntity.getIsLogin());
        resultMap.put("inv_id", invoiceId);
        resultEntity.setData(resultMap);
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("新增发票成功");
        
        return resultEntity;
    }
    
    /**
     * 删除发票 [ 发票信息 ]
     * @param invoiceId
     * @param allowVat
     * @param modelMap
     * by cj
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "buy/invoice/del", method = RequestMethod.POST)
    public ResultEntity delInvoice(HttpServletRequest request) {
    	ResultEntity resultEntity = new ResultEntity();

    	String inv_id = request.getParameter("inv_id");
    	Invoice invoice = new Invoice();
    	invoice.setMemberId(SessionEntity.getMemberId());
    	invoice.setInvoiceId(Integer.parseInt(inv_id));
    	invoiceDao.delete(invoice);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultEntity.setData(resultMap);
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("新增发票成功");
        
        return resultEntity;
    }
    
    /**
     * 根据规格获取其相关的货物信息
     * @param invoiceId
     * @param allowVat
     * @param modelMap
     * by cj
     * @return
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @ResponseBody
    @RequestMapping(value = "productData/product/getInfoBySpec", method = RequestMethod.POST)
    public ResultEntity getInfoBySpec(HttpServletRequest request)  {
    	ResultEntity resultEntity = new ResultEntity();
    	HashMap<String, Object> resultMap = new HashMap<>();
    	try {
    		String specValueId = request.getParameter("specValueId");
        	String goodsSpecValueJson = request.getParameter("goodsSpecValueJson");
        	goodsSpecValueJson = goodsSpecValueJson.replaceAll("GoodsSpecValueJsonVo", "");
        	JSONArray json = JSONArray.fromObject(goodsSpecValueJson);
        	int goodsId = 0;
        	ObjectMapper ob = new ObjectMapper();
        	List<HashMap<String, Object>> goodsSpecValueJsonVos = ob.readValue(json.toString(), new TypeReference<List<HashMap<String, Object>>>() {});
        	for(HashMap<String, Object> jsonVo : goodsSpecValueJsonVos) {
        		if(specValueId.equals(jsonVo.get("specValueIds").toString().replaceAll(" ", ""))){
        			goodsId = Integer.parseInt(jsonVo.get("goodsId").toString());
        			break;
        		}
        	}
        	GoodsDetailVo goodsDetail = goodsService.getDetail(goodsId);
        	resultMap.put("goodId", goodsDetail.getGoodsId());
        	resultMap.put("goodName", goodsDetail.getGoodsName());
        	resultMap.put("goodsImageSrc", goodsDetail.getImageSrc());
        	resultMap.put("goodsSaleNum", goodsDetail.getGoodsSaleNum());
        	resultMap.put("goodsStorage", goodsDetail.getGoodsStorage());
        	resultMap.put("commonSaleNum", goodsDetail.getCommonSaleNum());
        	resultMap.put("commonStorage", goodsDetail.getCommonStorage());
        	resultMap.put("goodsPrice", goodsDetail.getGoodsPrice());
		} catch (Exception e) {
			e.printStackTrace();
			resultEntity.setData(resultMap);
	        resultEntity.setCode(ResultEntity.FAIL);
	        resultEntity.setMessage("根据规格获取其相关的货物信息失败");
		}
    	
    	
        
        resultEntity.setData(resultMap);
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("根据规格获取其相关的货物信息成功");
        
        return resultEntity;
    }

}
