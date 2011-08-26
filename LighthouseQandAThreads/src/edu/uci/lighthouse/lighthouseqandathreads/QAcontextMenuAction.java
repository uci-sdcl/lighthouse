package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.Collection;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;


import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.lighthouseqandathreads.view.NewQuestionDialog;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseEntity;
import edu.uci.lighthouse.model.QAforums.LHforum;
import edu.uci.lighthouse.model.QAforums.LHthreadCreator;
import edu.uci.lighthouse.ui.utils.GraphUtils;
import edu.uci.lighthouse.ui.views.actions.ContextMenuPlugin;

public class QAcontextMenuAction extends Action{
	private QAController controller;
	private LHforum forum;
	private LighthouseEntity le;
    private LighthouseClass clazz;
	
    
	public  QAcontextMenuAction( LHforum forum, LighthouseClass entity, LighthouseEntity umlClass){
		this.forum = forum;
		this.clazz = entity;
		this.le = umlClass;
		setText(clazz.getShortName()+" "+"forum");
	}
	public void run() {
		LighthouseAuthor author = ModelUtility.getAuthor();
		LHthreadCreator tm = new LHthreadCreator(author);

		Display display = Display.getDefault();

		NewQuestionDialog nqDialog = new NewQuestionDialog(
				display.getActiveShell(), "Forum", null,
				le.getFullyQualifiedName(), MessageDialog.INFORMATION,
				SWT.OK, tm, forum);

		QAController controller = new QAController(nqDialog, forum, clazz);


		int response = nqDialog.open();
		controller.stopObserving();
	}
	


}
