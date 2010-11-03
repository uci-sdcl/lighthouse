package edu.uci.lighthouse.core.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.menus.AbstractWorkbenchTrimWidget;
import org.eclipse.ui.menus.IWorkbenchWidget;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.core.util.WorkbenchUtility;

/**
 * Signalizes whether lighthouse has connection with database or not 
 */
public class StatusWidget extends AbstractWorkbenchTrimWidget implements
		IWorkbenchWidget {

	private Label imageLabel;
	private Composite composite;
	private Image dbOk;
	private Image dbError;
	private static StatusWidget instance;
	
	@Override
	public void dispose() {
		if (composite != null && !composite.isDisposed()){
			composite.dispose();
		}
		composite = null;
	}

	@Override
	public void init(IWorkbenchWindow workbenchWindow) {
		super.init(workbenchWindow);
		dbOk = AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.debug.ui", "$nl$/icons/full/obj16/debugtt_obj.gif").createImage();
		dbError = AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.debug.ui", "$nl$/icons/full/obj16/threadt_obj.gif").createImage();
		instance = this;
	}

	@Override
	public void fill(Composite parent, int oldSide, int newSide) {
		composite = new Composite(parent, SWT.NONE);
		
		FillLayout layout = new FillLayout();
		layout.marginHeight = 4;
		layout.marginWidth  = 2;
		composite.setLayout(layout);
		
		imageLabel = new Label(composite, SWT.NONE);
		imageLabel.setImage(dbOk);
		imageLabel.setToolTipText("Connected");
		imageLabel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				WorkbenchUtility.openPreferences();
			}});
	}
	
	public void setStatus(final IStatus status){
		Display.getDefault().syncExec(new Runnable(){
		@Override
		public void run() {
			switch (status.getSeverity()) {
			case IStatus.OK:
				imageLabel.setImage(dbOk);
				imageLabel.setToolTipText("Connected");
				break;

			case IStatus.CANCEL:
				imageLabel.setImage(dbError);
				imageLabel.setToolTipText("Not connected");
				break;
			}
		}
	});
	}
	
	public static StatusWidget getInstance(){
		return instance;
	}

}
