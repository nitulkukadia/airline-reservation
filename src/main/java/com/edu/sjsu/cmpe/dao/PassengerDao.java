/**
 * Dao interface for Passenger entity.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	17-04-2017
 */

package com.edu.sjsu.cmpe.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.edu.sjsu.cmpe.dao.model.Passenger;

@Transactional
public interface PassengerDao extends CrudRepository<Passenger, Long> {

}
