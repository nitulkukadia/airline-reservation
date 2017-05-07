package com.edu.sjsu.cmpe.service.model.xml;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.edu.sjsu.cmpe.dao.model.Plane;
import com.edu.sjsu.cmpe.service.util.DateAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;

@XmlRootElement(name="flight")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlightResponse {
	
	private String number;
	
	private int price;
	
    private String from;
    
	private String to;

    /*  Date format: yy-mm-dd-hh, do not include minutes and seconds.
    ** Example: 2017-03-22-19
    */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yy-MM-dd-HH")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date departureTime;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yy-MM-dd-HH")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date arrivalTime;
    
    private int seatsLeft; 
    
    private String description;
   
    private Plane plane;
    
    @XmlElementWrapper(name = "passengers")
	@XmlElement(name="passenger")
    private List<PassengerResponse> passengers;
    
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

	public List<PassengerResponse> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<PassengerResponse> passengers) {
		this.passengers = passengers;
	}
}