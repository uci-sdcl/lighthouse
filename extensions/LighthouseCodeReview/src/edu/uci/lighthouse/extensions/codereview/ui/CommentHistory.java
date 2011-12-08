package edu.uci.lighthouse.extensions.codereview.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;

import edu.uci.lighthouse.extensions.codereview.model.Comment;

public class CommentHistory extends Composite {

	Browser browser;
	private String threadTemplate;
	private String postTemplate;

	private final static String CSS_FILE = "/template/chat.css";
	private final static String COMMENT_TEMPLATE_FILE = "/template/comment-template.html";
	private final static String PAGE_TEMPLATE_FILE = "/template/chat-template.html";

	private static Logger logger = Logger.getLogger(CommentHistory.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm aaa");

	public CommentHistory(Composite parent, int style) {
		super(parent, SWT.NULL);
		setLayout(new FillLayout());
		browser = new Browser(this, SWT.BORDER);
		threadTemplate = getContent(PAGE_TEMPLATE_FILE).replaceFirst("\\$css",
				getContent(CSS_FILE));
		postTemplate = getContent(COMMENT_TEMPLATE_FILE);
	}

	private String getContent(String filename) {
		StringBuffer buffer = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass()
				.getResourceAsStream(filename)));
		try {
			String line;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public void setComments(Collection<Comment> comments) {
		// https://ssl.gstatic.com/ui/v1/icons/mail/profile_mask2.png

		// logger.debug("http://www.gravatar.com/avatar/"+LHStringUtil.getMD5Hash("tproenca@gmail.com")+"?s=32");
		
		Set<Comment> set = new TreeSet<Comment>(new Comparator<Comment>(){
			@Override
			public int compare(Comment o1, Comment o2) {
				return o1.getTimestamp().compareTo(o2.getTimestamp());
			}
		});
		set.addAll(comments);
		
		StringBuffer buffer = new StringBuffer();
		for (Comment c : set) {
			String post = postTemplate
					.replaceFirst("\\$author", c.getAuthor().getName())
					.replaceFirst("\\$date", dateFormat.format(c.getTimestamp()))
					.replaceFirst("\\$comment", c.getContent())
					.replaceFirst("\\$img",c.getAuthor().getAvatar().toString()
							);
			buffer.append(post);
		}

		browser.setText(threadTemplate.replaceFirst("\\$comments",
				buffer.toString()));


	}

	public void clear() {
		browser.setText("");
	}

}
