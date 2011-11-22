package edu.uci.lighthouse.extensions.codereview;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.uci.lighthouse.core.dbactions.DatabaseActionsBuffer;
import edu.uci.lighthouse.extensions.codereview.dbactions.FetchNewReviewsAction;
import edu.uci.lighthouse.extensions.codereview.dbactions.RefreshCurrentReviewsAction;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "edu.uci.lighthouse.extensions.codereview"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		DatabaseActionsBuffer.getInstance().offer(new FetchNewReviewsAction());
		DatabaseActionsBuffer.getInstance().offer(new RefreshCurrentReviewsAction());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
