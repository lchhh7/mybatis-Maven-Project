package com.jinjin.jintranet.common.vo;

import java.util.Date;

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
public class CommutingVO extends DefaultVO{
	@Getter(AccessLevel.NONE)
	private static final String tableName = "COMMUTING";
	
	private Integer searchMemberId;
	private String searchStartDt;
	private String searchEndDt;
	private String searchCalendarType;
	
	private Integer id;

	private Integer memberId;
	@Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 날짜를 선택해주세요. (yyyy-MM-dd)")
	private Date commutingTm;
	@NotBlank(message = "결제자를 선택해주세요.")
	private String approveId;
	@Pattern(regexp = "^[Y|N]$")
	private String attendYn;

	private String dt;
	private String tm;
	private String dtm;

	private String cancelReason;
	
	private String scheduleStatus;
	private String startDt; 
	
	//엑셀필드
	private Integer selectedYear; 
	private Integer month; 
	
	//외근필드
	private Integer owInsertCheck;
	
	public CommutingVO(Integer id) {
		this.id = id;
	}
}
