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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.sjsu.cmpe.dao.model.Flight;
import com.edu.sjsu.cmpe.service.FlightService;
import com.edu.sjsu.cmpe.service.exception.BusinessException;
import com.edu.sjsu.cmpe.service.model.json.Response;
import com.edu.sjsu.cmpe.service.util.ServiceUtil;

@RestController
@RequestMapping("/flight")
public class FlightController {

	@Autowired
	private FlightService flightService;

	@PostMapping(value = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE })
	public Object createOrUpdateFlight(@PathVariable(name = "id", required = true) String id,
			@RequestParam(name = "price", required = true) int price,
			@RequestParam(name = "from", required = true) String from,
			@RequestParam(name = "to", required = true) String to,
			@RequestParam(name = "departureTime", required = true) String departureTime,
			@RequestParam(name = "arrivalTime", required = true) String arrivalTime,
			@RequestParam(name = "description", required = true) String descr,
			@RequestParam(name = "capacity", required = true) int capacity,
			@RequestParam(name = "model", required = true) String model,
			@RequestParam(name = "manufacturer", required = true) String manufacturer,
			@RequestParam(name = "yearOfManufacture", required = true) int yearOfManufacture) {
		try {
			return flightService.createOrUpdateFlight(id, price, from, to, departureTime, arrivalTime, descr, capacity,
					model, manufacturer, yearOfManufacture);
		} catch (BusinessException e) {
			Response errorResponse = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity<>(
					ServiceUtil.getXMLFromObject(ServiceUtil.buildResponse("BadRequest", errorResponse, null)),
					HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping(value = "/{flightNumber}",
			// params = "json=true",
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getFlightAsJson(
			@PathVariable(name = "flightNumber", required = true) String flightNumber) {
		try {
			Flight flight = flightService.getFlight(flightNumber);
			return ResponseEntity
					.ok(ServiceUtil.buildResponse("flight", ServiceUtil.getFlightResponse(flight, true, true), null));
		} catch (BusinessException e) {
			Response errorResponse = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity(ServiceUtil.buildResponse("BadRequest", errorResponse, null),
					HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
		}
	}

	@GetMapping(value = "/{flightNumber}", params = "xml=true", produces = { MediaType.APPLICATION_XML_VALUE })
	public Object getFlightAsXml(@PathVariable(name = "flightNumber", required = true) String flightNumber) {
		try {
			Flight flight = flightService.getFlight(flightNumber);
			Object response = ServiceUtil.buildResponse("flight", ServiceUtil.getFlightResponse(flight, true, true),
					null);
			return ResponseEntity.ok(ServiceUtil.getXMLFromObject(response));
		} catch (BusinessException e) {
			Response errorResponse = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity<>(
					ServiceUtil.getXMLFromObject(ServiceUtil.buildResponse("BadRequest", errorResponse, null)),
					HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
		}
	}

	@DeleteMapping(value = "/{flightNumber}", produces = { MediaType.APPLICATION_XML_VALUE })
	public Object deleteFlight(@PathVariable(name = "flightNumber", required = true) String flightNumber) {
		try {
			flightService.deleteFlight(flightNumber);
			Response reposne = new Response("200", "Flight with number " + flightNumber + " is deleted successfully");
			return reposne;
		} catch (BusinessException e) {
			Response errorResponse = new Response(e.getErrorCode(), e.getMessage());
			return new ResponseEntity<>(
					ServiceUtil.getXMLFromObject(ServiceUtil.buildResponse("BadRequest", errorResponse, null)),
					HttpStatus.valueOf(Integer.parseInt(e.getErrorCode())));
		}
	}
}
