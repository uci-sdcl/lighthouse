package edu.uci.lighthouse.lighthouseqandathreads.actions;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews.CompartmentNewPostView;
import edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews.CompartmentReplyView;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.LHthreadCreator;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import edu.uci.lighthouse.ui.utils.GraphUtils;

public class ReplyMenuAction extends Action{
	private Shell postShell;
	private TeamMember tm;
    private PersistAndUpdate pu;
    private ForumThread thread;
   
	public  ReplyMenuAction(ForumThread thread, TeamMember tm, PersistAndUpdate pu){

		this.tm = tm;
		this.pu = pu;
		
		this.thread = thread;
		
		setText("Reply To Question");
		
		if(thread.isClosed())
			this.setEnabled(false);
	}
	public void run() {
		LighthouseAuthor author = ModelUtility.getAuthor();
		LHthreadCreator tm = new LHthreadCreator(author);

	
		GridLayout layout = new GridLayout(1, false);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginWidth = 1;
		layout.marginHeight = 1;
		
		postShell = new Shell(GraphUtils.getGraphViewer()
				.getGraphControl().getDisplay().getActiveShell());
		
		postShell.setLayout(layout);
		
		
		CompartmentReplyView npv = new CompartmentReplyView(postShell, SWT.None,thread,tm,pu);
		
		
		postShell.setBackground(ColorConstants.black);
		
		postShell.setSize(postShell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		postShell.setText("Reply");
		
		postShell.open();
		
		
		

	}
	

	


}
