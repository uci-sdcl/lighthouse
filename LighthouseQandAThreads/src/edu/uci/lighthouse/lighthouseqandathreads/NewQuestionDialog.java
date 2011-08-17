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

public class NewQuestionDialog extends MessageDialog{

	private String question;
	
	public static int OK = 0;
	public static int CANCEL = 1;
	private static String [] labelArray = {"OK","CANCEL"};

	public NewQuestionDialog(Shell parentShell, String dialogTitle,
			Image dialogTitleImage, String dialogMessage, int dialogImageType,
			int defaultIndex) {

		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
				dialogImageType, labelArray, defaultIndex);
	}

	public Control createCustomArea(Composite parent) {
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);

		GridData compsiteData = new GridData(500,350);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(compsiteData);


		GridData questionLayoutData = new GridData(500,300);
	    final StyledText st = new StyledText(composite, SWT.BORDER);
		st.setLayoutData(questionLayoutData);
		
		
		st.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e)  {
				setQuestion(st.getText());

			}
		});

	    
	    return composite;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestion() {
		return question;
	}

}
