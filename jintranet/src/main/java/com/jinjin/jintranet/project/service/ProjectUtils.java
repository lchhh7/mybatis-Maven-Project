package com.jinjin.jintranet.project.service;

import com.jinjin.jintranet.common.vo.ProjectMemberVO;
import com.jinjin.jintranet.common.vo.ProjectVO;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ProjectUtils {
    static List<ProjectVO> removeHyphenP(List<ProjectVO> list) {
        for (ProjectVO vo : list) {
            removeHyphenP(vo);
        }

        return list;
    }

    static ProjectVO removeHyphenP(ProjectVO projectVO) {
        String startDt = projectVO.getStartDt();
        String endDt = projectVO.getEndDt();
        String launchDt = projectVO.getLaunchDt();

        if (!isBlank(startDt)) projectVO.setStartDt(startDt.replaceAll("-", ""));
        if (!isBlank(endDt)) projectVO.setEndDt(endDt.replaceAll("-", ""));
        if (!isBlank(launchDt)) projectVO.setLaunchDt(launchDt.replaceAll("-", ""));

        return projectVO;
    }

    static List<ProjectMemberVO> removeHyphenPM(List<ProjectMemberVO> list) {
        for (ProjectMemberVO vo : list) {
            removeHyphenPM(vo);
        }

        return list;
    }

    static ProjectMemberVO removeHyphenPM(ProjectMemberVO projectMemberVO) {
        String startDt = projectMemberVO.getManStartDt();
        String endDt = projectMemberVO.getManEndDt();

        if (!isBlank(startDt)) projectMemberVO.setManStartDt(startDt.replaceAll("-", ""));
        if (!isBlank(endDt)) projectMemberVO.setManEndDt(endDt.replaceAll("-", ""));

        return projectMemberVO;
    }
}
