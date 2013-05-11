package net.cloudfeeds.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cloudfeeds.model.Comment;
import net.cloudfeeds.model.Comments;
import net.cloudfeeds.service.APIService;

import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Guid;
import com.sun.syndication.feed.rss.Item;

public class SingleTrackCommentsRssView extends AbstractRssView {

	@Override
	protected List<Item> buildFeedItems(Map<String, Object> arg0,
			HttpServletRequest arg1, HttpServletResponse arg2) throws Exception {
		List <Comment> comments = ((Comments)arg0.get("comments.single")).getCommentList();
//		Track track = (Track)arg0.get("track");

		List <Item> items = new ArrayList<Item>(comments.size());
		String link = arg0.get("channel.link").toString();
		
		for(Comment comment : comments)
		{
			Item item = new Item();
			item.setTitle(comment.getUser().getUsername() + " commented:");
			Description description = new Description();
			description.setValue(comment.getBody());
			
			Content content = new Content();
			content.setType("encoded");
			content.setValue(comment.getBody());
			item.setContent(content);
			
			item.setDescription(description);
			item.setLink(link = link + APIService.PATH_SEPARATOR + "comment-" + comment.getId());

			Guid guid = new Guid();
			guid.setPermaLink(true);
			guid.setValue(link);
			item.setGuid(guid);
			item.setPubDate(comment.getCreatedAt().toGregorianCalendar().getTime());


			items.add(item);
		}
		arg2.setContentType("text/xml; charset=utf-8");
		return items;
	}


	


}
