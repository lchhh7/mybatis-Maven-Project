package com.jinjin.jintranet.project.service;

import com.jinjin.jintranet.common.vo.ProjectDefaultVO;
import com.jinjin.jintranet.common.vo.ProjectScheduleVO;
import com.jinjin.jintranet.common.vo.ProjectSegmentVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

import java.util.List;

@Mapper
public interface ProjectScheduleMapper {

    List<ProjectScheduleVO> findProjectScheduleByProjectId(ProjectDefaultVO projectDefaultVO);

    List<ProjectScheduleVO> findProjectScheduleByKind(ProjectScheduleVO projectScheduleVO);

    ProjectScheduleVO findProjectScheduleById(ProjectScheduleVO projectScheduleVO);

    void write(ProjectScheduleVO projectScheduleVO);

    void edit(ProjectScheduleVO projectScheduleVO);

    void delete(ProjectDefaultVO projectDefaultVO);

}
