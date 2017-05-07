/**
 * Model class for Passenger entity.
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
@Table(name="passenger")
@XmlRootElement
public class Passenger {

	@JsonSerialize(using = ToStringSerializer.class)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private long id;   

	@JsonProperty("firstname")
	@Column(name = "FIRST_NAME", nullable = false, length = 20)
	private String firstName;

	@JsonProperty("lastname")
	@Column(name = "LAST_NAME", nullable = false, length = 20)
	private String lastName;

	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "AGE", nullable = false)
	private int age;
	
	@Column(name = "GENDER", nullable = false)
	private String gender;
	
	@Column(name = "PHONE", nullable = false)
	private String phone;
	
	@OneToMany(mappedBy="passenger", fetch=FetchType.LAZY)
	private List<Reservation> reservations;
	
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

	@XmlElementWrapper(name = "reservations")
	@XmlElement(name="reservation")
	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
}
