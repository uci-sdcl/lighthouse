package edu.prenticehall.deitel;

import java.io.IOException;
import java.util.LinkedHashSet;

// ATM.java
// Represents an automated teller machine

public class ATM 
{ 
	interface MyInterface {
		public void methodFromInterface();
	}
	
	private class InnerClass<T extends Deposit> extends Transaction implements MyInterface {

		LinkedHashSet<Deposit> list = new LinkedHashSet<Deposit>(); 
		
		public InnerClass(int userAccountNumber, Screen atmScreen,
				BankDatabase atmBankDatabase) {
			super(userAccountNumber, atmScreen, atmBankDatabase);
			// TODO Auto-generated constructor stub
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void execute() {
			T tGeneric = (T) new Deposit(0,null,null,null,null);
			myGeneric(tGeneric);
		}

		@Override
		public void methodFromInterface() {
			// TODO Auto-generated method stub
			
		}
		
		private void myGeneric(T tGeneric) {
			list.add(tGeneric);
		}
		
	}
	
   public boolean userAuthenticated; // whether user is authenticated
   protected Integer currentAccountNumber; // current user's account number
   private InnerClass<Deposit> innerClass;
   
   private Screen screen; // ATM's screen
   Keypad keypad; // ATM's keypad

   public BankDatabase bankDatabase;
   
   // constants corresponding to main menu options
   private static final int BALANCE_INQUIRY = 1;
   private static final int WITHDRAWAL = 2;
   private static final int EXIT = 4;

   // no-argument ATM constructor initializes instance variables
   public ATM() 
   {
      userAuthenticated = false; // user is not authenticated to start
      currentAccountNumber = 0; // no current account number to start
      screen = new Screen(); // create screen
      keypad = new Keypad(); // create keypad 
   } // end no-argument ATM constructor

   public ATM(BankDatabase bankDatabase) {
	   this.bankDatabase = bankDatabase;
   }
   
   // start ATM 
   public void run()
   {
	   BalanceInquiry balanceInquiry = null;
	   balanceInquiry.getAccountNumber();
      // welcome and authenticate user; perform transactions
      while ( true )
      {
         // loop while user is not yet authenticated
         while ( !userAuthenticated ) 
         {
            screen.displayMessageLine( "\nWelcome!" );       
            try {
				authenticateUser(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // authenticate user
         } // end while
         
         performTransactions(); // user is now authenticated 
         userAuthenticated = false; // reset before next ATM session
         currentAccountNumber = 0; // reset before next ATM session 
         screen.displayMessageLine( "\nThank you! Goodbye!" );
      } // end while   
   } // end method run

   // attempts to authenticate user against database
   private void authenticateUser(Long myPassword) throws IOException
   {
	   CashDispenser cashDispenser = null;
	   cashDispenser.isSufficientCashAvailable(0);
	   
      screen.displayMessage( "\nPlease enter your account number: " );
      screen.displayMessage( "\nEnter your PIN: " ); // prompt for PIN
      
      // set userAuthenticated to boolean value returned by database
      
      // check whether authentication succeeded
      if ( userAuthenticated )
      {
         currentAccountNumber = 0; // save user's account #
      } // end if
      else
         screen.displayMessageLine( 
             "Invalid account number or PIN. Please try again." );
   } // end method authenticateUser

   // display the main menu and perform transactions
   private void performTransactions() 
   {
	   Withdrawal withdrawal = null;
	   withdrawal.getAccountNumber();
      
      boolean userExited = false; // user has not chosen to exit

      // loop while user has not chosen option to exit system
      while ( !userExited )
      {     
         // show main menu and get user selection
         int mainMenuSelection = displayMainMenu();

         // decide how to proceed based on user's menu selection
         switch ( mainMenuSelection )
         {
            // user chose to perform one of three transaction types
            case BALANCE_INQUIRY: 
            case WITHDRAWAL: 
            case EXIT: // user chose to terminate session
               screen.displayMessageLine( "\nExiting the system..." );
               userExited = true; // this ATM session should end
               break;
            default: // user did not enter an integer from 1-4
               screen.displayMessageLine( 
                  "\nYou did not enter a valid selection. Try again." );
               break;
         } // end switch
      } // end while
   } // end method performTransactions
   
   // display the main menu and return an input selection
   private Integer displayMainMenu()
   {
	   DepositSlot depositSlot = null;
	   depositSlot.isEnvelopeReceived();
	   
      screen.displayMessageLine( "\nMain menu:" );
      screen.displayMessageLine( "1 - View my balance" );
      screen.displayMessageLine( "2 - Withdraw cash" );
      screen.displayMessageLine( "3 - Deposit funds" );
      screen.displayMessageLine( "4 - Exit\n" );
      screen.displayMessage( "Enter a choice: " );
      return keypad.getInput(); // return user's selection
   } // end method displayMainMenu
         
   // return object of specified Transaction subclass
   private Transaction createTransaction( int type )
   {
	   Transaction temp = null; // temporary Transaction variable
      
      // determine which type of Transaction to create     
      switch ( type )
      {
         case BALANCE_INQUIRY: // create new BalanceInquiry transaction
            temp = new BalanceInquiry( 
               currentAccountNumber, screen, null );
            break;
         case WITHDRAWAL: // create new Withdrawal transaction
            temp = new Withdrawal( currentAccountNumber, screen, 
               null, keypad, null );
            break; 
      } // end switch

      return temp; // return the newly created object
   } // end method createTransaction
} // end class ATM



/**************************************************************************
 * (C) Copyright 1992-2005 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/