package net.shopnc.b2b2c.service;


import net.shopnc.b2b2c.constant.OrdersEvaluationState;
import net.shopnc.b2b2c.constant.OrdersOperationType;
import net.shopnc.b2b2c.dao.goods.GoodsCommonDao;
import net.shopnc.b2b2c.dao.goods.GoodsDao;
import net.shopnc.b2b2c.dao.member.EvaluateGoodsContentDao;
import net.shopnc.b2b2c.dao.member.EvaluateGoodsDao;
import net.shopnc.b2b2c.dao.member.EvaluateStoreDao;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.orders.OrdersDao;
import net.shopnc.b2b2c.dao.orders.OrdersGoodsDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.EvaluateGoods;
import net.shopnc.b2b2c.domain.EvaluateGoodsContent;
import net.shopnc.b2b2c.domain.EvaluateStore;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.goods.GoodsCommon;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.orders.Orders;
import net.shopnc.b2b2c.domain.orders.OrdersGoods;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.ExperienceService;
import net.shopnc.b2b2c.service.member.PointsService;
import net.shopnc.b2b2c.vo.member.*;
import net.shopnc.b2b2c.vo.orders.OrdersGoodsVo;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.OrdersHelper;
import net.shopnc.common.util.PriceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional(rollbackFor = {Exception.class})
public class EvaluateService extends BaseService {

    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private EvaluateGoodsDao evaluateGoodsDao;
    @Autowired
    private EvaluateGoodsContentDao evaluateGoodsContentDao;
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private EvaluateStoreDao evaluateStoreDao;
    @Autowired
    private OrdersGoodsDao ordersGoodsDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsCommonDao goodsCommonDao;
	@Autowired
    private PointsService pointsService;
    @Autowired
    private ExperienceService experienceService;
    @Autowired
    private SendMessageService sendMessageService;
    
    /**
     * 格式化日期
     */
    private String dateFormat(Timestamp date,String pattern){
    	DateFormat formater=new SimpleDateFormat(pattern);
    	return formater.format(date);
    }
    
    /**
     * 添加评价页面查询信息
     * @param memberId
     * @param ordersId
     * @return
     * @throws ShopException
     */
    public Map<String,Object> getEvaluateOrder(int memberId,int ordersId) throws ShopException{
    	List<Object> whereItems=new ArrayList<Object>();
    	whereItems.add("ordersId=:ordersId");
    	whereItems.add("memberId=:memberId");
    	HashMap<String,Object> params=new HashMap<String, Object>();
    	params.put("ordersId", ordersId);
    	params.put("memberId", memberId);
    	Orders orders=ordersDao.getOrdersInfo(whereItems, params);
    	
    	if (OrdersHelper.operationValidate(orders, OrdersOperationType.MEMBER_EVALUATION) == 0) {
            throw new ShopException("无权操作");
        }
    	
    	List<Object> ordersGoodsList=ordersDao.getOrdersGoodsVoList(Arrays.asList(ordersId));
    	
    	int storeId=orders.getStoreId();
    	Store store=storeDao.get(Store.class, storeId);
    	
    	Map<String,Object> returnMap=new HashMap<String, Object>();
    	returnMap.put("orders", orders);
    	returnMap.put("ordersGoodsList", ordersGoodsList);
    	returnMap.put("store", store);
    	EvaluateStoreVo evaluateStoreVo=this.getEvalStoreClass(storeId);
    	returnMap.put("evaluateStoreVo", evaluateStoreVo);
    	return returnMap;
    }
    
    /**
     * 追评页面查询商品信息
     */
    public List<EvaluateGoodsOrderVo> queryEvaluateByOrderId(int memberId,int orderId) throws ShopException{
    	Orders orders=ordersDao.get(Orders.class, orderId);
    	if (OrdersHelper.operationValidate(orders, OrdersOperationType.MEMBER_EVALUATION_APPEND) == 0) {
            throw new ShopException("无权操作");
        }
    	
    	List<EvaluateGoodsOrderVo> voList=new ArrayList<EvaluateGoodsOrderVo>();
    	List<Object> ordersGoodsList=ordersDao.getOrdersGoodsVoList(Arrays.asList(orderId));
    	for(int i=0;i<ordersGoodsList.size();i++){
    		OrdersGoodsVo ordersGoodsVo=(OrdersGoodsVo)ordersGoodsList.get(i);
    		String goodsId=String.valueOf(ordersGoodsVo.getGoodsId());
    		String goodsName=ordersGoodsVo.getGoodsName();
    		String goodsImage=ordersGoodsVo.getGoodsImage();
    		int orderGoodsId=ordersGoodsVo.getOrdersGoodsId();
    		int storeId=ordersGoodsVo.getStoreId();
    		
    		//通过orderGoodsId
    		EvaluateCommonVo evaluateCommonVo=evaluateGoodsDao.getFirstEvaluate(orderGoodsId,memberId);
    		
    		EvaluateGoodsOrderVo vo=new EvaluateGoodsOrderVo();
    		vo.setGoodsId(goodsId);
    		vo.setGoodsName(goodsName);
    		vo.setGoodsImage(goodsImage);
    		vo.setEvaluateId(evaluateCommonVo.getEvaluateId());
    		vo.setEvaluateContent(evaluateCommonVo.getEvaluateContent());
    		vo.setStoreId(storeId);
    		vo.setOrdersGoodsId(String.valueOf(orderGoodsId));
    		voList.add(vo);
    	}
    	return voList;
    }

