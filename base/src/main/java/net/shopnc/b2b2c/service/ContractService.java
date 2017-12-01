package net.shopnc.b2b2c.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.contract.ContractApplyDao;
import net.shopnc.b2b2c.dao.contract.ContractCostlogDao;
import net.shopnc.b2b2c.dao.contract.ContractDao;
import net.shopnc.b2b2c.dao.contract.ContractItemDao;
import net.shopnc.b2b2c.dao.contract.ContractLogDao;
import net.shopnc.b2b2c.dao.contract.ContractQuitapplyDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.contract.Contract;
import net.shopnc.b2b2c.domain.contract.ContractApply;
import net.shopnc.b2b2c.domain.contract.ContractCostlog;
import net.shopnc.b2b2c.domain.contract.ContractItem;
import net.shopnc.b2b2c.domain.contract.ContractLog;
import net.shopnc.b2b2c.domain.contract.ContractQuitapply;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.vo.contract.ContractApplyVo;
import net.shopnc.b2b2c.vo.contract.ContractCostlogVo;
import net.shopnc.b2b2c.vo.contract.ContractItemBean;
import net.shopnc.b2b2c.vo.contract.ContractItemVo;
import net.shopnc.b2b2c.vo.contract.ContractLogVo;
import net.shopnc.b2b2c.vo.contract.ContractQuitapplyVo;
import net.shopnc.b2b2c.vo.contract.ContractVo;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.PriceHelper;

@Service
@Transactional(rollbackFor = {Exception.class})
public class ContractService {
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ContractItemDao contractItemDao;
    @Autowired
    private ContractApplyDao contractApplyDao;
    @Autowired
    private ContractQuitapplyDao contractQuitapplyDao;
    @Autowired
    private ContractLogDao contractLogDao;
    @Autowired
    private ContractCostlogDao contractCostlogDao;
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private SendMessageService sendMessageService;
    
    public static final String IMG_DAY7="img/cti/7day.png";//7天保障
    public static final String IMG_JSWL="img/cti/jswl.png";//极速物流
    public static final String IMG_PSBJ="img/cti/psbf.png";//破损补寄
    public static final String IMG_PZCN="img/cti/pz.png";//品质保障

    /**
     * 格式化日期
     */
    private String dateformat(Timestamp date,String pattern){
    	if(pattern==null){
    		pattern="yyyy-MM-dd HH:mm:ss";
    	}
    	return new SimpleDateFormat(pattern).format(date);
    }
    
    /**
     * 格式化数字
     */
    private String numberformat(double num,String formatStyle){
    	if (formatStyle == null) {
			formatStyle = "0.00";
		}
		DecimalFormat formater = new DecimalFormat(formatStyle);
		formater.setRoundingMode(RoundingMode.HALF_UP);
		return formater.format(num);
    }
    
    /**
     * 获取申请审核状态
     */
    private String getApplyAuditStateStr(int auditState){
    	String state="";
    	if(auditState==0){
    		state="等待审核";
		}else if(auditState==1){
			state="审核通过";
		}else if(auditState==2){
			state="审核失败";
		}else if(auditState==3){
			state="保证金待审核";
		}else if(auditState==4){
			state="保证金审核通过";
		}else if(auditState==5){
			state="保证金审核失败";
		}
    	return state;
    }
    
    /**
     * 获取退出审核状态
     */
    private String getQuitAuditStateStr(int auditState){
    	String state="";
    	if(auditState==0){
    		state="等待审核";
		}else if(auditState==1){
			state="审核通过";
		}else if(auditState==2){
			state="审核失败";
		}
    	return state;
    }
    
    /**
     * admin保障服务管理列表
     */
    public DtGrid getAdminContractItemList(String dtGridPager) throws Exception{
    	List<ContractItemBean> list=new ArrayList<ContractItemBean>();
    	DtGrid dtGrid=contractItemDao.getDtGridList(dtGridPager, ContractItem.class);
    	List<ContractItem> contractItemList=(List)dtGrid.getExhibitDatas();
    	for(ContractItem ci : contractItemList){
    		String ctiIconUrl="";
    		int id=ci.getCtiId();
    		String ctiIcon=ci.getCtiIcon();
    		if(id==1 && (ctiIcon==null || ctiIcon.isEmpty())){//七天保障
    			ctiIconUrl=ShopConfig.getPublicRoot()+IMG_DAY7;
    		}else if(id==2 && (ctiIcon==null || ctiIcon.isEmpty())){//极速物流
    			ctiIconUrl=ShopConfig.getPublicRoot()+IMG_JSWL;
    		}else if(id==3 && (ctiIcon==null || ctiIcon.isEmpty())){//破损补寄
    			ctiIconUrl=ShopConfig.getPublicRoot()+IMG_PSBJ;
    		}else if(id==4 && (ctiIcon==null || ctiIcon.isEmpty())){//品质承若
    			ctiIconUrl=ShopConfig.getPublicRoot()+IMG_PZCN;
    		}else{
    			if(ctiIcon!=null && !ctiIcon.isEmpty()){
    				ctiIconUrl=ShopConfig.getUploadRoot()+ctiIcon;
    			}
    		}
    		ContractItemBean bean=new ContractItemBean();
    		BeanUtils.copyProperties(ci, bean);
    		bean.setCtiIconUrl(ctiIconUrl);
    		list.add(bean);
    	}
    	dtGrid.setExhibitDatas((List)list);
    	return dtGrid;
    }
    
