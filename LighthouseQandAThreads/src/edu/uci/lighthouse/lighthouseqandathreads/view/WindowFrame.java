package edu.uci.lighthouse.lighthouseqandathreads.view;


import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class WindowFrame extends ConversationElement{

	private ElementMenu menu; 
	
	public WindowFrame(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new GridLayout(1, false));
		menu = new ElementMenu(this,SWT.None);
		
		}



	public ElementMenu getElementMenu() {
		return menu;
	}

}
