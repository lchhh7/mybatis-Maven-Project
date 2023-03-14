package com.jinjin.jintranet.common.vo;

import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProjectVO extends ProjectDefaultVO {
    @NotBlank(message = "프로젝트명을 입력해주세요.")
    private String title;
    @NotBlank(message = "발주사명을 입력해주세요.")
    private String orderingName;
    @Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 프로젝트 시작일을 선택해주세요. (yyyy-MM-dd)")
    private String startDt;
    @Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 프로젝트 종료일을 선택해주세요. (yyyy-MM-dd)")
    private String endDt;
    @Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 프로젝트 착수일을 선택해주세요. (yyyy-MM-dd)")
    private String launchDt;
    @NotBlank(message = "진행부서를 선택해주세요.")
    private String department;

    private String ppsYN;
    @NotBlank(message = "계약번호를 입력해주세요.")
    private String contractNo;
    @NotBlank(message = "사업분야를 선택해주세요.")
    private String businessField;

    @NotNull(message = "올바른 부가세 포함 계약금액을 입력해주세요.")
    @Min(value = 1, message = "올바른 부가세 포함 계약금액을 입력해주세요.")
    @Max(value = 100_000_000_0000L, message = "올바른 부가세 포함 계약금액을 입력해주세요.")
    private Long amountSurtaxInclude;
    @NotNull(message = "올바른 부가세 제외 계약금액을 입력해주세요.")
    @Min(value = 1, message = "올바른 부가세 제외 계약금액을 입력해주세요.")
    @Max(value = 100_000_000_0000L, message = "올바른 부가세 제외 계약금액을 입력해주세요.")
    private Long amountSurtaxExclude;
    @NotNull(message = "올바른 계약보증금액을 입력해주세요.")
    @Min(value = 1, message = "올바른 계약보증금액을 입력해주세요.")
    @Max(value = 100_000_000_0000L, message = "올바른 계약보증금액을 입력해주세요.")
    private Long contractDeposit;
    @NotNull(message = "올바른 계약보증금 비율을 입력해주세요. (0 ~ 100)")
    @Min(value = 0, message = "올바른 계약보증금 비율을 입력해주세요. (0 ~ 100)")
    @Max(value = 100, message = "올바른 계약보증금 비율을 입력해주세요. (0 ~ 100)")
    @Digits(integer = 3, fraction = 2, message = "소수점 둘째자리까지만 입력해주세요.")
    private Double contractDepositRate;
    @NotNull(message = "올바른 하자보증금액을 입력해주세요.")
    @Min(value = 1, message = "올바른 하자보증금액을 입력해주세요.")
    @Max(value = 100_000_000_0000L, message = "올바른 하자보증금액을 입력해주세요.")
    private Long defectDeposit;
    @NotNull(message = "올바른 하자보증금 비율을 입력해주세요. (0 ~ 100)")
    @Min(value = 0, message = "올바른 하자보증금 비율을 입력해주세요. (0 ~ 100)")
    @Max(value = 100, message = "올바른 하자보증금 비율을 입력해주세요. (0 ~ 100)")
    @Digits(integer = 3, fraction = 2, message = "소수점 둘째자리까지만 입력해주세요.")
    private Double defectDepositRate;
    private String consortiumYN;
    private String subcontractYN;

    @NotBlank(message = "PM이름을 입력해주세요.")
    private String projectManagerName;
    @NotBlank(message = "사업실적 등록여부를 선택해주세요.")
    private String performanceRegistYN;

    private List<Integer> consortiums;
    private List<Integer> subcontracts;

    private String searchStartDt;
    private String searhEndDt;
    
    public ProjectVO(Integer projectId) {
        super(projectId);
    }

}
