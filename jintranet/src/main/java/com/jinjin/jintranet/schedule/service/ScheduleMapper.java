package com.jinjin.jintranet.schedule.service;

import com.jinjin.jintranet.common.vo.ScheduleVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface ScheduleMapper {
    ScheduleVO findForMain(ScheduleVO scheduleVO);

    Integer getAddVacationDays(ScheduleVO scheduleVO);

    List<ScheduleVO> findFullScheduleByMemberIdAndStartDtAndEndDt(ScheduleVO scheduleVO);

    List<ScheduleVO> findHalfScheduleByMemberIdAndStartDtAndEndDt(ScheduleVO scheduleVO);

    List<ScheduleVO> findAll(ScheduleVO scheduleVO);

    ScheduleVO findById(ScheduleVO scheduleVO);

    Integer write(ScheduleVO scheduleVO);

    void edit(ScheduleVO scheduleVO);

    void cancel(ScheduleVO scheduleVO);

    void delete(ScheduleVO scheduleVO);

    List<ScheduleVO> findAllForCommuting(ScheduleVO scheduleVO);
    //lch
    List<ScheduleVO> findBusinessTripForCommuting(ScheduleVO scheduleVO);
    //lch
    List<ScheduleVO> findOtherScheduleExceptOvertime(ScheduleVO scheduleVO);
	//lch
    List<Integer> searchYear(ScheduleVO scheduleVO);
  //lch
    List<ScheduleVO> searchType(ScheduleVO scheduleVO);
    //lch
    List<ScheduleVO> searching(ScheduleVO scheduleVO);
    
    List<ScheduleVO> todayBreak(ScheduleVO scheduleVO);
    
    void writePassengers(ScheduleVO scheduleVO);
    
    void initPassengers(ScheduleVO scheduleVO);
    
    List<String> selectPassengers(ScheduleVO scheduleVO);
}
