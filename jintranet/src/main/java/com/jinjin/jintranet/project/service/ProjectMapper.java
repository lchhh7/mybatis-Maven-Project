package com.jinjin.jintranet.project.service;

import com.jinjin.jintranet.common.vo.ProjectDefaultVO;
import com.jinjin.jintranet.common.vo.ProjectVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface ProjectMapper {

    List<ProjectVO> findProjectAll(ProjectVO projectVO);

    ProjectVO findProjectById(ProjectDefaultVO projectVO);

    List<ProjectVO> findCollaborationCompanyByProjectId(ProjectVO project);

    void write(ProjectVO projectVO);

    void writeCompany(ProjectVO projectVO);

    void edit(ProjectVO projectVO);

    void deleteCollaborationCompany(ProjectVO projectVO);

    void delete(ProjectVO projectVO);

    int getProjectCnt(ProjectVO projectVO);
    
    List<ProjectVO> searchProject(ProjectVO projectVO);
}
