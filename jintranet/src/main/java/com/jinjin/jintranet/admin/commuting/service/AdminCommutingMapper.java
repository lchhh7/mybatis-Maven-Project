package com.jinjin.jintranet.admin.commuting.service;

import com.jinjin.jintranet.common.vo.CommutingRequestVO;
import com.jinjin.jintranet.common.vo.CommutingVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface AdminCommutingMapper {


    int getCommutingCnt(CommutingRequestVO commutingRequestVO);

    List<ScheduleVO> findAll(CommutingRequestVO commutingRequestVO);

    CommutingRequestVO findById(CommutingRequestVO commutingRequestVO);

    void approve(CommutingRequestVO commutingRequestVO);
    
    void approveInit(CommutingRequestVO commutingRequestVO);

    void write(CommutingVO commutingVO);

    List<CommutingVO> findOne(CommutingVO commutingVO);

    void deleteCommuting(CommutingVO commutingVO);

    void addVacations(ScheduleVO scheduleVO);
}