    /**
     * 添加评价
     * @param evaluateVo
     */
    public void saveEvaluate(EvaluateVo evaluateVo){
    	NumberFormat formater = DecimalFormat.getInstance();
		formater.setMaximumFractionDigits(1);
		formater.setGroupingUsed(false);
    	
    	int evaluateNum=1;//第一次评价
    	String orderId=evaluateVo.getOrderId();
    	String storeId=evaluateVo.getStoreId();
    	String memberId=evaluateVo.getMemberId();
    	List<String> orderGoodsIdList=evaluateVo.getOrderGoodsIdList();//订单商品编号
    	List<String> goodsIdList=evaluateVo.getGoodsIdList();//商品ID
    	List<String> isAnonymousList=evaluateVo.getIsAnonymousList();//是否匿名
    	List<String> contentList=evaluateVo.getContentList();//评价内容
    	List<String> scoreList=evaluateVo.getScoreList();
    	List<String> imageList=evaluateVo.getImageList();
    	//String isOwnShop=evaluateVo.getIsOwnShop();
    	
    	Timestamp now=new Timestamp(new Date().getTime());
    	for(int i=0;i<goodsIdList.size();i++){
    		int hasImage=0;
    		String images="";
    		String content="";
    		if(imageList!=null && imageList.size()>0){
	    		images=imageList.get(i);
	    		if(images!=null && !images.isEmpty()){
	    			hasImage=1;
	    		}
    		}
    		if(contentList!=null && contentList.size()>0){
    			content=contentList.get(i);
    		}
    		if("".equals(content)){
    			content="好评";
    		}
    		
    		EvaluateGoods evaluateGoods=new EvaluateGoods();
    		evaluateGoods.setOrderId(Integer.parseInt(orderId));
    		evaluateGoods.setOrderGoodsId(Integer.parseInt(orderGoodsIdList.get(i)));
    		evaluateGoods.setGoodsId(Integer.parseInt(goodsIdList.get(i)));
    		evaluateGoods.setStoreId(Integer.parseInt(storeId));
    		evaluateGoods.setScores(Integer.parseInt(scoreList.get(i)));
    		evaluateGoods.setIsAnonymous(Integer.parseInt(isAnonymousList.get(i)));
    		evaluateGoods.setEvaluateTime(now);
    		evaluateGoods.setFromMemberId(Integer.parseInt(memberId));
    		evaluateGoods.setEvaluateNum(evaluateNum);
    		evaluateGoods.setHasImage(hasImage);
    		int evaluateId=(Integer)evaluateGoodsDao.save(evaluateGoods);
    		
    		EvaluateGoodsContent evaluateGoodsContent=new EvaluateGoodsContent();
    		evaluateGoodsContent.setContent(content);
    		evaluateGoodsContent.setAddTime(now);
    		evaluateGoodsContent.setEvaluateId(evaluateId);
    		evaluateGoodsContent.setImages(images);
    		evaluateGoodsContentDao.save(evaluateGoodsContent);
    	}
    	//非自营
    	//if("0".equals(isOwnShop)){
    		EvaluateStore evaluateStore=new EvaluateStore();
    		evaluateStore.setOrderId(Integer.parseInt(orderId));
    		evaluateStore.setStoreId(Integer.parseInt(storeId));
    		evaluateStore.setEvaluateTime(now);
    		evaluateStore.setFromMemberId(Integer.parseInt(memberId));
    		evaluateStore.setDescriptionCredit(Integer.parseInt(evaluateVo.getDescriptionCredit()));
    		evaluateStore.setServiceCredit(Integer.parseInt(evaluateVo.getServiceCredit()));
    		evaluateStore.setDeliveryCredit(Integer.parseInt(evaluateVo.getDeliveryCredit()));
    		evaluateStoreDao.save(evaluateStore);
    		
    		List<Object> list=evaluateStoreDao.getAvgScore(Integer.parseInt(storeId));
    		double avgDescriptionCredit=(Double)((Object[])list.get(0))[0];
    		double avgServiceCredit=(Double)((Object[])list.get(0))[1];
    		double avgDeliveryCredit=(Double)((Object[])list.get(0))[2];
    		Store store=storeDao.get(Store.class, Integer.parseInt(storeId));
    		store.setStoreDesccredit(Double.parseDouble(formater.format(avgDescriptionCredit)));
    		store.setStoreServicecredit(Double.parseDouble(formater.format(avgServiceCredit)));
    		store.setStoreDeliverycredit(Double.parseDouble(formater.format(avgDeliveryCredit)));
    		storeDao.update(store);
    	//}
    	//修改订单表评价状态
    	Orders orders=ordersDao.get(Orders.class, Integer.parseInt(orderId));
    	String ordersSn=String.valueOf(orders.getOrdersSn());
    	orders.setEvaluationState(OrdersEvaluationState.YES);
    	orders.setEvaluationTime(now);
    	ordersDao.update(orders);
    	
    	//计算好评数和好评率
    	if(goodsIdList!=null && goodsIdList.size()>0){
    		for(String goodsId : goodsIdList){
    			updateGoodsCounter(Integer.parseInt(goodsId));
    		}
    	}
		//增加会员积分
		pointsService.addPointsComments(Integer.parseInt(memberId));
		//增加会员经验
		experienceService.addExperienceComments(Integer.parseInt(memberId));
		
		//消息通知
		String memberName=evaluateVo.getMemberName();
		HashMap<String, Object> hashMapMsg = new HashMap<>();
		hashMapMsg.put("ordersSn", ordersSn);
		hashMapMsg.put("userName", memberName);
		hashMapMsg.put("evalTime", this.dateFormat(now, "yyyy-MM-dd HH:mm"));
		sendMessageService.sendStore("storeOrdersEvaluate", Integer.parseInt(storeId), hashMapMsg, orderId);
    }
    
