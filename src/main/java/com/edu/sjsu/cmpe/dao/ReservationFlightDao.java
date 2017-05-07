/**
 * Dao interface for ReservarionFlight entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	21-04-2017
 */

package com.edu.sjsu.cmpe.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.edu.sjsu.cmpe.dao.model.ReservationFlight;
import com.edu.sjsu.cmpe.dao.model.id.ReservationFlightId;

public interface ReservationFlightDao extends CrudRepository<ReservationFlight, ReservationFlightId> {
	
	@Query("select rf.reservationId from ReservationFlight rf, Flight f where f.from= :fromCity and rf.flightNumber = f.id")
    public Set<Long> findReservationsByFromCity(String fromCity);
	
	@Query("select rf.reservationId from ReservationFlight rf, Flight f where f.to = :toCity and rf.flightNumber = f.id")
    public Set<Long> findReservationsByToCity(String toCity);
	
	public List<ReservationFlight> findByFlightNumber(String flightNumber);
}