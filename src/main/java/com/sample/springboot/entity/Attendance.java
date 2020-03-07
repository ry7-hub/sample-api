package com.sample.springboot.entity;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "attendance")
public class Attendance {
	
//	private Long user_id;
//	private Date attendance_date;
	@Id
	private AttendanceKey key;
	private String day_of_week;
	private String kind;
	private Time start_time;
	private Time end_time;
	private Time break_start_time;
	private Time break_end_time;
	private Time all_time;
	private Time break_all_time;
	private Time overtime;
	private String remarks;
	private String aproval_kind;
	private Date create_date;
	private Date update_date;

}
