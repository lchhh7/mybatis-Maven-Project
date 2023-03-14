<%@ page import="com.jinjin.jintranet.security.SecurityUtils" %>
<%@ page import="com.jinjin.jintranet.common.vo.MemberVO" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String contextPath = request.getContextPath() + "/";
%>

<!DOCTYPE html>
<html class="no-js" lang="ko">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta charset="utf-8"/>
    <title>진진시스템 인트라넷</title>
    <meta name="description" content=""/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, user-scalable=no, maximum-scale=1, minimum-scale=1"/>

    <link rel="stylesheet" type="text/css" href="<c:url value='/common/css/reset.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/common/css/main.css'/>">
    <link rel="stylesheet" href="<c:url value='/common/css/sub.css'/>">
    <link rel="stylesheet" href="<c:url value='/common/jquery/css/jquery-ui.min.css'/>">
    
    <link rel="stylesheet" href='<c:url value="/common/jquery/css/jquery.timepicker.css"/>'>
    
    <script src="<c:url value='/common/jquery/js/jquery-1.10.1.min.js'/>"></script>
    <script src="<c:url value='/common/jquery/js/jquery-ui.min.js'/>"></script>
    
	<script src='<c:url value="/common/jquery/js/jquery.timepicker.min.js"/>'></script>
	
    <script type="text/javascript">
        var contextPath = "<%= contextPath %>";

        var doc = document.documentElement;
        doc.setAttribute('data-useragent', navigator.userAgent);
    </script>
</head>
