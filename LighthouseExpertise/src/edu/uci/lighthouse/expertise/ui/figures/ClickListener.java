package edu.uci.lighthouse.expertise.ui.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;

public class ClickListener implements MouseListener {
	
	/*Label listenee = new Label();
	
	public void addListener(Label lab)
	{
		listenee = lab;
	}*/
	
	String type;
	
	public void setType(String ty)
	{
		type = ty;
	}
	

	public void mouseDoubleClicked(MouseEvent e)
	{
		
	}

	public void mousePressed(MouseEvent e)
	{
		System.out.println(type + " pressed");
		
		
	}

	public void mouseReleased(MouseEvent e) 
	{
		System.out.println(type + " released");
	}

	public void mouseEntered(MouseEvent e) 
	{
		
	}

	public void mouseExited(MouseEvent e) 
	{
		
	}

	public void mouseClicked(MouseEvent e) 
	{
		//how does this one work?
		System.out.println("clicked");
	}
}
