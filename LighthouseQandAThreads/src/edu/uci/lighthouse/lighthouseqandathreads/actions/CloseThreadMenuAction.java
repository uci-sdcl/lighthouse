package edu.uci.lighthouse.lighthouseqandathreads.actions;

import org.eclipse.jface.action.Action;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class CloseThreadMenuAction extends Action{
	private TeamMember tm;
    private PersistAndUpdate pu;
    private ForumThread thread;
    private String author;

	public  CloseThreadMenuAction(ForumThread thread, TeamMember tm, PersistAndUpdate pu){
		this.tm = tm;
		this.pu = pu;
		
		this.thread = thread;
		setText("Close Thread");
		
		LighthouseAuthor LHauthor = ModelUtility.getAuthor();
		
		String author = LHauthor.getName();
		
		if(!thread.getRootQuestion().getTeamMemberAuthor().getAuthor().getName().equals(author))
			this.setEnabled(false);
	}
	public void run() {
		thread.setClosed(true);
		pu.run();

	}
}
