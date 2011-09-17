package edu.uci.lighthouse.lighthouseqandathreads.markers;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;

public class CallBackMarkerCreator {

	private int charStart;
	private int charFinish;
	private String message;
	private int startLine;
	private IFile file;
	
	public CallBackMarkerCreator(int charStart, int charFinish, String message, int startLine, IFile file){
		this.charFinish = charFinish;
		this.charStart = charStart;
		this.setMessage(message);
		this.startLine = startLine;
		this.file = file;
	}
	
	public void run(){
		IMarker marker;
		try {
			marker = file.createMarker("edu.uci.lighthouse.LighthouseQandAThreads.customMarker");
			marker.setAttribute(IMarker.MESSAGE, getMessage());
			marker.setAttribute(IMarker.LOCATION, "line "+startLine);
			marker.setAttribute(IMarker.CHAR_START, charStart);
			marker.setAttribute(IMarker.CHAR_END, charFinish);
		
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
