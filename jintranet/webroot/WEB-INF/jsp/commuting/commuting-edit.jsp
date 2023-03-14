<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<div class="modal" id="edit-modal">
    <div class="modal_wrap" style="z-index: 999;">
        <div class="title_bar clearfix">
            <h3>근태기록 수정</h3>
        </div>
        <form id="edit-form">
            <div class="modal_content">
                <input type="hidden" name="id">
                <input type="hidden" name="attendYn">
                <div class="mline mb10">
                    <p class="mtitle mb5">
                        날짜 <span class="required">*</span>
                    </p>
                    <input type="text" name="requestDt" class="inputbox width100" readonly>
                </div>
                <div class="mline mb10">
                    <p class="mtitle mb5">
                        수정할 기록 <span class="required">*</span>
                    </p>
                    <input type="text" name="type" class="inputbox width100" readonly>
                </div>
                <div class="mline mb10">
                    <p class="mtitle mb5">
                        시간 <span class="required">*</span>
                    </p>
                    <input type="text" name="requestTm" class="inputbox width100">
                </div>
                <div class="mline mb10">
                    <p class="mtitle mb5">
                        사유 <span class="required">*</span>
                    </p>
                    <input type="text" name="content" class="inputbox width100"
                           placeholder="근태기록 변경 사유를 입력해주세요.">
                </div>
                <div class="mline mb10">
                    <p class="mtitle mb5">
                        결재자 <span class="required">*</span>
                    </p>
                    <select class="selectbox width100" id="approveId">
                        <option value="">선택</option>
                        <c:forEach var="approves" items="${approves}" varStatus="status">
                            <option value="${approves.id}">${approves.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mbtnbox">
                    <a role="button" id="edit-btn" class="btn jjblue" onclick="editCommute();">수정</a>
                    <a role="button" id="edit-close-btn" class="btn jjblue">닫기</a>
                </div>
            </div>
        </form>
    </div>
</div>
