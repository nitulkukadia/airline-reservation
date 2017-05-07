package com.edu.sjsu.cmpe;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.edu.sjsu.cmpe.dao.PassengerDao;
import com.edu.sjsu.cmpe.dao.model.Passenger;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AirlineReservationApplication.class)
@WebAppConfiguration
public class AirlineReservationApplicationTests {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	private MockMvc mockMvc;
	
	private ObjectMapper mapper = new ObjectMapper();

	private Passenger passenger;

	@Autowired
	private PassengerDao passengerDao;

	@Autowired
	private WebApplicationContext webApplicationContext;


	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		passenger = new Passenger();
		passenger.setAge(20);
		passenger.setFirstName("Sushant");
		passenger.setLastName("Vairagade");
		passenger.setGender("male");
		passenger.setPhone("020202");
		passengerDao.save(passenger);
		//this.bookmarkList.add(bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/1/" + userName, "A description")));
		//this.bookmarkList.add(bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/2/" + userName, "A description")));
	}

	@After
	public void destroy() throws Exception {
		passengerDao.delete(passenger.getId());
	}

	//@Test
	public void contextLoads() {
	}


	//Passenger test cases
	@Test
	public void passengerNotFound() throws Exception {
		mockMvc.perform(get("/passenger/abc")).andExpect(status().isNotFound());
	}

	@Test
	public void getPassenger() throws Exception {
		mockMvc.perform(get("/passenger/" + passenger.getId()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(contentType))
		.andExpect(jsonPath("$.passenger.id", is(String.valueOf(passenger.getId()))))
		.andExpect(jsonPath("$.passenger.age", is(String.valueOf(passenger.getAge()))));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createPassenger() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/passenger?firstname=Try&lastname=Me&age=11&gender=famale&phone=9090"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(contentType))
		.andExpect(jsonPath("$.passenger.firstname", is("Try")))
		.andExpect(jsonPath("$.passenger.lasttname", is("Me")))
		.andExpect(jsonPath("$.passenger.age", is("11"))).andReturn();
		
		Map<String, Map<String, String>> newPassengerRseponse = (Map<String, Map<String,String>>) mapper.readValue(mvcResult.getResponse().getContentAsString(), Object.class);
		String newPassenger = newPassengerRseponse.get("passenger").get("id");
		mockMvc.perform(delete("/passenger/"+ newPassenger))
				.andExpect(status().isOk());
	}
}
