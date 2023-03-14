package com.jinjin.jintranet.project.service;

import com.jinjin.jintranet.code.service.CodeMapper;
import com.jinjin.jintranet.common.constant.Constants;
import com.jinjin.jintranet.common.util.FileUtils;
import com.jinjin.jintranet.common.vo.*;
import com.jinjin.jintranet.common.vo.ProjectDocumentVO;
import com.jinjin.jintranet.security.SecurityUtils;

import lombok.NoArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jinjin.jintranet.common.util.LoggingUtils.loggingStackTrace;
import static com.jinjin.jintranet.project.service.ProjectUtils.removeHyphenP;

@Service
@NoArgsConstructor
public class ProjectDocumentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDocumentService.class);

    private ProjectDocumentMapper documentMapper;

    private ProjectMapper projectMapper;

    private CodeMapper codeMapper;

    @Autowired
    public ProjectDocumentService(ProjectDocumentMapper documentMapper,ProjectMapper projectMapper,CodeMapper codeMapper) {
    	this.documentMapper = documentMapper;
    	this.projectMapper = projectMapper;
    	this.codeMapper = codeMapper;
    }

    public Map<String, ?> findProjectDocumentByProjectId(Integer id) {
        Map<String, Object> map = new HashMap<>();

        ProjectDefaultVO vo = new ProjectDefaultVO(id);

        map.put("project", removeHyphenP(projectMapper.findProjectById(vo)));
        map.put("documents", documentMapper.findProjectDocumentByProjectId(vo));
        map.put("segment", codeMapper.findCodeByMajorCd(new CodeVO(Constants.CODE_PROJECT_SEGMENT)));
        map.put("kind", codeMapper.findCodeByMajorCd(new CodeVO(Constants.CODE_PROJECT_DOCUMENT)));
        return map;
    }

    public ResponseEntity<?> findProjectDocumentById(Integer id) throws Exception {
        try {
            ProjectDocumentVO vo = new ProjectDocumentVO(id);
            return new ResponseEntity<>(documentMapper.findProjectDocumentById(vo), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> write(ProjectDocumentVO projectDocumentVO) throws Exception {
        try {
            Integer loginMemberId = SecurityUtils.getLoginMemberId();
            projectDocumentVO.setCrtId(loginMemberId);
            projectDocumentVO.setUdtId(loginMemberId);
            projectDocumentVO.setVersion(1); //추가
            documentMapper.write(projectDocumentVO);

            projectDocumentVO.setAction("I");

            documentMapper.writeDocumentHistory(projectDocumentVO);

            return new ResponseEntity<>("문서 정보를 정상적으로 저장했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("문서 정보 저장 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> edit(ProjectDocumentVO projectDocumentVO) throws Exception {
        try {
            Integer loginMemberId = SecurityUtils.getLoginMemberId();
            projectDocumentVO.setCrtId(loginMemberId);
            projectDocumentVO.setUdtId(loginMemberId);

            Integer version = projectDocumentVO.getVersion();
            projectDocumentVO.setVersion(++version);

            documentMapper.edit(projectDocumentVO);

            projectDocumentVO.setAction("U");

            documentMapper.writeDocumentHistory(projectDocumentVO);

            return new ResponseEntity<>("문서 정보를 정상적으로 수정했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("문서 정보 수정 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> delete(ProjectDefaultVO vo) throws Exception {
        try {
            vo.setDelId(SecurityUtils.getLoginMemberId());
            documentMapper.delete(vo);
            return new ResponseEntity<>("문서 정보를 정상적으로 삭제했습니다.", HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("문서 정보 삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<List<CodeVO>> findTaskBySegment(CodeVO codeVO) throws Exception {
        try {
            codeVO.setMajorCd(Constants.CODE_PROJECT_TASK);
            return new ResponseEntity<>(codeMapper.findCodeByMajorCdAndCodeFg(codeVO), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<List<ProjectDocumentVO>> findDocumentHistoryById(ProjectDocumentVO projectDocumentVO) throws Exception {
        try {
            return new ResponseEntity<>(documentMapper.findDocumentHistoryById(projectDocumentVO), HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> deleteAttach(Integer id) throws Exception {
        try {
            documentMapper.deleteAttach(new ProjectDocumentVO(id));

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            loggingStackTrace(e, LOGGER);
            return new ResponseEntity<>("첨부파일 삭제 중 오류가 발생했습니다.", HttpStatus.CONFLICT);
        }
    }

    public void download(int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProjectDocumentVO projectDocumentVO = documentMapper.findProjectDocumentById(new ProjectDocumentVO(id));

        AttachVO vo = new AttachVO();
        vo.setPath(projectDocumentVO.getPath());
        vo.setOriginalFileName(projectDocumentVO.getOriginalFileName());
        vo.setStoredFileName(projectDocumentVO.getStoredFileName());
        vo.setFileSize(projectDocumentVO.getFileSize());

        if (vo != null) {
            FileUtils.download(vo, request, response);
        }
    }

    public void downloadHistory(int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProjectDocumentVO projectDocumentVO = new ProjectDocumentVO();
        projectDocumentVO.setHistoryId(id);

        projectDocumentVO = documentMapper.downloadHistory(projectDocumentVO);
        AttachVO vo = new AttachVO();
        vo.setPath(projectDocumentVO.getPath());
        vo.setOriginalFileName(projectDocumentVO.getOriginalFileName());
        vo.setStoredFileName(projectDocumentVO.getStoredFileName());
        vo.setFileSize(projectDocumentVO.getFileSize());

        if (vo != null) {
            FileUtils.download(vo, request, response);
        }
    }
}