    /**
     * admin修改保障服务表
     */
    public void updateContractItem(ContractItemVo contractItemVo){
    	int ctiId=contractItemVo.getCtiId();
    	ContractItem contractItem=contractItemDao.get(ContractItem.class, ctiId);
    	contractItem.setCtiCost(new BigDecimal(contractItemVo.getCtiCost()));
    	contractItem.setCtiDescribe(contractItemVo.getCtiDescribe());
    	contractItem.setCtiDescurl(contractItemVo.getCtiDescurl());
    	contractItem.setCtiIcon(contractItemVo.getCtiIcon());
    	contractItem.setCtiName(contractItemVo.getCtiName());
    	contractItem.setCtiSort(Integer.parseInt(contractItemVo.getCtiSort()));
    	contractItem.setCtiState(Integer.parseInt(contractItemVo.getState()));
    	contractItemDao.update(contractItem);
    }
    
    /**
     * admin服务加入申请列表
     */
    public DtGrid getAdminContractApplyList(String dtGridPager) throws Exception{
    	DtGrid dtGrid=contractApplyDao.getDtGridList(dtGridPager, ContractApply.class);
    	List<ContractApply> list=(List)dtGrid.getExhibitDatas();
    	List<ContractApplyVo> voList=new ArrayList<ContractApplyVo>();
    	for(ContractApply ca : list){
    		ContractApplyVo vo=new ContractApplyVo();
    		vo.setCtaId(ca.getCtaId());
    		int ctaItemid=ca.getCtaItemid();
    		ContractItem cti=contractItemDao.get(ContractItem.class, ctaItemid);
    		vo.setCtaItemname(cti.getCtiName());
    		vo.setCtaStoreid(ca.getCtaStoreid());
    		vo.setCtaStorename(ca.getCtaStorename());
    		Timestamp t=ca.getCtaAddtime();
    		vo.setCtaAddtime(dateformat(t,"yyyy-MM-dd HH:mm:ss"));
    		int ctaAuditstate=ca.getCtaAuditstate();
    		vo.setCtaAuditstate(ctaAuditstate);
    		String ctaState=this.getApplyAuditStateStr(ctaAuditstate);
    		vo.setCtaState(ctaState);
    		vo.setCtaCost(numberformat(ca.getCtaCost().doubleValue(),null));
    		vo.setCtaCostimg(ca.getCtaCostimg());
    		voList.add(vo);
    	}
    	dtGrid.setExhibitDatas((List)voList);
    	return dtGrid;
    }
    
    /**
     * 审核apply
     */
    public void checkApply(int userId,String userName,ContractApplyVo contractApplyVo){
    	String cause=contractApplyVo.getCause();
    	int ctaId=contractApplyVo.getCtaId();
    	int ctaAuditstate=contractApplyVo.getCtaAuditstate();
    	ContractApply contractApply=contractApplyDao.get(ContractApply.class, ctaId);
    	contractApply.setCtaAuditstate(ctaAuditstate);
    	contractApplyDao.update(contractApply);
    	
    	int itemId=contractApply.getCtaItemid();
    	int storeId=contractApply.getCtaStoreid();
    	Contract contract=contractDao.getContract(storeId, itemId);
    	contract.setCtAuditstate(ctaAuditstate);
    	
    	//日志
    	if(ctaAuditstate!=0 && ctaAuditstate!=3){
	    	ContractItem contractItem=contractItemDao.get(ContractItem.class, itemId);
	    	String itemName=contractItem.getCtiName();
	    	String storeName=contractApply.getCtaStorename();
	    	String content="";
    		String messageApplyResult="";
    		String messageFailedCause="";
    		String tplCode="";
	    	if(ctaAuditstate==1){
	    		content="审核通过，待支付保证金";
	    		
	    		messageApplyResult="审核通过，待支付保证金";
	    		messageFailedCause="";
	    		tplCode="storeContractApply";
	    	}else if(ctaAuditstate==2){
	    		content="审核未通过，原因："+cause;
	    		contract.setCtJoinstate(0);
	    		
	    		messageApplyResult="审核未通过";
	    		messageFailedCause="原因："+cause;
	    		tplCode="storeContractApply";
	    	}else if(ctaAuditstate==4){
	    		content="保证金审核通过";
	    		contract.setCtJoinstate(2);
	    		contract.setCtCost(contractItem.getCtiCost());
	    		String c="申请加入保障服务，支付保证金";
	    		this.addContractCostlog(itemId, itemName, storeId, storeName, userId, userName, c, contractItem.getCtiCost().doubleValue());
	    		
	    		messageApplyResult="审核通过";
	    		messageFailedCause="";
	    		tplCode="storeContractCostApply";
	    	}else if(ctaAuditstate==5){
	    		content="保证金审核未通过，原因："+cause;
	    		
	    		messageApplyResult="审核未通过";
	    		messageFailedCause="原因："+cause;
	    		tplCode="storeContractCostApply";
	    	}
	    	this.addContractLog(itemId, itemName, storeId, storeName, userId, userName, content, "admin");
	    	
	    	//消息通知
	    	HashMap<String, Object> hashMapMsg = new HashMap<>();
    		hashMapMsg.put("itemName", itemName);
    		hashMapMsg.put("applyResult", messageApplyResult);
    		hashMapMsg.put("failedCause", messageFailedCause);
    		sendMessageService.sendStore(tplCode, storeId, hashMapMsg, String.valueOf(storeId));
    		
    	}
    	contractDao.update(contract);
    }
    
    
    /**
     * admin服务退出申请列表
     */
    public DtGrid getAdminContractQuitapplyList(String dtGridPager) throws Exception{
    	DtGrid dtGrid=contractQuitapplyDao.getDtGridList(dtGridPager, ContractQuitapply.class);
    	List<ContractQuitapply> list=(List)dtGrid.getExhibitDatas();
    	List<ContractQuitapplyVo> voList=new ArrayList<ContractQuitapplyVo>();
    	for(ContractQuitapply cq : list){
    		ContractQuitapplyVo vo=new ContractQuitapplyVo();
    		vo.setCtqId(cq.getCtqId());
    		vo.setCtqItemid(cq.getCtqItemid());
    		vo.setCtqItemname(cq.getCtqItemname());
    		vo.setCtqStoreid(cq.getCtqStoreid());
    		vo.setCtqStorename(cq.getCtqStorename());
    		vo.setCtqAddtime(this.dateformat(cq.getCtqAddtime(), null));
    		int ctqAuditstate=cq.getCtqAuditstate();
    		vo.setCtqAuditstate(ctqAuditstate);
    		String ctqState=this.getQuitAuditStateStr(ctqAuditstate);
    		vo.setCtqState(ctqState);
    		voList.add(vo);
    	}
    	dtGrid.setExhibitDatas((List)voList);
    	return dtGrid;
    }
    
