package edu.uci.lighthouse.core.listeners;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import edu.uci.lighthouse.core.parser.IParserAction;
import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.model.LighthouseFile;

/**
 * @author truizlop
 */
public class FileChangedListener implements IResourceChangeListener{

	// TODO - BUG - imagine a file that does not compile when you are opening Eclipse
	private HashMap<IFile, LighthouseFile> mapOpenFiles;
	
	public FileChangedListener(IWorkbenchWindow window) {		
		mapOpenFiles = new HashMap<IFile, LighthouseFile>();
		
		IElementChangedListener listener = new OpenFileListener(mapOpenFiles);
		JavaCore.addElementChangedListener(listener);
		IWorkbenchPage[] pages = window.getPages();		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();		
		for(IWorkbenchPage p : pages) {
			try{
				IEditorReference[] editors = p.getEditorReferences();
				for(IEditorReference ref : editors){
					IPath relativePath = new Path(ref.getEditorInput().getToolTipText());
					final IFile javaFile =  workspace.getRoot().getFile(relativePath);
					if(javaFile.getFileExtension().equalsIgnoreCase("java")) {
						// FIXME check if file compiles
						final LighthouseFile lighthouseFile = new LighthouseFile();
						LighthouseParser parser = new LighthouseParser();
						parser.executeInAJob(lighthouseFile, javaFile, new IParserAction() {
							@Override
							public void doAction() {
								mapOpenFiles.put(javaFile,lighthouseFile);							
							}
						}); // end-of-executeInAJob
					}
				}
			}catch(Exception e){
				e.printStackTrace(); // TODO handle Exception
			}
		} // end-of-for
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {		
		if(event.getType() == IResourceChangeEvent.POST_CHANGE){
			IResourceDelta rootDelta = event.getDelta();
			IResourceDeltaVisitor visitor = new FileChangedVisitor(mapOpenFiles);			
			try{
				rootDelta.accept(visitor);
			}catch(Exception e){
				e.printStackTrace(); // TODO handle Exception
			}
		}		
	}
	
}
