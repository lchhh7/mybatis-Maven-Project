package com.jinjin.jintranet.main.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinjin.jintranet.common.vo.CommutingVO;
import com.jinjin.jintranet.common.vo.DocumentVO;
import com.jinjin.jintranet.common.vo.NoticeVO;
import com.jinjin.jintranet.common.vo.ProjectVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;
import com.jinjin.jintranet.common.vo.SupplyVO;
import com.jinjin.jintranet.commuting.service.CommutingService;
import com.jinjin.jintranet.document.service.DocumentService;
import com.jinjin.jintranet.notice.service.NoticeService;
import com.jinjin.jintranet.project.service.ProjectService;
import com.jinjin.jintranet.schedule.service.ScheduleService;
import com.jinjin.jintranet.security.SecurityUtils;
import com.jinjin.jintranet.supply.service.SupplyService;

@Controller
public class MainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private final NoticeService noticeService;
    
    private final ProjectService projectService;
    
    private final SupplyService supplyService;
    
    private final DocumentService documentService;
    
    private final ScheduleService scheduleService;
    
    private final CommutingService commutingService;

    @Autowired
    public MainController(NoticeService noticeService,ProjectService projectService,SupplyService supplyService
    		,DocumentService documentService,ScheduleService scheduleService,CommutingService commutingService) {
    	this.noticeService = noticeService;
    	this.projectService = projectService;
    	this.supplyService = supplyService;
    	this.documentService = documentService;
    	this.scheduleService = scheduleService;
    	this.commutingService = commutingService;
    }
    /**
     * 메인페이지
     */
    @RequestMapping(value = "/main.do")
    public String main(ModelMap map,
                       HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);

        List<NoticeVO> noticeList = null;
        List<ProjectVO> projectList = null;
        List<SupplyVO> supplyList = null;
        List<Integer> yearList = null;
        List<ScheduleVO> typeList = null;
        
        try {
            noticeList = noticeService.findNoticeAll();

            ProjectVO projectVO = new ProjectVO();
            projectVO.setLastIndex(6);
            projectList = projectService.findProjectAllForMain(projectVO);
            supplyList = supplyService.findSupplyAllForMain();
            yearList = scheduleService.searchYear(new ScheduleVO());
            typeList = scheduleService.searchType(new ScheduleVO());
            
            map.put("noticeList", noticeList);
            map.put("supplyList", supplyList);
            map.put("projectList", projectList);
            map.put("yearList", yearList);
            map.put("typeList", typeList);
            map.putAll(commutingService.getWorkTime());
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "main/index";
    }

    /**
     * 프로젝트 > 부서 선택 시 화면 변경
     */
    @RequestMapping(value = "/main/project/{department}.do", method = RequestMethod.GET)
    public ResponseEntity<List<ProjectVO>> findProjectByDepartment(@PathVariable("department") String department) throws Exception {
        loggingCurrentMethod(LOGGER, department);

        ProjectVO projectVO = new ProjectVO();
        projectVO.setLastIndex(6);
        projectVO.setSearchDepartment(department);

        return projectService.findProjectAll(projectVO);
    }


    /**
     * 출근버튼
     */
    @RequestMapping(value = "/main/goToWorkButton.do", method = RequestMethod.POST)
    public ResponseEntity<String> goToWorkButton(@RequestBody CommutingVO cvo) throws Exception {
        
        commutingService.goToWorkButton(cvo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 신청한 연차 확인
     */
    @RequestMapping(value = "/main/schedule.do", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> schedule() throws Exception {
        try {
            return new ResponseEntity<>(scheduleService.main(), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    @RequestMapping(value = "/main/searching.do", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduleVO>> searching(
    		@RequestParam(value ="st", required = false , defaultValue ="") String st, 
    		@RequestParam(value ="y", required = false , defaultValue ="") String y
    		) throws Exception {
    	loggingCurrentMethod(LOGGER, st, y);
        try {
            ScheduleVO scheduleVO = new ScheduleVO();
            scheduleVO.setSearchTypeVA(st);
            if(!isBlank(y)) {
	            scheduleVO.setSearchStartDt(y+"-01-01");
	            scheduleVO.setSearchEndDt(y+"-12-31");
            }
            scheduleVO.setSearchMemberId(SecurityUtils.getLoginMemberId());
            return new ResponseEntity<>(scheduleService.searching(scheduleVO), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 문서번호 및 발급 문서목록 조회
     */
    @RequestMapping(value = "/main/document.do", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> documents(
            @RequestParam(value = "dt") String dt) throws Exception {
        try {
            DocumentVO documentVO = new DocumentVO();
            documentVO.setSearchDocumentDt(dt);

            return new ResponseEntity<>(documentService.main(documentVO), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 문서번호 발급
     */
    @RequestMapping(value = "/main/document.do", method = RequestMethod.POST)
    public ResponseEntity<String> write(@Valid @RequestBody DocumentVO documentVO) throws Exception {
        loggingCurrentMethod(LOGGER, documentVO);

        try {
            String documentNo = documentService.write(documentVO);
            return new ResponseEntity<>("문서번호 \"" + documentNo + "\"를 정상적으로 발급했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("문서번호 발급 중 오류가 발생했습니다", HttpStatus.CONFLICT);
        }
    }

    /**
     * 문서번호 삭제
     */
    @RequestMapping(value = "/main/document/{id}.do", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable("id") int id) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        try {
            documentService.delete(new DocumentVO(id));
            return new ResponseEntity<>("문서번호를 정상적으로 삭제했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("문서번호 삭제 중 오류가 발생했습니다", HttpStatus.CONFLICT);
        }
    }
    
    /**
     * 프로젝트 리스트 
     */
    @RequestMapping(value = "/main/pList.do", method = RequestMethod.GET)
    public ResponseEntity<List<ProjectVO>> pList() throws Exception {
        try {
        	List<ProjectVO> pList = projectService.searchProject(new ProjectVO());
        	return new ResponseEntity<>(pList,HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    /**
     * 공지사항 팝업
     */
    @RequestMapping(value = "/main/noticePopupInfo.do", method = RequestMethod.GET)
    public ResponseEntity<List<NoticeVO>> getPopup() throws Exception {
    	try {
        	List<NoticeVO> nList = noticeService.findNoticePopupInfo(new NoticeVO());
        	return new ResponseEntity<>(nList,HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    
    /**
     * 팝업내용
     */
    @RequestMapping(value = "/popupContent.do")
    public String popupContent(ModelMap map) throws Exception {
        return "main/noticePopup";
    }
    
}