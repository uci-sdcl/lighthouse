package editParts;

import java.util.Observer;
import java.util.Observable;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.SelectEditPartTracker;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public abstract class AppAbstractEditPart extends AbstractGraphicalEditPart implements Observer {
	public void activate() {
		super.activate();
		((Observable)getModel()).addObserver(this);
		}

	public void deactivate(){
		super.deactivate();
		((Observable)getModel()).deleteObserver(this);
	}

}
