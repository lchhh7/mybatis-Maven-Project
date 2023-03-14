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
                            <h3 class="st_title">프로젝트관리</h3>
                            <span class="st_exp">진행 중인 프로젝트를 조회할 수 있습니다.</span>
                        </div>
                        <div class="locationbar clearfix">
                            <a href="<c:url value="/main.do"/>" class="home"></a>
                            <span class="local here">프로젝트관리</span>
                        </div>
                    </div>
                    <div class="topsearch width100">
                        <div class="defaulttb sub_table">
                            <table class="width100">
                                <colgroup>
                                    <col width="15%">
                                    <col width="35%">
                                    <col width="15%">
                                    <col width="35%">
                                </colgroup>
                                <tr>
                                    <th>년도</th>
                                    <td><input type="text" id="searchYear" class="inputbox width100" maxlength="4"></td>
                                    <th>기간</th>
                                    <td><input type="text" id="searchStartDt" class="inputbox datepicker pjdate"
                                               readonly><span
                                            class="mr5 ml5">~</span><input type="text" id="searchEndDt"
                                                                           class="inputbox datepicker pjdate" readonly>
                                    </td>
                                </tr>
                                <tr>
                                    <th>프로젝트명</th>
                                    <td><input type="text" id="searchTitle" class="inputbox width100"></td>
                                    <th>발주사명</th>
                                    <td><input type="text" id="searchOrderingName" class="inputbox width100"></td>
                                </tr>
                                <tr>
                                    <th>진행부서</th>
                                    <td>
                                        <select id="searchDepartment" class="selectbox width60">
                                            <option value="" selected>전체</option>
                                            <c:forEach var="d" items="${department}">
                                                <option value="${d.minorCd}">${d.codeName}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td colspan="2">
                                        <a role="button" id="search-btn" class="btn jjblue floatright">검색</a>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div class="listbox width100">
                        <div class="lbtop mb10 clearfix">
                            <p id="total-cnt" class="total"></p>
                            <a href="<c:url value="/project/write.do"/>" class="btn jjblue">신규 프로젝트</a>
                        </div>
                        <div class="defaulttb main_table width100">
                            <table class="width100">
                                <colgroup>
                                    <col width="6%"/>
                                    <col width="41%"/>
                                    <col width="13%"/>
                                    <col width="12%"/>
                                    <col width="15%"/>
                                    <col width="6%"/>
                                    <col width="6%"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>번호</th>
                                    <th>프로젝트명</th>
                                    <th>발주사</th>
                                    <th>진행부서</th>
                                    <th>기간</th>
                                    <th>컨소시엄</th>
                                    <th>하도급</th>
                                </tr>
                                </thead>
                                <tbody id="projects">
                                </tbody>
                            </table>
                        </div>
                        <div id="page" class="pagination">
                        </div>
                    </div>
                </div>
                <%@ include file="../include/rnb.jsp" %>
            </div>
        </div>
    </div>
    <%@ include file="../include/footer.jsp" %>
</div>
<%@ include file="../include/datepicker.jsp" %>
<script src="<c:url value="/common/js/project/project.js" /> "></script>
</body>

</html>