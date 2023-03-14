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
                                <h3 class="st_title">공지사항</h3>
                                <span class="st_exp">글쓰기는 공지권한이 있는 관리자만 작성 가능합니다.</span>
                            </div>
                            <div class="locationbar clearfix">
                                <a href="<c:url value="/main.do"/>" class="home"></a>
                                <span class="local">공지사항</span>
                            </div>
                        </div>
                        <div class="topbox infotb width100">
                            <select id="search-type" class="selectbox width130px">
                                <option value="searchTitle" selected>제목</option>
                                <option value="searchCrtId">작성자</option>
                            </select>
                            <input type="text" id="search-content" class="inputbox width400px ml5">
                            <a role="button" id="search-btn" class="btn jjblue ml10">검색</a>
                        </div>
                        <div class="listbox width100">
                            <div class="lbtop mb10 clearfix">
                                <p id="total-cnt" class="total">총 게시물 75건</p>
                                <a href="<c:url value="/notice/write.do"/>" class="btn jjblue" id="writeAuthority">글쓰기</a>
                            </div>
                            <div class="defaulttb main_table width100">
                                <table class="width100">
                                    <colgroup>
                                        <col width="6%" />
                                        <col width="45%" />
                                        <col width="13%" />
                                        <col width="8%" />
                                        <col width="13%" />
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th>번호</th>
                                            <th>제목</th>
                                            <th>작성자</th>
                                            <th>첨부파일</th>
                                            <th>등록일</th>
                                        </tr>
                                    </thead>
                                    <tbody id="notice">
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

    <script src="<c:url value="/common/js/notice/notice.js"/>"></script>
</body>

</html>