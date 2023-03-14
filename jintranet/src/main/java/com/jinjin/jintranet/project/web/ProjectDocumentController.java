package com.jinjin.jintranet.project.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jinjin.jintranet.common.util.FileUtils;
import com.jinjin.jintranet.common.vo.AttachVO;
import com.jinjin.jintranet.common.vo.CodeVO;
import com.jinjin.jintranet.common.vo.ProjectDocumentVO;
import com.jinjin.jintranet.project.service.ProjectDocumentService;

@Controller
@RequestMapping("/project")
public class ProjectDocumentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDocumentController.class);

    private final ProjectDocumentService documentService;
    
    @Autowired
    public ProjectDocumentController(ProjectDocumentService documentService) {
    	this.documentService = documentService;
    }

    /**
     * 프로젝트 수정 > 문서
     */
    @RequestMapping(value = "/document.do", method = RequestMethod.GET)
    public String main(ModelMap map, @RequestParam("id") Integer id, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            map.putAll(documentService.findProjectDocumentByProjectId(id));
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "project/project-document";
    }

    /**
     * 문서 등록
     */
    @RequestMapping(value = "/document.do", method = RequestMethod.POST)
    public ResponseEntity<String> write(@Validated @RequestBody ProjectDocumentVO documentVO,BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, documentVO);

        if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }

        if ("F".equals(documentVO.getKind())) {
            if (isBlank(documentVO.getPath()) || isBlank(documentVO.getOriginalFileName()) ||
                    isBlank(documentVO.getStoredFileName()) || documentVO.getFileSize() <= 0) {
                return new ResponseEntity<>("파일을 업로드해주세요.", HttpStatus.BAD_REQUEST);
            }
        } else {
            if ("http://".equals(documentVO.getPath())) {
                return new ResponseEntity<>("관련링크를 입력해주세요.", HttpStatus.BAD_REQUEST);
            }
        }


        return documentService.write(documentVO);
    }

    /**
     * 문서 수정 모달 띄우기
     */
    @RequestMapping(value = "/document/{id}.do", method = RequestMethod.GET)
    public ResponseEntity<?> findProjectSegmentById(@PathVariable("id") Integer id) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        return documentService.findProjectDocumentById(id);
    }

    /**
     * 문서 수정
     */
    @RequestMapping(value = "/document/{id}.do", method = RequestMethod.PATCH)
    public ResponseEntity<String> edit(@PathVariable("id") Integer id, @Validated @RequestBody ProjectDocumentVO documentVO, BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, documentVO);

        if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }

        if ("F".equals(documentVO.getKind())) {
            if (isBlank(documentVO.getPath()) || isBlank(documentVO.getOriginalFileName()) ||
                    isBlank(documentVO.getStoredFileName()) || documentVO.getFileSize() <= 0) {
                return new ResponseEntity<>("파일을 업로드해주세요.", HttpStatus.BAD_REQUEST);
            }
        } else {
            if ("http://".equals(documentVO.getPath())) {
                return new ResponseEntity<>("관련링크를 입력해주세요.", HttpStatus.BAD_REQUEST);
            }
        }

        return documentService.edit(documentVO);
    }

    /**
     * 문서 삭제
     */
    @RequestMapping(value = "/document.do", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@RequestBody ProjectDocumentVO documentVO) throws Exception {
        loggingCurrentMethod(LOGGER, documentVO);
        return documentService.delete(documentVO);
    }
    /**
     * Segment 선택 > Task 조회
     */
    @RequestMapping(value = "/document/task.do", method = RequestMethod.POST)
    public ResponseEntity<List<CodeVO>> findTaskBySegment(@RequestBody CodeVO codeVO) throws Exception {
        loggingCurrentMethod(LOGGER, codeVO);
        return documentService.findTaskBySegment(codeVO);
    }

    /**
     * 문서 이력 조회
     */
    @RequestMapping(value = "/document/history/{id}.do", method = RequestMethod.GET)
    public ResponseEntity<List<ProjectDocumentVO>> findTaskBySegment(@PathVariable("id") Integer id) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        return documentService.findDocumentHistoryById(new ProjectDocumentVO(id));
    }

    /**
     * 문서 > 첨부파일 업로드
     */
    @RequestMapping(value = "/document/upload", method = RequestMethod.POST)
    public ResponseEntity<List<AttachVO>> upload(MultipartHttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            return new ResponseEntity<>(FileUtils.upload(request, "pd_attach"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * 문서 > 첨부파일 삭제
     */
    @RequestMapping(value = "/document/attach", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAttach(@RequestParam("id") Integer id) throws Exception {
        return documentService.deleteAttach(id);
    }

    /**
     * 문서 > 첨부파일 다운로드
     */
    @RequestMapping(value = "/document/download", method = RequestMethod.POST)
    public void download(@RequestParam int id,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        documentService.download(id, request, response);
    }
    /**
     * 문서 > 이력 > 첨부파일 다운로드
     */
    @RequestMapping(value = "/document/downloadHistory", method = RequestMethod.POST)
    public void downloadHistory(@RequestParam int id,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        documentService.downloadHistory(id, request, response);
    }
}