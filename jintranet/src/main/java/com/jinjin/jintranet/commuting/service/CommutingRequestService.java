package com.jinjin.jintranet.commuting.service;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.common.constant.Constants;
import com.jinjin.jintranet.common.vo.CommutingRequestVO;
import com.jinjin.jintranet.security.SecurityUtils;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
public class CommutingRequestService {
	
	private CommutingRequestMapper requestMapper;
	
	@Autowired
	public CommutingRequestService(CommutingRequestMapper requestMapper) {
		this.requestMapper = requestMapper;
	}
	

	public void writeAddBreak(CommutingRequestVO commutingRequestVO) {
        int id = SecurityUtils.getLoginMemberId();
        
        commutingRequestVO.setType(Constants.CODE_COMMUTING_REQUEST_TYPE_ADD);
        
        commutingRequestVO.setCrtId(id);
        commutingRequestVO.setUdtId(id);
        
        requestMapper.writeAddBreak(commutingRequestVO);
    }
	
	public void writeCommute(CommutingRequestVO commutingRequestVO) {
        int id = SecurityUtils.getLoginMemberId();
        
        commutingRequestVO.setMemberId(id);
        commutingRequestVO.setCrtId(id);
        commutingRequestVO.setUdtId(id);
        
        requestMapper.writeCommute(commutingRequestVO);
    }
	
	public void editCommute(CommutingRequestVO commutingRequestVO) {
		int id = SecurityUtils.getLoginMemberId();
		
		commutingRequestVO.setMemberId(id);
        commutingRequestVO.setCrtId(id);
        commutingRequestVO.setUdtId(id);
        requestMapper.editCommute(commutingRequestVO);
    }
	
	public void editRequest(CommutingRequestVO commutingRequestVO) {
		int id = SecurityUtils.getLoginMemberId();
		
        commutingRequestVO.setUdtId(id);
        requestMapper.editRequest(commutingRequestVO);
    }
	
	public void deleteRequest(CommutingRequestVO commutingRequestVO) {
        requestMapper.deleteRequest(commutingRequestVO);
    }
	
	public Integer checkInsertAddBreakByMonth(CommutingRequestVO commutingRequestVO) {
		return requestMapper.checkInsertAddBreakByMonth(commutingRequestVO);
	}
	
    public CommutingRequestVO getNearRequest() {
        CommutingRequestVO requestVO = 
        		new CommutingRequestVO().builder()
        		.searchMemberId(SecurityUtils.getLoginMemberId())
        		.searchStartDt(LocalDate.now().toString()).build();

        List<CommutingRequestVO> list = requestMapper.findAllOrderByCrtDtDesc(requestVO);

        if (list.size() == 0) {
            requestVO.setContent("신청근태가 없습니다");
        } else {
            requestVO = list.get(0);
            requestVO.setContent(requestVO.getRequestDt().replaceAll("-", ".") + "신청근태");
        }

        return requestVO;
    }
    
    public CommutingRequestVO findById(CommutingRequestVO commutingRequestVO) {
    	return requestMapper.findById(commutingRequestVO);
    }
    
    public List<Integer> searchYear(CommutingRequestVO commutingRequestVO) {
    	return requestMapper.searchYear(commutingRequestVO);
    }
    
    public List<CommutingRequestVO> searchType(CommutingRequestVO commutingRequestVO) {
    	return requestMapper.searchType(commutingRequestVO);
    }
    
    public List<CommutingRequestVO> searching(CommutingRequestVO commutingRequestVO) {	
    	return requestMapper.searching(commutingRequestVO);
    }
    
    public List<String> alreadyDoneCheckCommuting(CommutingRequestVO commutingRequestVO) {
    	int id = SecurityUtils.getLoginMemberId();
        
        commutingRequestVO.setSearchMemberId(id);
    	return requestMapper.alreadyDoneCheckCommuting(commutingRequestVO);
    }
}