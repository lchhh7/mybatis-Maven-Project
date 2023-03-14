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
                            <h3 class="st_title">문서 관리</h3>
                            <span class="st_exp">진행 중인 문서를 조회할 수 있습니다.</span>
                        </div>
                        <div class="locationbar clearfix">
                            <a href="<c:url value="/main.do"/>" class="home"></a>
                            <span class="local here">문서 관리</span>
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
                                    <th>문서명</th>
                                    <td><input type="text" id="searchDocumentName" class="inputbox width100"></td>
                                    <th>프로젝트명</th>
                                    <td><input type="text" id="searchProjectName" class="inputbox width100"></td>
                                </tr>
                                <tr>
                                    <td colspan="4">
                                        <a role="button" id="search-btn" class="btn jjblue floatright">검색</a>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div class="listbox width100">
                        <div class="lbtop mb10 clearfix">
                            <p id="total-cnt" class="total"></p>
                            <a href="<c:url value="/document/writePage.do"/>" class="btn jjblue">신규문서발급</a>
                        </div>
                        <div class="defaulttb main_table width100">
                            <table class="width100">
                                <colgroup>
                                    <col width="6%"/>
                                    <col width="23%"/>
                                    <col width="15%"/>
                                    <col width="37%"/>
                                    <col width="7%"/>
                                    <col width="11%"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>번호</th>
                                    <th>문서명</th>
                                    <th>문서번호</th>
                                    <th>관련프로젝트</th>
                                    <th>생성자</th>
                                    <th>생성일</th>
                                </tr>
                                </thead>
                                <tbody id="documents">
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
<script src="<c:url value="/common/js/document/document-common.js" /> "></script>
</body>

</html>