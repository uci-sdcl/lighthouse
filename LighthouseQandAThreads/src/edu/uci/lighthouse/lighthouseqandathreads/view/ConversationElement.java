package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.Observer;

import org.eclipse.swt.widgets.Composite;

public class ConversationElement extends Composite implements IHasObservablePoint{
	private ObservablePoint observablePoint = new ObservablePoint();
	
	public ConversationElement(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void observeMe(Observer observer) {
		observablePoint.addObserver(observer);
		
	}

	@Override
	public ObservablePoint getObservablePoint() {
		return this.observablePoint;
	}

}
