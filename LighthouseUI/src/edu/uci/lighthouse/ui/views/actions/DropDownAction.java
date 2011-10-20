package edu.uci.lighthouse.ui.views.actions;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.zest.core.widgets.IContainer;

public abstract class DropDownAction extends Action implements IMenuCreator {
	
	private Menu menu;
	protected IContainer container;
	private List<IAction> actions;
	protected IAction selectedAction;
	
	private static Logger logger = Logger.getLogger(LayoutDropDownAction.class);
	
	public DropDownAction(IContainer container){
		super(null,Action.AS_DROP_DOWN_MENU);
		this.container = container;
		init();
		actions = createActions();
		selectedAction = actions.get(getDefaultActionIndex());
		runAction(selectedAction);
		setMenuCreator(this);
	}

	protected abstract void init();
	
	protected abstract List<IAction> createActions();
	
	protected abstract int getDefaultActionIndex();
	
	@Override
	public void dispose() {
		logger.debug("dispose()");
		if (menu != null){
			menu.dispose();
			menu = null;
		}
	}

	@Override
	public Menu getMenu(Control parent) {
		if (menu == null) {
			menu = new Menu(parent);
			logger.debug("menu instance created.");
			for (IAction layoutAction : actions) {
				ActionContributionItem item = new ActionContributionItem(
						layoutAction);
				item.fill(menu, -1);
			}
		}
		return menu;
	}

	@Override
	public Menu getMenu(Menu parent) {
		return null;
	}
	
	@Override
	public void run() {
		logger.debug(getClass().getSimpleName()+" running!");
		runAction(getNextAction(actions,selectedAction));
	}
	
	private void runAction(IAction action){
		selectedAction.setChecked(false);
		action.setChecked(true);
		action.run();
		selectedAction = action;
	}
	
	private IAction getNextAction(List<IAction> list, IAction current){
		IAction result = null; 
		int index = list.indexOf(current);
		if (index != -1){
			index = ++index % list.size();
			result = list.get(index);
		}
		return result;
	}
}
