package com.jinjin.jintranet.common.vo;

import com.jinjin.jintranet.common.constant.Constants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class CompanyDefaultVO extends DefaultVO {
    private String tableName = "COMPANY";
    private String attachTableName = "COMPANY_ATTACH";

    private String companyKindCd = Constants.CODE_COMPANY_KIND;
}
