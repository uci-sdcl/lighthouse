package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.lighthouseqandathreads.ThreadController;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class ThreadList extends ScrolledComposite  {

	ListComposite composite;
	private PersistAndUpdate pu;
	
	public ThreadList(Composite parent, int style, PersistAndUpdate pu) {
		super(parent, style);
		
		this.pu = pu;
		
		GridData compsiteData = new GridData(LayoutMetrics.CONVERSATION_LIST_WIDTH, LayoutMetrics.CONVERSATION_LIST_HEIGHT);
		compsiteData.horizontalAlignment = GridData.CENTER;
		this.setLayoutData(compsiteData);
		this.setLayout(new GridLayout(1, false));
		

		composite = new ListComposite(this, SWT.None);
		this.setContent(composite);

		composite.setLayout(new GridLayout(1, false));
		this.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/**
	 * A ThreadController is added to observe thread and its created view
	 * @param thread
	 * @param tm
	 */
	public void addConversationElement(ForumThread thread, TeamMember tm, PersistAndUpdate pu) {
		ThreadView threadView = new ThreadView(composite, SWT.None, thread, tm,pu);
		
		composite.add(threadView);
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		composite.renderList();
		ThreadController controller = new ThreadController(thread,threadView,pu);
	}

	public void addConversationElement(ForumElement element) {
		element.setParent(composite);
		
		composite.add(element);
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		composite.renderList();
	}



}
