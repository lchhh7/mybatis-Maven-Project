package com.jinjin.jintranet.common.vo;

public class CodeVO extends DefaultVO {

    private final String tableName = "CODE";

    private Integer id;
    private String majorCd;
    private String minorCd;
    private String codeName;
    private String codeFg;
    private String codeOrd;

    public CodeVO() {}

    public CodeVO(String majorCd) {
        this.majorCd = majorCd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMajorCd() {
        return majorCd;
    }

    public void setMajorCd(String majorCd) {
        this.majorCd = majorCd;
    }

    public String getMinorCd() {
        return minorCd;
    }

    public void setMinorCd(String minorCd) {
        this.minorCd = minorCd;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeFg() {
        return codeFg;
    }

    public void setCodeFg(String codeFg) {
        this.codeFg = codeFg;
    }

    public String getCodeOrd() {
        return codeOrd;
    }

    public void setCodeOrd(String codeOrd) {
        this.codeOrd = codeOrd;
    }
}
