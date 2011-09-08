package edu.uci.lighthouse.lighthouseqandathreads.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.lighthouseqandathreads.Activator;
import edu.uci.lighthouse.lighthouseqandathreads.view.CompartmentViews.CompartmentThreadView;

public class PinMenuAction extends Action{
	
	private static final String DESCRIPTION = "Pin";
	private static final String DESCRIPTION2 = "Unpin";
	private CompartmentThreadView view;
	
	public PinMenuAction(CompartmentThreadView view){
		init();
		this.setText("pin window");
		this.view = view;
	}

	protected void init() {
		setToolTipText(DESCRIPTION);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		"/icons/pin.png"));

	}
	
	public void run(){
		view.flipPin();
		if(view.isPin()){
			setToolTipText(DESCRIPTION2);
			setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
			"/icons/unpin.png"));
		}else{
			setToolTipText(DESCRIPTION);
			setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
			"/icons/pin.png"));
		}
		
	}
}
