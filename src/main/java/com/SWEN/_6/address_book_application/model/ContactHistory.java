package com.SWEN._6.address_book_application.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class ContactHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long contactId;
    private String contactName;
    private ChageType changeType;
    @Column(length = 1024)
    private String changeDetails;
    private LocalDateTime changeTimestamp;
    
    
    
    public ContactHistory() {
    	        super();
    }
    
	public ContactHistory(Long contactId, ChageType changeType, String changeDetails, LocalDateTime changeTimestamp) {
		super();
		this.contactId = contactId;
		this.changeType = changeType;
		this.changeDetails = changeDetails;
		this.changeTimestamp = changeTimestamp;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getContactId() {
		return contactId;
	}
	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}
	public ChageType getChangeType() {
		return changeType;
	}
	public void setChangeType(ChageType changeType) {
		this.changeType = changeType;
	}
	public String getChangeDetails() {
		return changeDetails;
	}
	public void setChangeDetails(String changeDetails) {
		this.changeDetails = changeDetails;
	}
	public LocalDateTime getChangeTimestamp() {
		return changeTimestamp;
	}
	public void setChangeTimestamp(LocalDateTime changeTimestamp) {
		this.changeTimestamp = changeTimestamp;
	}
	
	public String getContactName() {
		return contactName;
	}
	
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
   
   
}
