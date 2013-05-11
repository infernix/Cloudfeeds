package net.cloudfeeds.view;

import com.sun.syndication.feed.module.Module;

public interface AtomNSModule extends Module {
    public static final String URI = "http://www.w3.org/2005/Atom";
    String getLink();
    void setLink(String href);
}

