package com.SWEN._6.address_book_application.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.SWEN._6.address_book_application.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}