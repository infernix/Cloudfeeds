<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="feedback-container">
<%
boolean success = (Boolean)request.getAttribute("success");
%>
<c:choose>
<c:when test="${!success}">
<h2 class="green">Good ideas are welcome</h2>
<form:form modelAttribute="feedback"
	method="post" id="feedback-form" >
	<p><form:label id="nameLabel" for="name" path="name"
		cssErrorClass="error">Name</form:label>
	<form:input path="name" /><!--<form:errors path="name" />--></p>
	<p><form:label id="emailLabel" for="email" path="email"
		cssErrorClass="error">Email</form:label>
	<form:input path="email" /><!--<form:errors path="email" />--></p>
		<p><form:label id="titleLabel" for="title" path="title"
		cssErrorClass="error">Title</form:label>
	<form:input path="title" /><!--<form:errors path="title" />--></p>
			<p><form:label id="feedbackLabel" for="feedback" path="feedback"
		cssErrorClass="error">Feedback</form:label>
	<form:textarea path="feedback" rows="10" cols="40"/><!--<form:errors path="feedback" />--></p>
	<p><input id="feedback-submit" type="submit" value="Send" /></p>
</form:form>
</c:when>

<c:otherwise>
<h2 class="success">Thank you :)</h2>
</c:otherwise>
</c:choose>
</div>