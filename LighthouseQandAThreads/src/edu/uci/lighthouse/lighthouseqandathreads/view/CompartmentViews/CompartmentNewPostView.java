package edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews;

import org.eclipse.draw2d.Panel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.lighthouseqandathreads.markers.CallBackMarkerCreator;
import edu.uci.lighthouse.lighthouseqandathreads.view.ForumView;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class CompartmentNewPostView extends Composite{
	private int NUM_COLUMNS = 1;
	private QuestionBoxView view;
	private CallBackMarkerCreator callback;
	public CompartmentNewPostView(Composite parent, int style,LHforum forum, TeamMember tm, PersistAndUpdate pu, CallBackMarkerCreator callback){
		super(parent,style);
		
		this.setLayout(new GridLayout(1, false));
		
		Color threadBack2 = new Color(this.getDisplay(),231,232,130);
		
		this.setBackground(threadBack2);
		this.callback = callback;
		
		view = new QuestionBoxView(this, SWT.None,forum,tm, pu,callback);
	}
	
	public String getMessage(){
		return view.getMessage();
	}

}
