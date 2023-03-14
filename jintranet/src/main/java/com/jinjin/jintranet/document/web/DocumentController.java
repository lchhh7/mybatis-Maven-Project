package com.jinjin.jintranet.document.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinjin.jintranet.common.vo.DocumentVO;
import com.jinjin.jintranet.common.vo.ProjectVO;
import com.jinjin.jintranet.document.service.DocumentService;
import com.jinjin.jintranet.project.service.ProjectService;

@Controller
public class DocumentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);

    private final DocumentService documentService;
    
    private final ProjectService projectService;
	
    @Autowired
    public DocumentController(DocumentService documentService,ProjectService projectService) {
    	this.documentService = documentService;
    	this.projectService = projectService;
    }
    /**
     * 문서관리 메인페이지
     */
    @RequestMapping(value = "/document.do", method = RequestMethod.GET)
    public String main(ModelMap map, HttpServletRequest request) throws Exception {
    	loggingCurrentMethod(LOGGER);
        try {
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "document/document";
    }
    
    /**
     * 문서번호 및 발급 문서목록 조회
     */
    @RequestMapping(value = "/document/info.do", method = RequestMethod.GET)
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
     * 문서번호 발급,출력 페이지 이동
     */
    @RequestMapping(value = "/document/writePage.do", method = RequestMethod.GET)
    public String writePage(ModelMap map, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	sdf.format(new Date());
        	DocumentVO documentVO = new DocumentVO();
        	documentVO.setSearchDocumentDt(sdf.format(new Date())); //임시
        	map.put("today",documentVO.getSearchDocumentDt());
        	
        	 map.put("projectList",projectService.searchProject(new ProjectVO()));
        	 map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }
        return "document/document-write";
    }
    
    /**
     *메인화면에서 문서번호 발급 페이지로 이동
     */
    @RequestMapping(value = "/document/mainToWritePage.do", method = RequestMethod.GET)
    public String mainToWritePage(ModelMap map, HttpServletRequest request,
    	@RequestParam(value = "dt", required = false, defaultValue = "") String dt) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
        	DocumentVO documentVO = new DocumentVO();
        	documentVO.setSearchDocumentDt(dt);
        	map.put("today",documentVO.getSearchDocumentDt());
        	
        	 map.put("projectList",projectService.searchProject(new ProjectVO()));
        	 map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }
        return "document/document-write";
    }
    
    		
    /**
     * 문서번호 발급
     */
    @RequestMapping(value = "/document/write.do", method = RequestMethod.POST)
    public ResponseEntity<String> write(@RequestBody DocumentVO documentVO) throws Exception {
        loggingCurrentMethod(LOGGER, documentVO);
        try {
            documentService.write(documentVO);
            return new ResponseEntity<>("문서번호 \"" + documentVO.getDocumentNo() + "\"를 정상적으로 발급했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("문서번호 발급 중 오류가 발생했습니다", HttpStatus.CONFLICT);
        }
    }
    
    @RequestMapping(value = "/document/edit.do", method = RequestMethod.GET)
    public String edit(ModelMap map, @RequestParam("id") Integer id, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        try {
        	
        	map.put("document", documentService.findDocumentById(new DocumentVO(id)));
        	map.put("projectList",projectService.searchProject(new ProjectVO()));
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "document/document-edit";
    }

    @RequestMapping(value = "/document/edit.do", method = RequestMethod.POST)
    public ResponseEntity<String> edit(@RequestBody DocumentVO documentVO) throws Exception {
        loggingCurrentMethod(LOGGER, documentVO);
        try {
        	documentService.edit(documentVO);
        	return new ResponseEntity<>("문서번호 \"" + documentVO.getDocumentNo() + "\"의 내용을 수정했습니다.", HttpStatus.OK);
        } catch(Exception e) {
        	loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("문서번호 수정 중 오류가 발생했습니다", HttpStatus.CONFLICT);
        }
    }
    
    @RequestMapping(value = "/document/delete.do", method = RequestMethod.POST)
    public ResponseEntity<String> delete(@RequestBody DocumentVO documentVO) throws Exception {
        loggingCurrentMethod(LOGGER, documentVO);
        try {
        	documentService.delete(documentVO);
        	return new ResponseEntity<>("문서를 삭제했습니다.", HttpStatus.OK);
        } catch(Exception e) {
        	loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("문서삭제 중 오류가 발생했습니다", HttpStatus.CONFLICT);
        }
    }
    
    @RequestMapping(value = "/document/search.do", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> searching(
    		 @RequestParam(value = "p", required = false, defaultValue = "1") int page,
    		@RequestParam(value ="sy", required = false , defaultValue ="") String sy, 
    		@RequestParam(value ="sdt", required = false , defaultValue ="") String sdt,
    		@RequestParam(value ="edt", required = false , defaultValue ="") String edt,
    		@RequestParam(value ="sn", required = false , defaultValue ="") String sn,
    		@RequestParam(value ="spn", required = false , defaultValue ="") String spn,
    		HttpServletRequest request
    		) throws Exception {
    	
            DocumentVO documentVO = new DocumentVO();
            documentVO.setPageIndex(page);
            documentVO.setSearchYear(sy);
            documentVO.setSearchStartDt(sdt);
            documentVO.setSearchEndDt(edt);
            documentVO.setSearchDocumentName(sn);
            documentVO.setSearchProjectName(spn);
            if(!isBlank(sy)) {
            	documentVO.setSearchStartDt(sy+"-01-01");
            	documentVO.setSearchEndDt(sy+"-12-31");
            }
            return documentService.searching(documentVO,request);
    }
}
