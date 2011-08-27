package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ListComposite extends Composite{

	ArrayList<Composite> list = new ArrayList<Composite>();
	private Composite temporaryParent = new Composite(this, SWT.None);
	
	public ListComposite(Composite parent, int style) {
		super(parent, style);
		
		this.setLayout(new GridLayout(1, false));
		
	}

	
	public void add(Composite newComposite){
		list.add(newComposite);
	}
	
	public void renderList(){
		changeParents();
		for(Composite child: list){
			child.setParent(this);
		}
		this.layout();
	}
	
	private void changeParents(){
		for(Composite child: list){
			child.setParent(temporaryParent);
		}
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
			if( index != list.size()-1){
				list.add(index+1, newComposite);
			}
			else
				list.add(newComposite);
		}
	}

}
