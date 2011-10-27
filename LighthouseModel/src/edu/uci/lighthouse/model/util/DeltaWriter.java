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
package edu.uci.lighthouse.model.util;

import java.io.OutputStream;
import java.io.PrintWriter;

import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseEvent;

public class DeltaWriter {

	public PrintWriter out;

	public DeltaWriter(OutputStream stream) {
		this.out = new PrintWriter(stream);
	}	
	
	public void flush() {
		out.flush();
	}

	public void close() {
		out.close();
	}
	
	public void write(LighthouseDelta delta) {
		out.println("Events:");
		for (LighthouseEvent event: delta.getEvents()) {
			out.println(event);
		}
	}
	
}
