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
                            <h3 class="st_title">프로젝트수정</h3>
                            <span class="st_exp">프로젝트 정보를 수정할 수 있습니다.</span>
                        </div>
                        <div class="locationbar clearfix">
                            <a href="<c:url value="/main.do"/>" class="home"></a>
                            <span class="local">프로젝트관리</span>
                            <span class="local">기본정보</span>
                        </div>
                    </div>

                    <div class="mcpart">
                        <ul class="tapmenu clearfix">
                            <li class="active"><a href="<c:url value="/project/edit.do?id=${project.id}"/>">기본정보</a></li>
                            <li><a href="<c:url value="/project/member.do?id=${project.id}"/>">투입인력</a></li>
                            <li><a href="<c:url value="/project/schedule.do?id=${project.id}"/>">일정</a></li>
                            <li><a href="<c:url value="/project/tailoring.do?id=${project.id}"/>">테일러링</a></li>
                            <li><a href="<c:url value="/project/document.do?id=${project.id}"/>">문서</a></li>
                        </ul>
                        <div class="defaulttb sub_table width100">
                            <form id="edit-form" name="project">
                                <table class="width100">
                                    <colgroup>
                                        <col width="20%">
                                        <col width="80%">
                                    </colgroup>
                                    <tbody>
                                    <tr>
                                        <th>프로젝트코드</th>
                                        <td><input type="text" name="id" readonly class="inputbox width170px"
                                                   value="${project.id}"></td>
                                    </tr>
                                    <tr>
                                        <th>프로젝트명<span class="required">*</span></th>
                                        <td><input type="text" name="title" class="inputbox width70"
                                                   value="${project.title}"></td>
                                    </tr>
                                    <tr>
                                        <th>발주사명<span class="required">*</span></th>
                                        <td><input type="text" name="orderingName" class="inputbox width70"
                                                   value="${project.orderingName}"></td>
                                    </tr>
                                    <tr>
                                        <th>계약번호<span class="required">*</span></th>
                                        <td>
                                            <label class="chklabel_tb mb10">
                                                <input type="checkbox" id="ppsYn" name="ppsYN" class="check"
                                                       value="Y"><span class="checkwd">조달청
                                                        입찰</span>
                                            </label>
                                            <input type="text" name="contractNo" class="inputbox width40"
                                                   value="${project.contractNo}">
                                            <span class="ml10">※ 조달청입찰인 경우 조달청의 계약번호를 입력</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>사업분야<span class="required">*</span></th>
                                        <td>
                                            <select id="businessField" name="businessField"
                                                    class="selectbox width200px">
                                                <option value="" selected>선택</option>
                                                <option value="SI">시스템통합(SI)</option>
                                                <option value="SM">시스템 관리 및 유지보수(SM)</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>기간<span class="required">*</span></th>
                                        <td><input type="text" name="startDt"
                                                   class="inputbox width170px datepicker"
                                                   value="${project.startDt}" readonly><span
                                                class="mr10 ml10">~</span><input type="text"
                                                                                 name="endDt"
                                                                                 class="inputbox width170px datepicker"
                                                                                 value="${project.endDt}" readonly></td>
                                    </tr>
                                    <tr>
                                        <th>착수일<span class="required">*</span></th>
                                        <td><input type="text"
                                                   name="launchDt"
                                                   class="inputbox width170px datepicker"
                                                   value="${project.launchDt}" readonly></td>
                                    </tr>
                                    <tr>
                                        <th>계약금액 (부가세포함)<span class="required">*</span></th>
                                        <td>
                                            <input type="text" name="amountSurtaxInclude"
                                                   class="inputbox width170px"
                                                   value="<fmt:formatNumber value="${project.amountSurtaxInclude}" type="number"/>"><span
                                                class="ml10">원</span></td>
                                    </tr>
                                    <tr>
                                        <th>계약금액 (부가세제외)<span class="required">*</span></th>
                                        <td><input type="text" name="amountSurtaxExclude"
                                                   class="inputbox width170px"
                                                   value="<fmt:formatNumber value="${project.amountSurtaxExclude}" type="number"/>"><span
                                                class="ml10">원</span></td>
                                    </tr>
                                    <tr>
                                        <th>계약보증금<span class="required">*</span></th>
                                        <td>
                                            <input type="text" name="contractDepositRate"
                                                   class="inputbox width170px"
                                                   value="${project.contractDepositRate}"><span
                                                class="ml10">%</span>
                                            <span class="ml25 mr5">금액 : </span><input type="text"
                                                                                      name="contractDeposit"
                                                                                      class="inputbox width170px"
                                                                                      value="<fmt:formatNumber value="${project.contractDeposit}" type="number"/>"><span
                                                class="ml10">원</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>하자보증금<span class="required">*</span></th>
                                        <td>
                                            <input type="text" name="defectDepositRate"
                                                   class="inputbox width170px"
                                                   value="${project.defectDepositRate}"><span
                                                class="ml10">%</span>
                                            <span class="ml25 mr5">금액 : </span><input type="text"
                                                                                      name="defectDeposit"
                                                                                      class="inputbox width170px"
                                                                                      value="<fmt:formatNumber value="${project.defectDeposit}" type="number"/>"><span
                                                class="ml10">원</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>컨소시엄</th>
                                        <td>
                                            <label class="chklabel_tb mb10">
                                                <input type="checkbox"
                                                       id="consortium-yn"
                                                       name="consortiumYN"
                                                       class="check"
                                                       value="Y"><span class="checkwd">컨소시엄으로 구성</span>
                                            </label>
                                            <div id="consortiums">
                                                <c:forEach var="c" items="${consortiums}" varStatus="status">
                                                    <input type="hidden"
                                                           name="consortiums[${status.count - 1}]"
                                                           value="${c.companyId}">
                                                    <input type="text"
                                                           class="inputbox width170px"
                                                           value="${c.companyName}"
                                                           readonly
                                                           disabled>
                                                </c:forEach>
                                                <a role="button" id="add-consortium" class="plus">업체선택</a>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>하도급</th>
                                        <td>
                                            <label class="chklabel_tb mb10">
                                                <input type="checkbox"
                                                       id="subcontract-yn"
                                                       name="subcontractYN"
                                                       class="check"
                                                       value="Y"><span class="checkwd">하도급 있음</span>
                                            </label>
                                            <div id="subcontracts">
                                                <c:forEach var="s" items="${subcontracts}" varStatus="status">
                                                    <input type="hidden"
                                                           name="subcontracts[${status.count - 1}]"
                                                           value="${s.companyId}">
                                                    <input type="text"
                                                           class="inputbox width170px"
                                                           value="${s.companyName}"
                                                           readonly
                                                           disabled>
                                                </c:forEach>
                                                <a role="button" id="add-subcontract" class="plus">업체선택</a>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>진행부서<span class="required">*</span></th>
                                        <td>
                                            <select id="department" name="department" class="selectbox width170px">
                                                <option value="">전체</option>
                                                <c:forEach var="d" items="${department}">
                                                    <option value="${d.minorCd}">${d.codeName}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>PM명<span class="required">*</span></th>
                                        <td><input type="text" name="projectManagerName" class="inputbox width60"
                                                   value="${project.projectManagerName}"></td>
                                    </tr>
                                    <tr>
                                        <th>사업실적 등록여부<span class="required">*</span></th>
                                        <td>
                                            <label class="chklabel_tb floatleft">
                                                <input type="radio" name="performanceRegistYN" value="Y"><span class="checkwd ml5">등록</span>
                                            </label>
                                            <label class="chklabel_tb floatleft ml15">
                                                <input type="radio" name="performanceRegistYN" value="N"><span class="checkwd ml5">미등록</span>
                                            </label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>이행완료일</th>
                                        <td><input type="text" name="implementDt" class="inputbox width170px datepicker"
                                                   value="${project.implementDt}" readonly></td>
                                    </tr>
                                    </tbody>
                                </table>
                                <span class="bttext"><span class="required">*</span> 표기가 되어있는 항목은 필수 입력 항목입니다.</span>
                        </div>
                        <div class="btnpart mt10">
                            <a role="button" id="save-btn" class="btn jjblue">저장</a>
                            <a role="button" id="del-btn" class="btn jjred">삭제</a>
                            <a href="<c:url value="/project.do"/> " class="btn jjblue">목록</a>
                        </div>
                    </div>
                </div>
                <%@ include file="../include/rnb.jsp" %>
            </div>
        </div>
    </div>
    <%@ include file="../include/footer.jsp" %>

    <!-- 컨소시엄 popup -->
    <div class="modal" id="consortium-modal">
        <div class="modal_wrap mw_big">
            <div class="title_bar">
                <h3>컨소시엄 업체</h3>
            </div>
            <div class="modal_content">
                <div class="defaulttb md_table">
                    <table class="fixedhead">
                        <colgroup>
                            <col width="15%">
                            <col width="25%">
                            <col width="40%">
                            <col width="20%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>참여여부</th>
                            <th>사업자번호</th>
                            <th>업체명</th>
                            <th>법인/개인구분</th>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="defaulttb md_table width100 scrollbody">
                    <table class="width100">
                        <colgroup>
                            <col width="15%">
                            <col width="25%">
                            <col width="40%">
                            <col width="20%">
                        </colgroup>
                        <tbody id="consortium-company">
                        <c:forEach var="c" items="${company}">
                            <tr>
                                <td class="companyId none">${c.id}</td>
                                <td><input type="checkbox" class="check"></td>
                                <td>${c.companyNo}</td>
                                <td class="companyName">${c.companyName}</td>
                                <td>${c.companyKind}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="mbtnbox">
                    <a role="button" id="consortium-save" class="btn jjblue closebtn">확인</a>
                    <a role="button" id="consortium-close" class="btn jjblue closebtn">닫기</a>
                </div>
            </div>
        </div>
    </div>


    <!-- 하도급 popup -->
    <div class="modal" id="subcontract-modal">
        <div class="modal_wrap mw_big">
            <div class="title_bar">
                <h3>하도급 업체</h3>
            </div>
            <div class="modal_content">
                <div class="defaulttb md_table">
                    <table class="fixedhead">
                        <colgroup>
                            <col width="15%">
                            <col width="25%">
                            <col width="40%">
                            <col width="20%">
                        </colgroup>
                        <thead>
                        <tr>
                            <th>참여여부</th>
                            <th>사업자번호</th>
                            <th>업체명</th>
                            <th>법인/개인구분</th>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="defaulttb md_table width100 scrollbody">
                    <table class="width100">
                        <colgroup>
                            <col width="15%">
                            <col width="25%">
                            <col width="40%">
                            <col width="20%">
                        </colgroup>
                        <tbody id="subcontract-company">
                        <c:forEach var="c" items="${company}">
                            <tr>
                                <td class="companyId none">${c.id}</td>
                                <td><input type="checkbox" class="check"></td>
                                <td>${c.companyNo}</td>
                                <td class="companyName">${c.companyName}</td>
                                <td>${c.companyKind}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="mbtnbox">
                    <a role="button" id="subcontract-save" class="btn jjblue closebtn">확인</a>
                    <a role="button" id="subcontract-close" class="btn jjblue closebtn">닫기</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const ppsYn = '${project.ppsYN}';
        const businessField = '${project.businessField}';
        const department = '${project.department}';
        const performanceRegistYn = '${project.performanceRegistYN}';

        document.getElementById("ppsYn").checked = (ppsYn === "Y");
        document.getElementById("businessField").value = businessField;
        document.getElementById("department").value = department;

        const registYn = document.getElementsByName("performanceRegistYN");
        registYn.forEach(function (el) {
            if (el.value === performanceRegistYn)
                el.checked = true;
        });

        const consortiumYn = document.getElementById("consortium-yn");
        consortiumYn.checked = ("${project.consortiumYN}" === "Y");
        const subcontractYn = document.getElementById("subcontract-yn");
        subcontractYn.checked = ("${project.subcontractYN}" === "Y");

        changeConYn();
        changeSubYn();
    });
</script>
<script src="<c:url value="/common/js/project/project-common.js"/> "></script>
<script src="<c:url value="/common/js/project/project-edit.js"/> "></script>
<%@ include file="../include/datepicker.jsp" %>



</body>

</html>