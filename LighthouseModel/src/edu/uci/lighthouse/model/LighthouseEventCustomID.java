package edu.uci.lighthouse.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LighthouseEventCustomID {
	  @Id
		@GeneratedValue(strategy=GenerationType.AUTO)
	  private int number;

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
}
