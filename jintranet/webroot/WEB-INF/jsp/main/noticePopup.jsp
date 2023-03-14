<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<input type="hidden" name="idx" class="popup-transparent">
<div id="post-popup-modal" class="popup">
	<div class="popup_wrap">
		<div class="title_bar clearfix">
			<h3>공지사항</h3>
		</div>
		<form id="post-popup-form" role="dialog" target="noticePopup">
			<div class="mline mb10">
				<p class="mtitle mb5">작성자 : 
				<input type="text" id="memberName" name="memberName" class="popup-transparent">
				등록일 : <input type="text" name="crtDt" class="popup-transparent"></p>
			</div>
			<div class="mline mb10">
				<p class="mtitle mb5">제목 : 
					<input type="text" name="title" class="popup-transparent" readonly>
				</p>
			</div>
			<div class="mline mb10">
				<p class="mtitle mb5">내용 : 
					<input type="text" name="content" class="popup-transparent" readonly></p>
				</p>
			</div>
		</form>
		<div id="agreePopup" class="mline mb10">
			<div class="agree-popup-inner">
				<input type="checkbox" name="popupCookie" id="popupCookie" readonly>
				다시보지 않기
				<a role="button" id="notice-close-btn" class="btn jjblue">닫기</a>
			</div>
		</div>
	</div>
</div>

<script src="<c:url value="/common/js/jscolor.js"/>"></script>
<!--  
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
-->