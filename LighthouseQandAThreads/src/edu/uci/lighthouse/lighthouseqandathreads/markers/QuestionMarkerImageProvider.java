package edu.uci.lighthouse.lighthouseqandathreads.markers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.internal.ide.IMarkerImageProvider;

public class QuestionMarkerImageProvider implements IMarkerImageProvider{

	public static String ID = "edu.uci.lighthouse.LighthouseQandAThreads.imageprovider";
	
	
	public String getImagePath(IMarker marker) {
		return "/icons/question.png";
	}

}
