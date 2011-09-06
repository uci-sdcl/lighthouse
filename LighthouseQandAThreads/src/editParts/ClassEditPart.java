package editParts;

import java.util.Observable;

import org.eclipse.draw2d.IFigure;

public class ClassEditPart extends AppAbstractEditPart{
	IFigure figure;
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected IFigure createFigure() {
	
		return figure;
	}

	
	public void setView(IFigure figure){
		this.figure = figure;
	}
	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		
	}

}
