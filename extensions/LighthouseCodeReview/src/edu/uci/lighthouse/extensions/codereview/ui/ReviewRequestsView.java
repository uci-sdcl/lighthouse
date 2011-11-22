package edu.uci.lighthouse.extensions.codereview.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import edu.uci.lighthouse.extensions.codereview.model.CodeReview;
import edu.uci.lighthouse.extensions.codereview.model.CodeReviewModel;
import edu.uci.lighthouse.extensions.codereview.model.CodeSelection;
import edu.uci.lighthouse.extensions.codereview.model.Comment;
import edu.uci.lighthouse.extensions.codereview.model.ICodeReviewModelListener;

/**
 * View that shows all requests for review.
 * 
 * @author Tiago Proenca (tproenca@gmail.com)
 */
public class ReviewRequestsView extends ViewPart implements ICodeReviewModelListener, ISelectionChangedListener{
	
//	private StyledText chatHistory;
	private CommentHistory chatHistory;
	private Text chatMessage;
	private TreeViewer viewer;
	private CodeReviewModel model = CodeReviewModel.getInstance();
	
	@Override
	public void createPartControl(Composite parent) {

		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new CodeReviewContentProvider());
		viewer.setLabelProvider(new CodeReviewLabelProvider());
		viewer.setSorter(new ViewerSorter());
		viewer.setInput(model);
		viewer.addSelectionChangedListener(this);
		
		Composite composite = new Composite(parent, SWT.NULL);
		//composite.setBackground(ColorConstants.red);
		composite.setLayout(new FormLayout());
		FormData fData;
		
		fData = new FormData();
		fData.top = new FormAttachment(0,0);
		fData.left = new FormAttachment(0,0);
		fData.right = new FormAttachment(100,0);
		fData.bottom = new FormAttachment(80,0);
//		chatHistory = new StyledText(composite, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
//		chatHistory.setEditable(false);
		chatHistory = new CommentHistory(composite, SWT.BORDER);
		chatHistory.setLayoutData(fData);
		
//		URL url = getClass().getResource("/chat.css");
		
		
//		StringBuffer buffer = new StringBuffer();
//		buffer.append("<style type=\"text/css\" >");
////		buffer.append("<style type=\"text/css\" src=\""+url.toString()+"\" />");
//		//getClass().getResourceAsStream("/chat.css")
//		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/chat.css")));
//		String line;
//		try {
//			while((line = br.readLine()) != null){
//				buffer.append(line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		buffer.append("</style>");
//		chatHistory.setText(buffer.toString());

		fData = new FormData();
		fData.top = new FormAttachment(chatHistory,0);
		fData.left = new FormAttachment(0,0);
		fData.right = new FormAttachment(100,0);
		fData.bottom = new FormAttachment(100,0);
		chatMessage = new Text(composite, SWT.BORDER);
		chatMessage.setLayoutData(fData);		
		
		model.addCodeReviewModelListener(this);
	}

	@Override
	public void setFocus() {
		//viewer.getControl().setFocus();
	}
	
	@Override
	public void added(Collection<CodeReview> reviews) {
		refreshView();
	}

	@Override
	public void changed(Collection<CodeReview> reviews) {
		refreshView();
	}

	@Override
	public void removed(Collection<CodeReview> reviews) {
		refreshView();
	}
	
	private void refreshView(){
		Display.getDefault().asyncExec(new Runnable(){
			@Override
			public void run() {
				viewer.refresh();				
			}
		});
	}
	
	@Override
	public void dispose() {
		model.removeCodeReviewModelListener(this);
		super.dispose();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
//		chatHistory.setText("");
		
		TreeSelection ts = (TreeSelection)event.getSelection();
		if (ts.getFirstElement() instanceof CodeSelection) {
			CodeSelection selection = (CodeSelection) ts.getFirstElement();
//			Color color = JFaceResources.getColorRegistry().get(JFacePreferences.COUNTER_COLOR);
//			StringBuffer buffer = new StringBuffer(chatHistory.getText());
//			for (Comment c: selection.getComments()){
//				chatHistory.addComment(c);
//				buffer.append(getHTMLText(c));
//				String styledText = c.getAuthor().getName()+": ";
//				StyleRange style = new StyleRange();
//				style.start = chatHistory.getCharCount();
//				style.length = styledText.length();
//				style.fontStyle = SWT.BOLD;
//				style.foreground = color;
//				chatHistory.append(styledText+c.getContent()+"\n");
//				chatHistory.setStyleRange(style);
			chatHistory.setComments(selection.getComments());
			}
//			chatHistory.setText(buffer.toString());
		
//		}
		
	}
	
	private String getHTMLText(Comment c){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<span class=\"author\">"+c.getAuthor().getName()+"</span>");
		buffer.append("<P>"+c.getContent()+"</P>");
		return buffer.toString();
	}
}
