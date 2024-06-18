package com.SWEN._6.address_book_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Address Book API",
        version = "v1.0",
        description = "This is an Address Book Application developed using Spring Boot. It allows users to manage their contacts and keep track of changes made to them.\n\n" +
                      "## Coming Up Features\n" +
                      "- **Authentication and Authorization**: Support for multiuser functionality, ensuring that each user can securely manage their own contacts.\n" +
                      "- **Pagination**: Implementing pagination to handle large sets of contacts efficiently.",
		contact = @Contact(
            name = "Support Team",
            email = "rashenafibab@gmail.com",
            url = "https://github.com/Ashenafi-Tesfaye/aopproject2swen656"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "http://springdoc.org"
        )
    )
)
public class AddressBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(AddressBookApplication.class, args);
	}

}
