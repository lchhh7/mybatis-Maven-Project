package com.jinjin.jintranet.commuting.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.common.vo.CommutingRequestVO;
import com.jinjin.jintranet.common.vo.HolidayVO;
import com.jinjin.jintranet.holiday.service.HolidayMapper;
import com.jinjin.jintranet.schedule.service.ScheduleMapper;
import com.jinjin.jintranet.security.SecurityUtils;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class CommutingCalculateService {
	
	private CommutingCalculateMapper commutingCalculateMapper;
	
	private CommutingRequestMapper requestMapper;

	private ScheduleMapper scheduleMapper;

	private HolidayMapper holidayMapper;
	
	@Autowired
	public CommutingCalculateService(CommutingCalculateMapper commutingCalculateMapper,CommutingRequestMapper requestMapper,ScheduleMapper scheduleMapper,HolidayMapper holidayMapper) {
		this.commutingCalculateMapper = commutingCalculateMapper;
		this.requestMapper = requestMapper;
		this.scheduleMapper = scheduleMapper;
		this.holidayMapper = holidayMapper;
	}
	

	public void write(CommutingRequestVO commutingRequestVO) {
       int id = SecurityUtils.getLoginMemberId();
        
       commutingRequestVO.setMemberId(id);
       commutingRequestVO.setCrtId(id);
       commutingRequestVO.setUdtId(id);
        
       int startHour = Integer.parseInt(commutingRequestVO.getStartTm().split(":")[0]);
       int endHour = Integer.parseInt(commutingRequestVO.getEndTm().split(":")[0]);
       int holidayCheck = 0;
       int weekendCheck = 0;
       try {
    	//명절체크
        HolidayVO holidayVO = new HolidayVO();
        holidayVO.setSearchStartDt(commutingRequestVO.getRequestDt());
        	holidayCheck = holidayMapper.holidayCheck(holidayVO); 
        //주말체크
        	weekendCheck = getDateDayCode(commutingRequestVO.getRequestDt());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        if(endHour < 9) {
        	endHour += 24;
        }
        
        int startMinute = Integer.parseInt(commutingRequestVO.getStartTm().split(":")[1]);
        int endMinute = Integer.parseInt(commutingRequestVO.getEndTm().split(":")[1]);
        if(startMinute > endMinute) {
        	endHour = endHour - 1;
        }
        //순수일한시간
        commutingRequestVO.setPureWorkTm(endHour-startHour+"");
        //20     3 -> 7시간인데 20~22(2시간) -> 1.5 배     22~03 (5시간) ->2배
        if(holidayCheck + weekendCheck == 0) {
        	if(endHour - startHour <=2) {
        		commutingRequestVO.setExtensionWorkTm((endHour-startHour)*1.5+"");
        		commutingRequestVO.setExtensionNightWorkTm(0+"");
        		commutingRequestVO.setTotalTm((endHour-startHour)*1.5+""); 
        	}else if(endHour - startHour > 2) {
        		commutingRequestVO.setExtensionWorkTm(3+"");
        		commutingRequestVO.setExtensionNightWorkTm((endHour - startHour -2)*2+"");
        		commutingRequestVO.setTotalTm(3 + (endHour - startHour -2)*2+""); 
        	}
        }else {
        	if(endHour - startHour < 8) {
        		commutingRequestVO.setExtensionWorkTm((endHour -startHour) *1.5+"");
        		commutingRequestVO.setTotalTm((endHour -startHour) *1.5+"");
        	}else {
        		commutingRequestVO.setExtensionWorkTm(12+"");
        		commutingRequestVO.setTotalTm(12+"");
        	}
        	commutingRequestVO.setExtensionNightWorkTm(0+"");
        }
        commutingCalculateMapper.write(commutingRequestVO);
    }
	
	public void approve(CommutingRequestVO commutingRequestVO) {
		commutingCalculateMapper.approve(commutingRequestVO);
	}
	
	public void approveInit(CommutingRequestVO commutingRequestVO) {
		commutingCalculateMapper.approveInit(commutingRequestVO);
	}
	
	
	public void edit(CommutingRequestVO commutingRequestVO) {
		int id = SecurityUtils.getLoginMemberId();
		
		commutingRequestVO.setMemberId(id);
        commutingRequestVO.setUdtId(id);
        
        int startHour = Integer.parseInt(commutingRequestVO.getStartTm().split(":")[0]);
        int endHour = Integer.parseInt(commutingRequestVO.getEndTm().split(":")[0]);
        int holidayCheck = 0;
        int weekendCheck = 0;
        try {
     	//명절체크
         HolidayVO holidayVO = new HolidayVO();
         holidayVO.setSearchStartDt(commutingRequestVO.getRequestDt());
         	holidayCheck = holidayMapper.holidayCheck(holidayVO); 
         //주말체크
         	weekendCheck = getDateDayCode(commutingRequestVO.getRequestDt());
 		
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
        
         if(endHour < 9) {
         	endHour += 24;
         }
         
         int startMinute = Integer.parseInt(commutingRequestVO.getStartTm().split(":")[1]);
         int endMinute = Integer.parseInt(commutingRequestVO.getEndTm().split(":")[1]);
         if(startMinute > endMinute) {
         	endHour = endHour - 1;
         }
         
         //순수일한시간
         commutingRequestVO.setPureWorkTm(endHour-startHour+"");
         //20     3 -> 7시간인데 20~22(2시간) -> 1.5 배     22~03 (5시간) ->2배
         if(holidayCheck + weekendCheck == 0) {
         	if(endHour - startHour <=2) {
         		commutingRequestVO.setExtensionWorkTm((endHour-startHour)*1.5+"");
         		commutingRequestVO.setExtensionNightWorkTm(0+"");
         		commutingRequestVO.setTotalTm((endHour-startHour)*1.5+""); 
         	}else if(endHour - startHour > 2) {
         		commutingRequestVO.setExtensionWorkTm(3+"");
         		commutingRequestVO.setExtensionNightWorkTm((endHour - startHour -2)*2+"");
         		commutingRequestVO.setTotalTm(3 + (endHour - startHour -2)*2+""); 
         	}
         }else {
         	if(endHour - startHour < 8) {
         		commutingRequestVO.setExtensionWorkTm((endHour -startHour) *1.5+"");
         		commutingRequestVO.setTotalTm((endHour -startHour) *1.5+"");
         	}else {
         		commutingRequestVO.setExtensionWorkTm(12+"");
         		commutingRequestVO.setTotalTm(12+"");
         	}
         	commutingRequestVO.setExtensionNightWorkTm(0+"");
         }
        commutingCalculateMapper.edit(commutingRequestVO);
    }
	
	
	public void deleteRequest(CommutingRequestVO commutingRequestVO) {
        commutingCalculateMapper.delete(commutingRequestVO);
    }
	
	public void deleteCascadeRequest(CommutingRequestVO commutingRequestVO) {
        commutingCalculateMapper.deleteCascade(commutingRequestVO);
    }
	
    
    public List<CommutingRequestVO> searching(CommutingRequestVO commutingRequestVO) {
    	return commutingCalculateMapper.search(commutingRequestVO);
    }
    
    public List<CommutingRequestVO> recodeSearch(CommutingRequestVO commutingRequestVO) {
    	return commutingCalculateMapper.recodeSearch(commutingRequestVO);
    }
    
    public static Integer getDateDayCode(String date) throws Exception {         
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
    	  java.util.Date nDate = dateFormat.parse(date);         
    	  Calendar cal = Calendar.getInstance();
    	  cal.setTime(nDate);         
    	  int dayNum = cal.get(Calendar.DAY_OF_WEEK);
    	  if(dayNum == 1 || dayNum == 7) { //주말
    		  return 1;
    	  }else {
    		  return 0;
    	  }
    	}
    
    public List<CommutingRequestVO> detailSearchByExcel(CommutingRequestVO commutingRequestVO) {
    	return commutingCalculateMapper.detailSearchByExcel(commutingRequestVO);
    }
    
    public CommutingRequestVO findByRequestId(CommutingRequestVO commutingRequestVO) {
    	return commutingCalculateMapper.findByRequestId(commutingRequestVO);
    }
}