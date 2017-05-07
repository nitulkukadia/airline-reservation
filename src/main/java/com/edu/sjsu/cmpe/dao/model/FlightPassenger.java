/**
 * Model class for FlightPassenger entity.
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

import com.edu.sjsu.cmpe.dao.model.id.FlightPassengerId;

@Entity
@Table(name="flight_passenger")
@IdClass(FlightPassengerId.class)
public class FlightPassenger {

	@Id
	@Column(name="FLIGHT_ID")
	private String flightNumber;
	
	@Id
	@Column(name="PASSENGER_ID")
	private long passengerId;

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public long getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(long passengerId) {
		this.passengerId = passengerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flightNumber == null) ? 0 : flightNumber.hashCode());
		result = prime * result + (int) (passengerId ^ (passengerId >>> 32));
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
		FlightPassenger other = (FlightPassenger) obj;
		if (flightNumber == null) {
			if (other.flightNumber != null)
				return false;
		} else if (!flightNumber.equals(other.flightNumber))
			return false;
		if (passengerId != other.passengerId)
			return false;
		return true;
	}
}
