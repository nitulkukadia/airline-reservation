/**
 * Model class for Plane entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	17-04-2017
 */

package com.edu.sjsu.cmpe.dao.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Embeddable
@XmlRootElement
public class Plane {
	
	@JsonProperty("model")
	@Column(name = "PLANE_MODEL")
	private String model;
	
	@JsonProperty("manufacturer")
	@Column(name = "PLANE_MANUFACTURER")
	private String manufacturer;
	
	@JsonProperty("yearOfManufacture")
	@Column(name = "PLANE_YEAR_OF_MANUFACTURE")
	@JsonSerialize(using = ToStringSerializer.class)
	private int yearOfManufacture;
	
	
	@JsonProperty("capacity")
	@Column(name = "PLANE_CAPACITY")
	@JsonSerialize(using = ToStringSerializer.class)
	private int capacity;
	

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getYearOfManufacture() {
		return yearOfManufacture;
	}

	public void setYearOfManufacture(int yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
