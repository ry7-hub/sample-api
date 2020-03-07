package com.sample.springboot.controller;

import static com.sample.springboot.support.SecurityConstants.HEADER_STRING;
import static com.sample.springboot.support.SecurityConstants.SECRET;
import static com.sample.springboot.support.SecurityConstants.TOKEN_PREFIX;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.springboot.entity.Attendance;
import com.sample.springboot.entity.AttendanceKey;
import com.sample.springboot.entity.User;
import com.sample.springboot.exception.BadRequestException;
import com.sample.springboot.form.BatchForm;
import com.sample.springboot.form.RegistAttendanceForm;
import com.sample.springboot.form.UpdateStatusForm;
import com.sample.springboot.response.SimpleResponse;
import com.sample.springboot.response.UserResponse;
import com.sample.springboot.service.AttendanceService;

import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
	
	@Autowired
	AttendanceService attendanceService;
	
	@GetMapping("/list")
	public List<Attendance> attendanceList(@RequestParam("userId") Long userId, @RequestParam("date") String date) throws Exception {
		List<Attendance> data = attendanceService.findAttendances(userId, date);
		return data;
	}
	
	@GetMapping("/find")
	public Attendance findAttendance(@RequestParam("userId") Long userId, @RequestParam("date") String date) throws Exception {
		Attendance data = attendanceService.findAttendance(userId, date);
		return data;
	}
	
	@PostMapping("/regist")
	public Attendance registAttendance(@Validated @RequestBody RegistAttendanceForm form) {
		Attendance attendance = attendanceService.update(form);
		return attendance;
	}
	
	@PostMapping("/update/status")
	public Attendance updateStatus(@Validated @RequestBody UpdateStatusForm form) throws Exception {
		Attendance attendance = attendanceService.updateStatus(form);
		return attendance;
	}
	
	@PostMapping("/update/batch")
	public SimpleResponse batch(@Validated @RequestBody BatchForm form) throws Exception {
		SimpleResponse simpleResponse = attendanceService.batch(form);
		return simpleResponse;
	}

}
