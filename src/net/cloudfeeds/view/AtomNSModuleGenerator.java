package net.cloudfeeds.view;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jdom.Element;
import org.jdom.Namespace;

import com.sun.syndication.feed.module.Module;
import com.sun.syndication.io.ModuleGenerator;

public class AtomNSModuleGenerator implements ModuleGenerator {
    private static final Namespace ATOM_NS = Namespace.getNamespace("atom", AtomNSModule.URI);

    private static final Set NAMESPACES;

    static {
        Set nss = new HashSet();
        nss.add(ATOM_NS);
        NAMESPACES = Collections.unmodifiableSet(nss);
    }

    public String getNamespaceUri() {
        return AtomNSModule.URI;
    }

    public Set getNamespaces() {
        return NAMESPACES;
    }

    public void generate(Module module, Element element) {
        AtomNSModule atomNSModule = (AtomNSModule) module;
        Element root = element;
        while (root.getParent() != null && root.getParent() instanceof Element) {
            root = (Element) element.getParent();
        }
        root.addNamespaceDeclaration(ATOM_NS);

        Element atomLink = new Element("link", ATOM_NS);
        atomLink.setAttribute("href", atomNSModule.getLink());
        atomLink.setAttribute("rel", "self");
        atomLink.setAttribute("type", "application/rss+xml");

        element.addContent(0, atomLink);
    }
}
