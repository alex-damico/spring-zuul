package com.example.servicetwo.model;

public class Customer {
	private String firstname;
	private String lastname;
	
	private String createdBy;
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Override
	public String toString() {
		return "Customer [firstname=" + firstname + ", lastname=" + lastname + ", createdBy=" + createdBy + "]";
	}
}
