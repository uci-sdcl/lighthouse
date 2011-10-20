package edu.uci.lighthouse.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class LighthousePerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addView("LighthouseUI.EmergingDesignView",IPageLayout.TOP,0.50f,layout.getEditorArea());
		//layout.addView("LighthouseUI.ImpactView",IPageLayout.BOTTOM,0.20f,layout.getEditorArea());
	}

}
