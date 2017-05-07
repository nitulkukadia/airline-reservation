/**
 * Model class for generating Flight response.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	21-04-2017
 */

package com.edu.sjsu.cmpe.service.model.json;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.edu.sjsu.cmpe.dao.model.Plane;
import com.edu.sjsu.cmpe.service.util.DateAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@XmlRootElement(name="flight")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlightResponse {
	
	private String flightNumber;
	
	@JsonSerialize(using = ToStringSerializer.class)
	private int price;
	
    private String from;
    
	private String to;

    /*  Date format: yy-mm-dd-hh, do not include minutes and seconds.
    ** Example: 2017-03-22-19
    */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd-HH")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date departureTime;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd-HH")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date arrivalTime;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer seatsLeft; 
    
    private String description;
   
    private Plane plane;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, List<PassengerResponse>> passengers;
    
	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String number) {
		this.flightNumber = number;
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

	public Integer getSeatsLeft() {
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

	public Map<String, List<PassengerResponse>> getPassengers() {
		return passengers;
	}

	public void setPassengerResponse(Map<String, List<PassengerResponse>> passengers) {
		this.passengers = passengers;
	}
}