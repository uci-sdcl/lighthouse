package edu.uci.lighthouse.model.jpa;

/**
 * Handle data base exception
 */
public class JPAException extends Exception {

	private static final long serialVersionUID = 7676004587653285319L;
	
	/**
	 * Constructor.
	 */
	public JPAException(String  message, Throwable reason){
		super(message, reason);		
	}

	public JPAException(Throwable reason){
		super(reason);		
	}
	
}
