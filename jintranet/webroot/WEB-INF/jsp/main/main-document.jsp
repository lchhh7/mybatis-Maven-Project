<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div id="document-modal" class="modal">
	<div class="modal_wrap">
		<div class="title_bar clearfix">
			<h3>문서번호발급</h3>
		</div>
		<form id="document-form">
			<div class="modal_content">
				<div class="mline mb10">
					<p class="mtitle mb5">문서번호 발급일</p>
					<input type="text" name="documentDt"
						class="inputbox mhalf_m mr10 datepicker" readonly> <input
						type="text" name="documentNo" class="inputbox mhalf_m" disabled>
				</div>
				<div class="mline mb10">
					<p class="mtitle mb5">문서명</p>
					<input type="text" name="title" class="inputbox width100">
				</div>
				<div class="mline mb10"> 
					<p class="mtitle mb5">관련 프로젝트</p>
						<select class="selectbox width100" name="searchProject" id="search-project">
						</select>
						<!-- 
						<select class="selectbox width100" name="searchProject">
							<option value="">없음</option> 
							<c:forEach var="projectList" items="${projectList}">
								<option value="${projectList.id}">${projectList.title}</option>
							</c:forEach>
						</select>
						-->
				</div>
				<div class="mline mb10">
					<p class="mtitle mb5">발급된 문서</p>
					<div class="defaulttb md_table width100">
						<table class="width100">
							<colgroup>
								<col width="10%">
								<col width="50%">
								<col width="20%">
								<col width="15%">
							</colgroup>
							<thead>
								<tr>
									<th>번호</th>
									<th>문서명</th>
									<th>작성자</th>
									<th></th>
								</tr>
							</thead>
							<tbody id="document-tbody">
							</tbody>
						</table>
					</div>
					<div class="mbtnbox">
						<a role="button" id="document-write-btn" class="btn jjblue">문서번호발급</a>
						<a role="button" id="document-close-btn" class="btn jjblue">닫기</a>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>