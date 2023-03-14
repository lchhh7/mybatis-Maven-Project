<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<div class="modal" id="request-modal" style="z-index: 1050;">
    <div class="modal_wrap">
        <div class="title_bar clearfix">
            <h3>일정 상세정보 확인</h3>
        </div>
        <form id="request-form">
            <div class="modal_content">
                <input type="hidden" name="id">
                <input type="hidden" name="type">
                <div class="mb10 clearfix">
                    <div class="floatleft mhalf_m mr10">
                        <p class="mtitle mb5">작성자</p>
                        <input type="text" class="inputbox width100" name="memberName" readonly>
                    </div>
                    <div class="floatleft mhalf_m">
                        <p class="mtitle mb5">일정종류</p>
                        <input type="text" name="typeName" class="inputbox width100" readonly>
                    </div>
                </div>
                <div class="underline"></div>
                <div class="mline mb10">
                    <p class="mtitle mb5">신청일 <span class="required">*</span></p>
                    <input type="text" name="requestDt" class="inputbox mhalf datepicker" disabled>
                    <input type="text" name="requestTm" class="inputbox mhalf" readonly onkeypress="colenWrite(this)" maxlength="5">
                </div>
                 <div class="mline mb10">
                     <input type="text" name="startTime1" class="inputbox mhalf timepicker1 none" disabled>
                      <input type="text" name="endTime1" class="inputbox mhalf timepicker1 none" autocomplete="off" onclick="timePickerCustom();" onkeypress="timePickerColenWrite(this);">
                      <input type="text" name="startTime2" class="inputbox mhalf timepicker2 none" disabled>
                      <input type="text" name="endTime2" class="inputbox mhalf timepicker2 none" autocomplete="off" onclick="timePickerCustom();" onkeypress="timePickerColenWrite(this);">
                </div>
                <div class="mline mb10">
                    <p class="mtitle mb5">내용 <span class="required">*</span></p>
                    <input type="text" name="content" class="inputbox width100" readonly>
                </div>
                <div id="approve-div" class="mb10 clearfix none">
                    <div class="floatleft mhalf_m mr10 ">
                        <p class="mtitle mb5">승인자 <span class="required">*</span></p>
                        <input type="text" name="approveName" class="inputbox width100" readonly>
                    </div>
                    <div class="floatleft mhalf_m ">
                        <p class="mtitle mb5">승인상태</p>
                        <input type="text" name="statusName" class="inputbox width100" readonly>
                    </div>
                </div>
                <div id="reason" class="mline mb10 none">
                    <p class="mtitle mb5">신청취소사유</p>
                    <input type="text" name="cancelReason" class="inputbox width100" placeholder="신청 취소 시 취소 사유를 입력해주세요.">
                </div>
                <div id="request-btn-box" class="mbtnbox">
                </div>
            </div>
        </form>
    </div>
</div>
