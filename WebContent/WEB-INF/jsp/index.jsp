<?xml version="1.0" encoding="iso-8859-1"?>
<%
request.setAttribute("isHome",true);
request.setAttribute("titlePrefix","");
request.setAttribute("redirectLink","/feedback");
request.setAttribute("redirectName","Feedback");
%>
<%@ include file="include/header.jsp" %>
<h1>Welcome to Cloudfeeds</h1>
<p>You are using <a onclick="window.open(this.href); return false;" href="http://soundcloud.com" class="soundcloud-link">Soundcloud</a> and you need a <strong>feed</strong>?</p>
<p><strong>Here you are! </strong></p>
<p><strong>Cloudfeeds</strong> offers feeds for various Soundcloud-Resources!</p>
<h2>RESTful</h2>
<p>Cloudfeeds is basically a <i>remix</i> of the <a onclick="window.open(this.href); return false;" href="http://wiki.github.com/soundcloud/api/" class="soundcloud-link">Soundcloud-API</a>.</p>
<p>Following resources are available as feeds: <strong>users, tracks, groups, comments</strong></p>
<p>Requesting these URL-Patterns will generate (in most cases) a feed:</p>

<strong style="float: left">URL-Pattern</strong><strong style="float: right;">Soundcloud-Resource</strong>
		<ul style="clear:both;padding-bottom:20px">
			<li><pre>/users/{user_id}/tracks</pre>
			<span class="list-resource">tracks</span></li>
			<li><pre>/users/{user_id}/favorites</pre>
			<span class="list-resource">tracks</span></li>			
			<li><pre>/users/{user_id}/followings</pre>
			<span class="list-resource">users</span></li>
			<li><pre>/users/{user_id}/followers</pre>
			<span class="list-resource">users</span></li>
			<li><pre>/users/{user_id}/groups</pre>	
			<span class="list-resource">groups</span></li>	
			<li><pre>/users/{user_id}/comments</pre>
			<span class="list-resource">comments</span></li>
			<li class="list-separator"><pre>/groups/{group_id}/tracks</pre>
			<span class="list-resource">tracks</span></li>	
			<li><pre>/groups/{group_id}/users</pre>	
			<span class="list-resource">users</span></li>			
			<li><pre>/groups/{group_id}/moderators</pre>
			<span class="list-resource">users</span></li>	
			<li><pre>/groups/{group_id}/members</pre>
		<span class="list-resource">users</span></li>	
		<li><pre>/groups/{group_id}/contributors</pre>	
			<span class="list-resource">users</span></li>
		<li class="list-separator"><pre>/tracks</pre>
			<span class="list-resource">tracks</span></li>
		<li><pre>/tracks/{track_id}</pre>		
			<span class="list-resource">comments</span></li>
</ul>
<p style="clear:both"><strong>user_id and track_id </strong><i>can either be the Soundcloud Integer-ID or the Permalink-Name (e.g. <a onclick="window.open(this.href); return false;" href="<%= APIService.HOME_URL %>/users/laurent-garnier/tracks">/users/laurent-garnier/tracks</a>)!</i></p>
<p>For collections of resource <strong>tracks</strong> you can apply a filter like described in the <a onclick="window.open(this.href); return false;" href="http://wiki.github.com/soundcloud/api/102-resources-tracks" class="soundcloud-link">API</a> <i>(e.g. <a onclick="window.open(this.href); return false;" href="<%= APIService.HOME_URL %>/tracks?genres=techno,minimal&amp;order=created_at">/tracks?genres=techno,minimal&amp;order=created_at</a>)!</i></p>
<p><strong>Of course, only public available resources are fetched!</strong></p>
<h2>Formats</h2>
<p>Currently only <strong>RSS 2.0</strong> is supported.
You will get a minimal but <i>valid</i> RSS-Feed!
<p>
<img src="<%= APIService.HOME_URL %>/feeddemon.png" alt="Feeddemon"" />
</p>

</p>
<%@ include file="include/footer.jsp" %>

