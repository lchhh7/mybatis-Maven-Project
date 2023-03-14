package com.jinjin.jintranet.common.util;

import com.jinjin.jintranet.common.vo.DefaultVO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class PageUtils {
    public static String page(DefaultVO defaultVO, String methodName, HttpServletRequest request) {
        String contextPath = request.getContextPath();
        StringBuffer sb = new StringBuffer(200);

        int page = defaultVO.getPageIndex();
        int firstPage = defaultVO.getFirstPage();
        int lastPage = defaultVO.getLastPage();
        int totalPage = defaultVO.getTotalPage();

        int prevPage = firstPage == 1 ? 1 : (firstPage - 1);
        int nextPage = lastPage == totalPage ? lastPage : (lastPage + 1);

        if (defaultVO.getTotalCnt() == 0) return "";

        if (defaultVO.isHasPreviousPage()) {
            sb.append("<a role='button' class='pfirst' onclick='" + methodName +"(\"1\")'><img src='" + contextPath +"/common/img/pfirst.png' alt='첫페이지'></a>");
            sb.append("<a role='button' class='pprev' onclick='" + methodName +"(\"" + prevPage +"\")'><img src='" + contextPath +"/common/img/pprev.png' alt='앞페이지'></a>");
        }

        for (int i = firstPage; i <= lastPage; i++) {
            sb.append("<a role='button' class='pnum");
            if (page == i) {
                sb.append(" active");
            }
            sb.append("' onclick='" + methodName +"(\"" + i + "\")'>").append(i).append("</a>");
        }

        if (defaultVO.isHasNextPage()) {
            sb.append("<a role='button' class='pnext' onclick='" + methodName +"(\"" + nextPage +"\")'><img src='" + contextPath +"/common/img/pnext.png' alt='뒤페이지'></a>");
            sb.append("<a role='button' class='plast' onclick='" + methodName +"(\"" + totalPage +"\")'><img src='" + contextPath +"/common/img/plast.png' alt='마지막페이지'></a>");
        }

        return sb.toString();
    }
}
