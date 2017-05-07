/**
 * Controller for Flight endpoint.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	19-04-2017
 */

package com.edu.sjsu.cmpe.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.sjsu.cmpe.dao.model.Reservation;
import com.edu.sjsu.cmpe.service.ReservationService;
import com.edu.sjsu.cmpe.service.exception.BusinessException;
import com.edu.sjsu.cmpe.service.model.json.ReservationResponse;
import com.edu.sjsu.cmpe.service.model.json.Response;
import com.edu.sjsu.cmpe.service.util.ServiceUtil;

@RestController
@RequestMapping("/reservation")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_XML_VALUE })
	public Object createReservation(@RequestParam(name = "passengerId", required = true) long passengerID,
			@RequestParam(name = "flightLists", required = true) String flightList) {
		String[] flightNumbers = flightList.split(",");
		try {
			Reservation reservation = reservationService.createReservation(passengerID, flightNumbers);
			return ServiceUtil.getXMLFromObject(ServiceUtil.buildResponse("reservation",
					ServiceUtil.buildReservationResponse(reservation, true, true), null));
		} catch (BusinessException e) {
			Response errorResponse = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity<>(
					ServiceUtil.getXMLFromObject(ServiceUtil.buildResponse("BadRequest", errorResponse, null)),
					HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
		}
	}

	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getReservation(@PathVariable(name = "id", required = true) long id) {
		try {
			Reservation reservation = reservationService.getReservation(id);
			Map<String, Object> response = ServiceUtil.buildResponse("reservation",
					ServiceUtil.buildReservationResponse(reservation, true, true), null);
			return ResponseEntity.ok(response);
		} catch (BusinessException e) {
			Response errorResponse = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity(ServiceUtil.buildResponse("BadRequest", errorResponse, null),
					HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
		}
	}


	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Object> searchReservation(@RequestParam(name = "passengerId", required = false) String passengerID,
			@RequestParam(name = "from", required = false) String fromCity,
			@RequestParam(name = "to", required = false) String toCity,
			@RequestParam(name = "flightNumber", required = false) String flightNumber) {

		boolean isValid = false;
		long passengerId = 0;
		if(!passengerID.startsWith("0"))  {
			try{
				passengerId = Long.parseLong(passengerID);
				isValid = true;
			} catch (NumberFormatException e) {
			}
		}
		if(!isValid) {
			Response errorResponse = new Response("404", "No reservation found for given search criteria");
			return new ResponseEntity(
					ServiceUtil.getXMLFromObject(ServiceUtil.buildResponse("BadRequest", errorResponse, null)),
					HttpStatus.valueOf(errorResponse.getCode()));
		}

		try {
			Set<Reservation> reservations = reservationService.searchReservations(passengerId, fromCity, toCity, flightNumber);
			if(reservations.size() == 0) {
				throw new BusinessException("404", "No such reservation found.");
			}
			Map<String, Set<ReservationResponse>> reservationResponses = ServiceUtil.buildReservationsResponse(reservations, true, false);
			Map<String, Object> response = ServiceUtil.buildResponse("reservations",
					reservationResponses, null);
			return ResponseEntity.ok(ServiceUtil.getXMLFromObject(response));
		} catch (BusinessException e) {
			Response errorResponse = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity<>(
					ServiceUtil.getXMLFromObject(ServiceUtil.buildResponse("BadRequest", errorResponse, null)),
					HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
		}
	}

	@PostMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> updateReservation(@PathVariable(name = "id", required = true) long reservationId,
			@RequestParam(name = "flightsAdded", required = false) String flightsAdded,
			@RequestParam(name = "flightsRemoved", required = false) String flightsRemoved) {
		try {
			String[] flightsToAdd = flightsAdded != null ? flightsAdded.split(",") : null;
			String[] flightsToRemove = flightsRemoved != null ? flightsRemoved.split(",") : null;
			Reservation reservation = reservationService.updateReservation(reservationId, flightsToAdd, flightsToRemove);
			Map<String, Object> response = ServiceUtil.buildResponse("reservation",
					ServiceUtil.buildReservationResponse(reservation, true, true), null);
			return ResponseEntity.ok(response);
		} catch (BusinessException e) {
			Response errorResponse = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity(ServiceUtil.buildResponse("BadRequest", errorResponse, null),
					HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
		}
	}

	@DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE })
	public Object cancelReservation(@PathVariable(name = "id", required = true) long id) {
		try {
			reservationService.cancelReservation(id);
			Response success = new Response("200", "Reservation with number " + id + " is canceled successfully");
			return success;
		} catch (BusinessException e) {
			Response error = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
		}
	}
}
