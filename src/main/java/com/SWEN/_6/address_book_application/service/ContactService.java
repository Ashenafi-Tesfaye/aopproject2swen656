package com.SWEN._6.address_book_application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SWEN._6.address_book_application.model.ChageType;
import com.SWEN._6.address_book_application.model.Contact;
import com.SWEN._6.address_book_application.repository.ContactHistoryRepository;
import com.SWEN._6.address_book_application.repository.ContactRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
	
	@Autowired
    private  ContactRepository contactRepository;
	
	@Autowired
    private  ContactHistoryRepository contactHistoryRepository;
	
	@Autowired
    private  ContactHistoryService  contactHistoryService;

   

    public List<Contact> getAllContacts() {
        return contactRepository.findAllActiveContacts();
    }

    public Contact getContactById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    public Contact addContact(Contact contact) {
        Contact savedContact = contactRepository.save(contact);
        contactHistoryService.addHistory(savedContact.getId(), ChageType.ADD, savedContact.getName(), "Initial creation");
        return savedContact;
    }

    public Contact updateContact(Long id, Contact contactDetails) {
        Optional<Contact> existingContact = contactRepository.findById(id);
        if (existingContact.isPresent()) {
            Contact contact = existingContact.get();
            String changeDetails = constructChangeDetails(contact, contactDetails);
            contact.setName(contactDetails.getName());
            contact.setStreet(contactDetails.getStreet());
            contact.setCity(contactDetails.getCity());
            contact.setState(contactDetails.getState());
            contact.setZip(contactDetails.getZip());
            contact.setPhone(contactDetails.getPhone());
            Contact updatedContact = contactRepository.save(contact);
            contactHistoryService.addHistory(updatedContact.getId(), ChageType.UPDATED, contact.getName() ,changeDetails);
            return updatedContact;
        }
        return null;
    }

    public void deleteContact(Long id) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            contact.setDeleted(true); 
            contactRepository.save(contact);
            
            
            contactHistoryService.addHistory(id, ChageType.DELETED, contact.getName(), "Contact marked as deleted instead of being physically removed." +   contact.toString());
        }
    }
    
    private String constructChangeDetails(Contact original, Contact updated) {
        StringBuilder changes = new StringBuilder();
        if (!original.getName().equals(updated.getName())) {
            changes.append("Name changed from ").append(original.getName()).append(" to ").append(updated.getName()).append(". ");
        }
        if (!original.getStreet().equals(updated.getStreet())) {
            changes.append("Street changed from ").append(original.getStreet()).append(" to ").append(updated.getStreet()).append(". ");
        }
        if (!original.getCity().equals(updated.getCity())) {
            changes.append("City changed from ").append(original.getCity()).append(" to ").append(updated.getCity()).append(". ");
        }
        if (!original.getState().equals(updated.getState())) {
            changes.append("State changed from ").append(original.getState()).append(" to ").append(updated.getState()).append(". ");
        }
        if (!original.getZip().equals(updated.getZip())) {
            changes.append("ZIP code changed from ").append(original.getZip()).append(" to ").append(updated.getZip()).append(". ");
        }
        if (!original.getPhone().equals(updated.getPhone())) {
            changes.append("Phone number changed from ").append(original.getPhone()).append(" to ").append(updated.getPhone()).append(". ");
        }
        return changes.toString().isEmpty() ? "No changes detected." : changes.toString();
    }
}