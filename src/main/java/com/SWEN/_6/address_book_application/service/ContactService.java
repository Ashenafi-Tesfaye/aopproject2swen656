package com.SWEN._6.address_book_application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SWEN._6.address_book_application.model.ChageType;
import com.SWEN._6.address_book_application.model.Contact;
import com.SWEN._6.address_book_application.repository.ContactRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
	
	@Autowired
    private  ContactRepository contactRepository;
	

   

    public List<Contact> getAllContacts() {
        return contactRepository.findAllActiveContacts();
    }

    public Contact getContactById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    public Contact addContact(Contact contact) {
        Contact savedContact = contactRepository.save(contact);
        return savedContact;
    }

    public Contact updateContact(Long id, Contact contactDetails) {
        Optional<Contact> existingContact = contactRepository.findById(id);
        if (existingContact.isPresent()) {
            Contact contact = existingContact.get();
            contact.setName(contactDetails.getName());
            contact.setStreet(contactDetails.getStreet());
            contact.setCity(contactDetails.getCity());
            contact.setState(contactDetails.getState());
            contact.setZip(contactDetails.getZip());
            contact.setPhone(contactDetails.getPhone());
            return contactRepository.save(contact);
        }
        return null;
    }

    public void deleteContact(Long id) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            contact.setDeleted(true); 
            contactRepository.save(contact);            
        }
    }
}