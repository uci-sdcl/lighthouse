package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

public class ConversationView extends Composite{

	ArrayList<ConversationElement> elements = new ArrayList<ConversationElement>();
	
	public ConversationView(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	public void addConversationElement(ConversationElement element){
		elements.add(element);
	}
	
	public void expand(){}
	
	public void collapse(){}

}
