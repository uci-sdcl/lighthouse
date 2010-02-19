/*
 * Creator: Rosalva Gallardo-Valencia
 * Course: Inf111, Winter 2008
 * 
 * Created on Feb 14, 2008
 * Updated on Feb 18, 2008
 * 
 * Copyright, 2008 University of California. 
 * 
 * The Printer class handles the printing process. This class has two constructors.
 * The constructor without arguments will create a printer whose output will be shown 
 * in the console screen. The constructor with one argument will create a printer
 * whose output will be shown in a file.
 * To print a receipt on the screen or file, you should call the method print and pass 
 * an array of Strings as parameter. The print function will check if each String 
 * does not exceed the maximum of characters per line, defined by lineLenght. If the 
 * String exceeds, the method will only print the first lineLength characters.
 */

package edu.uci.inf111.SelfCheckOut.Devices;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import edu.uci.inf111.SelfCheckOut.Exceptions.PrinterException;


/**
 * This class represents the printer of the system. The output of this printer
 * could be the console or a file.
 *
 */
public class Printer {
	/**
	 * Contains the maximum length of the printer.
	 */
	int lineLength = 38;
	/**
	 * Contains the path of the output file for the printer.
	 */
	String targetFile;
	/**
	 * This constructor creates a printer whose output will be shown 
	 * in the console screen. It also initializes the variables targetFile.
	 */
	public Printer() {
		targetFile = "";
	}
	/**
	 * This constructor creates a printer whose output will be shown in a file.
	 * This method also checks that the targetFile path exists. In the case that 
	 * targetFile path does not exist, a PrinterException is thrown.
	 * @param pTargetFile	A String containing the path of the output file. It is recommended
	 * to use a relative path (ie "ptr/PrinterOutput.txt") due to the fact that this will
	 * work with both PC and MAC. If you create it using a relative path,
	 * the file will be stored in the location of your project (Right click on the project,
	 * Properties, see Location). 
	 * @throws PrinterException, IOException
	 */
	public Printer(String pTargetFile) throws PrinterException {
		targetFile = pTargetFile;
		try { 
			/* 
			 * It checks if the targetPath exists.
			 */				
			File file = new File(targetFile);
			FileOutputStream fos = new FileOutputStream(file, true);
		    fos.close();
		    } 
		catch (FileNotFoundException e) {
			/* 
			 * We throw an exception if the targetPath does not exist.
			 * The message provides details about the type of exception that occurred.
			 */			
		    //e.printStackTrace();
			throw (new PrinterException("File not found. Please check the path you indicated."));

		    }
		catch (IOException e) {
			/* 
			 * We throw an exception if there was an io exception.
			 * The message provides details about the type of exception that occurred.
			 */			
		    //e.printStackTrace();
			throw (new PrinterException("IOException. Please check the path you indicated."));

		    }
	}

	/**
	 * Prints the arrayList of Strings passed as parameter on the screen or file, 
	 * depending on the constructor used to create the printer object.
	 * The print function will check that the arrayList is not null and that 
	 * the elements in the arrayList are not null.
	 * Also it will check if each String in the arrayList
	 * does not exceed the maximum of characters per line, defined by lineLenght. If the 
	 * String exceeds, the method will only print the first lineLength characters.
	 * While printing, this method will add a new line character at the end of each String in the arrayList.
	 * In the case there would be any problem printing the data to the file, 
	 * a PrinterException is thrown.
	 * @param dataToPrint ArrayList of Strings that contains the list of Strings that
	 * represent the Receipt
	 * @throws PrinterException
	 */
	public void print(ArrayList<String> dataToPrint) throws PrinterException
	{
		String line;
		// Check if the ArrayList is not null
		if (dataToPrint == null)
		{
			throw (new PrinterException("Data sent to print is null."));
		}
		
		Iterator<String> iterator = dataToPrint.iterator();
		
		//Print the output in the Console Screen
		if (targetFile.equals(""))
		{
			//Read each element in the array			
			while (iterator.hasNext())
			{
				line = (String)iterator.next();
				
				// Check if the current element in the ArrayList is not null
				if (line == null)
				{
					throw (new PrinterException("One line sent to print is null."));
				}
				//If the String exceeds the limit of characters 
				if (checkLength(line) == false) 
				{	//Print the first lineLenght characters and a new line
					System.out.print(line.substring(0, lineLength) + "\n");
				}
				else
				{	//Print the String and a new line
					System.out.print(line + "\n");
				}
			}
			
		}
		else
		{
			FileOutputStream fos; 
		    DataOutputStream dos;

		    try {
		      //Create the file and output streams for the output
		      File file = new File(targetFile);
		      fos = new FileOutputStream(file, true);
		      dos=new DataOutputStream(fos);

		    //Read each element in the array	
		      while (iterator.hasNext())
		      {
		    	  line = (String)iterator.next();
		    	 // Check if the current element in the ArrayList is not null
		    	  if (line == null)
					{
						throw (new PrinterException("One line sent to print is null."));
					}
		    	  //If the String exceeds the limit of characters 
		    	  if (checkLength(line) == false) 
		    	  {   //Print the first lineLenght characters and a new line
		    		  dos.writeBytes(line.substring(0, lineLength) + "\n");
		    	  }
		    	  else
		    	  {   //Print the String and a new line
		    		  dos.writeBytes(line + "\n");
		    	  }
				}
		      //Close the output streams
		      dos.close();
		      fos.close();

		    } catch (IOException e) {
		      //e.printStackTrace();
			  throw (new PrinterException("Printer Exception occurred while printing file."));

		    }
		}
		
	}
	/**
	 * Return the value of lineLength
	 * @return lineLength
	 */
	public int getLineLength()
	{
		return lineLength;
	}

	/**
	 * Checks that the length of a String does not exceed lineLength.
	 * @param line
	 * @return <code>true</code> if the string length is greater than lineLength; <code>false</code> otherwise.
	 */
	private boolean checkLength(String line) {
		if (line.length() <= lineLength) {
			return true;
		} else {
			return false;
		}
	}
	
}
