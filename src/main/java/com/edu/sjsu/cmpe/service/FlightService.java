/**
 * Service interface for Flight entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	17-04-2017
 */

package com.edu.sjsu.cmpe.service;

import com.edu.sjsu.cmpe.dao.model.Flight;
import com.edu.sjsu.cmpe.service.exception.BusinessException;

public interface FlightService {

	Flight createOrUpdateFlight(String id, int price, String from, String to, String departureTime, String arrivalTime,
			String descr, int capacity, String model, String manufacturer, int yearOfManufacture)
			throws BusinessException;
	
	Flight getFlight(String flightNumber) throws BusinessException;
	
	void deleteFlight(String flightNumber) throws BusinessException;

}
