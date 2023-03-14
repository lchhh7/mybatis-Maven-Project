package com.jinjin.jintranet.commuting.service;

import com.jinjin.jintranet.common.util.ListSortUtils;
import com.jinjin.jintranet.common.vo.CommutingRequestVO;
import com.jinjin.jintranet.common.vo.CommutingVO;
import com.jinjin.jintranet.common.vo.HolidayVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;
import com.jinjin.jintranet.holiday.service.HolidayMapper;
import com.jinjin.jintranet.schedule.service.ScheduleMapper;
import com.jinjin.jintranet.security.SecurityUtils;

import lombok.NoArgsConstructor;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jinjin.jintranet.common.util.SortUtils.sortListVO;

@Service
@NoArgsConstructor
public class CommutingService_bak {

	private CommutingMapper commutingMapper;

	private CommutingRequestMapper requestMapper;

	private ScheduleMapper scheduleMapper;

	private HolidayMapper holidayMapper;
	
	@Autowired
	public CommutingService_bak(CommutingMapper commutingMapper,CommutingRequestMapper requestMapper,ScheduleMapper scheduleMapper,HolidayMapper holidayMapper) {
		this.commutingMapper = commutingMapper;
		this.requestMapper = requestMapper;
		this.scheduleMapper = scheduleMapper;
		this.holidayMapper = holidayMapper;
	}
	
	public Map<String, Object> getWorkTime() {
		Map<String, Object> map = new HashMap<>();

		CommutingVO commutingVO = new CommutingVO();
		commutingVO.setSearchMemberId(SecurityUtils.getLoginMemberId());

		map.put("goToWorkTime", commutingMapper.goToWorkTime(commutingVO));
		map.put("offToWorkTime", commutingMapper.offToWorkTime(commutingVO));
		map.put("workingStatus", commutingMapper.workingStatus(commutingVO));

		return map;

	}

	public void goToWorkButton(CommutingVO cvo) {
		cvo.setMemberId(SecurityUtils.getLoginMemberId());
		commutingMapper.goToWorkButton(cvo);
	}

