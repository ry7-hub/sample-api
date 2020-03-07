package com.sample.springboot.service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sample.springboot.entity.Attendance;
import com.sample.springboot.entity.AttendanceKey;
import com.sample.springboot.form.BatchForm;
import com.sample.springboot.form.RegistAttendanceForm;
import com.sample.springboot.form.UpdateStatusForm;
import com.sample.springboot.repository.AttendanceRepository;
import com.sample.springboot.response.SimpleResponse;

@Service
@Transactional
public class AttendanceService {
	
	@Autowired
	AttendanceRepository attendanceRepository;
	
	public List<Attendance> findAttendances(Long userId, String date) {
		
		List<Attendance> data = attendanceRepository.findAttendances(userId, date);
		
		if(data.size() == 0) {
			data = new ArrayList<Attendance>();
			
			// dateからその月の１か月分データを作成
			String year = date.substring(0, 4);
			String month = date.substring(4);
			
			LocalDate localDate = LocalDate.of(Integer.parseInt(year),  Integer.parseInt(month), 1);
			Integer lastDay = localDate.lengthOfMonth();
			
			// 途中でエラーになったときは考慮してない
			for(int i = 0; i < lastDay; i++) {
				LocalDate targetDate = localDate.plusDays(i);
				String dayOfWeek = targetDate.getDayOfWeek().toString();
				
				Attendance attendance = new Attendance();
				AttendanceKey key = new AttendanceKey();
				key.setUser_id(userId);
				key.setAttendance_date(targetDate.getYear() + String.format("%02d", targetDate.getMonthValue()) +String.format("%02d",  targetDate.getDayOfMonth()));
				attendance.setKey(key);
				attendance.setDay_of_week(dayOfWeek);
				if("SUNDAY".equals(dayOfWeek) || "SATURDAY".equals(dayOfWeek)) {
					attendance.setKind("公休");
				}
				attendance.setAproval_kind("未申請");
				Attendance createData = create(attendance);
				data.add(createData);
			}
		}
		
		return data;
//		return attendanceRepository.findAttendances(userId, date);
	}
	
	public Attendance findAttendance(Long userId, String date) {
		Attendance data = attendanceRepository.findAttendance(userId, date);
		return data;
	}
	
	public Attendance create(Attendance attendance) {
		attendance.setCreate_date(new Date());
		attendanceRepository.save(attendance);
		return attendance;
	}
	
	public Attendance update(RegistAttendanceForm form) {
		AttendanceKey key = new AttendanceKey();
		key.setUser_id(form.getUser_id());
		key.setAttendance_date(form.getAttendance_date());
		Attendance attendance = attendanceRepository.findAttendance(form.getUser_id(), form.getAttendance_date());
		attendance.setKey(key);
		attendance.setKind(form.getKind());
		if(form.getStart_time() != null) {
			attendance.setStart_time(getTimeFromStr(form.getStart_time()));
		}
		if(form.getEnd_time() != null) {
			attendance.setEnd_time(getTimeFromStr(form.getEnd_time()));
		}
		if(form.getBreak_start_time() != null) {
			attendance.setBreak_start_time(getTimeFromStr(form.getBreak_start_time()));
		}
		if(form.getBreak_end_time() != null) {
			attendance.setBreak_end_time(getTimeFromStr(form.getBreak_end_time()));
		}
		Time breakAllTime = null;
		if(form.getBreak_start_time() != null && form.getBreak_end_time() != null) {
			breakAllTime = getDefferenceTime(form.getBreak_start_time(), form.getBreak_end_time());
			attendance.setBreak_all_time(breakAllTime);
		}
		Time allTimeMinusBreakAllTime = null;
		if(form.getStart_time() != null && form.getEnd_time() != null
				&& breakAllTime != null) {
			Time allTime = getDefferenceTime(form.getStart_time(), form.getEnd_time());
			allTimeMinusBreakAllTime = getDefferenceTime(allTime, breakAllTime);
			attendance.setAll_time(allTimeMinusBreakAllTime);
		}
		
		if (allTimeMinusBreakAllTime != null) {
			LocalTime time8 = LocalTime.of(8, 0);
			Time overTime = getDefferenceTime(allTimeMinusBreakAllTime, Time.valueOf(time8));
			attendance.setOvertime(overTime);
		}
		
		attendance.setRemarks(form.getRemarks());
		attendance.setAproval_kind(form.getAproval_kind());
		
		attendance.setUpdate_date(new Date());
		attendanceRepository.save(attendance);
		return attendance;
	}
	
	public Attendance updateStatus(UpdateStatusForm form) {
		Attendance data = attendanceRepository.findAttendance(form.getUser_id(), form.getDate());
		data.setAproval_kind(form.getStatus());
		Attendance ret =attendanceRepository.save(data);
		return ret;
	}
	
	public SimpleResponse batch(BatchForm form) {
		List<Attendance> attendances = attendanceRepository.findAttendances(form.getUser_id(), form.getMonth());
		SimpleResponse ret = new SimpleResponse();
		for(Attendance data : attendances) {
			if(data.getKind() == null) {
				ret.setStatus("Failed");
				ret.setMessage("入力が完了していない日があります。");
				return ret;
			}
			if(data.getKind().equals("出勤")) {
				if(data.getStart_time() == null || data.getEnd_time() == null
						|| data.getBreak_start_time() == null || data.getBreak_end_time() == null) {
					ret.setStatus("Failed");
					ret.setMessage("入力が完了していない日があります。");
					return ret;
				}
			}
		}
		Integer updateCount = attendanceRepository.batchUpdate(form.getUser_id(), form.getMonth());
		if (updateCount > 0) {
			ret.setStatus("Success");
			ret.setMessage("一括申請が完了しました。");
		}
		return ret;
	}
	
	
	private Time getTimeFromStr(String time) {
		LocalTime localTime = LocalTime.parse(time);
		return Time.valueOf(localTime);
	}
	
	private Time getDefferenceTime(String timeFrom, String timeTo) {
		// バリデーションは考えない
		LocalTime localTimeFrom = LocalTime.parse(timeFrom);
		LocalTime localTimeTo = LocalTime.parse(timeTo);
		Integer minFrom = localTimeFrom.getHour() * 60 + localTimeFrom.getMinute();
		Integer minTo= localTimeTo.getHour() * 60 + localTimeTo.getMinute();
		Integer deff = minTo - minFrom;
		Integer deffHour = deff < 60 ? 0 : deff / 60;
		Integer deffMin = deff % 60;
		LocalTime time = LocalTime.of(deffHour, deffMin);
		return Time.valueOf(time);
	}
	
	private Time getDefferenceTime(Time allTime, Time breakAllTime) {
		LocalTime localTimeBreakAll = breakAllTime.toLocalTime();
		LocalTime localTimeAll = allTime.toLocalTime();
		Integer minBreakAll = localTimeBreakAll.getHour() * 60 + localTimeBreakAll.getMinute();
		Integer minAll= localTimeAll.getHour() * 60 + localTimeAll.getMinute();
		Integer deff = minAll - minBreakAll;
		Integer deffHour = deff < 60 ? 0 : deff / 60;
		Integer deffMin = deff % 60;
		LocalTime time = LocalTime.of(deffHour, deffMin);
		return Time.valueOf(time);
	}

}