    /**
     * 追加评价
     * @param evaluateVo
     */
    public void saveEvaluateAgain(EvaluateVo evaluateVo){
    	Timestamp now=new Timestamp(new Date().getTime());
    	List<String> evaluateIdList=evaluateVo.getEvaluateIdList();
    	List<String> contentList=evaluateVo.getContentList();
    	List<String> imageList=evaluateVo.getImageList();
    	for(int i=0;i<evaluateIdList.size();i++){
    		int hasImage=0;
    		String images="";
    		if(imageList!=null && imageList.size()>0){
	    		images=imageList.get(i);
	    		if(images!=null && !images.isEmpty()){
	    			hasImage=1;
	    		}
    		}
    		String content="";
    		if(contentList!=null && contentList.size()>0){
    			content=contentList.get(i);
    		}
    		if("".equals(content)){
    			content="好评";
    		}
    		
    		String evaluateId=evaluateIdList.get(i);
    		EvaluateGoods evaluateGoods=evaluateGoodsDao.get(EvaluateGoods.class, Integer.parseInt(evaluateId));
    		int evaluateNum=evaluateGoods.getEvaluateNum();
    		evaluateGoods.setEvaluateNum(evaluateNum+1);
    		evaluateGoods.setHasImage(hasImage);
    		evaluateGoodsDao.save(evaluateGoods);
    		
    		EvaluateGoodsContent evaluateGoodsContent=new EvaluateGoodsContent();
    		evaluateGoodsContent.setContent(content);
    		evaluateGoodsContent.setAddTime(now);
    		evaluateGoodsContent.setEvaluateId(Integer.parseInt(evaluateId));
    		evaluateGoodsContent.setImages(images);
    		evaluateGoodsContentDao.save(evaluateGoodsContent);
    	}
    	//修改订单表评价状态
    	String orderId=evaluateVo.getOrderId();
    	Orders orders=ordersDao.get(Orders.class, Integer.parseInt(orderId));
    	String ordersSn=String.valueOf(orders.getOrdersSn());
    	orders.setEvaluationAppendState(OrdersEvaluationState.YES);
    	ordersDao.update(orders);
    	
    	//消息通知
		String memberName=evaluateVo.getMemberName();
		int storeId=orders.getStoreId();
		HashMap<String, Object> hashMapMsg = new HashMap<>();
		hashMapMsg.put("ordersSn", ordersSn);
		hashMapMsg.put("userName", memberName);
		hashMapMsg.put("evalTime", this.dateFormat(now, "yyyy-MM-dd HH:mm"));
		sendMessageService.sendStore("storeOrdersEvaluate", storeId, hashMapMsg, orderId);
    }
    
    /**
     * 买家评价列表
     * @param pageNo
     * @param memberId
     * @return
     */
    public Map<String,Object> queryPageEvaluate(int pageNo,int memberId){
    	long totalPageSize=evaluateGoodsDao.getPageEvaluateCount(memberId);//记录总数
    	
    	PageEntity pageEntity = new PageEntity();
    	pageEntity.setTotal(totalPageSize);
        pageEntity.setPageNo(pageNo);
        
    	List<EvaluateGoodsVo> voList=new ArrayList<EvaluateGoodsVo>();
    	List<EvaluateGoods> evaluateGoodsList=evaluateGoodsDao.getPageEvaluate(pageEntity.getPageNo(), pageEntity.getPageSize(),memberId);
    	for(EvaluateGoods e : evaluateGoodsList){
			int evaluateId=e.getEvaluateId();
			int goodsId=e.getGoodsId();
			int orderGoodsId=e.getOrderGoodsId();
			Timestamp evaluateTime=e.getEvaluateTime();
			int scores=e.getScores();
			OrdersGoods ordersGoods=ordersGoodsDao.get(OrdersGoods.class, orderGoodsId);
			String goodsName=ordersGoods.getGoodsName();
			String goodsImage=ordersGoods.getGoodsImage();
			
			List<EvaluateGoodsContent> evaluateGoodsContentList=evaluateGoodsContentDao.getEvaluateGoodsContentByEvaluateId(evaluateId);
			EvaluateGoodsContent evaluateGoodsContent1=evaluateGoodsContentList.get(0);//评价
			String evaluateContent1=evaluateGoodsContent1.getContent();
			String images1=evaluateGoodsContent1.getImages();
			String explainContent1=evaluateGoodsContent1.getExplainContent();
			Timestamp addTime1=evaluateGoodsContent1.getAddTime();
			List<String> image1List=new ArrayList<String>();
			if(images1!=null && !images1.isEmpty()){
				String[] images1Arr=images1.split("_");
				image1List=Arrays.asList(images1Arr);
			}
			String evaluateContent2="";
			String images2="";
			String explainContent2="";
			long days=0;
			List<String> image2List=new ArrayList<String>();
			if(evaluateGoodsContentList.size()>1){
				EvaluateGoodsContent evaluateGoodsContent2=evaluateGoodsContentList.get(1);//追评
				evaluateContent2=evaluateGoodsContent2.getContent();
				images2=evaluateGoodsContent2.getImages();
				explainContent2=evaluateGoodsContent2.getExplainContent();
				Timestamp addTime2=evaluateGoodsContent2.getAddTime();
				if(images2!=null && !images2.isEmpty()){
					String[] images2Arr=images2.split("_");
					image2List=Arrays.asList(images2Arr);
				}
				
				days=(addTime2.getTime()-addTime1.getTime())/1000/60/60/24;
			}
			
			EvaluateGoodsVo vo=new EvaluateGoodsVo();
			vo.setGoodsId(String.valueOf(goodsId));
			vo.setEvaluateId(String.valueOf(evaluateId));
			vo.setGoodsName(goodsName);
			vo.setGoodsImage(goodsImage);
			vo.setScores(String.valueOf(scores));
			vo.setEvaluateTime(evaluateTime);
			vo.setEvaluateContent1(evaluateContent1);
			vo.setEvaluateContent2(evaluateContent2);
			vo.setImages1(images1);
			vo.setImages2(images2);
			vo.setExplainContent1(explainContent1);
			vo.setExplainContent2(explainContent2);
			vo.setImage1List(image1List);
			vo.setImage2List(image2List);
			if(days==0){
				vo.setDays("当");
			}else{
				vo.setDays(String.valueOf(days));
			}
			voList.add(vo);
		}
    	
    	
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("list", voList);
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }
    
