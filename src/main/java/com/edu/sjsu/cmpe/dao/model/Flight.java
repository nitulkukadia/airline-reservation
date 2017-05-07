/**
 * Model class for Flight entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	17-04-2017
 */

package com.edu.sjsu.cmpe.dao.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.edu.sjsu.cmpe.service.util.DateAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
@Table(name="flight")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Flight {
	
	@Id
	@Column(name="ID")
	private String number; // Each flight has a unique flight number.
	
	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "PRICE")
	private int price;
	
	@Column(name = "LOC_FROM", nullable = false)
    private String from;
    
	@Column(name = "LOC_TO", nullable = false)
	private String to;

    /*  Date format: yy-mm-dd-hh, do not include minutes and seconds.
    ** Example: 2017-03-22-19
    */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yy-MM-dd-HH")
	@Column(name = "DEPARTURE", nullable = false)
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date departureTime;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yy-MM-dd-HH")
	@Column(name = "ARRIVAL", nullable = false)
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date arrivalTime;
    
    @Column(name = "SEATS_LEFT")
    private int seatsLeft; 
    
    @Column(name = "DESCR")
    private String description;
   
    @Embedded
    private Plane plane;
    
    @ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="flight_passenger",
			joinColumns= {@JoinColumn(name="FLIGHT_ID", referencedColumnName="ID")},
			inverseJoinColumns= {@JoinColumn(name="PASSENGER_ID", referencedColumnName="ID")})
    @XmlTransient
    private List<Passenger> passengers;
    

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getSeatsLeft() {
		return seatsLeft;
	}

	public void setSeatsLeft(int seatsLeft) {
		this.seatsLeft = seatsLeft;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}
}
