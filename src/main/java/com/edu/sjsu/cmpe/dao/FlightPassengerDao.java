/**
 * Dao interface for FlightPassenger entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	17-04-2017
 */

package com.edu.sjsu.cmpe.dao;

import org.springframework.data.repository.CrudRepository;

import com.edu.sjsu.cmpe.dao.model.FlightPassenger;
import com.edu.sjsu.cmpe.dao.model.id.FlightPassengerId;

public interface FlightPassengerDao extends CrudRepository<FlightPassenger, FlightPassengerId> {
	
	FlightPassenger findByPassengerId(long passengerId);

}