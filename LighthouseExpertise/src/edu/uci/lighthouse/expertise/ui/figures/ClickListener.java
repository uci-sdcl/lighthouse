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
package edu.uci.lighthouse.expertise.ui.figures;



import javax.swing.JOptionPane;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

//Created by Alex Taubman

public class ClickListener implements MouseListener {

	/*Label listenee = new Label();

	public void addListener(Label lab)
	{
		listenee = lab;
	}*/

	String type;
	Object callee;

	public void setType(Object ca, String ty)
	{
		callee = ca;
		type = ty;
	}


	public void mouseDoubleClicked(MouseEvent e)
	{

	}


	public void mouseRightClicked(MouseEvent e)
	{
		System.out.println("testright");
	}

	public void mousePressed(MouseEvent e)
	{
		System.out.println(type + " pressed" + e);


	}

	public void mouseReleased(MouseEvent e) 
	{
		System.out.println(type + " released");
		//if the help label was clicked
		if (type.equals("Help person"))
		{
			changeQuestionLabel();
		}
		else if (type.equals("New Question"))
		{
			newQuestionLabel();
		}
		//check for right click
	
		//if	((MouseEvent.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)
		{
		//	System.out.println("IT WORKED");
		}
	}

	public void changeQuestionLabel(){

		Display.getDefault().asyncExec(new Runnable() {

			//run a input dialog to get the new label text
			public void run() {

				Shell shell = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell();
				InputDialog dialog = new InputDialog(shell,"Lighthouse", "Type in new label text:", null, null);
				dialog.open();
				//if the cancel button was not pressed
				//if (dialog.CANCEL != 1)
				//change the label text
				((HelpFigure)callee).changeText(dialog.getValue());
			}

		});

	}

	public void newQuestionLabel(){


		((HelpFigureContainer)callee).addNewContainer(false);




	}

	public void mouseEntered(MouseEvent e) 
	{
		System.out.println("testenter");
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
