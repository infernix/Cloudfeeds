<%@ page isELIgnored="false"%><%@page
	import="net.cloudfeeds.service.APIService"%>
<%
	String redirectLink = (String) request.getAttribute("redirectLink");
	String redirectName = (String) request.getAttribute("redirectName");
%>
</div>
<div id="footer"><img id="validation-icon"
	src="<%=APIService.HOME_URL%>/rss.png" alt="Valid RSS 2.0" /><a
	href="http://soundcloud.com" title="Soundcloud"
	onclick="window.open(this.href); return false;"><img
	id="soundcloud-icon" src="<%=APIService.HOME_URL%>/soundcloud.png"
	alt="Soundcloud" /></a><a href="http://www.springframework.org/"
	title="Spring - java/j2ee Application Framework"
	onclick="window.open(this.href); return false;"><img
	src="http://www.springframework.org/buttons/spring_80x15.png"
	id="spring-icon" width="80" height="15" alt="Spring Framework" /></a> <a
	id="link-button" class="bottom-left"
	href="<%=APIService.HOME_URL%>${redirectLink}">${redirectName}</a></div>
</body>
</html>