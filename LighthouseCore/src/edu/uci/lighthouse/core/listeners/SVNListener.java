package edu.uci.lighthouse.core.listeners;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.tigris.subversion.subclipse.core.SVNProviderPlugin;
import org.tigris.subversion.subclipse.core.client.IConsoleListener;
import org.tigris.subversion.subclipse.ui.SVNUIPlugin;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.SVNNodeKind;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

public class SVNListener implements IConsoleListener{

	private Collection<File> files = new LinkedList<File>();
	private Collection<ISVNEventListener> listeners = new LinkedList<ISVNEventListener>();
	private IConsoleListener pluginConsoleListener;
	private int command;
	
	private static Logger logger = Logger.getLogger(SVNListener.class);
	
	public SVNListener(){
		/*
		 * Starts the SVNUIPlugin. This is required because we want the
		 * ConsoleListener instance for decorating it. If this is not done, we
		 * cannot guarantee that Lighthouse Core plug-in will start after
		 * subclipse and hence, we can get a null pointer.
		 */
		SVNUIPlugin.getPlugin();
		pluginConsoleListener = SVNProviderPlugin.getPlugin().getConsoleListener();
		logger.debug("ConsoleListener: "+pluginConsoleListener);
		SVNProviderPlugin.getPlugin().setConsoleListener(this);		
	}

	@Override
	public void logCommandLine(String arg0) {
		pluginConsoleListener.logCommandLine(arg0);
		logger.debug("logCommandLine: "+arg0);	
	}

