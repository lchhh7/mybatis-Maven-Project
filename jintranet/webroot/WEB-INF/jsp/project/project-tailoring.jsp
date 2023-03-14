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
                            <h3 class="st_title">테일러링</h3>
                            <span class="st_exp">진행 중이거나 완료된 프로젝트를 조회할 수 있습니다.</span>
                        </div>
                        <div class="locationbar clearfix">
                            <a href="<c:url value="/main.do"/>" class="home"></a>
                            <span class="local">프로젝트관리</span>
                            <span class="local">테일러링</span>
                        </div>
                    </div>
                    <div class="mcpart width100">
                        <ul class="tapmenu clearfix">
                            <li><a href="<c:url value="/project/edit.do?id=${project.id}"/>">기본정보</a></li>
                            <li><a href="<c:url value="/project/member.do?id=${project.id}"/>">투입인력</a></li>
                            <li><a href="<c:url value="/project/schedule.do?id=${project.id}"/>">일정</a></li>
                            <li class="active"><a href="<c:url value="/project/tailoring.do?id=${project.id}"/>">테일러링</a></li>
                            <li><a href="<c:url value="/project/document.do?id=${project.id}"/>">문서</a></li>
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
                            <p class="total">총 ${fn:length(segList)}개</p>
                            <a role="button" id="add-btn" class="btn jjblue">추가</a>
                        </div>
                        <div class="defaulttb main_table width100">
                            <table class="width100">
                                <colgroup>
                                    <col width="8%"/>
                                    <col width="15%"/>
                                    <col width="*%"/>
                                    <col width="18%"/>
                                    <col width="18%"/>
                                    <col width="18%"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>번호</th>
                                    <th>세그먼트코드</th>
                                    <th>세그먼트명</th>
                                    <th>등록일</th>
                                    <th>수정일</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${fn:length(segList) > 0}">
                                        <c:forEach var="s" items="${segList}" varStatus="status">
                                            <tr class="tbbody">
                                                <td>${status.count}</td>
                                                <td>${s.kindName}</td>
                                                <td>${s.title}</td>
                                                <td>${s.crtDt}</td>
                                                <td>${s.udtDt}</td>
                                                <td>
                                                    <a role="button" class="btn jjgray"
                                                       onclick="openEditModal('${s.id}')">수정</a>
                                                    <a role="button" class="btn jjred"
                                                       onclick="deleteSegment('${s.id}', '${s.title}')">삭제</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr class="tbbody">
                                            <td colspan="6">등록된 세그먼트가 없습니다.</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                        <div class="btnpart mt10">
                            <a class="btn jjblue" href="<c:url value="/project.do"/>">목록</a>
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
                <h3>세그먼트 추가</h3>
            </div>
            <form id="write-form">
                <div class="modal_content">
                    <div class="mline mb10">
                        <p class="mtitle mb5">세그먼트 코드<span class="required">*</span></p>
                        <select name="kind" class="selectbox width100">
                            <option value="">선택</option>
                            <c:forEach var="s" items="${segment}">
                                <option value="${s.minorCd}">${s.codeName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">세그먼트명<span class="required">*</span></p>
                        <input type="text" name="title" class="inputbox width100">
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5 floatleft">정렬순서</p>
                        <span class="m_alert ml10">※ 두 자리 숫자로 입력</span>
                        <input type="text" name="ord" class="inputbox width100">
                    </div>
                    <div class="mbtnbox">
                        <a role="button" id="write-btn" class="btn jjblue">저장</a>
                        <a role="button" id="write-close-btn" class="btn jjblue">닫기</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="modal" id="edit-modal">
        <div class="modal_wrap">
            <div class="title_bar clearfix">
                <h3>세그먼트 수정</h3>
            </div>
            <form id="edit-form">
                <div class="modal_content">
                    <input type="hidden" name="id">
                    <div class="mline mb10">
                        <p class="mtitle mb5">세그먼트 코드</p>
                        <select name="kind" class="selectbox width100">
                            <option value="">선택</option>
                            <c:forEach var="s" items="${segment}">
                                <option value="${s.minorCd}">${s.codeName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5">세그먼트명</p>
                        <input type="text" name="title" class="inputbox width100">
                    </div>
                    <div class="mline mb10">
                        <p class="mtitle mb5 floatleft">정렬순서</p>
                        <span class="m_alert ml10">※ 두 자리 숫자로 입력</span>
                        <input type="text" name="ord" class="inputbox width100">
                    </div>
                    <div class="mbtnbox">
                        <a role="button" id="edit-btn" class="btn jjblue">저장</a>
                        <a role="button" id="edit-close-btn" class="btn jjblue">닫기</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    const projectId = '${project.id}';
    const writeForm = document.getElementById('write-form');
    const editForm = document.getElementById('edit-form');
</script>
<script src="<c:url value="/common/js/project/project-tailoring.js"/> "></script>
</body>

</html>