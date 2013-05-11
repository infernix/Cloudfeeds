package net.cloudfeeds.view;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cloudfeeds.helper.CloudfeedsHelper;
import net.cloudfeeds.model.Track;
import net.cloudfeeds.model.Tracks;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jdom.CDATA;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Enclosure;
import com.sun.syndication.feed.rss.Guid;
import com.sun.syndication.feed.rss.Item;
import com.sun.syndication.feed.module.mediarss.MediaEntryModuleImpl;
import com.sun.syndication.feed.module.mediarss.io.*;
import com.sun.syndication.feed.module.mediarss.types.MediaContent;
import com.sun.syndication.feed.module.mediarss.types.UrlReference;


public class GenericTracksRssView extends AbstractRssView {

    private final Template template;
	

    @Autowired
    public GenericTracksRssView(VelocityEngine engine) throws Exception {
    	/*engine = new VelocityEngine();
    	engine.init();*/
    	template = engine.getTemplate("player.vm");

    	
    }
    

	@Override
	protected List<Item> buildFeedItems(Map<String, Object> arg0,
			HttpServletRequest arg1, HttpServletResponse arg2) throws Exception {
		List <Track> tracks = ((Tracks)arg0.get("tracks")).getTrackList();
		List <Item> items = new ArrayList<Item>(tracks.size());
		boolean podcast = arg0.containsKey("enclosures");
		Map <BigInteger,Enclosure> enclosures = null;
		if(podcast)
			enclosures = (Map <BigInteger,Enclosure>)arg0.get("enclosures");
		
		boolean showArtist = true;
		if(arg0.containsKey("showtrackartist"))
		{
			showArtist = (Boolean) arg0.get("showtrackartist");
		}
		for(Track track : tracks)
		{
			Item item = new Item();
			MediaContent[] mediacontents = new MediaContent[1];
			MediaContent mediaitem = new MediaContent( new UrlReference(track.getPermalinkUrl()) );
			mediaitem.setType("audio/mpeg");
			mediacontents[0] = mediaitem;

			String prepend = (showArtist)? track.getUser().getUsername() + " - " : "";
			item.setTitle(prepend + track.getTitle());
			Description description = new Description();
			description.setValue("");
			// item.setDescription(description);
			
			Content content = new Content();
			content.setType("encoded");
			
		    VelocityContext context = new VelocityContext();
		    context.put("url", track.getPermalinkUrl());
		    context.put("apiurl", CloudfeedsHelper.encodeURL("http://api.soundcloud.com/tracks/" + track.getId()));
		    StringWriter writer = new StringWriter();
		    template.merge( context, writer );
		    
			content.setValue(writer.toString() + "<br/>" + track.getDescription());
			item.setContent(content);
			item.setLink(track.getPermalinkUrl());
			
			if(podcast && enclosures.containsKey(track.getId()))
			{
				item.getEnclosures().add(enclosures.get(track.getId()));
			}
			
			Guid guid = new Guid();
			guid.setPermaLink(true);
			guid.setValue(track.getPermalinkUrl());
			item.setGuid(guid);
			item.setPubDate(track.getCreatedAt().toGregorianCalendar().getTime());
			if(track.isDownloadable()) {
				MediaEntryModuleImpl module = new MediaEntryModuleImpl();
				module.setMediaContents( mediacontents );
				item.getModules().add( module );
			}
			items.add(item);
		}
		arg2.setContentType("text/xml; charset=utf-8");
		return items;
	}
	


}
