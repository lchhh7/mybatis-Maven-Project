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
								<h3 class="st_title">공지사항</h3>
								<span class="st_exp">글쓰기는 공지권한이 있는 관리자만 작성 가능합니다.</span>
							</div>
							<div class="locationbar clearfix">
								<a href="<c:url value="/main.do"/>" class="home"></a> <span
									class="local">공지사항</span>
							</div>
						</div>
						<div class="defaulttb sub_table width100">
							<form id="write-form" enctype="multipart/form-data">
								<table class="width100">
									<colgroup>
										<col width="15%">
										<col width="85%">
									</colgroup>
									<tbody>
										<tr>
											<th>제목 <span class="required">*</span></th>
											<td><input type="text" name="title"
												class="inputbox width100"></td>
										</tr>
									<%-- 	<tr>
											<th>사용자 게시여부</th>
											<td><label class="chklabel_tb"> <input
													type="checkbox" name="postYN" id="postYN" class="check"><span
													class="checkwd">사용자 사이트에 공지를 게시합니다.</span>
											</label>
											
													<div class="mline mb5">
														<input type="hidden" name="postStrDt" class="inputbox mhalf datepicker" readonly>
														<input type="hidden" name="postEndDt" class="inputbox mhalf datepicker" readonly>
													</div>
												</td>
										</tr>
									--%>
										<tr>
											<th>내용 <span class="required">*</span></th>
											<td><textarea name="ir1" id="ir1" class="editor"
													rows="10" cols="100"></textarea></td>
										</tr>
										<tr>
											<th>첨부파일<br>
											<span class="max">(최대 2개)</span></th>
											<td>
												<div id="files"></div> <a role="button"
												id="upload-modal-btn" class="btn jjcyan mb5">파일 선택</a>
											</td>
										</tr>
										<%--
                                        TODO: 캡차
                                        <tr>
                                            <th>자동등록<br>방지문자</th>
                                            <td>
                                                <a href="#" class="eximg"><img src="<c:url value="/common/img/eximg.png"/>"> </a>
                                                <input type="text" name="captcha" class="inputbox width170px">
                                                <p class="mt5 mb5">이미지를 클릭하면 새로운 이미지가 표시됩니다.</p>
                                            </td>
                                        </tr>
                                        --%>
										<tr>
											<td colspan="2">
												<div class="floatright mt10 mb10">
													<a role="button" id="write-btn" class="btn jjblue">등록</a> <a
														href="<c:url value="/notice.do"/>" class="btn jjblue">목록</a>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</form>
						</div>
					</div>
					<%@ include file="../include/rnb.jsp"%>
					 <%@ include file="../include/datepicker.jsp" %>
				</div>
			</div>
		</div>
		<%@ include file="../include/footer.jsp"%>
	</div>
	<%@ include file="../include/fileupload.jsp"%>
	<script src="<c:url value="/common/smarteditor/js/HuskyEZCreator.js"/>"></script>
	<script src="<c:url value="/common/js/notice/notice-common.js"/>"></script>
	<script src="<c:url value="/common/js/notice/notice-write.js"/>"></script>
</body>

</html>