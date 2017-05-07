/**
 * Dao interface for Reservation entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	21-04-2017
 */

package com.edu.sjsu.cmpe.dao;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.edu.sjsu.cmpe.dao.model.Reservation;

@Transactional
public interface ReservationDao extends CrudRepository<Reservation, Long> {

	Set<Reservation> findByPassengerId(long passengerId);
		
}