    /**
     * 商品详情页面 查询商品评价列表
     */
    public Map<String,Object> queryPageGoodsEvaluate(int pageNo,EvaluateGoodsVo evaluateGoodsVo){
    	String _goodsId=evaluateGoodsVo.getGoodsId();
    	String evalLv=evaluateGoodsVo.getEvalLv();
    	
    	List<String> condition=new ArrayList<String>();
    	HashMap<String,Object> params=new HashMap<String, Object>();
    	if(evalLv!=null && !"all".equals(evalLv) && !evalLv.isEmpty()){
    		if("1".equals(evalLv)){
    			condition.add("scores>=:scoresBegin1");
    			params.put("scoresBegin1", 4);
    			condition.add("scores<=:scoresEnd1");
    			params.put("scoresEnd1", 5);
    		}else if("2".equals(evalLv)){
    			condition.add("scores>=:scoresBegin");
    			params.put("scoresBegin", 2);
    			condition.add("scores<=:scoresEnd");
    			params.put("scoresEnd", 3);
    		}else if("3".equals(evalLv)){
    			condition.add("scores=:scores");
    			params.put("scores", 1);
    		}else if("4".equals(evalLv)){
    			condition.add("hasImage=:hasImage");
    			params.put("hasImage", 1);
    		}
    	}
    	condition.add("goodsId=:goodsId");
    	params.put("goodsId", Integer.parseInt(_goodsId));
    	
    	
    	long totalPageSize=evaluateGoodsDao.getPageGoodsEvaluateCount(condition,params);//记录总数
    	
    	PageEntity pageEntity = new PageEntity();
    	pageEntity.setTotal(totalPageSize);
        pageEntity.setPageNo(pageNo);
    	
        //查询列表
        String orderBy="scores desc,hasImage desc";
    	List<EvaluateGoodsVo> voList=new ArrayList<EvaluateGoodsVo>();
    	List<EvaluateGoods> evaluateGoodsList=evaluateGoodsDao.getPageGoodsEvaluate(pageEntity.getPageNo(), pageEntity.getPageSize(),condition,params,orderBy);
    	for(EvaluateGoods e : evaluateGoodsList){
			int evaluateId=e.getEvaluateId();
			int goodsId=e.getGoodsId();
			int orderGoodsId=e.getOrderGoodsId();
			Timestamp evaluateTime=e.getEvaluateTime();
			int scores=e.getScores();
			int evalMemberId=e.getFromMemberId();//评价人ID
			int isAnonymous=e.getIsAnonymous();//是否匿名
			OrdersGoods ordersGoods=ordersGoodsDao.get(OrdersGoods.class, orderGoodsId);
			String goodsName=ordersGoods.getGoodsName();
			String goodsImage=ordersGoods.getGoodsImage();
			String scoreTitle="";
			
			Member member=memberDao.get(Member.class, evalMemberId);
			String memberName=member.getMemberName();
			if(isAnonymous==1){
				String str=memberName.substring(2);
				memberName=memberName.substring(0,2);
				for(int i=0;i<str.length();i++){
					memberName+="*";
				}
			}
			String memberHeadUrl=member.getAvatarUrl();
			
			List<EvaluateGoodsContent> evaluateGoodsContentList=evaluateGoodsContentDao.getEvaluateGoodsContentByEvaluateId(evaluateId);
			EvaluateGoodsContent evaluateGoodsContent1=evaluateGoodsContentList.get(0);//评价
			String evaluateContent1=evaluateGoodsContent1.getContent();
			String images1=evaluateGoodsContent1.getImages();
			String explainContent1=evaluateGoodsContent1.getExplainContent();
			Timestamp addTime1=evaluateGoodsContent1.getAddTime();
			List<String> image1List=new ArrayList<String>();
			if(images1!=null && !images1.isEmpty()){
				String[] images1Arr=images1.split("_");
				image1List=Arrays.asList(images1Arr);
			}
			String evaluateContent2="";
			String images2="";
			String explainContent2="";
			long days=0;
			List<String> image2List=new ArrayList<String>();
			if(evaluateGoodsContentList.size()>1){
				EvaluateGoodsContent evaluateGoodsContent2=evaluateGoodsContentList.get(1);//追评
				evaluateContent2=evaluateGoodsContent2.getContent();
				images2=evaluateGoodsContent2.getImages();
				explainContent2=evaluateGoodsContent2.getExplainContent();
				Timestamp addTime2=evaluateGoodsContent2.getAddTime();
				if(images2!=null && !images2.isEmpty()){
					String[] images2Arr=images2.split("_");
					image2List=Arrays.asList(images2Arr);
				}
				
				days=(addTime2.getTime()-addTime1.getTime())/1000/60/60/24;
			}
			
			EvaluateGoodsVo vo=new EvaluateGoodsVo();
			vo.setGoodsId(String.valueOf(goodsId));
			vo.setEvaluateId(String.valueOf(evaluateId));
			vo.setGoodsName(goodsName);
			vo.setGoodsImage(goodsImage);
			vo.setScores(String.valueOf(scores));
			vo.setEvaluateTime(evaluateTime);
			vo.setEvaluateContent1(evaluateContent1);
			vo.setEvaluateContent2(evaluateContent2);
			vo.setImages1(images1);
			vo.setImages2(images2);
			vo.setExplainContent1(explainContent1);
			vo.setExplainContent2(explainContent2);
			vo.setImage1List(image1List);
			vo.setImage2List(image2List);
			vo.setScoreTitle(scoreTitle);
			vo.setMemberName(memberName);
			vo.setMemberHeadUrl(memberHeadUrl);
			if(days==0){
				vo.setDays("当");
			}else{
				vo.setDays(String.valueOf(days));
			}
			voList.add(vo);
		}
    	
    	Map<String,Object> result = new HashMap<String,Object>();
        result.put("list", voList);
        result.put("showPage", pageEntity.getPageHtml());
    	return result;
    }
    
