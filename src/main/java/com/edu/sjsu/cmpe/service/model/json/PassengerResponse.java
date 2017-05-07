/**
 * Model class for generating Passenger response.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	21-04-2017
 */

package com.edu.sjsu.cmpe.service.model.json;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class PassengerResponse {

	@JsonSerialize(using = ToStringSerializer.class)
	private long id;

	@JsonProperty("firstname")
	private String firstName;

	@JsonProperty("lasttname")
	private String lastName;

	@JsonSerialize(using = ToStringSerializer.class)
	private int age;

	private String gender;
	private String phone;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Map<String, List<ReservationResponse>> reservations;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Map<String, List<ReservationResponse>> getReservations() {
		return reservations;
	}

	public void setReservations(Map<String, List<ReservationResponse>> reservations) {
		this.reservations = reservations;
	}
}
