package com.jinjin.jintranet.project.service;

import com.jinjin.jintranet.code.service.CodeMapper;
import com.jinjin.jintranet.common.constant.Constants;
import com.jinjin.jintranet.common.vo.*;
import com.jinjin.jintranet.project.web.ProjectSegmentController;
import com.jinjin.jintranet.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.project.service.ProjectUtils.removeHyphenP;
import static com.jinjin.jintranet.project.service.ProjectUtils.removeHyphenPM;

@Service
public class ProjectSegmentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectSegmentController.class);

    private ProjectSegmentMapper segmentMapper;

    private ProjectMapper projectMapper;

    private CodeMapper codeMapper;
    
    public ProjectSegmentService() {}
    
    @Autowired
    public ProjectSegmentService(ProjectSegmentMapper segmentMapper,ProjectMapper projectMapper,CodeMapper codeMapper) {
    	this.segmentMapper = segmentMapper;
    	this.projectMapper = projectMapper;
    	this.codeMapper =codeMapper;
    }
    
    public Map<String, ?> findProjectSegmentByProjectId(Integer id) {
        Map<String, Object> map = new HashMap<>();

        ProjectDefaultVO vo = new ProjectDefaultVO(id);

        map.put("project", removeHyphenP(projectMapper.findProjectById(vo)));
        map.put("segList", segmentMapper.findProjectSegmentByProjectId(vo));
        map.put("segment", codeMapper.findCodeByMajorCd(new CodeVO(Constants.CODE_PROJECT_SEGMENT)));
        return map;
    }

    public ResponseEntity<?> findProjectSegmentById(Integer id) throws Exception {
        try {
            ProjectSegmentVO vo = new ProjectSegmentVO(id);
            return new ResponseEntity<>(segmentMapper.findProjectSegmentById(vo), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> write(ProjectSegmentVO projectSegmentVO) throws Exception {
        try {
            Integer loginMemberId = SecurityUtils.getLoginMemberId();
            projectSegmentVO.setCrtId(loginMemberId);
            projectSegmentVO.setUdtId(loginMemberId);
            segmentMapper.write(projectSegmentVO);

            return new ResponseEntity<>("세그먼트 정보를 정상적으로 저장했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("세그먼트 정보 저장 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> edit(ProjectSegmentVO projectSegmentVO) throws Exception {
        try {
            projectSegmentVO.setUdtId(SecurityUtils.getLoginMemberId());
            segmentMapper.edit(projectSegmentVO);
            return new ResponseEntity<>("세그먼트 정보를 정상적으로 수정했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("세그먼트 정보 수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> delete(ProjectSegmentVO projectSegmentVO) throws Exception {
        try {
            projectSegmentVO.setDelId(SecurityUtils.getLoginMemberId());
            segmentMapper.delete(projectSegmentVO);
            return new ResponseEntity<>("세그먼트 정보를 정상적으로 삭제했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("세그먼트 정보 삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

}
