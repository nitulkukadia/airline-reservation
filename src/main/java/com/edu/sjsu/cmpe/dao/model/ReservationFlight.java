/**
 * Model class for ReservationFlight entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	17-04-2017
 */

package com.edu.sjsu.cmpe.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.edu.sjsu.cmpe.dao.model.id.ReservationFlightId;

@Entity
@Table(name="reservation_flight")
@IdClass(ReservationFlightId.class)
public class ReservationFlight {
	
	@Id
	@Column(name="RESERVATION_ID")
	private long reservationId;
	
	@Id
	@Column(name="FLIGHT_ID")
	private String flightNumber;

	public long getReservationId() {
		return reservationId;
	}

	public void setReservationId(long reservationId) {
		this.reservationId = reservationId;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	
}