    /**
     * 商品详情页面 查询评价信息
     * @param evaluateGoodsVo
     * @throws ShopException 
     */
    public EvaluateGoodsVo queryGoodsEvaluate(int goodsId) throws ShopException{
    	List<String> condition=new ArrayList<String>();
    	HashMap<String,Object> params=new HashMap<String, Object>();
    	condition.add("goodsId=:goodsId");
    	params.put("goodsId", goodsId);
    	long evalCount=evaluateGoodsDao.getPageGoodsEvaluateCount(condition,params);//评价总数
    	
    	condition=new ArrayList<String>();
		params=new HashMap<String, Object>();
		condition.add("goodsId=:goodsId");
    	params.put("goodsId", goodsId);
		condition.add("scores>=:scoresBegin1");
		params.put("scoresBegin1", 4);
		condition.add("scores<=:scoresEnd1");
		params.put("scoresEnd1", 5);
		long evalCount1=evaluateGoodsDao.getPageGoodsEvaluateCount(condition, params);
		condition=new ArrayList<String>();
		params=new HashMap<String, Object>();
		condition.add("goodsId=:goodsId");
    	params.put("goodsId", goodsId);
		condition.add("scores>=:scoresBegin");
		params.put("scoresBegin", 2);
		condition.add("scores<=:scoresEnd");
		params.put("scoresEnd", 3);
		long evalCount2=evaluateGoodsDao.getPageGoodsEvaluateCount(condition, params);
		condition=new ArrayList<String>();
		params=new HashMap<String, Object>();
		condition.add("goodsId=:goodsId");
    	params.put("goodsId", goodsId);
		condition.add("scores=:scores");
		params.put("scores", 1);
		long evalCount3=evaluateGoodsDao.getPageGoodsEvaluateCount(condition, params);
		int evalRate1=100;
		int evalRate2=100;
		int evalRate3=100;
		double total=evalCount;
		if(total==0){
			total=1;
		}
		evalRate1=PriceHelper.format(BigDecimal.valueOf(evalCount1/total*100),0).intValue();//好评率
		evalRate2=PriceHelper.format(BigDecimal.valueOf(evalCount2/total*100),0).intValue();//中评率
		evalRate3=PriceHelper.format(BigDecimal.valueOf(evalCount3/total*100),0).intValue();//差评率
		//有图个数
		condition=new ArrayList<String>();
		params=new HashMap<String, Object>();
		condition.add("goodsId=:goodsId");
		params.put("goodsId", goodsId);
		condition.add("hasImage=:hasImage");
		params.put("hasImage", 1);
		long hasImageCount=evaluateGoodsDao.getPageGoodsEvaluateCount(condition, params);
		
		
		//查询商品信息
        Goods goods = goodsDao.get(Goods.class, goodsId);
        GoodsCommon goodsCommon = goodsCommonDao.get(GoodsCommon.class, goods.getCommonId());
		String goodsName=goodsCommon.getGoodsName() + " " + goods.getGoodsSerial() + " " + goods.getGoodsSpecs();
		String goodsImage=goods.getImageSrc();
		BigDecimal goodsPrice=goods.getGoodsPrice();//市场价
		int storeId=goodsCommon.getStoreId();
		Store store=storeDao.get(Store.class, storeId);
		int isOwnShop=store.getIsOwnShop();//是否自营
		String isOwnShopStr="";
		if(isOwnShop==1){
			isOwnShopStr="平台自营";
		}
		
		double avgGoodsEval=evaluateGoodsDao.getAvgGoodsEval(goodsId);
		if(avgGoodsEval==0){
			avgGoodsEval=5;
		}
		
		EvaluateGoodsVo vo=new EvaluateGoodsVo();
		vo.setEvalCount((int)evalCount);
		vo.setEvalCount1(String.valueOf(evalCount1));
		vo.setEvalCount2(String.valueOf(evalCount2));
		vo.setEvalCount3(String.valueOf(evalCount3));
		vo.setEvalRate1(String.valueOf(evalRate1));
		vo.setEvalRate2(String.valueOf(evalRate2));
		vo.setEvalRate3(String.valueOf(evalRate3));
		vo.setHasImageCount(String.valueOf(hasImageCount));
		vo.setGoodsId(String.valueOf(goodsId));
		vo.setGoodsName(goodsName);
		vo.setGoodsImage(goodsImage);
		vo.setGoodsPrice(goodsPrice);
		vo.setIsOwnShopStr(isOwnShopStr);
		vo.setAvgGoodsEval(PriceHelper.format(BigDecimal.valueOf(avgGoodsEval),0).intValue());
		vo.setStoreId(String.valueOf(storeId));
    	return vo;
    }
    
