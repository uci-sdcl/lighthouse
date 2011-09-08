package edu.uci.lighthouse.lighthouseqandathreads.actions.extensions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.lighthouseqandathreads.Activator;
import edu.uci.lighthouse.lighthouseqandathreads.filters.ClosedThreadFilter;
import edu.uci.lighthouse.lighthouseqandathreads.filters.OpenThreadFilter;
import edu.uci.lighthouse.lighthouseqandathreads.filters.SolvedThreadFilter;
import edu.uci.lighthouse.ui.views.FilterManager;
import edu.uci.lighthouse.ui.views.actions.FilterModifiedAction;
import edu.uci.lighthouse.ui.views.actions.PluginAction;

public class ForumFilterAction extends PluginAction implements IMenuCreator {

	private static final String DESCRIPTION = "Filter by questions.";

	private Map<String, FilterAction> cachedActions = new HashMap<String, FilterAction>();

	private Menu menu;

	public ForumFilterAction() {
		super(null, Action.AS_DROP_DOWN_MENU);
		init();
		setMenuCreator(this);
	}

	protected void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		"/icons/QA.png"));

	}

	protected List<IAction> createActions() {
		List<IAction> result = new ArrayList<IAction>();
		
		String name1 = "has open threads";
		FilterAction action = cachedActions.get(name1);
		if (action == null) {
			action = new FilterAction(name1, new OpenThreadFilter());
			cachedActions.put(name1, action);
		}
		result.add(action);
		
		String name2 = "has answered threads";
		FilterAction action2 = cachedActions.get(name2);
		if (action2 == null) {
			action2 = new FilterAction(name2, new SolvedThreadFilter());
			cachedActions.put(name2, action2);
		}
		result.add(action2);
		
		
		String name3 = "has closed threads";
		FilterAction action3 = cachedActions.get(name3);
		if (action3 == null) {
			action3 = new FilterAction(name3, new ClosedThreadFilter());
			cachedActions.put(name3, action3);
		}
		result.add(action3);

		return result;
	}

	private class FilterAction extends Action {
		ViewerFilter filter;
		public FilterAction(String name, ViewerFilter filter) {
			super(name, Action.AS_CHECK_BOX);
			this.filter = filter;
		}

		public void run() {
			
			if (isChecked()) {
			
			} else {
				
			}
		}
	}

	public void dispose() {
		if (menu != null) {
			menu.dispose();
			menu = null;
		}
	}

	public Menu getMenu(Control parent) {
		if (menu != null) {
			menu.dispose();
		}

		menu = new Menu(parent);
		for (IAction action : createActions()) {
			ActionContributionItem item = new ActionContributionItem(
					action);
			item.fill(menu, -1);
		}

		return menu;
	}

	public Menu getMenu(Menu parent) {
		return null;
	}

}
