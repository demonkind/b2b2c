package net.shopnc.b2b2c.dao.member;

import java.util.HashMap;
import java.util.List;

import net.shopnc.b2b2c.domain.EvaluateGoods;
import net.shopnc.b2b2c.vo.member.EvaluateCommonVo;
import net.shopnc.common.dao.BaseDaoHibernate4;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.entity.dtgrid.QueryUtils;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;


@Repository
@Transactional(rollbackFor = {Exception.class})
public class EvaluateGoodsDao extends BaseDaoHibernate4<EvaluateGoods> {

	@Deprecated
	public EvaluateCommonVo getFirstEvaluate(int orderId,int goodsId,int memberId) {
		String hql = "select new net.shopnc.b2b2c.vo.member.EvaluateCommonVo(a,b) from EvaluateGoods a, EvaluateGoodsContent b where a.evaluateId=b.evaluateId and a.orderId=:orderId and a.goodsId=:goodsId and a.fromMemberId=:memberId order by b.addTime";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        map.put("goodsId", goodsId);
        map.put("memberId", memberId);
        List<Object> list=(List)super.find(hql, map);
        return (EvaluateCommonVo)list.get(0);
    }
	
	/**
	 * 查询首次评价信息
	 */
	public EvaluateCommonVo getFirstEvaluate(int orderGoodsId,int memberId){
		String hql = "select new net.shopnc.b2b2c.vo.member.EvaluateCommonVo(a,b) from EvaluateGoods a, EvaluateGoodsContent b where a.evaluateId=b.evaluateId and a.orderGoodsId=:orderGoodsId and a.fromMemberId=:memberId order by b.addTime";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("orderGoodsId", orderGoodsId);
        map.put("memberId", memberId);
        List<Object> list=(List)super.find(hql, map);
        return (EvaluateCommonVo)list.get(0);
	}
	
	/**
	 * 买家评价列表
	 */
	public List<EvaluateGoods> getPageEvaluate(int pageNo,int pageSize,int memberId){
		String hql="from EvaluateGoods a where a.fromMemberId=:memberId order by a.evaluateTime desc";
		HashMap<String,Object> params=new HashMap<String, Object>();
		params.put("memberId", memberId);
		List<EvaluateGoods> evaluateGoodsList=super.findByPage(hql, pageNo, pageSize, params);
		return evaluateGoodsList;
	}
	
	/**
	 * 买家评价列表数据总条数
	 */
	public long getPageEvaluateCount(int memberId){
		String hql="select count(*) from EvaluateGoods a where a.fromMemberId=:memberId order by a.evaluateTime desc";
		HashMap<String,Object> params=new HashMap<String, Object>();
		params.put("memberId", memberId);
		long count=super.findCount(hql, params);
		return count;
	}
	
	
	/**
	 * 评价商品列表
	 */
	public List<EvaluateGoods> getPageGoodsEvaluate(int pageNo,int pageSize,List<String> condition,HashMap<String,Object> params,String orderBy){
		StringBuffer hql=new StringBuffer("from EvaluateGoods where 1=1 ");
		for (String con : condition) {
            hql.append(" and " + con);
        }
		if(orderBy!=null && !orderBy.isEmpty()){
			hql.append(" order by ").append(orderBy+" ");
		}
		List<EvaluateGoods> evaluateGoodsList=super.findByPage(hql.toString(), pageNo, pageSize, params);
		return evaluateGoodsList;
	}
	
	/**
	 * 评价商品列表数据总条数
	 */
	public long getPageGoodsEvaluateCount(List<String> condition,HashMap<String,Object> params){
		StringBuffer hql=new StringBuffer("select count(*) from EvaluateGoods where 1=1 ");
		for (String con : condition) {
            hql.append(" and " + con);
        }
		long count=super.findCount(hql.toString(), params);
		return count;
	}
	
	/**
	 * 查询商品评分平均值
	 */
	public double getAvgGoodsEval(int goodsId){
		double num=0;
		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("goodsId", goodsId);
		String hql="select avg(scores) from EvaluateGoods where goodsId=:goodsId";
		List<Object> list=super.findObject(hql, params);
		if(list!=null && list.size()>0){
			Object obj=list.get(0);
			if(obj!=null){
				num=(Double)obj;
			}
		}
		return num;
	}
	
	/**
	 * 商家评价列表
	 */
	public List<EvaluateCommonVo> getPageSellerEvaluate(int pageNo,int pageSize,List<String> condition,HashMap<String,Object> params,String orderBy){
		StringBuffer hql=new StringBuffer("select new net.shopnc.b2b2c.vo.member.EvaluateCommonVo(eg,og,m) from EvaluateGoods eg,OrdersGoods og,Member m ");
		hql.append(" where eg.fromMemberId=m.memberId and eg.orderGoodsId=og.ordersGoodsId ");
		for (String con : condition) {
            hql.append(" and " + con);
        }
		if(orderBy!=null && !orderBy.isEmpty()){
			hql.append(" order by ").append(orderBy+" ");
		}
		List<EvaluateCommonVo> evaluateCommonVoList=(List)super.findObjectByPage(hql.toString(), pageNo, pageSize, params);
		return evaluateCommonVoList;
	}
	
