<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="../include/htmlHeader.jsp" %>

<body>
<div class="wrap clearfix">
    <div class="bodypart width100 clearfix">
        <%@ include file="../include/lnb.jsp" %>

        <div class="container floatleft">
            <%@ include file="../include/header.jsp" %>

            <div class="content width100 clearfix">
                <div class="mainpart floatleft">
                    <!-- 페이지 컨텐츠 -->
                    <div class="subtitle clearfix">
                        <div class="st clearfix">
                            <h3 class="st_title">투입인력</h3>
                            <span class="st_exp">진행 중이거나 완료된 프로젝트를 조회할 수 있습니다.</span>
                        </div>
                        <div class="locationbar clearfix">
                            <a href="<c:url value="/main.do"/>" class="home"></a>
                            <span class="local">프로젝트관리</span>
                            <span class="local">투입인력</span>
                        </div>
                    </div>
                    <div class="mcpart width100">
                        <ul class="tapmenu clearfix">
                            <li><a href="<c:url value="/project/edit.do?id=${project.id}"/>">기본정보</a></li>
                            <li class="active"><a href="<c:url value="/project/member.do?id=${project.id}"/>">투입인력</a></li>
                            <li><a href="<c:url value="/project/schedule.do?id=${project.id}"/>">일정</a></li>
                            <li><a href="<c:url value="/project/tailoring.do?id=${project.id}"/>">테일러링</a></li>
                            <li><a href="<c:url value="/project/document.do?id=${project.id}"/>">문서</a></li>
                        </ul>
                        <div class="defaulttb sub_table pjtop">
                            <table class="width100">
                                <colgroup>
                                    <col width="15%">
                                    <col width="35%">
                                    <col width="15%">
                                    <col width="35%">
                                </colgroup>
                                <tr>
                                    <th>프로젝트명</th>
                                    <td colspan="3">${project.title}</td>
                                </tr>
                                <tr>
                                    <th>기간</th>
                                    <td>${project.startDt} ~ ${project.endDt}</td>
                                    <th>진행부서</th>
                                    <td>${project.departmentName}</td>
                                </tr>
                                <tr>
                                    <th>착수일</th>
                                    <td>${project.launchDt}</td>
                                    <th>발주사</th>
                                    <td>${project.orderingName}</td>
                                </tr>
                            </table>
                        </div>
                        <div class="lbtop mb10 clearfix">
                            <p class="total">총 ${fn:length(pmList)}명</p>
                            <a role="button" id="add-btn" class="btn jjblue">추가</a>
                        </div>
                        <div class="defaulttb main_table width100">
                            <table class="width100">
                                <colgroup>
                                    <col width="6%" />
                                    <col />
                                    <col width="6%" />
                                    <col width="12%" />
                                    <col />
                                    <col width="10%" />
                                    <col width="13%" />
                                    <col width="8%" />
                                    <col width="15%"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>번호</th>
                                    <th>회사명</th>
                                    <th>이름</th>
                                    <th>소속부서</th>
                                    <th>역할</th>
                                    <th>구분</th>
                                    <th>투입기간</th>
                                    <th>투입공수<br/>(M/M)</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${fn:length(pmList) > 0}">
                                        <c:forEach var="pm" items="${pmList}" varStatus="status">
                                            <tr class="tbbody">
                                                <td>${status.count}</td>
                                                <td>${pm.companyName}</td>
                                                <td>${pm.memberName}</td>
                                                <td>${pm.departmentName}</td>
                                                <td>${pm.role}</td>
                                                <td>${pm.actionName}</td>
                                                <td>${pm.manStartDt}~<br/>${pm.manEndDt}</td>
                                                <td>${pm.manMonth}</td>
                                                <td>
                                                    <a role="button" class="btn jjgray" onclick="openEditModal('${pm.id}')">수정</a>
                                                    <a role="button" class="btn jjred" onclick="deleteMember('${pm.id}', '${pm.memberName}')">삭제</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr class="tbbody">
                                            <td colspan="9">등록된 투입인력이 없습니다.</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                        <div class="btnpart mt10">
                            <a class="btn jjblue" href="<c:url value="/project.do"/>">목록</a>
                        </div>
                    </div>
                </div>
                <%@ include file="../include/rnb.jsp" %>
            </div>
        </div>
    </div>
    <%@ include file="../include/footer.jsp" %>

    <div class="modal" id="write-modal">
        <div class="modal_wrap">
            <div class="title_bar clearfix">
                <h3>투입인력 등록</h3>
            </div>
            <form id="write-form">
                <div class="modal_content">
                    <div class="mline mb10 mhalf_m floatleft mr10">
                        <p class="mtitle mb5">회사명<span class="required">*</span></p>
                        <select name="companyId" class="selectbox width100">
                            <option value="">선택</option>
                            <c:forEach var="c" items="${company}">
                                <option value="${c.id}">${c.companyName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mline mb10 mhalf_m floatright">
                        <p class="mtitle mb5">이름</p>
                        <input type="text" name="memberName" class="inputbox width100">
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">역할<span class="required">*</span></p>
                        <input type="text" name="role" class="inputbox width100">
                    </div>
                    <div class="mline mb10 mhalf_m floatleft mr10">
                        <p class="mtitle mb5">구분<span class="required">*</span></p>
                        <select name="action" class="selectbox width100">
                            <option value="">선택</option>
                            <c:forEach var="a" items="${action}">
                                <option value="${a.minorCd}">${a.codeName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mline mb10 mhalf_m floatright">
                        <p class="mtitle mb5">소속부서</p>
                        <input type="text" name="departmentName" class="inputbox width100">
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">투입공수(M/M)</p>
                        <input type="text" name="manMonth" class="inputbox width100">
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">투입기간<span class="required">*</span></p>
                        <input type="text" name="manStartDt" class="inputbox mhalf datepicker" readonly>
                        <input type="text" name="manEndDt" class="inputbox mhalf datepicker" readonly>
                    </div>
                    <label class="chklabel_tb">
                        <input type="checkbox" name="allDay" class="check"><span class="checkwd s_gray">프로젝트 전체기간 적용</span>
                    </label>
                    <div class="mbtnbox">
                        <a role="button" id="write-btn" class="btn jjblue">저장</a>
                        <a role="button" id="write-close-btn" class="btn jjblue">닫기</a>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="modal" id="edit-modal">
        <div class="modal_wrap">
            <div class="title_bar clearfix">
                <h3>투입인력 수정</h3>
            </div>
            <form id="edit-form">
                <div class="modal_content">
                    <input type="hidden" name="id">
                    <div class="mline mb10 mhalf_m floatleft mr10">
                        <p class="mtitle mb5">회사명<span class="required">*</span></p>
                        <select name="companyId" class="selectbox width100">
                            <option value="">선택</option>
                            <c:forEach var="c" items="${company}">
                                <option value="${c.id}">${c.companyName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mline mb10 mhalf_m floatright">
                        <p class="mtitle mb5">이름</p>
                        <input type="text" name="memberName" class="inputbox width100">
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">역할<span class="required">*</span></p>
                        <input type="text" name="role" class="inputbox width100" placeholder="PM">
                    </div>
                    <div class="mline mb10 mhalf_m floatleft mr10">
                        <p class="mtitle mb5">구분<span class="required">*</span></p>
                        <select name="action" class="selectbox width100">
                            <option value="">선택</option>
                            <c:forEach var="a" items="${action}">
                                <option value="${a.minorCd}">${a.codeName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mline mb10 mhalf_m floatright">
                        <p class="mtitle mb5">소속부서</p>
                        <input type="text" name="departmentName" class="inputbox width100">
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">투입공수(M/M)</p>
                        <input type="text" name="manMonth" class="inputbox width100">
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">투입기간<span class="required">*</span></p>
                        <input type="text" name="manStartDt" class="inputbox mhalf datepicker" readonly>
                        <input type="text" name="manEndDt" class="inputbox mhalf datepicker" readonly>
                    </div>
                    <label class="chklabel_tb">
                        <input type="checkbox" name="allDay" class="check"><span class="checkwd s_gray">프로젝트 전체기간 적용</span>
                    </label>
                    <div class="mbtnbox">
                        <a role="button" id="edit-btn" class="btn jjblue">저장</a>
                        <a role="button" id="edit-close-btn" class="btn jjblue">닫기</a>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <script>
        const projectId = '${project.id}';
        const startDt = '${project.startDt}';
        const endDt = '${project.endDt}';
        const writeForm = document.getElementById('write-form');
        const editForm = document.getElementById('edit-form');
    </script>
    <script src="<c:url value="/common/js/project/project-member.js"/>"></script>
    <%@ include file="../include/datepicker.jsp" %>
</body>

</html>