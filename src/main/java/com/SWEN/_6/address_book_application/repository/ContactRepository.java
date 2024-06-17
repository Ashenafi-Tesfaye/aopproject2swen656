package com.SWEN._6.address_book_application.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SWEN._6.address_book_application.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

	@Query("SELECT c FROM Contact c WHERE c.deleted = false")
	List<Contact> findAllActiveContacts();
}