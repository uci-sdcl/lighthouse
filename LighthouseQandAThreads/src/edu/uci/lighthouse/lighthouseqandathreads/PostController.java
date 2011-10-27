/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
package edu.uci.lighthouse.lighthouseqandathreads;

import java.lang.reflect.Field;
import java.util.Observable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.lighthouseqandathreads.view.ForumElement;
import edu.uci.lighthouse.lighthouseqandathreads.view.ThreadView;
import edu.uci.lighthouse.model.QAforums.AddEvent;
import edu.uci.lighthouse.model.QAforums.Post;
import edu.uci.lighthouse.model.QAforums.UpdateEvent;


public class PostController implements IController<Post> {
	private Post post;
	private ForumElement view;
	private PersistAndUpdate persisterAndUpdater;

	public PostController(Post post, ForumElement view, PersistAndUpdate pu){
		this.post = post;
		this.view = view;
		post.addObserver(this);
		view.observeMe(this);
		this.persisterAndUpdater = pu;
	}
	
	public void update(Observable o, Object arg) {
		
		if(o instanceof Post && arg instanceof AddEvent){
			AddEvent<Post,Post> event = (AddEvent<Post,Post>)arg;
			//really need a better way of updating view...
			ThreadView threadView = (ThreadView)view.getParent().getParent();
			threadView.addPost(event.getAddition(), event.getAddition().isRoot()); 
			
			//commits to database
			persisterAndUpdater.run();
		}

	}


	public Composite getView() {
		return view;
	}


	public Post getModel() {
		return post;
	}


	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PersistAndUpdate getPersisterAndUpdater() {
		return persisterAndUpdater;
	}

	@Override
	public void setPersisterAndUpdater(PersistAndUpdate persisterAndUpdater) {
		this.persisterAndUpdater = persisterAndUpdater;
	}

}
