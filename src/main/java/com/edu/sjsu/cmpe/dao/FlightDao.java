/**
 * Dao interface for Flight entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	17-04-2017
 */

package com.edu.sjsu.cmpe.dao;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.edu.sjsu.cmpe.dao.model.Flight;

@Transactional
public interface FlightDao extends CrudRepository<Flight, String> {
	
	Flight findByNumber(String number);
	
	void deleteByNumber(String number);
	
	Set<Flight> findAllByFrom(String from);
	
	Set<Flight> findAllByTo(String to);
}