    /**
     * 审核quitapply
     */
    public void checkQuitapply(int userId,String userName,ContractQuitapplyVo contractQuitapplyVo){
    	String cause=contractQuitapplyVo.getCause();
    	int ctqId=contractQuitapplyVo.getCtqId();
    	int ctqAuditstate=contractQuitapplyVo.getCtqAuditstate();
    	ContractQuitapply contractQuitapply=contractQuitapplyDao.get(ContractQuitapply.class, ctqId);
    	contractQuitapply.setCtqAuditstate(ctqAuditstate);
    	contractQuitapplyDao.update(contractQuitapply);
    	
    	int itemId=contractQuitapply.getCtqItemid();
    	int storeId=contractQuitapply.getCtqStoreid();
    	Contract contract=contractDao.getContract(storeId, itemId);
    	if(ctqAuditstate==1){
    		contract.setCtQuitstate(0);
    	}else if(ctqAuditstate==2){
    		contract.setCtQuitstate(2);
    	}
    	
    	
    	//日志
    	if(ctqAuditstate!=0){
    		String itemName=contractQuitapply.getCtqItemname();
    		String storeName=contractQuitapply.getCtqStorename();
    		String content="";
    		String messageApplyResult="";
    		String messageFailedCause="";
    		if(ctqAuditstate==1){
    			content="管理员审核通过店铺退出保障服务的申请";
    			contract.setCtJoinstate(0);
    			
    			messageApplyResult="审核通过";
        		messageFailedCause="";
    		}else if(ctqAuditstate==2){
    			content="管理员拒绝店铺退出保障服务的申请，原因："+cause;
    			
    			messageApplyResult="审核失败";
        		messageFailedCause="原因："+cause;
    		}
    		this.addContractLog(itemId, itemName, storeId, storeName, userId, userName, content, "admin");
    		
    		//消息通知
	    	HashMap<String, Object> hashMapMsg = new HashMap<>();
    		hashMapMsg.put("itemName", itemName);
    		hashMapMsg.put("applyResult", messageApplyResult);
    		hashMapMsg.put("failedCause", messageFailedCause);
    		sendMessageService.sendStore("storeContractQuitApply", storeId, hashMapMsg, String.valueOf(storeId));
    	}
    	contractDao.update(contract);
    }
    
