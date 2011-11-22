package edu.uci.lighthouse.extensions.codereview.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import edu.uci.lighthouse.extensions.codereview.model.Comment;

public class CommentHistory extends Composite {

	Browser browser;
	private String threadTemplate;
	private String postTemplate;
	
	private final static String CSS_FILE = "/template/chat.css";
	private final static String COMMENT_TEMPLATE_FILE = "/template/comment-template.html";
	private final static String PAGE_TEMPLATE_FILE = "/template/chat-template.html";
	
	private static Logger logger = Logger
			.getLogger(CommentHistory.class);
	
	public CommentHistory(Composite parent, int style) {
		super(parent, SWT.NULL);
		setLayout(new FillLayout());
		browser = new Browser(this, SWT.BORDER);
		threadTemplate = getContent(PAGE_TEMPLATE_FILE).replaceFirst("\\$css", getContent(CSS_FILE));
		postTemplate = getContent(COMMENT_TEMPLATE_FILE);
	}

	private String getContent(String filename) {
		StringBuffer buffer = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));
		try {
			String line;
			while((line = br.readLine()) != null){
				buffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	public void setComments(Collection<Comment> comments){
//		StringBuffer buffer = new StringBuffer(browser.getText());
//		buffer.append("<div class=\"message\">\n");
//		buffer.append("<div class=\"avatar\">\n");
//		buffer.append("<img src=\"https://ssl.gstatic.com/ui/v1/icons/mail/profile_mask2.png\">\n");
//		buffer.append("</div>\n");
//		buffer.append("<div class=\"author\">\n");
//		buffer.append(c.getAuthor().getName());
//		buffer.append("<span class=\"date\">\n");
//		buffer.append(c.getTimestamp().toString());
//		buffer.append("</span>\n");
//		buffer.append("</div>\n");
//		buffer.append("<div>\n");
//		buffer.append(c.getContent());
//		buffer.append("</div>\n");
//		buffer.append("</div>\n");
//		buffer.append("<hr>");
//		
//		buffer.append("</body>");
//		buffer.append("</html>");
//		browser.setText(buffer.toString());
//		browser.setUrl("file://localhost/Users/tproenca/Workspaces/Lighthouse-inf219/lighthouse/extensions/LighthouseCodeReview/template.html");
		//https://ssl.gstatic.com/ui/v1/icons/mail/profile_mask2.png

//			logger.debug("http://www.gravatar.com/avatar/"+LHStringUtil.getMD5Hash("tproenca@gmail.com")+"?s=32");
		StringBuffer buffer = new StringBuffer();
		for (Comment c: comments) {
			String post = postTemplate
					.replaceFirst("\\$author", c.getAuthor().getName())
					.replaceFirst("\\$date", c.getTimestamp().toString())
					.replaceFirst("\\$comment", c.getContent())
					.replaceFirst("\\$img",
							"https://ssl.gstatic.com/ui/v1/icons/mail/profile_mask2.png");
			buffer.append(post);
		}
		
				browser.setText(threadTemplate.replaceFirst("\\$comments", buffer.toString()));


	}
	
	public void clear() {
		browser.setText("");
	}

}
