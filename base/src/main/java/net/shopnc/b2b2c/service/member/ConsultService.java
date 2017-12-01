package net.shopnc.b2b2c.service.member;


import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.goods.GoodsDao;
import net.shopnc.b2b2c.dao.member.ConsultClassDao;
import net.shopnc.b2b2c.dao.member.ConsultDao;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.goods.Goods;
import net.shopnc.b2b2c.domain.member.Consult;
import net.shopnc.b2b2c.domain.member.ConsultClass;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.vo.consult.ConsultVo;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.common.entity.PageEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-01-12
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class ConsultService extends BaseService {

    @Autowired
    private ConsultClassDao consultClassDao;
    @Autowired
    private ConsultDao consultDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private SiteService siteService;

    /**
     * 获取咨询分类表格数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getConsultClassDtGridList(String dtGridPager) throws Exception {
        return consultClassDao.getDtGridList(dtGridPager, ConsultClass.class);
    }
    /**
     * 删除咨询分类
     * @param classId
     * @return
     * @throws ShopException
     */
    public boolean delConsultClass(int classId) throws ShopException{
        if (classId <= 0) {
            throw new ParameterErrorException();
        }
        //查询分类下是否存在咨询信息
        /*HashMap<String,String> where = new HashMap<String, String>();
        where.put("classId", "classId = :classId");

        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("classId", classId);
        int consultCount = consultDao.findConsultCount(where, params).intValue();
        if (consultCount > 0) {
            throw new ShopException("该信息下存在咨询记录不能删除");
        }*/
        return consultClassDao.deleteConsultById(classId);
    }
    /**
     * 是否可以发表咨询
     * @param memberId
     * @return
     */
    public boolean isAbleConsult(Integer memberId) {
        boolean isAble = false;
        if (memberId==null || memberId<=0) {//游客
            if (siteService.getSiteInfo().get(SiteTitle.GUESTCONSULT).equals("1")) {
                isAble = true;
            }
        }else{
            //查询会员信息
            Member member = memberDao.get(Member.class, memberId);
            if (member!=null && member.getAllowTalk() == State.YES) {
                isAble = true;
            }
        }
        return isAble;
    }
    /**
     * 新增咨询
     * @param consult
     * @return
     * @throws ShopException
     */
    public Serializable addConsult(Consult consult) throws ShopException{
        if (this.isAbleConsult(consult.getMemberId()) == false) {
            throw new ShopException("您没有咨询权限");
        }
        //验证咨询类型是否合法
        ConsultClass classInfo = consultClassDao.get(ConsultClass.class, consult.getClassId());
        if (classInfo==null){
            throw new ParameterErrorException();
        }
        //查询商品信息
        Goods goodsInfo = goodsDao.get(Goods.class, consult.getGoodsId());
        if (goodsInfo==null){
            throw new ParameterErrorException();
        }
        //验证匿名
        if (consult.getAnonymousState() == State.YES) {
            consult.setAnonymousState(State.YES);
        }else{
            consult.setAnonymousState(State.NO);
        }
        consult.setAddTime(ShopHelper.getCurrentTimestamp());
        return consultDao.save(consult);
    }
    /**
     * 回复咨询
     * @param consultReply
     * @param consultId
     * @param storeId
     * @return
     * @throws ShopException
     */
    public boolean replyConsult(String consultReply, Integer consultId, Integer storeId) throws ShopException{
        if (consultReply==null || consultReply.length()<=0) {
            throw new ShopException("请填写回复内容");
        }
        if (consultId==null || consultId<=0 || storeId==null || storeId<=0) {
            throw new ParameterErrorException();
        }

        HashMap<String, String> update = new HashMap<String, String>();
        update.put("consultReply", "updateConsultReply");
        update.put("replyTime", "updateReplyTime");

        HashMap<String, String> where = new HashMap<String, String>();
        where.put("consultId", "whereConsultId");
        where.put("storeId", "whereStoreId");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("whereConsultId", consultId);
        params.put("whereStoreId", storeId);

        params.put("updateConsultReply", consultReply);
        params.put("updateReplyTime", ShopHelper.getCurrentTimestamp());
        consultDao.updateByWhere(where, update, params);
        return true;
    }
    /**
     * 删除咨询
     * @param params
     * @return
     * @throws ShopException
     */
    public boolean deleteConsult(HashMap<String,Object> params) throws ShopException{
        if (params == null || params.size()<=0) {
            throw new ParameterErrorException();
        }
        HashMap<String, String> where = new HashMap<String, String>();
        for (String key : params.keySet()) {
            if (key.equals("consultId")) {
                where.put("consultId", "consultId = :consultId");
            }
            if (key.equals("storeId")) {
                where.put("storeId", "storeId = :storeId");
            }
            if (key.equals("consultIdIn")) {
                where.put("consultIdIn", "consultId in (:consultIdIn)");
            }
        }
        consultDao.deleteConsult(where, params);
        return true;
    }
    /**
     * 查询咨询分页列表
     * @param params
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional
    public HashMap<String,Object> getConsultListByPage(HashMap<String,Object> params, int page, int pageSize) {
        HashMap<String,String> where = new HashMap<String,String>();
        for (String key : params.keySet()) {
            if (key.equals("goodsId")) {
                where.put("goodsId", "goodsId = :goodsId");
            }
            if (key.equals("classId")) {
                where.put("classId", "classId = :classId");
            }
            if (key.equals("storeId")) {
                where.put("storeId", "storeId = :storeId");
            }
            if (key.equals("memberId")) {
                where.put("memberId", "memberId = :memberId");
            }
            if (key.equals("consultReplyEq")) {
                where.put("consultReplyEq", "consultReply = :consultReplyEq");
            }
            if (key.equals("consultReplyNeq")) {
                where.put("consultReplyNeq", "consultReply != :consultReplyNeq");
            }
        }
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(consultDao.findConsultCount(where, params));
        pageEntity.setPageNo(page);
        if (pageSize > 0) {
            pageEntity.setPageSize(pageSize);
        }
        List<Consult> consultList = consultDao.getConsultListByPage(where, params, pageEntity.getPageNo(), pageEntity.getPageSize(),"");

        List<Integer> memberIdList = new ArrayList<Integer>();
        List<Integer> storeIdList = new ArrayList<Integer>();
        List<Integer> classIdList = new ArrayList<Integer>();
        List<Integer> goodsIdList = new ArrayList<Integer>();
        for (int i = 0; i < consultList.size(); i++) {
            Consult consultInfo = consultList.get(i);
            memberIdList.add(consultInfo.getMemberId());
            storeIdList.add(consultInfo.getStoreId());
            classIdList.add(consultInfo.getClassId());
            goodsIdList.add(consultInfo.getGoodsId());
        }

        //查询会员信息
        List<Member> listMember = new ArrayList<Member>();
        if (memberIdList.size() > 0) {
            HashMap<String,String> whereMember = new HashMap<String, String>();
            whereMember.put("memberIdIn", "memberId in (:memberIdIn)");
            HashMap<String,Object> paramsMember = new HashMap<String, Object>();
            paramsMember.put("memberIdIn", memberIdList);
            listMember = memberDao.getMemberList(whereMember, paramsMember);
        }
        //查询店铺信息
        List<Store> listStore = new ArrayList<Store>();
        if (storeIdList.size() > 0) {
            HashMap<String,String> whereStore = new HashMap<String, String>();
            whereStore.put("storeIdIn", "storeId in (:storeIdIn)");
            HashMap<String,Object> paramsStore = new HashMap<String, Object>();
            paramsStore.put("storeIdIn", storeIdList);
            listStore = storeDao.getStoreList(whereStore, paramsStore);
        }
        //查询咨询类型信息
        List<ConsultClass> listConsultClass = new ArrayList<ConsultClass>();
        if (classIdList.size() > 0) {
            HashMap<String,String> whereClass = new HashMap<String, String>();
            whereClass.put("classIdIn", "classId in (:classIdIn)");
            HashMap<String,Object> paramsClass = new HashMap<String, Object>();
            paramsClass.put("classIdIn", classIdList);
            listConsultClass = consultClassDao.getConsultClassList(whereClass, paramsClass);
        }
        //查询商品信息
        List<Object> listGoods = new ArrayList<Object>();
        if (goodsIdList.size() > 0) {
            HashMap<String,String> whereGoods = new HashMap<String, String>();
            whereGoods.put("goodsIdIn", "g.goodsId in (:goodsIdIn)");
            HashMap<String,Object> paramsGoods = new HashMap<String, Object>();
            paramsGoods.put("goodsIdIn", goodsIdList);
            listGoods = goodsDao.findGoodsAndCommonList(whereGoods, paramsGoods);
        }
        //构造咨询VO实体
        List<Object> consultVoList = new ArrayList<Object>();
        for (int i = 0; i < consultList.size(); i++) {
            Consult consultInfo = consultList.get(i);
            ConsultVo consultVo = new ConsultVo();
            consultVo.setConsultId(consultInfo.getConsultId());
            consultVo.setGoodsId(consultInfo.getGoodsId());
            consultVo.setMemberId(consultInfo.getMemberId());
            //商品名称
            for (int j = 0; j < listGoods.size(); j++) {
                if (((GoodsVo)listGoods.get(j)).getGoodsId() == consultInfo.getGoodsId()) {
                    consultVo.setGoodsName(((GoodsVo)listGoods.get(j)).getGoodsName());
                }
            }
            consultVo.setMemberId(consultInfo.getMemberId());
            //会员名称
            for (int j = 0; j < listMember.size(); j++) {
                if (listMember.get(j).getMemberId() == consultInfo.getMemberId()) {
                    consultVo.setMemberName(listMember.get(j).getMemberName());
                }
            }
            consultVo.setStoreId(consultInfo.getStoreId());
            //店铺名称
            for (int j = 0; j < listStore.size(); j++) {
                if (listStore.get(j).getStoreId() == consultInfo.getStoreId()) {
                    consultVo.setStoreName(listStore.get(j).getStoreName());
                }
            }
            consultVo.setClassId(consultInfo.getClassId());
            //分类名称
            for (int j = 0; j < listConsultClass.size(); j++) {
                if (listConsultClass.get(j).getClassId() == consultInfo.getClassId()) {
                    consultVo.setClassName(listConsultClass.get(j).getClassName());
                }
            }
            consultVo.setConsultContent(consultInfo.getConsultContent());
            consultVo.setAddTime(consultInfo.getAddTime());
            consultVo.setConsultReply(consultInfo.getConsultReply());
            consultVo.setReplyTime(consultInfo.getReplyTime());
            consultVo.setAnonymousState(consultInfo.getAnonymousState());
            consultVo.setReadState(consultInfo.getReadState());
            consultVoList.add(consultVo);
        }
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("list", consultVoList);
        result.put("showPage", pageEntity.getPageHtml());
        return result;
    }
    /**
     * 获取咨询表格数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getConsultDtGridList(String dtGridPager) throws Exception {
        DtGrid dtGrid = consultDao.getDtGridList(dtGridPager, Consult.class);
        List<Object> consultList = dtGrid.getExhibitDatas();

        List<Integer> memberIdList = new ArrayList<Integer>();
        List<Integer> storeIdList = new ArrayList<Integer>();
        List<Integer> classIdList = new ArrayList<Integer>();
        List<Integer> goodsIdList = new ArrayList<Integer>();
        for (int i = 0; i < consultList.size(); i++) {
            Consult consultInfo = (Consult) consultList.get(i);
            memberIdList.add(consultInfo.getMemberId());
            storeIdList.add(consultInfo.getStoreId());
            classIdList.add(consultInfo.getClassId());
            goodsIdList.add(consultInfo.getGoodsId());
        }
        //查询会员信息
        List<Member> listMember = new ArrayList<Member>();
        if (memberIdList.size() > 0) {
            HashMap<String,String> whereMember = new HashMap<String, String>();
            whereMember.put("memberIdIn", "memberId in (:memberIdIn)");
            HashMap<String,Object> paramsMember = new HashMap<String, Object>();
            paramsMember.put("memberIdIn", memberIdList);
            listMember = memberDao.getMemberList(whereMember, paramsMember);
        }
        //查询店铺信息
        List<Store> listStore = new ArrayList<Store>();
        if (storeIdList.size() > 0) {
            HashMap<String,String> whereStore = new HashMap<String, String>();
            whereStore.put("storeIdIn", "storeId in (:storeIdIn)");
            HashMap<String,Object> paramsStore = new HashMap<String, Object>();
            paramsStore.put("storeIdIn", storeIdList);
            listStore = storeDao.getStoreList(whereStore, paramsStore);
        }
        //查询咨询类型信息
        List<ConsultClass> listConsultClass = new ArrayList<ConsultClass>();
        if (classIdList.size() > 0) {
            HashMap<String,String> whereClass = new HashMap<String, String>();
            whereClass.put("classIdIn", "classId in (:classIdIn)");
            HashMap<String,Object> paramsClass = new HashMap<String, Object>();
            paramsClass.put("classIdIn", classIdList);
            listConsultClass = consultClassDao.getConsultClassList(whereClass, paramsClass);
        }
        //查询商品信息
        List<Object> listGoods = new ArrayList<Object>();
        if (goodsIdList.size() > 0) {
            HashMap<String,String> whereGoods = new HashMap<String, String>();
            whereGoods.put("goodsIdIn", "g.goodsId in (:goodsIdIn)");
            HashMap<String,Object> paramsGoods = new HashMap<String, Object>();
            paramsGoods.put("goodsIdIn", goodsIdList);
            listGoods = goodsDao.findGoodsAndCommonList(whereGoods, paramsGoods);
        }
        //构造咨询VO实体
        List<Object> consultVoList = new ArrayList<Object>();
        for (int i = 0; i < consultList.size(); i++) {
            Consult consultInfo = (Consult) consultList.get(i);
            ConsultVo consultVo = new ConsultVo();
            consultVo.setConsultId(consultInfo.getConsultId());
            consultVo.setGoodsId(consultInfo.getGoodsId());
            //商品名称
            for (int j = 0; j < listGoods.size(); j++) {
                if (((GoodsVo)listGoods.get(j)).getGoodsId() == consultInfo.getGoodsId()) {
                    consultVo.setGoodsName(((GoodsVo)listGoods.get(j)).getGoodsName());
                }
            }
            consultVo.setMemberId(consultInfo.getMemberId());
            //会员名称
            for (int j = 0; j < listMember.size(); j++) {
                if (listMember.get(j).getMemberId() == consultInfo.getMemberId()) {
                    consultVo.setMemberName(listMember.get(j).getMemberName());
                }
            }
            consultVo.setStoreId(consultInfo.getStoreId());
            //店铺名称
            for (int j = 0; j < listStore.size(); j++) {
                if (listStore.get(j).getStoreId() == consultInfo.getStoreId()) {
                    consultVo.setStoreName(listStore.get(j).getStoreName());
                }
            }
            consultVo.setClassId(consultInfo.getClassId());
            //分类名称
            for (int j = 0; j < listConsultClass.size(); j++) {
                if (listConsultClass.get(j).getClassId() == consultInfo.getClassId()) {
                    consultVo.setClassName(listConsultClass.get(j).getClassName());
                }
            }
            consultVo.setConsultContent(consultInfo.getConsultContent());
            consultVo.setAddTime(consultInfo.getAddTime());
            consultVo.setConsultReply(consultInfo.getConsultReply());
            consultVo.setReplyTime(consultInfo.getReplyTime());
            consultVo.setAnonymousState(consultInfo.getAnonymousState());
            consultVoList.add(consultVo);
        }
        dtGrid.setExhibitDatas(consultVoList);
        return dtGrid;
    }
    /**
     * 查询店铺待回复咨询
     * @param storeId
     * @return
     */
    public long getConsultNoReplyCountByStoreId(int storeId){
        HashMap<String, String> where = new HashMap<>();
        where.put("storeId", "storeId = :storeId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        return consultDao.findNoReplyCount(where, params);
    }
    /**
     * 查询会员未读回复数
     * @param memberId
     * @return
     */
    public long getConsultNoReadCountByMemberId(int memberId){
        HashMap<String, String> where = new HashMap<>();
        where.put("memberId", "memberId = :memberId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", memberId);
        return consultDao.findNoReadCount(where, params);
    }
    /**
     * 更新咨询为会员已读回复
     * @param memberId
     * @return
     */
    public boolean consultToReadByMemberId(int memberId){
        HashMap<String,String> where = new HashMap<>();
        where.put("memberId", "memberId = :memberId");
        HashMap<String,Object> params = new HashMap<>();
        params.put("memberId", memberId);
        return consultDao.updateToRead(where, params);
    }
}