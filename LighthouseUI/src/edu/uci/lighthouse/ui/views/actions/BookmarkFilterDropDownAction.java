package edu.uci.lighthouse.ui.views.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.IContainer;

import edu.uci.lighthouse.services.LighthouseServiceFactory;
import edu.uci.lighthouse.services.persistence.IPersistenceService;
import edu.uci.lighthouse.ui.LighthouseUIPlugin;
import edu.uci.lighthouse.ui.views.FilterManager;
import edu.uci.lighthouse.views.filters.BookmarkRegistry;
import edu.uci.lighthouse.views.filters.FilterBookmark;
import edu.uci.lighthouse.views.filters.IClassFilter;
import edu.uci.lighthouse.views.filters.IFilter;

public class BookmarkFilterDropDownAction extends Action implements IMenuCreator {

	private Menu menu;
	protected IContainer container;
	protected IAction selectedAction;
	
	private static final String ICON = "/icons/console.gif";
	private static final String DESCRIPTION = "Bookmarked filters";
	
	private BookmarkRegistry bookmarkRegistry = new BookmarkRegistry();
	
	private Map<String,BookmarkFilterAction> cachedActions = new HashMap<String,BookmarkFilterAction>();
	
	private static Logger logger = Logger.getLogger(BookmarkFilterDropDownAction.class);
	
	public BookmarkFilterDropDownAction(IContainer container) {
		super(null,Action.AS_DROP_DOWN_MENU);
		this.container = container;
		init();
		setMenuCreator(this);
	}

	@Override
	public void run() {
		InputDialog dlg = new InputDialog(
				Display.getCurrent().getActiveShell(), "Filter Bookmark",
				"Enter the bookmark name:", "", null);
		if (dlg.open() == Window.OK) {
			bookmarkRegistry.addBookmark(dlg.getValue());
			saveBookmarkRegistry();
		}

	}

	protected void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(LighthouseUIPlugin.PLUGIN_ID, ICON));
		loadBookmarkRegistry();
	}

	private void saveBookmarkRegistry() {
		IPersistenceService svc = (IPersistenceService) LighthouseServiceFactory.getService("GenericPersistenceService");
		try {
			svc.save(bookmarkRegistry);
		} catch (Exception e) {
			logger.error(e, e);
		}		
	}
	
	private void loadBookmarkRegistry() {
		IPersistenceService svc = (IPersistenceService) LighthouseServiceFactory.getService("GenericPersistenceService");
		try {
			bookmarkRegistry = (BookmarkRegistry) svc.load(bookmarkRegistry);
		} catch (Exception e) {
			logger.error(e, e);
		}		
	}

	protected List<IAction> createActions() {
		List<IAction> result = new ArrayList<IAction>();
		for (FilterBookmark bookmark : bookmarkRegistry.getBookmarkedFilters()) {
			BookmarkFilterAction action = cachedActions.get(bookmark.getName());
			if (action == null) {
				action = new BookmarkFilterAction(bookmark);
				cachedActions.put(bookmark.getName(), action);
			}
			result.add(action);
		}
		return result;
	}

	private final class BookmarkFilterAction extends Action {
		private FilterBookmark bookmark;
		private Collection<Object> filterInstances = new LinkedList<Object>();
		public BookmarkFilterAction(FilterBookmark bookmark){
			super(bookmark.getName(),Action.AS_CHECK_BOX);
			this.bookmark = bookmark;
		}
		@Override
		public void run() {
			if (isChecked()) {	
				addFilters();
			} else {
				removeFilters();
			}
		}
		private void addFilters() {
			for (IFilter obj : bookmark.getFilters()) {
				try {
				//Object obj = clazz.newInstance();
				filterInstances.add(obj);
				if (obj instanceof IClassFilter) {
					FilterManager.getInstance().addClassFilter((IClassFilter)obj);
				} else if (obj instanceof ViewerFilter) {
					FilterManager.getInstance().addViewerFilter((ViewerFilter)obj);
				}
				} catch (Exception e) {
					logger.error(e,e);
				}
			}
		}
		private void removeFilters(){
			for (Object obj : filterInstances) {
				if (obj instanceof IClassFilter) {
					FilterManager.getInstance().removeClassFilter((IClassFilter)obj);
				} else if (obj instanceof ViewerFilter) {
					FilterManager.getInstance().removeViewerFilter((ViewerFilter)obj);
				}
			}
			filterInstances.clear();
		}
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public Menu getMenu(Control parent) {
		if (menu != null){
			menu.dispose();
		}
		
//		if (menu == null) {
			menu = new Menu(parent);
			logger.debug("menu instance created.");
			for (IAction layoutAction : createActions()) {
				ActionContributionItem item = new ActionContributionItem(
						layoutAction);
				item.fill(menu, -1);
			}
//		}
		return menu;
	}

	@Override
	public Menu getMenu(Menu parent) {
		// TODO Auto-generated method stub
		return null;
	}
}
