package edu.uci.lighthouse.core.listeners;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.tigris.subversion.svnclientadapter.ISVNInfo;

/**
 * This interface is used for implementers receive events related to subversion.
 * 
 * @author tproenca
 * @see JavaFileChangedReporter
 */
public interface ISVNEventListener {

	/**
	 * Is called when the user checkout some file. Returns a map associating the
	 * file with the SVN related information.
	 * 
	 * @param svnFiles
	 *            a map associating a <code>IFile</code> with
	 *            <code>ISVNInfo</code>
	 */
	public void checkout(Map<IFile, ISVNInfo> svnFiles);

	/**
	 * Is called when the user update some file. Returns a map associating the
	 * file with the SVN related information.
	 * 
	 * @param svnFiles
	 *            a map associating a <code>IFile</code> with
	 *            <code>ISVNInfo</code>
	 */
	public void update(Map<IFile, ISVNInfo> svnFiles);

	/**
	 * Is called when the user commit some file. Returns a map associating the
	 * file with the SVN related information.
	 * 
	 * @param svnFiles
	 *            a map associating a <code>IFile</code> with
	 *            <code>ISVNInfo</code>
	 */
	public void commit(Map<IFile, ISVNInfo> svnFiles);

}