	@Override
	public void logCompleted(String arg0) {
		pluginConsoleListener.logCompleted(arg0);
		logger.debug("logCompleted: "+arg0);
		
		try {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			ISVNClientAdapter svnAdapter = SVNProviderPlugin.getPlugin().getSVNClient();
			SVNRevision svnRevision = SVNRevision.getRevision(parseRevision(arg0));
			
			for (File file : files) {
				IFile iFile = workspace.getRoot().getFileForLocation(Path.fromOSString(file.getPath()));
				ISVNInfo svnInfo = svnAdapter.getInfo(gerSVNUrlFromFile(file), svnRevision, svnRevision);				
				
				switch (command) {
				case Command.CHECKOUT:
					fireCheckout(iFile, svnInfo);
					break;

				case Command.UPDATE/*|Command.REVERT*/:
					fireUpdate(iFile, svnInfo);
					break;
					
				case Command.COMMIT:
					fireCommit(iFile, svnInfo);
					break;
				}
				
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	public void logError(String arg0) {
		pluginConsoleListener.logError(arg0);
		logger.debug("logError: "+arg0);
	}

	@Override
	public void logMessage(String arg0) {
		pluginConsoleListener.logMessage(arg0);
		logger.debug("logMessage: "+arg0);
		// Lighthouse
	}

	@Override
	public void logRevision(long arg0, String arg1) {
		pluginConsoleListener.logRevision(arg0, arg1);
		logger.debug("logRevision: "+arg0);
	}

	@Override
	public void onNotify(File file, SVNNodeKind kind) {
		pluginConsoleListener.onNotify(file, kind);
		logger.debug("onNotify: "+file + " ("+kind+")");
		
		if(kind == SVNNodeKind.FILE && file.getAbsolutePath().endsWith(".java")){
			files.add(file);
		}
	}

	@Override
	public void setCommand(int arg0) {
		pluginConsoleListener.setCommand(arg0);
		logger.debug("setCommand: "+arg0);
		
		command = arg0;
		files.clear();
	}
	
	public void addSVNEventListener(ISVNEventListener listener){
		listeners.add(listener);
	}
	
	public void removeSVNEventListener(ISVNEventListener listener){
		listeners.remove(listener);
	}
	
	protected void fireCheckout(IFile iFile, ISVNInfo info){				
		logger.info("checkout: "+iFile.getName()+" "+info.getLastChangedRevision()+" "+info.getLastChangedDate());
		for (ISVNEventListener listener : listeners) {
			listener.checkout(iFile, info);
		}
	}
	
	protected void fireCommit(IFile iFile, ISVNInfo info){
		logger.info("commit: "+iFile.getName()+" "+info.getLastChangedRevision()+" "+info.getLastChangedDate());
		for (ISVNEventListener listener : listeners) {
			listener.commit(iFile, info);
		}		
	}

	protected void fireUpdate(IFile iFile, ISVNInfo info){
		logger.info("update: "+iFile.getName()+" "+info.getLastChangedRevision()+" "+info.getLastChangedDate());
		for (ISVNEventListener listener : listeners) {
			listener.update(iFile, info);
		}		
	}
	
	private String parseRevision(String arg0){
		String result = null;
		String[] tokens = arg0.split("[\\s\\.]");
		for (String token : tokens) {
			if (token.matches("\\d+")){
				result = token;
				break;
			}
		}
		return result;
	}
	
	private SVNUrl gerSVNUrlFromFile(File file) {
		SVNUrl result = null;
		try {
			ISVNClientAdapter svnAdapter = SVNProviderPlugin.getPlugin().getSVNClient();
			 ISVNInfo info = svnAdapter.getInfo(file);
			 result = info.getUrl();
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}
	
//	class JavaFileInfo implements ISVNJavaFileInfo {
//
//		private ISVNInfo svnInfo;
//		private IFile iFile;
//		private String fqn;
//		
//		public JavaFileInfo(IFile iFile, ISVNInfo svnInfo){
//			this.iFile = iFile;
//			this.svnInfo = svnInfo;
//		}
//			
//		@Override
//		public Date getCommitDate() {
//			return svnInfo.getLastChangedDate();
//		}
//
//		@Override
//		public String getFullyQualifiedName() {
//			if (fqn == null) {
//				try {
//					LighthouseFile lighthouseFile = new LighthouseFile();
//					LighthouseParser parser = new LighthouseParser();
//					parser.executeInAJob(lighthouseFile, Collections.singleton(iFile), new IParserAction(){
//						@Override
//						public void doAction() {
//				
//						}
//					});	
//					
//					//
//					ICompilationUnit icu = JavaCore
//							.createCompilationUnitFrom(iFile);
//					icu.open(null);
//					IType[] types = icu.getTypes();
//					for (IType iType : types) {
//						String fileNameWithoutExtension = iFile.getName().replaceAll(".java", "");
//						String className = iType.getFullyQualifiedName().replaceAll("\\w+\\.", "");
//						if (fileNameWithoutExtension.equals(className)){
//							// Java files should have at least one class with the same name of the file
//							fqn = iType.getFullyQualifiedName();
//							break;
//						}
//					}
//				} catch (Exception e) {
//					logger.error(e);
//				}
//			}
//			return fqn;
//		}
//
//		@Override
//		public IFile getIFile() {
//			return iFile;
//		}
//
//		@Override
//		public long getRevision() {
//			return svnInfo.getLastChangedRevision().getNumber();
//		}
//		
//		@Override
//		public String toString() {
//			return iFile.getName() + " Revision: " + getRevision() + " Modified: "+getCommitDate()+" fqn: "+getFullyQualifiedName();
//		}
//	}
//	
//	class FileInfo implements ISVNJavaFileInfo {
//		private File file;
//		private Date commitDate;
//		private IFile iFile;
//		private SVNRevision revision;
//		
//		
//		public FileInfo(File file, SVNRevision revision/*, ISVNLogMessage logMessage*/){
//			this.file = file;
//			this.revision = revision;
//		}
//		
//		@Override
//		public Date getCommitDate() {
//			if (commitDate == null) {
//				ISVNLogMessage logMessage = getLogMessage();
//				if (logMessage != null){
//					commitDate = logMessage.getDate();
//				}
//			}
//			return commitDate;
//		}
//
//		@Override
//		public IFile getIFile() {
//			if (iFile == null){
//				IWorkspace workspace = ResourcesPlugin.getWorkspace();
//				iFile = workspace.getRoot().getFile(Path.fromOSString(file.getAbsolutePath()));
//			}			
//			return iFile;		
//		}
//
//		@Override
//		public long getRevision() {
//			return ((SVNRevision.Number)revision).getNumber();
//		}
//
//		@Override
//		public String toString() {
//			return file.getName() + " Revision: " + getRevision() + " Last Modified: "+getCommitDate();
//		}
//		
//		public ISVNLogMessage getLogMessage(){
//			ISVNLogMessage result = null;
//			try {
//				ISVNClientAdapter svnAdapter = SVNProviderPlugin.getPlugin().getSVNClient();
//				ISVNLogMessage[] logMessages = svnAdapter.getLogMessages(file,revision,revision);
//				result = logMessages.length > 0 ? logMessages[0] : null; 
//			} catch (Exception e) {
//				logger.error(e);
//			}
//			return result;
//		}
//
//		@Override
//		public String getFullyQualifiedName() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//	}
}
