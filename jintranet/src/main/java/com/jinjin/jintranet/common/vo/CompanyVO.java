package com.jinjin.jintranet.common.vo;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class CompanyVO extends CompanyDefaultVO {
    private String searchName;
    private String searchKind1;
    private String searchKind2;

    private Integer id;
    @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{5}$", message = "올바른 사업자 번호를 입력해주세요. (000-00-00000)")
    private String companyNo;
    @NotBlank(message = "업체명을 입력해주세요.")
    private String companyName;
    @Pattern(regexp = "^[P|C]$", message = "회사유형을 선택해주세요.")
    private String companyKind;

    private String companyKindName;

    private String licenseYN;
    private String bankbookYN;

    private Integer crtId;
    private Integer udtId;
    private Integer delId;

    // 분류 전
    private List<CompanyAttachVO> attaches;

    // 분류 후
    private CompanyAttachVO license;
    private List<CompanyAttachVO> bankbooks;
    
    public CompanyVO(Integer id) {
        this.id = id;
    }
}