    /**
     * admin店铺保障服务列表
     */
    public DtGrid getAdminContractList(String dtGridPager) throws Exception{
    	DtGrid dtGrid=contractDao.getDtGridList(dtGridPager, Contract.class);
    	List<Contract> list=(List)dtGrid.getExhibitDatas();
    	List<ContractVo> voList=new ArrayList<ContractVo>();
    	for(Contract c : list){
    		ContractVo vo=new ContractVo();
    		vo.setCtId(c.getCtId());
    		vo.setCtStoreid(c.getCtStoreid());
    		vo.setCtStorename(c.getCtStorename());
    		int ctItemid=c.getCtItemid();
    		vo.setCtItemid(ctItemid);
    		ContractItem cti=contractItemDao.get(ContractItem.class, ctItemid);
    		vo.setCtItemname(cti.getCtiName());
    		int ctAuditstate=c.getCtAuditstate();
    		vo.setCtAuditstate(ctAuditstate);
    		String ctAuditstateStr=this.getApplyAuditStateStr(ctAuditstate);
    		vo.setCtAuditstateStr(ctAuditstateStr);
    		int ctJoinstate=c.getCtJoinstate();
    		vo.setCtJoinstate(ctJoinstate);
    		String ctJoinstateStr="";
    		if(ctJoinstate==0){
    			ctJoinstateStr="未申请";
    		}else if(ctJoinstate==1){
    			ctJoinstateStr="已申请";
    		}if(ctJoinstate==2){
    			ctJoinstateStr="已加入";
    		}
    		vo.setCtJoinstateStr(ctJoinstateStr);
    		vo.setCtCost(numberformat(c.getCtCost().doubleValue(),null));
    		int ctClosestate=c.getCtClosestate();
    		vo.setCtClosestate(ctClosestate);
    		String ctClosestateStr="";
    		if(ctClosestate==0){
    			ctClosestateStr="永久关闭";
    		}else if(ctClosestate==1){
    			ctClosestateStr="开启";
    		}
    		vo.setCtClosestateStr(ctClosestateStr);
    		String ctState="";
    		if(ctClosestate==0){
    			ctState="永久禁止使用";
    		}else if(ctJoinstate==2){
    			ctState="已加入";
    		}else if(ctJoinstate==1){
    			ctState="申请进行中（"+ctAuditstateStr+"）";
    		}else{
    			ctState="未申请";
    		}
    		vo.setCtState(ctState);
    		voList.add(vo);
    	}
    	dtGrid.setExhibitDatas((List)voList);
    	return dtGrid;
    }
    
    /**
     * admin开启关闭店铺保障服务contract
     */
    public void checkClose(int userId,String userName,ContractVo contractVo){
    	String cause=contractVo.getCause();
    	int ctId=contractVo.getCtId();
    	int ctClosestate=contractVo.getCtClosestate();
    	Contract contract=contractDao.get(Contract.class, ctId);
    	int oldClosestate=contract.getCtClosestate();
    	contract.setCtClosestate(ctClosestate);
    	contractDao.update(contract);
    	
    	//日志
    	int itemId=contract.getCtItemid();
    	ContractItem cti=contractItemDao.get(ContractItem.class, itemId);
    	String itemName=cti.getCtiName();
    	int storeId=contract.getCtStoreid();
    	String storeName=contract.getCtStorename();
    	String content="";
    	if(oldClosestate==1 && ctClosestate==0){
        	content="关闭状态修改为“永久禁止使用”，原因："+cause;
        	this.addContractLog(itemId, itemName, storeId, storeName, userId, userName, content, "admin");
    	}else if(oldClosestate==0 && ctClosestate==1){
    		content="关闭状态修改为“允许使用”";
    		this.addContractLog(itemId, itemName, storeId, storeName, userId, userName, content, "admin");
    	}
    }
    
    /**
     * 修改保证金
     */
    public void updateCost(int userId,String userName,int ctId,String zjType,double cost,String cause){
    	String content1="";
    	String content2="";
    	Contract contract=contractDao.get(Contract.class, ctId);
    	BigDecimal ctCost=contract.getCtCost();
    	BigDecimal money=ctCost;
    	if("-".equals(zjType)){
    		money=PriceHelper.sub(ctCost, BigDecimal.valueOf(cost));
    		content1="保证金减少"+numberformat(cost,null)+"元";
    	}else if("+".equals(zjType)){
    		money=PriceHelper.add(ctCost, BigDecimal.valueOf(cost));
    		content1="保证金增加"+numberformat(cost,null)+"元";
    	}
    	contract.setCtCost(money);
    	contractDao.update(contract);
    	
    	//日志
    	int itemId=contract.getCtItemid();
    	ContractItem cti=contractItemDao.get(ContractItem.class, itemId);
    	String itemName=cti.getCtiName();
    	int storeId=contract.getCtStoreid();
    	String storeName=contract.getCtStorename();
    	content2="管理员操作保证金，原因描述："+cause;
    	this.addContractLog(itemId, itemName, storeId, storeName, userId, userName, content1, "admin");
    	this.addContractCostlog(itemId, itemName, storeId, storeName, userId, userName, content2, cost);
    }
    
