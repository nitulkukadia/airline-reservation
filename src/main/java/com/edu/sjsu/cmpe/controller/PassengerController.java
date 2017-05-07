/**
 * Controller for Flight endpoint.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	19-04-2017
 */

package com.edu.sjsu.cmpe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.sjsu.cmpe.dao.model.Passenger;
import com.edu.sjsu.cmpe.service.PassengerService;
import com.edu.sjsu.cmpe.service.exception.BusinessException;
import com.edu.sjsu.cmpe.service.model.json.PassengerResponse;
import com.edu.sjsu.cmpe.service.model.json.Response;
import com.edu.sjsu.cmpe.service.util.ServiceUtil;

@RestController
@RequestMapping("/passenger")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PassengerController {

	@Autowired
	private PassengerService passengerService;

	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> createPassenger(@RequestParam(name = "firstname", required = true) String firstName,
			@RequestParam(name = "lastname", required = true) String lastName,
			@RequestParam(name = "age", required = true) int age,
			@RequestParam(name = "gender", required = true) String gender,
			@RequestParam(name = "phone", required = true) String phone) {
		try {
			Passenger passenger = passengerService.createPassenger(firstName, lastName, age, gender, phone);
			PassengerResponse passengerResponse = ServiceUtil.buildPassengerResponse(passenger, true);
			return ResponseEntity.ok(ServiceUtil.buildResponse("passenger", passengerResponse, null));
		} catch (BusinessException e) {
			Response errorResponse = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity(ServiceUtil.buildResponse("BadRequest", errorResponse, null),
					HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
		}
	}

	@GetMapping(value = "/{id}",
			// params = "json=true",
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getPassengerAsJson(@PathVariable(name = "id", required = true) String id) {
		try {
			Passenger passenger = passengerService.getPassenger(id);
			if (passenger != null) {
				PassengerResponse passengerResponse = ServiceUtil.buildPassengerResponse(passenger, true);
				return ResponseEntity.ok(ServiceUtil.buildResponse("passenger", passengerResponse, null));
			} else {
				throw new BusinessException("404", "Passenger with id " + id + " not found.");
			}

		} catch (BusinessException e) {
			Response errorResponse = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity(ServiceUtil.buildResponse("BadRequest", errorResponse, null),
					HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
		}
	}

	@GetMapping(value = "/{id}", params = "xml=true", produces = { MediaType.APPLICATION_XML_VALUE })
	public Object getPassengerAsXml(@PathVariable(name = "id", required = true) String id) {
		try {
			Passenger passenger = passengerService.getPassenger(id);
			if (passenger != null) {
				PassengerResponse passengerResponse = ServiceUtil.buildPassengerResponse(passenger, true);
				Object response = ServiceUtil.buildResponse("passenger", passengerResponse, null);
				return ServiceUtil.getXMLFromObject(response);
			} else {
				throw new BusinessException("404", "Passenger with id " + id + " not found.");
			}
		} catch (BusinessException e) {
			Response errorResponse = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity(
					ServiceUtil.getXMLFromObject(ServiceUtil.buildResponse("BadRequest", errorResponse, null)),
					HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
		}
	}

	@PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> updatePassenger(@PathVariable(name = "id", required = true) String id,
			@RequestParam(name = "firstname", required = true) String firstName,
			@RequestParam(name = "lastname", required = true) String lastName,
			@RequestParam(name = "age", required = true) int age,
			@RequestParam(name = "gender", required = true) String gender,
			@RequestParam(name = "phone", required = true) String phone) {
		try {
			Passenger passenger = passengerService.updatePassenger(id, firstName, lastName, age, gender, phone);
			PassengerResponse passengerResponse = ServiceUtil.buildPassengerResponse(passenger, true);
			return ResponseEntity.ok(ServiceUtil.buildResponse("passenger", passengerResponse, null));
		} catch (BusinessException e) {
			Response errorResponse = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity(ServiceUtil.buildResponse("BadRequest", errorResponse, null),
					HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
		} catch (Exception e) {
			Response errorResponse = new Response("400", e.getMessage());
			return new ResponseEntity(ServiceUtil.buildResponse("BadRequest", errorResponse, null),
					HttpStatus.valueOf(400));
		}
	}

	@DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE })
	public Object deletePassenger(@PathVariable(name = "id", required = true) String id) {
		try {
			passengerService.deletePassenger(id);
			Response reposne = new Response("200", "Passenger with id " + id + " is deleted successfully");
			return reposne;
		} catch (BusinessException e) {
			Response errorResponse = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity(
					ServiceUtil.getXMLFromObject(ServiceUtil.buildResponse("BadRequest", errorResponse, null)),
					HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
		}
	}
}