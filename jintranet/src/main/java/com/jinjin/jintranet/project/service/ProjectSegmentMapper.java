package com.jinjin.jintranet.project.service;

import com.jinjin.jintranet.common.vo.ProjectDefaultVO;
import com.jinjin.jintranet.common.vo.ProjectSegmentVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface ProjectSegmentMapper {

    List<ProjectSegmentVO> findProjectSegmentByProjectId(ProjectDefaultVO vo);

    ProjectSegmentVO findProjectSegmentById(ProjectSegmentVO projectSegmentVO);

    void write(ProjectSegmentVO projectSegmentVO);

    void edit(ProjectSegmentVO projectSegmentVO);

    void delete(ProjectDefaultVO projectDefaultVO);

}
