package edu.uci.lighthouse.model.jpa;


/**
 * Handle data base exception
 * @author Nilmax
 */
public class JPAUtilityException extends Exception {

	private static final long serialVersionUID = 7676004587653285319L;
	
	/**
	 * Constructor.
	 */
	public JPAUtilityException(String  message, Throwable reason){
		super(message, reason);		
	}

}
