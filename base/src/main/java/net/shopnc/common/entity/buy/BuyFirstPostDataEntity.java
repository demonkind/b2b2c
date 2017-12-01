package net.shopnc.common.entity.buy;

import net.shopnc.b2b2c.constant.State;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 接收购买第一步提交的数据
 * Created by hbj on 2015/12/15.
 */
public class BuyFirstPostDataEntity {
    /**
     * 接收提交的购物车或商品Id及数量，示例buyItemList=100|2
     */
    private List<String> buyItemList;
    /**
     * 是否是购物车购买
     */
    private int isCart;
    /**
     * 处理后的购物车Id和购买数量HashMap<cartId,buyNum>
     */
    private HashMap<Integer,Integer> buyCartIdAndBuyNumList = new HashMap<Integer, Integer>();
    /**
     * 处理后的商品Id和购买数量HashMap<goodsId,buyNum>
     */
    private HashMap<Integer,Integer> buyGoodsIdAndBuyNumList = new HashMap<Integer, Integer>();
    /**
     * 处理后的购物车Id list
     */
    private List<Integer> cartIdList = new ArrayList<Integer>();
    /**
     * 处理后的商品Id list
     */
    private List<Integer> goodsIdList = new ArrayList<Integer>();
    /**
     * 会员Id
     */
    private int memberId;

    protected final Logger logger = Logger.getLogger(getClass());

    /**
     * ************************************************************************
     * 构造函数 块
     * ************************************************************************
     */
    public BuyFirstPostDataEntity(List<String> buyItemList, Integer isCart, int memberId){
        this.buyItemList = buyItemList;
        this.isCart = isCart != null && isCart.equals(State.YES) ? State.YES : State.NO;
        this.memberId = memberId;
        if (this.isCart == State.YES) {
            this.buyCartIdAndBuyNumList = this.ncParseBuyItemList(buyItemList);
            this.cartIdList = this.ncGetItemIdList(buyCartIdAndBuyNumList);
        } else {
            this.buyGoodsIdAndBuyNumList = this.ncParseBuyItemList(buyItemList);
            this.goodsIdList = this.ncGetItemIdList(buyGoodsIdAndBuyNumList);
        }
    }

    /**
     * ************************************************************************
     * Geter Seter toString 块
     * ************************************************************************
     */

    public List<String> getBuyItemList() {
        return buyItemList;
    }

    public void setBuyItemList(List<String> buyItemList) {
        this.buyItemList = buyItemList;
    }

    public int getIsCart() {
        return isCart;
    }

    public void setIsCart(int isCart) {
        this.isCart = isCart;
    }

    public HashMap<Integer, Integer> getBuyCartIdAndBuyNumList() {
        return buyCartIdAndBuyNumList;
    }

    public void setBuyCartIdAndBuyNumList(HashMap<Integer, Integer> buyCartIdAndBuyNumList) {
        this.buyCartIdAndBuyNumList = buyCartIdAndBuyNumList;
    }

    public HashMap<Integer, Integer> getBuyGoodsIdAndBuyNumList() {
        return buyGoodsIdAndBuyNumList;
    }

    public void setBuyGoodsIdAndBuyNumList(HashMap<Integer, Integer> buyGoodsIdAndBuyNumList) {
        this.buyGoodsIdAndBuyNumList = buyGoodsIdAndBuyNumList;
    }

    public List<Integer> getCartIdList() {
        return cartIdList;
    }

    public void setCartIdList(List<Integer> cartIdList) {
        this.cartIdList = cartIdList;
    }

    public List<Integer> getGoodsIdList() {
        return goodsIdList;
    }

    public void setGoodsIdList(List<Integer> goodsIdList) {
        this.goodsIdList = goodsIdList;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "BuyFirstPostDataEntity{" +
                "buyItemList=" + buyItemList +
                ", isCart=" + isCart +
                ", buyCartIdAndBuyNumList=" + buyCartIdAndBuyNumList +
                ", buyGoodsIdAndBuyNumList=" + buyGoodsIdAndBuyNumList +
                ", cartIdList=" + cartIdList +
                ", goodsIdList=" + goodsIdList +
                ", memberId=" + memberId +
                '}';
    }
    /**
     * ************************************************************************
     * 自定义类内使用函数 块
     * ************************************************************************
     */

    /**
     * 解析处理购物车Id/商品Id及数量 类内公用
     * @param buyItemList
     * @return
     */
    private HashMap<Integer,Integer> ncParseBuyItemList(List<String> buyItemList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        HashMap<Integer,Integer> hashMap = new HashMap<Integer, Integer>();
        String regEx = "^(\\d{1,10})\\|(\\d{1,6})$";
        Pattern pattern = Pattern.compile(regEx);
        for(int i=0; i<buyItemList.size(); i++) {
            Matcher matcher = pattern.matcher(buyItemList.get(i));
            if (matcher.matches() && matcher.groupCount() == 2) {
                hashMap.put(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) == 0 ? 1 : Integer.parseInt(matcher.group(2)));
            }
        }
        return hashMap;
    }

    /**
     * 得到物车Id/商品Id list 类内公用
     * @param buyItemAndNumList
     * @return
     */
    private List<Integer> ncGetItemIdList(HashMap<Integer,Integer> buyItemAndNumList) {
        logger.info(getClass().getSimpleName() + "." + Thread.currentThread() .getStackTrace()[1].getMethodName());
        List<Integer> cartIdList = new ArrayList<Integer>();
        for(int key : buyItemAndNumList.keySet()) {
            cartIdList.add(key);
        }
        return cartIdList;
    }

}
