package com.jinjin.jintranet.holiday.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jinjin.jintranet.common.vo.HolidayVO;

@Service
public class HolidayService {
	 private static final Logger LOGGER = LoggerFactory.getLogger(HolidayService.class);
	
	@Autowired
    private HolidayMapper holidayMapper;
    
    public List<String> currentYearRedDay(HolidayVO holidayVO) throws Exception {
           List<String> redDayCheck  =  holidayMapper.currentYearRedDay(holidayVO);
            return redDayCheck;
    }
}
