package edu.uci.lighthouse.core.parser;

import java.io.IOException;
import java.util.Arrays;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.compiler.IProblem;

public class JavaCompilerUtil {
	
	private static Logger logger = Logger.getLogger(JavaCompilerUtil.class);

	public static boolean hasErrors(ICompilationUnit icu){		
		WorkingCopyProblemRequestor workingCopy = new WorkingCopyProblemRequestor();
		try {
//			ICompilationUnit icu = JavaCore.createCompilationUnitFrom(iFile);
//			logger.debug("ICU="+icu);
			icu.getWorkingCopy(workingCopy, null);
//			workingCopyIcu.reconcile(ICompilationUnit.NO_AST,true, null,null);
		} catch (JavaModelException e) {
			logger.error(e);
		}
		return workingCopy.hasProblems;
	}
	
	public static boolean hasErrors(IFile iFile){		
//		WorkingCopyProblemRequestor workingCopy = new WorkingCopyProblemRequestor();
//		try {
//			ICompilationUnit icu = JavaCore.createCompilationUnitFrom(iFile);
//			logger.debug("ICU="+icu);
//			icu.getWorkingCopy(workingCopy, null);
//		} catch (JavaModelException e) {
//			logger.error(e);
//		}
//		return workingCopy.hasProblems;
	    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
	    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
	    Iterable<? extends JavaFileObject> compilationUnits = fileManager
	        .getJavaFileObjectsFromStrings(Arrays.asList(iFile.getLocation().toOSString()));
	    JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null,
	        null, compilationUnits);
	    boolean success = task.call();
	    try {
			fileManager.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return !success;
	}
}

class WorkingCopyProblemRequestor extends WorkingCopyOwner implements IProblemRequestor {
	
	boolean hasProblems = false;
	
	@Override
	public void acceptProblem(IProblem problem) {
		if (problem.isError()) {
			hasProblems = true;
		}
	}
	
	@Override
	public IProblemRequestor getProblemRequestor(
			ICompilationUnit workingCopy) {
		return this;
	}

	@Override
	public void beginReporting() {
	}

	@Override
	public void endReporting() {
	}

	@Override
	public boolean isActive() {
		return true;
	}		
}
