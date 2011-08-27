package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

public class ListComposite extends Composite{

	ArrayList<Composite> list = new ArrayList<Composite>();
	
	public ListComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	public void addBefore(Composite newComposite, Composite before){
		boolean hasComposite = list.contains(before);
		if(hasComposite){
			int index = list.indexOf(before);
			list.add(index, newComposite);
		}
	}
	
	public void addAfter(Composite newComposite, Composite after){
		boolean hasComposite = list.contains(after);
		if(hasComposite){
			int index = list.indexOf(after);
			if( index != list.size()-1)
				list.add(index+1, newComposite);
			else
				list.add(newComposite);
		}
	}

}
