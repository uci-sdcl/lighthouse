package edu.uci.lighthouse.core.listeners;

import java.util.Collections;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import edu.uci.lighthouse.core.controler.PushModel;
import edu.uci.lighthouse.core.parser.IParserAction;
import edu.uci.lighthouse.core.parser.LighthouseParser;
import edu.uci.lighthouse.model.LighthouseDelta;
import edu.uci.lighthouse.model.LighthouseFile;
import edu.uci.lighthouse.model.LighthouseModel;

public class FileChangedVisitor implements IResourceDeltaVisitor{

	private HashMap<IFile, LighthouseFile> mapOpenFiles = null;
		
	public FileChangedVisitor(HashMap<IFile, LighthouseFile> storage) {
		this.mapOpenFiles = storage;
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource res = delta.getResource();		
		if(res.getType() == IResource.FILE && res.getFileExtension().equalsIgnoreCase("java")){
			final IFile javaFilePath = (IFile)res;
			if (containsCompileErros(javaFilePath)) {
				return false;
			}
			if(delta.getKind() == IResourceDelta.ADDED){
				eventAdded(javaFilePath);				
			}else if(delta.getKind() == IResourceDelta.CHANGED) {
				eventChanged(javaFilePath);				
			}else if(delta.getKind() == IResourceDelta.REMOVED){
				eventRemoved(javaFilePath);	
			}	
		}
		return true;
	}

	private void eventChanged(final IFile javaFile) {
		final LighthouseFile lighthouseFile = new LighthouseFile();
		LighthouseParser parser = new LighthouseParser();
		parser.executeInAJob(lighthouseFile, Collections.singleton(javaFile), new IParserAction(){
			@Override
			public void doAction() {
				LighthouseFile oldModel = mapOpenFiles.get(javaFile);
				LighthouseDelta delta = new LighthouseDelta(oldModel, lighthouseFile);
				mapOpenFiles.put(javaFile, lighthouseFile);
				new PushModel(LighthouseModel.getInstance()).execute(delta);
			}
		});
	}
	
	private void eventAdded(IFile javaFile) {
		final LighthouseFile lighthouseFile = new LighthouseFile();
		LighthouseParser parser = new LighthouseParser();
		parser.executeInAJob(lighthouseFile, Collections.singleton(javaFile), new IParserAction() {
			@Override
			public void doAction() {
				LighthouseDelta delta = new LighthouseDelta(null,lighthouseFile);							
				new PushModel(LighthouseModel.getInstance()).execute(delta);
			}
		});
	}
	
	private void eventRemoved(IFile javaFilePath) {
		final LighthouseFile lighthouseFile = new LighthouseFile();
		LighthouseParser parser = new LighthouseParser();
		parser.executeInAJob(lighthouseFile, Collections.singleton(javaFilePath), new IParserAction(){
			@Override
			public void doAction() {
				LighthouseDelta delta = new LighthouseDelta(lighthouseFile, null);
				new PushModel(LighthouseModel.getInstance()).execute(delta);
			}
		});		
	}

	private boolean containsCompileErros(IFile file) {
		// Obtain the AST corresponding to this compilation unit
		// and create a visitor to check if there are compilation errors
		//IPath ipath = new Path(javaFilePath);
		//IWorkspace workspace = ResourcesPlugin.getWorkspace();
		//IFile file =  workspace.getRoot().getFile(ipath);
		
		ICompilationUnit icu = JavaCore.createCompilationUnitFrom(file);
		CompilationUnit unit = parse(icu);
		
		CompilationErrorCheckerVisitor checker = new CompilationErrorCheckerVisitor();
		unit.accept(checker);
		
		if(checker.isError()){
			// TODO Put in a Log File
			System.out.println("There are compilation errors."); // FIXME Remove this later
			return true; // Don't parse
		}
		return false;
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
