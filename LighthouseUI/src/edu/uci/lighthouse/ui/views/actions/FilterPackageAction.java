package edu.uci.lighthouse.ui.views.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.IContainer;

import edu.uci.lighthouse.ui.views.FilterManager;
import edu.uci.lighthouse.views.filters.PackageFilter;

public class FilterPackageAction  extends Action implements IMenuCreator{

	private Menu menu;
	private List<IAction> actions;
	
	//FIXME: Not using container (graph or viewer)
	protected IContainer container;
	
	private static final String ICON = "$nl$/icons/full/obj16/package_obj.gif";
	private static final String DESCRIPTION = "Show just modifications";
	
	private static Logger logger = Logger.getLogger(FilterModifiedAction.class);

	public FilterPackageAction(IContainer container){
		super(null, Action.AS_DROP_DOWN_MENU);
		this.container = container;
		init();
		actions = createActions();
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
			result.add(new PackageFilterAction(name));
		}
//		result.add(new PackageFilterAction("tiago"));
		return result;
	}
	
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
							if (packageFragment.isOpen()) {
								result.add(packageFragment.getElementName());
							}
						}
					} catch (Exception e) {
						logger.error(e);
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
	
	private final class PackageFilterAction extends Action{
		PackageFilter filter;
		public PackageFilterAction(String packageName){
			super(packageName,Action.AS_CHECK_BOX);
			filter = new PackageFilter(packageName);
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

}
