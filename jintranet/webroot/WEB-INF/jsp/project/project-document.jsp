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
                                <h3 class="st_title">문서</h3>
                                <span class="st_exp">진행 중이거나 완료된 프로젝트를 조회할 수 있습니다.</span>
                            </div>
                            <div class="locationbar clearfix">
                                <a href="<c:url value="/main.do"/>" class="home"></a>
                                <span class="local">프로젝트관리</span>
                                <span class="local">문서</span>
                            </div>
                        </div>
                        <form id="download-form" action="<c:url value="/project/document/download.do"/>" method="post">
                            <input type="hidden" name="id">
                        </form>
                        <form id="download-history-form" action="<c:url value="/project/document/downloadHistory.do"/>" method="post">
                            <input type="hidden" name="id">
                        </form>
                        <div class="mcpart width100">
                            <ul class="tapmenu clearfix">
                                <li><a href="<c:url value="/project/edit.do?id=${project.id}"/>">기본정보</a></li>
                                <li><a href="<c:url value="/project/member.do?id=${project.id}"/>">투입인력</a></li>
                                <li><a href="<c:url value="/project/schedule.do?id=${project.id}"/>">일정</a></li>
                                <li><a href="<c:url value="/project/tailoring.do?id=${project.id}"/>">테일러링</a></li>
                                <li class="active"><a href="<c:url value="/project/document.do?id=${project.id}"/>">문서</a></li>
                            </ul>
                            <div class="defaulttb sub_table pjtop">
                                <table class="width100">
                                    <colgroup>
                                        <col width="15%">
                                        <col width="35%">
                                        <col width="15%">
                                        <col width="35%">
                                    </colgroup>
                                    <tr>
                                        <th>프로젝트명</th>
                                        <td colspan="3">${project.title}</td>
                                    </tr>
                                    <tr>
                                        <th>기간</th>
                                        <td>${project.startDt} ~ ${project.endDt}</td>
                                        <th>진행부서</th>
                                        <td>${project.departmentName}</td>
                                    </tr>
                                    <tr>
                                        <th>착수일</th>
                                        <td>${project.launchDt}</td>
                                        <th>발주사</th>
                                        <td>${project.orderingName}</td>
                                    </tr>
                                </table>
                            </div>
                            <div class="lbtop mb10 clearfix">
                                <p class="total">총 ${fn:length(documents)}개</p>
                                <a role="button" id="add-btn" class="btn jjblue">문서등록</a>
                            </div>
                            <div class="defaulttb main_table width100">
                                <table class="width100">
                                    <colgroup>
                                        <col width="5%" />
                                        <col width="9%" />
                                        <col width="9%" />
                                        <col width="16%" />
                                        <col width="6%" />
                                        <col width="6%" />
                                        <col />
                                        <col width="17%" />
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th>번호</th>
                                            <th>세그먼트</th>
                                            <th>타스크</th>
                                            <th>문서명</th>
                                            <th>버전</th>
                                            <th>형식</th>
                                            <th>파일명/관련링크</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${fn:length(documents) > 0}">
                                            <c:forEach var="d" items="${documents}" varStatus="status">
                                                <tr class="tbbody">
                                                    <td>${status.count}</td>
                                                    <td>${d.segmentName}</td>
                                                    <td>${d.taskName}</td>
                                                    <td>${d.title}</td>
                                                    <td>${d.version}</td>
                                                    <td>${d.kindName}</td>
                                                    <td>
                                                        <c:if test='${d.kind eq "L"}'>
                                                            <a href="${d.path}" class="wordbreak" target="_blank">${d.path}</a>
                                                        </c:if>
                                                        <c:if test='${d.kind eq "F"}'>
                                                            <a role="button" class="wordbreak" onclick="downloadAttach('${d.id}')">${d.originalFileName}</a>
                                                        </c:if>
                                                    <td>
                                                        <a role="button" class="btn jjgray s_btn" onclick="openEditModal('${d.id}')">수정</a>
                                                        <a role="button" class="btn jjred s_btn" onclick="deleteDocument('${d.id}', '${d.title}')">삭제</a>
                                                        <a role="button" class="btn jjblue s_btn" onclick="openHistoryModal('${d.id}')">이력</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr class="tbbody">
                                                <td colspan="8">등록된 문서가 없습니다.</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>
                            <div class="btnpart mt10">
                                <a href="<c:url value="/project.do"/>" class="btn jjblue">목록</a>
                            </div>
                        </div>
                    </div>
                    <%@ include file="../include/rnb.jsp" %>
                </div>
            </div>
        </div>
        <%@ include file="../include/footer.jsp" %>

        <div class="modal" id="write-modal">
            <div class="modal_wrap">
                <div class="title_bar clearfix">
                    <h3>문서 등록</h3>
                </div>
                <form id="write-form">
                    <div class="modal_content">
                        <div class="mline mb10 mhalf_m floatleft mr10">
                            <p class="mtitle mb5">세그먼트<span class="required">*</span></p>
                            <select name="segment" class="selectbox width100">
                                <option value="">선택</option>
                                <c:forEach var="s" items="${segment}">
                                    <option value="${s.minorCd}">${s.codeName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mline mb10 mhalf_m floatright">
                            <p class="mtitle mb5">타스크</p>
                            <select name="task" class="selectbox width100">
                                <option value="">세그먼트를 선택해주세요</option>
                            </select>
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">문서명<span class="required">*</span></p>
                            <input type="text" name="title" class="inputbox width100">
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">문서형식<span class="required">*</span></p>
                            <select name="kind" class="selectbox width100">
                                <option value="">선택</option>
                                <c:forEach var="k" items="${kind}">
                                    <option value="${k.minorCd}">${k.codeName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div name="path-div" class="mline mb10 none">
                            <p class="mtitle mb5">첨부파일<span class="required">*</span></p>
                            <div id="files"></div>
                            <a role="button" id="upload-modal-btn" class="btn jjcyan mb5">파일 선택</a>
                        </div>
                        <div name="link-div" class="mline mb10 none">
                            <p class="mtitle mb5">관련링크<span class="required">*</span></p>
                            <input type="text" name="link" class="inputbox width100 mb5">
                        </div>
                        <div class="mbtnbox">
                            <a role="button" id="write-btn" class="btn jjblue" href="#">저장</a>
                            <a role="button" id="write-close-btn" class="btn jjblue">닫기</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="modal" id="edit-modal">
            <div class="modal_wrap">
                <div class="title_bar clearfix">
                    <h3>문서 수정</h3>
                </div>
                <form id="edit-form">
                    <input type="hidden" name="documentId">
                    <input type="hidden" name="version">
                    <div class="modal_content">
                        <div class="mline mb10 mhalf_m floatleft mr10">
                            <p class="mtitle mb5">세그먼트</p>
                            <select name="segment" class="selectbox width100">
                                <option value="">선택</option>
                                <c:forEach var="s" items="${segment}">
                                    <option value="${s.minorCd}">${s.codeName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mline mb10 mhalf_m floatright">
                            <p class="mtitle mb5">타스크</p>
                            <select name="task" class="selectbox width100"></select>
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">문서명</p>
                            <input type="text" name="title" class="inputbox width100">
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">문서형식</p>
                            <select name="kind" class="selectbox width100">
                                <option value="">선택</option>
                                <c:forEach var="k" items="${kind}">
                                    <option value="${k.minorCd}">${k.codeName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div name="path-div" class="mline mb10 none">
                            <p class="mtitle mb5">첨부파일<span class="required">*</span></p>
                            <div id="files2"></div>
                            <a role="button" id="upload-modal-btn2" class="btn jjcyan mb5">파일 선택</a>
                        </div>
                        <div name="link-div" class="mline mb10 none">
                            <p class="mtitle mb5">관련링크<span class="required">*</span></p>
                            <input type="text" name="link" class="inputbox width100 mb5">
                        </div>
                        <div class="mbtnbox">
                            <a role="button" id="edit-btn" class="btn jjblue">저장</a>
                            <a role="button" id="edit-close-btn" class="btn jjblue">닫기</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="modal" id="history-modal">
            <div class="modal_wrap mw_big">
                <div class="title_bar clearfix">
                    <h3>문서변경이력</h3>
                </div>
                <div class="modal_content">
                    <div class="mline mb10">
                        <p id="history-cnt" class="mtitle mb5"></p>
                        <div class="defaulttb md_table">
                            <table class="fixedhead">
                                <colgroup>
                                    <col width="7%">
                                    <col width="28%">
                                    <col width="5%">
                                    <col width="10%">
                                    <col width="27%">
                                    <col width="23%">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>번호</th>
                                        <th>문서명</th>
                                        <th>버전</th>
                                        <th>형식</th>
                                        <th>파일명/관련링크</th>
                                        <th>처리일(처리자)</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                        <div class="defaulttb md_table width100 scrollbody">
                            <table class="width100">
                                <colgroup>
                                    <col width="7%">
                                    <col width="28%">
                                    <col width="5%">
                                    <col width="10%">
                                    <col width="27%">
                                    <col width="23%">
                                </colgroup>
                                <tbody id="histories">
                                </tbody>
                            </table>
                        </div>
                        <div class="mbtnbox">
                            <a role="button" id="history-close-btn" class="btn jjblue">닫기</a>
                        </div>
                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="../include/fileupload.jsp" %>
    <%@ include file="project-document-fileupload.jsp" %>
    <script>
        const projectId = '${project.id}';
        const fileType = 'F';
        const linkType = 'L';
        const writeForm = document.getElementById('write-form');
        const editForm= document.getElementById('edit-form');
    </script>
    <script src="<c:url value="/common/js/project/project-document.js"/>"></script>
</body>

</html>