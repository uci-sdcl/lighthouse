package edu.uci.lighthouse.expertise.ui.figures;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.expertise.Activator;
import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;

public class HelpFigure extends CompartmentFigure {
	
	private Image icon;
	
	public HelpFigure() {
		setLayoutManager(new FlowLayout());
		icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/person.png").createImage();
	}

	@Override
	public boolean isVisible(MODE mode) {
		//FIXME: So far it is displaying in all modes 
		return true;
	}

	@Override
	public void populate(MODE mode) {
		/* It is display equals in any mode. */
		removeAll();
		Label label = new Label("Description", icon);
		add(label);
	}
	
}
