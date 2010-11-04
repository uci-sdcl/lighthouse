package edu.uci.lighthouse.views.filters;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ViewerFilter;

import edu.uci.lighthouse.model.io.IPersistable;
import edu.uci.lighthouse.ui.views.FilterManager;


public class BookmarkRegistry implements IPersistable{

	private static final long serialVersionUID = -6865779870365515667L;
	
	private static final String filename = "filterBookmark.bin";
	
	private Map<String, FilterBookmark> bookmarkList = new HashMap<String, FilterBookmark>();


	public void addBookmark(String name){
		bookmarkList.put(name, new FilterBookmark(name, getActiveFilters()));
	}
	
	public void removeBookmark(String name){
		bookmarkList.remove(name);
	}
	
	private Collection<IFilter> getActiveFilters() {
		Collection<IFilter> filters = new LinkedList<IFilter>();
		FilterManager filterManager = FilterManager.getInstance();
		for (IClassFilter filter : filterManager.getClassFilters()) {
			filters.add(filter);
		}
		for (ViewerFilter filter : filterManager.getViewerFilters()) {
			if (filter instanceof IFilter) {
				filters.add((IFilter) filter);
			}
		}
		return filters;
	}
	
	public Collection<FilterBookmark> getBookmarkedFilters() {
		return bookmarkList.values();
	}
	
	@Override
	public String getFileName() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toPortableString()  + "/.metadata/" + filename;
	}
}
