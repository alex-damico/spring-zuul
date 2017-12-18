package com.example.serviceone.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.serviceone.model.Customer;

@RestController
public class ServiceController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping(value = "/api/customer")
	public ResponseEntity<Customer> create(@RequestBody final Customer customer) {
		logger.debug(customer.toString());
		
		return new ResponseEntity<>(customer, HttpStatus.CREATED);
	}
	
}
