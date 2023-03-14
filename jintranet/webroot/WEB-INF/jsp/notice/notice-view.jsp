<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="../include/htmlHeader.jsp" %>

<body>
    <div class="wrap clearfix">
        <div class="bodypart width100 clearfix">
            <%@ include file="../include/lnb.jsp" %>

            <div class="container floatleft">
                <%@ include file="../include/header.jsp" %>
                <div class="content width100 clearfix">
                    <input type="hidden" id="id" value="${notice.id}">
                    <div class="mainpart floatleft">
                        <!-- 페이지 컨텐츠 -->
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
                        <form id="download-form" action="<c:url value="/notice/download.do"/>" method="post">
                            <input type="hidden" name="id">
                        </form>
                        <div class="mcpart clearfix">
                            <div class="icbox width100">
                                <h3 class="ictitle">${notice.title}</h3>
                            </div>
                            <div class="icbox width100 clearfix">
                                <span>작성자 <span class="icname ml5">${notice.memberName}</span></span>
                                <span class="floatright">작성일 <span class="icname ml5">${notice.crtDt}</span></span>
                            </div>
                            <div class="icbox width100">
                                <div class="iccontent">
                                    <p class="mb25">${notice.content}</p>
                                </div>
                            </div>
                            <div class="icbox width100 clearfix">
                                <span class="icfile floatleft mr25">첨부파일</span>
                                <div class="filelist floatleft">
                                    <c:forEach var="a" items="${notice.attaches}">
                                        <a role="button" class="file mb10" onclick="downloadAttach('${a.id}')">${a.originalFileName}</a>
                                    </c:forEach>
                                </div>
                            </div>

                            <%--
                            TODO: 이전글, 다음글글
                           <div class="floatleft">
                                <a class="btn jjblue" href="#"><img src="<c:url value="/common/img/prev.png"/>" alt=""> 이전글</a>
                                <a class="btn jjblue" href="#">다음글 <img src="<c:url value="/common/img/next.png"/>" alt=""></a>
                            </div>
                            --%>
                            <div class="floatright">
                                <a role="button" id="edit-btn" class="btn jjgray">수정</a>
                                <a role="button" id="delete-btn" class="btn jjred">삭제</a>
                                <a href="<c:url value="/notice.do"/>"class="btn jjblue">목록</a>
                            </div>
                        </div>
                    </div>
                    <%@ include file="../include/rnb.jsp" %>
                </div>
            </div>
        </div>
        <%@ include file="../include/footer.jsp" %>
    </div>

    <script src="<c:url value="/common/js/notice/notice-view.js"/>"></script>
</body>

</html>