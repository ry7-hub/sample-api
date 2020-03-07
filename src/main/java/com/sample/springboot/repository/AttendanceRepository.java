package com.sample.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sample.springboot.entity.Attendance;
import com.sample.springboot.entity.AttendanceKey;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceKey> {

	@Query(value="select * from attendance where user_id = :userId and substring(attendance_date, 1, 6) = :date order by attendance_date", nativeQuery = true)
	public List<Attendance> findAttendances(@Param("userId") Long userId, @Param("date") String date); 
	
	@Query(value="select * from attendance where user_id = :userId  and attendance_date = :date", nativeQuery = true)
	public Attendance findAttendance(@Param("userId") Long userId, @Param("date") String date);
	
	@Modifying
	@Query(value="update attendance set aproval_kind = '申請済' where user_id = :userId and substring(attendance_date, 1, 6) = :date", nativeQuery = true)
	public Integer batchUpdate(@Param("userId") Long userId, @Param("date") String date);
}
