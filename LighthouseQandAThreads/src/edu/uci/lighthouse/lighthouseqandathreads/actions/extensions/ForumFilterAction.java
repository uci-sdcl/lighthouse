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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.lighthouseqandathreads.Activator;
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
			action = new FilterAction(name1);
			cachedActions.put(name1, action);
		}
		result.add(action);
		
		String name2 = "has answered threads";
		FilterAction action2 = cachedActions.get(name2);
		if (action2 == null) {
			action2 = new FilterAction(name2);
			cachedActions.put(name2, action2);
		}
		result.add(action2);
		
		
		String name3 = "has closed threads";
		FilterAction action3 = cachedActions.get(name3);
		if (action3 == null) {
			action3 = new FilterAction(name3);
			cachedActions.put(name3, action3);
		}
		result.add(action3);

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
