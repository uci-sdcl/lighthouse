/*
 * Creator: Rosalva Gallardo-Valencia
 * Course: Inf111, Fall 2008
 * 
 * Created on Oct 2, 2008
 * Updated on Oct 6, 2008
 * 
 * Copyright, 2008 University of California. 
 * 
 * The SelfCheckOutGui class handles the Graphical User Interface for the Self CheckOut 
 * system. It allows the user to do the following actions in the system: 
 * Start Transaction, Add a Packaged Item, Add a Bulk Item, 
 * Bag Item, and Pay for Items.
 * Application messages, including exceptions, will be shown in the Messages section of the
 * screen.
 */
package edu.uci.inf111.SelfCheckOut.Gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import edu.uci.inf111.SelfCheckOut.App.GroceryItem;
import edu.uci.inf111.SelfCheckOut.App.SelfCheckOut;
import edu.uci.inf111.SelfCheckOut.Exceptions.AddWhileBaggingException;
import edu.uci.inf111.SelfCheckOut.Exceptions.AddWhilePayingException;
import edu.uci.inf111.SelfCheckOut.Exceptions.IncorrectStateException;
import edu.uci.inf111.SelfCheckOut.Exceptions.InvalidBICException;
import edu.uci.inf111.SelfCheckOut.Exceptions.InvalidProductException;
import edu.uci.inf111.SelfCheckOut.Exceptions.InvalidUPCException;

/**
 * This class contains the main method that will show the Graphical User
 * Interface of the Self CheckOut System.
 *
 */
