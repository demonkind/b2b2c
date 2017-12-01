package net.shopnc.common.entity.dtgrid;

import org.apache.commons.collections.MapUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by shopnc on 2015/11/4.
 */
public class QueryUtils {
    public static void parseDtGridHql(DtGrid dtGrid) throws Exception {
        //定义参数值列表
        List<Object> arguments = new ArrayList<Object>();
        //生成带占位符的hql
        String hql = getFastQuerySql(dtGrid.getFastQueryParameters(), arguments, dtGrid.getNcColumnsTypeList());
        if (hql.trim().startsWith("and")) {
            hql = " where "+hql.substring(5, hql.length());
        }
        dtGrid.setWhereHql(hql);
        //生成排序hql
        String sortHql = QueryUtils.getAdvanceQuerySortSql(dtGrid.getAdvanceQuerySorts());
        dtGrid.setSortHql(sortHql);
        dtGrid.setArguments(arguments);
    }
    /**
     * 获取快速查询的条件SQL
     *
     * @param params    快速查询参数
     * @param arguments 参数值列表
     * @return 条件SQL
     * @throws Exception
     */
    public static String getFastQuerySql(Map<String, Object> params, List<Object> arguments, HashMap<String, String> ncColumnsTypeList) throws Exception {
        //如果传递的条件参数为空则返回空字符串
        if (params == null || params.isEmpty()) {
            return "";
        }
        //定义条件SQL
        StringBuffer conditionSql = new StringBuffer();
        //遍历参数，拼接SQL
        for (String key : params.keySet()) {
            if ("".equals(MapUtils.getString(params, key, "").trim())) {
                continue;
            }

            if (key.indexOf("_") != -1 && !key.endsWith("_format")) {
                String field = key.substring(key.indexOf("_") + 1, key.length());
                //equal
                if (key.startsWith("eq_")) {
                    conditionSql.append(" and ").append(field).append(" = ?").append(arguments.size());
                    if (ncColumnsTypeList != null && ncColumnsTypeList.get(field) != null && !ncColumnsTypeList.get(field).isEmpty()) {
                        if (ncColumnsTypeList.get(field).equals("int")) {
                            arguments.add(Integer.parseInt(MapUtils.getString(params, key).toString()));
                        }
                        if (ncColumnsTypeList.get(field).equals("long")) {
                            arguments.add(Long.parseLong(MapUtils.getString(params, key).toString()));
                        }
                    } else {
                        arguments.add(MapUtils.getString(params, key));
                    }
                    continue;
                }
                //not equal
                if (key.startsWith("ne_")) {
                    conditionSql.append(" and ").append(field).append(" <> ?").append(arguments.size());
                    if (ncColumnsTypeList != null && ncColumnsTypeList.get(field) != null && !ncColumnsTypeList.get(field).isEmpty()) {
                        if (ncColumnsTypeList.get(field).equals("int")) {
                            arguments.add(Integer.parseInt(MapUtils.getString(params, key).toString()));
                        }
                    } else {
                        arguments.add(MapUtils.getString(params, key));
                    }
                    continue;
                }
                //like
                if (key.startsWith("lk_")) {
                    conditionSql.append(" and ").append(field).append(" like ?").append(arguments.size());
                    arguments.add("%" + MapUtils.getString(params, key) + "%");
                    continue;
                }
                //great then
                if (key.startsWith("gt_")) {
                    conditionSql.append(" and ").append(field).append(" > ?").append(arguments.size());
                    if (ncColumnsTypeList != null && ncColumnsTypeList.get(field) != null && !ncColumnsTypeList.get(field).isEmpty()) {
                        if (ncColumnsTypeList.get(field).equals("int")) {
                            arguments.add(Integer.parseInt(MapUtils.getString(params, key).toString()));
                        } else if (ncColumnsTypeList.get(field).equals("BigDecimal")) {
                            BigDecimal bigDecimal = new BigDecimal(MapUtils.getString(params, key).toString());
                            bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                            arguments.add(bigDecimal);
                        }
                    } else {
                        arguments.add(MapUtils.getString(params, key));
                    }
                    continue;
                }
                //great then and equal
                if (key.startsWith("ge_")) {
                    conditionSql.append(" and ").append(field).append(" >= ?").append(arguments.size());
                    if (ncColumnsTypeList != null && ncColumnsTypeList.get(field) != null && !ncColumnsTypeList.get(field).isEmpty()) {
                        if (ncColumnsTypeList.get(field).equals("int")) {
                            arguments.add(Integer.parseInt(MapUtils.getString(params, key).toString()));
                        } else if (ncColumnsTypeList.get(field).equals("BigDecimal")) {
                            BigDecimal bigDecimal = new BigDecimal(MapUtils.getString(params, key).toString());
                            bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                            arguments.add(bigDecimal);
                        } else if (ncColumnsTypeList.get(field).equals("Timestamp")) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = simpleDateFormat.parse(MapUtils.getString(params, key).toString());
                            arguments.add(date);

                        }
                    } else {
                        arguments.add(MapUtils.getString(params, key));
                    }
                    continue;
                }
                //less then
                if (key.startsWith("lt_")) {
                    conditionSql.append(" and ").append(field).append(" < ?").append(arguments.size());
                    if (ncColumnsTypeList != null && ncColumnsTypeList.get(field) != null && !ncColumnsTypeList.get(field).isEmpty()) {
                        if (ncColumnsTypeList.get(field).equals("int")) {
                            arguments.add(Integer.parseInt(MapUtils.getString(params, key).toString()));
                        } else if (ncColumnsTypeList.get(field).equals("BigDecimal")) {
                            BigDecimal bigDecimal = new BigDecimal(MapUtils.getString(params, key).toString());
                            bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                            arguments.add(bigDecimal);
                        }
                    } else {
                        arguments.add(MapUtils.getString(params, key));
                    }
                    continue;
                }
                //less then and equal
                if (key.startsWith("le_")) {
                    conditionSql.append(" and ").append(field).append(" <= ?").append(arguments.size());
                    if (ncColumnsTypeList != null && ncColumnsTypeList.get(field) != null && !ncColumnsTypeList.get(field).isEmpty()) {
                        if (ncColumnsTypeList.get(field).equals("int")) {
                            arguments.add(Integer.parseInt(MapUtils.getString(params, key).toString()));
                        } else if (ncColumnsTypeList.get(field).equals("BigDecimal")) {
                            BigDecimal bigDecimal = new BigDecimal(MapUtils.getString(params, key).toString());
                            bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                            arguments.add(bigDecimal);
                        } else if (ncColumnsTypeList.get(field).equals("Timestamp")) {
                            arguments.add(Timestamp.valueOf(MapUtils.getString(params, key).toString() + " 23:59:59"));
                        }
                    } else {
                        arguments.add(MapUtils.getString(params, key));
                    }
                    continue;
                }
            }
        }
        //返回条件SQL
        return conditionSql.toString();

    }

    /**
     * 获取高级查询的排序SQL
     *
     * @param advanceQuerySorts 排序列表
     * @return 条件SQL
     * @throws Exception
     */
    public static String getAdvanceQuerySortSql(List<Sort> advanceQuerySorts) {
        //定义条件SQL
        StringBuffer sortSql = new StringBuffer();
        if (advanceQuerySorts != null && advanceQuerySorts.size() > 0) {
            //加入前置的and参数
            sortSql.append(" order by ");
            for (Sort advanceQuerySort : advanceQuerySorts) {
                //获取参数：field-字段名 logic-排序逻辑
                String field = advanceQuerySort.getField();
                String logic = advanceQuerySort.getLogic();
                //拼接SQL
                getSingleAdvanceQuerySortSql(sortSql, field, logic);
            }
            sortSql.delete(sortSql.lastIndexOf(","), sortSql.length());
        }
        //返回条件SQL
        return sortSql.toString();
    }

    /**
     * 拼接单条的高级排序SQL
     *
     * @param sortSql 排序SQL
     * @param field   字段信息
     * @param logic   逻辑符号 0-asc 1-desc
     */
    private static void getSingleAdvanceQuerySortSql(StringBuffer sortSql, String field, String logic) {
        //获取左括号内容、右括号内容、逻辑符号内容
        logic = getSortLogicContent(logic);
        //根据条件类型拼接SQL
        sortSql.append(" ").append(field).append(" ").append(logic).append(",  ");
    }

    /**
     * 获取排序逻辑内容
     *
     * @param logic 逻辑码值
     * @return 逻辑内容
     */
    private static String getSortLogicContent(String logic) {
        String content = "";
        if ("0".equals(logic)) {
            content = "asc";
        } else if ("1".equals(logic)) {
            content = "desc";
        }
        return content;
    }
}
