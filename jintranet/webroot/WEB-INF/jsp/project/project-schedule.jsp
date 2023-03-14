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
                                <h3 class="st_title">일정</h3>
                                <span class="st_exp">진행 중인 프로젝트를 조회할 수 있습니다.</span>
                            </div>
                            <div class="locationbar clearfix">
                                <a href="<c:url value="/main.do"/>" class="home"></a>
                                <span class="local">프로젝트관리</span>
                                <span class="local">일정</span>
                            </div>
                        </div>
                        <div class="mcpart width100">
                            <ul class="tapmenu clearfix">
                                <li><a href="<c:url value="/project/edit.do?id=${project.id}"/>">기본정보</a></li>
                                <li><a href="<c:url value="/project/member.do?id=${project.id}"/>">투입인력</a></li>
                                <li class="active"><a href="<c:url value="/project/schedule.do?id=${project.id}"/>">일정</a></li>
                                <li><a href="<c:url value="/project/tailoring.do?id=${project.id}"/>">테일러링</a></li>
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
                            <div class="pschpart clearfix">
                                <a role="button" id="add-btn" class="btn jjblue">일정등록</a>
                                <div class="psch_title width100 clearfix">
                                    <a role="button" id="year-minus" class="schprev btn floatleft"></a>
                                    <a role="button" id="year-plus" class="schnext btn floatright"></a>
                                    <h3 id="target-year"></h3>
                                </div>
                            </div>
                            <div class="defaulttb sub_table pschtb">
                                <div class="pschbox clearfix floatright mb10">
                                    <label class="chklabel_tb floatleft mr20">
                                        <input type="checkbox" id="search-kind1" class="check greenchk" value="001" checked><span
                                            id="kind1-cnt"
                                            class="checkwd"></span>
                                    </label>
                                    <label class="chklabel_tb floatleft mr20">
                                        <input type="checkbox" id="search-kind2" class="check purplechk" value="002" checked><span
                                            id="kind2-cnt"
                                            class="checkwd"></span>
                                    </label>
                                    <label class="chklabel_tb floatleft">
                                        <input type="checkbox" id="search-kind3" class="check yellowchk" value="003" checked><span
                                            id="kind3-cnt"
                                            class="checkwd"></span>
                                    </label>
                                </div>
                                <table id="schedule-table" class="width100">
                                    <colgroup>
                                        <col width="10%">
                                        <col width="18%">
                                        <col width="18%">
                                        <col width="18%">
                                        <col width="18%">
                                        <col width="18%">
                                    </colgroup>
                                    <tbody>
                                        <tr class="tbhead">
                                            <td></td>
                                            <th>1주차</th>
                                            <th>2주차</th>
                                            <th>3주차</th>
                                            <th>4주차</th>
                                            <th>5주차</th>
                                        </tr>
                                        <tr class="tbbody">
                                            <th>1월</th>
                                            <td id="1-1"></td>
                                            <td id="1-2"></td>
                                            <td id="1-3"></td>
                                            <td id="1-4"></td>
                                            <td id="1-5"></td>
                                        </tr>
                                        <tr id="2" class="tbbody">
                                            <th>2월</th>
                                            <td id="2-1"></td>
                                            <td id="2-2"></td>
                                            <td id="2-3"></td>
                                            <td id="2-4"></td>
                                            <td id="2-5"></td>
                                        </tr>
                                        <tr class="tbbody">
                                            <th>3월</th>
                                            <td id="3-1"></td>
                                            <td id="3-2"></td>
                                            <td id="3-3"></td>
                                            <td id="3-4"></td>
                                            <td id="3-5"></td>
                                        </tr>
                                        <tr class="tbbody">
                                            <th>4월</th>
                                            <td id="4-1"></td>
                                            <td id="4-2"></td>
                                            <td id="4-3"></td>
                                            <td id="4-4"></td>
                                            <td id="4-5"></td>
                                        </tr>
                                        <tr class="tbbody">
                                            <th>5월</th>
                                            <td id="5-1"></td>
                                            <td id="5-2"></td>
                                            <td id="5-3"></td>
                                            <td id="5-4"></td>
                                            <td id="5-5"></td>
                                        </tr>
                                        <tr class="tbbody">
                                            <th>6월</th>
                                            <td id="6-1"></td>
                                            <td id="6-2"></td>
                                            <td id="6-3"></td>
                                            <td id="6-4"></td>
                                            <td id="6-5"></td>
                                        </tr>
                                        <tr class="tbbody">
                                            <th>7월</th>
                                            <td id="7-1"></td>
                                            <td id="7-2"></td>
                                            <td id="7-3"></td>
                                            <td id="7-4"></td>
                                            <td id="7-5"></td>
                                        </tr>
                                        <tr class="tbbody">
                                            <th>8월</th>
                                            <td id="8-1"></td>
                                            <td id="8-2"></td>
                                            <td id="8-3"></td>
                                            <td id="8-4"></td>
                                            <td id="8-5"></td>
                                        </tr>
                                        <tr class="tbbody">
                                            <th>9월</th>
                                            <td id="9-1"></td>
                                            <td id="9-2"></td>
                                            <td id="9-3"></td>
                                            <td id="9-4"></td>
                                            <td id="9-5"></td>
                                        </tr>
                                        <tr class="tbbody">
                                            <th>10월</th>
                                            <td id="10-1"></td>
                                            <td id="10-2"></td>
                                            <td id="10-3"></td>
                                            <td id="10-4"></td>
                                            <td id="10-5"></td>
                                        </tr>
                                        <tr class="tbbody">
                                            <th>11월</th>
                                            <td id="11-1"></td>
                                            <td id="11-2"></td>
                                            <td id="11-3"></td>
                                            <td id="11-4"></td>
                                            <td id="11-5"></td>
                                        </tr>
                                        <tr class="tbbody">
                                            <th>12월</th>
                                            <td id="12-1"></td>
                                            <td id="12-2"></td>
                                            <td id="12-3"></td>
                                            <td id="12-4"></td>
                                            <td id="12-5"></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <%@ include file="../include/rnb.jsp" %>
                </div>
            </div>
        </div>
        <%@ include file="../include/footer.jsp" %>

        <div class="modal" id="write-modal">
            <div class="modal_wrap" style="z-index: 999;">
                <div class="title_bar clearfix">
                    <h3>일정 등록</h3>
                </div>
                <form id="write-form">
                    <div class="modal_content">
                        <div class="mline mb10">
                            <p class="mtitle mb5">일정구분<span class="required">*</span></p>
                            <select name="kind" class="selectbox width100">
                                <option value="">선택</option>
                                <c:forEach var="s" items="${schedule}">
                                    <option value="${s.minorCd}">${s.codeName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">일정명<span class="required">*</span></p>
                            <input type="text" name="title" class="inputbox width100">
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">시작일<span class="required">*</span></p>
                            <input type="text" name="scheduleStartDt" class="inputbox mhalf datepicker" readonly>
                            <input type="text" name="scheduleStartTm" class="inputbox mhalf time" placeholder="10:00">
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">종료일<span class="required">*</span></p>
                            <input type="text" name="scheduleEndDt" class="inputbox mhalf datepicker" readonly>
                            <input type="text" name="scheduleEndTm" class="inputbox mhalf time" placeholder="19:00">
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">장소</p>
                            <input type="text" name="place" class="inputbox width100">
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">참석자</p>
                            <input type="text" name="attendee" class="inputbox width100">
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">비고</p>
                            <input type="text" name="remark" class="inputbox width100">
                        </div>
                        <div class="mbtnbox">
                            <a role="button" id="write-btn" class="btn jjblue">등록</a>
                            <a role="button" id="write-close-btn" class="btn jjblue">닫기</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="modal" id="edit-modal">
            <div class="modal_wrap" style="z-index: 999;">
                <div class="title_bar clearfix">
                    <h3>일정 정보</h3>
                </div>
                <form id="edit-form">
                    <input type="hidden" name="id">
                    <div class="modal_content">
                        <div class="mline mb10">
                            <p class="mtitle mb5">일정구분<span class="required">*</span></p>
                            <select name="kind" class="selectbox width100">
                                <option value="">선택</option>
                                <c:forEach var="s" items="${schedule}">
                                    <option value="${s.minorCd}">${s.codeName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">일정명<span class="required">*</span></p>
                            <input type="text" name="title" class="inputbox width100">
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">시작일<span class="required">*</span></p>
                            <input type="text" name="scheduleStartDt" class="inputbox mhalf datepicker" readonly>
                            <input type="text" name="scheduleStartTm" class="inputbox mhalf time" placeholder="10:00">
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">종료일<span class="required">*</span></p>
                            <input type="text" name="scheduleEndDt" class="inputbox mhalf datepicker" readonly>
                            <input type="text" name="scheduleEndTm" class="inputbox mhalf time" placeholder="19:00">
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">장소</p>
                            <input type="text" name="place" class="inputbox width100">
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">참석자</p>
                            <input type="text" name="attendee" class="inputbox width100">
                        </div>
                        <div class="mline mb10">
                            <p class="mtitle mb5">비고</p>
                            <input type="text" name="remark" class="inputbox width100">
                        </div>
                        <div class="mbtnbox">
                            <a role="button" id="edit-btn" class="btn jjblue">수정</a>
                            <a role="button" id="delete-btn" class="btn jjred">삭제</a>
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
        const targetYearDiv = document.getElementById('target-year');
        let targetYear = new Date().getFullYear();

        function getWeekOfMonth(date) {
            var selectedDayOfMonth = date.getDate();
            var first = new Date(date.getFullYear() + '/' + (date.getMonth() + 1) + '/01');
            var monthFirstDateDay = first.getDay();
            return Math.ceil((selectedDayOfMonth + monthFirstDateDay) / 7);
        }
    </script>
    <script src="<c:url value="/common/js/project/project-schedule.js"/>"></script>
    <%@ include file="../include/datepicker.jsp" %>
</body>

</html>