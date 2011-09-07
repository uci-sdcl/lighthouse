package edu.uci.lighthouse.lighthouseqandathreads;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews.CompartmentNewPostView;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.LHthreadCreator;
import edu.uci.lighthouse.model.QAforums.TeamMember;
import edu.uci.lighthouse.ui.utils.GraphUtils;

public class ReplyMenuAction extends Action{
	private LHforum forum;
	private LighthouseEntity le;
    private LighthouseClass clazz;
	private Shell postShell;
	private TeamMember tm;
    private PersistAndUpdate pu;
    
	public  ReplyMenuAction(){// LHforum forum, LighthouseClass entity, LighthouseEntity umlClass, TeamMember tm, PersistAndUpdate pu){
		/*this.forum = forum;
		this.clazz = entity;
		this.le = umlClass;
		this.tm = tm;
		this.pu = pu;*/
		setText("Reply To Question");
	}
	public void run() {
		LighthouseAuthor author = ModelUtility.getAuthor();
		LHthreadCreator tm = new LHthreadCreator(author);

		Display display = Display.getDefault();
		
		
		postShell = new Shell(GraphUtils.getGraphViewer()
				.getGraphControl().getDisplay().getActiveShell());
		
		postShell.setLayout(new org.eclipse.swt.layout.GridLayout(1, false));
		
		
		CompartmentNewPostView npv = new CompartmentNewPostView(postShell, SWT.None,forum,tm,pu);
		
		
		postShell.setBackground(ColorConstants.black);
		
		postShell.setSize(postShell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		postShell.setText(clazz.getFullyQualifiedName());
		
		postShell.open();

	}
	


}
