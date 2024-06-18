package com.SWEN._6.address_book_application.controller;
import jakarta.validation.Valid;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.SWEN._6.address_book_application.service.ContactService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.SWEN._6.address_book_application.model.Contact;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@Tag(name = "Contacts", description = "Manage contacts in the address book")
public class ContactController {
    
    
	@Autowired
    private ContactService contactService;


    @GetMapping
    @Operation(summary = "Retrieve all active contacts", description = "Retrieve all active contacts from the address book.")
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a specific contact by ID", description = "Retrieve a specific contact by its ID.")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        Contact contact = contactService.getContactById(id);
        return contact != null ? ResponseEntity.ok(contact) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Add a new contact", description = "Add a new contact to the address book.")
    public ResponseEntity<?> addContact(@Valid @RequestBody Contact contact, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(contactService.addContact(contact), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a specific contact", description = "Update a specific contact in the address book by its ID.")
    public ResponseEntity<?> updateContact(@PathVariable Long id, @Valid @RequestBody Contact contactDetails, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        Contact updatedContact = contactService.updateContact(id, contactDetails);
        return updatedContact != null ? ResponseEntity.ok(updatedContact) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a specific contact", description = "Delete a specific contact from the address book by its ID.")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}