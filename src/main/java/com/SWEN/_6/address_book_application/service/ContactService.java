package com.SWEN._6.address_book_application.service;

import org.springframework.stereotype.Service;

import com.SWEN._6.address_book_application.model.Contact;
import com.SWEN._6.address_book_application.repository.ContactRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Contact getContactById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    public Contact addContact(Contact contact) {
        return contactRepository.save(contact);
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
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
        }
    }
}