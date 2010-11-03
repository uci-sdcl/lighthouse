package edu.uci.lighthouse.core.preferences;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import edu.uci.lighthouse.core.dbactions.JobDecoratorAction;
import edu.uci.lighthouse.core.dbactions.pull.SynchronizeModelAction;
import edu.uci.lighthouse.core.util.WorkbenchUtility;
import edu.uci.lighthouse.model.util.DatabaseUtility;

public class LighthousePreferences extends PreferencePage implements
		IWorkbenchPreferencePage {

	private static final String ICON = "$nl$/icons/full/obj16/refresh_tab.gif";
	private Button btSynchronizeModel;
	private Image btImage;

	private static Logger logger = Logger
			.getLogger(LighthousePreferences.class);

	@Override
	public void init(IWorkbench workbench) {
		noDefaultAndApplyButton();
		setDescription("Expand the tree to edit preferences for a specific feature.");
	}

	@Override
	protected Control createContents(Composite parent) {
		btImage = AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.debug.ui", ICON).createImage();

		btSynchronizeModel = new Button(parent, SWT.PUSH);
		btSynchronizeModel.setText("Synchronize model with database");
		btSynchronizeModel.setImage(btImage);
		btSynchronizeModel.setEnabled(canConnect());
		btSynchronizeModel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				getShell().close();
				JobDecoratorAction action = new JobDecoratorAction(
						new SynchronizeModelAction(WorkbenchUtility
								.getSVNInfoFromWorkspace()),
						"Synchronize Model",
						"Synchronizing model with database...");
				action.run();
			}
		});
		return btSynchronizeModel;
	}

	@Override
	public void dispose() {
		btImage.dispose();
		btImage = null;
		super.dispose();
	}

	private boolean canConnect() {
		boolean result = false;
		try {
			DatabaseUtility.canConnect(DatabasePreferences
					.getDatabaseSettings());
			result = true;
		} catch (SQLException e) {
			logger.error(e);
		}
		return result;
	}

}
