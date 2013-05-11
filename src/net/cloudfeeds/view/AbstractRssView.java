package net.cloudfeeds.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.cloudfeeds.service.APIService;

import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.sun.syndication.feed.module.Module;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Image;

public abstract class AbstractRssView extends AbstractRssFeedView {

	public final static String FORMAT = "rss";
	

	
	private final AtomNSModule atomNSModule = new AtomNSModuleImpl();
	
	private static final String generator = APIService.APP_NAME + " " + APIService.APP_VERSION;
	
	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Channel feed,
			HttpServletRequest request) {


		feed.setGenerator(generator);
		atomNSModule.setLink(model.get("atomlink").toString());
		feed.getModules().add(atomNSModule);
		feed.setTitle(model.get("channel.title").toString());
		feed.setLink(model.get("channel.link").toString());
		if(model.containsKey("channel.image"))
			feed.setImage((Image)model.get("channel.image"));	
		feed.setDescription(model.get("channel.description").toString());
		super.buildFeedMetadata(model, feed, request);
	}

}
