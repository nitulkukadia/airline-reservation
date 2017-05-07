/**
 * Utility class for the app.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	21-04-2017
 */

package com.edu.sjsu.cmpe.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.edu.sjsu.cmpe.dao.model.Flight;
import com.edu.sjsu.cmpe.dao.model.Passenger;
import com.edu.sjsu.cmpe.dao.model.Reservation;
import com.edu.sjsu.cmpe.service.model.json.FlightResponse;
import com.edu.sjsu.cmpe.service.model.json.PassengerResponse;
import com.edu.sjsu.cmpe.service.model.json.ReservationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceUtil {

	public static Map<String, Object> buildResponse(String key, Object value, Map<String, Object> response) {
		if (response != null) {
			response.put(key, value);
		} else {
			response = new HashMap<>();
			response.put(key, value);
		}
		return response;
	}

	public static ReservationResponse buildReservationResponse(Reservation reservation, boolean needsPassenger, boolean needsSeatsLeft) {
		ReservationResponse reservationResponse = new ReservationResponse();
		reservationResponse.setOrderNumber(reservation.getOrderNumber());
		reservationResponse.setPrice(reservation.getPrice());
		if(needsPassenger) {
			reservationResponse.setPassenger(ServiceUtil.buildPassengerResponse(reservation.getPassenger(), false));
		}
		if (!reservation.getFlights().isEmpty()) {
			Map<String, List<FlightResponse>> flights = new HashMap<>();
			List<FlightResponse> flightResponseList = new ArrayList<>();
			for (Flight flight : reservation.getFlights()) {
				flightResponseList.add(getFlightResponse(flight, false, needsSeatsLeft));
			}
			flights.put("flight", flightResponseList);
			reservationResponse.setFlights(flights);
		}
		return reservationResponse;
	}
	
	public static Map<String, Set<ReservationResponse>> buildReservationsResponse(Set<Reservation> reservations, boolean needsPassenger, boolean needsSeatsLeft) {
		Set<ReservationResponse> reservationResponses = new HashSet<>();
		 Map<String, Set<ReservationResponse>> response = new HashMap<>();
		for (Reservation reservation : reservations) {
			reservationResponses.add(buildReservationResponse(reservation, needsPassenger, needsSeatsLeft));
		}
		response.put("reservation", reservationResponses);
		return response;
	}

	public static FlightResponse getFlightResponse(Flight flight, boolean needPessagers, boolean needSeatsLeft) {
		FlightResponse flightResponse = new FlightResponse();
		flightResponse.setArrivalTime(flight.getArrivalTime());
		flightResponse.setDepartureTime(flight.getDepartureTime());
		flightResponse.setDescription(flight.getDescription());
		flightResponse.setFrom(flight.getFrom());
		flightResponse.setFlightNumber(flight.getNumber());
		flightResponse.setPlane(flight.getPlane());
		flightResponse.setPrice(flight.getPrice());
		if (needSeatsLeft) {
			flightResponse.setSeatsLeft(flight.getSeatsLeft());
		}
		flightResponse.setTo(flight.getTo());
		if (needPessagers) {
			List<Passenger> passengers = flight.getPassengers();
			if (passengers != null && !passengers.isEmpty()) {
				flightResponse.setPassengerResponse(new HashMap<>());
				flightResponse.getPassengers().put("passenger", new ArrayList<>());
				for (Passenger passenger : passengers) {
					flightResponse.getPassengers().get("passenger").add(buildPassengerResponse(passenger, false));
				}
			}
		}
		return flightResponse;
	}

	/*public static com.edu.sjsu.cmpe.service.model.xml.FlightResponse getFlightResponseXML(Flight flight) {
		com.edu.sjsu.cmpe.service.model.xml.FlightResponse flightResponse = new com.edu.sjsu.cmpe.service.model.xml.FlightResponse();
		flightResponse.setArrivalTime(flight.getArrivalTime());
		flightResponse.setDepartureTime(flight.getDepartureTime());
		flightResponse.setDescription(flight.getDescription());
		flightResponse.setFrom(flight.getFrom());
		flightResponse.setNumber(flight.getNumber());
		flightResponse.setPlane(flight.getPlane());
		flightResponse.setPrice(flight.getPrice());
		flightResponse.setSeatsLeft(flight.getSeatsLeft());
		flightResponse.setTo(flight.getTo());
		List<Passenger> passengers = flight.getPassengers();
		if (passengers != null && !passengers.isEmpty()) {
			flightResponse.setPassengers(new ArrayList<>());
			for (Passenger passenger : passengers) {
				flightResponse.getPassengers().add(ServiceUtil.getPassengerResponseXML(passenger));
			}
		}
		return flightResponse;
	}

	private static com.edu.sjsu.cmpe.service.model.xml.PassengerResponse getPassengerResponseXML(Passenger passenger) {
		com.edu.sjsu.cmpe.service.model.xml.PassengerResponse passengerXML = new com.edu.sjsu.cmpe.service.model.xml.PassengerResponse();
		passengerXML.setAge(passenger.getAge());
		passengerXML.setFirstName(passenger.getFirstName());
		passengerXML.setLastName(passenger.getLastName());
		passengerXML.setGender(passenger.getGender());
		passengerXML.setId(passenger.getId());
		passengerXML.setPhone(passenger.getPhone());
		return passengerXML;
	}*/

	public static PassengerResponse buildPassengerResponse(Passenger passenger, boolean needReservations) {
		PassengerResponse passengerResponse = new PassengerResponse();
		passengerResponse.setId(passenger.getId());
		passengerResponse.setAge(passenger.getAge());
		passengerResponse.setFirstName(passenger.getFirstName());
		passengerResponse.setLastName(passenger.getLastName());
		passengerResponse.setGender(passenger.getGender());
		passengerResponse.setPhone(passenger.getPhone());
		if (needReservations && passenger.getReservations() != null && !passenger.getReservations().isEmpty()) {
			passengerResponse.setReservations(new HashMap<>());
			passengerResponse.getReservations().put("reservation", new ArrayList<>());
			for (Reservation reservation : passenger.getReservations()) {
				passengerResponse.getReservations().get("reservation").add(buildReservationResponse(reservation, false, false));
			}
		}
		return passengerResponse;
	}

	public static List<Flight> getFlights(Passenger passenger) {
		List<Flight> flights = null;
		if (passenger.getReservations() != null && !passenger.getReservations().isEmpty()) {
			flights = new ArrayList<>();
			for (Reservation curReservation : passenger.getReservations()) {
				flights.addAll(curReservation.getFlights());
			}
		}
		return flights;
	}

	public static boolean isOverlapping(Flight flight, List<Flight> existingFlights) {
		for (Flight existingFlight : existingFlights) {
			if (flight.getDepartureTime().getTime() <= existingFlight.getArrivalTime().getTime()
					&& flight.getArrivalTime().getTime() >= existingFlight.getDepartureTime().getTime()) {
				return true;
			}
		}
		return false;
	}

	public static String getJson(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getXMLFromJson(String json) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			return XML.toString(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getXMLFromObject(Object object) {
		String jsonString = getJson(object);
		if (jsonString != null) {
			return getXMLFromJson(jsonString);
		} else {
			return null;
		}
	}

}
