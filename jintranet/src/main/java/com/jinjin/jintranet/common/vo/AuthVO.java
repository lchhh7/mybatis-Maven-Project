package com.jinjin.jintranet.common.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AuthVO extends DefaultVO {
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private String tableName = "AUTH";
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private String subTableName = "AUTH_MEMBER";

    private Integer id;
    private String title;
    private Integer memberId;

    private Integer crtId;
    private Integer udtId;
    private Integer delId;

    private Date crtDt;
    private Date udtDt;
    private Date delDt;

    public AuthVO(Integer id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}
