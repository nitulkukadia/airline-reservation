/**
 * Service interface for Reservation entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	21-04-2017
 */

package com.edu.sjsu.cmpe.service;

import java.util.Set;

import com.edu.sjsu.cmpe.dao.model.Reservation;
import com.edu.sjsu.cmpe.service.exception.BusinessException;

public interface ReservationService {

	Reservation getReservation(long reservationId) throws BusinessException;
	
	Reservation createReservation(long passengerId, String[] flightNumbers) throws BusinessException;
	
	Reservation updateReservation(long reservationId, String[] flightsToAdd, String[] flightsToRemove) throws BusinessException;
	
	void cancelReservation(long reservationId) throws BusinessException;
	
	Set<Reservation> searchReservations(long passengerId, String fromCity, String toCity, String flightNumber);
	
}