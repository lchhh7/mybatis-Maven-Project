package com.jinjin.jintranet.admin.commuting.service;

import com.jinjin.jintranet.addBreak.service.AddBreakMapper;
import com.jinjin.jintranet.addBreak.service.AddBreakService;
import com.jinjin.jintranet.common.vo.AddBreakVO;
import com.jinjin.jintranet.common.vo.CommutingRequestVO;
import com.jinjin.jintranet.common.vo.CommutingVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;
import com.jinjin.jintranet.commuting.service.CommutingCalculateMapper;
import com.jinjin.jintranet.member.service.MemberMapper;
import com.jinjin.jintranet.schedule.service.ScheduleMapper;
import com.jinjin.jintranet.security.SecurityUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jinjin.jintranet.common.constant.Constants.*;
import static com.jinjin.jintranet.common.util.PageUtils.page;

@Service
public class AdminCommutingService {
	
    private AdminCommutingMapper commutingMapper;

    private MemberMapper memberMapper;
    
    private CommutingCalculateMapper commutingCalculateMapper;
    
    private AddBreakMapper addBreakMapper;
    
    public AdminCommutingService() {}
    
    @Autowired
    public AdminCommutingService(AdminCommutingMapper commutingMapper,MemberMapper memberMapper,CommutingCalculateMapper commutingCalculateMapper,AddBreakMapper addBreakMapper) {
    	this.commutingMapper = commutingMapper;
    	this.memberMapper = memberMapper;
    	this.commutingCalculateMapper = commutingCalculateMapper;
    	this.addBreakMapper = addBreakMapper;
    }
    
    public List<MemberVO> findMemberAll(MemberVO memberVO) {
        return memberMapper.findAll(memberVO);
    }

    public Map<String, Object> main(CommutingRequestVO commutingRequestVO, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        commutingRequestVO.setApproveId(SecurityUtils.getLoginMemberId());

        int totalCnt = commutingMapper.getCommutingCnt(commutingRequestVO);
        commutingRequestVO.setTotalCnt(totalCnt);

        List<ScheduleVO> list = commutingMapper.findAll(commutingRequestVO);

        map.put("list", list);
        map.put("page", page(commutingRequestVO, "commutings", request));
        map.put("totalCnt", totalCnt);

        return map;
    }

    public CommutingRequestVO findById(CommutingRequestVO commutingRequestVO) {
        return commutingMapper.findById(commutingRequestVO);
    }
    
    public void approve(CommutingRequestVO commutingRequestVO) {
        String status = commutingRequestVO.getStatus();
        commutingRequestVO = commutingMapper.findById(commutingRequestVO);
        commutingRequestVO.setStatus(status);
        commutingRequestVO.setApproveId(SecurityUtils.getLoginMemberId());
        commutingMapper.approve(commutingRequestVO);
        
        //commutingCalculateMapper.approve(commutingRequestVO);
        //approveName , content , crtDt , requestDt , id
        
        String type = commutingRequestVO.getType();

        if (CODE_STATUS_YES.equals(status) || CODE_STATUS_DELETE.equals(status)) {
            switch (type) {
                case CODE_COMMUTING_REQUEST_TYPE_ON:
                case CODE_COMMUTING_REQUEST_TYPE_OFF:
                    approveCommuting(commutingRequestVO);
                    break;
                case CODE_COMMUTING_REQUEST_TYPE_ADD:
                    approveAdd(commutingRequestVO);
                    break;
                case CODE_COMMUTING_REQUEST_TYPE_OVER:
                	monthlyEdit(commutingRequestVO);
                	break;
                default:
                    break;
            }
        }
    }
    
    public void approveInit(CommutingRequestVO commutingRequestVO) {
    	monthlyInit(commutingRequestVO);
    	commutingMapper.approveInit(commutingRequestVO);
    }

    private void approveCommuting(CommutingRequestVO commutingRequestVO) {
        String dt = commutingRequestVO.getRequestDt();
        String tm = commutingRequestVO.getRequestTm();

        Date commutingTm = new LocalDateTime(dt + "T" + tm).toDate();

        CommutingVO commutingVO = new CommutingVO();
        commutingVO.setMemberId(commutingRequestVO.getMemberId());
        commutingVO.setCommutingTm(commutingTm);
        commutingVO.setAttendYn(commutingRequestVO.getType());

        if (CODE_STATUS_YES.equals(commutingRequestVO.getStatus())) {
            commutingMapper.write(commutingVO);
        } else {
            commutingVO = commutingMapper.findOne(commutingVO).get(0);
            commutingVO.setCancelReason(commutingRequestVO.getCancelReason());
            commutingMapper.deleteCommuting(commutingVO);
        }
    }

