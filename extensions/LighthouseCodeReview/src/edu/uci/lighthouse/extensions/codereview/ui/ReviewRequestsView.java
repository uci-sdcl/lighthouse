package edu.uci.lighthouse.extensions.codereview.ui;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import edu.uci.lighthouse.core.util.ModelUtility;
import edu.uci.lighthouse.extensions.codereview.Activator;
import edu.uci.lighthouse.extensions.codereview.actions.CloseReviewAction;
import edu.uci.lighthouse.extensions.codereview.actions.CollapseAllAction;
import edu.uci.lighthouse.extensions.codereview.actions.ExpandAllAction;
import edu.uci.lighthouse.extensions.codereview.actions.RevieweeFilterAction;
import edu.uci.lighthouse.extensions.codereview.actions.ReviewerFilterAction;
import edu.uci.lighthouse.extensions.codereview.actions.TypeSorterAction;
import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewFacade;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModel;
import edu.uci.lighthouse.extensions.codereview.model.CodeSelection;
import edu.uci.lighthouse.extensions.codereview.model.FileSnapshot;
import edu.uci.lighthouse.extensions.codereview.model.ICodeReviewModelListener;
import edu.uci.lighthouse.extensions.codereview.model.IDatabaseEntry;
import edu.uci.lighthouse.extensions.codereview.util.EditorUtility;

/**
 * View that shows all requests for review.
 * 
 * @author Tiago Proenca (tproenca@gmail.com)
 */
public class ReviewRequestsView extends ViewPart implements
		ICodeReviewModelListener, ISelectionChangedListener {

	// private StyledText chatHistory;
	private TreeViewer viewer;
	private CommentHistory chatHistory;
	private Text chatMessage;
	private Button sendComment;
	
	private Action closeReviewAction;

	private CodeReviewModel model = CodeReviewModel.getInstance();
	private CodeSelection currentSelection = null;
	
	private static Logger logger = Logger.getLogger(ReviewRequestsView.class);

	@Override
	public void createPartControl(Composite parent) {
		FormData fData;
		parent.setLayout(new FormLayout());

		fData = new FormData();
		fData.left = new FormAttachment(50, 0);
		fData.top = new FormAttachment(0, 0);
		fData.bottom = new FormAttachment(100, 0);
		fData.width = 1;
		final Sash sash = new Sash(parent, SWT.BORDER | SWT.VERTICAL);
		final int SASH_LIMIT = 300;
		sash.setLayoutData(fData);
		sash.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Shell shell = sash.getShell();
				Rectangle sashRect = sash.getBounds();
				Rectangle shellRect = shell.getBounds();
				int right = shellRect.width - sashRect.width - SASH_LIMIT;
				e.x = Math.max(Math.min(e.x, right), SASH_LIMIT);
				if (e.x != sashRect.x) {
					FormData data = (FormData) sash.getLayoutData();
					data.left = new FormAttachment(0, e.x);
					sash.getParent().layout();
				}
			}
		});

		fData = new FormData();
		fData.top = new FormAttachment(0, 0);
		fData.left = new FormAttachment(0, 0);
		fData.right = new FormAttachment(sash, 0);
		fData.bottom = new FormAttachment(100, 0);
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		viewer.setContentProvider(new CodeReviewContentProvider());
		viewer.setLabelProvider(new CodeReviewLabelProvider());
		viewer.setSorter(new ViewerSorter());
		viewer.setInput(model);
		viewer.getControl().setLayoutData(fData);
		viewer.addSelectionChangedListener(this);
		viewer.addDoubleClickListener(new IDoubleClickListener(){
			@Override
			public void doubleClick(DoubleClickEvent event) {
				openInEditor(event.getSelection());
			}
		});
		viewer.setComparer(new IElementComparer() {
			@Override
			public int hashCode(Object element) {
				return element.hashCode();
			}

			@Override
			public boolean equals(Object a, Object b) {
				if (a instanceof IDatabaseEntry && b instanceof IDatabaseEntry) {
					return ((IDatabaseEntry) a).getId().equals(
							((IDatabaseEntry) b).getId());
				}
				return a.equals(b);
			}
		});
		
		fData = new FormData();
		fData.top = new FormAttachment(0, 0);
		fData.left = new FormAttachment(sash, 0);
		fData.right = new FormAttachment(100, 0);
		fData.bottom = new FormAttachment(100, 0);
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new FormLayout());
		composite.setLayoutData(fData);

		fData = new FormData();
		fData.top = new FormAttachment(0, 0);
		fData.left = new FormAttachment(0, 5);
		fData.right = new FormAttachment(100, 0);
		fData.bottom = new FormAttachment(100, -40);
		chatHistory = new CommentHistory(composite, SWT.BORDER);
		chatHistory.setLayoutData(fData);

		fData = new FormData();
		fData.top = new FormAttachment(chatHistory, 5);
		fData.left = new FormAttachment(0, 5);
		fData.right = new FormAttachment(100, -40);
		fData.bottom = new FormAttachment(100, -2);
		chatMessage = new Text(composite, SWT.BORDER | SWT.MULTI);
		chatMessage.setLayoutData(fData);
		chatMessage.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					sendComment();
				}
			}
		});

		fData = new FormData();
		fData.top = new FormAttachment(chatHistory, 4);
		fData.left = new FormAttachment(chatMessage, 3);
		fData.right = new FormAttachment(100, 0);
		fData.bottom = new FormAttachment(100, 0);
		sendComment = new Button(composite, SWT.PUSH);
		sendComment.setImage(Activator.getDefault().getImageRegistry()
				.get(ICodeReviewImages.IMG_SELECTION));
		sendComment.setLayoutData(fData);
		sendComment.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				sendComment();
			}
		});

		closeReviewAction = new CloseReviewAction(viewer);
		
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
		toolbarManager.add(new CollapseAllAction(viewer));
		toolbarManager.add(new ExpandAllAction(viewer));
		toolbarManager.add(new TypeSorterAction(viewer));
