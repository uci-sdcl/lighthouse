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
package edu.uci.lighthouse.views.filters;

import java.io.Serializable;
import java.util.HashSet;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.zest.core.viewers.EntityConnectionData;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.LighthouseRelationship;

public class PackageFilter extends ViewerFilter implements IDataFilter{

	HashSet<String> packagesName = new HashSet<String>();
	
//	public PackageFilter(String packageName){
//		this.packageName = packageName;
//	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof LighthouseClass) {
			LighthouseClass aClass = (LighthouseClass) element;
			for (String packageName : packagesName) {
				if (packageName.equals(aClass.getPackageName())) {
					return true;
				}
			}
		} else if (element instanceof LighthouseRelationship || element instanceof EntityConnectionData){
			return true;
		}
		return false;
	}
	
	public void addPackageName(String packageName){
		packagesName.add(packageName);
	}
	
	public void removePackageName(String packageName){
		packagesName.remove(packageName);
	}
	
	public int numberOfPackages() {
		return packagesName.size();
	}

	@Override
	public Serializable getData() {
		return packagesName;
	}

	@Override
	public void setData(Serializable data) {
		if (packagesName.getClass().isInstance(data)) {
			packagesName = (HashSet<String>) data;
		}
	}
}
