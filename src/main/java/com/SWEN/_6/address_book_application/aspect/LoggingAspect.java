package com.SWEN._6.address_book_application.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.SWEN._6.address_book_application.model.Contact;
import com.SWEN._6.address_book_application.service.ContactService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Aspect
@Component
public class LoggingAspect {
    private final ContactService contactService;
    private static final String LOG_FILE = "contact_changes.log";

    public LoggingAspect(ContactService contactService) {
        this.contactService = contactService;
    }

    @Before("execution(* com.example.addressbook.service.ContactService.deleteContact(..)) && args(id)")
    public void logBeforeDelete(Long id) throws IOException {
        Contact contact = contactService.getContactById(id);
        if (contact != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
                writer.write("Record Deleted: " + contact.toString() + "\n");
            }
        }
    }

    @AfterReturning(pointcut = "execution(* com.example.addressbook.service.ContactService.updateContact(..)) && args(id, contactDetails)", returning = "result")
    public void logAfterUpdate(Long id, Contact contactDetails, Contact result) throws IOException {
        if (result != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
                writer.write("Record Updated: " + result.toString() + "\n");
            }
        }
    }
}