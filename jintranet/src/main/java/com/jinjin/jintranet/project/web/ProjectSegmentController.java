package com.jinjin.jintranet.project.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;

import javax.servlet.http.HttpServletRequest;

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

import com.jinjin.jintranet.common.vo.ProjectSegmentVO;
import com.jinjin.jintranet.project.service.ProjectSegmentService;

@Controller
@RequestMapping("/project")
public class ProjectSegmentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectSegmentController.class);

    private final ProjectSegmentService segmentService;
    
    @Autowired
    public ProjectSegmentController(ProjectSegmentService segmentService) {
    	this.segmentService = segmentService;
    }

    /**
     * 프로젝트 수정 > 테일러링
     */
    @RequestMapping(value = "/tailoring.do", method = RequestMethod.GET)
    public String main(ModelMap map, @RequestParam("id") Integer id, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            map.putAll(segmentService.findProjectSegmentByProjectId(id));
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "project/project-tailoring";
    }

    /**
     * 세그먼트 등록
     */
    @RequestMapping(value = "/tailoring.do", method = RequestMethod.POST)
    public ResponseEntity<String> write(@Validated @RequestBody ProjectSegmentVO projectSegmentVO,BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, projectSegmentVO);

        if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }
        
        return segmentService.write(projectSegmentVO);
    }

    /**
     * 세그먼트 수정 모달 띄우기
     */
    @RequestMapping(value = "/tailoring/{id}.do", method = RequestMethod.GET)
    public ResponseEntity<?> findProjectSegmentById(@PathVariable("id") Integer id) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        return segmentService.findProjectSegmentById(id);
    }

    /**
     * 세그먼트 수정
     */
    @RequestMapping(value = "/tailoring/{id}.do", method = RequestMethod.PATCH)
    public ResponseEntity<String> edit(@PathVariable("id") Integer id, @Validated @RequestBody ProjectSegmentVO projectSegmentVO , BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, projectSegmentVO);

        if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }

        return segmentService.edit(projectSegmentVO);
    }

    /**
     * 세그먼트 삭제
     */
    @RequestMapping(value = "/tailoring.do", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@RequestBody ProjectSegmentVO projectSegmentVO) throws Exception {
        loggingCurrentMethod(LOGGER, projectSegmentVO);
        return segmentService.delete(projectSegmentVO);
    }
}
