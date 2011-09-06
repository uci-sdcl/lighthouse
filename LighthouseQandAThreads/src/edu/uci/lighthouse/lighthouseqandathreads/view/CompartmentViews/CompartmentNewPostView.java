package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import org.eclipse.draw2d.Panel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.lighthouseqandathreads.view.ForumView;
import edu.uci.lighthouse.lighthouseqandathreads.view.QuestionBoxView;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class CompartmentNewPostView extends Composite{
	
	public CompartmentNewPostView(Composite parent, int style,LHforum forum, TeamMember tm, PersistAndUpdate pu){
		super(parent,style);
		QuestionBoxView view = new QuestionBoxView(this, SWT.None,forum,tm);
	}

}