    /**
     * 查询服务保障信息
     * @param ctid
     * @return
     */
    public ContractVo getContract(int ctid){
    	Contract contract=contractDao.get(Contract.class, ctid);
    	int ctClosestate=contract.getCtClosestate();
    	int ctJoinstate=contract.getCtJoinstate();
    	int ctAuditstate=contract.getCtAuditstate();
    	String ctAuditstateStr=this.getApplyAuditStateStr(ctAuditstate);
    	String ctState="";
		if(ctClosestate==0){
			ctState="永久禁止使用";
		}else if(ctJoinstate==2){
			ctState="已加入";
		}else if(ctJoinstate==1){
			ctState="申请进行中（"+ctAuditstateStr+"）";
		}else{
			ctState="未申请";
		}
    	ContractVo vo=new ContractVo();
    	vo.setCtStorename(contract.getCtStorename());
    	int ctItemid=contract.getCtItemid();
    	vo.setCtItemid(ctItemid);
		ContractItem cti=contractItemDao.get(ContractItem.class, ctItemid);
		vo.setCtItemname(cti.getCtiName());
    	vo.setCtCost(numberformat(contract.getCtCost().doubleValue(),null));
    	vo.setCtStoreid(contract.getCtStoreid());
    	vo.setCtState(ctState);
    	return vo;
    }
    
    /**
     * 查询服务保障信息
     * @param storeId
     * @param itemId
     * @return
     */
    public ContractVo getContract(int storeId,int itemId){
    	Contract contract=contractDao.getContract(storeId, itemId);
    	if(contract==null){
    		ContractVo vo=new ContractVo();
    		vo.setCtItemid(itemId);
    		vo.setCtStoreid(storeId);
    		vo.setCtState("未申请");
    		ContractItem cti=contractItemDao.get(ContractItem.class, itemId);
    		vo.setCtItemname(cti.getCtiName());
        	vo.setCtiCost(numberformat(cti.getCtiCost().doubleValue(),null));
    		return vo;
    	}
    	int ctClosestate=contract.getCtClosestate();
    	int ctJoinstate=contract.getCtJoinstate();
    	int ctAuditstate=contract.getCtAuditstate();
    	String ctAuditstateStr=this.getApplyAuditStateStr(ctAuditstate);
    	String ctState="";
		if(ctClosestate==0){
			ctState="永久禁止使用";
		}else if(ctJoinstate==2){
			ctState="已加入";
		}else if(ctJoinstate==1){
			ctState="申请进行中（"+ctAuditstateStr+"）";
		}else{
			ctState="未申请";
		}
    	ContractVo vo=new ContractVo();
    	vo.setCtItemid(itemId);
    	vo.setCtStoreid(storeId);
    	vo.setCtStorename(contract.getCtStorename());
    	int ctItemid=contract.getCtItemid();
    	vo.setCtItemid(ctItemid);
		ContractItem cti=contractItemDao.get(ContractItem.class, ctItemid);
		vo.setCtItemname(cti.getCtiName());
		vo.setCtiCost(numberformat(cti.getCtiCost().doubleValue(),null));
    	vo.setCtCost(numberformat(contract.getCtCost().doubleValue(),null));
    	vo.setCtStoreid(contract.getCtStoreid());
    	vo.setCtState(ctState);
    	return vo;
    }
    
    /**
     * 保障服务日志 根据店铺和服务ID获取
     */
    public DtGrid getAdminContractLogList(String dtGridPager) throws Exception{
    	DtGrid dtGrid=contractLogDao.getDtGridList(dtGridPager, ContractLog.class);
    	List<ContractLog> list=(List)dtGrid.getExhibitDatas();
    	List<ContractLogVo> voList=new ArrayList<ContractLogVo>();
    	for(ContractLog cl : list){
    		ContractLogVo vo=new ContractLogVo();
    		vo.setLogStorename(cl.getLogStorename());
    		vo.setLogItemname(cl.getLogItemname());
    		vo.setLogAddtime(this.dateformat(cl.getLogAddtime(), null));
    		String logUserName=cl.getLogUsername();
    		String logRole=cl.getLogRole();
    		String operater="";//操作人
    		if("admin".equals(logRole)){
    			operater=logUserName+"（平台管理员）";
    		}else if("seller".equals(logRole)){
    			operater=logUserName+"（商家）";
    		}
    		vo.setOperater(operater);
    		vo.setLogMsg(cl.getLogMsg());
    		voList.add(vo);
    	}
    	dtGrid.setExhibitDatas((List)voList);
    	return dtGrid;
    }
    
    
    /**
     * 保证金日志 根据店铺和服务ID获取
     */
    public DtGrid getAdminContractCostlogList(String dtGridPager) throws Exception{
    	DtGrid dtGrid=contractCostlogDao.getDtGridList(dtGridPager, ContractCostlog.class);
    	List<ContractCostlog> list=(List)dtGrid.getExhibitDatas();
    	List<ContractCostlogVo> voList=new ArrayList<ContractCostlogVo>();
    	for(ContractCostlog cc : list){
    		ContractCostlogVo vo=new ContractCostlogVo();
    		vo.setClogItemid(cc.getClogItemid());
    		vo.setClogItemname(cc.getClogItemname());
    		vo.setClogStoreid(cc.getClogStoreid());
    		vo.setClogStorename(cc.getClogStorename());
    		vo.setClogPrice(numberformat(cc.getClogPrice().doubleValue(),null));
    		vo.setClogAdminname(cc.getClogAdminname());
    		vo.setClogAddtime(this.dateformat(cc.getClogAddtime(), null));
    		vo.setClogDesc(cc.getClogDesc());
    		voList.add(vo);
    	}
    	dtGrid.setExhibitDatas((List)voList);
    	return dtGrid;
    }
    
