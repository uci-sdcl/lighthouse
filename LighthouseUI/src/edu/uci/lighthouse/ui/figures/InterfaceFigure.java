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
package edu.uci.lighthouse.ui.figures;

import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import edu.uci.lighthouse.model.LighthouseInterface;
import edu.uci.lighthouse.ui.swt.util.ColorFactory;

public class InterfaceFigure extends AbstractUmlBoxFigure {

	private static final Color borderColor = ColorFactory.classBorder;
	private static final Color backgroundColor = ColorFactory.classGradientBackground;
	private static final Image icon = JavaUI.getSharedImages().getImageDescriptor(
			ISharedImages.IMG_OBJS_INTERFACE).createImage();
	
	public InterfaceFigure(LighthouseInterface aInterface) {
		super(aInterface);
	}

	@Override
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public Color getBorderColor() {
		return borderColor;
	}

	@Override
	public Image getIcon() {
		return icon;
	}
}