public class SelfCheckOutGUI extends JPanel implements ActionListener{
    /**
	 * Class serial version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * String for the UPC label
	 */
	protected static final String upcLabelString = "UPC Number";
	/**
	 * String for the BIC label
	 */
	protected static final String bicLabelString = "BIC Number";
	/**
	 * String for the Weight label
	 */
	protected static final String bicWeightLabelString = "Weight";
	/**
	 * Button for Start action
	 */
	protected JButton startButton;
	/**
	 * Button for Add Packaged Item action
	 */
	protected JButton addUPCButton;
	/**
	 * Button for Add Bulk Item action
	 */
	protected JButton addBICButton; 
	/**
	 * Button for Bag Item action
	 */
	protected JButton bagItemButton; 
	/**
	 * Button for Pay for Items action
	 */
	protected JButton payButton;
	/**
	 * Text Field for UPC
	 */	
    protected JTextField upcTextField; 
	/**
	 * Text Field for BIC
	 */	
    protected JTextField bicTextField; 
	/**
	 * Text Field for Weight
	 */	
    protected JTextField bicWeightTextField;
	/**
	 * Text Area for application messages
	 */	
    protected JTextArea messagesTextArea;
	/**
	 * SelfCheckOut object for the transaction
	 */	
    protected SelfCheckOut selfCheckOut = null;
	/**
	 * GroceryItem object used for the transaction
	 */
    protected GroceryItem groceryItem = null;
	/**
	 * This constructor creates the text fields, labels, and buttons. It organizes all 
	 * these objects in a Grid Bag that has 5 lines, one per each action.
	 * It also creates a text area to show the application messages including exceptions.
	 * Finally, it includes all the created controls in a panel
	 */    
    public SelfCheckOutGUI() {
        setLayout(new BorderLayout());

        //Text field for UPC
        upcTextField = new JTextField(10);
        upcTextField.setActionCommand(upcLabelString);
        upcTextField.addActionListener(this);
        
        //Text field for BIC
        bicTextField = new JTextField(10);
        bicTextField.setActionCommand(bicLabelString);
        bicTextField.addActionListener(this);
        
        //Text field for BIC Weight
        bicWeightTextField = new JTextField(10);
        bicWeightTextField.setActionCommand(bicWeightLabelString);
        bicWeightTextField.addActionListener(this);
        
        //Label for UPC
        JLabel upcTextFieldLabel = new JLabel(upcLabelString + ": ");
        upcTextFieldLabel.setLabelFor(upcTextField);
        
        //Label for BIC
        JLabel bicTextFieldLabel = new JLabel(bicLabelString + ": ");
        bicTextFieldLabel.setLabelFor(bicTextField);
        
        //Label for BIC Weight
        JLabel bicWeightTextFieldLabel = new JLabel(bicWeightLabelString + ": ");
        bicWeightTextFieldLabel.setLabelFor(bicWeightTextField);
        
        
        //Start Button
        startButton = new JButton("Start");
        startButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        startButton.setHorizontalTextPosition(AbstractButton.CENTER);
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
       
        //Add Packaged Item Button
        addUPCButton = new JButton("Add UPC");
        addUPCButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        addUPCButton.setHorizontalTextPosition(AbstractButton.CENTER);
        addUPCButton.setActionCommand("addUPC");
        addUPCButton.addActionListener(this);
        
        //Add Builk Item Button
        addBICButton = new JButton("Add BIC");
        addBICButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        addBICButton.setHorizontalTextPosition(AbstractButton.CENTER);
        addBICButton.setActionCommand("addBIC");
        addBICButton.addActionListener(this);
        
        //Bag Item Button
        bagItemButton = new JButton("Bag Item");
        bagItemButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        bagItemButton.setHorizontalTextPosition(AbstractButton.CENTER);
        bagItemButton.setActionCommand("bagItem");
        bagItemButton.addActionListener(this);
        
        //Pay Button
        payButton = new JButton("Pay");
        payButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        payButton.setHorizontalTextPosition(AbstractButton.CENTER);
        payButton.setActionCommand("payItems");
        payButton.addActionListener(this);
        
        
        //Lay out the text controls and the labels
        JPanel textControlsPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        textControlsPane.setLayout(gridbag);
		c.anchor = GridBagConstraints.EAST;
		
		//Line 1: Start Action
		c.gridwidth = GridBagConstraints.REMAINDER; //next-to-last
		c.fill = GridBagConstraints.NONE;      //reset to default
		c.weightx = 0.0;                       //reset to default
		textControlsPane.add(startButton, c);
		
		//Line 2: Add Packaged Item 		
		c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
		c.fill = GridBagConstraints.NONE;      //reset to default
		c.weightx = 0.0;                       //reset to default
		textControlsPane.add(upcTextFieldLabel, c);
		
		c.gridwidth = GridBagConstraints.RELATIVE;     
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		textControlsPane.add(upcTextField, c);   
		
		c.gridwidth = GridBagConstraints.REMAINDER;     //end row
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		textControlsPane.add(addUPCButton, c);   
		
		//Line 3: Add Bulk Item	
		c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
		c.fill = GridBagConstraints.NONE;      //reset to default
		c.weightx = 0.0;                       //reset to default
		textControlsPane.add(bicTextFieldLabel, c);
		
		c.gridwidth = GridBagConstraints.RELATIVE;     
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		textControlsPane.add(bicTextField, c); 
		
		c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
		c.fill = GridBagConstraints.NONE;      //reset to default
		c.weightx = 0.0;                       //reset to default
		textControlsPane.add(bicWeightTextFieldLabel, c);
		
		c.gridwidth = GridBagConstraints.RELATIVE;     
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		textControlsPane.add(bicWeightTextField, c); 
		
		c.gridwidth = GridBagConstraints.REMAINDER;     //end row
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		textControlsPane.add(addBICButton, c);   

		//Line 4: Bag Item
		c.gridwidth = GridBagConstraints.REMAINDER; //next-to-last
		c.fill = GridBagConstraints.NONE;      //reset to default
		c.weightx = 0.0;                       //reset to default
		textControlsPane.add(bagItemButton, c);
		
		//Line 5: Pay for Items
		c.gridwidth = GridBagConstraints.REMAINDER; //next-to-last
		c.fill = GridBagConstraints.NONE;      //reset to default
		c.weightx = 0.0;                       //reset to default
		textControlsPane.add(payButton, c);
		
        //Create border for Actions    
        textControlsPane.setBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Actions"),
                                BorderFactory.createEmptyBorder(20,100,20,300)));
        
        //Create a text area for the application messages
        messagesTextArea = new JTextArea();
        messagesTextArea.setFont(new Font("Serif", Font.ITALIC, 16));
        messagesTextArea.setLineWrap(true);
        messagesTextArea.setWrapStyleWord(true);
        messagesTextArea.setEditable(false);
        
        //Add scroll to the text area
        JScrollPane areaScrollPane = new JScrollPane(messagesTextArea);
        areaScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(250, 250));
        
        //Create border for Messages
        areaScrollPane.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Messages"),
                                BorderFactory.createEmptyBorder(5,5,5,5)),
                areaScrollPane.getBorder()));

        //Include all the controls in the application panel
        JPanel appPanel = new JPanel(new BorderLayout());
        appPanel.add(textControlsPane, 
                     BorderLayout.PAGE_START);
        appPanel.add(areaScrollPane,
                     BorderLayout.CENTER);

        add(appPanel, BorderLayout.LINE_START);
 
    }
	/**
	 * Method that receives the ActionEvent when a button is pressed in the 
	 * GUI. It calls to the appropriate action in the system and
	 * shows the result of the action in the message text area.
	 * If an exception is raised, this is showed in the message text area
	 * starting with the word EXCEPTION.
	 * @param e ActionEvent captured when user presses a button in the GUI
	 */       
    public void actionPerformed(ActionEvent e) {
    	//Instantiate actions class
        Actions actions = new Actions();

        try{
        	//Start Action
	        if ("start".equals(e.getActionCommand())){
	        	
					selfCheckOut = actions.start();
					messagesTextArea.setText("SelfCheckOut has been started");
					
			//Add Packaged Item	
	        } else if ("addUPC".equals(e.getActionCommand())){
	       	        		
					groceryItem = actions.addUPC(selfCheckOut, upcTextField.getText());					
					messagesTextArea.setText("Shopping cart " + actions.printShoppingCart(selfCheckOut.listItemsInCart()));
					messagesTextArea.append("\n\nUPC Product " + upcTextField.getText() + " added." );
					upcTextField.setText("");
					
			//Add Bulk Item		
	        } else if ("addBIC".equals(e.getActionCommand())){
        		        		
					groceryItem = actions.addBIC(selfCheckOut, bicTextField.getText(), Double.parseDouble(bicWeightTextField.getText()));
					messagesTextArea.setText("Shopping cart " + actions.printShoppingCart(selfCheckOut.listItemsInCart()));
					messagesTextArea.append("\n\nBIC Product " + bicTextField.getText() + " added. ");
					bicTextField.setText("");
					bicWeightTextField.setText("");
			
			//Bag Item
	        } else if ("bagItem".equals(e.getActionCommand())){
	        	        		
					actions.bagItem(selfCheckOut, groceryItem);
					messagesTextArea.setText("Shopping cart " + actions.printShoppingCart(selfCheckOut.listItemsInCart()));
					messagesTextArea.append("\n\nProduct bagged. ");
					
			//Pay for Items		
	        } else if ("payItems".equals(e.getActionCommand())){
	        
	        		messagesTextArea.setText("Shopping cart " + actions.printShoppingCart(selfCheckOut.listItemsInCart()));
	        		String partialText = "\n\nTotal $" + selfCheckOut.getTotalCost();
					actions.payItems(selfCheckOut);
					messagesTextArea.append(partialText + " Paid.");
					
				
	        }
	    //Show exception in the text area for messages
        } catch (AddWhileBaggingException awbe) {
        	messagesTextArea.setText("EXCEPTION: Item scanned before previous item is bagged.");
		} catch (AddWhilePayingException awpe) {
			messagesTextArea.setText("EXCEPTION: Item scanned while payment is being processed");
		} catch (InvalidProductException ipe) {
			messagesTextArea.setText("EXCEPTION: Item not recognized.");
		} catch (IncorrectStateException ise) {
			messagesTextArea.setText("EXCEPTION: Invalid action for current state of Self Check Out.");
		} catch (NumberFormatException nfe) {
			messagesTextArea.setText("EXCEPTION: Invalid format for the Weight.");
		} catch (InvalidUPCException iupce) {
			messagesTextArea.setText("EXCEPTION: Invalid UPC: " + iupce.getMessage());
		} catch (InvalidBICException ibice) {
			messagesTextArea.setText("EXCEPTION: Invalid BIC: " + ibice.getMessage());
		} catch (NullPointerException npe) {
			//Checks if the selfCheckOut object is null
			if (selfCheckOut == null)
			{
				messagesTextArea.setText("EXCEPTION: Self Check Out has not been started.");				
			}
			else
			{
				messagesTextArea.setText("EXCEPTION: Null Pointer Exception.");				
			}
		} catch (Exception exception) {
			messagesTextArea.setText("EXCEPTION: An exception has occurred: "+ exception.getMessage() +". Check console for more details.");
			exception.printStackTrace();
		}
        
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window
        JFrame frame = new JFrame("Self Check Out");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window
        frame.add(new SelfCheckOutGUI());

        //Display the window
        frame.pack();
        frame.setVisible(true);
    }
    /**
     * Show the Graphical User Interface for the Self Check Out application
     */
    public static void main(String[] args) {
        //Schedule a job for the event dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	createAndShowGUI();
            }
        });
    }
	
}
