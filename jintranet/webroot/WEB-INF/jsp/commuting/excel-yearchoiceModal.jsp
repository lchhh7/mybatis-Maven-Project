<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<div class="modal" id="excel-yearchoiceModal">
    <div class="modal_wrap" style="z-index: 999;">
        <div class="title_bar clearfix">
            <h3>휴가 엑셀 다운로드</h3>
        </div>
        <form id="choice-form">
            <div class="modal_content">
                <div class="mline mb10">
                    <p class="mtitle mb5">
                        년도 <span class="required">*</span>
                    </p>
                    <select class="selectbox width100" id="selectYear">
                        <option value="">선택</option>
                        <c:forEach var="year" items="${yearList}" varStatus="status">
                            <option value="${year}">${year}년</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mbtnbox">
                    <a role="button" id="yearchoice-btn" class="btn jjblue">확인</a>
                    <a role="button" id="yearchoice-close-btn" class="btn jjblue">닫기</a>
                </div>
            </div>
        </form>
    </div>
</div>
