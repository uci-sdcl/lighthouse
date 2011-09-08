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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.ui.views.FilterManager;
import edu.uci.lighthouse.ui.views.actions.FilterModifiedAction;
import edu.uci.lighthouse.ui.views.actions.PluginAction;

public class ForumFilterAction extends PluginAction implements IMenuCreator {

	private static final String DESCRIPTION = "Filter by open questions.";

	private Map<String, FilterAction> cachedActions = new HashMap<String, FilterAction>();

	private Menu menu;

	ForumFilterAction() {
		super(null, Action.AS_DROP_DOWN_MENU);
		init();
		setMenuCreator(this);
	}

	protected void init() {
		setToolTipText(DESCRIPTION);

	}

	protected List<IAction> createActions() {
		List<IAction> result = new ArrayList<IAction>();
		
		String name1 = "open questions";
		FilterAction action = cachedActions.get(name1);
		if (action == null) {
			action = new FilterAction(name1);
			cachedActions.put(name1, action);
		}
		result.add(action);

		return result;
	}

	private class FilterAction extends Action {

		public FilterAction(String name) {
			super(name, Action.AS_CHECK_BOX);
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
		for (IAction layoutAction : createActions()) {
			ActionContributionItem item = new ActionContributionItem(
					layoutAction);
			item.fill(menu, -1);
		}

		return menu;
	}

	public Menu getMenu(Menu parent) {
		return null;
	}

}
