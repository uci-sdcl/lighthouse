package edu.uci.lighthouse.sdk.wizards;

import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.templates.ITemplateSection;
import org.eclipse.pde.ui.templates.NewPluginTemplateWizard;

import edu.uci.lighthouse.sdk.templates.SimpleViewTemplateSection;

public class SimpleViewTemplateWizard extends NewPluginTemplateWizard {

	protected IFieldData fData;
	
    public void init(IFieldData data) {
        super.init(data);
        fData = data;
        setWindowTitle("Lighthouse Extensions Wizard");      
    }

    public ITemplateSection[] createTemplateSections() {
        return new ITemplateSection[] {new SimpleViewTemplateSection()};
    }

}
