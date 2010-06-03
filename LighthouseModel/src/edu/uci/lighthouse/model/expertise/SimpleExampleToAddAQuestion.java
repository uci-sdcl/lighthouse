package edu.uci.lighthouse.model.expertise;

import java.text.ParseException;
import java.util.Date;

import edu.uci.lighthouse.model.LighthouseAuthor;
import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHQuestionDAO;
import edu.uci.lighthouse.model.util.LHStringUtil;

public class SimpleExampleToAddAQuestion {

	public static void main(String[] args) throws ParseException, JPAException {
		
		LighthouseQuestion q = new LighthouseQuestion();
		q.setAuthor(new LighthouseAuthor("Alex"));
		q.setLhClass(new LighthouseClass("MyClass"));
		q.setSubject("I need help");
		q.setText("This is complicated");
		Date timestamp = LHStringUtil.simpleDateFormat.parse("2010-04-11 01:02:03");
		q.setTimestamp(timestamp);

		LHQuestionDAO dao = new LHQuestionDAO();
		dao.save(q);
		
	}
	
}
