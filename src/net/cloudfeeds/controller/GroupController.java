package net.cloudfeeds.controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.cloudfeeds.exception.HtmlMessageException;
import net.cloudfeeds.helper.CloudfeedsHelper;
import net.cloudfeeds.model.Group;
import net.cloudfeeds.model.Tracks;
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
@RequestMapping(value = "/groups/*")
public class GroupController {


	private APIService service = new APIService();

	private Set<ResourceType> allowedGroupResources = new HashSet<ResourceType>();

//	private Map<String, String> associatedGroupViews = new HashMap<String, String>();

	private GroupController() {
		for (ResourceType s : APIService.ALLOWED_GROUPS_RESOURCES) {
			allowedGroupResources.add(s);

		}
		// allowedFormats.add(AbstractAtomView.FORMAT);
	}

	private void setGroupChannelRSS(String group_id, ModelMap model,
			String title, String resource) throws Exception {
		Group group = (Group) service.unmarshallAPIResponse(service
				.fetchGroup(group_id),ResourceType.GROUP);
		String imageTitle = null;
		String imageLink = null;
		
		model.put("channel.title",imageTitle = group.getName() + " | " + title);
		model.put("channel.link", imageLink = group.getPermalinkUrl()
				+ APIService.PATH_SEPARATOR + resource);

		if (!group.getArtworkUrl().equals("")) {
			Image image = new Image();
			image.setTitle(imageTitle);
			image.setUrl(group.getArtworkUrl());
			image.setLink(imageLink);
			model.put("channel.image", image);
		}
		model.put("channel.description", (group.getDescription().equals(""))? resource.toUpperCase(): group.getDescription());

	}

	private String getGroupResource(String group_id, String resource, String format,
			ModelMap model, HttpServletRequest request) throws Exception {
		ResourceType type;
		try {
			type = ResourceType.valueOf(resource.toUpperCase());
		} catch (Exception e) {
			type = ResourceType.UNDEFINED;
		}
		
		if (allowedGroupResources.contains(type)) {
			if (type.isFormatAllowed(format)) {
				model.put("atomlink", APIService.HOME_URL + CloudfeedsHelper.getUrl2(request));
				if(type == ResourceType.TRACKS)
				{
					Tracks tracks = null;
					model.put(type.getViewName(), tracks = (Tracks)service
							.unmarshallAPIResponse(service.fetchGroupResource(
									group_id, resource, request.getQueryString()),type));
					if(format.toUpperCase().equals("PODCAST"))
						model.put("enclosures", CloudfeedsHelper.getEnclosures(tracks));
				}
				else
				{
				model.put(type.getViewName(), service
						.unmarshallAPIResponse(service.fetchGroupResource(
								group_id, resource, request.getQueryString()),type));
				}
				setGroupChannelRSS(group_id, model, resource.toUpperCase()
						+ " in the Soundcloud", resource);
				return type.getViewName() + "." + format;
			} else {
				Object [] input = new Object[]{format};
				throw new HtmlMessageException(
						APIService.ERROR_FORMAT_FORMAT
								.format(input),APIService.ERROR_FORMAT_FORMAT_HTML
								.format(input));
			}
		} else {
			Object [] input = new Object[]{"groups/" + resource};
			throw new HtmlMessageException(APIService.ERROR_RESOURCE_FORMAT
					.format(input),APIService.ERROR_RESOURCE_FORMAT_HTML
					.format(input));
		}

	}
	
	
	@RequestMapping(value = { "/{group_id}/{resource}/{format}" }, method = RequestMethod.GET)
	public String getGroupsRequest(@PathVariable String group_id,
			@PathVariable String resource, @PathVariable String format,
			ModelMap model, HttpServletRequest request) throws Exception {
		return getGroupResource(group_id, resource, format, model, request);
	}

	@RequestMapping(value = { "/{group_id}/{resource}" }, method = RequestMethod.GET)
	public String getGroupsRequest(@PathVariable String group_id,
			@PathVariable String resource, ModelMap model,
			HttpServletRequest request) throws Exception {
		return getGroupResource(group_id, resource, AbstractRssView.FORMAT, model,
				request);
	}
}
