package com.jinjin.jintranet.admin.schedule.service;

import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface AdminScheduleMapper {
    int getScheduleCnt(ScheduleVO scheduleVO);

    List<ScheduleVO> findAll(ScheduleVO scheduleVO);

    ScheduleVO findById(ScheduleVO memberVO);

    void approve(ScheduleVO scheduleVO);


    Integer getTotalVacationDays(MemberVO memberVO);
}
