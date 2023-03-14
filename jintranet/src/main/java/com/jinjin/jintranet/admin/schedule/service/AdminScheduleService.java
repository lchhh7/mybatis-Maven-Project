package com.jinjin.jintranet.admin.schedule.service;

import static com.jinjin.jintranet.common.util.PageUtils.page;
import static com.jinjin.jintranet.common.util.VacationDaysUtils.getMemberVacationDays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;
import com.jinjin.jintranet.member.service.MemberMapper;
import com.jinjin.jintranet.security.SecurityUtils;

@Service
public class AdminScheduleService {
	
    private AdminScheduleMapper scheduleMapper;
    
    private MemberMapper memberMapper;

    public AdminScheduleService () {}
    
    @Autowired
    public AdminScheduleService (AdminScheduleMapper scheduleMapper,MemberMapper memberMapper) {
    	this.scheduleMapper = scheduleMapper;
    	this.memberMapper = memberMapper;
    }
    
    public List<MemberVO> findMemberAll(MemberVO memberVO) {
        return memberMapper.findAll(memberVO);
    }

    public Map<String, Object> main(ScheduleVO scheduleVO, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        scheduleVO.setApproveId(SecurityUtils.getLoginMemberId());

        int totalCnt = scheduleMapper.getScheduleCnt(scheduleVO);
        scheduleVO.setTotalCnt(totalCnt);

        List<ScheduleVO> list = scheduleMapper.findAll(scheduleVO);

        map.put("list", list);
        map.put("page", page(scheduleVO, "schedules", request));
        map.put("totalCnt", totalCnt);

        return map;
    }

    public ScheduleVO findById(int id) {
        ScheduleVO scheduleVO = new ScheduleVO(id);
        ScheduleVO vo = scheduleMapper.findById(scheduleVO);

        return vo;
    }

    public void approve(ScheduleVO scheduleVO) {
        scheduleMapper.approve(scheduleVO);
    }


    public List<MemberVO> vacationDays() throws Exception{
    	List<MemberVO> list = new ArrayList<>();
    	try {
    		
    	
        List<MemberVO> members = memberMapper.findAll(new MemberVO());

        LocalDate now = LocalDate.now();

        int year = now.getYear();
        int month = now.getMonthOfYear();
        int date = now.getDayOfMonth();

        for (MemberVO m : members) {
            list.add(getMemberVacationDays(m, year, month, date));
        }
        System.out.println(list);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        return list;
    }








}

