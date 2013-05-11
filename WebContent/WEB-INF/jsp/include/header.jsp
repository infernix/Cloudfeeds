
<%@ page isELIgnored="false" 
%><%@page import="net.cloudfeeds.service.APIService"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" 
%><%
boolean isHome = (Boolean)request.getAttribute("isHome");
String titlePrefix = (String)request.getAttribute("titlePrefix");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title><%= titlePrefix + APIService.APP_NAME + " " + APIService.APP_VERSION%></title>
<link href="<%= APIService.HOME_URL %>/favicon.ico" rel="shortcut icon" type="image/x-icon" />
<link rel="stylesheet" href="<%= APIService.HOME_URL %>/style.css" type="text/css"
	media="screen" />
<!--[if lte IE 6]>
	<style type="text/css" media="screen">
		body
		{
			display:none;
		}
	</style>
	<script type="text/javascript">
		alert("Your browser sucks!");
	 </script>
<![endif]-->

<c:if test="${isHome}">
<link rel="stylesheet" href="<%= APIService.HOME_URL %>/fancybox/jquery.fancybox-1.3.1.css" type="text/css" media="screen" />
<script type="text/javascript" src="<%= APIService.HOME_URL %>/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%= APIService.HOME_URL %>/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%= APIService.HOME_URL %>/fancybox/jquery.fancybox-1.3.1.pack.js"></script>


<script type="text/javascript">

function applyAjax()  {
			$.fancybox.hideActivity();
			$("#feedback-form").submit(function() { 
        		$(this).ajaxSubmit({ 
        				target:'#feedback-container',
						success:applyAjax,
						beforeSubmit:$.fancybox.showActivity
						});
        		return false; 
    		});
} 
$(document).ready(function() {
	$('a#link-button').fancybox({
		'ajax'	:	{'data': {raw:"true"}},
		'onComplete' : applyAjax,
		'transitionIn'	:	'fade',
		'transitionOut'	:	'fade',
		'speedIn'		:	600, 
		'speedOut'		:	200, 
		'overlayShow'	:	true
		
	});

});
 </script>
</c:if>
</head>
<body>
		<script type="text/javascript">

			  var _gaq = _gaq || [];
			  _gaq.push(['_setAccount', 'UA-5838008-3']);
			  _gaq.push(['_setDomainName', '.theblackestbox.net']);
			  _gaq.push(['_trackPageview']);

			  (function() {
				var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
				ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
				var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
			  })();

		</script>

<div id="logo"><a href="<%= APIService.HOME_URL %>/"><img src="<%= APIService.HOME_URL %>/left.png" alt="Cloudfeeds" /></a></div>
<div id="try-it"><a onclick="window.open(this.href); return false;" href="<%= APIService.HOME_URL %>/tracks"><img src="<%= APIService.HOME_URL %>/right.png" alt="Try Cloudfeeds" /></a></div>
<div id="content">