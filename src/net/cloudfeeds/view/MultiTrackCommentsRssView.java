package net.cloudfeeds.view;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cloudfeeds.model.Comment;
import net.cloudfeeds.model.Comments;
import net.cloudfeeds.model.Track;
import net.cloudfeeds.model.Tracks;
import net.cloudfeeds.service.APIService;

import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Guid;
import com.sun.syndication.feed.rss.Item;

public class MultiTrackCommentsRssView extends AbstractRssView {

	@Override
	protected List<Item> buildFeedItems(Map<String, Object> arg0,
			HttpServletRequest arg1, HttpServletResponse arg2) throws Exception {
		List <Comment> comments = ((Comments)arg0.get("comments.multi")).getCommentList();
		List <Track> tracks = ((Tracks)arg0.get("tracks")).getTrackList();

		Map <BigInteger,Track> trackMap = new HashMap<BigInteger, Track>();
		for(Track t : tracks)
		{
			trackMap.put(t.getId(), t);
		}
		List <Item> items = new ArrayList<Item>(comments.size());
		int i = 0;
		for(Comment comment : comments)
		{

			Track track = trackMap.get(comment.getTrackId());
			String link = track.getPermalinkUrl() + APIService.PATH_SEPARATOR + "comment-" + comment.getId();
			Item item = new Item();
			item.setTitle(comment.getUser().getUsername() + " commented on " + track.getUser().getUsername() + " - " + track.getTitle() + ":");
			Description description = new Description();
			description.setValue(comment.getBody());
			item.setDescription(description);
			
			Content content = new Content();
			content.setType("encoded");
			content.setValue(comment.getBody());
			item.setContent(content);
			item.setLink(link);
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
