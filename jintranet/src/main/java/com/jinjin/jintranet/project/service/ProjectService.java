package com.jinjin.jintranet.project.service;

import com.jinjin.jintranet.common.vo.ProjectVO;
import com.jinjin.jintranet.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.common.util.PageUtils.page;
import static com.jinjin.jintranet.project.service.ProjectUtils.removeHyphenP;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class ProjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);

    private ProjectMapper projectMapper;
    
    public ProjectService() {}
    
    @Autowired
    public ProjectService(ProjectMapper projectMapper) {
    	this.projectMapper = projectMapper;
    }
    
    public List<ProjectVO> findProjectAllForMain(ProjectVO projectVO) {
        return removeHyphenP(projectMapper.findProjectAll(projectVO));
    }

    public ResponseEntity<List<ProjectVO>> findProjectAll(ProjectVO projectVO) throws Exception {
        try {
            return new ResponseEntity<>(removeHyphenP(projectMapper.findProjectAll(projectVO)), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
    public ResponseEntity<Map<String, Object>> findProjectAll(ProjectVO projectVO, HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();

            int totalCnt = projectMapper.getProjectCnt(projectVO);
            projectVO.setTotalCnt(totalCnt);

            List<ProjectVO> list = removeHyphenP(projectMapper.findProjectAll(projectVO));

            map.put("list", list);
            map.put("page", page(projectVO, "projects", request));
            map.put("totalCnt", totalCnt);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public Map<String, Object> findProjectById(ProjectVO projectVO) {
        Map<String, Object> map = new HashMap<>();

        ProjectVO project = projectMapper.findProjectById(projectVO);
        map.put("project", project);

        project.setCompanyKind("C");
        List<ProjectVO> consortiums = projectMapper.findCollaborationCompanyByProjectId(project);
        map.put("consortiums", consortiums);

        project.setCompanyKind("S");
        List<ProjectVO> subcontracts = projectMapper.findCollaborationCompanyByProjectId(project);
        map.put("subcontracts", subcontracts);

        return map;
    }


    public ResponseEntity<String> write(ProjectVO projectVO) throws Exception {
        try {
            Integer loginId = SecurityUtils.getLoginMemberId();

            projectVO.setCrtId(loginId);
            projectVO.setUdtId(loginId);

            List<Integer> consortiums = projectVO.getConsortiums();
            List<Integer> subcontracts = projectVO.getSubcontracts();


            projectMapper.write(projectVO);

            if ("Y".equals(projectVO.getConsortiumYN())) writeCompany(projectVO, consortiums, "C");
            if ("Y".equals(projectVO.getSubcontractYN())) writeCompany(projectVO, subcontracts, "S");

            return new ResponseEntity<>(projectVO.getId().toString(), HttpStatus.CREATED);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("프로젝트 등록 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> edit(ProjectVO projectVO) throws Exception {

        try {
            Integer loginId = SecurityUtils.getLoginMemberId();

            projectVO.setUdtId(loginId);
            projectVO.setDelId(loginId);

            // 컨소시엄, 하도급은 프로젝트 정보 업데이트마다 지우고 새로 만듬
            projectMapper.deleteCollaborationCompany(projectVO);
            projectMapper.edit(projectVO);

            if ("Y".equals(projectVO.getConsortiumYN())) writeCompany(projectVO, projectVO.getConsortiums(), "C");
            if ("Y".equals(projectVO.getSubcontractYN())) writeCompany(projectVO, projectVO.getSubcontracts(), "S");

            return new ResponseEntity<>("프로젝트 기본정보가 정상적으로 수정되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("정보 저장 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }


    private void writeCompany(ProjectVO projectVO, List<Integer> list, String kind) {
        projectVO.setCompanyKind(kind);

        for (Integer id : list) {
            projectVO.setCompanyId(id);
            projectMapper.writeCompany(projectVO);
        }
    }

    private boolean isEmptyFieldProjectVO(ProjectVO projectVO) {
        if (isBlank(projectVO.getTitle()) || isBlank(projectVO.getOrderingName()) || isBlank(projectVO.getContractNo()) ||
                isBlank(projectVO.getBusinessField()) || isBlank(projectVO.getStartDt()) || isBlank(projectVO.getEndDt()) ||
                isBlank(projectVO.getLaunchDt()) || isBlank(projectVO.getDepartment()) ||
                isBlank(projectVO.getProjectManagerName()) || isBlank(projectVO.getPerformanceRegistYN())
        ) return true;

        if (projectVO.getAmountSurtaxInclude() == null || projectVO.getAmountSurtaxExclude() == null ||
                projectVO.getContractDeposit() == null || projectVO.getContractDepositRate() == null ||
                projectVO.getDefectDeposit() == null || projectVO.getDefectDepositRate() == null
        ) return true;

        return false;
    }
    private boolean isOver100Rate(ProjectVO projectVO) {
        return (projectVO.getContractDepositRate() > 100 || projectVO.getDefectDepositRate() > 100);
    }
    private boolean isNotValidDepartment(ProjectVO projectVO) {
        return false;
    }

    private boolean isNotValidProjectDate(ProjectVO projectVO) {


        return false;
    }


    public ResponseEntity<String> delete(ProjectVO projectVO) throws Exception {
        try {
            projectMapper.delete(projectVO);
            return new ResponseEntity<>("프로젝트 정보를 정상적으로 삭제했습니다", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("프로젝트 정보 삭제 중 오류가 발생했습니다", HttpStatus.CONFLICT);
        }

    }
    
    public List<ProjectVO> searchProject(ProjectVO projectVO) {
    	return projectMapper.searchProject(projectVO);
    }
}
