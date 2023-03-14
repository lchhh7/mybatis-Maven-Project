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
                            <h3 class="st_title">정보수정</h3>
                            <span class="st_exp">정보를 수정할 수 있습니다.</span>
                        </div>
                        <div class="locationbar clearfix">
                            <a href="<c:url value="/main.do"/>" class="home"></a>
                            <span class="local">정보수정</span>
                        </div>
                    </div>
                    <div class="mcpart">
                        <div class="defaulttb sub_table width100">
                            <form name="edit">
                                <table class="width100">
                                    <colgroup>
                                        <col width="20%">
                                        <col width="80%">
                                    </colgroup>
                                    <tbody>
                                    <tr>
                                        <th>이름<span class="required">*</span></th>
                                        <td><input type="text" name="name" class="inputbox width40" value="${member.name}"></td>
                                    </tr>
                                    <tr>
                                        <th>아이디</th>
                                        <td><input type="text" name="memberId" class="inputbox width40" value="${member.memberId}" readonly></td>
                                    </tr>
                                    <tr>
                                        <th>비밀번호</th>
                                        <td>
                                            <a role="button" id="password-btn" class="btn jjblue">비밀번호 변경</a>
                                            <span class="s_blue ml15"></span>
                                        </td>
                                    <tr>
                                        <th>직급<span class="required">*</span></th>
                                        <td>
                                            <select name="position" class="selectbox width40">
                                                <option value="">선택</option>
                                                <c:forEach var="p" items="${position}">
                                                    <option value="${p.minorCd}">${p.codeName}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>부서</th>
                                        <td>
                                            <select name="department" class="selectbox width40">
                                                <option value="">선택</option>
                                                <c:forEach var="d" items="${department}">
                                                    <option value="${d.minorCd}">${d.codeName}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>내선번호</th>
                                        <td><input type="text" name="phoneNo" class="inputbox width40" value="${member.phoneNo}" maxlength="3"></td>
                                    </tr>
                                    <tr>
                                        <th>전화번호<span class="required">*</span></th>
                                        <td><input type="text" name="mobileNo" class="inputbox width40" value="${member.mobileNo}" maxlength="13"></td>
                                    </tr>
                                    <tr>
                                        <th>HEX<span class="required">*</span></th>
                                        <td><input type="text" name="color" class="inputbox width40" value="${member.color}" readonly></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                        <div class="btnpart mt10">
                            <a role="button" id="edit-btn" class="btn jjblue">저장</a>
                        </div>
                    </div>
                </div>
                <%@ include file="../include/rnb.jsp" %>
            </div>
        </div>
    </div>

    <%@ include file="../include/footer.jsp" %>

    <div class="modal" id="password-modal">
        <div class="modal_wrap">
            <div class="title_bar clearfix">
                <h3>비밀번호 변경</h3>
            </div>
            <form name="password">
                <div class="modal_content">
                    <div class="mline mb10">
                        <p class="mtitle mb5">현재 비밀번호</p>
                        <input type="password" name="password" class="inputbox width100" autocomplete="current-password">
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">새로운 비밀번호</p>
                        <input type="password" name="newPassword" class="inputbox width100" autocomplete="new-password">
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">비밀번호 확인</p>
                        <input type="password" name="newPassword2" class="inputbox width100" autocomplete="new-password">
                    </div>
                    <div class="mbtnbox">
                        <a role="button" id="password-edit-btn" class="btn jjblue">비밀번호변경</a>
                        <a role="button" id="password-close-btn" class="btn jjblue">닫기</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="<c:url value="/common/js/jscolor.js"/>"></script>
<script src='<c:url value="/common/js/member.js" />'></script>
<script>
    const editForm = document.edit;
    const passwordForm = document.password;

    document.addEventListener('DOMContentLoaded', function () {
        editForm.position.value = '${member.position}';
        editForm.department.value = '${member.department}';
    });
</script>
</body>

</html>