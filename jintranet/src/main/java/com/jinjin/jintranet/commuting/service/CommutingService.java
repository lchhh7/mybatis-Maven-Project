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
import java.util.stream.Collectors;

import static com.jinjin.jintranet.common.util.SortUtils.sortListVO;

@Service
@NoArgsConstructor
public class CommutingService {

	private CommutingMapper commutingMapper;

	private CommutingRequestMapper requestMapper;

	private CommutingCalculateMapper calculateMapper;

	private ScheduleMapper scheduleMapper;

	private HolidayMapper holidayMapper;

	@Autowired
	public CommutingService(CommutingMapper commutingMapper, CommutingRequestMapper requestMapper,
			CommutingCalculateMapper calculateMapper, ScheduleMapper scheduleMapper, HolidayMapper holidayMapper) {
		this.commutingMapper = commutingMapper;
		this.requestMapper = requestMapper;
		this.calculateMapper = calculateMapper;
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
			String lastDayOfMonth = new LocalDate(startDt).plusMonths(1).minusDays(1).toString();

			//int commutingExistsCheck = -1;

			HolidayVO holidayVO = new HolidayVO()
					.builder()
					.searchStartDt(startDt)
					.searchEndDt(lastDayOfMonth).build();
			
			ScheduleVO scheduleVO = new ScheduleVO()
					.builder()
					.searchMemberId(loginId)
					.searchStartDt(startDt)
					.searchEndDt(lastDayOfMonth).build();
			
			CommutingRequestVO requestVO = new CommutingRequestVO()
					.builder()
					.searchMemberId(loginId)
					.searchStartDt(startDt)
					.searchEndDt(lastDayOfMonth).build();
			
			CommutingVO commutingVO = new CommutingVO()
					.builder()
					.memberId(loginId)
					.searchStartDt(startDt)
					.searchEndDt(endDt).build();
			
			List<HolidayVO> holidays = holidayMapper.findAll(holidayVO);
			List<ScheduleVO> vacations = scheduleMapper.findAllForCommuting(scheduleVO);
			List<ScheduleVO> businessTrips = scheduleMapper.findBusinessTripForCommuting(scheduleVO);
			List<CommutingRequestVO> overtimeRequests = requestMapper.findAll(requestVO);
			List<CommutingRequestVO> timeList = calculateMapper.search(requestVO);

			/*List<ScheduleVO> searchBusinessTrips = new ArrayList<>();
			for (ScheduleVO vo : businessTrips) {
				vo.setScheduleId(vo.getId());
				if (vo.getMemberId() == loginId || scheduleMapper.selectPassengers(vo).contains(Integer.toString(loginId))) {
					searchBusinessTrips.add(vo);
				}
			}*/
			
			List<ScheduleVO> searchBusinessTrips =
					Optional.ofNullable(businessTrips).orElseGet(Collections::emptyList).stream()
					.peek(vo -> vo.setScheduleId(vo.getId()))
					.filter(vo ->vo.getMemberId() == loginId || scheduleMapper.selectPassengers(vo).contains(Integer.toString(loginId)))
					.collect(Collectors.toList());

			List<CommutingVO> workingTimes = commutingMapper.getWorkingTime(commutingVO);
			List<CommutingVO> quittingTimes = commutingMapper.getQuittingTime(commutingVO);
			
			map.put("vacations", vacations);
			map.put("holidays", holidays);
			map.put("businessTrips", searchBusinessTrips);
			map.put("overtimeRequests", overtimeRequests);
			map.put("proposalPeriods", timeList);

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

				List<ScheduleVO> businessTripForCommuting = scheduleMapper.findBusinessTripForCommuting(scheduleVO);
				map.putAll(calculateOvertime(timeList, businessTripForCommuting, scheduleVO, requestVO, loginId));
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

	private Map<String, Object> calculateOvertime(List<CommutingRequestVO> proposalList,
			List<ScheduleVO> businessTripForCommuting, ScheduleVO scheduleVO, CommutingRequestVO requestVO,
			Integer loginId) {
		Map<String, Object> map = new HashMap<>();
		List<CommutingVO> overTimes = new LinkedList<>();
		float pureWorkTm = 0;
		float extensionWorkTm = 0;
		float extensionNightWorkTm = 0;
		float totalTm = 0;
		String allTmMin = "00";
		// 계산용 객체 생성

		List<CommutingRequestVO> timeList = proposalList;

		// TODO: 저녁시간 따로 ㅃ기 STANDARD_TIME_OF_OVER_TIME

		// 주말출장 갯수
		int businessTripInWeekEndCount = 0;

		for (ScheduleVO vo : businessTripForCommuting) {
			vo.setScheduleId(vo.getId());
			if (vo.getMemberId() == loginId || scheduleMapper.selectPassengers(vo).contains(loginId + "")) {
				businessTripInWeekEndCount += BusinessTripInWeekEnd(vo.getStartDt(), vo.getEndDt());
			}
		}

		for (CommutingRequestVO vo : timeList) {
			pureWorkTm += Float.parseFloat(vo.getPureWorkTm());
			extensionWorkTm += Float.parseFloat(vo.getExtensionWorkTm());
			extensionNightWorkTm += Float.parseFloat(vo.getExtensionNightWorkTm());
			totalTm += Float.parseFloat(vo.getTotalTm());
			
			CommutingVO cvo = new CommutingVO();
			cvo.setDt(vo.getRequestDt().substring(0,10));
			cvo.setTm(vo.getTotalTm());
			overTimes.add(cvo);
		}

		float totalHour = totalTm + businessTripInWeekEndCount * 12;
		int availableCnt = (int) totalHour / 8;

		if (totalHour > (int) totalHour) {
			allTmMin = "30";
		}
		
		//overTimes.add(null)
		
		map.put("overtimes", overTimes);
		map.put("allTm", (int) totalHour + ":" + allTmMin);
		map.put("totalOverTime",
				new StringBuffer().append("<p class=\"todaytotal\">").append(totalHour).append("시간 ")
						.append("<span class=\"todaytotal_rest\">(").append(availableCnt)
						.append("개)</span></p><p class=\"todaytotal_rest\">순 근무 시간(")
						.append(pureWorkTm + businessTripInWeekEndCount * 8).append(") / (")
						.append(extensionWorkTm + businessTripInWeekEndCount * 12).append("+")
						.append(extensionNightWorkTm).append(")").toString());
		map.put("availableCnt", availableCnt);
		return map;
	}

	private String under10(int i) {
		return i < 10 ? "0" + i : Integer.toString(i);
	}

	// 출장 기간 중 주말 갯수 찾기
	private Integer BusinessTripInWeekEnd(String start_dt, String end_dt) {
		int count = 0;
		LocalDate strLocalDate = new LocalDate(start_dt);
		LocalDate endLocalDate = new LocalDate(end_dt);

		while (strLocalDate.getDayOfYear() <= endLocalDate.getDayOfYear()) {
			List<HolidayVO> holidayList = holidayMapper.findAll(HolidayVO.builder()
					.searchStartDt(strLocalDate.toString()).searchEndDt(strLocalDate.toString()).build());

			if (strLocalDate.getDayOfWeek() == 6 || strLocalDate.getDayOfWeek() == 7 || holidayList.size() != 0) {
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

	public void xlsxWritterByYear(Map<String, List<String>> listMap, String breakResult, boolean checkYear)
			throws Exception {
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

		if (checkYear) {
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

		if (s > b) {
			int temp = s;
			s = b;
			b = temp;
		}

		for (int i = 0; i < b; i++) {

			xssfRow = xssfSheet.createRow(rowNo++);
			if (i < listMap.get("allDtList").size()) {
				xssfCell = xssfRow.createCell((short) 0);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue((String) listMap.get("allDtList").get(i));

				xssfCell = xssfRow.createCell((short) 1);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue((String) listMap.get("allTmList").get(i));
			}

			if (m <= 11) {
				xssfCell = xssfRow.createCell((short) 3);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue(++m);
			}

			if (i < listMap.get("allTm").size()) {
				xssfCell = xssfRow.createCell((short) 4);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue((String) listMap.get("allTm").get(i));
			}
			if (i == 0 && checkYear) {
				xssfCell = xssfRow.createCell((short) 6);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue(breakResult);
			}
		}
		String path = "C:\\ExcelDown\\";
		String localFile = "잔업기록-" + SecurityUtils.getLoginMemberName() + ".xlsx";
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

	public void commutingXlsxWritter(Map<String, Map<String, List>> resultMap, Integer selectYear, int month)
			throws Exception {

		List<MemberVO> memberList = resultMap.get("result").get("memberList");
		List<List<String>> totalOvertimeList = resultMap.get("result").get("totalOvertimeList");
		for (int a = 0; a < totalOvertimeList.size(); a++) {
			if (totalOvertimeList.get(a).isEmpty()) {
				totalOvertimeList.remove(a);
				memberList.remove(a);
			}
		}
		List<String> breakResultList = resultMap.get("result").get("breakResultList");

		float sumPure = 0;
		float sumTotal = 0;

		XSSFWorkbook xssfWb = new XSSFWorkbook();

		boolean checkYear = selectYear == LocalDate.now().getYear() ? false : true;
		if (checkYear)
			month = 12;

		for (int i = 0; i < month; i++) {
			XSSFSheet xssfSheet = xssfWb.createSheet(i + 1 + "월");
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

				if (totalOvertimeList.get(j).get(i) != null) {
					xssfCell.setCellValue(totalOvertimeList.get(j).get(i));
				}
			}

			xssfRow = xssfSheet.createRow(rowNo++);
			xssfRow = xssfSheet.createRow(rowNo++);

			xssfCell = xssfRow.createCell((short) 0);
			xssfCell.setCellStyle(cellStyle_Body);
			xssfCell.setCellValue("이름");

			xssfCell = xssfRow.createCell((short) 1);
			xssfCell.setCellStyle(cellStyle_Body);
			xssfCell.setCellValue("순 근무시간");

			xssfCell = xssfRow.createCell((short) 2);
			xssfCell.setCellStyle(cellStyle_Body);
			xssfCell.setCellValue("근무시간");

			xssfCell = xssfRow.createCell((short) 3);
			xssfCell.setCellStyle(cellStyle_Body);
			xssfCell.setCellValue("승인자");

			xssfCell = xssfRow.createCell((short) 4);
			xssfCell.setCellStyle(cellStyle_Body);
			xssfCell.setCellValue("출근시간");

			xssfCell = xssfRow.createCell((short) 5);
			xssfCell.setCellStyle(cellStyle_Body);
			xssfCell.setCellValue("퇴근시간");

			for (int j = 0; j < memberList.size(); j++) {
				xssfRow = xssfSheet.createRow(rowNo++);
				xssfCell = xssfRow.createCell((short) 0);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue(memberList.get(j).getName());

				List<CommutingRequestVO> detailOverTimeList = resultMap.get("result")
						.get("detailOverTimeList" + (i + 1));

				if (!detailOverTimeList.isEmpty()) {
					for (int k = 0; k < detailOverTimeList.size(); k++) {
						if (memberList.get(j).getId() == detailOverTimeList.get(k).getMemberId()) {
							xssfRow = xssfSheet.createRow(rowNo++);

							xssfCell = xssfRow.createCell((short) 0);
							xssfCell.setCellStyle(cellStyle_Table_Center);
							xssfCell.setCellValue(detailOverTimeList.get(k).getRequestDt().split("-")[1] + "-"
									+ detailOverTimeList.get(k).getRequestDt().split("-")[2].substring(0, 2));

							xssfCell = xssfRow.createCell((short) 1);
							xssfCell.setCellStyle(cellStyle_Table_Center);
							xssfCell.setCellValue(detailOverTimeList.get(k).getPureWorkTm());

							xssfCell = xssfRow.createCell((short) 2);
							xssfCell.setCellStyle(cellStyle_Table_Center);
							xssfCell.setCellValue(detailOverTimeList.get(k).getTotalTm());

							xssfCell = xssfRow.createCell((short) 3);
							xssfCell.setCellStyle(cellStyle_Table_Center);
							xssfCell.setCellValue(detailOverTimeList.get(k).getApproveName());

							String a = commutingMapper.workingTimeByMemberIdAndRequestDt(CommutingVO.builder()
									.memberId(memberList.get(j).getId())
									.searchStartDt(detailOverTimeList.get(k).getRequestDt().substring(0, 10))
									.build()) == null
											? " "
											: commutingMapper
													.workingTimeByMemberIdAndRequestDt(
															CommutingVO.builder().memberId(memberList.get(j).getId())
																	.searchStartDt(detailOverTimeList.get(k)
																			.getRequestDt().substring(0, 10))
																	.build())
													.getTm();

							String b = commutingMapper.quittingTimeByMemberIdAndRequestDt(CommutingVO.builder()
									.memberId(memberList.get(j).getId())
									.searchStartDt(detailOverTimeList.get(k).getRequestDt().substring(0, 10))
									.build()) == null
											? " "
											: commutingMapper
													.quittingTimeByMemberIdAndRequestDt(
															CommutingVO.builder().memberId(memberList.get(j).getId())
																	.searchStartDt(detailOverTimeList.get(k)
																			.getRequestDt().substring(0, 10))
																	.build())
													.getTm();

							xssfCell = xssfRow.createCell((short) 4);
							xssfCell.setCellStyle(cellStyle_Table_Center);
							xssfCell.setCellValue(a);

							xssfCell = xssfRow.createCell((short) 5);
							xssfCell.setCellStyle(cellStyle_Table_Center);
							xssfCell.setCellValue(b);

							sumPure += Float.parseFloat(detailOverTimeList.get(k).getPureWorkTm());
							sumTotal += Float.parseFloat(detailOverTimeList.get(k).getTotalTm());
						}
					}
				}
				xssfRow = xssfSheet.createRow(rowNo++);
				xssfCell = xssfRow.createCell((short) 0);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue("합계");

				xssfCell = xssfRow.createCell((short) 1);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue(sumPure);

				xssfCell = xssfRow.createCell((short) 2);
				xssfCell.setCellStyle(cellStyle_Table_Center);
				xssfCell.setCellValue(sumTotal);

				sumPure = 0;
				sumTotal = 0;
			}
		}

		XSSFSheet xssfSheet = xssfWb.createSheet("최종휴가");
		XSSFRow xssfRow = null;
		XSSFCell xssfCell = null;

		int rowNo = 0; // 행 갯수

		xssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
		xssfRow = xssfSheet.createRow(rowNo++);
		xssfCell = xssfRow.createCell((short) 1);
		xssfCell.setCellValue("최종휴가");

		CellStyle cellStyle_Body = xssfWb.createCellStyle();
		cellStyle_Body.setAlignment(HorizontalAlignment.LEFT);

		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellStyle(cellStyle_Body);
		xssfCell.setCellValue("최종 휴가");

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

			xssfCell = xssfRow.createCell((short) 2);
			xssfCell.setCellStyle(cellStyle_Table_Center);
			xssfCell.setCellValue(breakResultList.get(j));
		}

		String path = "C:\\ExcelDown\\";
		String localFile = selectYear + "월별 야근기록(관리자).xlsx";
		excelCreate(xssfWb, path, localFile);
	}
}