package com.jinjin.jintranet.project.service;

import com.jinjin.jintranet.code.service.CodeMapper;
import com.jinjin.jintranet.common.constant.Constants;
import com.jinjin.jintranet.common.vo.*;
import com.jinjin.jintranet.project.web.ProjectScheduleController;
import com.jinjin.jintranet.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.project.service.ProjectUtils.removeHyphenP;

@Service
public class ProjectScheduleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectScheduleService.class);

    private ProjectScheduleMapper scheduleMapper;

    private ProjectMapper projectMapper;

    private CodeMapper codeMapper;

    public ProjectScheduleService() {}
    
    @Autowired
    public ProjectScheduleService(ProjectScheduleMapper scheduleMapper,ProjectMapper projectMapper,CodeMapper codeMapper) {
    	this.scheduleMapper = scheduleMapper;
    	this.projectMapper = projectMapper;
    	this.codeMapper = codeMapper;
    }
    
    public Map<String, ?> findProjectScheduleByProjectId(Integer id) {
        Map<String, Object> map = new HashMap<>();

        ProjectDefaultVO vo = new ProjectDefaultVO(id);

        map.put("project", removeHyphenP(projectMapper.findProjectById(vo)));
        map.put("schedule", codeMapper.findCodeByMajorCd(new CodeVO(Constants.CODE_PROJECT_SCHEDULE)));
        return map;
    }

    public ResponseEntity<List<ProjectScheduleVO>> findProjectScheduleByKind(ProjectScheduleVO projectScheduleVO) {
        try {
            return new ResponseEntity<>(scheduleMapper.findProjectScheduleByKind(projectScheduleVO), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<ProjectScheduleVO> findProjectScheduleById(Integer id) throws Exception {
        try {
            ProjectScheduleVO vo = new ProjectScheduleVO(id);
            return new ResponseEntity<>(scheduleMapper.findProjectScheduleById(vo), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> write(ProjectScheduleVO projectScheduleVO) throws Exception {
        try {
            Integer loginMemberId = SecurityUtils.getLoginMemberId();
            projectScheduleVO.setCrtId(loginMemberId);
            projectScheduleVO.setUdtId(loginMemberId);
            scheduleMapper.write(projectScheduleVO);

            return new ResponseEntity<>("일정 정보를 정상적으로 저장했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("일정 정보 저장 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> edit(ProjectScheduleVO projectScheduleVO) throws Exception {
        try {
            projectScheduleVO.setUdtId(SecurityUtils.getLoginMemberId());
            scheduleMapper.edit(projectScheduleVO);
            return new ResponseEntity<>("일정 정보를 정상적으로 수정했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("일정 정보 수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> delete(ProjectDefaultVO vo) throws Exception {
        try {
            vo.setDelId(SecurityUtils.getLoginMemberId());
            scheduleMapper.delete(vo);
            return new ResponseEntity<>("일정 정보를 정상적으로 삭제했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("일정 정보 삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }


}
