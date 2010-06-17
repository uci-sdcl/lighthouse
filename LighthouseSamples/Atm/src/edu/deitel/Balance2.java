package edu.deitel;

import edu.deitel.transactions.Transaction;

public class Balance2 extends Transaction {

	public Balance2(int userAccountNumber, Screen atmScreen,
			BankDatabase atmBankDatabase) {
		super(userAccountNumber, atmScreen, atmBankDatabase);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		BankDatabase bank = getBankDatabase();
		double  balance = bank.getAvailableBalance(getAccountNumber());
		
		

	}

}
