package edu.uci.lighthouse.sdk.templates;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelFactory;
import org.eclipse.pde.core.plugin.IPluginReference;
import org.eclipse.pde.internal.ui.IHelpContextIds;
import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.templates.OptionTemplateSection;
import org.eclipse.pde.ui.templates.PluginReference;
import org.eclipse.pde.ui.templates.TemplateOption;

import edu.uci.lighthouse.sdk.Activator;

public class SimpleViewTemplateSection extends OptionTemplateSection {

	private static final String KEY_CLASS_NAME = "className";
	private static final String KEY_LABEL_TEXT = "labelText";
	//private static final String KEY_MESSAGE_NAME = "message";
	//private static final String KEY_IMP_MESSAGE_NAME = "importantMessage";
	private String packageName = null;
	
	public SimpleViewTemplateSection() {
		super();
		setPageCount(1);
		createOptions();
	}
	
	private void createOptions() {
		addOption(KEY_CLASS_NAME, "Class name:", "SimpleCompartmentFigure", 0);
		addOption(KEY_LABEL_TEXT, "Label text:", "Hello world!", 0);
		//addOption(KEY_MESSAGE_NAME, "Message", "Hello World", 0);
		//addOption(KEY_IMP_MESSAGE_NAME, "Important?", false, 0);
	}
	
	@Override
	protected String getTemplateDirectory() {
		// TODO Auto-generated method stub
		return "templates";
	}

	public void addPages(Wizard wizard) {
		WizardPage page = createPage(0, IHelpContextIds.TEMPLATE_INTRO);
		page.setTitle("Lighthouse CompartmentFigure Template");
		page.setDescription("Creates a simple CompartmentFigure to demonstrate...");
		wizard.addPage(page);
		markPagesAdded();
	}
	
	/**
	 * This is the folder relative to your install url and template directory
	 * where the templates are stored.
	 */
	public String getSectionId() {
		return "compartment";
	}
	
	protected void initializeFields(IFieldData data) {
		String id = data.getId();
		initializeOption(KEY_PACKAGE_NAME, getFormattedPackageName(id));
		this.packageName = getFormattedPackageName(id);
	}

	/**
	 * Validate your options here!
	 */
	public void validateOptions(TemplateOption changed) {
		// TODO Auto-generated method stub

	}


	protected void updateModel(IProgressMonitor monitor) throws CoreException {
		IPluginBase plugin = model.getPluginBase();
		IPluginModelFactory factory = model.getPluginFactory();
        		
		// org.eclipse.core.runtime.applications
		IPluginExtension extension = createExtension(getUsedExtensionPoint(), true);
		
		IPluginElement element = factory.createElement(extension);
		element.setName("compartment");
		String fullClassName = 
			getStringOption(KEY_PACKAGE_NAME)+"."+getStringOption(KEY_CLASS_NAME);
		
		element.setAttribute("class", fullClassName);
		extension.add(element);
		
		plugin.add(extension);
	}

	public String getUsedExtensionPoint() {
		return "edu.uci.lighthouse.ui.figures.compartment";
	}

	/**
	 * The location of your plugin supplying the template content
	 */
	protected URL getInstallURL() {
		return Activator.getDefault().getBundle().getEntry("/");
	}

	protected ResourceBundle getPluginResourceBundle() {
		return Platform.getResourceBundle(Activator.getDefault().getBundle());
	}

	/**
	 * You can use this method to add files relative to your section id
	 */
	public String[] getNewFiles() {
		return new String[0];
	}
	
	public IPluginReference[] getDependencies(String schemaVersion) {
		ArrayList<IPluginReference> result = new ArrayList<IPluginReference>();

		result.add(new PluginReference("org.eclipse.draw2d", null, 0)); //$NON-NLS-1$
		result.add(new PluginReference("edu.uci.lighthouse.ui", null, 0)); //$NON-NLS-1$
        result.add(new PluginReference("org.eclipse.core.runtime", null, 0)); //$NON-NLS-1$
        result.add(new PluginReference("org.eclipse.ui", null, 0)); //$NON-NLS-1$

		return (IPluginReference[]) result.toArray(
				new IPluginReference[result.size()]);
	}
	
	public boolean isDependentOnParentWizard() {
		return true;
	}
	
	public int getNumberOfWorkUnits() {
		return super.getNumberOfWorkUnits() + 1;
	}
	
	public String getStringOption(String name) {
		if (name.equals(KEY_PACKAGE_NAME)) {
			return packageName;
		}
		return super.getStringOption(name);
	}
	
	protected String getFormattedPackageName(String id){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < id.length(); i++) {
			char ch = id.charAt(i);
			if (buffer.length() == 0) {
				if (Character.isJavaIdentifierStart(ch))
					buffer.append(Character.toLowerCase(ch));
			} else {
				if (Character.isJavaIdentifierPart(ch) || ch == '.')
					buffer.append(ch);
			}
		}
		return buffer.toString().toLowerCase(Locale.ENGLISH);
	}
	

	public String getDescription() {
		return "Creates a simple CompartmentFigure";
	}

	public String getLabel() {
		return "Simple View";
	}

}
