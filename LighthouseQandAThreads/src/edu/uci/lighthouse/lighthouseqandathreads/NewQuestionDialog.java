package edu.uci.lighthouse.lighthouseqandathreads;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import edu.uci.lighthouse.lighthouseqandathreads.model.Forum;
import edu.uci.lighthouse.lighthouseqandathreads.model.Post;

import edu.uci.lighthouse.lighthouseqandathreads.model.Thread;

public class NewQuestionDialog extends MessageDialog {

	private String question;

	public static int OK = 0;
	public static int CANCEL = 1;
	private static String[] labelArray = { "OK", "CANCEL" };
	private Tree tree;
	private StyledText messageBox;

	// TODO: only a test attribute will be removed
	private Forum testForum;

	public NewQuestionDialog(Shell parentShell, String dialogTitle,
			Image dialogTitleImage, String dialogMessage, int dialogImageType,
			int defaultIndex) {

		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
				dialogImageType, labelArray, defaultIndex);
	}

	public Control createCustomArea(Composite parent) {

		GridData data = new GridData(GridData.FILL_HORIZONTAL);

		TabFolder tabFolder = new TabFolder(parent, SWT.BORDER);

		TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
		tabItem.setText("Create Thread");
		createQuestionComposite(tabFolder, tabItem);

		TabItem tabItem2 = new TabItem(tabFolder, SWT.NULL);
		tabItem2.setText("Threads");
		createThreadComposite(tabFolder, tabItem2);

		return tabFolder;
	}

	private void createThreadComposite(TabFolder tabFolder, TabItem tabItem) {
		GridData compsiteData = new GridData(650, 450);

		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(compsiteData);

		tabItem.setControl(composite);

		// ----
		Composite treeAndMsgComp = new Composite(composite, SWT.NONE);
		treeAndMsgComp.setLayout(new GridLayout(2, false));
		treeAndMsgComp.setLayoutData(compsiteData);

		GridData questionLayoutData = new GridData(281, 380);
		tree = new Tree(treeAndMsgComp, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL
				| SWT.H_SCROLL);
		tree.setLayoutData(questionLayoutData);
		tree.addSelectionListener(new ListListener());

		GridData msgBoxData = new GridData(300, 400);
		messageBox = new StyledText(treeAndMsgComp, SWT.BORDER);
		messageBox.setLayoutData(msgBoxData);
		// ----

		GridData replyBoxData = new GridData(615, 100);
		final StyledText replyBox = new StyledText(composite, SWT.BORDER);
		replyBox.setLayoutData(replyBoxData);

		replyBox.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				// reply
			}
		});

		populateTree(testForum);

	}

	public void setTestForm(Forum test) {
		this.testForum = test;
	}

	private void createQuestionComposite(TabFolder tabFolder, TabItem tabItem) {

		GridData compsiteData = new GridData(650, 450);

		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(compsiteData);

		tabItem.setControl(composite);

		GridData questionLayoutData = new GridData(600, 400);
		final StyledText st = new StyledText(composite, SWT.BORDER);
		st.setLayoutData(questionLayoutData);

		st.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setQuestion(st.getText());

			}
		});

	}

	private void populateTree(Forum forum) {
		for (Thread thread : forum.getThreads()) {
			setupTreeBranch(thread);
		}
	}

	private void setupTreeBranch(Thread thread) {
		Post rootPost = thread.getRootQuestion();

		TreeItem item = new TreeItem(tree, 0);
		item.setData(rootPost);
		item.setText(thread.getRootQuestion().getSubject());

		List<Post> posts = thread.getRootQuestion().getResponses();
		for (Post child : posts) {
			TreeItem childItem = new TreeItem(item, 0);
			childItem.setData(child);
			childItem.setText(child.getSubject());
			setupSubTreeBranch(child, childItem);
		}
	}

	private void setupSubTreeBranch(Post post, TreeItem parentItem) {
		List<Post> children = post.getResponses();
		for (Post child : children) {
			TreeItem childItem = new TreeItem(parentItem, 0);
			childItem.setData(child);
			childItem.setText(child.getSubject());
			setupSubTreeBranch(child, childItem);
		}
	}

	private class ListListener extends SelectionAdapter {

		public void widgetSelected(SelectionEvent e) {
			if(e.getSource() instanceof Tree){
				Tree tree = (Tree)e.getSource();
				
				
				TreeItem[] items = tree.getSelection();
				for(TreeItem item : items){
					Post post = (Post) item.getData();
					messageBox.setText(post.getTeamMemberAuthor().getAuthor()
							.getName()
							+ ": \n" + post.getMessage());
				}
				
			}

		}
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestion() {
		return question;
	}

}
