package com.jinjin.jintranet.schedule.service;

import com.jinjin.jintranet.auth.service.AuthMapper;
import com.jinjin.jintranet.common.constant.Constants;
import com.jinjin.jintranet.common.util.EmailUtils;
import com.jinjin.jintranet.common.vo.AuthVO;
import com.jinjin.jintranet.common.vo.CommutingVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;
import com.jinjin.jintranet.commuting.service.CommutingMapper;
import com.jinjin.jintranet.commuting.service.CommutingService;
import com.jinjin.jintranet.security.SecurityUtils;

import lombok.NoArgsConstructor;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jinjin.jintranet.common.constant.Constants.*;

@Service
@NoArgsConstructor
public class ScheduleService {

	private ScheduleMapper scheduleMapper;

	private AuthMapper authMapper;

	private CommutingMapper commutingMapper;
    
    @Autowired
    public ScheduleService(ScheduleMapper scheduleMapper,AuthMapper authMapper,CommutingMapper commutingMapper) {
    	this.scheduleMapper = scheduleMapper;
    	this.authMapper = authMapper;
    	this.commutingMapper = commutingMapper;
    }
    
    public Map<String, Object> main() {
        Map<String, Object> map = new HashMap<>();

        ScheduleVO result = Optional.ofNullable(scheduleMapper.findForMain(
        		new ScheduleVO().builder().searchMemberId(SecurityUtils.getLoginMemberId()).build()))
        		.orElse(new ScheduleVO());
        
        map.put("schedule", result);
        map.put("cnt", result.getRcount());

        return map;
    }

    public List<MemberVO> findApproves() {
        AuthVO authVO = new AuthVO(Constants.CODE_AUTH_SCHEDULE);

        return authMapper.findAuthMemberByAuthId(authVO);
    }

    public List<ScheduleVO> findAll(ScheduleVO scheduleVO) {
        List<ScheduleVO> list = scheduleMapper.findAll(scheduleVO);
        return list;
    }

    public ScheduleVO findById(ScheduleVO scheduleVO) {
        return scheduleMapper.findById(scheduleVO);
    }

    public void write(ScheduleVO scheduleVO) {
        String type = scheduleVO.getType();
        if (CODE_SCHEDULE_TYPE_VACATION.equals(type)) {
        	
        	EmailUtils.email(scheduleVO);
        	
            if ("1".equals(scheduleVO.getVacationType())) scheduleVO.setType(CODE_SCHEDULE_TYPE_FULL_VACATION);
            else scheduleVO.setType(CODE_SCHEDULE_TYPE_HALF_VACATION);
        }

        int loginMemberId = SecurityUtils.getLoginMemberId();
        scheduleVO.setMemberId(loginMemberId);
        scheduleVO.setCrtId(loginMemberId);
        scheduleVO.setUdtId(loginMemberId);
        
        int sid = scheduleMapper.write(scheduleVO);
        scheduleVO.setScheduleId(sid);
        
        // 외근, 출장은 정상 출퇴근(외근시작 시간 ~ 19:00)으로 간주
        if (CODE_SCHEDULE_TYPE_OUTSIDE_WORK.equals(type)) {
        	List<String> mList = scheduleVO.getPassengers();
        	insertCommuting(scheduleVO, loginMemberId);
        	mList.stream().forEach(i ->insertCommuting(scheduleVO, Integer.parseInt(i)));
			
			/*
			 * for(String i : mList) { insertCommuting(scheduleVO, Integer.parseInt(i)); }
			 */
			 
        }
    }
// TODO: 동행자
    private void insertCommuting(ScheduleVO scheduleVO, int loginMemberId) {
        CommutingVO commutingVO = new CommutingVO();
        commutingVO.setMemberId(loginMemberId);
        commutingVO.setOwInsertCheck(scheduleVO.getId());
        
        String startDt = scheduleVO.getStartDt();
        String endDt = scheduleVO.getEndDt();
        
        LocalDate ld1 = new LocalDate(startDt);
        LocalDate ld2 = new LocalDate(endDt);
        int days = Days.daysBetween(ld1, ld2).getDays() + 1;

        for (int i = 0; i < days; i++) {
            String dt = ld1.plusDays(i).toString();

            commutingVO.setCommutingTm(new LocalDateTime(dt + "T"+scheduleVO.getStartTm()).toDate());
            commutingVO.setAttendYn("Y");
            commutingMapper.write(commutingVO);

            commutingVO.setCommutingTm(new LocalDateTime(dt + "T19:00").toDate());
            commutingVO.setAttendYn("N");
            commutingMapper.write(commutingVO);
        }
    }

    public void edit(ScheduleVO scheduleVO) {
        int loginMemberId = SecurityUtils.getLoginMemberId();
        scheduleVO.setUdtId(loginMemberId);
        //commuting 초기화
        owAutoDelete(scheduleVO);
        
        // 외근, 출장은 정상 출퇴근(외근시작 시간 ~ 19:00)으로 간주
        if (CODE_SCHEDULE_TYPE_OUTSIDE_WORK.equals(scheduleVO.getType())) {
        	List<String> mList = scheduleVO.getPassengers();
        	insertCommuting(scheduleVO, loginMemberId);
        	mList.stream().forEach(i -> insertCommuting(scheduleVO, Integer.parseInt(i)));
        }
        
        scheduleMapper.edit(scheduleVO);
    }
    
    public void cancel(ScheduleVO scheduleVO) {
        int loginMemberId = SecurityUtils.getLoginMemberId();
        scheduleVO.setUdtId(loginMemberId);
        scheduleVO.setStatus(Constants.CODE_STATUS_CANCEL_REQUEST);
        scheduleMapper.cancel(scheduleVO);
    }

    public void delete(ScheduleVO scheduleVO) {
        int loginMemberId = SecurityUtils.getLoginMemberId();
        scheduleVO.setUdtId(loginMemberId);
        
        owAutoDelete(scheduleVO); //삭제시 commuting에 insert된거 같이 삭제함
        
        scheduleMapper.delete(scheduleVO);
    }

 // TODO: 여기부터
    public void owAutoDelete(ScheduleVO scheduleVO) {
    	CommutingVO cvo = new CommutingVO();
    	cvo.setOwInsertCheck(scheduleVO.getId());
        commutingMapper.owAutoDelete(cvo);
    }
    
    public List<Integer> searchYear(ScheduleVO scheduleVO) {
    	return scheduleMapper.searchYear(scheduleVO);
    }
    
    public List<ScheduleVO> searchType(ScheduleVO scheduleVO) {
    	return scheduleMapper.searchType(scheduleVO);
    }
    
    public List<ScheduleVO> searching(ScheduleVO scheduleVO) {
    	return scheduleMapper.searching(scheduleVO);
    }
    
    public void writePassengers(ScheduleVO scheduleVO) {
    	 scheduleMapper.writePassengers(scheduleVO);
    }
    
    public void initPassengers(ScheduleVO scheduleVO) {
   	 scheduleMapper.initPassengers(scheduleVO);
   }
    
    public List<String> selectPassengers(ScheduleVO scheduleVO) {
      	return scheduleMapper.selectPassengers(scheduleVO);
    }
}
