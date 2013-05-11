package net.cloudfeeds.view;

import com.sun.syndication.feed.module.ModuleImpl;

public class AtomNSModuleImpl extends ModuleImpl implements AtomNSModule {
    private String link;

    public AtomNSModuleImpl() {
        super(AtomNSModule.class, URI);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Class getInterface() {
        return AtomNSModule.class;
    }

    public void copyFrom(Object obj) {
        AtomNSModule module = (AtomNSModule) obj;
        module.setLink(this.link);
    }
}
