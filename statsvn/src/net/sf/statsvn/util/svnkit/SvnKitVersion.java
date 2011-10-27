/*******************************************************************************
* Copyright (c) {2009,2011} {Software Design and Collaboration Laboratory (SDCL)
*				, University of California, Irvine}.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    {Software Design and Collaboration Laboratory (SDCL)
*	, University of California, Irvine}
*			- initial API and implementation and/or initial documentation
*******************************************************************************/
package net.sf.statsvn.util.svnkit;

import net.sf.statsvn.util.ISvnProcessor;
import net.sf.statsvn.util.SvnStartupUtils;
import net.sf.statsvn.util.SvnVersionMismatchException;

/**
 * Runs svn -version using svnkit. (Possible?)
 *  
 * @author jkealey, yogesh
 */
public class SvnKitVersion extends SvnStartupUtils {

    public SvnKitVersion(ISvnProcessor processor) {
        super(processor);
    }

    public SvnKitProcessor getSvnKitProcessor() {
        return (SvnKitProcessor) getProcessor();
    }
    
    public String checkSvnVersionSufficient() throws SvnVersionMismatchException {
        // TODO: Not sure how to implement with svnkit. 
        return "1.4.0";
    }

}
