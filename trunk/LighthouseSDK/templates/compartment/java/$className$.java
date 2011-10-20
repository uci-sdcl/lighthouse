package $packageName$;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;

import edu.uci.lighthouse.ui.figures.CompartmentFigure;
import edu.uci.lighthouse.ui.figures.ILighthouseClassFigure.MODE;

public class $className$ extends CompartmentFigure {

	public $className$() {
		// Sets the layout manager.
		setLayoutManager(new FlowLayout());
	}

	@Override
	public boolean isVisible(MODE mode) {
		// Sets visibility
		return true;
	}

	@Override
	public void populate(MODE mode) {
		// Creates a new label
		add(new Label("$labelText$"));
	}

}
