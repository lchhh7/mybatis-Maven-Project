package com.jinjin.jintranet.admin.member.service;

import static com.jinjin.jintranet.common.util.PageUtils.page;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinjin.jintranet.auth.service.AuthMapper;
import com.jinjin.jintranet.common.vo.AuthVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.security.SecurityUtils;

@Service
public class AdminMemberService {

	private AdminMemberMapper memberMapper;

	private AuthMapper authMapper;

	public AdminMemberService() {
	}

	@Autowired
	public AdminMemberService(AdminMemberMapper memberMapper, AuthMapper authMapper) {
		this.memberMapper = memberMapper;
		this.authMapper = authMapper;
	}

	public Map<String, Object> main(MemberVO memberVO, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();

		int totalCnt = memberMapper.getMemberCnt(memberVO);
		memberVO.setTotalCnt(totalCnt);

		List<MemberVO> list = memberMapper.findAll(memberVO);

		map.put("list", list);
		map.put("page", page(memberVO, "members", request));
		map.put("totalCnt", totalCnt);

		return map;
	}

	public MemberVO findById(int id) {
		MemberVO memberVO = new MemberVO(id);
		MemberVO vo = memberMapper.findById(memberVO);

		AuthVO authVO = new AuthVO();
		authVO.setMemberId(id);

		List<AuthVO> auths = authMapper.findAuthByMemberId(authVO);
		vo.setAuths(auths);

		return vo;
	}

	public int write(MemberVO memberVO) {
		Integer loginMemberId = 1;

		memberVO.setCrtId(loginMemberId);
		memberVO.setUdtId(loginMemberId);
		String encPassword = DigestUtils.sha256Hex(memberVO.getPassword());
		memberVO.setPassword(encPassword);

		memberMapper.write(memberVO);

		if (memberVO.getAuths().size() > 0) {
			int memberId = memberVO.getId();
			List<AuthVO> list = memberVO.getAuths();

			for (AuthVO authVO : list) {
				authVO.setMemberId(memberId);
				authVO.setCrtId(loginMemberId);

				authMapper.writeAuthMember(authVO);
			}
		}
		return memberVO.getId();
	}

	public void edit(MemberVO memberVO) {
		Integer loginMemberId = 1;
		int memberId = memberVO.getId();

		AuthVO vo = new AuthVO();
		vo.setMemberId(memberId);
		vo.setDelId(loginMemberId);

		authMapper.deleteAuthMemberByMemberId(vo);

		if (memberVO.getAuths().size() > 0) {
			List<AuthVO> list = memberVO.getAuths();

			for (AuthVO authVO : list) {
				authVO.setMemberId(memberId);
				authVO.setCrtId(loginMemberId);

				authMapper.writeAuthMember(authVO);
			}
		}

		String password = memberVO.getPassword();

		if (!isBlank(password)) {
			String encPassword = DigestUtils.sha256Hex(memberVO.getPassword());
			memberVO.setPassword(encPassword);
		}

		memberVO.setUdtId(loginMemberId);

		memberMapper.edit(memberVO);
	}

	public void delete(MemberVO memberVO) {
		memberVO.setDelId(SecurityUtils.getLoginMemberId());

		memberMapper.delete(memberVO);
	}

