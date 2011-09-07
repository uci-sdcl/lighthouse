package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.lighthouseqandathreads.view.WindowFrame;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class ReplyBoxView extends WindowFrame{

	public ReplyBoxView(Composite parent, int style,
			ForumThread thread, TeamMember tm, PersistAndUpdate pu) {
		super(parent, style);
	}

}
