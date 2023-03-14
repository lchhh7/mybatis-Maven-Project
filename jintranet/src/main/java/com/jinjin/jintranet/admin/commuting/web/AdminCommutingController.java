package com.jinjin.jintranet.admin.commuting.web;

import com.jinjin.jintranet.addBreak.service.AddBreakService;
import com.jinjin.jintranet.admin.commuting.service.AdminCommutingService;
import com.jinjin.jintranet.common.vo.AddBreakVO;
import com.jinjin.jintranet.common.vo.CommutingRequestVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.commuting.service.CommutingCalculateService;
import com.jinjin.jintranet.commuting.service.CommutingRequestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;

@Controller
@RequestMapping("/admin")
public class AdminCommutingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminCommutingController.class);
    
    private final AddBreakService addBreakService;
    
    private final AdminCommutingService commutingService;
    
    private CommutingCalculateService calculateService;
    
    @Autowired
    public AdminCommutingController(AddBreakService addBreakService,AdminCommutingService commutingService,CommutingCalculateService calculateService){
    	this.addBreakService = addBreakService;
    	this.commutingService = commutingService;
    	this.calculateService = calculateService;
    }
    /**
     * 근태수정관리(관) > 목록 페이지 이동
     */
    @RequestMapping(value = "/commuting.do", method = RequestMethod.GET)
    public String main(ModelMap map, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            map.put("members", commutingService.findMemberAll(new MemberVO()));
            map.put("yearList", addBreakService.searchYear(new AddBreakVO()));
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }
        return "admin-commuting/admin-commuting";
    }

    /**
     * 근태수정관리(관) > 목록 조회
     */
    @RequestMapping(value = "/commuting/search.do", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(value = "p", required = false, defaultValue = "1") int page,
            @RequestParam(value = "m", required = false) Integer m,
            @RequestParam(value = "r", required = false, defaultValue = "") String r,
            @RequestParam(value = "y", required = false, defaultValue = "") String y,
            @RequestParam(value = "n", required = false, defaultValue = "") String n,
            HttpServletRequest request) throws Exception {

        CommutingRequestVO commutingRequestVO = new CommutingRequestVO();
        commutingRequestVO.setPageIndex(page);
        commutingRequestVO.setSearchMemberId(m);
        commutingRequestVO.setSearchStatusR(r);
        commutingRequestVO.setSearchStatusY(y);
        commutingRequestVO.setSearchStatusN(n);

        loggingCurrentMethod(LOGGER, commutingRequestVO);
        try {
            Map<String, Object> map = commutingService.main(commutingRequestVO, request);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/commuting/{id}.do", method = RequestMethod.GET)
    public ResponseEntity<CommutingRequestVO> findById(@PathVariable("id") int id) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        		
        CommutingRequestVO commutingRequestVO = commutingService.findById(new CommutingRequestVO(id));
        
        commutingRequestVO.setRequestId(id);
        CommutingRequestVO timeVO = calculateService.findByRequestId(commutingRequestVO);
        
        if(timeVO != null){
        	commutingRequestVO.setStartTm(timeVO.getStartTm());
        	commutingRequestVO.setEndTm(timeVO.getEndTm());
        	commutingRequestVO.setPureWorkTm(timeVO.getPureWorkTm());
        }
        
        try {
            return new ResponseEntity<>(commutingRequestVO, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 근태신청관리(관) > 출,퇴근신청 승인 , 이전 달 추가휴가 업데이트
     */
    @RequestMapping(value = "/commuting/{id}.do", method = RequestMethod.PATCH)
    public ResponseEntity<String> approve(
            @PathVariable("id") int id,
            @RequestBody CommutingRequestVO commutingRequestVO) throws Exception {
        loggingCurrentMethod(LOGGER, id, commutingRequestVO);
        try {
            if (id != commutingRequestVO.getId()) {
                return new ResponseEntity<>("비정상적인 접근입니다.", HttpStatus.NOT_FOUND);
            }
            commutingService.approve(commutingRequestVO);
            
            commutingRequestVO.setRequestId(id);
            calculateService.approve(commutingRequestVO);
            //0802 lch update
            
            return new ResponseEntity<>("정상적으로 처리되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("처리 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }
    
    @RequestMapping(value = "/commuting/approveInit.do", method = RequestMethod.PATCH)
    public ResponseEntity<String> approveInit( @RequestBody CommutingRequestVO commutingRequestVO) {
    	try {
    		commutingService.approveInit(commutingRequestVO);
    		
    		commutingRequestVO.setRequestId(commutingRequestVO.getId());
    		calculateService.approveInit(commutingRequestVO);
    		
    		return new ResponseEntity<>("정상적으로 처리되었습니다",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("처리 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
		}
    }
}
