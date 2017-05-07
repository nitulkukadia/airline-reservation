/**
 * Model class for generating Reservation response.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	21-04-2017
 */

package com.edu.sjsu.cmpe.service.model.json;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class ReservationResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private long orderNumber;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private PassengerResponse passenger;
	
	@JsonSerialize(using = ToStringSerializer.class)
	private int price;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Map<String, List<FlightResponse>> flights;

	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public PassengerResponse getPassenger() {
		return passenger;
	}

	public void setPassenger(PassengerResponse passenger) {
		this.passenger = passenger;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Map<String, List<FlightResponse>> getFlights() {
		return flights;
	}

	public void setFlights(Map<String, List<FlightResponse>> flights) {
		this.flights = flights;
	}
}
