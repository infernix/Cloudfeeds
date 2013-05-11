package net.cloudfeeds.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cloudfeeds.model.Track;
import net.cloudfeeds.model.User;
import net.cloudfeeds.model.Users;

import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Guid;
import com.sun.syndication.feed.rss.Image;
import com.sun.syndication.feed.rss.Item;

public class GenericUsersRssView extends AbstractRssView {


	@Override
	protected List<Item> buildFeedItems(Map<String, Object> arg0,
			HttpServletRequest arg1, HttpServletResponse arg2) throws Exception {
		List <User> users = ((Users)arg0.get("users")).getUserList();
		List <Item> items = new ArrayList<Item>(users.size());
		for(User user : users)
		{
			Item item = new Item();
			item.setTitle(user.getUsername());
			Description description = new Description();
			description.setValue(user.getDescription());
			item.setDescription(description);
			
			Content content = new Content();
			content.setType("encoded");
			content.setValue(user.getDescription());
			item.setContent(content);
			
			item.setLink(user.getPermalinkUrl());
			Guid guid = new Guid();
			guid.setPermaLink(true);
			guid.setValue(user.getPermalinkUrl());
			item.setGuid(guid);
			items.add(item);
		}
		arg2.setContentType("text/xml; charset=utf-8");
		return items;
	}
	

}