	/**
	 * 商家评价列表数据总条数
	 */
	public long getPageSellerEvaluateCount(List<String> condition,HashMap<String,Object> params){
		StringBuffer hql=new StringBuffer("select count(*) from EvaluateGoods eg,OrdersGoods og,Member m ");
		hql.append(" where eg.fromMemberId=m.memberId and eg.orderGoodsId=og.ordersGoodsId ");
		for (String con : condition) {
            hql.append(" and " + con);
        }
		long count=super.findCount(hql.toString(), params);
		return count;
	}
	
	/**
	 * 删除评论子表
	 * @param evaluateId
	 */
	public void deleteEvaluateGoodsContent(int evaluateId){
		HashMap<String,Object> params=new HashMap<String, Object>();
		params.put("evaluateId", evaluateId);
		String hql="delete from EvaluateGoodsContent where evaluateId=:evaluateId";
		super.delete(hql, params);
	}
	
	/**
	 * 删除评论主表
	 * @param evaluateId
	 */
	public void deleteEvaluateGoods(int evaluateId){
		HashMap<String,Object> params=new HashMap<String, Object>();
		params.put("evaluateId", evaluateId);
		String hql="delete from EvaluateGoods where evaluateId=:evaluateId";
		super.delete(hql, params);
	}
	
	
	
	/**
	 * 后台评价列表
	 */
	public DtGrid getDtGridEvaluate(String dtGridPager) throws Exception{
		StringBuffer hql=new StringBuffer("select new net.shopnc.b2b2c.vo.member.EvaluateCommonVo(eg,og,m) from EvaluateGoods eg,OrdersGoods og,Member m ");
		hql.append(" where 1=1 and eg.fromMemberId=m.memberId and eg.orderGoodsId=og.ordersGoodsId ");
		StringBuffer countHql=new StringBuffer("select count(*) from EvaluateGoods eg,OrdersGoods og,Member m ");
		countHql.append(" where 1=1 and eg.fromMemberId=m.memberId and eg.orderGoodsId=og.ordersGoodsId ");
		DtGrid dtGrid=this.getDtGridList(dtGridPager, hql.toString(), countHql.toString());
		return dtGrid;
	}
	
	/**
	 * 后台店铺评价列表
	 */
	public DtGrid getDtGridEvaluateStore(String dtGridPager) throws Exception{
		StringBuffer hql=new StringBuffer("select new net.shopnc.b2b2c.vo.member.EvaluateCommonVo(es,m) from EvaluateStore es,Member m");
		hql.append(" where 1=1 and es.fromMemberId=m.memberId ");
		StringBuffer countHql=new StringBuffer("select count(*) from EvaluateStore es,Member m");
		countHql.append(" where 1=1 and es.fromMemberId=m.memberId ");
		DtGrid dtGrid=this.getDtGridList(dtGridPager, hql.toString(), countHql.toString());
		return dtGrid;
	}
	
	/**
	 * 后台DtGrid查询
	 */
	public DtGrid getDtGridList(String dtGridPager, String hql,String countHql) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        DtGrid dtGrid = mapper.readValue(dtGridPager, DtGrid.class);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (dtGrid.getNcColumnsType() != null && dtGrid.getNcColumnsType().size() > 0) {
            for (String key : dtGrid.getNcColumnsType().keySet()) {
                for (int i = 0; i< dtGrid.getNcColumnsType().get(key).size(); i++) {
                    hashMap.put((String) dtGrid.getNcColumnsType().get(key).get(i), key);
                }
                dtGrid.setNcColumnsTypeList(hashMap);
            }
        }
        QueryUtils.parseDtGridHql(dtGrid);
        //定义参数值列表
        List<Object> arguments = dtGrid.getArguments();
        //生成带占位符的hql
        String whereHql = dtGrid.getWhereHql();
        if(whereHql!=null && !whereHql.isEmpty()){
        	whereHql=" and "+whereHql.replace("where", " ");
        }
        //生成排序hql
        String sortHql = dtGrid.getSortHql();

        long recordCount = findCount(countHql + whereHql, arguments);
        dtGrid.setRecordCount(recordCount);
        int pageCount = (int)recordCount/dtGrid.getPageSize()+(recordCount%dtGrid.getPageSize() > 0 ?1:0);
        dtGrid.setPageCount(pageCount);
        List<Object> list = (List) findByPage(hql + whereHql + sortHql, dtGrid.getNowPage(), dtGrid.getPageSize(), arguments);
        dtGrid.setExhibitDatas(list);
        return dtGrid;
    }
}