	/**
	 * @return Map<String, Object> list : 출근, 퇴근, 철야 목록 overtimes : 유효 잔업 목록
	 *         totalOverTime : 총 잔업 시간 vacations : 휴가목록 holidays : 공휴일 목록 TODO: 휴가,
	 *         공휴일에 잔업한 것도 시간 계산 특히, 반차
	 */
	public Map<String, Object> findAll(String startDt, String endDt, int loginId) throws Exception {

		Map<String, Object> map = new HashMap<>();

		List<CommutingVO> list = new LinkedList<>();
		try {
			String lastDayOfMonth = new LocalDate(startDt).plusMonths(1).minusDays(1).toString();

			int commutingExistsCheck = -1;

			HolidayVO holidayVO = new HolidayVO();
			holidayVO.setSearchStartDt(startDt);
			holidayVO.setSearchEndDt(lastDayOfMonth);

			List<HolidayVO> holidays = holidayMapper.findAll(holidayVO);
			map.put("holidays", holidays);

			ScheduleVO scheduleVO = new ScheduleVO();
			scheduleVO.setSearchMemberId(loginId);
			scheduleVO.setSearchStartDt(startDt);
			scheduleVO.setSearchEndDt(lastDayOfMonth);

			List<ScheduleVO> vacations = scheduleMapper.findAllForCommuting(scheduleVO);
			map.put("vacations", vacations);

			List<ScheduleVO> businessTrips = scheduleMapper.findBusinessTripForCommuting(scheduleVO);
			List<ScheduleVO> searchBusinessTrips  = new ArrayList<>();
			for (ScheduleVO vo : businessTrips) {
				vo.setScheduleId(vo.getId());
				if(vo.getMemberId() == loginId || scheduleMapper.selectPassengers(vo).contains(loginId+"")) {
					searchBusinessTrips.add(vo);
				}
			}
			map.put("businessTrips", searchBusinessTrips);

			CommutingRequestVO requestVO = new CommutingRequestVO();
			requestVO.setSearchMemberId(loginId);
			requestVO.setSearchStartDt(startDt);
			requestVO.setSearchEndDt(lastDayOfMonth);

			// TODO: sql조건에 type = 'O' 인 것 추가
			List<CommutingRequestVO> overtimeRequests = requestMapper.findAll(requestVO);
			map.put("overtimeRequests", overtimeRequests);

			CommutingVO commutingVO = new CommutingVO();
			commutingVO.setMemberId(loginId);
			commutingVO.setSearchStartDt(startDt);
			commutingVO.setSearchEndDt(endDt);

			List<CommutingVO> workingTimes = commutingMapper.getWorkingTime(commutingVO);
			List<CommutingVO> quittingTimes = commutingMapper.getQuittingTime(commutingVO);

			commutingVO.setSearchEndDt(new LocalDate(endDt).plusDays(1).toString());

			List<CommutingVO> allNightTimes = commutingMapper.getAllNightTime(commutingVO);
			setDtAllNight(allNightTimes);

			list.addAll(workingTimes);
			list.addAll(allNightTimes);
			list.addAll(quittingTimes);

			if (list.size() > 0) {
				sortListVO(list, "getDtm", "ASC");
				if (!"Y".equals(list.get(0).getAttendYn()))
					list.remove(0);

				map.put("list", list);

				List<CommutingVO> selectedList = new LinkedList<>();
				// 야근 계산 & 초과근무 stick

				CommutingRequestVO commutingRequestVO = new CommutingRequestVO();
				commutingRequestVO.setSearchMemberId(loginId);
				commutingRequestVO.setSearchStartDt(startDt);
				commutingRequestVO.setSearchEndDt(endDt);
				
				List<String> needDayList = requestMapper.findOvertimeByDay(commutingRequestVO);

				List<ScheduleVO> businessTripForCommuting = scheduleMapper.findBusinessTripForCommuting(scheduleVO);
				if (needDayList.size() != 0) {
					for (int j = 0; j < needDayList.size(); j++) {
						for (int i = 0; i < list.size(); i = i + 2) {
							// 오늘일대만 체크하면됨
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							String d = sdf.format(new Date());
							if (d.equals(needDayList.get(j))) {
								commutingExistsCheck = commutingMapper
										.commutingExistsCheck(CommutingVO.builder().memberId(loginId).startDt(needDayList.get(j)).build());
							}	

							if (list.get(i).getDt().equals(needDayList.get(j))
									&& (commutingExistsCheck == -1 || commutingExistsCheck >= 2)) {
								selectedList.add(list.get(i));
								selectedList.add(list.get(i + 1));
								break;
							}

							commutingExistsCheck = -1;
						}
					}
				}
				map.putAll(calculateOvertime(selectedList, businessTripForCommuting, scheduleVO, loginId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	// TODO: commuting_request 조회 시 type = 'O' & status = 'Y' 인 것 만 가져오기

	private void setDtAllNight(List<CommutingVO> allNightTimes) {
		for (CommutingVO c : allNightTimes) {
			String dt = new LocalDate(c.getDt()).minusDays(1).toString();
			c.setDt(dt);
		}
	}

	public CommutingVO findById(CommutingVO commutingVO) {
		return commutingMapper.findById(commutingVO);
	}

	private Map<String, Object> calculateOvertime(List<CommutingVO> list, List<ScheduleVO> businessTripForCommuting,
			ScheduleVO scheduleVO, Integer loginId) throws ParseException {
		Map<String, Object> map = new HashMap<>();
		List<CommutingVO> overTimes = new LinkedList<>();
		int totalOverTime = 0;
		// TODO: 저녁시간 따로 ㅃ기 STANDARD_TIME_OF_OVER_TIME

		final int MILLIS_TO_MIN = 60_000; // millis > min 계산
		final int LAUNCH_TIME_VALUE = 60; // 점심시간
		final int DINNER_TIME_VALUE = 60; // 저녁시간
		final int STANDARD_TIME_OF_OVER_TIME = 540; // 반차 아닌 근무시간
		final int STANDARD_TIME_OF_OVER_TIME_BY_HV = 300; // 반차 근무시간
		final int MIN_OVER_TIME = 120;

		final String LAUNCH_TIME_END_TM = "13:00";
		final String DINNER_TIME_END_TM = "19:00";
		int cnt = list.size() - 1;

		// 2씩 증가 > c1은 무조건 출근
		for (int i = 0; i < cnt; i++) {
			CommutingVO c1 = list.get(i); // 출근
			CommutingVO c2 = list.get(++i); // 퇴근
			String attend1 = c1.getAttendYn();
			String attend2 = c2.getAttendYn();

			// 누락되거나 잘 못 찍힌 것이 있으면 계산하지 않고 빈 map 리턴
			if (!("Y".equals(attend1) && !"Y".equals(attend2)))
				return map;

			boolean isEatLaunch = false;
			boolean isEatDinner = false;
			// 13:00 이전에 출근 했으면 점심식사를 한 것으로 간주

			if (c1.getTm().compareTo(LAUNCH_TIME_END_TM) < 0) {
				isEatLaunch = true;
			}
			if (c1.getTm().compareTo(DINNER_TIME_END_TM) > 0) {
				isEatDinner = true;
			}

			int holidayCheckCount = 0; // 빨간날 체크
			List<HolidayVO> holidayList = holidayMapper
					.findAll(HolidayVO.builder().searchStartDt(scheduleVO.getSearchStartDt())
							.searchEndDt(scheduleVO.getSearchEndDt()).build());
			for (int j = 0; j < holidayList.size(); j++) {
				if (c1.getDt().equals(DateFormatUtils.format(holidayList.get(j).getHolidayDt(), "yyyy-MM-dd"))) {
					holidayCheckCount++;
					break;
				}
			}

			if (holidayCheckCount > 0 || new LocalDate(c1.getDt()).getDayOfWeek() >= 6) { // 주말이거나 공휴일 일때 -> 밥 안먹고 모든시간을
				// 잔업으로 침
				isEatLaunch = false;
				isEatDinner = false;
			}

			long millis1 = new DateTime(c1.getDtm()).getMillis() / 10000 * 10000;
			long millis2 = new DateTime(c2.getDtm()).getMillis() / 10000 * 10000;

			int diff = (int) ((millis2 - millis1) / MILLIS_TO_MIN);

			int dayOfWeek = new LocalDate(c1.getDt()).getDayOfWeek();
			// 위로 쉬는날, 빨간날 계산 (평일 + 주말,공휴일 + 야근 포함)

			// 아래로 평일 계산(평일 + 반차 +야근 / 평일 + 휴가 + 야근 / 일반평일 나눠서 계산)
			if (dayOfWeek < 6 && holidayCheckCount==0) { // OT제외 다른일정 체크
				List<ScheduleVO> scheduleExceptionOvertimeList = scheduleMapper
						.findOtherScheduleExceptOvertime(
								ScheduleVO.builder().memberId(loginId).startDt(c1.getDt()).build());

				// OT 제외 다른일정이 있는경우
				if (scheduleExceptionOvertimeList.size() == 1) {
					if ("HV".equals(scheduleExceptionOvertimeList.get(0).getType())) { // 반차
						if (c1.getTm().compareTo(LAUNCH_TIME_END_TM) > 0) { // 오전반자
							diff = mealTimeException(diff, false, isEatDinner, LAUNCH_TIME_VALUE, DINNER_TIME_VALUE);
							diff = diff - STANDARD_TIME_OF_OVER_TIME_BY_HV; // 반차기준 근무시간 빼기

							if (diff < MIN_OVER_TIME) {
								diff = 0;
							}
						} else { // 오후반차
							diff = mealTimeException(diff, true, true, LAUNCH_TIME_VALUE, DINNER_TIME_VALUE);
							diff = diff - STANDARD_TIME_OF_OVER_TIME_BY_HV;
						}
					} else if ("FV".equals(scheduleExceptionOvertimeList.get(0).getType())) { // 휴가(연차)
						diff = mealTimeException(diff, false, false, LAUNCH_TIME_VALUE, DINNER_TIME_VALUE);
					}
				} else { // OT 제외 일정이 2개 이상 --> 외근 + 반차 + 야근만 가능
					diff = mealTimeException(diff, isEatLaunch, isEatDinner, LAUNCH_TIME_VALUE, DINNER_TIME_VALUE);
					diff = diff - STANDARD_TIME_OF_OVER_TIME;

					if (diff < MIN_OVER_TIME) {
						diff = 0;
					}
				}
			}

			if (diff > 0) {
				CommutingVO commutingVO = new CommutingVO();
				commutingVO.setId(c1.getId()); //?
				commutingVO.setDt(c1.getDt());
				commutingVO.setTm(under10(diff / 60) + ":" + under10(diff % 60));
				overTimes.add(commutingVO);

				totalOverTime += diff;
			}
		}

		int businessTripInWeekEndCount = 0;

		for (ScheduleVO vo : businessTripForCommuting) {
			vo.setScheduleId(vo.getId());
			if(vo.getMemberId() == loginId || scheduleMapper.selectPassengers(vo).contains(loginId+"")) {
				businessTripInWeekEndCount += BusinessTripInWeekEnd(vo.getStartDt(), vo.getEndDt());
			}
		}

		int totalHour = totalOverTime / 60 + businessTripInWeekEndCount * 8;
		String totalMin = under10(totalOverTime % 60);
		int availableCnt = totalHour / 8;
		int remaining = 480 - (totalOverTime - (availableCnt * 8 * 60));
		int remainingHour = remaining / 60;
		String remainingMin = under10(remaining % 60);
		
		Collections.sort(overTimes,new ListSortUtils()); //id값으로 정렬
		
		map.put("overtimes", overTimes);
		map.put("allTm", totalHour+":"+totalMin);
		map.put("totalOverTime",
				new StringBuffer().append("<p class=\"todaytotal\">").append(totalHour).append("시간 ").append(totalMin)
						.append("분<span class=\"todaytotal_rest\">(").append(availableCnt)
						.append("개)</span></p><p class=\"todaytotal_rest\">다음 신청가능 휴가까지 ").append(remainingHour)
						.append("시간 ").append(remainingMin).append("분 남음</p>").toString());
		map.put("availableCnt", availableCnt);
		return map;
	}
	
	/*
	 * private Map<String, Object> calculateTime1123() { return map; }
	 */
	

	private String under10(int i) {
		return i < 10 ? "0" + i : Integer.toString(i);
	}

	// 출장 기간 중 주말 갯수 찾기
	private Integer BusinessTripInWeekEnd(String start_dt, String end_dt) {
		int count = 0;
		LocalDate strLocalDate = new LocalDate(start_dt);
		LocalDate endLocalDate = new LocalDate(end_dt);
		
		
		while (strLocalDate.getDayOfYear() <= endLocalDate.getDayOfYear()) {
			
			List<HolidayVO> holidayList = holidayMapper
					.findAll(HolidayVO.builder().searchStartDt(strLocalDate.toString())
							.searchEndDt(strLocalDate.toString()).build());
			
			if (strLocalDate.getDayOfWeek() == 6 || strLocalDate.getDayOfWeek() == 7 || holidayList.size() !=0 ) {
				count++;
			}
			strLocalDate = strLocalDate.plusDays(1);
		}
		return count;
	}

	public int mealTimeException(int diff, boolean isEatLaunch, boolean isEatDinner, int LAUNCH_TIME_VALUE,
			int DINNER_TIME_VALUE) {
		if (isEatLaunch) {
			diff -= LAUNCH_TIME_VALUE;
		}
		if (isEatDinner) {
			diff -= DINNER_TIME_VALUE;
		}
		return diff;
	}

	public void write(CommutingVO commutingVO) {
		commutingMapper.write(commutingVO);
	}

	public void excelCreate(XSSFWorkbook xssfWb, String path, String localFile) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			FileOutputStream fos = null;
			fos = new FileOutputStream(new File(path + localFile));
			xssfWb.write(fos);
			if (xssfWb != null)
				xssfWb.close();
			if (fos != null)
				fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void xlsxWritterByYear(Map<String, List<String>> listMap,String breakResult,boolean checkYear) throws Exception {
		XSSFWorkbook xssfWb = new XSSFWorkbook();
		XSSFSheet xssfSheet = xssfWb.createSheet("모든 잔업기록"); // 워크시트 이름
		XSSFRow xssfRow = null;
		XSSFCell xssfCell = null;

		int rowNo = 0; // 행 갯수
		int m = 0;
		// 워크북 생성

		// 셀병합
		xssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
		xssfRow = xssfSheet.createRow(rowNo++);
		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellValue("잔업 기록");
		CellStyle cellStyle_Body = xssfWb.createCellStyle();
		cellStyle_Body.setAlignment(HorizontalAlignment.CENTER);

		// 헤더 생성
		xssfRow = xssfSheet.createRow(rowNo++);
		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellStyle(cellStyle_Body);
		xssfCell.setCellValue("날 짜");

		xssfCell = xssfRow.createCell((short) 1);
		xssfCell.setCellStyle(cellStyle_Body);
		xssfCell.setCellValue("근무 시간");
		
		xssfCell = xssfRow.createCell((short) 3);
		xssfCell.setCellStyle(cellStyle_Body);
		xssfCell.setCellValue("달");
		
		xssfCell = xssfRow.createCell((short) 4);
		xssfCell.setCellStyle(cellStyle_Body);
		xssfCell.setCellValue("합 계");
		
		if(checkYear) {
		xssfCell = xssfRow.createCell((short) 6);
		xssfCell.setCellStyle(cellStyle_Body);
		xssfCell.setCellValue("최종 휴가");
		}
		
		// 테이블 스타일 설정
		CellStyle cellStyle_Table_Center = xssfWb.createCellStyle();
		cellStyle_Table_Center.setBorderTop(BorderStyle.THIN);
		cellStyle_Table_Center.setBorderBottom(BorderStyle.THIN);
		cellStyle_Table_Center.setBorderLeft(BorderStyle.THIN);
		cellStyle_Table_Center.setBorderRight(BorderStyle.THIN);
		cellStyle_Table_Center.setAlignment(HorizontalAlignment.CENTER);
		
		int b = listMap.get("allDtList").size();
		int s = listMap.get("allTm").size();
		
		if(s > b) {
			int temp = s;
			s = b;
			b = temp;
		}
		
		for (int i = 0; i < b; i++) {
			
			xssfRow = xssfSheet.createRow(rowNo++);
			if(i < listMap.get("allDtList").size()) {
				xssfCell = xssfRow.createCell((short) 0);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue((String) listMap.get("allDtList").get(i));

				xssfCell = xssfRow.createCell((short) 1);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue((String) listMap.get("allTmList").get(i));
			}
			
			if(m<=11) {
				xssfCell = xssfRow.createCell((short) 3);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue(++m);
			}
			
			if(i < listMap.get("allTm").size()) {
				xssfCell = xssfRow.createCell((short) 4);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue((String) listMap.get("allTm").get(i));
			}
			if(i ==0 && checkYear) {
				xssfCell = xssfRow.createCell((short) 6);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue(breakResult);
			}
		}
		String path = "C:\\ExcelDown\\";
		String localFile = "잔업기록-"+SecurityUtils.getLoginMemberName()+".xlsx";
		excelCreate(xssfWb, path, localFile);
	}
	
	public void xlsxWritter(Map<String, List<String>> listMap, int month) throws Exception {
		XSSFWorkbook xssfWb = new XSSFWorkbook();
		XSSFSheet xssfSheet = xssfWb.createSheet("휴가계산"); // 워크시트 이름
		XSSFRow xssfRow = null;
		XSSFCell xssfCell = null;

		int rowNo = 0; // 행 갯수
		// 워크북 생성

		// 셀병합
		xssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
		xssfRow = xssfSheet.createRow(rowNo++);
		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellValue("잔업 기록");
		CellStyle cellStyle_Body = xssfWb.createCellStyle();
		cellStyle_Body.setAlignment(HorizontalAlignment.CENTER);

		// 헤더 생성
		xssfRow = xssfSheet.createRow(rowNo++);
		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellStyle(cellStyle_Body);
		xssfCell.setCellValue("날짜");

		xssfCell = xssfRow.createCell((short) 1);
		xssfCell.setCellStyle(cellStyle_Body);
		xssfCell.setCellValue("야근시간");

		// 테이블 스타일 설정
		CellStyle cellStyle_Table_Center = xssfWb.createCellStyle();
		cellStyle_Table_Center.setBorderTop(BorderStyle.THIN);
		cellStyle_Table_Center.setBorderBottom(BorderStyle.THIN);
		cellStyle_Table_Center.setBorderLeft(BorderStyle.THIN);
		cellStyle_Table_Center.setBorderRight(BorderStyle.THIN);
		cellStyle_Table_Center.setAlignment(HorizontalAlignment.CENTER);

		for (int i = 0; i < listMap.get("dtList").size(); i++) {

			xssfRow = xssfSheet.createRow(rowNo++);
			xssfCell = xssfRow.createCell((short) 0);
			xssfCell.setCellStyle(cellStyle_Table_Center);
			xssfCell.setCellValue((String) listMap.get("dtList").get(i));

			xssfCell = xssfRow.createCell((short) 1);
			xssfCell.setCellStyle(cellStyle_Table_Center);
			xssfCell.setCellValue((String) listMap.get("TmList").get(i));
		}
		String path = "C:\\ExcelDown\\";
		String localFile = month + "월 야근기록" + ".xlsx";
		excelCreate(xssfWb, path, localFile);
	}
	
	public void commutingXlsxWritter(Map<String, Map<String, List>> resultMap,Integer selectYear,int month) throws Exception {

		List<MemberVO> memberList = resultMap.get("result").get("memberList");
		List<List<String>> totalOvertimeList = resultMap.get("result").get("totalOvertimeList");
		List<String> breakResultList = resultMap.get("result").get("breakResultList");

		XSSFWorkbook xssfWb = new XSSFWorkbook();
		
		boolean checkYear = selectYear == LocalDate.now().getYear() ? false : true;
		if(checkYear) month = 13;
		
		for (int i = 0; i < month-1 ; i++) {
			XSSFSheet xssfSheet = xssfWb.createSheet(i+1 + "월");
			XSSFRow xssfRow = null;
			XSSFCell xssfCell = null;

			int rowNo = 0; // 행 갯수

			xssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
			xssfRow = xssfSheet.createRow(rowNo++);
			xssfCell = xssfRow.createCell((short) 0);
			xssfCell.setCellValue("잔업 기록");

			CellStyle cellStyle_Body = xssfWb.createCellStyle();
			cellStyle_Body.setAlignment(HorizontalAlignment.LEFT);

			xssfRow = xssfSheet.createRow(rowNo++);
			xssfCell = xssfRow.createCell((short) 0);
			xssfCell.setCellStyle(cellStyle_Body);
			xssfCell.setCellValue("이름");

			xssfCell = xssfRow.createCell((short) 1);
			xssfCell.setCellStyle(cellStyle_Body);
			xssfCell.setCellValue("추가휴가갯수");

			if(checkYear && i==11) {
				xssfCell = xssfRow.createCell((short) 5);
				xssfCell.setCellStyle(cellStyle_Body);
				xssfCell.setCellValue("최종 휴가");
			}
			
			CellStyle cellStyle_Table_Center = xssfWb.createCellStyle();
			cellStyle_Table_Center.setBorderTop(BorderStyle.THIN);
			cellStyle_Table_Center.setBorderBottom(BorderStyle.THIN);
			cellStyle_Table_Center.setBorderLeft(BorderStyle.THIN);
			cellStyle_Table_Center.setBorderRight(BorderStyle.THIN);
			cellStyle_Table_Center.setAlignment(HorizontalAlignment.CENTER);

			for (int j = 0; j < memberList.size(); j++) {
				xssfRow = xssfSheet.createRow(rowNo++);
				xssfCell = xssfRow.createCell((short) 0);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue(memberList.get(j).getName());

				xssfCell = xssfRow.createCell((short) 1);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue(totalOvertimeList.get(j).get(i));
				
				if(i ==11 && checkYear) {
					xssfCell = xssfRow.createCell((short) 5);
					xssfCell.setCellStyle(cellStyle_Table_Center);
					xssfCell.setCellValue(breakResultList.get(j));
				}
			}
		}
		String path = "C:\\ExcelDown\\";
		String localFile = selectYear+"월별 야근기록(관리자).xlsx";
		excelCreate(xssfWb, path, localFile);
	}
}