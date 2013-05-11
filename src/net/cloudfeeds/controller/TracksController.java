package net.cloudfeeds.controller;

import javax.servlet.http.HttpServletRequest;

import net.cloudfeeds.exception.HtmlMessageException;
import net.cloudfeeds.helper.CloudfeedsHelper;
import net.cloudfeeds.model.Track;
import net.cloudfeeds.model.Tracks;
import net.cloudfeeds.service.APIService;
import net.cloudfeeds.service.APIService.ResourceType;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sun.syndication.feed.rss.Image;

@Controller
@RequestMapping(value = "/*")
public class TracksController {

	private APIService service = new APIService();

	private void setTrackChannelRSS(ModelMap model, String trackname,
			String title) throws Exception {
		Track track = (Track) service.unmarshallAPIResponse(service
				.fetchTrack(trackname), ResourceType.TRACK);
		// model.put("track",track);

		String imageTitle = null;
		String imageLink = null;

		model.put("channel.title", imageTitle = track.getUser().getUsername()
				+ " - " + track.getTitle() + " | " + title);
		model.put("channel.link", imageLink = track.getPermalinkUrl());

		if (!track.getArtworkUrl().equals("")) {
			Image image = new Image();
			image.setTitle(imageTitle);
			image.setUrl(track.getArtworkUrl());
			image.setLink(imageLink);
			model.put("channel.image", image);
		}
		model.put("channel.description",
				(track.getDescription().equals("")) ? "COMMENTS" : track
						.getDescription());
	}

	private void setTracksChannelRSS(ModelMap model, String desc)
			throws Exception {
		model.put("channel.title", "TRACKS in the Soundcloud");
		model.put("channel.link", "http://soundcloud.com");

		model.put("channel.description", (desc == null) ? "" : desc);
	}

	@RequestMapping(value = { "/tracks" }, method = RequestMethod.GET)
	public String getTracksRequest(ModelMap model, HttpServletRequest request)
			throws Exception {
		return getTracksRequestFormat(model,request,"rss");
	}

	@RequestMapping(value = { "/tracks/{format}" }, method = RequestMethod.GET)
	public String getTracksRequestFormat(ModelMap model,
			HttpServletRequest request, @PathVariable String format)
			throws Exception {
		if (ResourceType.TRACKS.isFormatAllowed(format)) {
			model.put("atomlink", APIService.HOME_URL
					+ CloudfeedsHelper.getUrl2(request));
			Tracks tracks = null;
			model.put("tracks", tracks = (Tracks) service
					.unmarshallAPIResponse(service.fetchTracks(request
							.getQueryString()), ResourceType.TRACKS));
			if(format.toUpperCase().equals("PODCAST"))
				model.put("enclosures", CloudfeedsHelper.getEnclosures(tracks));
			setTracksChannelRSS(model, request.getQueryString());
			return "tracks.rss";
		} else {
			Object[] input = new Object[] { format };
			throw new HtmlMessageException(APIService.ERROR_FORMAT_FORMAT
					.format(input), APIService.ERROR_FORMAT_FORMAT_HTML
					.format(input));
		}
	}

	@RequestMapping(value = { "/tracks/{track_id}" }, method = RequestMethod.GET)
	public String getCommentsRequest(@PathVariable String track_id,
			ModelMap model, HttpServletRequest request) throws Exception {
		model.put("atomlink", APIService.HOME_URL
				+ CloudfeedsHelper.getUrl2(request));

		model.put("comments.single", service.unmarshallAPIResponse(service
				.fetchTrackComments(track_id, request.getQueryString()),
				ResourceType.COMMENTS));
		setTrackChannelRSS(model, track_id, "comments".toUpperCase()
				+ " in the Soundcloud");
		return "comments.single.rss";
	}

}
