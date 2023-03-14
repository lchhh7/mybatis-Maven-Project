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
                    <div class="subtitle clearfix">
                        <div class="st clearfix">
                            <h3 class="st_title">근태수정관리 - 관리자용</h3>
                            <span class="st_exp">수정신청이 들어온 근태기록을 확인합니다.</span>
                        </div>
                        <div class="locationbar clearfix">
                            <a href="../main.html" class="home"></a>
                            <span class="local">근태수정관리(관)</span>
                        </div>
                    </div>
                    <div class="topbox width100">
                        <select class="selectbox width120px" id="search-member-id">
                            <option value="" disabled selected hidden>신청자명</option>
                            <option value="">전체</option>
                            <c:forEach var="m" items="${members}">
                                <option value="${m.id}">${m.name}</option>
                            </c:forEach>
                        </select>
                        <div class="searchtb width45">
                            <div class="defaulttb sub_table ml10">
                                <table class="width100">
                                    <colgroup>
                                        <col width="27%"/>
                                        <col width="73%"/>
                                    </colgroup>
                                    <tbody>
                                    <tr>
                                        <th>승인여부</th>
                                        <td>
                                            <label class="chklabel s_chk">
                                                <input type="checkbox" id="search-status1" value='R' checked><span
                                                    class="checkwd">대기</span>
                                            </label>
                                            <label class="chklabel s_chk">
                                                <input type="checkbox" id="search-status2" value='Y'><span
                                                    class="checkwd">승인</span>
                                            </label>
                                            <label class="chklabel s_chk last_chk">
                                                <input type="checkbox" id="search-status3" value='N'><span
                                                    class="checkwd">비승인</span>
                                            </label>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <a role="button" id="excelWriterByAdmin" class="btn jjdownload ml10">
                        <span class="dwicon">전체초과근무</span></a>
                    </div>
                    <div class="listbox width100">
                        <div class="lbtop mb10 clearfix">
                            <p id="totalCommuting" class="total"></p>
                        </div>
                        <div class="defaulttb main_table width100">
                            <table class="width100">
                                <colgroup>
                                    <col width="12%"/>
                                    <col width="12%"/>
                                    <col width="15%"/>
                                    <col width="12%"/>
                                    <col width="15%"/>
                                    <col width="10%"/>
                                </colgroup>
                                <thead>
                                <tr class="not">
                                    <th>이름</th>
                                    <th>구분</th>
                                    <th>시간</th>
                                    <th>내용</th>
                                    <th>등록일자</th>
                                    <th>승인여부</th>
                                </tr>
                                </thead>
                                <tbody id="commutings">
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
    <%@ include file="admin-commuting-modify.jsp" %>
    <%@ include file="admin-excel-yearchoiceModal.jsp" %>
</div>
<script src='<c:url value="/common/js/admin/admin-commuting/admin-commuting.js" />'></script>
<script src='<c:url value="/common/js/admin/admin-commuting/admin-excel.js" />'></script>
</body>

</html>