package com.jinjin.jintranet.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinjin.jintranet.auth.service.AuthMapper;
import com.jinjin.jintranet.code.service.CodeMapper;
import com.jinjin.jintranet.common.constant.Constants;
import com.jinjin.jintranet.common.vo.AuthVO;
import com.jinjin.jintranet.common.vo.CodeVO;
import com.jinjin.jintranet.common.vo.MemberVO;
import com.jinjin.jintranet.common.vo.ScheduleVO;
import com.jinjin.jintranet.member.service.MemberMapper;
import com.jinjin.jintranet.schedule.service.ScheduleMapper;
import com.jinjin.jintranet.security.SecurityUtils;

@Component
public class MenuUtils {
    private static AuthMapper authMapper;
    private static CodeMapper codeMapper;
    private static ScheduleMapper scheduleMapper;
    private static MemberMapper memberMapper;
    @Autowired
    public MenuUtils(AuthMapper authMapper, CodeMapper codeMapper, ScheduleMapper scheduleMapper, MemberMapper memberMapper) {
        this.authMapper = authMapper;
        this.codeMapper = codeMapper;
        this.scheduleMapper = scheduleMapper;
        this.memberMapper = memberMapper;
    }

    private static String getMenuListByMember(MemberVO memberVO, HttpServletRequest request) {
        String contextPath = request.getContextPath();

        StringBuffer sb = new StringBuffer();
        
        sb.append("<li><a class='m_icon1' href='" + contextPath + "/notice.do'>공지사항</a></li>");
        sb.append("<li><a class='m_icon2' href='" + contextPath + "/schedule.do'>일정관리</a></li>");
        sb.append("<li><a class='m_icon3' href='" + contextPath + "/commuting.do'>근태관리</a></li>");
        sb.append("<li><a class='m_icon4' href='" + contextPath + "/supply.do'>비품관리</a></li>");
        sb.append("<li><a class='m_icon5' href='" + contextPath + "/project.do'>프로젝트관리</a></li>");
        sb.append("<li><a class='m_icon10' href='" + contextPath + "/document.do'>문서발급관리</a></li>");

        AuthVO authVO = new AuthVO();
        authVO.setMemberId(memberVO.getId());

        List<AuthVO> authList = authMapper.findAuthByMemberId(authVO);
        for (AuthVO vo : authList) {
            switch (vo.getId()) {
                case 1:
                    break;
                case 2:
                    sb.append("<li><a class='m_icon6' href='" + contextPath + "/admin/member.do'>사용자관리(관)</a></li>");
                    break;
                case 3:
                    sb.append("<li><a class='m_icon7' id='m_icon7' href='" + contextPath + "/admin/schedule.do'>일정신청관리(관)</a></li>");
                    sb.append("<li><a class='m_icon8' id='m_icon8' href='" + contextPath + "/admin/commuting.do'>근태수정관리(관)</a></li>");
                    break;
                case 4:
                    break;
                case 5:
                    sb.append("<li><a class='m_icon9' href='" + contextPath + "/admin/company.do'>업체관리(관)</a></li>");
                    break;
            }
        }

        String[] arr = request.getServletPath().split("/");
        String path = arr[1].split(".do")[0];

        path = ("admin".equals(path)) ? (path + "/" + arr[2]).split(".do")[0] : path;

        int n = -1;
        switch (path) {
            case "notice":
                n = 0;
                break;
            case "schedule":
                n = 1;
                break;
            case "commuting":
                n = 2;
                break;
            case "supply":
                n = 3;
                break;
            case "project":
                n = 4;
                break;
            case "document":
            	n = 5;
            	break;
            case "admin/member":
                n = 6;
                break;
            case "admin/schedule":
                n = 7;
                break;
            case "admin/commuting":
                n = 8;
                break;
            case "admin/company":
                n = 9;
                break;
        }

        if (n == -1) {
            return sb.toString();

        } else {

            arr = sb.toString().split("</li>");
            arr[n] = arr[n].replace("<li>", "<li class=\"active\">");

            StringBuffer result = new StringBuffer();
            for (int i = 0; i < arr.length; i++) {
                result.append(arr[i]).append("</li>");
            }

            return result.toString();
        }
    }

    public static Map<String, Object> getDefaultMenu(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        MemberVO loginMember = SecurityUtils.getLoginMember();

        map.put("position", codeMapper.findCodeByMajorCd(new CodeVO(Constants.CODE_POSITION)));
        map.put("department", codeMapper.findCodeByMajorCd(new CodeVO(Constants.CODE_DEPARTMENT)));

        ScheduleVO scheduleVO = new ScheduleVO();
        scheduleVO.setSearchStartDt(LocalDate.now().toString());
        scheduleVO.setSearchEndDt(LocalDate.now().toString());
        scheduleVO.setSearchTypeSC("0");
        scheduleVO.setSearchTypeVA("0");
        scheduleVO.setSearchTypeOW("0");

        map.put("schedules", scheduleMapper.todayBreak(scheduleVO));
        map.put("member", memberMapper.findMemberByMemberId(new MemberVO(loginMember.getMemberId())));

        map.put("menu", getMenuListByMember(loginMember, request));

        return map;
    }
}
