package com.jinjin.jintranet.addBreak.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.common.vo.AddBreakVO;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AddBreakService {
	
	private AddBreakMapper addBreakMapper;
	
	@Autowired
	public AddBreakService(AddBreakMapper addBreakMapper) {
		this.addBreakMapper = addBreakMapper;
	}
	
	public AddBreakVO monthlySelect(AddBreakVO addBreakVO) {
		return addBreakMapper.monthlySelect(addBreakVO);
	}
	
	public void monthlyEdit(AddBreakVO addBreakVO) {
		addBreakMapper.monthlyEdit(addBreakVO);
	}
	
	public Integer addBreakCount(AddBreakVO addBreakVO) {
		return addBreakMapper.addBreakCount(addBreakVO);
	}
	
	public List<String> allAddBreakByMember(AddBreakVO addBreakVO) {
		return addBreakMapper.allAddBreakByMember(addBreakVO);
	}
	
	public List<Integer> searchYear(AddBreakVO addBreakVO) {
		return addBreakMapper.searchYear(addBreakVO);
	}
	
	public void emptyDataInsert(AddBreakVO addBreakVO) {
		addBreakMapper.emptyDataInsert(addBreakVO);
	}
	
	public float totalAddBreakTime(AddBreakVO addBreakVO) {
		return addBreakMapper.totalAddBreakTime(addBreakVO);
	}
}