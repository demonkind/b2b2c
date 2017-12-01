package net.shopnc.common.util;

import java.math.BigDecimal;

/**
 * 货币类型常规计算<br/>
 * Created by hbj on 2015/12/1.
 */
public class PriceHelper {
    /**
     * 格式化货币类型，默认保留两位小数
     * @param bigDecimal
     * @return
     */
    public static BigDecimal format(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            bigDecimal = new BigDecimal(0);
        }
        return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 格式化货币类型，自行设置保留位数
     * @param bigDecimal
     * @param scale
     * @return
     */
    public static BigDecimal format(BigDecimal bigDecimal, int scale) {
        if (bigDecimal == null) {
            bigDecimal = new BigDecimal(0);
        }
        return bigDecimal.setScale(scale, BigDecimal.ROUND_DOWN);
    }

    /**
     * 乘法，结果保留两位小数
     * @param bigDecimal
     * @param number
     * @return
     */
    public static BigDecimal mul(BigDecimal bigDecimal, int number) {
        if (bigDecimal == null) {
            bigDecimal = format(new BigDecimal(0));
        }
        BigDecimal bigDecimalNumber = new BigDecimal(number).setScale(0,BigDecimal.ROUND_DOWN);
        return format(bigDecimal.multiply(bigDecimalNumber));
    }

    /**
     * 加法，结果保留两位小数
     * @param bigDecimal1
     * @param bigDecimal2
     * @return
     */
    public static BigDecimal add(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        if (bigDecimal1 == null) {
            bigDecimal1 = format(new BigDecimal(0));
        }
        if (bigDecimal2 == null) {
            bigDecimal2 = format(new BigDecimal(0));
        }
        return format(bigDecimal1.add(bigDecimal2));
    }

    /**
     * 减法，结果保留两位小数
     * @param bigDecimal1
     * @param bigDecimal2
     * @return
     */
    public static BigDecimal sub(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        if (bigDecimal1 == null) {
            bigDecimal1 = format(new BigDecimal(0));
        }
        if (bigDecimal2 == null) {
            bigDecimal2 = format(new BigDecimal(0));
        }
        return format(bigDecimal1.subtract(bigDecimal2));
    }

    /**
     * 除法，结果保留两位小数
     * @param bigDecimal
     * @param number
     * @return
     */
    public static BigDecimal div(BigDecimal bigDecimal, int number) {
        if (bigDecimal == null) {
            bigDecimal = format(new BigDecimal(0));
        }
        BigDecimal bigDecimalNumber = new BigDecimal(number).setScale(0,BigDecimal.ROUND_DOWN);
        return bigDecimal.divide(bigDecimalNumber, 2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 等于
     * @param leftNum 比数
     * @param rightNum 被比标量
     * @return
     */
    public static Boolean isEquals(BigDecimal leftNum, BigDecimal rightNum) {
        if (leftNum == null) {
            leftNum = BigDecimal.ZERO;
        }
        if (rightNum == null) {
            rightNum = BigDecimal.ZERO;
        }
        leftNum = format(leftNum);
        rightNum = format(rightNum);
        if(leftNum.equals(rightNum)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 小于
     * @param leftNum 比数
     * @param rightNum 被比标量
     * @return
     */
    public static Boolean isLessThan(BigDecimal leftNum, BigDecimal rightNum) {
        if (leftNum == null) {
            leftNum = BigDecimal.ZERO;
        }
        if (rightNum == null) {
            rightNum = BigDecimal.ZERO;
        }
        leftNum = format(leftNum);
        rightNum = format(rightNum);
        int r= leftNum.compareTo(rightNum);
        if(r == -1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 小于等于
     * @param leftNum 比数
     * @param rightNum 被比标量
     * @return
     */
    public static Boolean isLessThanOrEquals(BigDecimal leftNum, BigDecimal rightNum) {
        if (leftNum == null) {
            leftNum = BigDecimal.ZERO;
        }
        if (rightNum == null) {
            rightNum = BigDecimal.ZERO;
        }
        leftNum = format(leftNum);
        rightNum = format(rightNum);
        int r= leftNum.compareTo(rightNum);
        if(r == -1 || r == 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 大于
     * @param leftNum 比数
     * @param rightNum 被比标量
     * @return
     */
    public static Boolean isGreaterThan(BigDecimal leftNum, BigDecimal rightNum) {
        if (leftNum == null) {
            leftNum = BigDecimal.ZERO;
        }
        if (rightNum == null) {
            rightNum = BigDecimal.ZERO;
        }
        leftNum = format(leftNum);
        rightNum = format(rightNum);
        int r= leftNum.compareTo(rightNum);
        if(r == 1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 大于等于
     * @param leftNum 比数
     * @param rightNum 被比标量
     * @return
     */
    public static Boolean isGreaterThanOrEquals(BigDecimal leftNum, BigDecimal rightNum) {
        if (leftNum == null) {
            leftNum = BigDecimal.ZERO;
        }
        if (rightNum == null) {
            rightNum = BigDecimal.ZERO;
        }
        leftNum = format(leftNum);
        rightNum = format(rightNum);
        int r= leftNum.compareTo(rightNum);
        if(r == 1 || r == 0){
            return true;
        }else{
            return false;
        }
    }

}
