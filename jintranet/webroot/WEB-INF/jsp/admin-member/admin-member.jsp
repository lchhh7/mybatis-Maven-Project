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
                            <h3 class="st_title">사용자관리-관리자용</h3>
                            <span class="st_exp">사용자의 정보 및 권한을 수정할 수 있습니다.</span>
                        </div>
                        <div class="locationbar clearfix">
                            <a href="<c:url value="main.do"/>" class="home"></a>
                            <span class="local">사용자관리(관)</span>
                        </div>
                    </div>
                    <div class="topbox width100 clearfix">
                        <p class="rd2 mr15">이름</p>
                        <input type="text" id="searchName" class="inputbox width15 floatleft">
                        <select id="searchPosition" class="selectbox width15 floatleft ml10">
                            <option value="" disabled selected hidden>직급</option>
                            <option value="">선택</option>
                            <c:forEach var="p" items="${position}">
                                <option value="${p.minorCd}">${p.codeName}</option>
                            </c:forEach>
                        </select>
                        <select id="searchDepartment" class="selectbox width20 floatleft ml10">
                            <option value="" disabled selected hidden>부서</option>
                            <option value="">선택</option>
                            <c:forEach var="d" items="${department}">
                                <option value="${d.minorCd}">${d.codeName}</option>
                            </c:forEach>
                        </select>
                        <a role="button" id="search-btn" class="btn jjblue ml10 floatleft">검색</a>
                    </div>
                    <div class="listbox width100">
                        <div class="lbtop mb10 clearfix">
                            <p id="total-cnt" class="total">총 사용자 50명</p>
                            <a role="button" id="write-modal-btn" class="btn jjblue">사용자 등록</a>&nbsp;
                             <a class="btn jjdownload" href="javascript:void(0)" id="memberList"><span
                                                    class="dwicon">사용자 목록</span></a>
                        </div>
                        <div class="defaulttb main_table width100">
                            <table class="width100">
                                <colgroup>
                                    <col width="10%"/>
                                    <col width="15%"/>
                                    <col width="8%"/>
                                    <col width="15%"/>
                                    <col width="17%"/>
                                    <col width="11%"/>
                                    <col width="16%"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>이름</th>
                                    <th>아이디</th>
                                    <th>직급</th>
                                    <th>부서</th>
                                    <th>전화번호</th>
                                    <th>내선번호</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody id="members">
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

    <!-- 사용자 추가 모달 -->
    <div class="modal" id="write-modal">
        <div class="modal_wrap">
            <div class="title_bar clearfix">
                <h3>사용자 등록</h3>
            </div>
            <form id="write-form">
                <div class="modal_content">
                    <div class="mline mb10">
                        <p class="mtitle mb5">이름 <span class="required">*</span></p>
                        <input type="text" name="name" class="inputbox width100">
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">아이디 <span class="required">*</span></p>
                        <input type="text" name="memberId" class="inputbox width100">
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">비밀번호 <span class="required">*</span></p>
                        <input type="password" name="password" class="inputbox width100">
                    </div>
                    <div class="mline mb10 mhalf_m floatleft mr10">
                        <p class="mtitle mb5">직급 <span class="required">*</span></p>
                        <select name="position" class="selectbox width100">
                            <option value="">선택</option>
                            <c:forEach var="p" items="${position}">
                                <option value="${p.minorCd}">${p.codeName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mline mb10 mhalf_m floatright">
                        <p class="mtitle mb5">부서</p>
                        <select name="department" class="selectbox width100">
                            <option value="">선택</option>
                            <c:forEach var="d" items="${department}">
                                <option value="${d.minorCd}">${d.codeName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">전화번호 <span class="required">*</span></p>
                        <input type="text" name="mobileNo" class="inputbox width100">
                    </div>
                    <div class="mline mb10 mhalf_m floatleft mr10">
                        <p class="mtitle mb5">내선번호</p>
                        <input type="text" name="phoneNo" class="inputbox width100">
                    </div>
                    <div class="mline mb10 mhalf_m floatright">
                        <p class="mtitle mb5">HEX <span class="required">*</span></p>
                        <input type="text" name="color" class="inputbox width100" data-jscolor="" readonly>
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">관리자 권한 부여</p>
                        <div class="mchkbox width100">
                            <label class="chklabel m_label">
                                <input type="checkbox" name="notice" value="1"><span
                                    class="checkwd">공지사항</span>
                            </label>
                            <label class="chklabel m_label">
                                <input type="checkbox" name="member" value="2"><span
                                    class="checkwd">사용자관리</span>
                            </label>
                            <label class="chklabel m_label">
                                <input type="checkbox" name="schedule" value="3"><span
                                    class="checkwd">일정&근태관리</span>
                            </label>
                            <label class="chklabel m_label">
                                <input type="checkbox" name="supply" value="4"><span
                                    class="checkwd">비품관리</span>
                            </label>
                            <label class="chklabel m_label">
                                <input type="checkbox" name="company" value="5"><span
                                    class="checkwd">업체관리</span>
                            </label>
                        </div>
                    </div>
                    <div class="mbtnbox">
                        <a role="button" id="write-btn" class="btn jjblue">저장</a>
                        <a role="button" id="write-close-btn" class="btn jjblue">닫기</a>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="modal" id="edit-modal">
        <div class="modal_wrap">
            <div class="title_bar clearfix">
                <h3>사용자정보 수정</h3>
            </div>
            <form id="edit-form">
                <input type="hidden" name="id">
                <div class="modal_content">
                    <div class="mline mb10">
                        <p class="mtitle mb5">이름 <span class="required">*</span></p>
                        <input type="text" name="name" class="inputbox width100">
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">아이디 <span class="required">*</span></p>
                        <input type="text" name="memberId" class="inputbox width100" readonly>
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">비밀번호 <span class="required">*</span></p>
                        <input type="password" name="password" class="inputbox width100">
                    </div>
                    <div class="mline mb10 mhalf_m floatleft mr10">
                        <p class="mtitle mb5">직급 <span class="required">*</span></p>
                        <select name="position" class="selectbox width100">
                            <option value="">선택</option>
                            <c:forEach var="p" items="${position}">
                                <option value="${p.minorCd}">${p.codeName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mline mb10 mhalf_m floatright">
                        <p class="mtitle mb5">부서</p>
                        <select name="department" class="selectbox width100">
                            <option value="">선택</option>
                            <c:forEach var="d" items="${department}">
                                <option value="${d.minorCd}">${d.codeName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">전화번호 <span class="required">*</span></p>
                        <input type="text" name="mobileNo" class="inputbox width100">
                    </div>
                    <div class="mline mb10 mhalf_m floatleft mr10">
                        <p class="mtitle mb5">내선번호 </p>
                        <input type="text" name="phoneNo" class="inputbox width100">
                    </div>
                    <div class="mline mb10 mhalf_m floatright">
                        <p class="mtitle mb5">HEX <span class="required">*</span></p>
                        <input type="text" name="color" class="inputbox width100" data-jscolor="" readonly>
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">관리자 권한 부여</p>
                        <div class="mchkbox width100">
                            <label class="chklabel m_label">
                                <input type="checkbox" name="notice" value="1"><span
                                    class="checkwd">공지사항</span>
                            </label>
                            <label class="chklabel m_label">
                                <input type="checkbox" name="member" value="2"><span
                                    class="checkwd">사용자관리</span>
                            </label>
                            <label class="chklabel m_label">
                                <input type="checkbox" name="schedule" value="3"><span
                                    class="checkwd">일정&근태관리</span>
                            </label>
                            <label class="chklabel m_label">
                                <input type="checkbox" name="supply" value="4"><span
                                    class="checkwd">비품관리</span>
                            </label>
                            <label class="chklabel m_label">
                                <input type="checkbox" name="company" value="5"><span
                                    class="checkwd">업체관리</span>
                            </label>
                        </div>
                    </div>
                    <div class="mbtnbox">
                        <a role="button" id="edit-btn" class="btn jjblue">저장</a>
                        <a role="button" id="edit-close-btn" class="btn jjblue">닫기</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="<c:url value="/common/js/jscolor.js"/>"></script>
<script src="<c:url value="/common/js/admin/member/admin-member.js"/>"></script>
<script src="<c:url value="/common/js/admin/member/admin-memberExcel.js"/>"></script>
</body>

</html>