<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<div class="aside floatleft">
    <div class="profile width100">
        <div class="pf_wrap">
            <div class="pf_part">
                <div class="clearfix">
                    <div class="pficon"></div>
                    <div class="pfname clearfix">
                        <p class="position">${member.positionName}</p>
                        <p class="name">${member.name}</p>
                        <p class="mailadd">${member.memberId}@jinjin.co.kr</p>
                    </div>
                </div>
                <div class="bottom_pf clearfix">
                    <p class="dept">${member.departmentName}</p>
                    <p class="num">내선번호 : ${member.phoneNo}</p>
                </div>
            </div>
            <a class="btn jjblue" href="<c:url value='/member/edit.do'/>">정보수정</a>
        </div>
    </div>
    <div class="today_sch">
        <div class="title_bar clearfix">
            <h3>오늘의 일정</h3>
            <a class="more floatright" href="<c:url value="/schedule.do"/>">더보기</a>
        </div>
        <ul class="sch_list">
            <c:forEach var="s" items="${schedules}">
                <c:if test='${s.type eq "SC" or s.type eq "OW"}'>
                    <li class='sch'>
                        <a role="button" onclick="">
                            <p class="sname">${s.title}</p>
                            <p class="stime">${s.startTm} - ${s.endTm}</p>
                        </a>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </div>
    <div class="today_rest">
        <div class="title_bar clearfix">
            <h3>오늘의 휴가</h3>
            <a class="more floatright" href="<c:url value="/schedule.do"/>">더보기</a>
        </div>
        <ul class="sch_list">
            <c:forEach var="s" items="${schedules}">
                <c:if test='${(s.type eq "FV" or s.type eq "HV") and s.status eq "Y"}'>
                    <c:choose>
                        <c:when test='${s.type eq "FV"}'>
                            <c:set var="className" value="r_full"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="className" value="r_half"/>
                        </c:otherwise>
                    </c:choose>
                    <li class='rest ${className}'>
                        <a role="button" onclick="">
                            <p class="rtime">${s.startTm} - ${s.endTm}</p>
                            <p class="rname">${s.title}</p>
                        </a>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </div>
</div>