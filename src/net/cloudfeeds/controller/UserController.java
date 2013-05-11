package net.cloudfeeds.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.cloudfeeds.exception.HtmlMessageException;
import net.cloudfeeds.helper.CloudfeedsHelper;
import net.cloudfeeds.model.Comment;
import net.cloudfeeds.model.Comments;
import net.cloudfeeds.model.Tracks;
import net.cloudfeeds.model.User;
import net.cloudfeeds.service.APIService;
import net.cloudfeeds.service.APIService.ResourceType;
import net.cloudfeeds.view.AbstractRssView;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sun.syndication.feed.rss.Image;

@Controller
@RequestMapping(value = "/users/*")
public class UserController {

	private APIService service = new APIService();

	private Set<ResourceType> allowedUsersResources = new HashSet<ResourceType>();

	private UserController() {
		for (ResourceType s : APIService.ALLOWED_USERS_RESOURCES) {
			allowedUsersResources.add(s);

		}

		// allowedFormats.add(AbstractAtomView.FORMAT);
	}

	private void setUserChannelRSS(String user_id, ModelMap model,
			String title, String resource) throws Exception {
		User user = (User) service.unmarshallAPIResponse(service
				.fetchUser(user_id),ResourceType.USER);
		String imageTitle = null;
		String imageLink = null;

		model.put("channel.title", imageTitle = user.getUsername() + " | "
				+ title);
		model.put("channel.link", imageLink = user.getPermalinkUrl()
				+ APIService.PATH_SEPARATOR + resource);

		if (!user.getAvatarUrl().equals("")) {
			Image image = new Image();
			image.setTitle(imageTitle);
			image.setUrl(user.getAvatarUrl());
			image.setLink(imageLink);
			model.put("channel.image", image);
		}
		model.put("channel.description",
				(user.getDescription().equals("")) ? resource.toUpperCase()
						: user.getDescription());
	}

	private void setCommentsTracks(ModelMap model) throws Exception {
		List <Comment> comments = ((Comments)model.get("comments.multi")).getCommentList();

		StringBuilder builder = new StringBuilder();
		String sep = ",";
		for(Comment comment : comments)
		{
			builder.append(comment.getTrackId());
			builder.append(sep);
			
		}
		builder.replace(builder.length()-1, builder.length(), "");

		Object tracks = service.unmarshallAPIResponse(service.fetchTracks("ids=" + builder.toString()),ResourceType.TRACKS);
		model.put("tracks", tracks);
	}

	private String getUsersResource(String user_id, String resource, String format,
			ModelMap model, HttpServletRequest request) throws Exception {
		ResourceType type;
		try {
			type = ResourceType.valueOf(resource.toUpperCase());
		} catch (Exception e) {
			type = ResourceType.UNDEFINED;
		}

		if (allowedUsersResources.contains(type)) {
			if (type.isFormatAllowed(format)) {
				model.put("atomlink", APIService.HOME_URL + CloudfeedsHelper.getUrl2(request));
				setUserChannelRSS(user_id, model, resource.toUpperCase()
						+ " in the Soundcloud", resource);
				model.put("showtrackartist", false);			
				if(type == ResourceType.TRACKS || type == ResourceType.FAVORITES)
				{
					Tracks tracks = null;
					model.put(type.getViewName(), tracks = (Tracks)service
							.unmarshallAPIResponse(service.fetchUserResource(user_id,
									resource, request.getQueryString()),type));
					if(format.toUpperCase().equals("PODCAST"))
						model.put("enclosures", CloudfeedsHelper.getEnclosures(tracks));
					return type.getViewName() + "." + format;
				}
				else if(type == ResourceType.COMMENTS)
				{
				model.put(type.getViewName()+ ".multi", service
						.unmarshallAPIResponse(service.fetchUserResource(user_id,
								resource, request.getQueryString()),type));
					setCommentsTracks(model);
					return type.getViewName() + ".multi." + format;
				}
				else
				{
					model.put(type.getViewName(), service
							.unmarshallAPIResponse(service.fetchUserResource(user_id,
									resource, request.getQueryString()),type));
					return type.getViewName() + "." + format;
				}


			} else {
				Object [] input = new Object[]{format};
				throw new HtmlMessageException(
						APIService.ERROR_FORMAT_FORMAT
								.format(input),APIService.ERROR_FORMAT_FORMAT_HTML
								.format(input));
			}
		} else {
			Object [] input = new Object[]{"users/" + resource};
			throw new HtmlMessageException(APIService.ERROR_RESOURCE_FORMAT
					.format(input),APIService.ERROR_RESOURCE_FORMAT_HTML
					.format(input));
		}

	}
	
	@RequestMapping(value = { "/{user_id}/{resource}/{format}" }, method = RequestMethod.GET)
	public String getUsersRequest(@PathVariable String user_id,
			@PathVariable String resource, @PathVariable String format,
			ModelMap model, HttpServletRequest request) throws Exception {
		return getUsersResource(user_id, resource, format, model, request);
	}

	@RequestMapping(value = { "/{user_id}/{resource}" }, method = RequestMethod.GET)
	public String getUsersRequest(@PathVariable String user_id,
			@PathVariable String resource, ModelMap model,
			HttpServletRequest request) throws Exception {
		return getUsersResource(user_id, resource, AbstractRssView.FORMAT, model,
				request);
	}

}