//		toolbarManager.add(new Separator());
		toolbarManager.add(new RevieweeFilterAction(viewer));
		toolbarManager.add(new ReviewerFilterAction(viewer));
		
		hookContextMenu();
		
		model.addCodeReviewModelListener(this);
		getSite().setSelectionProvider(viewer);
//		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				if (obj instanceof CodeReview) {
					closeReviewAction.setEnabled(ModelUtility.getAuthor().equals(((CodeReview)obj).getReviewee()));
					manager.add(closeReviewAction);
				}
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}
	
	private void sendComment() {
		String comment = chatMessage.getText();
		if (currentSelection != null && !"".equals(comment.trim())) {
			CodeReviewFacade crf = new CodeReviewFacade();
			crf.addComment(currentSelection, comment);
			chatMessage.setText("");
		}
	}

	@Override
	public void setFocus() {
		// viewer.getControl().setFocus();
	}

	@Override
	public void added(Collection<CodeReview> reviews) {
		refreshView(reviews);
	}

	@Override
	public void changed(Collection<CodeReview> reviews) {
		refreshView(reviews);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				refreshHistory(viewer.getSelection());
			}
		});
	}

	@Override
	public void removed(Collection<CodeReview> reviews) {
		refreshView(reviews);
	}

	private void refreshView(final Collection<CodeReview> reviews) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
//				if (viewer.getTree().getItems().length == 0) {
//					viewer.setInput(model);
//				} else {
//
//					for (CodeReview r : reviews) {
//						viewer.refresh(r, false);
//					}
//				}
				viewer.refresh();
			}
		});
	}

	@Override
	public void dispose() {
		model.removeCodeReviewModelListener(this);
//		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		super.dispose();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		chatHistory.clear();
//		TreeSelection ts = (TreeSelection) event.getSelection();
		// if (ts.getFirstElement() instanceof CodeSelection) {
		// currentSelection = (CodeSelection) ts.getFirstElement();
		// chatHistory.setComments(currentSelection.getComments());
		// }
//		if (ts.getFirstElement() instanceof CodeReview) {
//			CodeReview review = (CodeReview) ts.getFirstElement();
//			closeReviewAction.getAction().setEnabled(review.getReviewee().equals(
//					ModelUtility.getAuthor()));
//			closeReviewAction.get
//		}
//		closeReviewAction
//				.setVisible((ts.getFirstElement() instanceof CodeReview));

		refreshHistory(event.getSelection());
	}
	
	private void refreshHistory(ISelection selection){
		TreeSelection ts = (TreeSelection) selection;
		if (ts.getFirstElement() instanceof CodeSelection) {
			currentSelection = (CodeSelection) ts.getFirstElement();
			chatHistory.setComments(currentSelection.getComments());
		}	
	}
	
	private void openInEditor(ISelection selection){
		Object element = ((TreeSelection) selection).getFirstElement();
		
		CodeSelection cs = null;
		FileSnapshot fs = null;
		
		if (element instanceof FileSnapshot) {
			 fs = (FileSnapshot) element;
		} else if (element instanceof CodeSelection) {
			 cs = (CodeSelection) element;
			 fs = model.getFileSnapshot(cs);
		}
		
		if (fs != null) {
		CodeReview review = model.getReview(fs);
		if (review.getReviewee().equals(ModelUtility.getAuthor())) {
			EditorUtility.openLocalFileInEditor(fs, cs);
//			openRemoteFileInEditor(fs, cs);
		} else {
			EditorUtility.openRemoteFileInEditor(fs, cs);
		}
		}
		
	}

