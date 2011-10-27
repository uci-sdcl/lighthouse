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
package edu.uci.lighthouse.lighthouseqandathreads.markers;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;

public class CallBackMarkerCreator {

	private int charStart;
	private int charFinish;
	private String message;
	private int startLine;
	private IFile file;
	private IEditorPart part;
	private CompilationUnitEditor javaEditor;
	
	public CallBackMarkerCreator(int charStart, int charFinish, String message, int startLine, IFile file, IEditorPart part, CompilationUnitEditor javaEditor){
		this.charFinish = charFinish;
		this.charStart = charStart;
		this.setMessage(message);
		this.startLine = startLine;
		this.file = file;
		this.part = part;
		this.javaEditor = javaEditor;
	}
	
	public void run(){
		IMarker marker;
		try {
			marker = file.createMarker("edu.uci.lighthouse.LighthouseQandAThreads.customMarker");
			marker.setAttribute(IMarker.MESSAGE, getMessage());
			marker.setAttribute(IMarker.LOCATION, "line "+startLine);
			marker.setAttribute(IMarker.CHAR_START, charStart);
			marker.setAttribute(IMarker.CHAR_END, charFinish);
			
			
			
			
		      IDocumentProvider documentProvider = javaEditor.getDocumentProvider();
		      IDocument document = documentProvider.getDocument(part.getEditorInput());
		      IAnnotationModel annoModel = documentProvider.getAnnotationModel(part.getEditorInput());
		      
		      
		      SimpleMarkerAnnotation ma = new SimpleMarkerAnnotation(
						"com.ibm.example.myannotation",marker);

		      annoModel.connect(document);
		      annoModel.addAnnotation(ma,new Position(charStart,charFinish - charStart));
		      annoModel.disconnect(document);
			
			
			
		
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private String getMessage() {
		return message;
	}
}
