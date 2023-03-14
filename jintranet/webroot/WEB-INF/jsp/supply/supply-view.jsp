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
                                <h3 class="st_title">비품요청 확인</h3>
                                <span class="st_exp">비품 신청내역을 확인할 수 있습니다.</span>
                            </div>
                            <div class="locationbar clearfix">
                                <a href="<c:url value="/main.do"/>" class="home"></a>
                                <span class="local">비품관리</span>
                                <span class="local">비품요청 확인</span>
                            </div>
                        </div>
                        <form id="download-form" action="<c:url value="/supply/download.do"/>" method="post">
                            <input type="hidden" name="id">
                        </form>
                        <div class="mcpart">
                            <form id="view-form">
                                <div class="defaulttb sub_table width100">
                                    <input type="hidden" id="id" value="${supply.id}">
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
                                                <td colspan="3">${supply.name}</td>
                                            </tr>
                                            <tr>
                                                <th>작성자</th>
                                                <td><span>${supply.memberName}</span></td>
                                                <th>작성일</th>
                                                <td>${supply.crtDt}</td>
                                            </tr>
                                            <tr>
                                                <th>수량<span class="required">*</span></th>
                                                <td colspan="3">
                                                    <fmt:formatNumber value="${supply.amount}" pattern="#,###,###"/> 개
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>단가<span class="required">*</span></th>
                                                <td colspan="3">
                                                    <fmt:formatNumber value="${supply.price}" pattern="#,###,###.00"/> 원
                                                </td>
                                            </tr>
                                            <tr>
												<th>배송비<span class="required">*</span></th>
												<td colspan="3">
													<fmt:formatNumber value="${supply.shippingFee}" pattern="#,###,###"/> 원
												</td>
											</tr>
                                            <tr>
                                                <th>합계</th>
                                                <td colspan="3">
                                                    <fmt:formatNumber value="${(supply.amount * supply.price)+supply.shippingFee}" pattern="#,###,###"/> 원
                                                </td>
                                            </tr>
                                            <c:choose>
                                                <c:when test='${supply.memberId eq member.id and supply.status eq "Y"}'>
                                                <tr>
                                                    <th>결제방법</th>
                                                    <td colspan="3">
                                                        <select name="payment" class="selectbox width20">
                                                            <option value="">선택</option>
                                                            <c:forEach var="p" items="${payment}">
                                                                <option value="${p.minorCd}" <c:if test="${p.minorCd eq supply.payment}">selected</c:if>>${p.codeName}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>계산서 발행</th>
                                                    <td colspan="3">
                                                        <label class="chklabel_tb floatleft">
                                                            <input type="radio" name="billYN" value="Y" <c:if test='${supply.billYN eq "Y"}'>checked</c:if>><span class="checkwd ml5">발행</span>
                                                        </label>
                                                        <label class="chklabel_tb floatleft ml15">
                                                            <input type="radio" name="billYN" value="N" <c:if test='${supply.billYN eq "N"}'>checked</c:if>><span class="checkwd ml5">미발행</span>
                                                        </label>
                                                    </td>
                                                </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr>
                                                        <th>결제방법</th>
                                                        <td colspan="3">
                                                        <c:forEach var="p" items="${payment}">
                                                            <c:if test="${p.minorCd eq supply.payment}">${p.codeName}</c:if>
                                                        </c:forEach>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th>계산서 발행</th>
                                                        <td colspan="3">
                                                            <c:if test='${supply.billYN eq "Y"}'>발행</c:if>
                                                            <c:if test='${supply.billYN eq "N"}'>미발행</c:if>
                                                        </td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                            <tr>
                                                <th>판매처 URL</th>
                                                <td colspan="3">
                                                    <a href="${supply.url}" class="wordbreak" target="_blank">${supply.url}</a>
                                                    <c:if test='${supply.url1 ne null && supply.url1 ne ""}'>
                                                    	<br/><a href="${supply.url1}" class="wordbreak" target="_blank">${supply.url1}</a>
                                                    </c:if>
                                                    <c:if test='${supply.url2 ne null && supply.url2 ne ""}'>
                                                    	<br/><a href="${supply.url2}" class="wordbreak" target="_blank">${supply.url2}</a>
                                                    </c:if>
                                                    <c:if test='${supply.url3 ne null && supply.url3 ne ""}'>
                                                    	<br/><a href="${supply.url3}" class="wordbreak" target="_blank">${supply.url3}</a>
                                                    </c:if>
                                                    <c:if test='${supply.url4 ne null && supply.url4 ne ""}'>
                                                    	<br/><a href="${supply.url4}" class="wordbreak" target="_blank">${supply.url4}</a>
                                                    </c:if>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>첨부파일<br><span class="max">(최대 2개)</span></th>
                                                <td colspan="3">
                                                    <div class="filelist floatleft">
                                                        <c:forEach var="a" items="${supply.attaches}">
                                                            <a role="button" class="file mb10" onclick="downloadAttach('${a.id}')">${a.originalFileName}</a>
                                                        </c:forEach>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>승인자<span class="required">*</span></th>
                                                <td colspan="3">
                                                <c:forEach var="a" items="${approves}">
                                                    <c:if test="${a.id eq supply.approveId}">${a.name}</c:if>
                                                </c:forEach>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>승인상태</th>
                                                <td><span>${supply.statusName}</span></td>
                                                <th>처리일</th>
                                                <td>${supply.approveDt}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </form>
                            <div class="btnpart mt10">
                                <c:if test="${supply.memberId eq member.id }">
                                    <c:if test='${supply.status ne "N"}'>
                                        <a role="button" id="edit-btn" class="btn jjblue">수정</a>
                                    </c:if>
                                    <c:if test='${supply.status eq "R"}'>
                                        <a role="button" id="delete-btn" class="btn jjred">삭제</a>
                                    </c:if>
                                </c:if>
                                <c:if test="${supply.approveId eq member.id}">
                                    <a role="button" id="approve-btn" class="btn floatleft mr5 jjblue">승인</a>
                                    <a role="button" id="ready-btn" class="btn floatleft mr5 jjgray">승인초기화</a>
                                    <a role="button" id="refuse-btn" class="btn floatleft mr5 jjred">비승인</a>
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
    <script>
        const viewForm = document.getElementById('view-form');
        const editBtn = document.getElementById('edit-btn');
        const status = '${supply.status}';
    </script>
    <c:if test="${supply.memberId eq member.id }">
        <script src="<c:url value="/common/js/supply/supply-view.js"/>"></script>
    </c:if>
    <c:if test="${supply.approveId eq member.id}">
        <script src="<c:url value="/common/js/supply/supply-approve.js"/>"></script>
    </c:if>
</body>

</html>