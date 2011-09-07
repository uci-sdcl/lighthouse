package edu.uci.lighthouse.lighthouseqandathreads.actions;

import org.eclipse.jface.action.Action;

import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class CloseThreadMenuAction extends Action{
	private TeamMember tm;
    private PersistAndUpdate pu;
    private ForumThread thread;
	public  CloseThreadMenuAction(ForumThread thread, TeamMember tm, PersistAndUpdate pu){
		this.tm = tm;
		this.pu = pu;
		
		this.thread = thread;
		setText("Close Thread");
		
		this.setEnabled(false);
	}
	public void run() {


	}
}
