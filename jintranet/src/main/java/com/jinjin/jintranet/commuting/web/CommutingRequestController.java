package com.jinjin.jintranet.commuting.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinjin.jintranet.addBreak.service.AddBreakService;
import com.jinjin.jintranet.common.util.EmailUtils;
import com.jinjin.jintranet.common.vo.AddBreakVO;
import com.jinjin.jintranet.common.vo.CommutingRequestVO;
import com.jinjin.jintranet.commuting.service.CommutingCalculateService;
import com.jinjin.jintranet.commuting.service.CommutingRequestService;
import com.jinjin.jintranet.security.SecurityUtils;

@Controller
public class CommutingRequestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommutingRequestController.class);
    
    @Autowired
    private CommutingRequestService requestService;
    
    @Autowired
    private CommutingCalculateService calculateService;
    
    @Autowired
    private AddBreakService addBreakService;
    
  //근태 내용 등록 
    @RequestMapping(value = "/commuting/writeCommute.do", method = RequestMethod.POST)
    public ResponseEntity<String> writeCommute(@Validated @RequestBody CommutingRequestVO commutingRequestVO, BindingResult bindingResult) {
    	try {
    		if(bindingResult.hasErrors()) {
    			return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    		}
            
            EmailUtils.commutingEmail(commutingRequestVO);
            
            requestService.writeCommute(commutingRequestVO);
            
            if("O".equals(commutingRequestVO.getType())) {
            	List<CommutingRequestVO> crvo = calculateService.recodeSearch(commutingRequestVO);
            	if(crvo.size() != 0) {
	            	for(int i=0;
	            			i<crvo.size() ;i++) {
	            		calculateService.deleteRequest(new CommutingRequestVO(crvo.get(i).getId()));
	            	}
            	}
            	calculateService.write(commutingRequestVO);
            }
    		return new ResponseEntity<>("근태등록 신청을 정상적으로 등록했습니다.", HttpStatus.OK);
    	} catch (Exception e) {
    		loggingStackTrace(e, LOGGER);
    		return new ResponseEntity<>("근태등록 신청 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
    	}
    }
    
    
    //근태 내용 수정
    @RequestMapping(value = "/commuting/editCommute.do", method = RequestMethod.POST)
    public ResponseEntity<String> editCommute(@Validated @RequestBody CommutingRequestVO commutingRequestVO,BindingResult bindingResult) throws Exception {
    	try {
    		if(bindingResult.hasErrors()) {
    			return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(),HttpStatus.BAD_REQUEST);
    		}
    		
            requestService.editCommute(commutingRequestVO);
          //addBreakService.monthlyEdit();
    		return new ResponseEntity<>("누락된 근태를 정상적으로 등록했습니다.", HttpStatus.OK);
    	} catch (Exception e) {
    		loggingStackTrace(e, LOGGER);
    		return new ResponseEntity<>("근태등록 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
    	}
    }
    
  //근태 요청을 수정
    @RequestMapping(value = "/commuting/editRequest/{id}.do", method = RequestMethod.POST)
    public ResponseEntity<String> editRequest(@PathVariable("id") Integer id , @RequestBody CommutingRequestVO commutingRequestVO) throws Exception {
    	try {
    		commutingRequestVO.setId(id);
            requestService.editRequest(commutingRequestVO);
            
            commutingRequestVO.setRequestId(id);
            
            calculateService.edit(commutingRequestVO);
    		return new ResponseEntity<>("근태요청를 정상적으로 수정했습니다.", HttpStatus.OK);
    	} catch (Exception e) {
    		loggingStackTrace(e, LOGGER);
    		return new ResponseEntity<>("근태요청수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
    	}
    }
    
    //근태 요청을 삭제
    @RequestMapping(value = "/commuting/deleteRequest/{id}.do", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteRequest(@PathVariable("id") Integer id) throws Exception {
    	try {
    		calculateService.deleteCascadeRequest(new CommutingRequestVO().builder().requestId(id).build());
            requestService.deleteRequest(new CommutingRequestVO(id));
    		return new ResponseEntity<>("근태요청를 정상적으로 삭제했습니다.", HttpStatus.OK);
    	} catch (Exception e) {
    		loggingStackTrace(e, LOGGER);
    		return new ResponseEntity<>("근태요청삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
    	}
    }
    
    @RequestMapping(value = "/commuting/request/{id}.do", method = RequestMethod.GET)
    public ResponseEntity<CommutingRequestVO> findById(@PathVariable("id") Integer id) throws Exception {
        loggingCurrentMethod(LOGGER, id);

        try {
            CommutingRequestVO commutingRequestVO = requestService.findById(new CommutingRequestVO(id));
            
            commutingRequestVO.setRequestId(id);
            CommutingRequestVO timeVO = calculateService.findByRequestId(commutingRequestVO);
            
            if(timeVO != null){
            	commutingRequestVO.setStartTm(timeVO.getStartTm());
            	commutingRequestVO.setEndTm(timeVO.getEndTm());
            	commutingRequestVO.setPureWorkTm(timeVO.getPureWorkTm());
            }
            return new ResponseEntity<>(commutingRequestVO, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    @RequestMapping(value = "/commuting/searching.do", method = RequestMethod.GET)
    public ResponseEntity<List<CommutingRequestVO>> searching(
    		@RequestParam(value ="st", required = false , defaultValue ="") String st, 
    		@RequestParam(value ="y", required = false , defaultValue ="") String y
    		) throws Exception {
    	loggingCurrentMethod(LOGGER, st, y);
        try {
            CommutingRequestVO commutingRequestVO =  new CommutingRequestVO();
            commutingRequestVO.setSearchType(st);
            if(!isBlank(y)) {
            	commutingRequestVO.setSearchStartDt(y+"-01-01");
            	commutingRequestVO.setSearchEndDt(y+"-12-31");
            }
            commutingRequestVO.setSearchMemberId(SecurityUtils.getLoginMemberId());
            return new ResponseEntity<>(requestService.searching(commutingRequestVO), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    @RequestMapping(value ="/commuting/checkCommuting.do" , method = RequestMethod.POST)
    public ResponseEntity<Boolean> alreadyDoneCommutingFunction(@RequestBody CommutingRequestVO commutingRequestVO) {
    	try {
    	List<String> findOvertimeByDay = requestService.alreadyDoneCheckCommuting(commutingRequestVO);
    		return new ResponseEntity<>(findOvertimeByDay.isEmpty() , HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
    }
}
