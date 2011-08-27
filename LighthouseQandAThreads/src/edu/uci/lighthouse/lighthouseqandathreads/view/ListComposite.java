package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class ListComposite extends Composite{

	ArrayList<Composite> list = new ArrayList<Composite>();
	private Composite temporaryParent = new Composite(new Shell(), SWT.None);
	
	public ListComposite(Composite parent, int style) {
		super(parent, style);
		
		this.setLayout(new GridLayout(1, false));
		temporaryParent.setVisible(false);
		setLayout(new GridLayout(1, false));
	}

	
	public void add(Composite newComposite){
		list.add(newComposite);
	}
	
	public void renderList(){
		changeParents();
		for(int i = 0; i< list.size(); i++){
			list.get(i).setParent(this);
		}
		this.setSize(this.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		this.layout();
	}
	
	private void changeParents(){
		for(Composite child: list){
			child.setParent(temporaryParent);
		}

	}
	
	public void clearReplyBox(){
		for(Composite child: list){
			if(child instanceof RespondBoxView)
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
