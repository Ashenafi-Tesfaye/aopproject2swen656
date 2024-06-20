package com.SWEN._6.address_book_application.service;

import com.SWEN._6.address_book_application.model.ChageType;
import com.SWEN._6.address_book_application.model.Contact;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class ContactHistoryAspect {

    private ContactHistoryService contactHistoryService;

   
    private ContactService contactService;
    
    public ContactHistoryAspect(ContactHistoryService contactHistoryService, ContactService contactService) {
        this.contactHistoryService = contactHistoryService;
        this.contactService = contactService;
    }

    private Contact originalContact;

    private static final ThreadLocal<Contact> originalContactThreadLocal = new ThreadLocal<>();


    @Pointcut("execution(* com.SWEN._6.address_book_application.service.ContactService.updateContact(..)) && args(id, contactDetails)")
    public void updateContactPointcut(Long id, com.SWEN._6.address_book_application.model.Contact contactDetails) {}

    @Before("updateContactPointcut(id, contactDetails)")
    public void beforeUpdateContact(Long id, com.SWEN._6.address_book_application.model.Contact contactDetails) {
        originalContactThreadLocal.set(deepCopyContact(contactService.getContactById(id)));
        System.out.println("Original Contact before update: " + originalContact);
    }

    @AfterReturning(pointcut = "updateContactPointcut(id, contactDetails)", returning = "result")
    public void logUpdateContact(Long id, com.SWEN._6.address_book_application.model.Contact contactDetails, com.SWEN._6.address_book_application.model.Contact result) {
        System.out.println("logUpdateContact executed");
        System.out.println("logUpdateContact executed");
        Contact originalContact = originalContactThreadLocal.get();
        if (result != null && originalContact != null) {
            String changeDetails = constructChangeDetails(originalContact, result);
            System.out.println("Updated Contact after update: " + result);
            contactHistoryService.addHistory(result.getId(), ChageType.UPDATED, result.getName(), changeDetails);
        }
        originalContactThreadLocal.remove(); 
    }

    @Pointcut("execution(* com.SWEN._6.address_book_application.service.ContactService.addContact(..)) && args(contact)")
    public void addContactPointcut(com.SWEN._6.address_book_application.model.Contact contact) {}


   @AfterReturning(pointcut = "addContactPointcut(contact)", returning = "result")
    public void logAddContact(com.SWEN._6.address_book_application.model.Contact result, com.SWEN._6.address_book_application.model.Contact contact) {
        if (result != null) {
            contactHistoryService.addHistory(result.getId(), ChageType.ADD, result.getName(), "Initial creation");
        }
    }

    @AfterReturning(pointcut = "execution(* com.SWEN._6.address_book_application.service.ContactService.deleteContact(..))")
    public void logDeleteContact(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        Contact existingContact = contactService.getContactById(id);
        if (existingContact != null) {
            contactHistoryService.addHistory(id, ChageType.DELETED, existingContact.getName(), "Contact marked as deleted instead of being physically removed. " + existingContact.toString());
        }
    }

    private String constructChangeDetails(Contact original, Contact updated) {
        StringBuilder changes = new StringBuilder();

        System.out.println("Original ZIP: " + original.getZip());
        System.out.println("Updated ZIP: " + updated.getZip());
        
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
            changes.append("Zip changed from ").append(original.getZip()).append(" to ").append(updated.getZip()).append(". ");
        }
        if (!original.getPhone().equals(updated.getPhone())) {
            changes.append("Phone changed from ").append(original.getPhone()).append(" to ").append(updated.getPhone()).append(". ");
        }
        return changes.toString().isEmpty() ? "No changes detected." : changes.toString();
    }

    private Contact deepCopyContact(Contact original) {
        if (original == null) {
            return null;
        }
        Contact copy = new Contact();
        copy.setId(original.getId());
        copy.setName(original.getName());
        copy.setStreet(original.getStreet());
        copy.setCity(original.getCity());
        copy.setState(original.getState());
        copy.setZip(original.getZip());
        copy.setPhone(original.getPhone());
        return copy;
    }
}