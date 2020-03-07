package com.sample.springboot.exception;

import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class BadRequestException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private Integer status;
    private String message;
	
	public BadRequestException(Integer status, String message) {
		this.status = status;
        this.message = message;
	}
	
	public String toJson() {
		String json = "";
		try {
			HashMap<String, Object> ret = new HashMap<String, Object>();
			ret.put("status", this.status);
			ret.put("message", this.message);
			json = new ObjectMapper().writeValueAsString(ret);
		} catch (JsonProcessingException e) {
			// 何もしない
		}
		return json;
	}
}
