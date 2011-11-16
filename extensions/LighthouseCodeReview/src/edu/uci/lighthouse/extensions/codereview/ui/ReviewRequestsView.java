package edu.uci.lighthouse.extensions.codereview.ui;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

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
