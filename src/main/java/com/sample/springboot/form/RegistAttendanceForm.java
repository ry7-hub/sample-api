package com.sample.springboot.form;

import lombok.Data;

@Data
public class RegistAttendanceForm {
	private Long user_id;
	private String attendance_date;
	private String kind;
	private String start_time;
	private String end_time;
	private String break_start_time;
	private String break_end_time;
	private String remarks;
	private String aproval_kind;
}
