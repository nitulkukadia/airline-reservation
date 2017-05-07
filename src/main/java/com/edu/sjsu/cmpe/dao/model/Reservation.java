/**
 * Model class for Reservation entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	17-04-2017
 */

package com.edu.sjsu.cmpe.dao.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
@Table(name="reservation")
@XmlRootElement
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private long orderNumber;

	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "PRICE", nullable = false)
	private int price;

	@ManyToOne(optional=false)
	@JoinColumn(name="PASSENGER_ID", referencedColumnName="ID")
	private Passenger passenger;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name="reservation_flight",
			joinColumns= {@JoinColumn(name="RESERVATION_ID", referencedColumnName="ID")},
			inverseJoinColumns= {@JoinColumn(name="FLIGHT_ID", referencedColumnName="ID")})
	private List<Flight> flights;

	public long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@XmlTransient
	public Passenger getPassenger() {
		return passenger;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

	@XmlElementWrapper(name = "flights")
	@XmlElement(name="flight")
	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}
}
