package edu.uci.lighthouse.core.listeners;

import java.util.Collections;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import edu.uci.lighthouse.core.parser.IParserAction;
import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.model.LighthouseFile;

/**
 * @author truizlop
 */
public class OpenFileListener implements IElementChangedListener{

	private HashMap<IFile, LighthouseFile> mapOpenFiles;
	
	public OpenFileListener(HashMap<IFile, LighthouseFile> map){
		mapOpenFiles = map;
	}
	
	@Override
	public void elementChanged(ElementChangedEvent event) {
		IJavaElementDelta delta = event.getDelta();
		traverseDeltaTree(delta);
	}

	private void traverseDeltaTree(IJavaElementDelta delta){
		IJavaElement element = delta.getElement();
		int type = element.getElementType();
		
		if(type == IJavaElement.COMPILATION_UNIT){
			ICompilationUnit icu = (ICompilationUnit) element;
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			
			IPath relativePath = icu.getPath();
			final IFile javaFile =  workspace.getRoot().getFile(relativePath);
			
			if((delta.getFlags() & IJavaElementDelta.F_PRIMARY_WORKING_COPY) != 0){
				if(icu.isWorkingCopy()){
					CompilationUnit unit = parse(icu);
					CompilationErrorCheckerVisitor checker = new CompilationErrorCheckerVisitor();
					unit.accept(checker);
					
					if(!checker.isError()){
						final LighthouseFile lighthouseFile = new LighthouseFile();
						LighthouseParser parser = new LighthouseParser();
						parser.executeInAJob(lighthouseFile, Collections.singleton(javaFile), new IParserAction() {
							@Override
							public void doAction() {
								mapOpenFiles.put(javaFile, lighthouseFile);
							}
						});
					}
				}else{
					CompilationUnit unit = parse(icu);
					CompilationErrorCheckerVisitor checker = new CompilationErrorCheckerVisitor();
					unit.accept(checker);
					
					if(!checker.isError()){
						mapOpenFiles.remove(javaFile.getLocationURI().getPath());
					}
				}
			}
		}else{
			for(IJavaElementDelta child : delta.getChangedChildren()){
				traverseDeltaTree(child);
			}
		}
	}
	
	/**
	 * Obtains a Compilation Unit which follows the Java Language 
	 * Specification 3 and has resolved bindings. 
	 * We need the bindings to be able to get the changes between
	 * two copies of the same class.
	 * 
	 * @param unit
	 * 		Compilation unit provided (we are not sure it has the
	 * 		settings we need).
	 * @return
	 * 		Compilation unit with the settings we need.
	 */
	private CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3); 	// Java Language Specification 3
		parser.setKind(ASTParser.K_COMPILATION_UNIT); 		// We want a CompilationUnit instance
		parser.setSource(unit); 							// Set source
		parser.setResolveBindings(true); 					// We may need bindings later on
		return (CompilationUnit) parser.createAST(null /* IProgressMonitor */); // Parse
	}
}
