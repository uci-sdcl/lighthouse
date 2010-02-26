package edu.uci.lighthouse.model.util;

import edu.uci.lighthouse.model.LighthouseClass;
import edu.uci.lighthouse.model.jpa.JPAException;
import edu.uci.lighthouse.model.jpa.LHEntityDAO;

public class Test {

	/**
	 * @param args
	 * @throws  
	 */
	public static void main(String[] args) throws JPAException {
		// TODO Auto-generated method stub

		
		System.out.println("begin");
		
		LighthouseClass clazz = new LighthouseClass("Myclass");
		new LHEntityDAO().save(clazz);
		
		System.out.println("end");
		
	}

}
