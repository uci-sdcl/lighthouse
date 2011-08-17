package edu.uci.lighthouse.lighthouseqandathreads.model;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;



public class PostTreeItem extends TreeItem{

	Post post;
	
	
	public PostTreeItem(Post post, Tree parent, int style) {
		super(parent, style);
		this.post = post;
	}
	
	public PostTreeItem(Post post, TreeItem treeItem, int style) {
		super(treeItem, style);
		this.post = post;
	}

}