    /**
     * seller查询评价列表 用于解释
     * @param pageNo
     * @param storeId
     * @param goodsName
     * @param memberName
     * @return
     */
    public Map<String,Object> queryPageSellerEvaluate(int pageNo,int storeId,String goodsName,String memberName){
    	List<String> condition=new ArrayList<String>();
    	HashMap<String,Object> params=new HashMap<String, Object>();
    	if(goodsName!=null && !goodsName.isEmpty()){
    		condition.add("og.goodsName like :goodsName");
    		params.put("goodsName", "%"+goodsName+"%");
    	}
    	if(memberName!=null && !memberName.isEmpty()){
    		condition.add("m.memberName like :memberName");
    		params.put("memberName", "%"+memberName+"%");
    	}
    	condition.add("eg.storeId=:storeId");
    	params.put("storeId", storeId);
    	long totalPageSize=evaluateGoodsDao.getPageSellerEvaluateCount(condition, params);//记录总数
    	
    	PageEntity pageEntity = new PageEntity();
    	pageEntity.setTotal(totalPageSize);
        pageEntity.setPageNo(pageNo);
        
        String orderBy="eg.evaluateTime desc";
        List<EvaluateCommonVo> evaluateCommonVoList=evaluateGoodsDao.getPageSellerEvaluate(pageNo, pageEntity.getPageSize(), condition, params, orderBy);
        List<EvaluateGoodsVo> voList=new ArrayList<EvaluateGoodsVo>();
        for(EvaluateCommonVo ec : evaluateCommonVoList){
        	EvaluateGoods e=ec.getEvaluateGoods();
        	OrdersGoods ordersGoods=ec.getOrdersGoods();
        	Member member=ec.getMember();
        	
        	int evaluateId=e.getEvaluateId();
			int goodsId=e.getGoodsId();
			Timestamp evaluateTime=e.getEvaluateTime();
			int scores=e.getScores();
			
			String _goodsName=ordersGoods.getGoodsName();//商品名称
			String _memberName=member.getMemberName();//评价人昵称
			
			List<EvaluateGoodsContent> evaluateGoodsContentList=evaluateGoodsContentDao.getEvaluateGoodsContentByEvaluateId(evaluateId);
			EvaluateGoodsContent evaluateGoodsContent1=evaluateGoodsContentList.get(0);//评价
			String evaluateContent1=evaluateGoodsContent1.getContent();
			String images1=evaluateGoodsContent1.getImages();
			String explainContent1=evaluateGoodsContent1.getExplainContent();
			Timestamp addTime1=evaluateGoodsContent1.getAddTime();
			List<String> image1List=new ArrayList<String>();
			if(images1!=null && !images1.isEmpty()){
				String[] images1Arr=images1.split("_");
				image1List=Arrays.asList(images1Arr);
			}
			String evaluateContent2="";
			String images2="";
			String explainContent2="";
			long days=0;
			List<String> image2List=new ArrayList<String>();
			if(evaluateGoodsContentList.size()>1){
				EvaluateGoodsContent evaluateGoodsContent2=evaluateGoodsContentList.get(1);//追评
				evaluateContent2=evaluateGoodsContent2.getContent();
				images2=evaluateGoodsContent2.getImages();
				explainContent2=evaluateGoodsContent2.getExplainContent();
				Timestamp addTime2=evaluateGoodsContent2.getAddTime();
				if(images2!=null && !images2.isEmpty()){
					String[] images2Arr=images2.split("_");
					image2List=Arrays.asList(images2Arr);
				}
				
				days=(addTime2.getTime()-addTime1.getTime())/1000/60/60/24;
			}
			
			EvaluateGoodsVo vo=new EvaluateGoodsVo();
			vo.setGoodsId(String.valueOf(goodsId));
			vo.setEvaluateId(String.valueOf(evaluateId));
			vo.setGoodsName(_goodsName);
			vo.setScores(String.valueOf(scores));
			vo.setEvaluateTime(evaluateTime);
			vo.setEvaluateContent1(evaluateContent1);
			vo.setEvaluateContent2(evaluateContent2);
			vo.setImages1(images1);
			vo.setImages2(images2);
			vo.setExplainContent1(explainContent1);
			vo.setExplainContent2(explainContent2);
			vo.setImage1List(image1List);
			vo.setImage2List(image2List);
			vo.setMemberName(_memberName);
			if(days==0){
				vo.setDays("当");
			}else{
				vo.setDays(String.valueOf(days));
			}
			voList.add(vo);
        }
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("list", voList);
        result.put("showPage", pageEntity.getPageHtml());
    	return result;
    }
    
    /**
     * seller解释评论
     * @param evaluateId
     * @param explainContent
     * @param type
     * @return
     */
    public EvaluateGoodsContent saveExplaint(int evaluateId,String explainContent,String type,String sellerName){
    	List<EvaluateGoodsContent> list=evaluateGoodsContentDao.getEvaluateGoodsContentByEvaluateId(evaluateId);
    	EvaluateGoodsContent evaluateGoodsContent=new EvaluateGoodsContent();
    	if("1".equals(type)){
    		evaluateGoodsContent=list.get(0);
    		evaluateGoodsContent.setExplainContent(explainContent);
    		evaluateGoodsContentDao.update(evaluateGoodsContent);
    	}else if("2".equals(type)){
    		evaluateGoodsContent=list.get(1);
    		evaluateGoodsContent.setExplainContent(explainContent);
    		evaluateGoodsContentDao.update(evaluateGoodsContent);
    	}
    	
    	//消息通知
    	Timestamp now=new Timestamp(new Date().getTime());
    	EvaluateGoods evaluateGoods=evaluateGoodsDao.get(EvaluateGoods.class, evaluateId);
    	int orderId=evaluateGoods.getOrderId();
    	Orders orders=ordersDao.get(Orders.class, orderId);
		HashMap<String, Object> hashMapMsg = new HashMap<>();
		hashMapMsg.put("ordersSn", String.valueOf(orders.getOrdersSn()));
		hashMapMsg.put("sellerName", sellerName);
		hashMapMsg.put("evalTime", this.dateFormat(now, "yyyy-MM-dd HH:mm"));
		sendMessageService.sendMember("memberOrdersEvaluateExplain", evaluateGoods.getFromMemberId(), hashMapMsg,Integer.toString(orderId));
    	
    	return evaluateGoodsContent;
    }
    
