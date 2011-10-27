/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
package edu.uci.lighthouse.ui.views.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.IContainer;

import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.LighthouseEvent;
import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.model.jpa.LHAuthorDAO;
import edu.uci.lighthouse.ui.LighthouseUIPlugin;
import edu.uci.lighthouse.ui.views.FilterManager;
import edu.uci.lighthouse.views.filters.IClassFilter;

public class FilterAuthorAction extends Action implements IMenuCreator {
	
	private Menu menu;
	protected IContainer container;
//	private List<IAction> actions;
	
	private static final String ICON = "/icons/in_others_workspaces.gif";
	private static final String DESCRIPTION = "Authors";
	
	Map<String,AuthorClassFilter> cacheFilters = new HashMap<String,AuthorClassFilter>();

	private static Logger logger = Logger.getLogger(FilterAuthorAction.class);
	
	public FilterAuthorAction(IContainer container){
		super(null, Action.AS_DROP_DOWN_MENU);
		this.container = container;
		init();
//		actions = createActions();
		setMenuCreator(this);
	}
	
	protected void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin(LighthouseUIPlugin.PLUGIN_ID, ICON));
	}
	
	protected List<IAction> createActions() {
		List<IAction> result = new ArrayList<IAction>();
		try {
			List<LighthouseAuthor> authors = new LHAuthorDAO().list();
			for (LighthouseAuthor author : authors) {
				AuthorClassFilter filter = cacheFilters.get(author.getName());
				if (filter == null){
					filter = new AuthorClassFilter(author.getName());
					cacheFilters.put(author.getName(), filter);
				}
				result.add(filter);
			}
		} catch (Exception e) {
			logger.error(e,e);
		}

		return result;
	}
	
	private final class AuthorClassFilter extends Action implements IClassFilter {
		private String name;
		public AuthorClassFilter(String name){
			super(name,Action.AS_CHECK_BOX);
			this.name = name;
		}
		@Override
		public void run() {
			if (isChecked()) {				
				FilterManager.getInstance().addClassFilter(this);
			} else {
				FilterManager.getInstance().removeClassFilter(this);
			}
		}
		
		@Override
		public boolean select(LighthouseModel model, LighthouseEntity entity) {
			Collection<LighthouseEvent> events = model.getEvents(entity);
			if (events.size() > 0){
				for (LighthouseEvent event : events) {					
					if (select(model,event)) {
						return true;
					}
				}
			} else {
				/* The entity has no events, but it should be include in the diagram, i.e., this is a committed entity. */ 
				return true;
			}
			return false;
		}
		
		@Override
		public boolean select(LighthouseModel model, LighthouseEvent event) {			
			return name.equalsIgnoreCase(event.getAuthor().getName());
		}
	}

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
		return null;
	}

}
