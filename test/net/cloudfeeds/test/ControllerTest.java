package net.cloudfeeds.test;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import static org.junit.Assert.*;

import static net.cloudfeeds.service.APIService.*;

public class ControllerTest {
	
	private final static String VALID_USER = "laurent-garnier";
	private final static String VALID_GROUP = "200";
	private final static String VALID_TRACK = "confort-fit-lucifer-laurent-garnier-remix-tokyo-dawn-rec";

	private final static int VALID_RESPONSE = 200;
	
	private final DefaultHttpClient client = new DefaultHttpClient();
	
	@Test
	public void testTracks() throws ClientProtocolException, IOException
	{
		HttpResponse response  = client.execute(new HttpGet(HOME_URL + PATH_SEPARATOR + RESOURCE_TRACKS));
		assertEquals(VALID_RESPONSE , response.getStatusLine().getStatusCode());
		client.getConnectionManager().shutdown();
	}
	
	@Test
	public void testTrackComments() throws ClientProtocolException, IOException
	{
		HttpResponse response  = client.execute(new HttpGet(HOME_URL + PATH_SEPARATOR + RESOURCE_TRACKS + PATH_SEPARATOR + VALID_TRACK));
		assertEquals(VALID_RESPONSE , response.getStatusLine().getStatusCode());
		client.getConnectionManager().shutdown();
	}
	
	@Test
	public void testUserTracks() throws ClientProtocolException, IOException
	{
		HttpResponse response  = client.execute(new HttpGet(HOME_URL + PATH_SEPARATOR + RESOURCE_USERS + PATH_SEPARATOR + VALID_USER + PATH_SEPARATOR + RESOURCE_TRACKS));
		assertEquals(VALID_RESPONSE , response.getStatusLine().getStatusCode());
		client.getConnectionManager().shutdown();
	}
	
	@Test
	public void testUserComments() throws ClientProtocolException, IOException
	{
		HttpResponse response  = client.execute(new HttpGet(HOME_URL + PATH_SEPARATOR + RESOURCE_USERS + PATH_SEPARATOR + VALID_USER + PATH_SEPARATOR + RESOURCE_COMMENTS));
		assertEquals(VALID_RESPONSE , response.getStatusLine().getStatusCode());
		client.getConnectionManager().shutdown();
	}
	
	@Test
	public void testUserFavorites() throws ClientProtocolException, IOException
	{
		HttpResponse response  = client.execute(new HttpGet(HOME_URL + PATH_SEPARATOR + RESOURCE_USERS + PATH_SEPARATOR + VALID_USER + PATH_SEPARATOR + RESOURCE_FAVORITES));
		assertEquals(VALID_RESPONSE , response.getStatusLine().getStatusCode());
		client.getConnectionManager().shutdown();
	}
	
	@Test
	public void testUserGroups() throws ClientProtocolException, IOException
	{
		HttpResponse response  = client.execute(new HttpGet(HOME_URL + PATH_SEPARATOR + RESOURCE_USERS + PATH_SEPARATOR + VALID_USER + PATH_SEPARATOR + RESOURCE_GROUPS));
		assertEquals(VALID_RESPONSE , response.getStatusLine().getStatusCode());
		client.getConnectionManager().shutdown();
	}
	
	@Test
	public void testUserFollowings() throws ClientProtocolException, IOException
	{
		HttpResponse response  = client.execute(new HttpGet(HOME_URL + PATH_SEPARATOR + RESOURCE_USERS + PATH_SEPARATOR + VALID_USER + PATH_SEPARATOR + RESOURCE_FOLLOWINGS));
		assertEquals(VALID_RESPONSE , response.getStatusLine().getStatusCode());
		client.getConnectionManager().shutdown();
	}
	
	@Test
	public void testUserFollowers() throws ClientProtocolException, IOException
	{
		HttpResponse response  = client.execute(new HttpGet(HOME_URL + PATH_SEPARATOR + RESOURCE_USERS + PATH_SEPARATOR + VALID_USER + PATH_SEPARATOR + RESOURCE_FOLLOWERS));
		assertEquals(VALID_RESPONSE , response.getStatusLine().getStatusCode());
		client.getConnectionManager().shutdown();
	}
	
	
	@Test
	public void testGroupTracks() throws ClientProtocolException, IOException
	{
		HttpResponse response  = client.execute(new HttpGet(HOME_URL + PATH_SEPARATOR + RESOURCE_GROUPS + PATH_SEPARATOR + VALID_GROUP + PATH_SEPARATOR + RESOURCE_TRACKS));
		assertEquals(VALID_RESPONSE , response.getStatusLine().getStatusCode());
		client.getConnectionManager().shutdown();
	}
	
	@Test
	public void testGroupUsers() throws ClientProtocolException, IOException
	{
		HttpResponse response  = client.execute(new HttpGet(HOME_URL + PATH_SEPARATOR + RESOURCE_GROUPS + PATH_SEPARATOR + VALID_GROUP + PATH_SEPARATOR + RESOURCE_USERS));
		assertEquals(VALID_RESPONSE , response.getStatusLine().getStatusCode());
		client.getConnectionManager().shutdown();
	}
	
	@Test
	public void testGroupModerators() throws ClientProtocolException, IOException
	{
		HttpResponse response  = client.execute(new HttpGet(HOME_URL + PATH_SEPARATOR + RESOURCE_GROUPS + PATH_SEPARATOR + VALID_GROUP + PATH_SEPARATOR + RESOURCE_MODERATORS));
		assertEquals(VALID_RESPONSE , response.getStatusLine().getStatusCode());
		client.getConnectionManager().shutdown();
	}
	
	@Test
	public void testGroupMembers() throws ClientProtocolException, IOException
	{
		HttpResponse response  = client.execute(new HttpGet(HOME_URL + PATH_SEPARATOR + RESOURCE_GROUPS + PATH_SEPARATOR + VALID_GROUP + PATH_SEPARATOR + RESOURCE_MEMBERS));
		assertEquals(VALID_RESPONSE , response.getStatusLine().getStatusCode());
		client.getConnectionManager().shutdown();
	}
	
	@Test
	public void testGroupContributors() throws ClientProtocolException, IOException
	{
		HttpResponse response  = client.execute(new HttpGet(HOME_URL + PATH_SEPARATOR + RESOURCE_GROUPS + PATH_SEPARATOR + VALID_GROUP + PATH_SEPARATOR + RESOURCE_CONTRIBUTORS));
		assertEquals(VALID_RESPONSE , response.getStatusLine().getStatusCode());
		client.getConnectionManager().shutdown();
	}
}
