package com.jinjin.jintranet.project.service;

import com.jinjin.jintranet.common.vo.ProjectDefaultVO;
import com.jinjin.jintranet.common.vo.ProjectMemberVO;
import com.jinjin.jintranet.common.vo.ProjectVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper

public interface ProjectMemberMapper {
    List<ProjectMemberVO> findProjectMemberByProjectId(ProjectDefaultVO pmVO);

    void write(ProjectMemberVO projectMemberVO);

    ProjectMemberVO findProjectMemberById(ProjectMemberVO vo);

    void edit(ProjectMemberVO projectMemberVO);

    void delete(ProjectMemberVO projectMemberVO);

}
