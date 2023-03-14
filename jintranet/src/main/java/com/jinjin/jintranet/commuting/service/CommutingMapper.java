package com.jinjin.jintranet.commuting.service;

import com.jinjin.jintranet.common.vo.CommutingVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface CommutingMapper {

    List<CommutingVO> getWorkingTime(CommutingVO commutingVO);

    List<CommutingVO> getQuittingTime(CommutingVO commutingVO);

    List<CommutingVO> getAllNightTime(CommutingVO commutingVO);

    CommutingVO findById(CommutingVO commutingVO);
    
    String goToWorkTime(CommutingVO cvo);
	
	String offToWorkTime(CommutingVO cvo);
	
	String workingStatus(CommutingVO cvo);
	
	void goToWorkButton(CommutingVO cvo);

    void write(CommutingVO commutingVO);
    
    Integer commutingExistsCheck(CommutingVO commutingVO);
    
    void owAutoDelete(CommutingVO commutingVO);
    
    CommutingVO workingTimeByMemberIdAndRequestDt(CommutingVO commutingVO);

    CommutingVO quittingTimeByMemberIdAndRequestDt(CommutingVO commutingVO);
}
