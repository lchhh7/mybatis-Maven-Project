<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<div class="modal" id="detail-info-modal">
	<div class="modal_wrap">
		<div class="title_bar clearfix">
			<h3>휴가신청 상세정보</h3>
		</div>
		<form id="view-form">
			<input type="hidden" name="id">
			<div class="modal_content">
				<div class="mline mb10 clearfix">
					<div class="mhalf_m floatleft mr10">
						<p class="mtitle mb5">요청자</p>
						<input type="text" class="inputbox width100" name="memberName" disabled>
					</div>
					<div class="mhalf_m floatright">
						<p class="mtitle mb5">휴가종류</p>
						<input type="text" disabled class="inputbox width100" name="typeName">
					</div>
				</div>
				<div class="underline"></div>
				<div class="mline mb10">
					<p class="mtitle mb5">시작일</p>
					<input type="text" name="startDt" disabled class="inputbox mhalf"> <input
						type="text"name="startTm"  disabled class="inputbox mhalf">
				</div>
				<div class="mline mb10">
					<p class="mtitle mb5">종료일</p>
					<input type="text" name="endDt" disabled class="inputbox mhalf"> <input
						type="text" name="endTm" disabled class="inputbox mhalf">
				</div>
				<div class="mline mb10">
					<p class="mtitle mb5">제목</p>
					<input type="text" name="title" disabled class="inputbox width100">
				</div>
				<div class="mline mb10">
					<p class="mtitle mb5">내용</p>
					<input type="text" name="content" disabled class="inputbox width100">
				</div>
				<div class="mline mb10 clearfix">
					<div class="mhalf_m floatleft mr10">
						<p class="mtitle mb5">승인상태</p>
						<input type="text" class="inputbox width100" name="statusName" readonly>
					</div>
					<div class="mhalf_m floatright">
						<p class="mtitle mb5">승인날짜</p>
						<input type="text" disabled class="inputbox width100" name="approveDt" readonly>
					</div>
				</div>
				<div id="approve-btn-box" class="mbtnbox">
				</div>
			</div>
		</form>
	</div>
</div>