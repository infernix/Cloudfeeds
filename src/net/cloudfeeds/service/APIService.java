package net.cloudfeeds.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.Format;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.cloudfeeds.exception.HtmlMessageException;
import net.cloudfeeds.model.Comments;
import net.cloudfeeds.model.Group;
import net.cloudfeeds.model.Groups;
import net.cloudfeeds.model.Track;
import net.cloudfeeds.model.Tracks;
import net.cloudfeeds.model.User;
import net.cloudfeeds.model.Users;
import net.cloudfeeds.view.AbstractRssView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class APIService {
	
	/********************Configuration***********************/

	public static final String APP_NAME = "Cloudfeeds";
	public static final String APP_VERSION = "1.3.6";
	
	//the soundcloud consumer key. you can get it by registering your app under http://soundcloud.com/you/apps/ (after loggin in)
	public static final String CONSUMER_KEY = "5d881d05ea317df73def25a3798664a3";

	
	//don't know for what is that
	public static final String APP_AUTHOR_NAME = "Webmaster";
	
	
	//information for the feedback mailer
	public static final String APP_FEEDBACK_EMAIL = "";
	public static final String SMTP_HOST = "";
	public static final String SMTP_USER = "";
	public static final String SMTP_PASSWORD = "";

	
	
	//needed for global links in the feeds, workaround for mod_jk
	public static final String HOME_URL = "http://spirit.infernix.net:8080/Cloudfeeds";

	/********************************************************/
	
	public static final String SOUNDCLOUD_URL = "http://api.soundcloud.com";
	public static final String PATH_SEPARATOR = "/";
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	public static final String ASK = "?";
	public static final String AND = "&";

	public static final String RESOURCE_FOLLOWINGS = "followings";
	public static final String RESOURCE_FOLLOWERS = "followers";
	public static final String RESOURCE_GROUPS = "groups";
	public static final String RESOURCE_COMMENTS = "comments";
	public static final String RESOURCE_FAVORITES = "favorites";
	public static final String RESOURCE_TRACKS = "tracks";
	public static final String RESOURCE_USERS = "users";
	public static final String RESOURCE_MODERATORS = "moderators";
	public static final String RESOURCE_MEMBERS = "members";
	public static final String RESOURCE_CONTRIBUTORS = "contributors";

	public enum FeedFormat
	{
		RSS,UNDEFINED
	}
	
	
	public enum ResourceType {
		TRACK(Track.class), USER(User.class), GROUP(Group.class), FOLLOWINGS(
				Users.class), FOLLOWERS(Users.class), GROUPS(Groups.class), COMMENTS(
				Comments.class), FAVORITES(Tracks.class), TRACKS(Tracks.class), USERS(
				Users.class), MODERATORS(Users.class), MEMBERS(Users.class), CONTRIBUTORS(
				Users.class), UNDEFINED(Object.class);

		private Class modelClass;

		private String viewName;

		private ResourceType(Class modelClass) {
			this.modelClass = modelClass;
			this.viewName = modelClass.getSimpleName().toLowerCase();
		}

		public Class getModelClass() {
			return modelClass;
		}

		public String getViewName() {
			return viewName;
			
		}
		
		public boolean isFormatAllowed(String format)
		{
			format = format.toUpperCase();
			if("RSS".equals(format))
			{
				return true;
			}
			/*
			else if("PODCAST".equals(format) && modelClass.equals(Tracks.class))
			{
				return true;
			}
			*/
			else
			{
				return false;
			}
		}

	}

	public static final int FETCH_DEADLINE = 17000;

	public static final String MODEL_PACKAGE = "net.cloudfeeds.model";

	public final static ResourceType[] ALLOWED_USERS_RESOURCES = new ResourceType[] {
			ResourceType.FOLLOWINGS, ResourceType.FOLLOWERS,
			ResourceType.GROUPS, ResourceType.COMMENTS, ResourceType.FAVORITES,
			ResourceType.TRACKS };

	public final static ResourceType[] ALLOWED_GROUPS_RESOURCES = new ResourceType[] {
			ResourceType.USERS, ResourceType.MODERATORS, ResourceType.MEMBERS,
			ResourceType.CONTRIBUTORS, ResourceType.TRACKS };

	public final static MessageFormat ERROR_FORMAT_FORMAT = new MessageFormat(
			"Sorry, but the feed-format you specified is not available: {0}"
					+ LINE_SEPARATOR + "Currently, only ["
					+ AbstractRssView.FORMAT + "] is supported!");

	public final static MessageFormat ERROR_RESOURCE_FORMAT = new MessageFormat(
			"Sorry, but the resource you specified is not available: {0}");

	public final static MessageFormat ERROR_USER_FORMAT = new MessageFormat(
			"Sorry, but the user you specified was not found: {0}");

	public final static MessageFormat ERROR_GROUP_FORMAT = new MessageFormat(
			"Sorry, but the group you specified was not found: {0}");

	public final static MessageFormat ERROR_TRACK_FORMAT = new MessageFormat(
			"Sorry, but the track you specified was not found: {0}");

	public final static MessageFormat ERROR_FORMAT_FORMAT_HTML = new MessageFormat(
			"Sorry, but the feed-format you specified is not available: <span class=\"format\">{0}</span>"
					+ "<br/>"
					+ "Currently, only <span class=\"formats\">"
					+ AbstractRssView.FORMAT + "</span> is supported!");

	public final static MessageFormat ERROR_RESOURCE_FORMAT_HTML = new MessageFormat(
			"Sorry, but the resource you specified is not available: <span class=\"resource\">{0}</span>");

	public final static MessageFormat ERROR_USER_FORMAT_HTML = new MessageFormat(
			"Sorry, but the user you specified was not found: <span class=\"user\">{0}</span>");

	public final static MessageFormat ERROR_GROUP_FORMAT_HTML = new MessageFormat(
			"Sorry, but the group you specified was not found: <span class=\"group\">{0}</span>");

	public final static MessageFormat ERROR_TRACK_FORMAT_HTML = new MessageFormat(
			"Sorry, but the track you specified was not found: <span class=\"track\">{0}</span>");

	public final static String ERROR_DEADLINE = "Sorry, but " + SOUNDCLOUD_URL
			+ " was not available." + LINE_SEPARATOR
			+ "Please try again later!";


	private boolean useJson = false;

	private Unmarshaller unmarshaller;



	private final Log log = LogFactory.getLog(this.getClass());

	private final MessageFormat userFormat = new MessageFormat(SOUNDCLOUD_URL
			+ PATH_SEPARATOR + RESOURCE_USERS + PATH_SEPARATOR + "{0}"
			+ PATH_SEPARATOR);

	private final Format userResourceFormat = new MessageFormat(SOUNDCLOUD_URL
			+ PATH_SEPARATOR + RESOURCE_USERS + PATH_SEPARATOR + "{0}"
			+ PATH_SEPARATOR + "{1}" + PATH_SEPARATOR + "{2}");

	private final MessageFormat groupFormat = new MessageFormat(SOUNDCLOUD_URL
			+ PATH_SEPARATOR + RESOURCE_GROUPS + PATH_SEPARATOR + "{0}"
			+ PATH_SEPARATOR);

	private final Format groupResourceFormat = new MessageFormat(SOUNDCLOUD_URL
			+ PATH_SEPARATOR + RESOURCE_GROUPS + PATH_SEPARATOR + "{0}"
			+ PATH_SEPARATOR + "{1}" + PATH_SEPARATOR + "{2}");

	private final MessageFormat tracksFormat = new MessageFormat(SOUNDCLOUD_URL
			+ PATH_SEPARATOR + RESOURCE_TRACKS + "{0}");

	private final MessageFormat trackFormat = new MessageFormat(SOUNDCLOUD_URL
			+ PATH_SEPARATOR + RESOURCE_TRACKS + PATH_SEPARATOR + "{0}");

	private final MessageFormat trackCommentsFormat = new MessageFormat(
			SOUNDCLOUD_URL + PATH_SEPARATOR + RESOURCE_TRACKS + PATH_SEPARATOR
					+ "{0}" + PATH_SEPARATOR + RESOURCE_COMMENTS
					+ PATH_SEPARATOR + "{1}");

	public APIService() {

		/*
		 * mapper = new ObjectMapper(); AnnotationIntrospector secondary = new
		 * JacksonAnnotationIntrospector(); AnnotationIntrospector primary = new
		 * JaxbAnnotationIntrospector(); AnnotationIntrospector pair = new
		 * AnnotationIntrospector.Pair(primary, secondary); // make deserializer
		 * use JAXB annotations (only)
		 * mapper.getDeserializationConfig().setAnnotationIntrospector( pair);
		 * // make serializer use JAXB annotations (only)
		 * mapper.getDeserializationConfig().set(
		 * 
		 * DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false
		 * 
		 * );
		 */
	}

	/*
	 * public Document getXML(String url) throws MalformedURLException,
	 * IOException, ParserConfigurationException, SAXException {
	 * 
	 * DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	 * DocumentBuilder db = dbf.newDocumentBuilder(); return db.parse(new
	 * ByteArrayInputStream(fetchURL(url).getContent())); }
	 */
	public HttpResponse fetchURL(String url) throws ClientProtocolException, IOException{
		HttpClient fetch = new DefaultHttpClient();
		HttpParams params = fetch.getParams();
		HttpConnectionParams.setSoTimeout(params, FETCH_DEADLINE);

		HttpResponse fetchResponse;

			
			
			if(!url.contains(this.consumerKey()))
			{
				url+=(ASK + this.consumerKey());
			}
			
			log.info("Fetching URL: " + url);

			HttpGet request = new HttpGet(url);

			/*
			 * if (useJson) { request.addHeader(new HTTPHeader("Accept",
			 * "application/json")); }
			 */
			long start = System.currentTimeMillis();
			fetchResponse = null;
			try {
				fetchResponse = fetch.execute(request);
			} catch (java.net.SocketTimeoutException e) {
				throw new IOException(ERROR_DEADLINE);
			}
			log.info("Fetching DONE in " + (System.currentTimeMillis() - start)
					+ " ms: " + fetchResponse.getStatusLine().getStatusCode());

			int code = fetchResponse.getStatusLine().getStatusCode();
			if(code != 404 && code != 200)
			{
				throw new IOException(ERROR_DEADLINE);
			}

		return fetchResponse;
	}

	/*
	 * private Object unmarshallJson(ResourceType type, byte[] content) throws
	 * JsonParseException, JsonMappingException, IOException { Object o = null;
	 * if (type.getModelClass().equals(Users.class)) { Users users = new
	 * Users(); List<User> list = mapper.readValue( new
	 * ByteArrayInputStream(content), new TypeReference<List<User>>() { });
	 * users.setUserList(list); o = users; } else if
	 * (type.getModelClass().equals(Groups.class)) { Groups users = new
	 * Groups(); List<Group> list = mapper.readValue(new ByteArrayInputStream(
	 * content), new TypeReference<List<Group>>() { }); } else if
	 * (type.getModelClass().equals(Comments.class)) { Comments comments = new
	 * Comments(); List<Comment> list = mapper.readValue(new
	 * ByteArrayInputStream( content), new TypeReference<List<Comment>>() { });
	 * comments.setCommentList(list); o = comments; } else if
	 * (type.getModelClass().equals(Tracks.class)) { Tracks tracks = new
	 * Tracks(); List<Track> list = mapper.readValue(new ByteArrayInputStream(
	 * content), new TypeReference<List<Track>>() { });
	 * tracks.setTrackList(list); o = tracks; } else { o = mapper.readValue(new
	 * ByteArrayInputStream(content), type .getModelClass()); }
	 * 
	 * return o; }
	 */
	public Object unmarshallAPIResponse(HttpResponse response,
			ResourceType resourceType) throws JAXBException, IOException {
		log.info("Unmarshalling Response");
		JAXBContext jc;
			jc = JAXBContext.newInstance(resourceType.getModelClass());
			unmarshaller = jc.createUnmarshaller();
		long start = System.nanoTime();
		Object o = null;

		o = unmarshaller.unmarshal(response.getEntity().getContent());
		response.getEntity().consumeContent();
		log
				.info("Unmarshalling DONE in " + (System.nanoTime() - start)
						+ " ns");

		return o;
	}

	public HttpResponse fetchUser(String username)
			throws MalformedURLException, IOException, HtmlMessageException {
		HttpResponse fetchResponse =  fetchURL(userFormat.format(new Object[] { username }));
		if (fetchResponse.getStatusLine().getStatusCode() != 200) {
			Object[] input = new Object[] { username };
			throw new HtmlMessageException(ERROR_USER_FORMAT.format(input),
					ERROR_USER_FORMAT_HTML.format(input));
		}
		return fetchResponse;
	}

	public HttpResponse fetchUserResource(String username, String resource,
			String query) throws MalformedURLException, IOException,
			HtmlMessageException {
		HttpResponse fetchResponse = fetchURL(userResourceFormat
				.format(new Object[] { username, resource, buildQuery(query) }));
		if (fetchResponse.getStatusLine().getStatusCode() != 200) {
			Object[] input = new Object[] { username };
			throw new HtmlMessageException(ERROR_USER_FORMAT.format(input),
					ERROR_USER_FORMAT_HTML.format(input));
		}
		return fetchResponse;
	}

	public HttpResponse fetchGroup(String groupname)
			throws MalformedURLException, IOException, HtmlMessageException {
		HttpResponse fetchResponse = fetchURL(groupFormat
				.format(new Object[] { groupname }));
		if (fetchResponse.getStatusLine().getStatusCode() != 200) {
			Object[] input = new Object[] { groupname };
			throw new HtmlMessageException(ERROR_GROUP_FORMAT.format(input),
					ERROR_GROUP_FORMAT_HTML.format(input));
		}
		return fetchResponse;
	}

	public HttpResponse fetchGroupResource(String groupname, String resource,
			String query) throws MalformedURLException, IOException,
			HtmlMessageException {

		HttpResponse fetchResponse = fetchURL(groupResourceFormat
				.format(new Object[] { groupname, resource, buildQuery(query) }));
		if (fetchResponse.getStatusLine().getStatusCode() != 200) {
			Object[] input = new Object[] { groupname };
			throw new HtmlMessageException(ERROR_GROUP_FORMAT.format(input),
					ERROR_GROUP_FORMAT_HTML.format(input));
		}
		return fetchResponse;
	}

	public HttpResponse fetchTracks(String query) throws MalformedURLException,
			IOException {
		return fetchURL(tracksFormat.format(new Object[] { buildQuery(query) }));
	}

	public HttpResponse fetchTrack(String track) throws MalformedURLException,
			IOException {
		return fetchURL(trackFormat.format(new Object[] { track }));
	}

	public HttpResponse fetchTrackComments(String track, String query)
			throws MalformedURLException, IOException, HtmlMessageException {

		HttpResponse fetchResponse = fetchURL(trackCommentsFormat
				.format(new Object[] { track, buildQuery(query) }));
		if (fetchResponse.getStatusLine().getStatusCode() != 200) {
			Object[] input = new Object[] { track };
			throw new HtmlMessageException(ERROR_TRACK_FORMAT.format(input),
					ERROR_TRACK_FORMAT_HTML.format(input));
		}
		return fetchResponse;
	}

	private String buildQuery(String query) {
		String ck = ASK + this.consumerKey();
		return (query == null) ? ck : ck + AND + query;
	}
	
	private String consumerKey()
	{
		return "consumer_key=" + CONSUMER_KEY;
	}

	public boolean isUseJson() {
		return useJson;
	}

}
