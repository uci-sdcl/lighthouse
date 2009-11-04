package edu.uci.lighthouse.ui.views.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.IContainer;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import edu.uci.lighthouse.ui.LighthouseUIPlugin;

public class LayoutDropDownAction extends DropDownAction {
	
	private static Logger logger = Logger.getLogger(LayoutDropDownAction.class);
	
	private static final String ICON = "/icons/frlayout.gif";
	private static final String DESCRIPTION = "Diagram Mode";

	public LayoutDropDownAction(IContainer container){
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
		final int style = LayoutStyles.NO_LAYOUT_NODE_RESIZING;
		result.add(new LayoutAction(new SpringLayoutAlgorithm(style)));
		result.add(new LayoutAction(new TreeLayoutAlgorithm(style)));
		result.add(new LayoutAction(new RadialLayoutAlgorithm(style)));
		result.add(new LayoutAction(new GridLayoutAlgorithm(style)));
		return result;
	}
	
	private final class LayoutAction extends Action {
		private LayoutAlgorithm algorithm;
		public LayoutAction(LayoutAlgorithm algorithm){
			super(formatLayoutAlgorithmName(algorithm),Action.AS_RADIO_BUTTON);
			this.algorithm = algorithm;
		}
		@Override
		public void run() {
			if (isChecked()) {
				logger.debug(getText() + " running...");
				container.setLayoutAlgorithm(algorithm, true);
				selectedAction = this;
			}
		}
	}

	@Override
	protected int getDefaultActionIndex() {
		return 3;
	}
	
	private String formatLayoutAlgorithmName(LayoutAlgorithm algorithm){
		String result = algorithm.getClass().getSimpleName();
		result = result.replaceFirst("LayoutAlgorithm", " Layout");
		return result;
	}
}
