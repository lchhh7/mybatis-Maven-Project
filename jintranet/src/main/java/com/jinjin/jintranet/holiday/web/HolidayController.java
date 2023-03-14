package com.jinjin.jintranet.holiday.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinjin.jintranet.common.vo.HolidayVO;
import com.jinjin.jintranet.holiday.service.HolidayService;

@Controller
public class HolidayController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HolidayController.class);
    
    private final HolidayService holidayService;
    
    @Autowired
    public HolidayController(HolidayService holidayService) {
    	this.holidayService = holidayService;
    }
    
    @RequestMapping(value="/redDayCheck.do", method = RequestMethod.POST)
    public ResponseEntity<List<String>> redDayCheck() throws Exception {
    	try {
    		List<String> list = holidayService.currentYearRedDay(new HolidayVO());
    		System.out.println(holidayService.currentYearRedDay(new HolidayVO())+"!!!!");
    		return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(holidayService.currentYearRedDay(new HolidayVO())+"@@@@");
			loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
    }
}
