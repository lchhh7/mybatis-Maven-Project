package com.jinjin.jintranet.common.vo;

import com.jinjin.jintranet.common.constant.Constants;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class DefaultVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String rnum;
    private int pageIndex = 1;
    private int pageSize = 10;
    private int firstIndex = 1;
    private int lastIndex = 10;
    private int firstPage;
    private int lastPage;
    private int recordCountPerPage = 10;
    private int totalCnt;
    private int totalPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;

    private String positionCd = Constants.CODE_POSITION;
    private String departmentCd = Constants.CODE_DEPARTMENT;
    private String statusCd = Constants.CODE_STATUS;
    private String projectActionCd = Constants.CODE_PROJECT_ACTION;
    private String projectSegmentCd = Constants.CODE_PROJECT_SEGMENT;
    private String projectTaskCd = Constants.CODE_PROJECT_TASK;
    private String projectScheduleCd = Constants.CODE_PROJECT_SCHEDULE;
    private String projectDocumentCd = Constants.CODE_PROJECT_DOCUMENT;

    public DefaultVO (){

    }
    public DefaultVO(int pageIndex, int totalCnt) {
        this.pageIndex = pageIndex;
        this.setTotalCnt(totalCnt);
    }

    private void calculate() {
		totalPage = (totalCnt -1) / 10 + 1;
		if (this.getPageIndex() > totalPage) {
            this.setPageIndex(totalPage);
        }

		firstPage = (this.getPageIndex() - 1) / 10 * 10 + 1;

		lastPage = firstPage + 9;
		if (lastPage > totalPage) {
            lastPage = totalPage;
        }

        firstIndex = this.pageIndex == 1 ? 1 : (this.pageIndex - 1) * 10 + 1;
        lastIndex = firstIndex + 9;

		hasPreviousPage  = firstPage == 1 ? false : true;
        if (!hasPreviousPage) {
            if (pageIndex != firstPage) {
                hasPreviousPage = true;
            }
        }

        hasNextPage = (lastPage == totalPage) ? false : true;
        if (!hasNextPage) {
            if (pageIndex != lastPage) {
                hasNextPage = true;
            }
        }
    }


    public String getRnum() {
        return rnum;
    }

    public void setRnum(String rnum) {
        this.rnum = rnum;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public int getRecordCountPerPage() {
        return recordCountPerPage;
    }

    public void setRecordCountPerPage(int recordCountPerPage) {
        this.recordCountPerPage = recordCountPerPage;
    }

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
        if (totalCnt == 0) {
            this.firstPage = 0;
            this.lastPage = 0;
        }

        if (totalCnt > 0) {
            calculate();
        }
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
