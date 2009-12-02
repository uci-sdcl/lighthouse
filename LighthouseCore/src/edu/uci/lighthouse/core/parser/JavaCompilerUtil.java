package edu.uci.lighthouse.core.parser;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.compiler.IProblem;

public class JavaCompilerUtil {
	
	private static Logger logger = Logger.getLogger(JavaCompilerUtil.class);

	@Deprecated
	public static boolean hasErrors(ICompilationUnit icu){		
		WorkingCopyProblemRequestor workingCopy = new WorkingCopyProblemRequestor();
		try {
			icu.getWorkingCopy(workingCopy, null);
		} catch (JavaModelException e) {
			logger.error(e);
		}
		return workingCopy.hasProblems;
	}
	
	public static boolean hasErrors(IFile iFile){		
		WorkingCopyProblemRequestor workingCopy = new WorkingCopyProblemRequestor();
		try {
			ICompilationUnit icu = JavaCore.createCompilationUnitFrom(iFile);
			
			 // creation of DOM/AST from a ICompilationUnit
//			 ASTParser parser = ASTParser.newParser(AST.JLS3);
//			 parser.setKind(ASTParser.K_COMPILATION_UNIT);
//			 parser.setSource(icu);
//			 CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
//			 astRoot.
//			return astRoot.getMessages().length > 0;
			
			ICompilationUnit icuWorkingCopy = icu.getWorkingCopy(workingCopy, null);
			
			//icuWorkingCopy.discardWorkingCopy();
		} catch (JavaModelException e) {
			logger.error(e);
		}
		return workingCopy.hasProblems;
//			return false;
	}
	
	public static boolean hasErrors2(IFile iFile) {
		boolean result = false;
//		try {
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
//			StandardJavaFileManager fileManager = compiler
//					.getStandardFileManager(diagnostics, null, null);
//			Iterable<? extends JavaFileObject> compilationUnits = fileManager
//					.getJavaFileObjectsFromStrings(Arrays.asList(iFile
//							.getLocation().toOSString()));
//				JavaCompiler.CompilationTask task = compiler.getTask(null,
//					fileManager, diagnostics, null, null, compilationUnits);
//			result = !task.call();
//			
//			for (Diagnostic diagnostic : diagnostics.getDiagnostics()){
//				System.err.println(diagnostic);
//			}
//			
//			fileManager.close();
//		} catch (IOException e) {
//			logger.error(e);
//		}
//			IWorkspace workspace = ResourcesPlugin.getWorkspace();	
//			IProject[] projects = workspace.getRoot().getProjects();
//			IJavaProject jProject = JavaCore.create(projects[0]);
//			jProject.
			
			
			try {
				IJavaElement javaFile = JavaCore.create(iFile);
//				javaFile.getJavaProject().getProject().build(kind, monitor);
//				String sourcePath = "";
				IClasspathEntry[] classPath = javaFile.getJavaProject().getResolvedClasspath(true);
				StringBuffer classPathBuffer = new StringBuffer(".");
				for (IClasspathEntry iClasspathEntry : classPath) {
//					System.err.println(iClasspathEntry.toString());
//					if (iClasspathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE){
//						sourcePath = iClasspathEntry.getPath().toOSString();
//						javaFile.getJavaProject().getProject().
//						System.out.println(iClasspathEntry);
//					}
					if (iClasspathEntry.getEntryKind() != IClasspathEntry.CPE_SOURCE && iClasspathEntry.getExtraAttributes().length == 0 && iClasspathEntry.getSourceAttachmentPath() == null){
						classPathBuffer.append(":"+iClasspathEntry.getPath().toOSString());
						System.err.println(iClasspathEntry.getPath().toOSString());
					}
				}
				IPackageFragmentRoot[] roots = javaFile.getJavaProject().getPackageFragmentRoots();
//				for (int i = 0; i < roots.length; i++) {
////					System.err.println( roots[i].getResource().getLocation().toOSString());
//					System.err.println(roots[i]);
//				}
				
				
				String sourcePath = roots[0].getResource().getLocation().toOSString();
				
				result = compiler.run(null,null, null,"-classpath",classPathBuffer.toString(),"-sourcepath",sourcePath,iFile.getLocation().toOSString()) != 0;
//				System.err.println(System.getProperty("java.class.path"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			result = false;
//		result = compiler.run(null,null, null, iFile.getLocation().toOSString()) != 0;
		return result;
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
