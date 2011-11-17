package edu.uci.lighthouse.extensions.codereview.ui;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeSelection;
import edu.uci.lighthouse.extensions.codereview.model.Comment;
import edu.uci.lighthouse.extensions.codereview.model.FileSnapshot;
import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.jpa.AbstractDAO;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.JPAUtility;
import edu.uci.lighthouse.model.jpa.LHAuthorDAO;

/**
 * View that shows all requests for review.
 * 
 * @author Tiago Proenca (tproenca@gmail.com)
 */
public class ReviewRequestsView extends ViewPart {

	private TableViewer viewer;

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		// viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		
//		AbstractNotification n = new AbstractNotification(){

//		Notifications.getService().notify(  
//				  Collections.singletonList(n)) ; 
		RequestNotification n = new RequestNotification(parent.getDisplay());
		n.create();
		n.open();
		
		try {
			List<LighthouseAuthor> authors = new LHAuthorDAO().list();
			Comment comment = new Comment(authors.get(0), "comment1");
//			AbstractDAO<Comment, Date> commentDAO = new AbstractDAO<Comment, Date>(){};
//			Comment comment2 =  commentDAO.save(comment);
			
//			EntityManager entityManager = JPAUtility.createEntityManager();
//			JPAUtility.beginTransaction(entityManager);
//			entityManager.persist(comment);
//			JPAUtility.commitTransaction(entityManager);
////			JPAUtility.closeEntityManager(entityManager);
//			System.out.println("Datetime:"+comment.getTimestamp()+" Id:"+comment.getId());
//			comment.setContent("Tiago");
////			entityManager = JPAUtility.createEntityManager();
//			JPAUtility.beginTransaction(entityManager);
////			comment = entityManager.find(Comment.class, comment.getId());
////			entityManager.merge(comment);
//			entityManager.refresh(comment);
//			JPAUtility.commitTransaction(entityManager);
//			JPAUtility.closeEntityManager(entityManager);			
//			
//			System.out.println("Datetime:"+comment.getTimestamp()+" Id:"+comment.getId());
			
			ITextSelection selection = new ITextSelection(){
				@Override
				public boolean isEmpty() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public int getOffset() {
					// TODO Auto-generated method stub
					return 1;
				}

				@Override
				public int getLength() {
					// TODO Auto-generated method stub
					return 5;
				}

				@Override
				public int getStartLine() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public int getEndLine() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public String getText() {
					// TODO Auto-generated method stub
					return "";
				}
			};
			
			CodeSelection codeSelection = new CodeSelection(selection);
			codeSelection.addComment(comment);
			
			FileSnapshot fs = new FileSnapshot();
			fs.addCodeSelection(codeSelection);
			fs.setContent("File Content");
			
			CodeReview creview = new CodeReview(authors.get(0));
			creview.setReviewer(authors.get(0));
			creview.addFileSnapshot(fs);
			
			
			EntityManager entityManager = JPAUtility.createEntityManager();
			JPAUtility.beginTransaction(entityManager);
			entityManager.persist(creview);
			JPAUtility.commitTransaction(entityManager);
			JPAUtility.closeEntityManager(entityManager);
			
		} catch (JPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Comment comment = new Comment();
		
		
		
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private static class ViewContentProvider implements
			IStructuredContentProvider {
		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			// TODO (tproenca): Return the model objects here. Model objects
			// should contain name, timestamp, comment, fqn.
			return new Object[]{""};
		}
	}

	private static class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			// TODO (tproenca): Return the labels here
			return "";
		}

	}

}
