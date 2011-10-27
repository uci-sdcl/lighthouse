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
package $packageName$;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;

import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;

public class $className$ extends CompartmentFigure {

	public $className$() {
		// Sets the layout manager.
		setLayoutManager(new FlowLayout());
	}

	@Override
	public boolean isVisible(MODE mode) {
		// Sets visibility
		return true;
	}

	@Override
	public void populate(MODE mode) {
		// Creates a new label
		add(new Label("$labelText$"));
	}

}
