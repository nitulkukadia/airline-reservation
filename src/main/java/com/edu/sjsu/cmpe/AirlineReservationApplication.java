/**
 * Spring boot application server class.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	17-04-2017
 */

package com.edu.sjsu.cmpe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AirlineReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlineReservationApplication.class, args);
	}
}