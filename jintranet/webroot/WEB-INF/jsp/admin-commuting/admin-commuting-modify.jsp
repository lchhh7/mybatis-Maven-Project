<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<!DOCTYPE html>
<div class="modal" id="modify-modal">
    <div class="modal_wrap">
        <div class="title_bar clearfix">
            <h3>근태수정 신청정보</h3>
        </div>
        <form id="modify-form">
            <input type="hidden" name="id">
            <input type="hidden" name="memberId">
            <div class="modal_content">
                <div class="mline mb10">
                    <p class="mtitle mb5">신청자</p>
                    <input type="text" name="memberName" class="inputbox width100" disabled>
                </div>
                <div class="mline mb10  mhalf_m floatleft mr10">
                    <p class="mtitle mb5 ">날짜</p>
                    <input type="text" name="requestDt" class="inputbox width100 datepicker" disabled>
                </div>
                <div class="mline mb10 mhalf_m floatright">
                    <p class="mtitle mb5">시간</p>
                    <input type="text" name="requestTm" class="inputbox width100" disabled>
                </div>
                <div class="mline mb10  mhalf_m floatleft mr10">
                    <input type="text" name="startTime1" class="inputbox timepicker1 none" disabled>
                     <input type="text" name="startTime2" class="inputbox timepicker2 none" disabled>
                </div>
                <div class="mline mb10 mhalf_m floatright">
                    <input type="text" name="endTime1" class="inputbox timepicker1 none" disabled>
                    <input type="text" name="endTime2" class="inputbox timepicker2 none" disabled>
                </div>
                <div class="mline mb10">
                    <p class="mtitle mb5">수정 사유</p>
                    <input type="text" name="content" class="inputbox width100" disabled>
                </div>
                <div class="mline mb10 clearfix">
                    <div class="mhalf_m floatleft mr10">
                        <p class="mtitle mb5">승인상태</p>
                        <input type="text" class="inputbox width100" name="statusName" disabled>
                    </div>
                    <div class="mhalf_m floatright">
                        <p class="mtitle mb5">승인날짜</p>
                        <input type="text" disabled class="inputbox width100" name="approveDt" disabled>
                    </div>
                </div>
                <div id="modify-btn-box" class="mbtnbox">
                </div>
            </div>
        </form>
    </div>
</div>