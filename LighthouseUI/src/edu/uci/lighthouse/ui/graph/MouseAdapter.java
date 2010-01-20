package edu.uci.lighthouse.ui.graph;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;

public class MouseAdapter implements MouseListener{

	@Override
	public void mouseDoubleClicked(MouseEvent me) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
		System.out.println("mousePressed");
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}

}
