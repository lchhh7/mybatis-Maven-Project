package com.jinjin.jintranet.project.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinjin.jintranet.common.vo.CompanyVO;
import com.jinjin.jintranet.common.vo.ProjectVO;
import com.jinjin.jintranet.company.service.CompanyService;
import com.jinjin.jintranet.project.service.ProjectService;

@Controller
public class ProjectController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

    private final CompanyService companyService;
    
    @Autowired
    public ProjectController(ProjectService projectService,CompanyService companyService) {
    	this.projectService = projectService;
    	this.companyService =  companyService;
    }
    /**
     * 프로젝트관리 메인페이지
     */
    @RequestMapping(value = "/project.do", method = RequestMethod.GET)
    public String main(ModelMap map, HttpServletRequest request) throws Exception {
    	loggingCurrentMethod(LOGGER);
        try {
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "project/project";
    }

    /**
     * 검색
     */
    @RequestMapping(value = "/project/search.do", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> findProject(
            @RequestParam(value = "p", required = false, defaultValue = "1") int page,
            @RequestParam(value = "y", required = false, defaultValue = "") String year,
            @RequestParam(value = "s", required = false, defaultValue = "") String startDt,
            @RequestParam(value = "e", required = false, defaultValue = "") String endDt,
            @RequestParam(value = "t", required = false, defaultValue = "") String title,
            @RequestParam(value = "o", required = false, defaultValue = "") String orderingName,
            @RequestParam(value = "d", required = false, defaultValue = "") String department,
            HttpServletRequest request) throws Exception {

        ProjectVO projectVO = new ProjectVO();
        projectVO.setPageIndex(page);
        projectVO.setSearchYear(year);
        projectVO.setSearchStartDt(startDt);
        projectVO.setSearchEndDt(endDt);
        projectVO.setSearchTitle(title);
        projectVO.setSearchOrderingName(orderingName);
        projectVO.setSearchDepartment(department);

    	loggingCurrentMethod(LOGGER, projectVO);

        return projectService.findProjectAll(projectVO, request);
    }

    /**
     * 신규생성 페이지 이동
     */
    @RequestMapping(value = "/project/write.do", method = RequestMethod.GET)
    public String write(ModelMap map, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            List<CompanyVO> company = companyService.findCompanyAllForProject();

            map.put("company", company);
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "project/project-write";
    }

    @RequestMapping(value = "/project/write.do", method = RequestMethod.POST)
    public ResponseEntity<String> write(@Validated @RequestBody ProjectVO projectVO,BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, projectVO);

        if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }

        return projectService.write(projectVO);
    }

    @RequestMapping(value = "/project/edit.do", method = RequestMethod.GET)
    public String edit(ModelMap map, @RequestParam("id") Integer id, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        try {
            map.putAll(projectService.findProjectById(new ProjectVO(id)));
            List<CompanyVO> company = companyService.findCompanyAllForProject();

            map.put("company", company);
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "project/project-edit";
    }

    @RequestMapping(value = "/project/edit.do", method = RequestMethod.POST)
    public ResponseEntity<String> edit(@Validated @RequestBody ProjectVO projectVO,BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, projectVO);

        if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }

        return projectService.edit(projectVO);
    }

    @RequestMapping(value = "/project.do", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@RequestBody ProjectVO projectVO) throws Exception {
        loggingCurrentMethod(LOGGER, projectVO);
        return projectService.delete(projectVO);
    }
}
