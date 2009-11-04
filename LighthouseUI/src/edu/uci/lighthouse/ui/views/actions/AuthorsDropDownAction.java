package edu.uci.lighthouse.ui.views.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.IContainer;

import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.ui.LighthouseUIPlugin;

public class AuthorsDropDownAction extends DropDownAction {
	
	private static Logger logger = Logger.getLogger(AuthorsDropDownAction.class);
	
	private static final String ICON = "/icons/in_others_workspaces.gif";
	private static final String DESCRIPTION = "Authors";

	public AuthorsDropDownAction(IContainer container){
		super(container);
	}
	
	@Override
	protected void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(LighthouseUIPlugin.PLUGIN_ID, ICON));
	}
	
	@Override
	protected List<IAction> createActions() {
		List<IAction> result = new ArrayList<IAction>();
		result.add(new AuthorAction("Max"));
		result.add(new AuthorAction("Bob"));
		result.add(new AuthorAction("Ana"));
		result.add(new AuthorAction("Jim"));
		return result;
	}
	
	private final class AuthorAction extends Action {
		private String name;
		public AuthorAction(String name){
			super(name,Action.AS_RADIO_BUTTON);
			this.name = name;
		}
		@Override
		public void run() {
			if (isChecked()) {				
//				edu.uci.lighthouse.core.Activator.getDefault().setAuthor(new LighthouseAuthor(name));
			}
		}
	}

	@Override
	protected int getDefaultActionIndex() {
		return 0;
	}

}
