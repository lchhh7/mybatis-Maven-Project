package com.jinjin.jintranet.common.vo;

import com.jinjin.jintranet.common.constant.Constants;
import org.joda.time.LocalDateTime;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class ProjectDefaultVO extends DefaultVO {
    private final String tableName = "PROJECT";
    private final String companyTableName = "PROJECT_COMPANY";
    private final String memberTableName = "PROJECT_MEMBER";
    private final String segmentTableName = "PROJECT_SEGMENT";
    private final String scheduleTableName = "PROJECT_SCHEDULE";
    private final String documentTableName = "PROJECT_DOCUMENT";
    private final String documentHistoryTableName = "PROJECT_DOCUMENT_HISTORY";

    private String projectActionCd = Constants.CODE_PROJECT_ACTION;
    private String projectSegmentCd = Constants.CODE_PROJECT_SEGMENT;
    private String projectTaskCd = Constants.CODE_PROJECT_TASK;
    private String projectScheduleCd = Constants.CODE_PROJECT_SCHEDULE;
    private String projectDocumentCd = Constants.CODE_PROJECT_DOCUMENT;


    private String searchYear;
    private String searchStartDt;
    private String searchEndDt;
    private String searchTitle;
    private String searchOrderingName;
    private String searchDepartment;

    private Integer id;
    private String title;
    private String orderingName;
    private String startDt;
    private String endDt;
    private String department;
    private String departmentName;
    private String implementDt;

    //
    private Integer companyId;
    private String companyName;
    private String companyKind;
    //

    private Integer crtId;
    private Integer udtId;
    private Integer delId;

    private String crtDt;
    private String udtDt;
    private String delDt;

    public ProjectDefaultVO() {
    }

    public ProjectDefaultVO(Integer id) {
        this.id = id;
    }

    public String getSearchYear() {
        return searchYear;
    }

    public void setSearchYear(String searchYear) {
        this.searchYear = searchYear;
    }

    public String getSearchStartDt() {
        return searchStartDt;
    }

    public void setSearchStartDt(String searchStartDt) {
        this.searchStartDt = searchStartDt;
    }

    public String getSearchEndDt() {
        return searchEndDt;
    }

    public void setSearchEndDt(String searchEndDt) {
        this.searchEndDt = searchEndDt;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public String getSearchOrderingName() {
        return searchOrderingName;
    }

    public void setSearchOrderingName(String searchOrderingName) {
        this.searchOrderingName = searchOrderingName;
    }

    public String getSearchDepartment() {
        return searchDepartment;
    }

    public void setSearchDepartment(String searchDepartment) {
        this.searchDepartment = searchDepartment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrderingName() {
        return orderingName;
    }

    public void setOrderingName(String orderingName) {
        this.orderingName = orderingName;
    }

    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getImplementDt() {
        return implementDt;
    }

    public void setImplementDt(String implementDt) {
        this.implementDt = implementDt;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyKind() {
        return companyKind;
    }

    public void setCompanyKind(String companyKind) {
        this.companyKind = companyKind;
    }


    public Integer getCrtId() {
        return crtId;
    }

    public void setCrtId(Integer crtId) {
        this.crtId = crtId;
    }

    public Integer getUdtId() {
        return udtId;
    }

    public void setUdtId(Integer udtId) {
        this.udtId = udtId;
    }

    public Integer getDelId() {
        return delId;
    }

    public void setDelId(Integer delId) {
        this.delId = delId;
    }

    public String getCrtDt() {
        return crtDt;
    }

    public void setCrtDt(String crtDt) {
        this.crtDt = crtDt;
    }

    public String getUdtDt() {
        return udtDt;
    }

    public void setUdtDt(String udtDt) {
        this.udtDt = udtDt;
    }

    public String getDelDt() {
        return delDt;
    }

    public void setDelDt(String delDt) {
        this.delDt = delDt;
    }


}
