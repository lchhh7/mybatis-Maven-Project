package com.jinjin.jintranet.admin.member.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinjin.jintranet.addBreak.service.AddBreakService;
import com.jinjin.jintranet.admin.member.service.AdminMemberService;
import com.jinjin.jintranet.common.util.DownUtils;
import com.jinjin.jintranet.common.vo.AddBreakVO;
import com.jinjin.jintranet.common.vo.MemberVO;

@Controller
@RequestMapping("/admin")
public class AdminMemberController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminMemberController.class);

    private final AdminMemberService memberService;

    private final AddBreakService addBreakService;

    @Autowired
    public AdminMemberController(AdminMemberService memberService,AddBreakService addBreakService) {
        this.memberService = memberService;
        this.addBreakService = addBreakService;
    }
    /**
     * 사용자관리 > 목록 페이지로 이동
     */
    @RequestMapping(value = "/member.do", method = RequestMethod.GET)
    public String main(ModelMap map, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "admin-member/admin-member";
    }

    /**
     * 사용자관리 > 목록 조회
     */
    @RequestMapping(value = "/member/search.do", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(value = "p", required = false, defaultValue = "1") int page,
            @RequestParam(value = "n", required = false, defaultValue = "") String n,
            @RequestParam(value = "po", required = false, defaultValue = "") String p,
            @RequestParam(value = "d", required = false, defaultValue = "") String d,
            HttpServletRequest request) throws Exception {

        MemberVO memberVO = new MemberVO();
        memberVO.setPageIndex(page);
        memberVO.setSearchName(n);
        memberVO.setSearchPosition(p);
        memberVO.setSearchDepartment(d);

        loggingCurrentMethod(LOGGER, memberVO);
        try {
            Map<String, Object> map = memberService.main(memberVO, request);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 사용자관리 > 사용자 등록
     */
    @RequestMapping(value = "/member.do", method = RequestMethod.POST)
    public ResponseEntity<String> write(@Validated @RequestBody MemberVO memberVO, BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, memberVO);
        try {
            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }

            if (isBlank(memberVO.getPassword())) {
                return new ResponseEntity<>("비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
            }

            int id = memberService.write(memberVO);
            LocalDate now = LocalDate.now();
            int month = now.getMonthOfYear(); // 현재 보고 있는 달
            AddBreakVO vo = new AddBreakVO();

            for(int i=1; i<= month ; i++) {
                addBreakService.emptyDataInsert(vo.builder().month(Integer.toString(i)).memberId(id).build());
            }
            return new ResponseEntity<>("사용자 정보를 정상적으로 등록했습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("사용자 정보 등록 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }

    }

    /**
     * 사용자관리 > 사용자 조회
     */
    @RequestMapping(value = "/member/{id}.do", method = RequestMethod.GET)
    public ResponseEntity<MemberVO> findById(@PathVariable("id") int id) throws Exception {
        loggingCurrentMethod(LOGGER, id);

        try {
            MemberVO vo = memberService.findById(id);

            return new ResponseEntity<>(vo, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 사용자관리 > 사용자 수정
     */
    @RequestMapping(value = "/member/{id}.do", method = RequestMethod.PATCH)
    public ResponseEntity edit(@PathVariable("id") int id,@Validated @RequestBody MemberVO memberVO, BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, id, memberVO);
        try {
            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }

            memberService.edit(memberVO);

            return new ResponseEntity("사용자 정보를 정상적으로 수정했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity("사용자 정보 수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    /**
     * 사용자관리 > 사용자 삭제
     */
    @RequestMapping(value = "/member/{id}.do", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable("id") int id) throws Exception {
        loggingCurrentMethod(LOGGER, id);

        try {
            memberService.delete(new MemberVO(id));

            return new ResponseEntity<>("사용자 정보를 정상적으로 삭제했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("사용자 정보를 삭제하는 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/memberListPrintByExcel.do", method = RequestMethod.POST)
    public void memberListPrintByExcel(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("## AdminMemberController.memberListPrintByExcel()");
        try {
            List<MemberVO> memberList = memberService.findMemberList(new MemberVO());
            memberService.memberListXlsxWritter(memberList); // 생성
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/downLoadmlFile")
    public void downLoadmlFile(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws Exception {
        String path = "C:\\ExcelDown\\"; // Link의 자바파일에서 excel 파일이 생성된 경로
        String realFileNm = "사원리스트.xlsx";
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
