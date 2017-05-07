/**
 * Service interface for Passenger entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	17-04-2017
 */

package com.edu.sjsu.cmpe.service;

import com.edu.sjsu.cmpe.dao.model.Passenger;
import com.edu.sjsu.cmpe.service.exception.BusinessException;

public interface PassengerService {

	Passenger createPassenger(String firstName, String lastName, int age, String gender, String phone)
			throws BusinessException;

	Passenger getPassenger(String id) throws BusinessException;

	Passenger updatePassenger(String id, String firstName, String lastName, int age, String gender, String phone);

	void deletePassenger(String id);
}
