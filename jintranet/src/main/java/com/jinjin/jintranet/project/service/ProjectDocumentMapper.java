package com.jinjin.jintranet.project.service;

import com.jinjin.jintranet.common.vo.*;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface ProjectDocumentMapper {
    
    List<ProjectDocumentVO> findProjectDocumentByProjectId(ProjectDefaultVO projectDefaultVO);

    ProjectDocumentVO findProjectDocumentById(ProjectDocumentVO projectDocumentVO);

    void write(ProjectDocumentVO projectDocumentVO);

    void edit(ProjectDocumentVO projectDocumentVO);

    void delete(ProjectDefaultVO projectDefaultVO);

    void writeDocumentHistory(ProjectDocumentVO projectDocumentVO);

    List<ProjectDocumentVO> findDocumentHistoryById(ProjectDocumentVO projectDocumentVO);

    void deleteAttach(ProjectDocumentVO projectDocumentVO);

    ProjectDocumentVO downloadHistory(ProjectDocumentVO projectDocumentVO);
}
