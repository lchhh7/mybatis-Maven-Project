package com.jinjin.jintranet.common.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CompanyAttachVO extends AttachVO {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String tableName = "COMPANY_ATTACH";
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String mainTableName = "COMPANY";

    private Integer companyId;
    private String attachKind;

    private String searchAttachKind;

    public CompanyAttachVO(Integer id) {
        super(id);
    }
}
