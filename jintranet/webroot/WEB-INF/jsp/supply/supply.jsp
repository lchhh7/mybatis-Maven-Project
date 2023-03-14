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
                            <h3 class="st_title">비품관리</h3>
                            <span class="st_exp">필요한 비품을 신청할 수 있습니다.</span>
                        </div>
                        <div class="locationbar clearfix">
                            <a href="<c:url value="/main.do"/>" class="home"></a>
                            <span class="local">비품관리</span>
                        </div>
                    </div>
                    <div class="topbox width100 clearfix">
                        <select id="search-select" class="selectbox width15 mr10">
                            <option value="1">비품명</option>
                            <option value="2">작성자</option>
                        </select>
                        <input type="text" id="search-name" class="inputbox width25">
                        <select id="search-member-id" class="selectbox width15 none">
                            <option value="">선택</option>
                            <c:forEach var="m" items="${members}">
                                <option value="${m.id}">${m.name}</option>
                            </c:forEach>
                        </select>
                        <div class="searchtb width40 ml10">
                            <div class="defaulttb sub_table">
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
                                                <input type="checkbox" id="search-status1" class="check" value="R" checked><span
                                                    class="checkwd">대기</span>
                                            </label>
                                            <label class="chklabel s_chk">
                                                <input type="checkbox" id="search-status2" class="check" value="Y" checked><span
                                                    class="checkwd">승인</span>
                                            </label>
                                            <label class="chklabel s_chk last_chk">
                                                <input type="checkbox" id="search-status3" class="check" value="N" checked><span
                                                    class="checkwd">비승인</span>
                                            </label>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <a role="button" id="search-btn" class="btn jjblue ml10">검색</a>
                    </div>
                    <div class="listbox width100">
                        <div class="lbtop mb10 clearfix">
                            <p id="total-cnt" class="total"></p>
                            <a href="<c:url value="/supply/write.do"/>" class="btn jjblue">비품신청</a>
                        </div>
                        <div class="defaulttb main_table width100">
                            <table class="width100">
                                <colgroup>
                                    <col width="8%"/>
                                    <col width="*"/>
                                    <col width="10%"/>
                                    <col width="20%"/>
                                    <col width="12%"/>
                                    <col width="12%"/>
                                    <col width="12%"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>번호</th>
                                    <th>비품명</th>
                                    <th>수량</th>
                                    <th>총 금액</th>
                                    <th>작성자</th>
                                    <th>승인자</th>
                                    <th>승인여부</th>
                                </tr>
                                </thead>
                                <tbody id="supply">
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
<script src="<c:url value="/common/js/supply/supply.js"/>"></script>
</body>

</html>