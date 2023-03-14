<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/jsp/include/htmlHeader.jsp" %>
<body>
<div class="wrap loginwrap">
    <div class="login_header clearfix">
        <img class="logo" src="<c:url value='/common/img/jinjinlogo.svg'/>" alt="진진로고">
        <a href="http://www.jinjin.co.kr/" target="_blank" class="pagelink">홈페이지</a>
    </div>
    <div class="login_container">
        <div class="login_main">
            <p class="login_title">진진시스템 인트라넷 로그인</p>
            <div class="login_box">
                <h3>로그인</h3>
                <input type="text" id="id" class="inputbox" placeholder="아이디를 입력해주세요.">
                <input type="password" id="password" class="inputbox" placeholder="비밀번호를 입력해주세요.">
                <label class="idcheck">
                    <input type="checkbox" id="saveId" class="check">
                    <span>아이디 저장</span>
                </label>
                <a role="button" id="login-btn" class="loginbtn">로그인</a>
            </div>

            <div class="login_footer">
                <address>(04782) 서울시 성동구 연무장 5길 9-16, 7층 704호(성수동2가, 블루스톤타워) 전화 : 02-447-5967, 팩스 : 02-447-5968
                    <br>Copyright© 2021 JinJin System Co., Ltd. All Rights reserved.
                </address>
            </div>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const cookieId = getCookie("id");

        if (cookieId !== null) {
            document.getElementById("id").value = cookieId;
            document.getElementById("saveId").checked = true;
        }
    });

    const login = function () {
        const id = document.getElementById("id").value.trim();
        const password = document.getElementById("password").value.trim();

        if (!(id && password)) {
            alert('아이디와 비밀번호를 모두 입력해주세요.');
            return false;
        }

        $.ajax({
            url: contextPath + "loginProcess.do",
            method: "post",
            data: JSON.stringify({memberId: id.trim(), password: password.trim()}),
            contentType: "application/json; charset:UTF-8"
        })
            .done(function () {
                if (document.getElementById("saveId").checked) {
                    setCookie("id", id, 30);
                } else {
                    deleteCookie("id");
                }

                document.location.href = "<c:url value='/loginAdaptor.do'/>";
            })
            .fail(function (data) {
                alert(data.responseText);
                return false;
            });
    }

    document.getElementById('password').addEventListener('keydown', function (e) {
        if (e.code === 'Enter')
            login();
    });
    document.getElementById('login-btn').addEventListener('click', function () {
        login();
    }, true);

    <%-- 쿠키 설정 --%>
    function setCookie(name, value, exp) {
        var date = new Date();
        date.setTime(date.getTime() + exp * 24 * 60 * 60 * 1000);
        document.cookie = name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
    };

    <%-- 쿠키값 조회 --%>
    function getCookie(name) {
        var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
        return value ? value[2] : null;
    };

    <%-- 쿠키값 삭제 --%>
    function deleteCookie(name) {
        document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    }
</script>
</body>
</html>