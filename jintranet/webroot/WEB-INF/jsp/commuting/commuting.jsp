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
                    <div class="subtitle clearfix">
                        <div class="st clearfix">
                            <h3 class="st_title">근태관리</h3>
                            <span class="st_exp">나의 근태현황을 확인할 수 있습니다.</span>
                        </div>
                        <div class="locationbar clearfix">
                            <a href="<c:url value="/main.do"/>" class="home"></a>
                            <span class="local">근태관리</span>
                        </div>
                    </div>
                    <div class="topcal width100 clearfix">
                        <div class="worktime tc floatleft">
                            <div class="tcontent rccont">
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
                                <div class="checkbtn bottc clearfix">
                                   
                                </div>
                            </div>
                        </div>
                        <div class="daycheck floatleft">
                            <div class="tcontent">
                                <div class="apptop midtc clearfix">
                                    <div class="statetitle">
                                        <p id="request-title"></p>
                                    </div>
                                    <div class="statement dchk">
                                        <div class="s_title">
                                            <p id="request-content"></p>
                                        </div>
                                        <div class="state dchk">
                                            <p id="request-status">${nearRequest.statusName}</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="bottc clearfix">
                                    <a role="button" id="apply-detail-btn" class="go">내역확인</a>
                                </div>
                            </div>
                        </div>
                            <div class="defaulttb caltable rctable floatleft">
                                <table class="width100">
                                    <colgroup>
                                        <col width="30%"/>
                                        <col width="70%"/>
                                    </colgroup>
                                    <tbody>
                                    <tr>
                                        <th>금월초과근무<br>통계</th>
                                        <td id="total-ot" class="dwbtntb">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>초과근무<br>엑셀다운로드</th>
                                        <td class="dwbtntb">
                                            <a class="btn jjdownload" href="javascript:void(0)" id="excelWriter"><span
                                                    class="dwicon">월간 초과근무</span></a>
                                            <a class="btn jjdownload ml5" href="javascript:void(0)" id="excel_yearchoiceModal"><span
                                                    class="dwicon">연간 초과근무</span></a>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                    </div>
                    <div class="mcpart" id='calendar-container'>
                        <div id='calendar'></div>
                    </div>

                    <c:set var="id" value="${memberId}"></c:set>

                </div>
                <%@ include file="../include/rnb.jsp" %>
            </div>
        </div>
    </div>
    <%@ include file="apply-modal.jsp" %>
    <%@ include file="commuting-edit.jsp" %>
    <%@ include file="commuting-write.jsp" %>
     <%@ include file="commuting-apply.jsp" %>
     <%@ include file="excel-yearchoiceModal.jsp" %>

    <%@ include file="../include/footer.jsp" %>
    <%@ include file="../include/datepicker.jsp" %>
    <%@ include file="../include/fullcalendar.jsp" %>
</div>
<script src='<c:url value="/common/js/commuting/commuting-common.js" />'></script>
<script src='<c:url value="/common/js/commuting/commuting-write.js" />'></script>
<script src='<c:url value="/common/js/commuting/commuting-excel.js" />'></script>
<script src='<c:url value="/common/js/commuting/commuting-apply.js" />'></script>
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

    const commuteInsert = function (value) {
        if (value == 'N' && new Date().getTime() > 0 && new Date().getTime() < 9) {
            value = 'V';
        }
        $.ajax({
            type: "post",
            url: contextPath + "/main/goToWorkButton.do",
            data: JSON.stringify({attendYn: value}),
            contentType: "application/json; charset=utf-8",
        }).done(function () {
            window.location.reload();
        }).fail(function () {
            alert("정보 조회 중 오류가 발생했습니다. 관리자에게 문의하세요.");
        });
    }
    goTime();
    
    document.getElementById('commuteOn').addEventListener('click', function () {
    	commuteInsert('Y');
    }, true);
    
    document.getElementById('commuteOff').addEventListener('click', function () {
    	commuteInsert('N');
    }, true);
</script>
</body>

</html>
