package net.cloudfeeds.helper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import net.cloudfeeds.feedback.Feedback;
import net.cloudfeeds.model.Track;
import net.cloudfeeds.model.Tracks;
import net.cloudfeeds.service.APIService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.sun.syndication.feed.rss.Enclosure;

public class CloudfeedsHelper {

	private static final Log log = LogFactory.getLog(CloudfeedsHelper.class);


	private static final int DEADLINE = 20000;

	public static String getUrl(HttpServletRequest req) {
		String reqUrl = req.getRequestURL().toString();
		String queryString = req.getQueryString(); // d=789
		if (queryString != null) {
			reqUrl += "?" + queryString;
		}
		return reqUrl;
	}

	public static String getUrl2(HttpServletRequest req) {
		String reqUri = req.getServletPath();
		String queryString = req.getQueryString();
		if (queryString != null) {
			reqUri += "?" + queryString;
		}
		return reqUri;
	}
	
	public static String getUri(HttpServletRequest req) {
		String reqUri = req.getServletPath().toString();
	    String queryString = req.getQueryString();   // d=789
	    if (queryString != null) {
	        reqUri += "?"+queryString;
	    }
	    return reqUri;

	}

	private static Enclosure getEnclosure(Track track,HttpClient client)
			throws ClientProtocolException, IOException {

		Enclosure e = new Enclosure();
		e.setUrl(track.getDownloadUrl());

		HttpGet httpget = new HttpGet(track.getDownloadUrl());

		long start = System.currentTimeMillis();
		HttpResponse response = client.execute(httpget);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			e.setLength(entity.getContentLength());
			e.setType(entity.getContentType().getValue());
		}

		httpget.abort();
		log.info("Enclosure fetched from " + httpget.getURI() + " in "
				+ (System.currentTimeMillis() - start) + " ms");

		return e;

	}

	public static Map<BigInteger, Enclosure> getEnclosures(Tracks tracks)
			throws ClientProtocolException, IOException {
		List<Track> trackList = tracks.getTrackList();
		Map<BigInteger, Enclosure> enclosures = new HashMap<BigInteger, Enclosure>();
		HttpClient client = new DefaultHttpClient();
		for (Track t : trackList) {
			if (t.isDownloadable() && !enclosures.containsKey(t.getId())) {

				enclosures.put(t.getId(), getEnclosure(t,client));
			}
		}
//		client.getConnectionManager().shutdown();
		return enclosures;
	}

	public static void sendFeedback(Feedback feedback) {
		Properties props = new Properties();
		props.put("mail.smtp.host", APIService.SMTP_HOST);

		Authenticator auth = new SimpleAuthenticator(APIService.SMTP_USER,
				APIService.SMTP_PASSWORD);
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(false);

		// create a message
		Message msg = new MimeMessage(session);

		try {
			InternetAddress addressFrom = new InternetAddress(feedback
					.getEmail(), feedback.getName());
			msg.setFrom(addressFrom);

			msg.setRecipient(RecipientType.TO, new InternetAddress(
					APIService.APP_FEEDBACK_EMAIL, APIService.APP_AUTHOR_NAME));

			// Setting the Subject and Content Type
			msg.setSubject("Cloudfeeds-Feedback | " + feedback.getTitle());
			msg.setContent(feedback.getFeedback(), "text/plain");
			Transport.send(msg);
		} catch (Exception e) {
			log.error("Sending Feedback failed!", e);
		}
	}
	
	public static String encodeURL(String url)
	{
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

	private static class SimpleAuthenticator extends Authenticator {
		private final String username;
		private final String password;

		public SimpleAuthenticator(String username, String password) {
			super();
			this.username = username;
			this.password = password;
		}

		@Override
		public PasswordAuthentication getPasswordAuthentication() {

			return new PasswordAuthentication(username, password);
		}
	}

}
