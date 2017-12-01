package net.shopnc.b2b2c.dao.member;

import net.shopnc.b2b2c.dao.goods.CategoryDao;
import net.shopnc.b2b2c.domain.goods.Category;
import net.shopnc.b2b2c.domain.member.GoodsBrowse;
import net.shopnc.b2b2c.vo.goodsbrowse.GoodsBrowseVo;
import net.shopnc.common.dao.BaseDaoHibernate4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-01-27
 */
@Repository
@Transactional(rollbackFor = {Exception.class})
public class GoodsBrowseDao extends BaseDaoHibernate4<GoodsBrowse> {
    @Autowired
    private CategoryDao categoryDao;

    /**
     * 商品浏览总数
     * @param where 查询条件
     * @param params HQL参数值
     * @return 商品浏览总数
     */
    public Long findGoodsBrowseCount(HashMap<String,String> where, HashMap<String,Object> params)
    {
        String hql = "select count(*) from GoodsBrowse where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        return super.findCount(hql, params);
    }

    /**
     * 商品浏览记录详细列表分页
     * @param where 查询条件
     * @param params HQL参数值
     * @param pageNo 当前分页
     * @param pageSize 分页条数
     * @param sort 分页条件
     * @param group 分组条件
     * @return 浏览记录列表
     */
    public List<GoodsBrowseVo> getGoodsBrowseListByPage(HashMap<String,String> where, HashMap<String,Object> params,int pageNo, int pageSize, String sort, String group) {
        String hql = "select new net.shopnc.b2b2c.vo.goodsbrowse.GoodsBrowseVo(gBrowse, g, gc) from GoodsBrowse gBrowse, Goods g, GoodsCommon gc where gBrowse.goodsId=g.goodsId and g.commonId=gc.commonId ";
        //where
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        //group
        if (group!=null && group.length()>0) {
            hql += (" group by " + group);
        }else{
            hql += " group by g.goodsId";
        }
        //sort
        if (sort!=null && sort.length()>0) {
            hql += (" order by " + sort);
        } else {
            hql += " order by gBrowse.addTime desc";
        }
        List<Object> browseList = (List)super.findObjectByPage(hql, pageNo, pageSize, params);
        List<GoodsBrowseVo> browseListNew = new ArrayList<GoodsBrowseVo>();
        if (browseList!=null && browseList.size()>0) {
            for (int j = 0; j < browseList.size(); j++) {
                browseListNew.add((GoodsBrowseVo)browseList.get(j));
            }
        }
        return browseListNew;
    }

    /**
     * 商品浏览记录简单列表
     * @param where 查询条件
     * @param params HQL参数值
     * @param num 查询条数
     * @param sort 排序条件
     * @param group 分组条件
     * @return 商品浏览记录简单列表
     */
    public List<GoodsBrowseVo> getGoodsBrowseList(HashMap<String,String> where, HashMap<String,Object> params, int num, String sort, String group) {
        String hql = "select new net.shopnc.b2b2c.vo.goodsbrowse.GoodsBrowseVo(gBrowse, g, gc) from GoodsBrowse gBrowse, Goods g, GoodsCommon gc where gBrowse.goodsId=g.goodsId and g.commonId=gc.commonId ";
        //where
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        //group
        if (group!=null && group.length()>0) {
            hql += (" group by " + group);
        }else{
            hql += " group by g.goodsId";
        }
        //sort
        if (sort!=null && sort.length()>0) {
            hql += (" order by " + sort);
        } else {
            hql += " order by gBrowse.addTime desc";
        }
        List<Object> browseList = (List)super.findObjectByPage(hql, 1, num, params);

        List<GoodsBrowseVo> browseListNew = new ArrayList<GoodsBrowseVo>();
        if (browseList!=null && browseList.size()>0) {
            for (int j = 0; j < browseList.size(); j++) {
                browseListNew.add((GoodsBrowseVo)browseList.get(j));
            }
        }
        return browseListNew;
    }

    /**
     * 商品浏览记录按照商品分类分组的列表
     * @param where 查询条件
     * @param params HQL参数值
     * @return 浏览的商品分类列表
     */
    public HashMap<String,Object> getGoodsBrowseCategoryList(HashMap<String,String> where, HashMap<String,Object> params){
        HashMap<String, Object> goodsCategoryTree = new HashMap<>();

        String hql = "from GoodsBrowse where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        hql += " group by goodsCategoryId order by goodsCategoryId asc";
        List<Object> browseCategoryList = super.findObject(hql, params);
        if (browseCategoryList==null || browseCategoryList.size()<=0) {
            return goodsCategoryTree;
        }

        //处理分类
        for (int i = 0; i < browseCategoryList.size(); i++) {
            GoodsBrowse item = (GoodsBrowse)browseCategoryList.get(i);
            Integer categoryId1 = item.getGoodsCategoryId1();
            Integer categoryId2 = item.getGoodsCategoryId2();
            if (categoryId1==null || categoryId1<=0) {
                break;
            }
            //遍历获得一级分类信息
            if (goodsCategoryTree==null || goodsCategoryTree.size()<=0 || goodsCategoryTree.get(categoryId1.toString())==null) {
                //查询一级分类
                Category categoryTmp = categoryDao.get(Category.class, categoryId1);
                HashMap<String, Object> treeItem1 = new HashMap<>();
                treeItem1.put("categoryId", categoryTmp.getCategoryId());
                treeItem1.put("categoryName", categoryTmp.getCategoryName());
                goodsCategoryTree.put(categoryId1.toString(), treeItem1);
            }
            //遍历获得二级分类信息
            if (categoryId2!=null && categoryId2>0) {
                Category categoryTmp = categoryDao.get(Category.class, categoryId2);
                HashMap<String, Object> treeItem1 = (HashMap<String, Object>)goodsCategoryTree.get(categoryId1.toString());
                HashMap<String, Object> treeItem1SonClass = new HashMap<>();
                if (treeItem1.get("sonClass")!=null) {
                    treeItem1SonClass = (HashMap<String,Object>)treeItem1.get("sonClass");
                }
                HashMap<String, Object> treeItem2 = new HashMap<>();
                treeItem2.put("categoryId", categoryTmp.getCategoryId());
                treeItem2.put("categoryName", categoryTmp.getCategoryName());
                treeItem1SonClass.put(categoryId2.toString(), treeItem2);
                treeItem1.put("sonClass", treeItem1SonClass);
                goodsCategoryTree.put(categoryId1.toString(), treeItem1);
            }
        }
        return goodsCategoryTree;
    }

    /**
     * 删除浏览记录
     * @param where 查询条件
     * @param params HQL参数值
     */
    public void deleteGoodsBrowse(HashMap<String,String> where, HashMap<String,Object> params)
    {
        String hql = "delete GoodsBrowse where 1=1 ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        super.delete(hql, params);
    }

    /**
     * 获得浏览商品基础信息列表
     * @param where 查询条件
     * @param params HQL参数值
     * @return 商品信息列表
     */
    public List<Object> getGoodsAndCommonList(HashMap<String,String> where, HashMap<String,Object> params) {
        String hql = "select new net.shopnc.b2b2c.vo.goodsbrowse.GoodsBrowseVo(g, gc) from Goods g, GoodsCommon gc where g.commonId=gc.commonId ";
        for (String key : where.keySet()) {
            hql += (" and " + where.get(key));
        }
        return super.findObject(hql, params);
    }
}