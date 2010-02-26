package edu.deitel;

public class Balance extends Transaction {

	public Balance(int userAccountNumber, Screen atmScreen,
			BankDatabase atmBankDatabase) {
		super(userAccountNumber, atmScreen, atmBankDatabase);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		BankDatabase bank = getBankDatabase();
		double balance = bank.getAvailableBalance(getAccountNumber());
		
		Screen s = getScreen();
		s.displayMessage("balance: "+balance);
		
	}

}
