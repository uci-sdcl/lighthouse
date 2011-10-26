package edu.uci.lighthouse.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LighthouseEventCustomID implements Serializable{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 5218231795439723280L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int ID;

	public void setID(int number) {
		this.ID = number;
	}

	public int getID() {
		return ID;
	}
}
