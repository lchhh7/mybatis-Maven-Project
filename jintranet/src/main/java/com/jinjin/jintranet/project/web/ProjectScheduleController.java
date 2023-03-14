package com.jinjin.jintranet.project.web;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingCurrentMethod;
import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.MenuUtils.getDefaultMenu;

import java.util.List;

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

import com.jinjin.jintranet.common.vo.ProjectDefaultVO;
import com.jinjin.jintranet.common.vo.ProjectScheduleVO;
import com.jinjin.jintranet.project.service.ProjectScheduleService;

@Controller
@RequestMapping("/project")
public class ProjectScheduleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectScheduleController.class);

    private final ProjectScheduleService scheduleService;
    
    @Autowired
    public ProjectScheduleController(ProjectScheduleService scheduleService) {
    	this.scheduleService = scheduleService;
    }

    /**
     * 프로젝트 수정 > 일정
     */
    @RequestMapping(value = "/schedule.do", method = RequestMethod.GET)
    public String main(ModelMap map, @RequestParam("id") Integer id, HttpServletRequest request) throws Exception {
        loggingCurrentMethod(LOGGER);
        try {
            map.putAll(scheduleService.findProjectScheduleByProjectId(id));
            map.putAll(getDefaultMenu(request));
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
        }

        return "project/project-schedule";
    }

    @RequestMapping(value = "/schedules.do", method = RequestMethod.POST)
    public ResponseEntity<List<ProjectScheduleVO>> findProjectScheduleByKind(
            @RequestBody ProjectScheduleVO projectScheduleVO) throws Exception {
        loggingCurrentMethod(LOGGER, projectScheduleVO);
        return scheduleService.findProjectScheduleByKind(projectScheduleVO);
    }

    /**
     * 프로젝트 일정 등록
     */
    @RequestMapping(value = "/schedule.do", method = RequestMethod.POST)
    public ResponseEntity<String> write(@Validated @RequestBody ProjectScheduleVO projectScheduleVO, BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, projectScheduleVO);

        if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }

        return scheduleService.write(projectScheduleVO);
    }

    /**
     * 프로젝트 일정 모달 띄우기
     */
    @RequestMapping(value = "/schedule/{id}.do", method = RequestMethod.GET)
    public ResponseEntity<ProjectScheduleVO> findProjectSegmentById(@PathVariable("id") Integer id) throws Exception {
        loggingCurrentMethod(LOGGER, id);
        return scheduleService.findProjectScheduleById(id);
    }

    /**
     * 프로젝트 일정 수정
     */
    @RequestMapping(value = "/schedule/{id}.do", method = RequestMethod.PATCH)
    public ResponseEntity<String> edit(@PathVariable("id") Integer id,@Validated @RequestBody ProjectScheduleVO projectScheduleVO , BindingResult bindingResult) throws Exception {
        loggingCurrentMethod(LOGGER, projectScheduleVO);

        if (bindingResult.hasErrors()) {
         	return new ResponseEntity<>(bindingResult.getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
         }

        return scheduleService.edit(projectScheduleVO);
    }

    /**
     * 프로젝트 일정 삭제
     */
    @RequestMapping(value = "/schedule.do", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@RequestBody ProjectDefaultVO vo) throws Exception {
        loggingCurrentMethod(LOGGER, vo);
        return scheduleService.delete(vo);
    }
}
