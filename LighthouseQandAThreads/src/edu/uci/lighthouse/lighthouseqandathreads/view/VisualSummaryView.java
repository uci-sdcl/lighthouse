package edu.uci.lighthouse.lighthouseqandathreads.view;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.lighthouseqandathreads.Activator;

public class VisualSummaryView extends Panel{
	private Image icon;
	private FlowLayout layout;
	private int total;
	private int answered;

	public VisualSummaryView(int total, int answered){
		layout = new FlowLayout();
		layout.setMajorAlignment(FlowLayout.ALIGN_LEFTTOP);
		layout.setMinorSpacing(25);
		setLayoutManager(layout);
		icon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		"/icons/visualSummary.png").createImage();
		this.total = total;
		this.answered = answered;
		Label label = new Label(answered+"/"+total+"");
		label.setIcon(icon);
		this.add(label);
		
	}
	
	public void setQuestionTotal(int total){
		this.total = total;
	}
	
	public void setQuestionAnswered(int answered){
		this.answered = answered;
	}
	
	

}
