package edu.uci.lighthouse.ui.views.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;


public class ZoomDropDownAction extends Action implements IMenuCreator{

	private Menu menu;
	private IContributionItem item;
	
	private static final String ICON = "icons/full/etool16/insp_sbook.gif";
	private static final String DESCRIPTION = "Zoom";
	
	public ZoomDropDownAction(IZoomableWorkbenchPart part){
		super(null,Action.AS_DROP_DOWN_MENU);
		init();
		item = new ZoomContributionViewItem(part);
		setMenuCreator(this);
	}
	
	private void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.jdt.debug.ui", ICON));
	}
	
	@Override
	public void dispose() {
		if (menu != null){
			menu.dispose();
			menu = null;
		}
	}

	@Override
	public Menu getMenu(Control parent) {
		if (menu == null) {
			menu = new Menu(parent);
			item.fill(menu, -1);
		}
		return menu;
	}

	@Override
	public Menu getMenu(Menu parent) {
		return null;
	}

}
