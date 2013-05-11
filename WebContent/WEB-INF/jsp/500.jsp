<?xml version="1.0" encoding="iso-8859-1"?>
<%
request.setAttribute("isHome",false);
request.setAttribute("titlePrefix","500 - ");
request.setAttribute("redirectLink","/");
request.setAttribute("redirectName","Home");
%>
<%@ include file="include/header.jsp" %>
<% Exception exception = (Exception) request.getAttribute("exception");
String message = "Sorry, an internal error occured";
if(exception != null)
{
	message = exception.getMessage();
	//exception.printStackTrace();
}
%>
<h1>Error</h1>
<h2 class="error"><%= message %></h2>
<%@ include file="include/footer.jsp" %>