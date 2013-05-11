package net.cloudfeeds.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cloudfeeds.model.Group;
import net.cloudfeeds.model.Groups;
import net.cloudfeeds.model.User;

import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Guid;
import com.sun.syndication.feed.rss.Item;

public class GenericGroupsRssView extends AbstractRssView {

	
	@Override
	protected List<Item> buildFeedItems(Map<String, Object> arg0,
			HttpServletRequest arg1, HttpServletResponse arg2) throws Exception {
		List <Group> groups = ((Groups)arg0.get("groups")).getGroupList();
		List <Item> items = new ArrayList<Item>(groups.size());
		for(Group group : groups)
		{
			Item item = new Item();
			item.setTitle(group.getName());
			Description description = new Description();
			description.setValue(group.getDescription());
			item.setDescription(description);
			
			Content content = new Content();
			content.setType("encoded");
			content.setValue(group.getDescription());
			item.setContent(content);
			item.setLink(group.getPermalinkUrl());
			Guid guid = new Guid();
			guid.setPermaLink(true);
			guid.setValue(group.getPermalinkUrl());
			item.setGuid(guid);
			items.add(item);
		}
		arg2.setContentType("text/xml; charset=utf-8");
		return items;
	}
	

}