    /**
     * 添加保障日志
     */
    public void addContractLog(int itemId,String itemName,int storeId,String storeName,int userId,String userName,String content,String logRole){
    	ContractLog contractLog=new ContractLog();
    	contractLog.setLogAddtime(new Timestamp(new Date().getTime()));
    	contractLog.setLogItemid(itemId);
    	contractLog.setLogItemname(itemName);
    	contractLog.setLogStoreid(storeId);
    	contractLog.setLogStorename(storeName);
    	contractLog.setLogUserid(userId);
    	contractLog.setLogUsername(userName);
    	contractLog.setLogMsg(content);
    	contractLog.setLogRole(logRole);
    	contractLogDao.save(contractLog);
    }
    
    /**
     * 添加保证金日志
     */
    public void addContractCostlog(int itemId,String itemName,int storeId,String storeName,int userId,String userName,String content,double cost){
    	ContractCostlog contractCostlog=new ContractCostlog();
    	contractCostlog.setClogAddtime(new Timestamp(new Date().getTime()));
    	contractCostlog.setClogAdminid(userId);
    	contractCostlog.setClogAdminname(userName);
    	contractCostlog.setClogItemid(itemId);
    	contractCostlog.setClogItemname(itemName);
    	contractCostlog.setClogDesc(content);
    	contractCostlog.setClogPrice(BigDecimal.valueOf(cost));
    	contractCostlog.setClogStoreid(storeId);
    	contractCostlog.setClogStorename(storeName);
    	contractCostlogDao.save(contractCostlog);
    }
    
    
    
    /**
     * seller保障服务列表
     */
    public List<ContractVo> getSellerContractList(int storeId){
    	List<String> condition=new ArrayList<String>();
    	HashMap<String,Object> params=new HashMap<String,Object>();
    	condition.add("ctiState=:ctiState");
    	params.put("ctiState", 1);
    	List<ContractItem> itemList=contractItemDao.getAllContractItem(condition,params);
    	List<ContractVo> voList=new ArrayList<ContractVo>();
    	for(ContractItem item : itemList){
    		int itemId=item.getCtiId();
    		String itemName=item.getCtiName();
    		String desc=item.getCtiDescribe();
    		String ctiIcon=item.getCtiIcon();
    		BigDecimal cost=item.getCtiCost();//所需保证金
    		String ctiDescurl=item.getCtiDescurl();
    		
    		Contract contract=contractDao.getContract(storeId, itemId);
    		int auditState=-1;
    		int quitState=0;
    		int closeState=1;
    		int joinState=0;
    		int ctId=-1;
    		if(contract!=null){
	    		auditState=contract.getCtAuditstate();
	    		quitState=contract.getCtQuitstate();
	    		closeState=contract.getCtClosestate();
	    		joinState=contract.getCtJoinstate();
	    		ctId=contract.getCtId();
    		}
    		
    		String ctiIconUrl="";
    		int id=item.getCtiId();
    		if(id==1 && (ctiIcon==null || ctiIcon.isEmpty())){//七天保障
    			ctiIconUrl=ShopConfig.getPublicRoot()+IMG_DAY7;
    		}else if(id==2 && (ctiIcon==null || ctiIcon.isEmpty())){//极速物流
    			ctiIconUrl=ShopConfig.getPublicRoot()+IMG_JSWL;
    		}else if(id==3 && (ctiIcon==null || ctiIcon.isEmpty())){//破损补寄
    			ctiIconUrl=ShopConfig.getPublicRoot()+IMG_PSBJ;
    		}else if(id==4 && (ctiIcon==null || ctiIcon.isEmpty())){//品质承若
    			ctiIconUrl=ShopConfig.getPublicRoot()+IMG_PZCN;
    		}else{
    			if(ctiIcon!=null && !ctiIcon.isEmpty()){
    				ctiIconUrl=ShopConfig.getUploadRoot()+ctiIcon;
    			}
    		}
    		
    		ContractVo vo=new ContractVo();
    		vo.setCtId(ctId);
    		vo.setCtItemid(itemId);
    		vo.setCtItemname(itemName);
    		vo.setDesc(desc);
    		vo.setIcon(ctiIcon);
    		vo.setCtCost(numberformat(cost.doubleValue(),null));
    		vo.setCtAuditstate(auditState);
    		vo.setCtQuitstate(quitState);
    		vo.setCtClosestate(closeState);
    		vo.setCtJoinstate(joinState);
    		vo.setIconUrl(ctiIconUrl);
    		vo.setCtDescurl(ctiDescurl);
    		voList.add(vo);
    	}
    	return voList;
    }
    
