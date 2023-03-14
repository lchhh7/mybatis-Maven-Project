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
public class HolidayVO extends DefaultVO{
	@Getter(AccessLevel.NONE)
	private final String tableName = "HOLIDAY";

	private Integer id;
	private String title;
	private Date holidayDt;

	private String searchStartDt;
	private String searchEndDt;
}
