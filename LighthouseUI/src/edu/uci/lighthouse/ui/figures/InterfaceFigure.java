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