    /**
     * 卖家保障日志
     */
    public Map<String,Object> getPageSellerContractLog(int pageNo,int storeId,int itemId){
    	List<String> condition=new ArrayList<String>();
    	HashMap<String,Object> params=new HashMap<String,Object>();
    	condition.add("logItemid=:itemId");
    	condition.add("logStoreid=:storeId");
    	params.put("itemId", itemId);
    	params.put("storeId", storeId);
    	long maxPageSize=contractLogDao.getContractCostlogCount(condition, params);
    	PageEntity pageEntity = new PageEntity();
    	pageEntity.setTotal(maxPageSize);
        pageEntity.setPageNo(pageNo);
    	List<ContractLog> contractLogList=contractLogDao.getContractCostlog(pageNo,pageEntity.getPageSize(),condition, params);
    	
        List<ContractLogVo> voList=new ArrayList<ContractLogVo>();
        for(ContractLog cl : contractLogList){
    		ContractLogVo vo=new ContractLogVo();
    		vo.setLogAddtime(this.dateformat(cl.getLogAddtime(), null));
    		String logUserName=cl.getLogUsername();
    		String logRole=cl.getLogRole();
    		String operater="";//操作人
    		if("admin".equals(logRole)){
    			operater="平台管理员";
    		}else if("seller".equals(logRole)){
    			operater=logUserName+"（商家）";
    		}
    		vo.setOperater(operater);
    		vo.setLogMsg(cl.getLogMsg());
    		voList.add(vo);
        }
        
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("list", voList);
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }
    
    /**
     * 卖家保证金日志
     */
    public Map<String,Object> getPageSellerContractCostlog(int pageNo,int storeId,int itemId){
    	List<String> condition=new ArrayList<String>();
    	HashMap<String,Object> params=new HashMap<String,Object>();
    	condition.add("clogItemid=:itemId");
    	condition.add("clogStoreid=:storeId");
    	params.put("itemId", itemId);
    	params.put("storeId", storeId);
    	long maxPageSize=contractCostlogDao.getContractCostlogCount(condition, params);
    	PageEntity pageEntity = new PageEntity();
    	pageEntity.setTotal(maxPageSize);
        pageEntity.setPageNo(pageNo);
        List<ContractCostlog> contractCostlogList=contractCostlogDao.getContractCostlog(pageNo, pageEntity.getPageSize(), condition, params);
        List<ContractCostlogVo> voList=new ArrayList<ContractCostlogVo>();
        for(ContractCostlog cl : contractCostlogList){
        	ContractCostlogVo vo=new ContractCostlogVo();
        	vo.setClogPrice(numberformat(cl.getClogPrice().doubleValue(),null));
        	vo.setClogAddtime(dateformat(cl.getClogAddtime(),null));
        	vo.setClogDesc(cl.getClogDesc());
        	voList.add(vo);
        }
    	
    	Map<String,Object> result = new HashMap<String,Object>();
        result.put("list", voList);
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }
    
    /**
     * 申请加入保障服务
     */
    public void joinContract(int userId,String userName,int storeId,int itemId){
    	Date now=new Date();
    	ContractItem contractItem=contractItemDao.get(ContractItem.class, itemId);
    	Store store=storeDao.get(Store.class, storeId);
    	ContractApply apply=new ContractApply();
    	apply.setCtaItemid(itemId);
    	apply.setCtaStoreid(storeId);
    	apply.setCtaStorename(store.getStoreName());
    	apply.setCtaAddtime(new Timestamp(now.getTime()));
    	apply.setCtaAuditstate(0);
    	apply.setCtaCost(contractItem.getCtiCost());
    	contractApplyDao.save(apply);
    	
    	Contract contract=contractDao.getContract(storeId, itemId);
    	if(contract==null){
    		//插入
    		contract=new Contract();
    		contract.setCtStoreid(storeId);
    		contract.setCtStorename(store.getStoreName());
    		contract.setCtItemid(itemId);
    		contract.setCtAuditstate(0);
    		contract.setCtJoinstate(1);
    		contract.setCtCost(BigDecimal.valueOf(0));
    		contract.setCtClosestate(1);
    		contract.setCtQuitstate(0);
    		contractDao.save(contract);
    	}else{
    		//更新
    		contract.setCtAuditstate(0);
    		contract.setCtJoinstate(1);
    		contract.setCtQuitstate(0);
    		contractDao.update(contract);
    	}
    	String content="店铺申请加入保障服务";
    	this.addContractLog(itemId, contractItem.getCtiName(), storeId, store.getStoreName(), userId, userName, content, "seller");
    }
    
    /**
     * 获取申请服务保障信息
     */
    public ContractApply getContractApply(int storeId,int itemId){
    	List<String> condition=new ArrayList<String>();
    	HashMap<String,Object> params=new HashMap<String,Object>();
    	condition.add("ctaStoreid=:storeId");
    	condition.add("ctaItemid=:itemId");
    	condition.add("(ctaAuditstate=:ctaAuditstate1 or ctaAuditstate=:ctaAuditstate5)");
    	params.put("storeId", storeId);
    	params.put("itemId", itemId);
    	params.put("ctaAuditstate1", 1);
    	params.put("ctaAuditstate5", 5);
    	ContractApply contractApply=contractApplyDao.getContractApply(condition, params);
    	return contractApply;
    }
    
