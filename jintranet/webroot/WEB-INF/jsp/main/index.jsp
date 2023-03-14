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
						<div class="topcontents width100 clearfix">
							<div class="worktime tc floatleft">
								<div class="moretop">
									<a class="more floatright"
										href="<c:url value='/commuting.do'/>">더보기</a>
								</div>
								<div class="tcontent">
									<div class="timecheck midtc">
										<div class="working clearfix">
											<p class="wtime" id="workingTime"></p>
											<p>
												<span class="wk">${workingStatus}</span>
											</p>
										</div>
										<div class="wstart clearfix">
											<p class="ws">출근시간</p>
											<p class="wstime" id="goWorkTime"></p>
										</div>
									</div>
								</div>
							</div>

							<div class="approve tc floatleft">
								<div class="moretop">
									<a role="button" id="vacation-btn" class="more floatright">더보기</a>
								</div>
								<div class="tcontent">
									<div class="apptop midtc clearfix">
										<div class="statetitle">
											<p id="vacation-info">신청한 휴가가 없습니다.</p>
										</div>
										<div class="statement">
											<div class="s_title">
												<p>승인상태</p>
											</div>
											<div class="state">
												<p id="vacation-status"></p>
											</div>
										</div>
									</div>
									<div class="bottc clearfix">
										<p class="app_t">승인대기</p>
										<p id="vacation-cnt" class="app_c"></p>
									</div>
								</div>
							</div>

							<div class="docnum tc floatleft">
								<div class="moretop">
									<a class="moreblack floatright"
										href="<c:url value='/document.do'/>">더보기</a>
								</div>
								<div class="tcontent">
									<div class="doctop midtc clearfix">
										<p class="doctitle">오늘의 문서번호</p>
										<div class="document clearfix">
											<span id="document-no" class="todaydoc"></span> <a
												role="button" id="document-btn" class="btn jjblue docbtn">문서번호발급</a>
										</div>
									</div>
									<div class="bottc clearfix">
										<p class="app_t">이전 문서번호</p>
										<p id="document-cnt" class="app_c"></p>
									</div>
								</div>
							</div>
						</div>

						<div class="project width100">
							<div class="title_bar clearfix">
								<h3>현재 진행 중인 프로젝트</h3>
								<a class="more floatright" href="<c:url value="/project.do"/>">더보기</a>
							</div>
							<div class="pcontent">
								<ul class="tapmenu clearfix">
									<li class="active"><a role="button"
										onclick="projects('');">전체</a></li>
									<c:forEach var="d" items="${department}">
										<li><a role="button" onclick="projects('${d.minorCd}')">${d.codeName}</a></li>
									</c:forEach>
								</ul>
								<div class="defaulttb main_table mtbox width100">
									<table class="width100">
										<colgroup>
											<col width="7%" />
											<col width="48%" />
											<col width="20%" />
											<col width="25%" />
										</colgroup>
										<thead>
											<tr>
												<th>번호</th>
												<th>프로젝트명</th>
												<th>발주사</th>
												<th>기간</th>
											</tr>
										</thead>
										<tbody id="projects">
											<c:forEach var="p" items="${projectList}" varStatus="status">
												<tr class="tbbody"
													onclick="location.href='<c:url value='/project/edit.do?id=${p.id}'/>'">
													<td>${status.count}</td>
													<td>${p.title}</td>
													<td>${p.orderingName}</td>
													<td>${p.startDt}~${p.endDt}</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</div>

						<div class="bottomcontents width100">
							<div class="info bc">
								<div class="title_bar clearfix">
									<h3>공지사항</h3>
									<a class="more floatright" href="<c:url value="/notice.do"/>">더보기</a>
								</div>
								<div class="bccontent width100 clearfix">
									<div class="bcicon icon1"></div>
									<div class="bcinfo">
										<a href="#" id="infoTrigger"><span class="bcstate">${fn:length(noticeList)}건</span>의
											신규 공지사항이 있습니다.</a>
									</div>
								</div>
							</div>
							<div class="product bc">
								<div class="title_bar clearfix">
									<h3>비품관리</h3>
									<a class="more floatright" href="<c:url value="/supply.do"/>">더보기</a>
								</div>
								<div class="bccontent clearfix">
									<div class="bcicon icon2"></div>
									<div class="bcinfo">
										<a href="#" id="supplyTrigger"><span class="bcstate">${fn:length(supplyList)}건</span>의
											대기 중인 비품신청이 있습니다.</a>
									</div>
								</div>
							</div>
						</div>
					</div>
					<%@ include file="../include/rnb.jsp"%>
				</div>
			</div>
		</div>
		<%@ include file="../include/footer.jsp"%>
	</div>
	<%@ include file="main-document.jsp"%>
	<%@ include file="main-vacation.jsp"%>
	<%@ include file="../schedule/edit-modal.jsp"%> <!-- 더보기 수정용 모달(나중에 삭제해야됨) -->
	<%@ include file="../include/datepicker.jsp"%>
	<%@include file="noticePopup.jsp" %>
	
	</div>
	
	<script src="<c:url value="/common/js/jscolor.js"/>"></script>
	<script src='<c:url value="/common/js/main/main.js" />'></script>
	<script src='<c:url value="/common/js/main/main-document.js" />'></script>
	<script src='<c:url value="/common/js/main/main-vacation.js" />'></script>
	<script src='<c:url value="/common/js/main/main-popup.js" />'></script>
	<script>
	const goTime = function () {
        const workingStatus = "${workingStatus}";
        const goWorkTime = document.getElementById("goWorkTime");
        const goToWorkTime = "${goToWorkTime}";
        
        if (workingStatus == '근무중') {
            goWorkTime.innerHTML = goToWorkTime.substr(11, 2) + "시" + goToWorkTime.substr(14, 2) + "분";
            workTime();
        } else if (workingStatus != '근무중' && "${goToWorkTime}" != null) {
            const workingTime = document.getElementById("workingTime");
            const goToWorkTime = "${goToWorkTime}";
            goToWorkTime.innerHTML = goToWorkTime.substr(11, 2) + "시" + goToWorkTime.substr(14, 2) + "분";
            goWorkTime.innerHTML = goToWorkTime.substr(11, 2) + "시" + goToWorkTime.substr(14, 2) + "분";
            const goTime = new Date(goToWorkTime.substr(0, 4), goToWorkTime.substr(5, 2) - 1, goToWorkTime.substr(8, 2), goToWorkTime.substr(11, 2), goToWorkTime.substr(14, 2));
            const offTime = "${offToWorkTime}";
            const now = new Date(offTime.substr(0, 4), offTime.substr(5, 2) - 1, offTime.substr(8, 2), offTime.substr(11, 2), offTime.substr(14, 2));
            const subTime = now.getTime() - goTime.getTime();
            const time = parseInt(subTime / 1000 / 60 / 60);
            const minute = parseInt((subTime - (time * 1000 * 60 * 60)) / 1000 / 60);
            workingTime.innerHTML = time + "시간 " + minute + "분";
        }
    }

    const workTime = function () {
        const workingTime = document.getElementById("workingTime");
        const goWorkTime = "${goToWorkTime}";
        const goTime = new Date(goWorkTime.substr(0, 4), goWorkTime.substr(5, 2) - 1, goWorkTime.substr(8, 2), goWorkTime.substr(11, 2), goWorkTime.substr(14, 2));
        const now = new Date();
        const subTime = now.getTime() - goTime.getTime();
        const time = parseInt(subTime / 1000 / 60 / 60);
        const minute = parseInt((subTime - (time * 1000 * 60 * 60)) / 1000 / 60);
        workingTime.innerHTML = time + "시간 " + minute + "분";
        setTimeout(workTime, 60000);
    }
	goTime();
	</script>
</body>
</html>
