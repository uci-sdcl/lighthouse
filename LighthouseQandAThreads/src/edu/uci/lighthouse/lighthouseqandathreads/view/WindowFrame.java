package edu.uci.lighthouse.lighthouseqandathreads.view;


import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class WindowFrame extends ForumElement{

	private ElementMenu menu; 
	private GridData data;
	
	public WindowFrame(Composite parent, int style) {
		super(parent, style);

		GridLayout layout = new GridLayout(1, false);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginWidth = 1;
		layout.marginHeight = 1;
		
		this.setLayout(layout);
		menu = new ElementMenu(this,SWT.None);
		
		data = new GridData();
		data.horizontalAlignment =  GridData.CENTER;
		this.setLayoutData(data);
		
		}



	public ElementMenu getElementMenu() {
		return menu;
	}
	
	public void setMinHeight(int height){
		data.minimumHeight = height;
	}
	
	public void setMinWidth(int width){
		data.minimumWidth = width;
	}
	
	public GridData getGridData(){
		return data;
	}

}
