package net.shopnc.common.entity;

import net.shopnc.b2b2c.constant.Common;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by hbj on 2015/12/9.
 */
public class PageEntity {
    /**
     * 总共的数据量
     */
    private long total;
    /**
     * 每页显示多少条
     */
    private int pageSize = Common.PAGESIZE;
    /**
     * 共有多少页
     */
    private int totalPage;
    /**
     * 当前是第几页
     */
    private int pageNo;
    /**
     * 连接路径
     */
    private String path = "";
    /**
     * 页码HTML信息
     */
    private String pageHtml;
    /**
     * 开始页面
     */
    private int startPage;
    /**
     * 结束页面
     */
    private int endPage;
    /**
     * 显示的页数
     */
    private int displayNum = 5;

    public PageEntity(){
        this.setPageUrl();
    }

    public int getStartPage()
    {
        return startPage;
    }

    public int getEndPage()
    {
        return endPage;
    }

    public String getPath()
    {
        return path;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public void setPageNo(int pageNo)
    {
        this.pageNo = pageNo <= 0 ? 1 : pageNo;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public int getTotalPage()
    {
        return ((int)this.total + this.pageSize - 1) / this.pageSize;
    }

    public int getPageNo()
    {
        return pageNo;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public String getPageHtml()
    {
        totalPage = getTotalPage();
        StringBuffer displayInfo = new StringBuffer();
        displayInfo.append("<ul>");
        if (totalPage != 0 && pageSize != 0)
        {
            if (pageNo <= 1)
            {
                displayInfo.append("<li><span>首页</span></li>");
                displayInfo.append("<li><span>上一页</span></li>");
            }
            else
            {
                displayInfo.append("<li><a class='demo' href='" + path + "1'><span>首页</span></a></li>");
                displayInfo.append("<li><a class='demo' href='" + path + (pageNo - 1) + "'>上一页</span></a></li>");
            }

            countPages();
            for (int i = startPage; i <= endPage; i++)
            {
                if (i == pageNo)
                {
                    displayInfo.append("<li><span class='currentpage'>" + i + "</span></li>");
                }
                else
                {
                    displayInfo.append("<li><a class='demo' href='" + path + i + "'><span>" + i + "</span></a></li>");
                }
            }

            if (pageNo >= totalPage)
            {
                displayInfo.append("<li><span>下一页</span></li>");
                displayInfo.append("<li><span>尾页</span></li>");
            }
            else
            {
                displayInfo.append("<li><a class='demo' href='" + path + (pageNo + 1) + "'><span>下一页</span></a></li>");
                displayInfo.append("<li><a class='demo' href='" + path + totalPage + "'><span>尾页</span></a></li>");
            }
        } else {
            displayInfo.append("<li><span>首页</span></li>");
            displayInfo.append("<li><span>上一页</span></li>");
            displayInfo.append("<li><span>下一页</span></li>");
            displayInfo.append("<li><span>尾页</span></li>");
        }
        displayInfo.append("</ul>");
        return displayInfo.toString();
    }

    public void countPages()
    {

        if (pageNo - displayNum / 2 < 1)
        {
            startPage = 1;
            endPage = displayNum > totalPage ? totalPage : displayNum;
        }
        else if (pageNo + displayNum / 2 > totalPage)
        {
            int n = totalPage - displayNum + 1;
            startPage = n > 0 ? n : 1;
            endPage = totalPage;
        }
        else
        {
            startPage = pageNo - displayNum / 2;
            endPage = startPage + displayNum - 1;
        }
    }

    /**
     * 取url地址
     *
     * @param
     * @return string 字符串类型的返回结果
     */
    private void setPageUrl(){
        HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getRequestURL().toString() + "?";
        String paramStr = "";
        Enumeration parameterNames=request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String paraName=(String)parameterNames.nextElement();
            if (paraName.equals("page")) {
                continue;
            }
            paramStr += ("&" + paraName + "=" + request.getParameter(paraName));
        }
        paramStr += ("&page=");
        paramStr = paramStr.substring(1, paramStr.length());
        url += paramStr;
        this.setPath(url);
    }
}
