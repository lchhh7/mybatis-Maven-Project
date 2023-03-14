package com.jinjin.jintranet.commuting.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import com.jinjin.jintranet.addBreak.service.AddBreakService;
import com.jinjin.jintranet.common.util.DownUtils;
import com.jinjin.jintranet.common.util.VacationDaysUtils;
import com.jinjin.jintranet.common.vo.AddBreakVO;
import com.jinjin.jintranet.common.vo.CommutingRequestVO;
import com.jinjin.jintranet.common.vo.CommutingVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;
import com.jinjin.jintranet.commuting.service.CommutingCalculateService;
import com.jinjin.jintranet.commuting.service.CommutingRequestService;
import com.jinjin.jintranet.commuting.service.CommutingService;
import com.jinjin.jintranet.member.service.MemberService;
import com.jinjin.jintranet.schedule.service.ScheduleService;
import com.jinjin.jintranet.security.SecurityUtils;

@Controller
public class CommutingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommutingController.class);
    
    private final MemberService memberService;
    
    private final ScheduleService scheduleService;

    private final CommutingService commutingService;

    private final CommutingRequestService requestService;
    
    private final AddBreakService addBreakService;
    
    private final CommutingCalculateService calculateService;
    
    @Autowired
    public CommutingController(MemberService memberService,ScheduleService scheduleService, CommutingService commutingService,
    		CommutingRequestService requestService,AddBreakService addBreakService,CommutingCalculateService calculateService) {
    	this.memberService = memberService;
    	this.scheduleService = scheduleService;
    	this.commutingService = commutingService;
    	this.requestService = requestService;
    	this.addBreakService = addBreakService;
    	this.calculateService = calculateService;
    }
    /**
     * 근태관리 > 메인페이지
     */
    @RequestMapping(value = "/commuting.do", method = RequestMethod.GET)
    public String main(ModelMap map, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            map.putAll(commutingService.getWorkTime());             // 출퇴근 시간, 상태
            map.put("approves", scheduleService.findApproves());    // 결재자
            map.put("typeList", requestService.searchType(new CommutingRequestVO()));
            map.put("yearList", requestService.searchYear(new CommutingRequestVO()));
            map.putAll(getDefaultMenu(request));                    // 기본 메뉴
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "commuting/commuting";
    }

    
    /**
     * 근태관리 > 목록 조회
     */
    @RequestMapping(value = "/commuting/search.do", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findScheduleAll(
            @RequestParam(value = "sd") String sd) throws Exception {
        loggingCurrentMethod(LOGGER, sd);
        Map<String, Object> map = new HashMap<>();
        try {
            LocalDate now = LocalDate.now();
            LocalDate ld = new LocalDate(sd);

            if (ld.getDayOfMonth() > 1) {
                ld = ld.plusMonths(1);
            }

            int year = ld.getYear();
            int month = ld.getMonthOfYear();

            LocalDate baseDt = new LocalDate(year, month, 1);
            String startDt = baseDt.toString(); // ld랑 같음
            String endDt = (month == now.getMonthOfYear()) ?
                    now.plusDays(1).toString() :
                    baseDt.dayOfMonth().withMaximumValue().plusDays(1).toString();

            map.putAll(commutingService.findAll(startDt, endDt , SecurityUtils.getLoginMemberId()));
            map.put("nearRequest", requestService.getNearRequest());
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    
    //추가휴가 갯수 조회
    @RequestMapping(value = "/commuting/addBreak/search.do", method = RequestMethod.GET)
    public ResponseEntity<String> addBreakCount(
            @RequestParam(value = "m") int m) throws Exception {
        loggingCurrentMethod(LOGGER, m);
        Map<String, Object> map = new HashMap<>();
        try {
            LocalDate now = LocalDate.now();

            LocalDate baseDt = new LocalDate(now.getYear(), m, 1);
            String startDt = baseDt.toString();
            String endDt = baseDt.dayOfMonth().withMaximumValue().plusDays(1).toString();

            CommutingVO commutingVO = new CommutingVO();
            commutingVO.setSearchStartDt(startDt);
            commutingVO.setSearchEndDt(endDt);

            int checkNumber = requestService.checkInsertAddBreakByMonth(CommutingRequestVO.builder().memberId(SecurityUtils.getLoginMemberId()).requestDt(startDt).build());
            if (checkNumber == 0) {
                int a = (Integer) commutingService.findAll(startDt, endDt ,SecurityUtils.getLoginMemberId()).get("availableCnt");
                return new ResponseEntity<>(Integer.toString(a), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Integer.toString(0), HttpStatus.OK);
            }
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 일정관리 > 일정 선택
     */
    @RequestMapping(value = "/commuting/{id}.do", method = RequestMethod.GET)
    public ResponseEntity<CommutingVO> findById(@PathVariable("id") Integer id) throws Exception {
        loggingCurrentMethod(LOGGER, id);

        try {
            CommutingVO commutingVO = commutingService.findById(new CommutingVO(id));
            return new ResponseEntity<>(commutingVO, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }


    @RequestMapping(value = "/individualOvertimeByExcel.do", method = RequestMethod.POST)
	@ResponseBody
	public void individualOvertimeByExcel(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CommutingVO cvo) {
		Map<String, List<String>> resultList = new HashMap<>();
		LOGGER.info("## CommutingController.individualOvertimeByExcel()");
		try {
			LocalDate now = LocalDate.now();
			LocalDate baseDt = new LocalDate(now.getYear(), cvo.getMonth(), 1);

			int month = cvo.getMonth(); // 현재 보고 있는 달

			String startDt = baseDt.toString();
			String endDt = baseDt.dayOfMonth().withMaximumValue().plusDays(1).toString();

			List<CommutingVO> overtimes = (List<CommutingVO>) commutingService.findAll(startDt, endDt , SecurityUtils.getLoginMemberId()).get("overtimes");
			List<String> overtimesTm = new LinkedList<>();
			List<String> overtimesDt = new LinkedList<>();
			
			for (CommutingVO vo : overtimes) {
				overtimesTm.add(vo.getTm());
				overtimesDt.add(vo.getDt());
			}
			
			resultList.put("TmList", overtimesTm);
			resultList.put("dtList", overtimesDt);
			commutingService.xlsxWritter(resultList, month); // 생성

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    @RequestMapping(value = "/currentByExcelWriter.do", method = RequestMethod.POST)
	@ResponseBody
	public void currentByExcelWriter(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CommutingVO cvo) {
		Map<String, List<String>> resultList = new HashMap<>();
		LOGGER.info("## CommutingController.currentByExcelWriter()");
		try {
			LocalDate now = LocalDate.now();
			int month = now.getMonthOfYear(); // 현재 보고 있는 달
			
			boolean checkYear = cvo.getSelectedYear() == now.getYear() ? false : true;
			String breakResult ="";
			
			month = (cvo.getSelectedYear() != now.getYear() ? 12 : month); //과거기록이면 모든달 다보여야됨
			
			
			List<String> allTmList = new LinkedList<>();
			List<String> overtimesTm = new LinkedList<>();
			List<String> overtimesDt = new LinkedList<>();
			
				MemberVO mvo = 
						VacationDaysUtils.getMemberVacationDays(SecurityUtils.getLoginMember(),
								cvo.getSelectedYear(), month, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
				
				breakResult =  "("+ mvo.getTotal() + "+" + mvo.getAdd() + "-" +mvo.getUse() +")" +" => " + (mvo.getTotal()+mvo.getAdd()-mvo.getUse());
			for(int j=1; j<=month ;j++) {
				LocalDate baseDt = new LocalDate(cvo.getSelectedYear(), j, 1);
	
	
				String startDt = baseDt.toString();
				String endDt = baseDt.dayOfMonth().withMaximumValue().plusDays(1).toString();
				
				List<CommutingVO> overtimes = (List<CommutingVO>) commutingService.findAll(startDt, endDt , SecurityUtils.getLoginMemberId()).get("overtimes");
				String allTm = (String) commutingService.findAll(startDt, endDt , SecurityUtils.getLoginMemberId()).get("allTm");
				allTmList.add(allTm);
				if(overtimes == null) {
					continue;
				}
				
				for (CommutingVO vo: overtimes) {
					overtimesTm.add(vo.getTm());
					overtimesDt.add(vo.getDt());
					
				}
			}
			resultList.put("allTmList", overtimesTm);
			resultList.put("allDtList", overtimesDt);
			resultList.put("allTm", allTmList);
			commutingService.xlsxWritterByYear(resultList,breakResult,checkYear); // 생성

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	@RequestMapping(value = "/adminOvertimeByExcel.do", method = RequestMethod.POST)
	@ResponseBody
	public void adminOvertimeByExcel(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CommutingVO cvo) {
		Map<String,Map<String,List>> resultMap = new HashMap<>();
		Map<String,List> result = new HashMap<>();
		LOGGER.info("## CommutingController.adminOvertimeByExcel()");
		try {
			int month = cvo.getMonth(); // 현재 달
			
			month = (cvo.getSelectedYear() != LocalDate.now().getYear() ? 12 : month); //과거기록이면 모든달 다보여야됨
			
			List<MemberVO> memberList = memberService.findAll(new MemberVO());
			List<List<String>> totalOvertimeList = new LinkedList<>();
			List<String> breakResultList = new LinkedList<>();
			String breakResult ="";
			
			for(MemberVO vo : memberList) {
				List<String> overtimeByMember = addBreakService.allAddBreakByMember(AddBreakVO.builder().memberId(vo.getId())
						.year(Integer.toString(cvo.getSelectedYear())).build());
				if(overtimeByMember != null) {
					totalOvertimeList.add(overtimeByMember);
				}
				
					MemberVO mvo = 
							VacationDaysUtils.getMemberVacationDays(vo, cvo.getSelectedYear(), month, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
					/*MemberVO mvo = 
							VacationDaysUtils.getMemberVacationDays(memberList.get(i), cvo.getSelectedYear(), 12, 31);
					*/
					breakResult =  "("+ mvo.getTotal() + "+" + mvo.getAdd() + "-" +mvo.getUse() +")" +" => " + (mvo.getTotal()+mvo.getAdd()-mvo.getUse());
					breakResultList.add(breakResult);
			}
			for(int i=1 ; i<=month ; i++) {
				
				LocalDate baseDt = new LocalDate(cvo.getSelectedYear(), i, 1);
				String startDt = baseDt.toString();
				String endDt = baseDt.plusMonths(1).minusDays(1).toString();
				result.put("detailOverTimeList"+i, calculateService.detailSearchByExcel(CommutingRequestVO.builder().searchStartDt(startDt).searchEndDt(endDt).build()));
			}
			result.put("memberList", memberList);
			result.put("totalOvertimeList", totalOvertimeList);
			result.put("breakResultList", breakResultList);
			resultMap.put("result", result);
			
			
			commutingService.commutingXlsxWritter(resultMap,cvo.getSelectedYear(),month); // 생성
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/downLoadmFile")
	public void downLoadmFile(@RequestParam(value = "month") int month , HttpServletRequest request, HttpServletResponse response, ModelMap model)
			 {
		String path = "C:\\ExcelDown\\"; // Link의 자바파일에서 excel 파일이 생성된 경로
		String realFileNm = month + "월 야근기록" + ".xlsx";
		try {
			commonExcel(path, realFileNm, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/downLoadyFile")
	public void downLoadyFile(@RequestParam(value = "month") int month , HttpServletRequest request, HttpServletResponse response, ModelMap model)
			 {
		String path = "C:\\ExcelDown\\"; // Link의 자바파일에서 excel 파일이 생성된 경로
		String realFileNm = "잔업기록-"+SecurityUtils.getLoginMemberName()+".xlsx";
		try {
			commonExcel(path, realFileNm, request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		@RequestMapping(value = "/downLoadAdminFile")
		public void downLoadAdminFile(@RequestParam(value = "selectYear") int selectYear ,HttpServletRequest request, HttpServletResponse response, ModelMap model)
				throws Exception {
			String path = "C:\\ExcelDown\\"; // Link의 자바파일에서 excel 파일이 생성된 경로
			String realFileNm = selectYear+"월별 야근기록(관리자).xlsx";
			commonExcel(path, realFileNm, request, response);
		}

	public void commonExcel(String path, String realFileNm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		File uFile = new File(path, realFileNm);
		int fSize = (int) uFile.length();
		if (fSize > 0) { // 파일 사이즈가 0보다 클 경우 다운로드
			String mimetype = "application/octet-stream;charset=UTF-8"; // minetype은 파일확장자에 맞게 설정

			response.setContentType(mimetype);
			DownUtils.setDisposition(realFileNm, request, response);
			response.setContentLength(fSize);

			BufferedInputStream in = null;
			BufferedOutputStream out = null;

			try {
				in = new BufferedInputStream(new FileInputStream(uFile));
				out = new BufferedOutputStream(response.getOutputStream());
				FileCopyUtils.copy(in, out);
				out.flush();
			} catch (Exception ex) {
			} finally {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			}
		} else {
			response.setContentType("application/octet-stream;charset=UTF-8");

			
			PrintWriter printwriter = response.getWriter();
			printwriter.println("<html>");
			printwriter.println("<br><br><br><h2>Could not get file name:<br>" + realFileNm + "</h2>");
			printwriter.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
			printwriter.println("<br><br><br>&copy; webAccess");
			printwriter.println("</html>");
			printwriter.flush();
			printwriter.close();
		}
	}
}
