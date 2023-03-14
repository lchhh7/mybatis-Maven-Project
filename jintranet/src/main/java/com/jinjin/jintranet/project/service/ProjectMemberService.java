package com.jinjin.jintranet.project.service;

import com.jinjin.jintranet.code.service.CodeMapper;
import com.jinjin.jintranet.common.constant.Constants;
import com.jinjin.jintranet.common.vo.CodeVO;
import com.jinjin.jintranet.common.vo.CompanyVO;
import com.jinjin.jintranet.common.vo.ProjectDefaultVO;
import com.jinjin.jintranet.common.vo.ProjectMemberVO;
import com.jinjin.jintranet.company.service.CompanyMapper;
import com.jinjin.jintranet.project.web.ProjectMemberController;
import com.jinjin.jintranet.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.project.service.ProjectUtils.removeHyphenP;
import static com.jinjin.jintranet.project.service.ProjectUtils.removeHyphenPM;

@Service
public class ProjectMemberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectMemberController.class);

    private ProjectMapper projectMapper;

    private ProjectMemberMapper projectMemberMapper;

    private CodeMapper codeMapper;

    private CompanyMapper companyMapper;
    
    public ProjectMemberService() {}
    
    @Autowired
    public ProjectMemberService(ProjectMapper projectMapper,ProjectMemberMapper projectMemberMapper,CodeMapper codeMapper,CompanyMapper companyMapper) {
    	this.projectMapper = projectMapper;
    	this.projectMemberMapper = projectMemberMapper;
    	this.codeMapper = codeMapper;
    	this.companyMapper = companyMapper;
    }
    
    public Map<String, Object> findProjectMemberByProjectId(Integer id) {
        Map<String, Object> map = new HashMap<>();

        ProjectDefaultVO vo = new ProjectDefaultVO(id);

        map.put("project", removeHyphenP(projectMapper.findProjectById(vo)));
        map.put("pmList", removeHyphenPM(projectMemberMapper.findProjectMemberByProjectId(vo)));
        map.put("action", codeMapper.findCodeByMajorCd(new CodeVO(Constants.CODE_PROJECT_ACTION)));
        map.put("company", companyMapper.findCompanyAllForProject(new CompanyVO()));
        return map;
    }

    public ResponseEntity<ProjectMemberVO> findProjectMemberById(Integer id) throws Exception {
        try {
            ProjectMemberVO vo = new ProjectMemberVO(id);
            return new ResponseEntity<>(projectMemberMapper.findProjectMemberById(vo), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> write(ProjectMemberVO projectMemberVO) throws Exception {
        try {
            Integer loginMemberId = SecurityUtils.getLoginMemberId();
            projectMemberVO.setCrtId(loginMemberId);
            projectMemberVO.setUdtId(loginMemberId);
            projectMemberMapper.write(projectMemberVO);
            return new ResponseEntity<>("투입인력 정보를 정상적으로 저장했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("투입인력 정보 저장 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }



    public ResponseEntity<String> edit(ProjectMemberVO projectMemberVO) throws Exception {
        try {
            projectMemberVO.setUdtId(SecurityUtils.getLoginMemberId());
            projectMemberMapper.edit(projectMemberVO);
            return new ResponseEntity<>("투입인력 정보를 정상적으로 수정했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("투입인력 정보 수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> delete(ProjectMemberVO projectMemberVO) throws Exception {
        try {
            projectMemberVO.setDelId(SecurityUtils.getLoginMemberId());
            projectMemberMapper.delete(projectMemberVO);
            return new ResponseEntity<>("투입인력 정보를 정상적으로 삭제했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("투입인력 정보 삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }
}
