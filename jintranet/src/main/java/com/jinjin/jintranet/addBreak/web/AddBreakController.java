package com.jinjin.jintranet.addBreak.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;

import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.jinjin.jintranet.addBreak.service.AddBreakService;
import com.jinjin.jintranet.common.vo.AddBreakVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.commuting.service.CommutingService;
import com.jinjin.jintranet.member.service.MemberService;

@Controller
public class AddBreakController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddBreakController.class);
    
    private final MemberService memberService;
    
    private final CommutingService commutingService;

    private final AddBreakService addBreakService;
    
    @Autowired
    public AddBreakController(MemberService memberService,CommutingService commutingService,AddBreakService addBreakService) {
    	this.memberService = memberService;
    	this.commutingService = commutingService;
    	this.addBreakService = addBreakService;
    }
    /* 매달 2일 monthly_addBreak update하기
     * insert는 procedure + jobs 이용하니까 monthly-addBreak 테이블을 변경하면
     * procedure , jobs이 break되어 있는지 꼭 확인해야됨
     */
    @Scheduled(cron ="0 0 0 2,5 * ?")
    public void edit() throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
        	List<MemberVO> memberList = memberService.findAll(new MemberVO());
        	LocalDate now = LocalDate.now();
        	int lastYear = now.minusYears(1).getYear();
        	int year = now.getYear();
            int lastMonth = now.minusMonths(1).getMonthOfYear();
            int month = now.getMonthOfYear();
        	
            String startDt = new LocalDate((month == 1 ? lastYear : year), lastMonth, 1).toString();
            String endDt = new LocalDate(year, month, 1).toString();
            
        
	        for(int i = 0; i <memberList.size() ;i++) {
	        	Map<String,Object> map = commutingService.findAll(startDt, endDt, memberList.get(i).getId());
	        	if(map.get("allTm") == null || "0:00".equals(map.get("allTm"))) {
	        		continue;
	        	}
	        	
	        	String monthlyHours = map.get("allTm").toString();
	        	int monthlyDays = Integer.parseInt(map.get("allTm").toString().split(":")[0]) / 8;
		        	addBreakService.monthlyEdit(
				        	AddBreakVO.builder().memberId(memberList.get(i).getId()).monthlyAccumulateHours(monthlyHours)
				        	.monthlyAccumulateDays(Integer.toString(monthlyDays)).year(Integer.toString((month == 1 ? lastYear : year)))
				        	.month(Integer.toString(lastMonth)).build()
		        			);
	        }
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }
    }
}
  