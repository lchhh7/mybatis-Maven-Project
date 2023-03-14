package com.jinjin.jintranet.schedule.web;

import static com.jinjin.jintranet.common.constant.Constants.CODE_SCHEDULE_TYPE_OVERTIME_WORK;
import static com.jinjin.jintranet.common.constant.Constants.CODE_SCHEDULE_TYPE_VACATION;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;
import static com.jinjin.jintranet.common.util.VacationDaysUtils.getMemberVacationDays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinjin.jintranet.common.vo.HolidayVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;
import com.jinjin.jintranet.holiday.service.HolidayMapper;
import com.jinjin.jintranet.member.service.MemberService;
import com.jinjin.jintranet.schedule.service.ScheduleService;
import com.jinjin.jintranet.security.SecurityUtils;

@Controller
public class ScheduleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);

    private final ScheduleService scheduleService;
    
    private final MemberService memberService;
    
    private final HolidayMapper holidayMapper;
    
    @Autowired
    public ScheduleController(ScheduleService scheduleService,MemberService memberService,HolidayMapper holidayMapper) {
    	this.scheduleService = scheduleService;
    	this.memberService = memberService;
    	this.holidayMapper = holidayMapper;
    }
    /**
     * 일정관리 > 메인화면
     */
    @RequestMapping(value = "/schedule.do", method = RequestMethod.GET)
    public String main(ModelMap map, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            MemberVO memberVO = SecurityUtils.getLoginMember();
            
            
            LocalDate now = LocalDate.now();

            int year = now.getYear();
            int month = now.getMonthOfYear();
            int date = now.getDayOfMonth();

            map.put("vacationDays", getMemberVacationDays(memberVO, year, month, date));
            map.put("approves", scheduleService.findApproves());
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "schedule/schedule";
    }

    /**
     * 일정관리 > 일정 목록
     */
    @RequestMapping(value = "/schedule/search.do", method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> findScheduleAll(
            @RequestParam(value = "m", required = false, defaultValue = "") String m,
            @RequestParam(value = "sc", required = false, defaultValue = "") String sc,
            @RequestParam(value = "va", required = false, defaultValue = "") String va,
            @RequestParam(value = "ow", required = false, defaultValue = "") String ow,
            @RequestParam(value = "bt", required = false, defaultValue = "") String bt,
            @RequestParam(value = "sd") String sd,
            @RequestParam(value = "ed") String ed) throws Exception {

        Map<String, Object> map = new HashMap<>();

        ScheduleVO scheduleVO = new ScheduleVO();
        if (!"".equals(m)) scheduleVO.setSearchMemberId(Integer.parseInt(m)); 
        scheduleVO.setSearchTypeSC(sc);
        scheduleVO.setSearchTypeVA(va);
        scheduleVO.setSearchTypeOW(ow);
        scheduleVO.setSearchTypeBT(bt);
        scheduleVO.setSearchStartDt(sd);
        scheduleVO.setSearchEndDt(ed);

        HolidayVO holidayVO = new HolidayVO().builder().searchStartDt(sd).searchEndDt(ed).build();

        loggingCurrentMethod(LOGGER, scheduleVO);

        try {
            List<ScheduleVO> list = scheduleService.findAll(scheduleVO);
            List<HolidayVO> holidays = holidayMapper.findAll(holidayVO);

            map.put("list", list);
            map.put("holidays", holidays);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 일정관리 > 일정 등록
     */
    @RequestMapping(value = "/schedule.do", method = RequestMethod.POST)
    public ResponseEntity<String> write(@Validated @RequestBody ScheduleVO scheduleVO, BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, scheduleVO);
        try {
        	if (bindingResult.hasErrors()) {
             	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
             }

            if (CODE_SCHEDULE_TYPE_VACATION.equals(scheduleVO.getType())) {
                String vacationType = scheduleVO.getVacationType();
                if (!("1".equals(vacationType) || "2".equals(vacationType) || "3".equals(vacationType))) {
                    return new ResponseEntity<>("올바른 휴가 종류를 선택해주세요.", HttpStatus.BAD_REQUEST);
                }

                if (scheduleVO.getApproveId() == null || scheduleVO.getApproveId() <= 0) {
                    return new ResponseEntity<>("결제자를 선택해주세요.", HttpStatus.BAD_REQUEST);
                }
            }

            if (CODE_SCHEDULE_TYPE_OVERTIME_WORK.equals(scheduleVO.getType())) {
                if (scheduleVO.getApproveId() == null || scheduleVO.getApproveId() <= 0) {
                    return new ResponseEntity<>("결제자를 선택해주세요.", HttpStatus.BAD_REQUEST);
                }
            }

            if (!isValidDate(scheduleVO)){
                return new ResponseEntity<>("일정 종료일과 시작일을 올바르게 입력해주세요.", HttpStatus.BAD_REQUEST);
            }
            scheduleService.write(scheduleVO);
            
            List<String> mList = scheduleVO.getPassengers();
            
        	for(String i: mList) {
        		scheduleService.writePassengers(
        				new ScheduleVO().builder().scheduleId(scheduleVO.getId()).memberId(Integer.parseInt(i)).build());
        	}
        	
        	/*mList.stream().forEach(str -> scheduleService.writePassengers(
    				new ScheduleVO().builder().scheduleId(scheduleVO.getId()).memberId(Integer.parseInt(str)).build()));*/
        	
            return new ResponseEntity<>("일정을 정상적으로 등록했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("일정 등록 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    /**
     * 일정관리 > 일정 선택
     */
    @RequestMapping(value = "/schedule/{id}.do", method = RequestMethod.GET)
    public ResponseEntity<ScheduleVO> findById(@PathVariable("id") Integer id) throws Exception {
        loggingCurrentMethod(LOGGER, id);

        try {
        	/*선택된 동석자 리스트*/
        	List<String> pList = scheduleService.selectPassengers(new ScheduleVO().builder().scheduleId(id).build());
            ScheduleVO scheduleVO = scheduleService.findById(new ScheduleVO(id));
            scheduleVO.setPassengers(pList);
            return new ResponseEntity<>(scheduleVO, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 일정관리 > 일정 수정
     */
    @RequestMapping(value = "/schedule/{id}.do", method = RequestMethod.PATCH)
    public ResponseEntity<String> edit(@PathVariable("id") Integer id,@Validated @RequestBody ScheduleVO scheduleVO, BindingResult bindingResult ) throws Exception {
        loggingCurrentMethod(LOGGER, id, scheduleVO);

        try {
        	
        	 if (bindingResult.hasErrors()) {
             	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
             }
        	
            ScheduleVO scheduleById = scheduleService.findById(new ScheduleVO(id));
            if (!"R".equals(scheduleById.getStatus())) {
                return new ResponseEntity<>("대기중인 일정만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST);
            }
            if (!isValidDate(scheduleVO)){
                return new ResponseEntity<>("일정 종료일과 시작일을 올바르게 입력해주세요.", HttpStatus.BAD_REQUEST);
            }
            
            scheduleVO.setScheduleId(id);
            scheduleService.owAutoDelete(scheduleVO);
            scheduleService.initPassengers(scheduleVO);
            
            List<String> mList = scheduleVO.getPassengers();
            
        	for( String s : mList) {
        		ScheduleVO svo = new ScheduleVO().builder().scheduleId(scheduleVO.getId()).memberId(Integer.parseInt(s)).build();
        		scheduleService.writePassengers(svo);
        	}
        	
            scheduleVO.setId(id);
            scheduleService.edit(scheduleVO);
            return new ResponseEntity<>("일정을 정상적으로 수정했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("일정 수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }
    /**
     * 일정관리 > 일정 취소요청
     */
    @RequestMapping(value = "/schedule/cancel/{id}.do", method = RequestMethod.PATCH)
    public ResponseEntity<String> cancel(@PathVariable("id") Integer id) throws Exception {
        loggingCurrentMethod(LOGGER, id);

        try {
            ScheduleVO scheduleVO = scheduleService.findById(new ScheduleVO(id));
            if (scheduleVO == null) { return new ResponseEntity<>("유효하지 않은 일정입니다.", HttpStatus.BAD_REQUEST);
            }
            if (!"Y".equals(scheduleVO.getStatus())) {
                return new ResponseEntity<>("승인 상태인 일정만 취소요청 할 수 있습니다.", HttpStatus.BAD_REQUEST);
            }
            if (!isValidDate(scheduleVO)){
                return new ResponseEntity<>("일정 종료일과 시작일을 올바르게 입력해주세요.", HttpStatus.BAD_REQUEST);
            }

            LocalDate startDt = new LocalDate(scheduleVO.getStartDt());
            LocalDate now = LocalDate.now();
            if (now.isAfter(startDt)) {
                return new ResponseEntity<>("지난 일정은 취소요청 할 수 없습니다.", HttpStatus.BAD_REQUEST);
            }
            
            scheduleService.cancel(scheduleVO);
            return new ResponseEntity<>("일정을 정상적으로 취소 요청했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("일정 취소요청 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    /**
     * 일정관리 > 일정 삭제
     */
    @RequestMapping(value = "/schedule/{id}.do", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable("id") Integer id) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        
        try {
            scheduleService.delete(new ScheduleVO(id));
            scheduleService.initPassengers(new ScheduleVO().builder().id(id).scheduleId(id).build());
            return new ResponseEntity<>("일정을 정상적으로 삭제했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("일정 삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }
    
    /**
     * 전체 동행자 목록
     */
    @RequestMapping(value = "/schedule/passengers.do", method = RequestMethod.GET)
    public ResponseEntity<List<MemberVO>> passengerList() throws Exception {
        	List<MemberVO> mList = memberService.findAllPassenger(new MemberVO());
            return new ResponseEntity<>(mList , HttpStatus.OK);
    }
    
    private boolean isValidDate(ScheduleVO scheduleVO) {
        LocalDate startDt = new LocalDate(scheduleVO.getStartDt());
        LocalDate endDt = new LocalDate(scheduleVO.getEndDt());

        if (startDt.isAfter(endDt)) return false;

        return true;
    }
}
