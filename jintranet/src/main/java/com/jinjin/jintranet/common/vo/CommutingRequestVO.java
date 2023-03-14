package com.jinjin.jintranet.common.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommutingRequestVO extends CommutingCodeVO {
	@Getter(AccessLevel.NONE)
	private static final String tableName = "COMMUTING_REQUEST";
	
	@Getter(AccessLevel.NONE)
	private static final String subtableName = "COMMUTING_CALCULATE";
	
	private Integer searchMemberId;
	private String searchStatusR;
	private String searchStatusY;
	private String searchStatusN;
	private String searchType;
	private String searchStartDt;
	private String searchEndDt;
	
	private Integer id;

	@NotBlank(message = "신청 유형을 선택해주세요.")
	private String type;
	
	private String status;
	private Integer memberId;
	@Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 날짜를 선택해주세요. (yyyy-MM-dd)")
	private String requestDt;
	@Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "올바른 일정 시작시간을 입력해주세요. (HH:mm)")
	private String requestTm;

	@NotBlank(message = "내용을 입력해주세요.")
	private String content;
	
	@NotNull(message = "결제자를 선택해주세요.")
	private Integer approveId;
	
	private String approveDt;
	private String cancelReason;
	private Integer crtId;
	private Integer udtId;
	private Integer delId;
	private String crtDt;
	private String udtDt;
	private String delDt;

	private String memberName;
	private String approveName;
	private String typeName;
	private String statusName;
	
	//11.26 추가(calculate db)
	private Integer requestId;
	
	@Pattern(regexp = "^([01][0-9]|2[0-4]):([0-5][0-9])$", message = "시작시간을 클릭해 선택해주세요. (HH:mm)")
	private String startTm;
	
	@Pattern(regexp = "^([01][0-9]|2[0-4]):([0-5][0-9])$", message = "끝 시간을 클릭해 선택해주세요. (HH:mm)")
	private String endTm;
	private String pureWorkTm;
	private String extensionWorkTm;
	private String extensionNightWorkTm;
	private String totalTm;
	
	public CommutingRequestVO(Integer id) {
		this.id = id;
	}
}
