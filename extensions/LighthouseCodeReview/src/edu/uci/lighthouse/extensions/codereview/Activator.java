package edu.uci.lighthouse.extensions.codereview;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.uci.lighthouse.core.dbactions.DatabaseActionsBuffer;
import edu.uci.lighthouse.extensions.codereview.dbactions.FetchNewReviewsAction;
import edu.uci.lighthouse.extensions.codereview.dbactions.RefreshCurrentReviewsAction;
import edu.uci.lighthouse.extensions.codereview.ui.ICodeReviewImages;

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

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		reg.put(ICodeReviewImages.IMG_REVIEW, ImageDescriptor.createFromFile(getClass(), "/icons/review.gif"));
		reg.put(ICodeReviewImages.IMG_SELECTION, ImageDescriptor.createFromFile(getClass(), "/icons/selection.png"));
		reg.put(ICodeReviewImages.IMG_ME, ImageDescriptor.createFromFile(getClass(), "/icons/me.png"));
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
