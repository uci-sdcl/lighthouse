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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
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

import edu.uci.lighthouse.model.LighthouseModel;
import edu.uci.lighthouse.ui.views.FilterManager;
import edu.uci.lighthouse.views.filters.PackageFilter;

public class FilterPackageAction  extends Action implements IMenuCreator{

	private Menu menu;
//	private List<IAction> actions;
	
	//FIXME: Not using container (graph or viewer)
	protected IContainer container;
	
	private static final String ICON = "$nl$/icons/full/obj16/package_obj.gif";
	private static final String DESCRIPTION = "Show just modifications";
	
	Map<String,PackageFilterAction> cacheFilters = new HashMap<String,PackageFilterAction>();
	
	private static final String DEFAULT_PACKAGE = "(default package)";
	
	private static Logger logger = Logger.getLogger(FilterModifiedAction.class);

	public FilterPackageAction(IContainer container){
		super(null, Action.AS_DROP_DOWN_MENU);
		this.container = container;
		init();
//		actions = createActions();
		setMenuCreator(this);
	}
	
	protected void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin
				.imageDescriptorFromPlugin("org.eclipse.jdt.ui", ICON));
	}
	
	protected List<IAction> createActions() {
		List<IAction> result = new ArrayList<IAction>();
		Collection<String> packageNames = getPackageNames();
		for (String name : packageNames) {
			PackageFilterAction filter = cacheFilters.get(name);
			if (filter == null){
				filter = new PackageFilterAction(name);
				cacheFilters.put(name, filter);
			}
			result.add(filter);
		}
//		result.add(new PackageFilterAction("tiago"));
		return result;
	}

	private Collection<String> getPackageNames(){
		LighthouseModel model = LighthouseModel.getInstance();
		List<String> result = new LinkedList<String>(model.getPackageNames());
		Collections.sort(result);
		return result;
	}
	
	/*
	private Collection<String> getPackageNames(){
		Set<String> result = new TreeSet<String>();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();	
		IProject[] projects = workspace.getRoot().getProjects();
		for (IProject project : projects) {
			if (project.isOpen()){
				IJavaProject javaProject = JavaCore.create(project);
				if (javaProject != null){
					try {
						IPackageFragment[] packageFragments = javaProject.getPackageFragments();
						for (IPackageFragment packageFragment : packageFragments) {
							if (packageFragment.getKind() == IPackageFragmentRoot.K_SOURCE && packageFragment.getCompilationUnits().length > 0) {
								System.out.println(packageFragment.getElementName());
								result.add(packageFragment.getElementName());
							}
						}
					} catch (Exception e) {
						logger.error(e,e);
					}
				}
			}
		}
		
//		final Collection<IFile> files = new LinkedList<IFile>();
//		for (int i = 0; i < projects.length; i++) {
//			if (projects[i].isOpen()) {
//				files.addAll(getFilesFromProject(projects[i]));
//			}
//		}
		return result;
	}
	*/
	private final class PackageFilterAction extends Action{
		PackageFilter filter;
		public PackageFilterAction(String packageName){
			super(packageName,Action.AS_CHECK_BOX);
			filter = new PackageFilter(packageName);
			if ("".equals(packageName)){
				setText(DEFAULT_PACKAGE);
			}
		}
		@Override
		public void run() {
			if (isChecked()) {				
				FilterManager.getInstance().addViewerFilter(filter);
			} else {
				FilterManager.getInstance().removeViewerFilter(filter);
			}
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
