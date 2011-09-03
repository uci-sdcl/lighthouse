package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class ListComposite extends Composite implements Observer{

	ArrayList<Composite> list = new ArrayList<Composite>();
	private Composite temporaryParent = new Composite(new Shell(), SWT.None);
	
	public ListComposite(Composite parent, int style) {
		super(parent, style);
		
		GridData compsiteData = new GridData();

		this.setBackground(ColorConstants.black);
		
		compsiteData.horizontalAlignment = GridData.CENTER;
		
		temporaryParent.setVisible(false);
		
		setLayout(new GridLayout(1, false));
		this.setLayoutData(compsiteData);
	
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
	
	public void clearChildren(){
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(Composite child: list){		
				indexes.add(list.indexOf(child));
		
		}
		
		for(Integer index : indexes){
			Composite child = list.get(index.intValue());
			child.dispose();
		}
		
		list.clear();
		
		
	}
	
	public void disposeRespondBoxes(){
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(Composite child: list){
			if(child instanceof RespondBoxView){
				
				indexes.add(list.indexOf(child));
			}			
		}
		
		for(Integer index : indexes){
			Composite child = list.get(index.intValue());
			child.dispose();
			list.remove(index.intValue());
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


	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof RespondBoxView){
			this.disposeRespondBoxes();
			this.renderList();
		}
		
	}

}
