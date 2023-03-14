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

import com.jinjin.jintranet.common.vo.ProjectMemberVO;
import com.jinjin.jintranet.project.service.ProjectMemberService;

@Controller
@RequestMapping("/project")
public class ProjectMemberController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectMemberController.class);

    private final ProjectMemberService projectMemberService;
    
    @Autowired
    public ProjectMemberController(ProjectMemberService projectMemberService) {
    	this.projectMemberService = projectMemberService;
    }

    /**
     * 프로젝트 수정 > 투입인력
     */
    @RequestMapping(value = "/member.do", method = RequestMethod.GET)
    public String main(ModelMap map, @RequestParam("id") Integer id, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            map.putAll(projectMemberService.findProjectMemberByProjectId(id));
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "project/project-member";
    }

    /**
     * 투입인력 등록
     */
    @RequestMapping(value = "/member.do", method = RequestMethod.POST)
    public ResponseEntity<String> write(@Validated @RequestBody ProjectMemberVO projectMemberVO,BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, projectMemberVO);

        if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }

        return projectMemberService.write(projectMemberVO);
    }

    /**
     * 투입인력 수정 모달 띄우기
     */
    @RequestMapping(value = "/member/{id}.do", method = RequestMethod.GET)
    public ResponseEntity<?> findProjectMemberById(@PathVariable("id") Integer id) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        return projectMemberService.findProjectMemberById(id);
    }

    /**
     * 투입인력 수정
     */
    @RequestMapping(value = "/member/{id}.do", method = RequestMethod.PATCH)
    public ResponseEntity<String> edit(@PathVariable("id") Integer id, @Validated @RequestBody ProjectMemberVO projectMemberVO, BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, projectMemberVO);

        if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }

        return projectMemberService.edit(projectMemberVO);
    }

    /**
     * 투입인력 삭제
     */
    @RequestMapping(value = "/member.do", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@RequestBody ProjectMemberVO projectMemberVO) throws Exception {
        loggingCurrentMethod(LOGGER, projectMemberVO);
        return projectMemberService.delete(projectMemberVO);
    }
}
