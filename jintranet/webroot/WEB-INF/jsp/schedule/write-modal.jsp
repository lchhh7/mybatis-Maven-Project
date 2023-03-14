<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div class="modal" id="write-modal" style="z-index: 999;">
	<div class="modal_wrap">
		<div class="title_bar clearfix">
			<h3>일정 상세정보 입력</h3>
		</div>
		<form id="write-form">
			<div class="modal_content">
				<div class="mline mb10">
					<p class="mtitle mb5">
						일정종류 <span class="required">*</span>
					</p>
					<select name="type" class="selectbox width100">
						<option value="" disabled selected hidden>선택</option>
						<option value="SC">일정</option>
						<option value="VA">휴가</option>
						<option value="OW">외근</option>
						<option value="BT">출장</option>
					</select>
				</div>
				<div class="underline"></div>
				<div id="vacation" class="mb10 clearfix none">
					<div class="floatleft mhalf mr5">
						<p class="mtitle mb5">
							휴가종류 <span class="required">*</span>
						</p>
						<select name="vacationType" class="selectbox width100">
							<option value="" disabled selected hidden>선택</option>
							<option value="1">연차</option>
							<option value="2">오전 반차</option>
							<option value="3">오후 반차</option>
						</select>
					</div>
					<div class="floatleft mhalf_m">
						<p class="mtitle mb5">
							결재자 <span class="required">*</span>
						</p>
						<select name="approveId" class="selectbox width100">
							<option value="" disabled selected hidden>선택</option>
							<c:forEach var="a" items="${approves}">
								<option value="${a.id}">${a.name}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div id="overtime" class="mb10 clearfix none">
					<p class="mtitle mb5">
						결재자 <span class="required">*</span>
					</p>
					<select name="overtimeApproveId" class="selectbox width100">
						<option value="" disabled selected hidden>선택</option>
						<c:forEach var="a" items="${approves}">
							<option value="${a.id}">${a.name}</option>
						</c:forEach>
					</select>
				</div>
				<div class="mline mb10">
					<p class="mtitle mb5">
						시작일 <span class="required">*</span>
					</p>
					<input type="text" name="startDt" class="inputbox mhalf datepicker"
						readonly> <input type="text" name="startTm"
						class="inputbox mhalf" onkeypress="colenWrite(this)" maxlength="5">
				</div>
				<div class="mline mb10">
					<p class="mtitle mb5">
						종료일 <span class="required">*</span>
					</p>
					<input type="text" name="endDt" class="inputbox mhalf datepicker"
						readonly> <input type="text" name="endTm"
						class="inputbox mhalf" onkeypress="colenWrite(this)" maxlength="5">
				</div>
				<div class="mline mb10">
					<p class="mtitle mb5">
						제목 <span class="required">*</span>
					</p>
					<input type="text" name="title" class="inputbox width100"
						placeholder="달력에 보일 내용을 간단히 입력해주세요.">
				</div>
				<div class="mline mb10">
					<p class="mtitle mb5">
						내용 <span class="required">*</span>
					</p>
					<input type="text" name="content" class="inputbox width100"
						placeholder="일정의 상세 내용을 입력해주세요.">
				</div>

				<div id="add-passengers" class="mb10 clearfix none">
					<a role="button" id="add-passenger" class="plus">동석자 선택</a>
				</div>

				<div class="mline mb10">
					<p class="mtitle mb5">
						HEX <span class="required">*</span>
					</p>
					<input type="text" name="color" class="inputbox width100"
						data-jscolor="" readonly>
				</div>
				<div class="mbtnbox">
					<a role="button" id="write-btn" class="btn jjblue">저장</a> <a
						role="button" id="write-close-btn" class="btn jjblue">닫기</a>
				</div>
			</div>
		</form>
	</div>
</div>
<%@ include file="../schedule/passengers.jsp"%>