package edu.uci.lighthouse.ui.views.decorators;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.IContainer;
import org.eclipse.zest.layouts.LayoutAlgorithm;

public abstract class ContainerDecorator implements IContainer{
	
	protected IContainer container;
	
	public ContainerDecorator(IContainer container){
		this.container = container;
		init(container.getGraph().getParent());
	}
	
	/** */
	protected abstract void init(Composite parent);

	/* (non-Javadoc)
	 * @see org.eclipse.zest.core.widgets.IContainer#applyLayout()
	 */
	@Override
	public void applyLayout() {
		container.applyLayout();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.zest.core.widgets.IContainer#getGraph()
	 */
	@Override
	public Graph getGraph() {
		return container.getGraph();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.zest.core.widgets.IContainer#getItemType()
	 */
	@Override
	public int getItemType() {
		return container.getItemType();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.zest.core.widgets.IContainer#getNodes()
	 */
	@Override
	public List getNodes() {
		return container.getNodes();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.zest.core.widgets.IContainer#setLayoutAlgorithm(org.eclipse.zest.layouts.LayoutAlgorithm, boolean)
	 */
	@Override
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm,
			boolean applyLayout) {
		container.setLayoutAlgorithm(algorithm, applyLayout);
	}
}