    private void approveAdd(CommutingRequestVO commutingRequestVO) {
        int cnt = Integer.parseInt(commutingRequestVO.getContent());
        ScheduleVO scheduleVO = new ScheduleVO();
        scheduleVO.setType("AV");
        scheduleVO.setStatus("Y");
        scheduleVO.setMemberId(commutingRequestVO.getMemberId());
        scheduleVO.setTitle(commutingRequestVO.getRequestDt() + " 추가휴가신청");
        scheduleVO.setContent(commutingRequestVO.getRequestDt() + " 추가휴가신청");
        scheduleVO.setStartDt(commutingRequestVO.getRequestDt());
        scheduleVO.setEndDt(commutingRequestVO.getRequestDt());
        scheduleVO.setStartTm("10:00");
        scheduleVO.setEndTm("19:00");
        scheduleVO.setApproveId(commutingRequestVO.getApproveId());
        scheduleVO.setCrtId(commutingRequestVO.getApproveId());
        scheduleVO.setUdtId(commutingRequestVO.getApproveId());

        for (int i = 0; i < cnt; i++) {
            commutingMapper.addVacations(scheduleVO);
        }
    }
    
    public void monthlyEdit(CommutingRequestVO commutingRequestVO) {
    	 CommutingRequestVO calVO =  commutingCalculateMapper.findByRequestId(
         		new CommutingRequestVO().builder().
         		memberId(commutingRequestVO.getMemberId()).
         		requestDt(commutingRequestVO.getRequestDt()).
         		requestId(commutingRequestVO.getId()).build());
         
         String totalTm = calVO.getTotalTm();
     	
     	AddBreakVO addBreakVO = addBreakMapper.monthlySelect(new AddBreakVO().builder().year(calVO.getRequestDt().split("-")[0])
     			.month(calVO.getRequestDt().split("-")[1]).memberId(calVO.getMemberId()).build());
     	
     	String monthlyAccumulateHours = addBreakVO.getMonthlyAccumulateHours();
     	
     	float convertTime = Float.parseFloat((monthlyAccumulateHours.split(":")[1] == "30") ?  (monthlyAccumulateHours.split(":")[0] + ".5f") : monthlyAccumulateHours.split(":")[0]);
     	
     	String monthlyHours = (convertTime + Float.parseFloat(totalTm) > (int) (convertTime + Float.parseFloat(totalTm)) ) ? 
     			(int) (convertTime + Float.parseFloat(totalTm))+":30" : (int) (convertTime + Float.parseFloat(totalTm))+":00" ;
     	
     	int monthlyDays = ((int) (convertTime + Float.parseFloat(totalTm))) /8;
     	
     	addBreakMapper.monthlyEdit(AddBreakVO.builder().memberId(calVO.getMemberId()).monthlyAccumulateHours(monthlyHours)
 	        	.monthlyAccumulateDays(Integer.toString(monthlyDays)).year(calVO.getRequestDt().split("-")[0])
 	        	.month(calVO.getRequestDt().split("-")[1]).build());
    }
    
    public void monthlyInit(CommutingRequestVO commutingRequestVO) {
   	 CommutingRequestVO calVO =  commutingCalculateMapper.findByRequestId(
        		new CommutingRequestVO().builder().
        		memberId(commutingRequestVO.getMemberId()).
        		requestDt(commutingRequestVO.getRequestDt()).
        		requestId(commutingRequestVO.getId()).build());
        
   	 	if(calVO.getStatus().equals("Y")) {
	        String totalTm = calVO.getTotalTm();
	    	
	    	AddBreakVO addBreakVO = addBreakMapper.monthlySelect(new AddBreakVO().builder().year(calVO.getRequestDt().split("-")[0])
	    			.month(calVO.getRequestDt().split("-")[1]).memberId(calVO.getMemberId()).build());
	    	
	    	String monthlyAccumulateHours = addBreakVO.getMonthlyAccumulateHours();
	    	
	    	float convertTime = Float.parseFloat((monthlyAccumulateHours.split(":")[1] == "30") ?  (monthlyAccumulateHours.split(":")[0] + ".5f") : monthlyAccumulateHours.split(":")[0]);
	    	
	    	String monthlyHours = (convertTime - Float.parseFloat(totalTm) > (int) (convertTime - Float.parseFloat(totalTm)) ) ? 
	    			(int) (convertTime - Float.parseFloat(totalTm))+":30" : (int) (convertTime - Float.parseFloat(totalTm))+":00" ;
	    	
	    	int monthlyDays = ((int) (convertTime - Float.parseFloat(totalTm))) /8;
	    	
	    	addBreakMapper.monthlyEdit(AddBreakVO.builder().memberId(calVO.getMemberId()).monthlyAccumulateHours(monthlyHours)
		        	.monthlyAccumulateDays(Integer.toString(monthlyDays)).year(calVO.getRequestDt().split("-")[0])
		        	.month(calVO.getRequestDt().split("-")[1]).build());
   	 	}
   }

}
