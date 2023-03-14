package com.jinjin.jintranet.holiday.service;

import com.jinjin.jintranet.common.vo.HolidayVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface HolidayMapper {
    List<HolidayVO> findAll(HolidayVO holidayVO);
    
    Integer holidayCheck(HolidayVO holidayVO);
    
    List<String> currentYearRedDay(HolidayVO holidayVO);
}