//	private void openLocalFileInEditor(String filename, String content, CodeSelection cs) {
//		String packaName = getPackageName(content);
//		if (packaName != null) {
//			String fqn = packaName + "." + filename.replaceAll(".java", "");
//			try {
//			IWorkspace workspace = ResourcesPlugin.getWorkspace();
//			IJavaModel javaModel = JavaCore.create(workspace.getRoot());
//			IJavaProject[] projects = javaModel.getJavaProjects();
//			for (int i = 0; i < projects.length; i++) {
//				IType type = projects[i].findType(fqn);
//				if (type != null) {
//					IJavaElement target = (IJavaElement) type;
//					IMarker selection = null;
//					if (cs != null) {
//						IMarker[] markers = target.getResource().findMarkers("LighthouseCodeReview.requestMarker", true, IResource.DEPTH_INFINITE);
//						int charStart = cs.getSelection().getOffset();
//						int charEnd = charStart + cs.getSelection().getLength();
//						for (IMarker m : markers) {
//							if (charStart == MarkerUtilities.getCharStart(m) && charEnd == MarkerUtilities.getCharEnd(m)) {
//								selection = m;
//								break;
//							}
//						}
//					}
//					IEditorPart editorPart = JavaUI
//					.openInEditor(target, true, false);
//					if (selection != null) {
//						IDE.gotoMarker(editorPart, selection);
//					}
//				}
//			}
//			} catch (Exception ex) {
//				//logger.error(ex,ex);
//			}
//		}
//	}
//	
//	private String getPackageName(String fileContent) {
//		String result = null;
//		Scanner s = new Scanner(fileContent);
//		s.findInLine("package ((\\w+\\.*)+);");
//		 MatchResult match = s.match();
//		 if (match.groupCount() > 0) {
//			 result = match.group(1);
//		 }
//		 s.close();
//		return result;
//	}

//	private void openRemoteFileInEditor2(FileSnapshot fs,
//			CodeSelection cs) {
//		try {
//			IEditorInput editorInput = new FileSnapshotEditorInput(new FileSnapshotStorage(fs));
//			
//			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//			IWorkbenchPage page = window.getActivePage();
//			
//			IDE.openEditor(page, editorInput, IDE.getEditorDescriptor(fs.getFilename()).getId());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
	
//	private void openRemoteFileInEditor(FileSnapshot fs,
//			CodeSelection cs) {
//		String packageName = getPackageName(fs.getContent());
//		if (packageName != null) {
//			String fqn = packageName + "." + fs.getFilename().replaceAll(".java", "");
//		try {
//			IWorkspace ws = ResourcesPlugin.getWorkspace();
//			IProject project = ws.getRoot().getProject(Activator.PLUGIN_ID);
//			if (!project.exists()) {
//				project.create(null);
//			}
//			if (!project.isOpen()) {
//				project.open(null);
//			}
//			IFolder folder = project.getFolder("src");
//			if (!folder.exists()) {
//				folder.create(true, true, null);
//			}
//			IJavaProject jProject = JavaCore.create(project);
//			IPackageFragmentRoot srcFolder = jProject
//					.getPackageFragmentRoot(folder);
//			srcFolder.createPackageFragment(packageName, true, null);
//			
//			IFile iFile = project.getFile(new Path("src/"+packageName.replaceAll("\\.", "/")+"/"+fs.getFilename()));
//			if (!iFile.exists()) {
//				iFile.create(new ByteArrayInputStream(fs.getContent().getBytes()), true, null);
//				ResourceAttributes ra = new ResourceAttributes();
//				ra.setReadOnly(true);
//				iFile.setResourceAttributes(ra);
//			} else {
//				ResourceAttributes ra = new ResourceAttributes();
//				ra.setReadOnly(false);
//				iFile.setResourceAttributes(ra);
//				iFile.setContents(new ByteArrayInputStream(fs.getContent().getBytes()), true, false, null);
//				ra.setReadOnly(true);
//				iFile.setResourceAttributes(ra);
//			}
//
//			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//			IWorkbenchPage page = window.getActivePage();
//			
//			IDE.openEditor(page, new RemoteFileEditorInput(iFile, fs), IDE.getEditorDescriptor(fs.getFilename()).getId());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		}
//		
//	}


	

	
}
