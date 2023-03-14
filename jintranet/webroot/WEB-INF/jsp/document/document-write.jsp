<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="../include/htmlHeader.jsp"%>

<body>
	<div class="wrap clearfix">
		<div class="bodypart width100 clearfix">
			<%@ include file="../include/lnb.jsp"%>

			<div class="container floatleft">
				<%@ include file="../include/header.jsp"%>

				<div class="content width100 clearfix">
					<div class="mainpart floatleft">
						<!-- 페이지 컨텐츠 -->
						<div class="subtitle clearfix">
							<div class="st clearfix">
								<h3 class="st_title">문서번호발급</h3>
								<span class="st_exp">신규 문서번호를 발급할 수 있습니다.</span>
							</div>
							<div class="locationbar clearfix">
								<a href="<c:url value="/main.do"/>" class="home"></a> <span
									class="local">문서발급관리</span> <span class="local">신규문서발급</span>
							</div>
						</div>
						<div class="mcpart">
							<div class="defaulttb sub_table width100">
								<form name="documentWriteForm"
									action="<c:url value="/document/write.do"/>" method="post">
									<table class="width100">
										<colgroup>
											<col width="20%">
											<col width="80%">
										</colgroup>
										<tbody>
											<tr>
												<th>문서번호 발급일</th>
												<td><input type="text" name="documentDt"
													class="inputbox mhalf_m mr10 datepicker" value ="${today}" readonly></td>
											</tr>
											<tr>
												<th>문서 번호</th>
												<td><input type="text" name="documentNo"
													class="inputbox mhalf_m" disabled ></td>
											</tr>
											<tr>
												<th>문서 제목<span class="required">*</span></th>
												<td><input type="text" name="title"
													class="inputbox mhalf_m"></td>
											</tr>
											<tr>
												<th>관련 프로젝트</th>
												<td>
													<div class="floatleft mhalf mr5">
														<select class="selectbox width100" id="search-project" name="searchProject">
															<option value="">없음</option>
															<c:forEach var="projectList" items="${projectList}">
																<option value="${projectList.id}">${projectList.title}</option>
															</c:forEach>
														</select>
													</div>
												</td>
											</tr>
										</tbody>
									</table>
								</form>
								<span class="bttext"><span class="required">*</span> 표기가
									되어있는 항목은 필수 입력 항목입니다.</span>
							</div>
							<div class="btnpart mt10">
								<a role="button" id="save-btn" class="btn jjblue">저장</a> <a
									href="<c:url value="/document.do"/> " class="btn jjblue">목록</a>
							</div>
						</div>
					</div>
					<%@ include file="../include/rnb.jsp"%>
				</div>
			</div>
		</div>
		<%@ include file="../include/footer.jsp"%>
	</div>
	<script src="<c:url value="/common/js/document/document-write.js"/> "></script>
	<!-- <script src="<c:url value="/common/js/document/document-common.js"/> "></script> -->
	<link rel="stylesheet"
		href='<c:url value="/common/jquery/css/datepicker.css"/>'>
	<script src='<c:url value="/common/jquery/js/datepicker.js"/>'></script>
	<script src='<c:url value="/common/jquery/js/moment.js"/>'></script>
	<script src='<c:url value="/common/js/datepicker.js"/>'></script>
</body>

</html>