<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div id="vacation-modal" class="modal">
	<div class="modal_wrap mw_big">
		<div class="title_bar clearfix">
			<h3>신청승인상태</h3>
		</div>

		<div class="modal_content">
			<div class="mline mb10">
				<div>
					<div class="floatleft mhalf mr5">
						<div class="floatleft mhalf mr5">
							<select class="selectbox width100" id="search-type">
								<option value="">전체</option>
								<c:forEach var="typeList" items="${typeList}">
									<option value="${typeList.type}">${typeList.typeName}</option>
								</c:forEach>
							</select>
						</div>
						<div class="floatleft mhalf_m">
							<select class="selectbox width100" id="search-year">
								<option value="">전체</option>
								<c:forEach var="yearList" items="${yearList}">
									<option value="${yearList}">${yearList}</option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div>
						<div class="floatleft mhalf_m">
							<a role="button" class="btn jjblue" onclick="searching()">검색</a>
						</div>
					</div>
				</div>
				<br>
				<div class="defaulttb md_table">
					<table class="fixedhead">
						<colgroup>
							<col width="17%">
							<col width="33%">
							<col width="23%">
							<col width="12%">
							<col width="13%">
						</colgroup>
						<thead>
							<tr>
								<th>종류</th>
								<th>일정</th>
								<th>신청일</th>
								<th>결재자</th>
								<th>승인상태</th>
							</tr>
						</thead>
					</table>
				</div>
				<div class="defaulttb md_table width100 scrollbody">
					<table class="width100">
						<colgroup>
							<col width="17%">
							<col width="33%">
							<col width="23%">
							<col width="12%">
							<col width="13%">
						</colgroup>
						<tbody id="vacation-tbody">
						</tbody>
					</table>
				</div>
				<div class="mbtnbox">
					<a role="button" id="vacation-close-btn"
						class="btn jjblue closebtn">닫기</a>
				</div>
			</div>
		</div>
	</div>
</div>