    /**
     * admin评价列表
     * @param pageNo
     * @param memberName
     * @return
     * @throws Exception 
     */
    public DtGrid queryPageAdminEvaluate(String dtGridPager) throws Exception{
    	DtGrid dtGrid=evaluateGoodsDao.getDtGridEvaluate(dtGridPager);
    	List<EvaluateCommonVo> list=(List)dtGrid.getExhibitDatas();
    	List<EvaluateGoodsVo> voList=new ArrayList<EvaluateGoodsVo>();
    	for(EvaluateCommonVo ec : list){
        	EvaluateGoods e=ec.getEvaluateGoods();
        	OrdersGoods ordersGoods=ec.getOrdersGoods();
        	Member member=ec.getMember();
        	
        	int evaluateId=e.getEvaluateId();
			int goodsId=e.getGoodsId();
			Timestamp evaluateTime=e.getEvaluateTime();
			int scores=e.getScores();
			String _goodsName=ordersGoods.getGoodsName();//商品名称
			String _memberName=member.getMemberName();//评价人昵称
			int memberId=e.getFromMemberId();
			int orderId=e.getOrderId();
			Orders orders=ordersDao.get(Orders.class, orderId);
			String ordersSn=String.valueOf(orders.getOrdersSn());//订单编号
			int storeId=e.getStoreId();
			Store store=storeDao.get(Store.class, storeId);
			int sellerId=store.getSellerId();//商家ID
			String storeName=store.getStoreName();//店铺名称
			
			List<EvaluateGoodsContent> evaluateGoodsContentList=evaluateGoodsContentDao.getEvaluateGoodsContentByEvaluateId(evaluateId);
			EvaluateGoodsContent evaluateGoodsContent1=evaluateGoodsContentList.get(0);//评价
			String evaluateContent1=evaluateGoodsContent1.getContent();
			String images1=evaluateGoodsContent1.getImages();
			String explainContent1=evaluateGoodsContent1.getExplainContent();
			Timestamp addTime1=evaluateGoodsContent1.getAddTime();
			List<String> image1List=new ArrayList<String>();
			if(images1!=null && !images1.isEmpty()){
				String[] images1Arr=images1.split("_");
				image1List=Arrays.asList(images1Arr);
			}
			String evaluateContent2="";
			String images2="";
			String explainContent2="";
			long days=0;
			List<String> image2List=new ArrayList<String>();
			if(evaluateGoodsContentList.size()>1){
				EvaluateGoodsContent evaluateGoodsContent2=evaluateGoodsContentList.get(1);//追评
				evaluateContent2=evaluateGoodsContent2.getContent();
				images2=evaluateGoodsContent2.getImages();
				explainContent2=evaluateGoodsContent2.getExplainContent();
				Timestamp addTime2=evaluateGoodsContent2.getAddTime();
				if(images2!=null && !images2.isEmpty()){
					String[] images2Arr=images2.split("_");
					image2List=Arrays.asList(images2Arr);
				}
				
				days=(addTime2.getTime()-addTime1.getTime())/1000/60/60/24;
			}
			
			EvaluateGoodsVo vo=new EvaluateGoodsVo();
			vo.setGoodsId(String.valueOf(goodsId));
			vo.setEvaluateId(String.valueOf(evaluateId));
			vo.setGoodsName(_goodsName);
			vo.setScores(String.valueOf(scores));
			vo.setEvaluateTime(evaluateTime);
			vo.setEvaluateTimeStr(new SimpleDateFormat("yyyy-MM-dd").format(evaluateTime));
			vo.setEvaluateContent1(evaluateContent1);
			vo.setEvaluateContent2(evaluateContent2);
			vo.setImages1(images1);
			vo.setImages2(images2);
			vo.setExplainContent1(explainContent1);
			vo.setExplainContent2(explainContent2);
			vo.setImage1List(image1List);
			vo.setImage2List(image2List);
			vo.setMemberName(_memberName);
			vo.setStoreName(storeName);
			vo.setOrdersSn(ordersSn);
			vo.setSellerId(String.valueOf(sellerId));
			vo.setMemberId(String.valueOf(memberId));
			if(days==0){
				vo.setDays("当");
			}else{
				vo.setDays(String.valueOf(days));
			}
			voList.add(vo);
        }
    	dtGrid.setExhibitDatas((List)voList);
    	return dtGrid;
    }
    
    /**
     * admin删除评论
     * @param evaluateId
     */
    public void deleteEvaluate(int evaluateId){
    	EvaluateGoods evaluateGoods=evaluateGoodsDao.get(EvaluateGoods.class, evaluateId);
    	evaluateGoodsDao.deleteEvaluateGoodsContent(evaluateId);
    	evaluateGoodsDao.deleteEvaluateGoods(evaluateId);
    	int goodsId=evaluateGoods.getGoodsId();
    	updateGoodsCounter(goodsId);
    	int orderId=evaluateGoods.getOrderId();
    	Orders orders=ordersDao.get(Orders.class, orderId);
    	orders.setEvaluationState(1);
    	orders.setEvaluationAppendState(1);
    	ordersDao.update(orders);
    }
    
    /**
     * admin店铺评价列表
     * @throws Exception 
     */
    public DtGrid queryPageAdminStoreEvaluate(String dtGridPager) throws Exception{
    	DtGrid dtGrid=evaluateGoodsDao.getDtGridEvaluateStore(dtGridPager);
    	List<EvaluateStoreVo> voList=new ArrayList<EvaluateStoreVo>();
    	List<EvaluateCommonVo> list=(List)dtGrid.getExhibitDatas();
    	for(EvaluateCommonVo ec : list){
    		EvaluateStore es=ec.getEvaluateStore();
    		Member member=ec.getMember();
    		int evaluateId=es.getEvaluateId();
    		int descriptionCredit=es.getDescriptionCredit();
    		int serviceCredit=es.getServiceCredit();
    		int deliveryCredit=es.getDeliveryCredit();
    		String memberName=member.getMemberName();
    		int storeId=es.getStoreId();
    		Store store=storeDao.get(Store.class, storeId);
    		String storeName=store.getStoreName();
    		Timestamp evaluateTime=es.getEvaluateTime();
    		String evaluateTimeStr=new SimpleDateFormat("yyyy-MM-dd").format(evaluateTime);
    		int orderId=es.getOrderId();
    		Orders orders=ordersDao.get(Orders.class, orderId);
    		String ordersSn=String.valueOf(orders.getOrdersSn());
    		
    		EvaluateStoreVo vo=new EvaluateStoreVo();
    		vo.setEvaluateId(evaluateId);
    		vo.setDescriptionCredit(descriptionCredit);
    		vo.setServiceCredit(serviceCredit);
    		vo.setDeliveryCredit(deliveryCredit);
    		vo.setMemberName(memberName);
    		vo.setStoreName(storeName);
    		vo.setEvaluateTimeStr(evaluateTimeStr);
    		vo.setOrdersSn(ordersSn);
    		voList.add(vo);
    	}
    	dtGrid.setExhibitDatas((List)voList);
    	return dtGrid;
    }
    
    /**
     * 删除店铺评价
     * @param evaluateId
     */
    public void deleteStoreEvaluate(int evaluateId){
    	EvaluateStore entity=evaluateStoreDao.get(EvaluateStore.class, evaluateId);
    	evaluateStoreDao.delete(entity);
    }
    
