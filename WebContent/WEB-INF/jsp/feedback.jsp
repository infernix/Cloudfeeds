<?xml version="1.0" encoding="iso-8859-1"?>
<%
request.setAttribute("isHome",false);
request.setAttribute("titlePrefix","Feedback - ");
request.setAttribute("redirectLink","/");
request.setAttribute("redirectName","Home");
%>
<%@ include file="include/header.jsp" %>
<h1>Feedback</h1>
<div class="box">
<%@ include file="/WEB-INF/jsp/feedback.raw.jsp" %>
</div>
<%@ include file="include/footer.jsp" %>