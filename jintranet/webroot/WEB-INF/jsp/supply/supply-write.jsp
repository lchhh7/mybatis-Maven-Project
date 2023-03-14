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
								<h3 class="st_title">비품요청</h3>
								<span class="st_exp">필요한 비품을 신청할 수 있습니다.</span>
							</div>
							<div class="locationbar clearfix">
								<a href="<c:url value="/main.do"/>" class="home"></a> <span
									class="local">비품관리</span> <span class="local">비품요청</span>
							</div>
						</div>
						<div class="mcpart">
							<div class="defaulttb sub_table width100">
								<form id="write-form">
									<table class="width100" style="table-layout: fixed;">
										<colgroup>
											<col width="15%">
											<col width="35%">
											<col width="15%">
											<col width="35%">
										</colgroup>
										<tbody>
											<tr>
												<th>비품명<span class="required">*</span></th>
												<td colspan="3"><input type="text" name="name"
													class="inputbox width70"></td>
											</tr>
											<tr>
												<th>수량<span class="required">*</span></th>
												<td colspan="3"><input type="text" name="amount"
													id="amount" class="inputbox width20" maxlength="4"><span>&nbsp;개</span></td>
											</tr>
											<tr>
												<th>단품 금액<span class="required">*</span></th>
												<td colspan="3"><input type="text" name="price"
													id="price" class="inputbox width20" maxlength="13"><span>&nbsp;원</span></td>
											</tr>
											<tr>
												<th>배송비<span class="required">*</span></th>
												<td colspan="3"><input type="text" name="shippingFee"
													id="shippingFee" class="inputbox width20" value="0" maxlength="13"><span>&nbsp;원</span></td>
											</tr>
											<tr>
												<th>총 금액</th>
												<td colspan="3"><input type="text" name="total"
													id="total" class="inputbox width20 disabled"><span>&nbsp;원</span></td>
											</tr>
											<tr>
												<th>결제방법</th>
												<td colspan="3"><select name="payment"
													class="selectbox width20">
														<option value="">선택</option>
														<c:forEach var="p" items="${payment}">
															<option value="${p.minorCd}"
																<c:if test="${p.minorCd eq supply.payment}">selected</c:if>>${p.codeName}</option>
														</c:forEach>
												</select></td>
											</tr>
											<tr>
												<th>계산서 발행</th>
												<td colspan="3"><label class="chklabel_tb floatleft">
														<input type="radio" name="billYN" value="Y"
														<c:if test='${supply.billYN eq "Y"}'>checked</c:if>><span
														class="checkwd ml5">발행</span>
												</label> <label class="chklabel_tb floatleft ml15"> <input
														type="radio" name="billYN" value="N"
														<c:if test='${supply.billYN eq "N"}'>checked</c:if>><span
														class="checkwd ml5">미발행</span>
												</label></td>
											</tr>

											<tr>
												<th>판매처 URL</th>
												<td colspan="3"><input type="text" name="url"
													class="inputbox width70"> &nbsp;
													<a role="button" name="urlLink" class="btn jjblue">+</a>
													</td>
											</tr>
											<tr>
												<th>첨부파일<br>
												<span class="max">(최대 2개)</span></th>
												<td colspan="3">
													<div id="files"></div> <a role="button"
													id="upload-modal-btn" class="btn jjcyan mb5">파일 선택</a>
												</td>
											</tr>
											<tr>
												<th>승인자<span class="required">*</span></th>
												<td colspan="3"><select name="approveId"
													class="selectbox width20">
														<option value="">선택</option>
														<c:forEach var="a" items="${approves}">
															<option value="${a.id}">${a.name}</option>
														</c:forEach>
												</select></td>
											</tr>
										</tbody>
									</table>
								</form>
							</div>
							<div class="btnpart mt10">
								<a role="button" id="write-btn" class="btn jjblue">등록</a> <a
									href="<c:url value="/supply.do"/>" class="btn jjblue">목록</a>
							</div>
						</div>
					</div>
					<%@ include file="../include/rnb.jsp"%>
				</div>
			</div>
		</div>
		<%@ include file="../include/footer.jsp"%>
	</div>
	<%@ include file="../include/fileupload.jsp"%>
	<script src="<c:url value="/common/js/supply/supply-common.js"/>"></script>
	<script src="<c:url value="/common/js/supply/supply-write.js"/>"></script>
</body>

</html>