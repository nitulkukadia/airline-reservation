/**
 * Id class for REservationFlight composite key.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	17-04-2017
 */

package com.edu.sjsu.cmpe.dao.model.id;

import java.io.Serializable;

public class ReservationFlightId implements Serializable {
		
	private static final long serialVersionUID = 1L;
	private long reservationId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flightNumber == null) ? 0 : flightNumber.hashCode());
		result = prime * result + (int) (reservationId ^ (reservationId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReservationFlightId other = (ReservationFlightId) obj;
		if (flightNumber == null) {
			if (other.flightNumber != null)
				return false;
		} else if (!flightNumber.equals(other.flightNumber))
			return false;
		if (reservationId != other.reservationId)
			return false;
		return true;
	}
}
