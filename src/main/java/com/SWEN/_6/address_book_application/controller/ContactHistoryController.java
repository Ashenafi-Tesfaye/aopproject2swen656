package com.SWEN._6.address_book_application.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SWEN._6.address_book_application.model.ContactHistory;
import com.SWEN._6.address_book_application.service.ContactHistoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/history/contacts")
@Tag(name = "Contact History", description = "Retrieve contact change history")
public class ContactHistoryController {
	
	@Autowired
	private ContactHistoryService contactHistoryService;

    @GetMapping 
    @Operation(summary = "Retrieve contact history", description = "Retrieve the history of changes made to contacts.")
    public ResponseEntity<List<ContactHistory>> listContactLogHistory( @RequestParam(required = false) String contactName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String changeType) {

        LocalDateTime startDateTime = parseDateTime(startDate);
        LocalDateTime endDateTime = parseDateTime(endDate);
            
        List<ContactHistory> histories = contactHistoryService.findContactHistories(contactName, startDateTime, endDateTime, changeType);
        return ResponseEntity.ok(histories);
        
    }

       private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use ISO_LOCAL_DATE_TIME format.");
        }
    }

}
