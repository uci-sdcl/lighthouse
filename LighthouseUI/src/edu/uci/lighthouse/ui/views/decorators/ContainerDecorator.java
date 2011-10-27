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
