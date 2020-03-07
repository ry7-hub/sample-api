package com.sample.springboot.form;

import lombok.Data;

@Data
public class UpdateStatusForm {
	private Long user_id;
	private String date;
	private String status;
}
