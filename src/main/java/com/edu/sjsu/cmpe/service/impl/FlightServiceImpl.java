/**
 * Service class for Flight entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	17-04-2017
 */

package com.edu.sjsu.cmpe.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.sjsu.cmpe.dao.FlightDao;
import com.edu.sjsu.cmpe.dao.model.Flight;
import com.edu.sjsu.cmpe.dao.model.Passenger;
import com.edu.sjsu.cmpe.dao.model.Plane;
import com.edu.sjsu.cmpe.service.FlightService;
import com.edu.sjsu.cmpe.service.exception.BusinessException;
import com.edu.sjsu.cmpe.service.util.ServiceUtil;

@Component
public class FlightServiceImpl implements FlightService {

	@Autowired
	private FlightDao flightDao;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public Flight createOrUpdateFlight(String id, int price, String from, String to, String departureTime, String arrivalTime,
			String descr, int capacity, String model, String manufacturer, int yearOfManufacture)
					throws BusinessException {
		DateFormat format = new SimpleDateFormat("yy-MM-dd-HH", Locale.ENGLISH);
		boolean update = true;
		Flight flight = flightDao.findByNumber(id);
		if(flight == null) {
			flight = new Flight();
			update = false;
		}
		flight.setNumber(id);
		flight.setFrom(from);
		flight.setTo(to);
		try {
			flight.setArrivalTime(format.parse(arrivalTime));
			flight.setDepartureTime(format.parse(departureTime));
		} catch (ParseException e) {
			throw new BusinessException("400", "Invalid date.");
		}
		
		if(update) {
			int existingSeatsLeft = flight.getSeatsLeft();
			int existingCapacity = flight.getPlane().getCapacity();
			int reservedSeats = existingCapacity - existingSeatsLeft;
			//decreasing the capacity
			if(existingCapacity > capacity && capacity > reservedSeats) {
				throw new BusinessException("400", "Cannot decrease flight capacity to " + capacity + " as ther are "+ reservedSeats +" current active reservations");
			} else if(existingCapacity < capacity) {  //Increasing the capacity
				int increaseInSeats = capacity - existingCapacity;
				flight.setSeatsLeft(existingSeatsLeft + increaseInSeats);
			}
		} else {
			flight.setSeatsLeft(capacity);
		}
		flight.setPrice(price);
		flight.setDescription(descr);
		Plane plane = new Plane();
		plane.setManufacturer(manufacturer);
		plane.setModel(model);
		plane.setYearOfManufacture(yearOfManufacture);
		plane.setCapacity(capacity);
		flight.setPlane(plane);
		if (update) {
			Flight existingFlight = flightDao.findOne(id);
			for (Passenger passenger : existingFlight.getPassengers()) {
				List<Flight> existingPassengerflights = ServiceUtil.getFlights(passenger);
				for (Flight existingPassengerFlight : existingPassengerflights) {
					if (flight.getNumber() != existingPassengerFlight.getNumber()
							&& flight.getDepartureTime().getTime() <= existingPassengerFlight.getArrivalTime().getTime()
							&& flight.getArrivalTime().getTime() >= existingPassengerFlight.getDepartureTime()
							.getTime()) {
						throw new BusinessException("400",
								"Can't update this flight as it causes overlapping with flight of passenger with id "
										+ passenger.getId() + " having reservation of flight with number "
										+ existingFlight.getNumber());
					}
				}
			}
		}

		flight = flightDao.save(flight);
		return flight;
	}

	@Override
	@Transactional
	public Flight getFlight(String flightNumber) throws BusinessException {
		Flight flight = flightDao.findByNumber(flightNumber);
		if (flight != null) {
			return flight;
		} else {
			throw new BusinessException("404",
					"Sorry, the requested flight with number " + flightNumber + " does not exist");
		}
	}

	@Override
	@Transactional(rollbackOn=Exception.class)
	public void deleteFlight(String flightNumber) throws BusinessException {
		Flight flight = flightDao.findByNumber(flightNumber);
		if (flight != null) {
			if (flight.getSeatsLeft() == flight.getPlane().getCapacity()) {
				flightDao.delete(flight);
			} else {
				throw new BusinessException("400",
						"Sorry, the flight with number " + flightNumber + " cannot be deleted.");
			}
		} else {
			throw new BusinessException("404",
					"Sorry, the requested flight with number " + flightNumber + " does not exist");
		}
	}
}