    /**
     * 计算商品的好评数和好评率
     */
    public void updateGoodsCounter(int goodsId){
    	List<String> condition=new ArrayList<String>();
    	HashMap<String,Object> params=new HashMap<String, Object>();
    	condition.add("goodsId=:goodsId");
    	params.put("goodsId", goodsId);
    	long evalCount=evaluateGoodsDao.getPageGoodsEvaluateCount(condition,params);//评价总数
    	
    	condition=new ArrayList<String>();
		params=new HashMap<String, Object>();
		condition.add("goodsId=:goodsId");
    	params.put("goodsId", goodsId);
		condition.add("scores>=:scoresBegin1");
		params.put("scoresBegin1", 4);
		condition.add("scores<=:scoresEnd1");
		params.put("scoresEnd1", 5);
		long evalCount1=evaluateGoodsDao.getPageGoodsEvaluateCount(condition, params);//好评数
		
		int evalRate1=0;
		if(evalCount!=0){
			evalRate1=PriceHelper.format(BigDecimal.valueOf(evalCount1/evalCount*100),0).intValue();//好评率
		}
		Goods goods = goodsDao.get(Goods.class, goodsId);
		goods.setEvaluateNum((int)evalCount);
		goods.setGoodsRate((int)evalRate1);
		goodsDao.update(goods);
    }
    
    /**
     * 取得当前店铺在同行业分类下的 描述评价、服务评价、发货评价的高中低
     * @param storeId
     */
    public EvaluateStoreVo getEvalStoreClass(int storeId){
    	DecimalFormat formater = new DecimalFormat("0.0");
		formater.setRoundingMode(RoundingMode.HALF_UP);
		
		DecimalFormat formater1 = new DecimalFormat("0.00");
		formater1.setRoundingMode(RoundingMode.HALF_UP);
    	
    	Store store=storeDao.get(Store.class, storeId);
    	double storeDesEval=store.getStoreDesccredit();//当前店铺描述评分
    	double storeServiceEval=store.getStoreServicecredit();//当前店铺服务评分
    	double storeDeliveryEval=store.getStoreDeliverycredit();//当前店铺发货速度评分
    	int classId=store.getClassId();
    	List<Object> list=storeDao.getAvgEvaluateByClassId(classId);
    	Object obj=list.get(0);
    	double avgDescriptionCredit=0;
		double avgServiceCredit=0;
		double avgDeliveryCredit=0;
    	if(obj!=null){
    		avgDescriptionCredit=(Double)((Object[])obj)[0];
    		avgServiceCredit=(Double)((Object[])obj)[1];
    		avgDeliveryCredit=(Double)((Object[])obj)[2];
    	}
    	//double avgStoreEval=(avgDescriptionCredit+avgServiceCredit+avgDeliveryCredit)/3;
    	double avgStoreEval=(storeDesEval+storeServiceEval+storeDeliveryEval)/3;
    	int avgStoreEvalRate=(int)(avgStoreEval/5*100);
    	
    	double desEvalRate=-1;
    	double serviceEvalRate=-1;
    	double deliveryEvalRate=-1;
    	
		String desEvalArrow="equal";
		String desEvalTitle="持平";
		String serviceEvalArrow="equal";
		String serviceEvalTitle="持平";
		String deliveryEvalArrow="equal";
		String deliveryEvalTitle="持平";
		if(storeDesEval<avgDescriptionCredit){
			desEvalArrow="low";
			desEvalRate=(avgDescriptionCredit-storeDesEval)/avgDescriptionCredit*100;
			//desEvalTitle="低于"+formater1.format(desEvalRate)+"%";
			desEvalTitle="低于";
		}else if(storeDesEval>avgDescriptionCredit){
			desEvalArrow="high";
			desEvalRate=(storeDesEval-avgDescriptionCredit)/avgDescriptionCredit*100;
			//desEvalTitle="高于"+formater1.format(desEvalRate)+"%";
			desEvalTitle="高于";
		}
		if(storeServiceEval<avgServiceCredit){
			serviceEvalArrow="low";
			serviceEvalRate=(avgServiceCredit-storeServiceEval)/avgServiceCredit*100;
			//serviceEvalTitle="低于"+formater1.format(serviceEvalRate)+"%";
			serviceEvalTitle="低于";
		}else if(storeServiceEval>avgServiceCredit){
			serviceEvalArrow="high";
			serviceEvalRate=(storeServiceEval-avgServiceCredit)/avgServiceCredit*100;
			//serviceEvalTitle="高于"+formater1.format(serviceEvalRate)+"%";
			serviceEvalTitle="高于";
		}
		if(storeDeliveryEval<avgDeliveryCredit){
			deliveryEvalArrow="low";
			deliveryEvalRate=(avgDeliveryCredit-storeDeliveryEval)/avgDeliveryCredit*100;
			//deliveryEvalTitle="低于"+formater1.format(deliveryEvalRate)+"%";
			deliveryEvalTitle="低于";
		}else if(storeDeliveryEval>avgDeliveryCredit){
			deliveryEvalArrow="high";
			deliveryEvalRate=(storeDeliveryEval-avgDeliveryCredit)/avgDeliveryCredit*100;
			//deliveryEvalTitle="高于"+formater1.format(deliveryEvalRate)+"%";
			deliveryEvalTitle="高于";
		}
		
		EvaluateStoreVo vo=new EvaluateStoreVo();
		vo.setDesEvalArrow(desEvalArrow);
		vo.setServiceEvalArrow(serviceEvalArrow);
		vo.setDeliveryEvalArrow(deliveryEvalArrow);
		vo.setDesEvalTitle(desEvalTitle);
		vo.setServiceEvalTitle(serviceEvalTitle);
		vo.setDeliveryEvalTitle(deliveryEvalTitle);
		vo.setStoreDesEval(formater.format(storeDesEval));
		vo.setStoreServiceEval(formater.format(storeServiceEval));
		vo.setStoreDeliveryEval(formater.format(storeDeliveryEval));
		if(desEvalRate==-1){
			vo.setDesEvalRate("--");
		}else{
			vo.setDesEvalRate(formater1.format(desEvalRate)+"%");
		}
		if(serviceEvalRate==-1){
			vo.setServiceEvalRate("--");
		}else{
			vo.setServiceEvalRate(formater1.format(serviceEvalRate)+"%");
		}
		if(deliveryEvalRate==-1){
			vo.setDeliveryEvalRate("--");
		}else{
			vo.setDeliveryEvalRate(formater1.format(deliveryEvalRate)+"%");
		}
		vo.setAvgStoreEval(formater.format(avgStoreEval));
		vo.setAvgStoreEvalRate(String.valueOf(avgStoreEvalRate));
		return vo;
    }
    
}