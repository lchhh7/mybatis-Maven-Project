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
                            <h3 class="st_title">업체관리</h3>
                            <span class="st_exp">업체를 관리할 수 있습니다.</span>
                        </div>
                        <div class="locationbar clearfix">
                            <a href="<c:url value="/main.do"/>" class="home"></a>
                            <span class="local">업체관리</span>
                        </div>
                    </div>
                    <div class="topbox width100 clearfix">
                        <p class="rd2 mr15">업체명</p>
                        <input type="text" id="search-name" class="inputbox width25 floatleft">
                        <div class="searchtb stb2 width40 ml10">
                            <div class="defaulttb sub_table">
                                <table class="width100">
                                    <colgroup>
                                        <col width="30%"/>
                                        <col width="70%"/>
                                    </colgroup>
                                    <tbody>
                                    <tr>
                                        <th>법인/개인</th>
                                        <td>
                                            <label class="chklabel s_chk ml15">
                                                <input type="checkbox" id="search-kind1" class="check" value="C"
                                                       checked><span
                                                    class="checkwd">법인</span>
                                            </label>
                                            <label class="chklabel s_chk ml5">
                                                <input type="checkbox" id="search-kind2" class="check" value="P"
                                                       checked><span
                                                    class="checkwd">개인</span>
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
                            <a href="<c:url value="/admin/company/write.do"/>" class="btn jjblue">신규업체</a>
                        </div>
                        <div class="defaulttb main_table width100">
                            <table class="width100">
                                <colgroup>
                                    <col width="8%"/>
                                    <col width="28%"/>
                                    <col width="24%"/>
                                    <col width="12%"/>
                                    <col width="14%"/>
                                    <col width="14%"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>번호</th>
                                    <th>사업자번호</th>
                                    <th>업체명</th>
                                    <th>회사유형</th>
                                    <th>사업자등록증</th>
                                    <th>통장사본</th>
                                </tr>
                                </thead>
                                <tbody id="company">
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
<script src="<c:url value="/common/js/company/company.js"/>"></script>
</body>

</html>