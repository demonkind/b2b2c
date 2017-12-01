package net.shopnc.common.entity.dtgrid;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shopnc on 2015/11/3.
 */
public class DtGrid {
    /**
     * 是否导出
     */
    private boolean isExport;
    /**
     * 是否出错
     */
    private boolean isSuccess = true;
    /**
     * 每页显示条数
     */
    private Integer pageSize;
    /**
     * 开始记录数
     */
    private Integer startRecord;
    /**
     * 当前页数
     */
    private Integer nowPage;
    /**
     * 记录总数
     */
    private long recordCount;
    /**
     * 总页数
     */
    private Integer pageCount;
    /**
     * 参数列表
     */
    private Map<String, Object> parameters;
    /**
     * 快速查询参数列表
     */
    private Map<String, Object> fastQueryParameters;

    private List<String> advanceQueryConditions;
    /**
     * 高级排序列表
     */
    private List<Sort> advanceQuerySorts;
    /**
     * 带占位符的HQL
     */
    @JsonIgnore
    private String whereHql;
    /**
     * order语句
     */
    @JsonIgnore
    private String sortHql;
    /**
     * 经过整理的where条件
     */
    @JsonIgnore
    private List<Object> arguments;
    /**
     * 字段类型标识
     */
    private HashMap<String,ArrayList> ncColumnsType;
    /**
     * 解析后的字段类型标识
     */
    private HashMap<String,String> ncColumnsTypeList;
    /**
     * 显示数据集
     */
    private List<Object> exhibitDatas;

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean getIsExport() {
        return isExport;
    }

    public void setIsExport(boolean isExport) {
        this.isExport = isExport;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartRecord() {
        return startRecord;
    }

    public void setStartRecord(Integer startRecord) {
        this.startRecord = startRecord;
    }

    public Integer getNowPage() {
        return nowPage;
    }

    public void setNowPage(Integer nowPage) {
        this.nowPage = nowPage;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Object> getFastQueryParameters() {
        return fastQueryParameters;
    }

    public void setFastQueryParameters(Map<String, Object> fastQueryParameters) {
        this.fastQueryParameters = fastQueryParameters;
    }

    public List<String> getAdvanceQueryConditions() {
        return advanceQueryConditions;
    }

    public void setAdvanceQueryConditions(List<String> advanceQueryConditions) {
        this.advanceQueryConditions = advanceQueryConditions;
    }

    public List<Object> getExhibitDatas() {
        return exhibitDatas;
    }

    public void setExhibitDatas(List<Object> exhibitDatas) {
        this.exhibitDatas = exhibitDatas;
    }

    public List<Sort> getAdvanceQuerySorts() {
        return advanceQuerySorts;
    }

    public void setAdvanceQuerySorts(List<Sort> advanceQuerySorts) {
        this.advanceQuerySorts = advanceQuerySorts;
    }

    public String getWhereHql() {
        return whereHql;
    }

    public void setWhereHql(String whereHql) {
        this.whereHql = whereHql;
    }

    public String getSortHql() {
        return sortHql;
    }

    public void setSortHql(String orderHql) {
        this.sortHql = orderHql;
    }

    public List<Object> getArguments() {
        return arguments;
    }

    public void setArguments(List<Object> arguments) {
        this.arguments = arguments;
    }

    public HashMap<String, ArrayList> getNcColumnsType() {
        return ncColumnsType;
    }

    public void setNcColumnsType(HashMap<String, ArrayList> ncColumnsType) {
        this.ncColumnsType = ncColumnsType;
    }

    public HashMap<String, String> getNcColumnsTypeList() {
        return ncColumnsTypeList;
    }

    public void setNcColumnsTypeList(HashMap<String, String> ncColumnsTypeList) {
        this.ncColumnsTypeList = ncColumnsTypeList;
    }

    @Override
    public String toString() {
        return "DtGrid{" +
                "isExport=" + isExport +
                ", pageSize=" + pageSize +
                ", startRecord=" + startRecord +
                ", nowPage=" + nowPage +
                ", recordCount=" + recordCount +
                ", pageCount=" + pageCount +
                ", parameters=" + parameters +
                ", fastQueryParameters=" + fastQueryParameters +
                ", advanceQueryConditions=" + advanceQueryConditions +
                ", advanceQuerySorts=" + advanceQuerySorts +
                ", exhibitDatas=" + exhibitDatas +
                '}';
    }


}
