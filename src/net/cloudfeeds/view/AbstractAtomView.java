package net.cloudfeeds.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;

import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.rss.Image;

public abstract class AbstractAtomView extends AbstractAtomFeedView {

	public final static String FORMAT = "atom";
	
	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Feed feed,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		feed.setTitle(model.get("channel.title").toString());

		super.buildFeedMetadata(model, feed, request);
	}

}
