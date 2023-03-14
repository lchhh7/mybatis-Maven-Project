package com.jinjin.jintranet.common.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class SupplyVO extends DefaultVO {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String tableName = "SUPPLY";

    private String searchName;
    private String searchMemberId;
    private String searchApproveId;
    private String searchStatus1;
    private String searchStatus2;
    private String searchStatus3;

    private Integer id;

    @NotBlank(message = "비품명을 입력해주세요.")
    private String name;
    private String status;
    private String statusName;

    @Min(value = 1, message = "1이상의 수량을 입력해주세요.")
    @Max(value = 9999, message = "4자리 수 이하의 숫자를 입력해주세요.")
    @NotNull(message = "수량을 입력해주세요.")
    private Integer amount;

    @Min(value = 1, message = "1원이상의 금액을 입력해주세요.")
    @Max(value = 100000000, message = "1억원이하의 금액을 입력해주세요.")
    @NotNull(message = "단품 금액을 입력해주세요.")
    private double price;
    
    private Integer shippingFee;
    
    private String url;
    private String url1;
    private String url2;
    private String url3;
    private String url4;
    
    private Integer memberId;
    private String memberName;
    @NotNull(message = "승인자를 선택해주세요.")
    private Integer approveId;
    private String approveName;
    private String approveDt;
    private String payment;
    private String billYN;

    private Integer crtId;
    private Integer udtId;
    private Integer delId;

    private String crtDt;
    private String udtDt;
    private String delDt;
    
    private List<SupplyAttachVO> attaches;

    public SupplyVO(Integer id) {
        this.id = id;
    }
}
