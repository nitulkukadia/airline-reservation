/**
 * Service class for Reservation entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	04-05-2017
 */

package com.edu.sjsu.cmpe.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.sjsu.cmpe.dao.FlightDao;
import com.edu.sjsu.cmpe.dao.FlightPassengerDao;
import com.edu.sjsu.cmpe.dao.PassengerDao;
import com.edu.sjsu.cmpe.dao.ReservationDao;
import com.edu.sjsu.cmpe.dao.ReservationFlightDao;
import com.edu.sjsu.cmpe.dao.model.Flight;
import com.edu.sjsu.cmpe.dao.model.Passenger;
import com.edu.sjsu.cmpe.dao.model.Reservation;
import com.edu.sjsu.cmpe.dao.model.ReservationFlight;
import com.edu.sjsu.cmpe.dao.model.id.FlightPassengerId;
import com.edu.sjsu.cmpe.service.ReservationService;
import com.edu.sjsu.cmpe.service.exception.BusinessException;
import com.edu.sjsu.cmpe.service.util.ServiceUtil;

@Component
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private PassengerDao passengerDao;

	@Autowired
	private FlightDao flightDao;

	@Autowired
	private ReservationDao reservationDao;

	@Autowired
	private FlightPassengerDao flightPassengerDao;

	@Autowired
	private ReservationFlightDao reservationFlightDao;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public Reservation createReservation(long passengerID, String[] flightNumbers) throws BusinessException {
		Reservation reservation = new Reservation();
		Passenger passenger = passengerDao.findOne(passengerID);
		if (passenger != null) {
			reservation.setPassenger(passenger);
			List<Flight> flights = new ArrayList<>();
			List<Flight> existingFlights = ServiceUtil.getFlights(passenger);
			int price = 0;
			for (String flightNumber : flightNumbers) {
				Flight flight = flightDao.findByNumber(flightNumber);
				if (flight != null) {
					if (existingFlights == null || !ServiceUtil.isOverlapping(flight, existingFlights)) {
						int seatsLeft = flight.getSeatsLeft();
						if (seatsLeft > 0) {
							List<Passenger> passengers = flight.getPassengers();
							if (passengers == null) {
								passengers = new ArrayList<>();
							}
							passengers.add(passenger);
							flight.setPassengers(passengers);
							flight.setSeatsLeft(flight.getSeatsLeft() - 1);
							flight = flightDao.save(flight);
							flights.add(flight);
							if (existingFlights == null) {
								existingFlights = new ArrayList<>();
							}
							existingFlights.add(flight);
							price += flight.getPrice();
						} else {
							throw new BusinessException("400",
									"Sorry, the requested flight with number " + flightNumber + " is full.");
						}
					} else {
						throw new BusinessException("400", "Sorry, the requested flight with number " + flightNumber
								+ " has an overlapping schedule.");
					}
				} else {
					throw new BusinessException("404",
							"Sorry, the requested flight with number " + flightNumber + " does not exist");
				}
			}
			reservation.setFlights(flights);
			reservation.setPrice(price);
			reservation = reservationDao.save(reservation);
			passenger = passengerDao.save(passenger);
			return reservation;
		} else {
			throw new BusinessException("404",
					"Sorry, the requested passenger with id " + passengerID + " does not exist");

		}
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void cancelReservation(long reservationId) throws BusinessException {
		try {
			Reservation reservation = reservationDao.findOne(reservationId);
			if (reservation != null) {
				List<Flight> flights = reservation.getFlights();
				for (Flight flight : flights) {
					// There's no need to delete from ReservationFlight due to
					// mapping
					/*
					 * ReservationFlightId reservationFlightId = new
					 * ReservationFlightId();
					 * reservationFlightId.setFlightNumber(flight.getNumber());
					 * reservationFlightId.setReservationId(reservationId);
					 * reservationFlightDao.delete(reservationFlightId);
					 */

					FlightPassengerId flightPassengerId = new FlightPassengerId();
					flightPassengerId.setFlightNumber(flight.getNumber());
					flightPassengerId.setPassengerId(reservation.getPassenger().getId());
					flightPassengerDao.delete(flightPassengerId);

					flight.setSeatsLeft(flight.getSeatsLeft() + 1);
					flightDao.save(flight);
					// the only passenger then remove the flight
					/*
					 * if(flight.getSeatsLeft() ==
					 * flight.getPlane().getCapacity()) {
					 * flightDao.delete(flight.getNumber()); }
					 */
				}
				reservationDao.delete(reservationId);
			} else {
				throw new BusinessException("404",
						"Sorry, the requested reservation with number " + reservationId + " does not exist");
			}
		} catch (Exception e) {
			throw new BusinessException("500",
					"Something went wrong while cancelling reservation with number " + reservationId);
		}
	}

	@Override
	@Transactional
	public Reservation getReservation(long reservationId) throws BusinessException {
		Reservation reservation = reservationDao.findOne(reservationId);
		if (reservation != null) {
			return reservation;
		} else {
			throw new BusinessException("404",
					"Sorry, the requested reservation with number " + reservationId + " does not exist");
		}
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public Reservation updateReservation(long reservationId, String[] flightsToAdd, String[] flightsToRemove)
			throws BusinessException {
		Reservation reservation = getReservation(reservationId);
		Passenger passenger = passengerDao.findOne(reservation.getPassenger().getId());
		List<Flight> existingFlights = ServiceUtil.getFlights(passenger);
		if (flightsToRemove != null && flightsToRemove.length > 0) {
			for (String flight : flightsToRemove) {
				cancelFlight(flight, reservation, existingFlights);
			}
		}

		if (flightsToAdd != null && flightsToAdd.length > 0) {
			for (String flight : flightsToAdd) {
				addFlight(flight, reservation, passenger, existingFlights);
			}
		}
		return reservation;
	}

	@Transactional(rollbackOn = Exception.class)
	private void addFlight(String flightNumber, Reservation reservation, Passenger passenger,
			List<Flight> existingFlights) throws BusinessException {
		Flight flight = flightDao.findByNumber(flightNumber);
		if (flight != null) {
			if (existingFlights == null || !ServiceUtil.isOverlapping(flight, existingFlights)) {
				int seatsLeft = flight.getSeatsLeft();
				if (seatsLeft > 0) {
					if (existingFlights == null) {
						existingFlights = new ArrayList<>();
					}
					existingFlights.add(flight);
					reservation.getFlights().add(flight);
					reservation.setPrice(reservation.getPrice() + flight.getPrice());
					System.out.println("Saving reservation === > ");
					reservation = reservationDao.save(reservation);
					System.out.println("Saved reservation === > ");

					List<Passenger> passengers = flight.getPassengers();
					if (passengers == null) {
						passengers = new ArrayList<>();
					}
					passengers.add(passenger);
					flight.setSeatsLeft(flight.getSeatsLeft() - 1);
					System.out.println("Saving flight === > ");
					flight = flightDao.save(flight);
					System.out.println("Saved flight === > ");
				} else {
					throw new BusinessException("400",
							"Sorry, the requested flight with number " + flightNumber + " is full.");
				}
			} else {
				throw new BusinessException("400",
						"Sorry, the requested flight with number " + flightNumber + " has an overlapping schedule.");
			}
		} else {
			throw new BusinessException("404",
					"Sorry, the requested flight with number " + flightNumber + " does not exist");
		}
	}

	@Transactional(rollbackOn = Exception.class)
	private void cancelFlight(String flight, Reservation reservation, List<Flight> existingFlights)
			throws BusinessException {
		int index = -1;
		List<Flight> existingReservationFlights = reservation.getFlights();
		for (int i = 0; i < existingReservationFlights.size(); i++) {
			Flight existingFlight = existingReservationFlights.get(i);
			if (flight.equals(existingFlight.getNumber())) {
				index = i;
				// No need to remove reservation link as it will be done in
				// cascading
				/*
				 * ReservationFlightId reservationFlightId = new
				 * ReservationFlightId();
				 * reservationFlightId.setFlightNumber(flight);
				 * reservationFlightId.setReservationId(reservation.
				 * getOrderNumber());
				 * reservationFlightDao.delete(reservationFlightId);
				 * System.out.println(reservationFlightDao.findOne(
				 * reservationFlightId));
				 * System.out.println("reservationFlightDao ===> "+
				 * reservationFlightId.getFlightNumber() +
				 * reservationFlightId.getReservationId());
				 * System.out.println(reservationFlightDao.findOne(
				 * reservationFlightId));
				 */

				// Remove Flight Passenger link
				FlightPassengerId flightPassengerId = new FlightPassengerId();
				flightPassengerId.setFlightNumber(flight);
				flightPassengerId.setPassengerId(reservation.getPassenger().getId());
				flightPassengerDao.delete(flightPassengerId);

				existingFlight.setSeatsLeft(existingFlight.getSeatsLeft() + 1);
				flightDao.save(existingFlight);

				// Update reservation price
				reservation.setPrice(reservation.getPrice() - existingFlight.getPrice());
				// reservation = reservationDao.save(reservation);
				existingReservationFlights.remove(index);
				reservationDao.save(reservation);
				for (int j = 0; j < existingFlights.size(); j++) {
					if (existingFlights.get(j).getNumber().equals(flight)) {
						existingFlights.remove(j);
						return;
					}
				}
			}
		}
	}

	@Override
	public Set<Reservation> searchReservations(long passengerId, String fromCity, String toCity, String flightNumber) {
		Set<Reservation> responseSet = new HashSet<>();
		Set<Long> reservationNumbers = new HashSet<>();
		boolean hasCopied = false;

		if (passengerId > 0) {
			Set<Reservation> reservations = reservationDao.findByPassengerId(passengerId);
			Set<Long> passengerReservationNumbers = new HashSet<>();

			for (Reservation reservation : reservations) {
				passengerReservationNumbers.add(reservation.getOrderNumber());
			}
			if (passengerReservationNumbers.size() == 0) {
				return responseSet;
			} else {
				hasCopied = true;
				reservationNumbers.addAll(passengerReservationNumbers);
			}
		}

		if (flightNumber != null && !flightNumber.isEmpty()) {
			List<ReservationFlight> reservations = reservationFlightDao.findByFlightNumber(flightNumber);
			Set<Long> flightReservationNumbers = new HashSet<>();

			for (ReservationFlight reservationFlight : reservations) {
				flightReservationNumbers.add(reservationFlight.getReservationId());
			}
			if (flightReservationNumbers.size() == 0) {
				return responseSet;
			} else {
				if (hasCopied) {
					reservationNumbers.retainAll(flightReservationNumbers);
					if (reservationNumbers.size() == 0) {
						return responseSet;
					}
				} else {
					hasCopied = true;
					reservationNumbers.addAll(flightReservationNumbers);
				}
			}
		}

		if (fromCity != null && !fromCity.isEmpty()) {
			Set<Long> reservations = reservationFlightDao.findReservationsByFromCity(fromCity);
			Set<Long> fromCityReservatoinNumbers = new HashSet<>();

			if (reservations != null && reservations.size() > 0) {
				fromCityReservatoinNumbers.addAll(reservations);
				if (hasCopied) {
					reservationNumbers.retainAll(fromCityReservatoinNumbers);
					if (reservationNumbers.size() == 0) {
						return responseSet;
					}
				} else {
					hasCopied = true;
					reservationNumbers.addAll(fromCityReservatoinNumbers);
				}
			} else {
				return responseSet;
			}
		}

		if (toCity != null && !toCity.isEmpty()) {
			Set<Long> reservations = reservationFlightDao.findReservationsByToCity(toCity);
			Set<Long> toCityReservatoinNumbers = new HashSet<>();

			if (reservations != null && reservations.size() > 0) {
				toCityReservatoinNumbers.addAll(reservations);
				if (hasCopied) {
					reservationNumbers.retainAll(toCityReservatoinNumbers);
					if (reservationNumbers.size() == 0) {
						return responseSet;
					}
				} else {
					hasCopied = true;
					reservationNumbers.addAll(toCityReservatoinNumbers);
				}
			} else {
				return responseSet;
			}
		}
		Iterable<Reservation> reservations = reservationDao.findAll(reservationNumbers);
		for (Reservation reservation : reservations) {
			responseSet.add(reservation);
		}
		return responseSet;
	}
}