package com.jinjin.jintranet.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor
public class ProjectMemberVO extends ProjectDefaultVO {
    private Integer pmId;
    private Integer projectId;

    @NotNull(message = "회사명을 선택해주세요.")
    private Integer companyId;
    @NotBlank(message = "투입인력 이름을 입력해주세요.")
    private String memberName;
    @NotBlank(message = "역할을 입력해주세요.")
    private String role;
    @NotBlank(message = "구분을 선택해주세요.")
    private String action;
    @Pattern(regexp = "^{0}||[0-9.]{4}$", message = "소숫점 첫재자리까지 입력해주세요.")
    private String manMonth;
    @Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 투입 시작일을 선택해주세요. (yyyy-MM-dd)")
    private String manStartDt;
    @Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 투입 종료일을 선택해주세요. (yyyy-MM-dd)")
    private String manEndDt;

    private String actionName;

    public ProjectMemberVO(Integer id) {
        super(id);
    }

}
