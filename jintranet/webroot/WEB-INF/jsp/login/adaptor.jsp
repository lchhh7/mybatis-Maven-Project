<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head></head>
<body onload="init();">
<script>
    function init() {
        var frm = document.getElementById("MemberVO");
        frm.submit();
    }
</script>
<form id="MemberVO" name="MemberVO" action="<c:url value='/j_spring_security_check'/>" method="post">
    <input type="hidden" name="j_username" value="${MemberVO.memberId}"/>
    <input type="hidden" name="j_password" value="${MemberVO.password}"/>
</form>
</body>
</html>