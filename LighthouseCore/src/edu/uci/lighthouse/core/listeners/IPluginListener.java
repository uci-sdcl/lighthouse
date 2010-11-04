package edu.uci.lighthouse.core.listeners;

import org.osgi.framework.BundleContext;

public interface IPluginListener {
	/**
	 * This method is called upon plug-in activation
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin;
	 */
	public void start(BundleContext context) throws Exception;

	/**
	 * This method is called when the plug-in is stopped
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin;
	 */
	public void stop(BundleContext context) throws Exception;
}
