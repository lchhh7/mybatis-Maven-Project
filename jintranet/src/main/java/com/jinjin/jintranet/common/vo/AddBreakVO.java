package com.jinjin.jintranet.common.vo;

import java.util.Date;

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
public class AddBreakVO extends DefaultVO{
	@Getter(AccessLevel.NONE)
	private static final String tableName = "MONTHLY_ADDBREAK";

	private Integer id;

	private Integer memberId;

	private String year;
	private String month;

	private String monthlyAccumulateHours;
	private String monthlyAccumulateDays;
}
