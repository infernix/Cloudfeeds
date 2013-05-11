<?xml version="1.0" encoding="iso-8859-1"?>
<%@page import="net.cloudfeeds.exception.HtmlMessageException,net.cloudfeeds.service.APIService"%>
<%
request.setAttribute("isHome",false);
request.setAttribute("titlePrefix","404 - ");
request.setAttribute("redirectLink","/");
request.setAttribute("redirectName","Home");
%>
<%@ include file="include/header.jsp" %>
<% HtmlMessageException exception = (HtmlMessageException) request.getAttribute("exception");
String message = "Sorry, not found";
if(exception != null)
{
	message = exception.getHtmlMessage();
}
%>
<h1>Error</h1>
<h2 class="error"><%= message %></h2>
<%@ include file="include/footer.jsp" %>