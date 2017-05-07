/**
 * Model class for generating Map for the service response.
 * 
 * @author	Sushant Vairagade
 * @version 1.0
 * @Since	21-04-2017
 */

package com.edu.sjsu.cmpe.service.model.json;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Response")
public class Response {
	private String code;
	private String msg;
	
	public Response() {}
	
	public Response(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
}