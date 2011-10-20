package edu.uci.lighthouse.ui.views.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

import edu.uci.lighthouse.ui.swt.util.ColorFactory;
import edu.uci.lighthouse.ui.utils.GraphUtils;
import edu.uci.lighthouse.ui.views.HighlightManager;
import edu.uci.lighthouse.ui.views.IHightlightAction;

public class HighlightDropDownAction extends Action implements IMenuCreator,
		SelectionListener, IHightlightAction {

	private Menu menu;
	protected IContainer container;
	private List<IAction> actions;
	/* We need this to know what have been selected previously. */
	private Collection<GraphNode> selectedNodes;

	private static final String ICON = "$nl$/icons/elcl16/highlight.gif";
	private static final String DESCRIPTION = "Highlight elements";

	private HighlightManager manager;

	private static Logger logger = Logger
			.getLogger(HighlightDropDownAction.class);

	public HighlightDropDownAction(HighlightManager manager) {
		super(null, Action.AS_DROP_DOWN_MENU);
		this.container = manager.getViewer().getGraphControl();
		this.manager = manager;
		init();
		actions = createActions();
		selectedNodes = new LinkedList<GraphNode>();
		setMenuCreator(this);
		// ISelectionChangedListener,ISelectionProvider
		container.getGraph().addSelectionListener(this);
	}

	private void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.help.ui", ICON));
	}

	private List<IAction> createActions() {
		List<IAction> result = new ArrayList<IAction>();
		result.add(new HighlightClassAction());
		result.add(new HighlightRelationshipsAction());
		return result;
	}

	private final class HighlightClassAction extends Action {
		public HighlightClassAction() {
			super("Highlight Class", Action.AS_CHECK_BOX);
			setChecked(true);
		}

		@Override
		public void run() {
			if (isChecked()) {
				highlightNodes(GraphUtils.getSelectedGraphNodes(container.getGraph()));
			} else {
				unhighlightNodes(GraphUtils.getSelectedGraphNodes(container.getGraph()));
			}
		}
	}

	private final class HighlightRelationshipsAction extends Action {
		public HighlightRelationshipsAction() {
			super("Highlight Relationships", Action.AS_CHECK_BOX);
			setChecked(true);
		}

		@Override
		public void run() {
			if (isChecked()) {
				highlightConnections(GraphUtils.getSelectedGraphNodes(container.getGraph()));
			} else {
				unhighlightConnections(GraphUtils.getSelectedGraphNodes(container.getGraph()));
			}
		}
	}

	private void updateFigures(Collection<GraphNode> nodes, boolean highlight) {
		for (GraphNode node : nodes) {
			if (!node.isDisposed()) {
				if (highlight) {
					logger.debug("highlight: " + node);
					manager.add(this, node);
				} else {
					logger.debug("unhighlight: " + node);
					manager.remove(this, node);

				}
			}
		}
	}

	private void updateConnections(Collection<GraphConnection> relationships,
			boolean highlight) {
		for (GraphConnection connection : relationships) {
			if (highlight) {
				connection.highlight();
			} else {
				connection.unhighlight();
			}
		}
	}

	private void unhighlightNodes(Collection<GraphNode> nodes) {
		updateFigures(nodes, false);
	}

	private void highlightNodes(Collection<GraphNode> nodes) {
		updateFigures(nodes, true);
	}

	private void highlightConnections(Collection<GraphNode> nodes) {
		for (GraphNode node : nodes) {
			updateConnections(node.getSourceConnections(), true);
			updateConnections(node.getTargetConnections(), true);
		}
	}

	private void unhighlightConnections(Collection<GraphNode> nodes) {
		for (GraphNode node : nodes) {
			updateConnections(node.getSourceConnections(), false);
			updateConnections(node.getTargetConnections(), false);
		}
	}

	@Override
	public void dispose() {
		logger.debug("dispose()");
		if (menu != null) {
			menu.dispose();
			menu = null;
		}
		container.getGraph().removeSelectionListener(this);
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
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		logger.debug("widgetSelected: " + e.item);
		unhighlightNodes(selectedNodes);
		unhighlightConnections(selectedNodes);
		selectedNodes.clear();
		selectedNodes.addAll(GraphUtils.getSelectedGraphNodes(container.getGraph()));
		if (e.item instanceof GraphNode) {
			for (IAction action : actions) {
				action.run();
			}
		}
	}

	@Override
	public int getPriority() {
		return IHightlightAction.HIGHLIGHT;
	}

	@Override
	public Color getColor() {
		return ColorFactory.classHighlight;
	}
}
