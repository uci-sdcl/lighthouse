package edu.uci.lighthouse.lighthouseqandathreads;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;

public class NewQuestionDialog extends MessageDialog{

	private String question;
	
	public static int OK = 0;
	public static int CANCEL = 1;
	private static String [] labelArray = {"OK","CANCEL"};
	private Tree tree;

	public NewQuestionDialog(Shell parentShell, String dialogTitle,
			Image dialogTitleImage, String dialogMessage, int dialogImageType,
			int defaultIndex) {

		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
				dialogImageType, labelArray, defaultIndex);
	}

	public Control createCustomArea(Composite parent) {
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);

		
		
		TabFolder tabFolder = new TabFolder(parent, SWT.BORDER);
		
	    TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
	    tabItem.setText("Ask a question");
	    createQuestionComposite(tabFolder, tabItem);
 
	    TabItem tabItem2 = new TabItem(tabFolder, SWT.NULL);
	    tabItem2.setText("Threads");
	    createThreadComposite(tabFolder, tabItem2);



	    
	    return tabFolder;
	}
	
	private void createThreadComposite(TabFolder tabFolder, TabItem tabItem){
		GridData compsiteData = new GridData(650,450);
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(compsiteData);
	
		 tabItem.setControl(composite);


		GridData questionLayoutData = new GridData(600,400);
		
		tree = new Tree(composite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL
				| SWT.H_SCROLL);
		tree.setLayoutData(questionLayoutData);

		tree.addSelectionListener(new ListListener());
		
		
	}
	
	private class ListListener extends SelectionAdapter {

		public void widgetSelected(SelectionEvent e) {
			
		}
	}
	
	private void createQuestionComposite(TabFolder tabFolder, TabItem tabItem){
		
		GridData compsiteData = new GridData(650,450);
	
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(compsiteData);
	
		 tabItem.setControl(composite);


		GridData questionLayoutData = new GridData(600,400);
	    final StyledText st = new StyledText(composite, SWT.BORDER);
		st.setLayoutData(questionLayoutData);
		
		
		st.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e)  {
				setQuestion(st.getText());

			}
		});
		
		
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestion() {
		return question;
	}

}
