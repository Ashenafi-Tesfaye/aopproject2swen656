package com.SWEN._6.address_book_application.service;

import com.SWEN._6.address_book_application.model.ChageType;
import com.SWEN._6.address_book_application.model.ContactHistory;
import com.SWEN._6.address_book_application.repository.ContactHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactHistoryService {
	
	@Autowired
    private ContactHistoryRepository contactHistoryRepository;

  
    public void addHistory(Long contactId, ChageType add, String contactName, String changeDetails) {
        ContactHistory history = new ContactHistory();
        history.setContactId(contactId);
        history.setChangeType(add);
        history.setContactName(contactName);
        history.setChangeDetails(changeDetails);
        history.setChangeTimestamp(LocalDateTime.now());
        contactHistoryRepository.save(history);
    }    
    
    
    public List<ContactHistory> findContactHistories(String contactName, LocalDateTime startDate, LocalDateTime endDate, String changeType) {
        return contactHistoryRepository.findAll();
    }
}