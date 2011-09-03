package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import org.eclipse.draw2d.Panel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import edu.uci.lighthouse.lighthouseqandathreads.ForumController;
import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.lighthouseqandathreads.view.ForumView;
import edu.uci.lighthouse.lighthouseqandathreads.view.ListComposite;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.Post;

public class CompartmentThreadView extends Composite {
	final StyledText postNewThreadBox;

	public CompartmentThreadView(Composite parent, int style, ForumThread thread) {
		super(parent, style);

		this.setLayout(new GridLayout(1, false));
		
		ListComposite listOfReplies = new ListComposite(this,SWT.None);
		for(Post post : thread.getRootQuestion().getResponses()){
			Composite composite = new Composite(this, SWT.None);
			Label replyLabel = new Label(composite, SWT.None);
			replyLabel.setText(post.getMessage());
			
			listOfReplies.add(composite);
		}

		postNewThreadBox = new StyledText(this, SWT.BORDER | SWT.V_SCROLL);
		GridData postNewThreadBoxData = new GridData(SWT.FILL, SWT.FILL, true,
				true);
		postNewThreadBox.setLayoutData(postNewThreadBoxData);

	}

}
