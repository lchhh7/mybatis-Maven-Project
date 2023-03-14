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
                                <h3 class="st_title">업체정보 등록</h3>
                                <span class="st_exp">업체정보를 등록할 수 있습니다.</span>
                            </div>
                            <div class="locationbar clearfix">
                                <a href="<c:url value="/main.do"/>" class="home"></a>
                                <span class="local">업체관리</span>
                                <span class="local">업체정보 등록</span>
                            </div>
                        </div>
                        <div class="mcpart">
                            <div class="defaulttb sub_table width100">
                                <form id="write-form">
                                    <table class="width100" style="table-layout: fixed;">
                                        <colgroup>
                                            <col width="15%">
                                            <col width="35%">
                                            <col width="15%">
                                            <col width="35%">
                                        </colgroup>
                                        <tbody>
                                            <tr>
                                                <th>업체명<span class="required">*</span></th>
                                                <td colspan="3"><input type="text" name="companyName" class="inputbox width25"></td>
                                            </tr>
                                            <tr>
                                                <th>사업자번호<span class="required">*</span></th>
                                                <td colspan="3"><input type="text" name="companyNo" class="inputbox width25"></td>
                                            </tr>
                                            <tr>
                                                <th>회사유형<span class="required">*</span></th>
                                                <td colspan="3">
                                                    <label class="chklabel_tb floatleft">
                                                        <input type="radio" name="companyKind" value="C"><span class="checkwd ml5">법인</span>
                                                    </label>
                                                    <label class="chklabel_tb floatleft ml15">
                                                        <input type="radio" name="companyKind" value="P"><span class="checkwd ml5">개인</span>
                                                    </label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>사업자등록증</th>
                                                <td colspan="3">
                                                    <div id="files2"></div>
                                                    <a role="button" id="upload-modal-btn2" class="btn jjcyan mb5">파일 선택</a>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>통장사본</th>
                                                <td colspan="3">
                                                    <div id="files"></div>
                                                    <a role="button" id="upload-modal-btn" class="btn jjcyan mb5">파일 선택</a>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </form>
                            </div>
                            <div class="btnpart mt10">
                                <a role="button" id="write-btn" class="btn jjblue">등록</a>
                                <a href="<c:url value="/admin/company.do"/>" class="btn jjblue">목록</a>
                            </div>
                        </div>
                    </div>
                    <%@ include file="../include/rnb.jsp" %>
                </div>
            </div>
        </div>
        <%@ include file="../include/footer.jsp" %>
    </div>
    <%@ include file="../include/fileupload.jsp" %>
    <%@ include file="company-fileupload.jsp" %>
    <script src="<c:url value="/common/js/company/company-common.js"/>"></script>
    <script src="<c:url value="/common/js/company/company-write.js"/>"></script>
</script>
</body>

</html>