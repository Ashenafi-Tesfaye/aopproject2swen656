package com.SWEN._6.address_book_application.repository;



import com.SWEN._6.address_book_application.model.ContactHistory;


import org.springframework.data.jpa.repository.JpaRepository;


public interface ContactHistoryRepository extends JpaRepository<ContactHistory, Long> {


}
