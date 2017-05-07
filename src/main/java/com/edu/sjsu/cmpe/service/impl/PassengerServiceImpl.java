/**
 * Service class for Passenger entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	17-04-2017
 */

package com.edu.sjsu.cmpe.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.sjsu.cmpe.dao.PassengerDao;
import com.edu.sjsu.cmpe.dao.model.Passenger;
import com.edu.sjsu.cmpe.dao.model.Reservation;
import com.edu.sjsu.cmpe.service.PassengerService;
import com.edu.sjsu.cmpe.service.ReservationService;
import com.edu.sjsu.cmpe.service.exception.BusinessException;

@Component
public class PassengerServiceImpl implements PassengerService {

	@Autowired
	private PassengerDao passengerDao;

	@Autowired 
	private ReservationService reservationService;

	@Override
	@Transactional
	public Passenger createPassenger(String firstName, String lastName, int age, String gender, String phone)
			throws BusinessException {
		Passenger passenger = new Passenger();
		passenger.setFirstName(firstName);
		passenger.setLastName(lastName);
		passenger.setAge(age);
		passenger.setGender(gender);
		passenger.setPhone(phone);
		try {
			passengerDao.save(passenger);
		} catch (Exception e) {
			throw new BusinessException("400", "DB constraint violated, please make sure phone number is unique");
		}
		return passenger;
	}

	@Override
	@Transactional
	public Passenger getPassenger(String id) throws BusinessException {
		Passenger passenger = null;
		try {
			if(!id.startsWith("0")) {
				try {
					long passengerId = Long.parseLong(id);
					passenger = passengerDao.findOne(passengerId);
					return passenger;
				} catch(NumberFormatException nfe) {

				}
			}
			throw new BusinessException("404", "Sorry, the requested passenger with id " + id + " does not exist");
		} catch (Exception e) {
			if(e instanceof BusinessException) {
				throw e;
			}
			throw new BusinessException("400", e.getMessage());
		}
	}

	@Override
	@Transactional
	public Passenger updatePassenger(String id, String firstName, String lastName, int age, String gender, String phone) throws BusinessException {
		Passenger passenger = getPassenger(id);
		if (passenger != null) {
			passenger.setFirstName(firstName);
			passenger.setLastName(lastName);
			passenger.setAge(age);
			passenger.setGender(gender);
			passenger.setPhone(phone);
			try {
				System.out.println("Saving");
				passengerDao.save(passenger);
				System.out.println("Saved");
			} catch (Exception e) {
				throw new BusinessException("400", "DB constraint violated, please make sure phone number is unique");
			}
			System.out.println("No exception");
			return passenger;
		} else {
			throw new BusinessException("404",
					"Sorry, the requested passenger with id " + id + " does not exist");
		}
	}

	@Override
	@Transactional
	public void deletePassenger(String id)  throws BusinessException {
		Passenger passenger = getPassenger(id);
		if (passenger != null) {
			for (Reservation reservation : passenger.getReservations()) {
				reservationService.cancelReservation(reservation.getOrderNumber());
			}
			passengerDao.delete(passenger.getId());
		} else {
			throw new BusinessException("404",
					"Sorry, the requested passenger with id " + id + " does not exist");
		}
	}
}
