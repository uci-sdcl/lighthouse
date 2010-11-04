package edu.uci.lighthouse.ui.views.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.IContainer;

import edu.uci.lighthouse.ui.views.FilterManager;
import edu.uci.lighthouse.views.filters.ViewModifiedFilter;

public class FilterModifiedAction extends Action {

	//FIXME: Not using container (graph or viewer)
	protected IContainer container;
	
	private static final String ICON = "$nl$/icons/full/etool16/java_attach.gif";
	private static final String DESCRIPTION = "Show just modifications";

	private static Logger logger = Logger.getLogger(FilterModifiedAction.class);
	
	ViewerFilter filter = new ViewModifiedFilter();
	
	public FilterModifiedAction(IContainer container){
		super(null, IAction.AS_CHECK_BOX);
		this.container = container;
		init();
	}
	
	private void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.jdt.ui", ICON));
	}
	
	@Override
	public void run() {
		if (isChecked()){
			FilterManager.getInstance().addViewerFilter(filter);
		} else {
			FilterManager.getInstance().removeViewerFilter(filter);
		}
	}

}
