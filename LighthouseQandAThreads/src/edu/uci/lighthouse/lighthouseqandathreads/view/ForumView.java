package edu.uci.lighthouse.lighthouseqandathreads.view;

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.lighthouseqandathreads.PersistAndUpdate;
import edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews.QuestionBoxView;
import edu.uci.lighthouse.model.QAforums.ForumThread;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.TeamMember;

public class ForumView extends Composite{

	ArrayList<ForumElement> elements = new ArrayList<ForumElement>();
	private ThreadList cl;
	private PersistAndUpdate pu;


	
	public ForumView(Composite parent, int style, TeamMember tm, LHforum forum, PersistAndUpdate pu) {
		super(parent, style);
		this.pu = pu;
		
		//layout
		GridData compsiteData = new GridData(LayoutMetrics.CONVERSATION_VIEW_WIDTH,LayoutMetrics.CONVERSATION_VIEW_HEIGHT);
		compsiteData.horizontalAlignment = SWT.CENTER;
		this.setLayoutData(compsiteData);
		this.setLayout(new GridLayout(1, false));
		
	
		
		//post new thread box-----------------------------
		QuestionBoxView qbv = new QuestionBoxView(this, SWT.None, forum, tm,pu,null);
		//conversation list ---------------------------------------------------

	      cl = new ThreadList(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER, pu);


		
	}
	

	
	public void addConversationElement(ForumThread thread, TeamMember tm, PersistAndUpdate pu){
		cl.addConversationElement(thread,tm,pu);
		
	}
	
	public void addConversationElement(ForumElement element){
		cl.addConversationElement(element);

	}
	
	public void expand(){}
	
	public void collapse(){}


}
