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
	private FlowLayout layout;
	
	public HelpFigure() {
		layout = new FlowLayout();
		setLayoutManager(layout);
		icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/person.png").createImage();
	}

	@Override
	public boolean isVisible(MODE mode) {
		//QUESTION: How do I get the current level?
		//if (level == mode.THREE || level == mode.FOUR)
		//	return true;
		return true;
	}

	@Override
	public void populate(MODE mode) {
		/* It is display equals in any mode. */
		removeAll();
		
		//QUESTION: How do I get the size of the figure?
		int figureWidth = this.getSize().width;
		
		Label expertise1 = new Label("1", icon);		
		Label expertise2 = new Label("2", icon);
		Label expertise3 = new Label("3", icon);
		
		int labelWidth = icon.getBounds().width;
		
		int labelHeight = icon.getBounds().height;
		int extraWidth = figureWidth - 3 * labelWidth;
		System.out.println(figureWidth);
		Label emptySpace = new Label();
		emptySpace.setSize(extraWidth/2, labelHeight);
		
		
		add(expertise1);
		add(emptySpace);
		add(expertise2);
		add(emptySpace);
		add(expertise3);
	}
	
}
