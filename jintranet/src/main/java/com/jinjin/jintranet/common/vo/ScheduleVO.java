package com.jinjin.jintranet.common.vo;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleVO extends ScheduleCodeVO  {
	@Getter(AccessLevel.NONE)
	private static final String tableName = "SCHEDULE";
	private final String passengerTableName = "SCHEDULE_PASSENGER";
	
	
	private Integer searchMemberId;
	
	private String searchTypeSC;
	private String searchTypeVA;
	private String searchTypeOW;
	private String searchTypeBT;
	private String searchStartDt;
	private String searchEndDt;

	private String searchStatusR;
	private String searchStatusY;
	private String searchStatusN;

	private Integer id;

	@NotBlank(message = "일정 종류를 선택해주세요.")
	private String type;
	private String vacationType;
	private Integer memberId;

	@NotBlank(message = "제목을 입력해주세요.")
	private String title;
	@NotBlank(message = "내용을 입력해주세요.")
	private String content;
	@Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 일정 시작일을 선택해주세요. (yyyy-MM-dd)")
	private String startDt;
	@Pattern(regexp = "^(19[0-9]{2}|2[0-9]{3})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 일정 종료일 선택해주세요. (yyyy-MM-dd)")
	private String endDt;
	@Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "올바른 일정 시작시간을 입력해주세요. (HH:mm)")
	private String startTm;
	@Pattern(regexp = "^([01][0-9]|2[0-3]):([0-5][0-9])$", message = "올바른 일정 종료시간을 입력해주세요.  (HH:mm)")
	private String endTm;

	private Integer approveId;
	private Date approveDt;
	private String status;
	private String color;
	private String cancelReason;

	private Integer crtId;
	private Integer udtId;
	private Integer delId;

	private String typeName;
	private String memberName;
	private String statusName;
	private String approveName;

	private String crtDt;
	private String udtDt;
	private String delDt;
	
	private Integer rcount;
	
	private List<String> passengers;
	private String passenger;
	private Integer scheduleId;
	
	public ScheduleVO(Integer id) {
		this.id = id;
	}
}
