<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<div class="modal" id="write-modal">
    <div class="modal_wrap" style="z-index: 999;">
        <div class="title_bar clearfix">
            <h3>근태기록 등록</h3>
        </div>
        <form id="write-form">
            <div class="modal_content">
                <div class="mline mb10">
                    <p class="mtitle mb5">
                        날짜 <span class="required">*</span>
                    </p>
                    <input type="text" name="requestDt" class="inputbox width100" readonly>
                </div>
                <div class="mline mb10">
                    <p class="mtitle mb5">
                        등록할 기록 <span class="required">*</span>
                    </p>
                    <select name="type" class="selectbox width100">
                        <option value="">선택</option>
                        <option value="Y">출근시간</option>
                        <option value="N">퇴근시간</option>
                        <option value="O">잔업</option>
                    </select>
                </div>
                <div class="mline mb10">
                    <p class="mtitle mb5">
                        시간 <span class="required">*</span>
                    </p>
                     <input type="text" id="startTime1" name="startTime1" class="inputbox mhalf timepicker1 none" disabled>
                      <input type="text" id="endTime1" name="endTime1" class="inputbox mhalf timepicker1 none" autocomplete="off" onclick="timePickerCustom();" onkeypress="timePickerColenWrite(this);">
                      <input type="text" id="startTime2" name="startTime2" class="inputbox mhalf timepicker2 none" disabled>
                      <input type="text" id="endTime2" name="endTime2" class="inputbox mhalf timepicker2 none" autocomplete="off" onclick="timePickerCustom();" onkeypress="timePickerColenWrite(this);">
                      <br/>
                    <input type="text" id="requestTm" name="requestTm" class="inputbox width100" onkeypress="colenWrite(this);" maxlength="5">
                </div>
                <div class="mline mb10">
                    <p class="mtitle mb5">
                        사유 <span class="required">*</span>
                    </p>
                    <input type="text" name="content" class="inputbox width100"
                           placeholder="등록 사유를 입력해주세요.">
                </div>
                <div class="mline mb10">
                    <p class="mtitle mb5">
                        결재자 <span class="required">*</span>
                    </p>
                    <select name="approveId" class="selectbox width100">
                        <option value="">선택</option>
                        <c:forEach var="a" items="${approves}">
                            <option value="${a.id}">${a.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mbtnbox">
                    <a role="button" id="write-btn" class="btn jjblue">등록</a>
                    <a role="button" id="write-close-btn" class="btn jjblue">닫기</a>
                </div>
            </div>
        </form>
    </div>
</div>