    /**
     * 申请支付保证金
     */
    public void payCost(int userId,String userName,ContractApplyVo contractApplyVo){
    	int ctaId=contractApplyVo.getCtaId();
    	String ctaCostimg=contractApplyVo.getCtaCostimg();
    	ContractApply contractApply=contractApplyDao.get(ContractApply.class, ctaId);
    	contractApply.setCtaCostimg(ctaCostimg);
    	contractApply.setCtaAuditstate(3);
    	contractApplyDao.update(contractApply);
    	int itemId=contractApply.getCtaItemid();
    	int storeId=contractApply.getCtaStoreid();
    	Contract contract=contractDao.getContract(storeId, itemId);
    	contract.setCtAuditstate(3);
    	contractDao.update(contract);
    	String content="店铺支付保证金";
    	ContractItem contractItem=contractItemDao.get(ContractItem.class, itemId);
    	this.addContractLog(itemId, contractItem.getCtiName(), storeId, contractApply.getCtaStorename(), userId, userName, content, "seller");
    }
    
    /**
     * 申请退出保障服务
     */
    public void quitContract(int userId,String userName,int storeId,int itemId){
    	ContractItem contractItem=contractItemDao.get(ContractItem.class, itemId);
    	String itemName=contractItem.getCtiName();
    	Store store=storeDao.get(Store.class, storeId);
    	ContractQuitapply contractQuitapply=new ContractQuitapply();
    	contractQuitapply.setCtqItemid(itemId);
    	contractQuitapply.setCtqItemname(itemName);
    	contractQuitapply.setCtqStoreid(storeId);
    	contractQuitapply.setCtqStorename(store.getStoreName());
    	contractQuitapply.setCtqAddtime(new Timestamp(new Date().getTime()));
    	contractQuitapply.setCtqAuditstate(0);
    	contractQuitapplyDao.save(contractQuitapply);
    	
    	Contract contract=contractDao.getContract(storeId, itemId);
    	contract.setCtQuitstate(1);
    	contractDao.update(contract);
    	
    	String content="店铺申请退出保障服务";
    	this.addContractLog(itemId, itemName, storeId, store.getStoreName(), userId, userName, content, "seller");
    	
    }
    
    /**
     * 首页显示四个
     */
    public List<ContractItemBean> getMainContract(){
    	List<String> condition=new ArrayList<String>();
    	condition.add("ctiState=:ctiState");
    	HashMap<String,Object> params=new HashMap<String,Object>();
    	params.put("ctiState", 1);
    	List<ContractItem> contractItemList=contractItemDao.getAllContractItem(condition, params);
    	List<ContractItemBean> list=new ArrayList<ContractItemBean>();
    	int size=contractItemList.size();
    	if(size>4){
    		size=4;
    	}
    	
		for(int i=0;i<size;i++){
			ContractItem item=contractItemList.get(i);
			String ctiIconUrl="";
    		int id=item.getCtiId();
    		String ctiIcon=item.getCtiIcon();
    		if(id==1 && (ctiIcon==null || ctiIcon.isEmpty())){//七天保障
    			ctiIconUrl=ShopConfig.getPublicRoot()+IMG_DAY7;
    		}else if(id==2 && (ctiIcon==null || ctiIcon.isEmpty())){//极速物流
    			ctiIconUrl=ShopConfig.getPublicRoot()+IMG_JSWL;
    		}else if(id==3 && (ctiIcon==null || ctiIcon.isEmpty())){//破损补寄
    			ctiIconUrl=ShopConfig.getPublicRoot()+IMG_PSBJ;
    		}else if(id==4 && (ctiIcon==null || ctiIcon.isEmpty())){//品质承若
    			ctiIconUrl=ShopConfig.getPublicRoot()+IMG_PZCN;
    		}else{
    			if(ctiIcon!=null && !ctiIcon.isEmpty()){
    				ctiIconUrl=ShopConfig.getUploadRoot()+ctiIcon;
    			}
    		}
    		ContractItemBean bean=new ContractItemBean();
    		BeanUtils.copyProperties(item, bean);
    		bean.setCtiIconUrl(ctiIconUrl);
			list.add(bean);
		}
		return list;
    }
    
    /**
     * 商品详情页面显示开通的保障服务
     */
    public List<ContractVo> getStoreContract(int storeId){
    	List<ContractVo> contractVoList=this.getSellerContractList(storeId);
    	List<ContractVo> voList=new ArrayList<ContractVo>();
    	for(ContractVo contractVo : contractVoList){
    		int joinState=contractVo.getCtJoinstate();
    		if(joinState==2){
    			voList.add(contractVo);
    		}
    	}
    	return voList;
    }
    
    /**
     * 查询申请加入数、申请保证金数、申请退出数
     */
    public Map<String,Long> countApply(){
    	long countApply=contractApplyDao.countApply(0);//申请加入数
    	long countCostApply=contractApplyDao.countApply(3);//申请保证金数
    	long countQuitApply=contractQuitapplyDao.countQuitApply(0);//申请退出数
    	Map<String,Long> map=new HashMap<String,Long>();
    	map.put("countApply", countApply);
    	map.put("countCostApply", countCostApply);
    	map.put("countQuitApply", countQuitApply);
    	return map;
    }
}
