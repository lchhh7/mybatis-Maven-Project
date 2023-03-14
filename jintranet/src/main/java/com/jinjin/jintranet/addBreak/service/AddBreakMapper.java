package com.jinjin.jintranet.addBreak.service;

import java.util.List;

import com.jinjin.jintranet.common.vo.AddBreakVO;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper
public interface AddBreakMapper {
	AddBreakVO monthlySelect(AddBreakVO addBreakVO);
    
    void monthlyEdit(AddBreakVO addBreakVO);
    
    Integer addBreakCount(AddBreakVO addBreakVO);
    
    List<String> allAddBreakByMember(AddBreakVO addBreakVO);
    
    List<Integer> searchYear(AddBreakVO addBreakVO);
    
    void emptyDataInsert(AddBreakVO addBreakVO);
    
    float totalAddBreakTime(AddBreakVO addBreakVO);
}
