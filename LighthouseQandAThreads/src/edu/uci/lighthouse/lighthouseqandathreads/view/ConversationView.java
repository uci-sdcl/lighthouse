package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ConversationView extends Composite{

	ArrayList<ConversationElement> elements = new ArrayList<ConversationElement>();
	private String message;
	
	public ConversationView(Composite parent, int style) {
		super(parent, style);
		
		//layout
		GridData compsiteData = new GridData(450, 550);
		this.setLayout(new GridLayout(1, false));
		this.setLayoutData(compsiteData);
		
		//post new thread box-----------------------------
		GridData subjectData = new GridData(400, 30);
		final StyledText postNewThreadBox = new StyledText(this, SWT.BORDER);
		postNewThreadBox.setLayoutData(subjectData);
		
		postNewThreadBox.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setMessage(postNewThreadBox.getText());

			}
		});
		//---------------------------------------------------
		
		GridData scData = new GridData(400, 500);
	      ScrolledComposite sc = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
	      sc.setLayoutData(scData);
	      ConversationList cl = new ConversationList(sc, SWT.NONE);
	      sc.setContent(cl);


		
		
	}
	
	public void addConversationElement(ConversationElement element){
		elements.add(element);
	}
	
	public void expand(){}
	
	public void collapse(){}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
