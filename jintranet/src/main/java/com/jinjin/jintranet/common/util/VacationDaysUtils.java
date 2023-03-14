package com.jinjin.jintranet.common.util;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinjin.jintranet.addBreak.service.AddBreakMapper;
import com.jinjin.jintranet.common.vo.AddBreakVO;
import com.jinjin.jintranet.common.vo.HolidayVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;
import com.jinjin.jintranet.commuting.service.CommutingService;
import com.jinjin.jintranet.holiday.service.HolidayMapper;
import com.jinjin.jintranet.schedule.service.ScheduleMapper;

@Component
public class VacationDaysUtils {
	 private static final Logger LOGGER = LoggerFactory.getLogger(VacationDaysUtils.class);
	
    private static ScheduleMapper scheduleMapper;
    private static HolidayMapper holidayMapper;
    private static AddBreakMapper addBreakMapper;
    @Autowired
    public VacationDaysUtils(ScheduleMapper scheduleMapper, HolidayMapper holidayMapper,AddBreakMapper addBreakMapper) {
        this.scheduleMapper = scheduleMapper;
        this.holidayMapper = holidayMapper;
        this.addBreakMapper = addBreakMapper;
    }
    
    
    public static MemberVO getMemberVacationDays(
            MemberVO memberVO, int year, int month, int date) throws Exception {

    	try {
    		
        double total = getTotalVacationDays(memberVO, year, month, date);
        double use = getUseVacationDays(memberVO, year, month, date);
        Integer add = getAddVacationDays(memberVO, year);
        memberVO.setTotal(total);
        memberVO.setUse(use);
        memberVO.setAdd(add);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}

        return memberVO;
    }

    private static double getTotalVacationDays(
            MemberVO memberVO, int curYear, int curMonth, int curDate) {

        double total = 0;

        LocalDate doe = new LocalDate(memberVO.getCrtDt());
        int yearsOfService = doe.getYear();
        
        if(curYear < yearsOfService) {
        	return 0;
        }
        
        if (curYear - yearsOfService < 2) {
            if (curYear == yearsOfService) {
                int monthsOfService = doe.getMonthOfYear();
                if (curMonth > monthsOfService) {
                    total = curMonth - monthsOfService;
                    if (curDate < doe.getDayOfMonth()) total--;
                }
            } else {
                LocalDate prev = new LocalDate(curYear - 1, 12, 31);
                int prevDays = prev.getDayOfYear();
                int monthsOfService = doe.getMonthOfYear(); //1월 9월 
                int DaysOfService = doe.getDayOfMonth();
                double daysOfService = prevDays - doe.getDayOfYear();

                total = Math.floor(daysOfService / 365 * 15);
                
                for(int i = 1 ; i <= monthsOfService ; i++) {
                	if (curMonth > i) {
                    	total++;
                    }
                }
                
            }
        } else {
            total = 15;

            int add = (curYear - yearsOfService)/2 - 1;
            total += add;

            if (total > 25) total = 25;
        }

        return total;
    }

    private static int getAddVacationDays(MemberVO memberVO,int curYear) throws Exception {
    	int allAddBreak = 0;
    	try {
    		
    	int loginId = memberVO.getId();
    	//int currentYear = LocalDate.now().getYear();
    		try {
    			Integer add = (Integer) addBreakMapper.addBreakCount(AddBreakVO.builder().memberId(loginId).year(Integer.toString(curYear)).build());
    			allAddBreak += add == null ? 0 : add;
			} catch (Exception e) {
				loggingStackTrace(e, LOGGER);
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

        return allAddBreak;
    }
    

    private static Double getUseVacationDays(MemberVO memberVO, int curYear, int curMonth, int curDate) {
        ScheduleVO scheduleVO = new ScheduleVO();
        scheduleVO.setMemberId(memberVO.getId());
        
        LocalDate doe = new LocalDate(memberVO.getCrtDt());
        int yearsOfService = doe.getYear();

        //int doeYear = (curYear - yearsOfService == 1) ? curYear - 1 : curYear;

        
        LocalDate startDt = new LocalDate(curYear, 1, 1);
        LocalDate endDt = new LocalDate(curYear, 12, 31);

        scheduleVO.setStartDt(startDt.toString());
        scheduleVO.setEndDt(endDt.toString());
        List<ScheduleVO> fullVacations = scheduleMapper.findFullScheduleByMemberIdAndStartDtAndEndDt(scheduleVO);
        List<ScheduleVO> halfVacations = scheduleMapper.findHalfScheduleByMemberIdAndStartDtAndEndDt(scheduleVO);


        HolidayVO holidayVO = new HolidayVO();
        holidayVO.setSearchStartDt(startDt.toString());
        holidayVO.setSearchEndDt(endDt.toString());

        List<HolidayVO> holidays = holidayMapper.findAll(holidayVO);

        int full = getVacationCount(fullVacations, holidays);
        //int half = getVacationCount(halfVacations, holidays);
        double half = halfVacations.size();

        return full + (half * 0.5);
    }

    private static int getVacationCount(List<ScheduleVO> vacations, List<HolidayVO> holidays) {
        Set<LocalDate> set = new HashSet<>();

        Set<LocalDate> tmp = new HashSet<>();
        for (HolidayVO tmpVO : holidays) {
            tmp.add(new LocalDate(tmpVO.getHolidayDt()));
        }


        for (ScheduleVO vo : vacations) {
            LocalDate startDt2 = new LocalDate(vo.getStartDt());
            LocalDate endDt2 = new LocalDate(vo.getEndDt());

            int days = Days.daysBetween(startDt2, endDt2).getDays();
            for (int i = 0; i <= days; i++) {
                set.add(startDt2.plusDays(i));
            }
        }

        set.removeAll(tmp);

        Iterator it = set.iterator();
        while (it.hasNext()) {
            int dow = ((LocalDate)it.next()).getDayOfWeek();
            if (dow == 6 || dow == 7) {
                it.remove();
            }
        }

        return set.size();
    }
}
