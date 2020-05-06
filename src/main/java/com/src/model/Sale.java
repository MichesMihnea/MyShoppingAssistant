package com.src.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.CascadeType;

/**
 * @author Mihnea
 * This class models the Sale entity. It represents an item that is on sale. Since
 * it shares all the features of the Product, bringing only two extra Date fields,
 * it makes sense that this class extends the Product class.
 */
@Entity // This tells Hibernate to make a table named "sale" out of this class
@Table(name="sale")
public class Sale extends Product {

	//The Date the Sale starts
	@Column(name = "startDate")
	private Date startDate;
	
	//The Date the Sale ends
	@Column(name = "endDate")
	private Date endDate;

	//Getters and setters

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}