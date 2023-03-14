<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="../include/htmlHeader.jsp" %>

<body>
    <div class="wrap clearfix">
        <div class="bodypart width100 clearfix">
            <%@ include file="../include/lnb.jsp" %>

            <div class="container floatleft">
                <%@ include file="../include/header.jsp" %>

                <div class="content width100 clearfix">
                    <div class="mainpart floatleft">
                        <!-- 페이지 컨텐츠 -->
                        <div class="subtitle clearfix">
                            <div class="st clearfix">
                                <h3 class="st_title">비품요청 수정</h3>
                                <span class="st_exp">비품 신청내역을 수정할 수 있습니다.</span>
                            </div>
                            <div class="locationbar clearfix">
                                <a href="<c:url value="/main.do"/>" class="home"></a>
                                <span class="local">비품관리</span>
                                <span class="local">비품요청 수정</span>
                            </div>
                        </div>
                        <form id="download-form" action="<c:url value="/supply/download.do"/>" method="post">
                            <input type="hidden" name="id">
                        </form>
                        <div class="mcpart">
                            <div class="defaulttb sub_table width100">
                                <form id="edit-form">
                                    <input type="hidden" name="id" value="${supply.id}">
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
                                                <td colspan="3"><input type="text" name="name" class="inputbox width70" value="${supply.name}"></td>
                                            </tr>
                                            <tr>
                                                <th>작성자</th>
                                                <td><span>${supply.memberName}</span></td>
                                                <th>작성일</th>
                                                <td>${supply.crtDt}</td>
                                            </tr>
                                            <tr>
                                                <th>수량<span class="required">*</span></th>
                                                <td colspan="3"><input type="text" name="amount" id="amount"
                                                        class="inputbox width20" value="${supply.amount}" maxlength="4"><span>&nbsp;개</span></td>
                                            </tr>
                                            <tr>
                                                <th>단가<span class="required">*</span></th>
                                                <td colspan="3"><input type="text" name="price" id="price"
                                                        class="inputbox width20" value="${supply.price}" maxlength="13"><span>&nbsp;원</span></td>
                                            </tr>
                                            <tr>
												<th>배송비<span class="required">*</span></th>
												<td colspan="3"><input type="text" name="shippingFee"
													id="shippingFee" class="inputbox width20" value="${supply.shippingFee}" maxlength="13"><span>&nbsp;원</span></td>
											</tr>
                                            <tr>
                                                <th>합계</th>
                                                <td colspan="3"><input type="text" name="total" id="total" class="inputbox width20 disabled"
                                                                       value="${supply.amount * supply.price}" readonly disabled><span>&nbsp;원</span></td>
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
                                                <td colspan="3">
                                                    <input type="text" name="url" class="inputbox width70" value="${supply.url}">
                                                    <a role="button" name="urlLink" class="btn jjblue">+</a>
                                                    <c:if test='${supply.url1 ne null && supply.url1 ne ""}'>
                                                    	<input type="text" name="url1" class="inputbox width70" value="${supply.url1}">
                                                    </c:if>
                                                    <c:if test='${supply.url2 ne null && supply.url2 ne ""}'>
                                                    	<input type="text" name="url2" class="inputbox width70" value="${supply.url2}">
                                                    </c:if>
                                                    <c:if test='${supply.url3 ne null && supply.url3 ne ""}'>
                                                    	<input type="text" name="url3" class="inputbox width70" value="${supply.url3}">
                                                    </c:if>
                                                    <c:if test='${supply.url4 ne null && supply.url4 ne ""}'>
                                                    	<input type="text" name="url4" class="inputbox width70" value="${supply.url4}">
                                                    </c:if>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>첨부파일<br><span class="max">(최대 2개)</span></th>
                                                <td colspan="3">
                                                    <div id="files">
                                                        <c:forEach var="a" items="${supply.attaches}">
                                                            <div class="filelist mt5 mb10 clearfix">
                                                                <a role="button" class="file floatleft" onclick="downloadAttach('${a.id}')">${a.originalFileName}</a>
                                                                <img class="ml15 eximg pointer" src="<c:url value="/common/img/delete.png"/>" alt="삭제" onclick="deleteAttach(this, ${a.id})">
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                    <a role="button" id="upload-modal-btn" class="btn jjcyan mb5">파일 선택</a>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>승인자<span class="required">*</span></th>
                                                <td colspan="3">
                                                    <select name="approveId" class="selectbox width20">
                                                        <option value="">선택</option>
                                                        <c:forEach var="a" items="${approves}">
                                                            <option value="${a.id}" <c:if test="${a.id eq supply.approveId}">selected</c:if>>${a.name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </form>
                            </div>
                            <div class="btnpart mt10">
                                <c:if test="${member.id == supply.memberId}">
                                    <a role="button" id="edit-btn" class="btn jjblue">저장</a>
                                    <a role="button" id="delete-btn" class="btn jjred">삭제</a>
                                </c:if>
                                <a href="<c:url value="/supply.do"/> " class="btn jjblue">목록</a>
                            </div>
                        </div>
                    </div>
                    <%@ include file="../include/rnb.jsp" %>
                </div>
            </div>
        </div>
        <%@ include file="../include/footer.jsp" %>
    </div>
    <%@ include file="../include/fileupload.jsp" %>
    <script src="<c:url value="/common/js/supply/supply-common.js"/>"></script>
    <c:if test="${supply.memberId eq member.id }">
    <script src="<c:url value="/common/js/supply/supply-edit.js"/>"></script>
    </c:if>
</body>

</html>