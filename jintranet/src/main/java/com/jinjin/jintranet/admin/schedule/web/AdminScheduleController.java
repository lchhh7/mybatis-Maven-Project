package com.jinjin.jintranet.admin.schedule.web;

import com.jinjin.jintranet.admin.schedule.service.AdminScheduleService;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;
import com.jinjin.jintranet.member.service.MemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;

@Controller
@RequestMapping("/admin")
public class AdminScheduleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminScheduleController.class);

    private final AdminScheduleService scheduleService;
    
    @Autowired
    public AdminScheduleController(AdminScheduleService scheduleService) {
    	this.scheduleService = scheduleService;
    }
    /**
     * 일정신청관리(관) > 목록 페이지 이동
     */
    @RequestMapping(value = "/schedule.do", method = RequestMethod.GET)
    public String main(ModelMap map, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
        	
            map.put("members", scheduleService.findMemberAll(new MemberVO()));
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }
        return "admin-schedule/admin-schedule";
    }

    /**
     * 일정신청관리(관) > 목록 조회
     */
    @RequestMapping(value = "/schedule/search.do", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(value = "p", required = false, defaultValue = "1") int page,
            @RequestParam(value = "m", required = false) Integer m,
            @RequestParam(value = "r", required = false, defaultValue = "") String r,
            @RequestParam(value = "y", required = false, defaultValue = "") String y,
            @RequestParam(value = "n", required = false, defaultValue = "") String n,
            HttpServletRequest request) throws Exception {

        ScheduleVO scheduleVO = new ScheduleVO();
        scheduleVO.setPageIndex(page);
        scheduleVO.setSearchMemberId(m);
        scheduleVO.setSearchStatusR(r);
        scheduleVO.setSearchStatusY(y);
        scheduleVO.setSearchStatusN(n);

        loggingCurrentMethod(LOGGER, scheduleVO);
        try {
            Map<String, Object> map = scheduleService.main(scheduleVO, request);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 일정신청관리(관) > 신청내역 조회
     */
    @RequestMapping(value = "/schedule/{id}.do", method = RequestMethod.GET)
    public ResponseEntity<ScheduleVO> findById(@PathVariable("id") int id) throws Exception {
        loggingCurrentMethod(LOGGER, id);

        try {
            ScheduleVO vo = scheduleService.findById(id);
            return new ResponseEntity<>(vo, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 일정신청관리(관) > 신청내역 처리
     * status
     * R : 대기
     * Y : 승인
     * N : 비승인
     * C : 취소요청
     * D : 취소요청 승인
     * 취소요청 비승인 = Y
     */
    @RequestMapping(value = "/schedule/{id}.do", method = RequestMethod.PATCH)
    public ResponseEntity<String> approve(
            @PathVariable("id") int id,
            @RequestBody ScheduleVO scheduleVO) throws Exception {
        loggingCurrentMethod(LOGGER, id, scheduleVO);
        try {
            if (id != scheduleVO.getId()) {
                return new ResponseEntity<>("비정상적인 접근입니다.", HttpStatus.NOT_FOUND);
            }

            scheduleService.approve(scheduleVO);
            return new ResponseEntity<>("정상적으로 처리되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("처리 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    /**
     * 일정신청관리(관) > 휴가일수조회
     */
    @RequestMapping(value = "/schedule/vacationDays.do", method = RequestMethod.GET)
    public ResponseEntity<List<MemberVO>> vacationDays() throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            List<MemberVO> list = scheduleService.vacationDays();

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
        	e.printStackTrace();
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
