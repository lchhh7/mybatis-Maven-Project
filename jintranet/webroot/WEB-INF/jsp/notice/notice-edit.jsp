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
                        <div class="defaulttb sub_table width100">
                            <form id="edit-form">
                                <input type="hidden" name="id" value="${notice.id}">
                                <table class="width100">
                                    <colgroup>
                                        <col width="15%">
                                        <col width="85%">
                                    </colgroup>
                                    <tbody>
                                        <tr>
                                            <th>제목 <span class="required">*</span></th>
                                            <td><input type="text" name="title" class="inputbox width100" value="${notice.title}"></td>
                                        </tr>
                                        <tr>
                                            <th>내용 <span class="required">*</span></th>
                                            <td>
                                                <textarea name="ir1" id="ir1" class="editor" rows="10" cols="100">${notice.content}</textarea>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>첨부파일<br><span class="max">(최대 2개)</span></th>
                                            <td>
                                                <div id="files">
                                                    <c:forEach var="a" items="${notice.attaches}">
                                                        <div class="filelist mt5 mb10 clearfix">
                                                            <a role="button" class="file floatleft" onclick="downloadAttach('${a.id}')">${a.originalFileName}</a>
                                                            <img class="ml15 eximg pointer" src="<c:url value="/common/img/delete.png"/>" alt="삭제" onclick="deleteAttach(this, ${a.id})">
                                                        </div>
                                                    </c:forEach>
                                                </div>
                                                <a role="button" id="upload-modal-btn" class="btn jjcyan mb5">파일 선택</a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2">
                                                <div class="floatright mt10 mb10">
                                                    <a role="button" id="edit-btn" class="btn jjblue">저장</a>
                                                    <a role="button" id="cancel-btn" class="btn jjblue">취소</a>
                                                </div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                    </div>
                    <%@ include file="../include/rnb.jsp" %>
                </div>
            </div>
        </div>
        <%@ include file="../include/footer.jsp" %>
    </div>

    <%@ include file="../include/fileupload.jsp" %>
    <script src="<c:url value="/common/smarteditor/js/HuskyEZCreator.js"/>"></script>
    <script src="<c:url value="/common/js/notice/notice-common.js"/>"></script>
    <script src="<c:url value="/common/js/notice/notice-edit.js"/>"></script>
</body>

</html>