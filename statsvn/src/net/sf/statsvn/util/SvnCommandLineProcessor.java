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
package net.sf.statsvn.util;


/**
 * Base processor that uses the command line svn client. 
 * @author jkealey
 *
 */
public class SvnCommandLineProcessor implements ISvnProcessor {

    
    private ISvnDiffProcessor diffProcessorInstance;
    public ISvnDiffProcessor getDiffProcessor()
    {
        if (diffProcessorInstance==null) diffProcessorInstance = new SvnDiffUtils(this);
        return diffProcessorInstance;
    }
    
    private ISvnInfoProcessor infoProcessorInstance;
    public ISvnInfoProcessor getInfoProcessor()
    {
        if (infoProcessorInstance==null) infoProcessorInstance = new SvnInfoUtils(this);
        return infoProcessorInstance;
    }
    
    private ISvnPropgetProcessor propgetProcessorInstance;
    public ISvnPropgetProcessor getPropgetProcessor()
    {
        if (propgetProcessorInstance==null) propgetProcessorInstance = new SvnPropgetUtils(this);
        return propgetProcessorInstance;
    }  
    
    private ISvnVersionProcessor versionProcessorInstance;
    public ISvnVersionProcessor getVersionProcessor()
    {
        if (versionProcessorInstance==null) versionProcessorInstance = new SvnStartupUtils(this);
        return versionProcessorInstance;
    }     
}
