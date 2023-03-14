package com.jinjin.jintranet.commuting.service;

import java.util.List;

import com.jinjin.jintranet.common.vo.CommutingRequestVO;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper
public interface CommutingCalculateMapper {

    void write(CommutingRequestVO commutingRequestVO);
    
    void approve(CommutingRequestVO commutingRequestVO);
    
    void approveInit(CommutingRequestVO commutingRequestVO);
    
    void edit(CommutingRequestVO commutingRequestVO);
    
    void delete(CommutingRequestVO commutingRequestVO);
    
    void deleteCascade(CommutingRequestVO commutingRequestVO);
    
    List<CommutingRequestVO> search(CommutingRequestVO commutingRequestVO);
    
    List<CommutingRequestVO> recodeSearch(CommutingRequestVO commutingRequestVO);
    
    List<CommutingRequestVO> detailSearchByExcel(CommutingRequestVO commutingRequestVO);
    
    CommutingRequestVO findByRequestId(CommutingRequestVO commutingRequestVO);
}