	public List<MemberVO> findMemberList(MemberVO memberVO) {
		return memberMapper.findMemberList(memberVO);
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

	public void memberListXlsxWritter(List<MemberVO> result) throws Exception {

		List<MemberVO> memberList = result;

		XSSFWorkbook xssfWb = new XSSFWorkbook();

		Font sheet1Font = xssfWb.createFont();
		sheet1Font.setFontName("맑은 고딕");
		sheet1Font.setFontHeight((short) 300);

		Font font = xssfWb.createFont();
		font.setFontName("맑은 고딕");
		font.setFontHeight((short) 180);

		XSSFSheet xssfSheet = xssfWb.createSheet("직원 리스트");
		XSSFRow xssfRow = null;
		XSSFCell xssfCell = null;

		int rowNo = 0; // 행 갯수
		XSSFColor myColor = new XSSFColor(Color.LIGHT_GRAY);

		XSSFCellStyle sheet1_cellStyle_Body = xssfWb.createCellStyle();
		sheet1_cellStyle_Body.setFillForegroundColor(myColor);
		sheet1_cellStyle_Body.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		sheet1_cellStyle_Body.setAlignment(HorizontalAlignment.CENTER);
		sheet1_cellStyle_Body.setBorderRight(XSSFCellStyle.BORDER_THIN);
		sheet1_cellStyle_Body.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		sheet1_cellStyle_Body.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		sheet1_cellStyle_Body.setBorderTop(XSSFCellStyle.BORDER_THIN);
		sheet1_cellStyle_Body.setFont(sheet1Font);

		XSSFCellStyle cellStyle_Body = xssfWb.createCellStyle();
		cellStyle_Body.setFillForegroundColor(myColor);
		cellStyle_Body.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle_Body.setAlignment(HorizontalAlignment.CENTER);
		cellStyle_Body.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle_Body.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle_Body.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle_Body.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle_Body.setFont(font);

		xssfRow = xssfSheet.createRow(rowNo++);

		xssfCell = xssfRow.createCell((short) 0);
		xssfCell.setCellStyle(sheet1_cellStyle_Body);
		xssfCell.setCellValue("부서");

		xssfCell = xssfRow.createCell((short) 1);
		xssfCell.setCellStyle(sheet1_cellStyle_Body);
		xssfCell.setCellValue("직함");

		xssfCell = xssfRow.createCell((short) 2);
		xssfCell.setCellStyle(sheet1_cellStyle_Body);
		xssfCell.setCellValue("이름");

		xssfCell = xssfRow.createCell((short) 3);
		xssfCell.setCellStyle(sheet1_cellStyle_Body);
		xssfCell.setCellValue("내선");

		xssfCell = xssfRow.createCell((short) 4);
		xssfCell.setCellStyle(sheet1_cellStyle_Body);
		xssfCell.setCellValue("핸드폰");

		xssfCell = xssfRow.createCell((short) 5);
		xssfCell.setCellStyle(sheet1_cellStyle_Body);
		xssfCell.setCellValue("메일");

		CellStyle sheet1_cellStyle_Table_Center = xssfWb.createCellStyle();
		sheet1_cellStyle_Table_Center.setBorderTop(BorderStyle.THIN);
		sheet1_cellStyle_Table_Center.setBorderBottom(BorderStyle.THIN);
		sheet1_cellStyle_Table_Center.setBorderLeft(BorderStyle.THIN);
		sheet1_cellStyle_Table_Center.setBorderRight(BorderStyle.THIN);
		sheet1_cellStyle_Table_Center.setAlignment(HorizontalAlignment.CENTER);
		sheet1_cellStyle_Table_Center.setFont(sheet1Font);

		CellStyle cellStyle_Table_Center = xssfWb.createCellStyle();
		cellStyle_Table_Center.setBorderTop(BorderStyle.THIN);
		cellStyle_Table_Center.setBorderBottom(BorderStyle.THIN);
		cellStyle_Table_Center.setBorderLeft(BorderStyle.THIN);
		cellStyle_Table_Center.setBorderRight(BorderStyle.THIN);
		cellStyle_Table_Center.setAlignment(HorizontalAlignment.CENTER);
		cellStyle_Table_Center.setFont(font);

		for (int j = 0; j < memberList.size(); j++) {
			xssfSheet.setColumnWidth(j, xssfSheet.getColumnWidth(j) * 2);

			xssfRow = xssfSheet.createRow(rowNo++);

			xssfCell = xssfRow.createCell((short) 0);
			xssfCell.setCellStyle(sheet1_cellStyle_Table_Center);
			xssfCell.setCellValue(memberList.get(j).getDepartmentName());

			xssfCell = xssfRow.createCell((short) 1);
			xssfCell.setCellStyle(sheet1_cellStyle_Table_Center);
			xssfCell.setCellValue(memberList.get(j).getPositionName());

			xssfCell = xssfRow.createCell((short) 2);
			xssfCell.setCellStyle(sheet1_cellStyle_Table_Center);
			xssfCell.setCellValue(memberList.get(j).getName());

			xssfCell = xssfRow.createCell((short) 3);
			xssfCell.setCellStyle(sheet1_cellStyle_Table_Center);
			xssfCell.setCellValue(memberList.get(j).getPhoneNo());

			xssfCell = xssfRow.createCell((short) 4);
			xssfCell.setCellStyle(sheet1_cellStyle_Table_Center);
			xssfCell.setCellValue(memberList.get(j).getMobileNo());

			xssfCell = xssfRow.createCell((short) 5);
			xssfCell.setCellStyle(sheet1_cellStyle_Table_Center);
			xssfCell.setCellValue(memberList.get(j).getMemberId());
		}
		xssfSheet.setColumnWidth(0, 5000);
		xssfSheet.setColumnWidth(1, 3200);
		xssfSheet.setColumnWidth(2, 3000);
		xssfSheet.setColumnWidth(3, 1600);
		xssfSheet.setColumnWidth(4, 5500);
		xssfSheet.setColumnWidth(5, 3200);
		
		xssfSheet.setMargin(xssfSheet.LeftMargin, 0.338031);
		xssfSheet.setMargin(xssfSheet.RightMargin, 0.248031);

		XSSFSheet xssfSheet2 = xssfWb.createSheet("4분할");
		XSSFRow xssfRow2 = null;
		XSSFCell xssfCell2 = null;

		XSSFPrintSetup print = xssfSheet2.getPrintSetup();
		print.setPaperSize(XSSFPrintSetup.A4_PAPERSIZE);

		int rowNo2 = 0; // 행 갯수

		for (int i = 0; i < 2; i++) {
			xssfRow2 = xssfSheet2.createRow(rowNo2++);
			
			xssfRow2.setHeight((short)312);
			
			xssfCell2 = xssfRow2.createCell((short) 0);
			xssfCell2.setCellStyle(cellStyle_Body);
			xssfCell2.setCellValue("부서");

			xssfCell2 = xssfRow2.createCell((short) 1);
			xssfCell2.setCellStyle(cellStyle_Body);
			xssfCell2.setCellValue("직함");

			xssfCell2 = xssfRow2.createCell((short) 2);
			xssfCell2.setCellStyle(cellStyle_Body);
			xssfCell2.setCellValue("이름");

			xssfCell2 = xssfRow2.createCell((short) 3);
			xssfCell2.setCellStyle(cellStyle_Body);
			xssfCell2.setCellValue("내선");

			xssfCell2 = xssfRow2.createCell((short) 4);
			xssfCell2.setCellStyle(cellStyle_Body);
			xssfCell2.setCellValue("핸드폰");

			xssfCell2 = xssfRow2.createCell((short) 5);
			xssfCell2.setCellStyle(cellStyle_Body);
			xssfCell2.setCellValue("메일");

			xssfCell2 = xssfRow2.createCell((short) 6);
			xssfCell2.setCellStyle(cellStyle_Body);
			xssfCell2.setCellValue("부서");

			xssfCell2 = xssfRow2.createCell((short) 7);
			xssfCell2.setCellStyle(cellStyle_Body);
			xssfCell2.setCellValue("직함");

			xssfCell2 = xssfRow2.createCell((short) 8);
			xssfCell2.setCellStyle(cellStyle_Body);
			xssfCell2.setCellValue("이름");

			xssfCell2 = xssfRow2.createCell((short) 9);
			xssfCell2.setCellStyle(cellStyle_Body);
			xssfCell2.setCellValue("내선");

			xssfCell2 = xssfRow2.createCell((short) 10);
			xssfCell2.setCellStyle(cellStyle_Body);
			xssfCell2.setCellValue("핸드폰");

			xssfCell2 = xssfRow2.createCell((short) 11);
			xssfCell2.setCellStyle(cellStyle_Body);
			xssfCell2.setCellValue("메일");

			for (int j = 0; j < memberList.size(); j++) {

				xssfRow2 = xssfSheet2.createRow(rowNo2++);
				
				xssfRow2.setHeight((short) 310);
				
				xssfCell2 = xssfRow2.createCell((short) 0);
				xssfCell2.setCellStyle(cellStyle_Table_Center);
				xssfCell2.setCellValue(memberList.get(j).getDepartmentName());

				xssfCell2 = xssfRow2.createCell((short) 1);
				xssfCell2.setCellStyle(cellStyle_Table_Center);
				xssfCell2.setCellValue(memberList.get(j).getPositionName());

				xssfCell2 = xssfRow2.createCell((short) 2);
				xssfCell2.setCellStyle(cellStyle_Table_Center);
				xssfCell2.setCellValue(memberList.get(j).getName());

				xssfCell2 = xssfRow2.createCell((short) 3);
				xssfCell2.setCellStyle(cellStyle_Table_Center);
				xssfCell2.setCellValue(memberList.get(j).getPhoneNo());

				xssfCell2 = xssfRow2.createCell((short) 4);
				xssfCell2.setCellStyle(cellStyle_Table_Center);
				xssfCell2.setCellValue(memberList.get(j).getMobileNo());

				xssfCell2 = xssfRow2.createCell((short) 5);
				xssfCell2.setCellStyle(cellStyle_Table_Center);
				xssfCell2.setCellValue(memberList.get(j).getMemberId());

				xssfCell2 = xssfRow2.createCell((short) 6);
				xssfCell2.setCellStyle(cellStyle_Table_Center);
				xssfCell2.setCellValue(memberList.get(j).getDepartmentName());

				xssfCell2 = xssfRow2.createCell((short) 7);
				xssfCell2.setCellStyle(cellStyle_Table_Center);
				xssfCell2.setCellValue(memberList.get(j).getPositionName());

				xssfCell2 = xssfRow2.createCell((short) 8);
				xssfCell2.setCellStyle(cellStyle_Table_Center);
				xssfCell2.setCellValue(memberList.get(j).getName());

				xssfCell2 = xssfRow2.createCell((short) 9);
				xssfCell2.setCellStyle(cellStyle_Table_Center);
				xssfCell2.setCellValue(memberList.get(j).getPhoneNo());

				xssfCell2 = xssfRow2.createCell((short) 10);
				xssfCell2.setCellStyle(cellStyle_Table_Center);
				xssfCell2.setCellValue(memberList.get(j).getMobileNo());

				xssfCell2 = xssfRow2.createCell((short) 11);
				xssfCell2.setCellStyle(cellStyle_Table_Center);
				xssfCell2.setCellValue(memberList.get(j).getMemberId());
			}
		}
		xssfSheet2.setColumnWidth(0, 2700);
		xssfSheet2.setColumnWidth(1, 1800);
		xssfSheet2.setColumnWidth(2, 1500);
		xssfSheet2.setColumnWidth(3, 1000);
		xssfSheet2.setColumnWidth(4, 3100);
		xssfSheet2.setColumnWidth(5, 1800);
		xssfSheet2.setColumnWidth(6, 2700);
		xssfSheet2.setColumnWidth(7, 1800);
		xssfSheet2.setColumnWidth(8, 1500);
		xssfSheet2.setColumnWidth(9, 1000);
		xssfSheet2.setColumnWidth(10, 3100);
		xssfSheet2.setColumnWidth(11, 1800);
		
		xssfSheet2.setMargin(xssfSheet2.LeftMargin, 0.248031);
		xssfSheet2.setMargin(xssfSheet2.RightMargin, 0.248031);

		String path = "C:\\ExcelDown\\";
		String localFile = "사원리스트.xlsx";
		excelCreate(xssfWb, path, localFile);
	}

}
