package com.jinjin.jintranet.commuting.service;

import com.jinjin.jintranet.common.vo.CommutingRequestVO;
import com.jinjin.jintranet.common.vo.CommutingVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface CommutingRequestMapper {

    void writeAddBreak(CommutingRequestVO CommutingRequestVO);
    
    void writeCommute(CommutingRequestVO CommutingRequestVO);
    
    void editCommute(CommutingRequestVO CommutingRequestVO);
    
    void editRequest(CommutingRequestVO CommutingRequestVO);
    
    void deleteRequest(CommutingRequestVO CommutingRequestVO);
    
    Integer checkInsertAddBreakByMonth(CommutingRequestVO CommutingRequestVO);

    List<CommutingRequestVO> findAll(CommutingRequestVO requestVO);

    List<CommutingRequestVO> findAllOrderByCrtDtDesc(CommutingRequestVO requestVO);
    
    CommutingRequestVO findById(CommutingRequestVO requestVO);
    
    List<Integer> searchYear(CommutingRequestVO requestVO);
    
    List<CommutingRequestVO> searchType(CommutingRequestVO requestVO);
    
    List<CommutingRequestVO> searching(CommutingRequestVO requestVO);
    
    List<String> findOvertimeByDay(CommutingRequestVO requestVO);
    
    List<String> alreadyDoneCheckCommuting(CommutingRequestVO requestVO);
}
