package com.sample.springboot.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class AttendanceKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long user_id;
	private String attendance_date;
}
