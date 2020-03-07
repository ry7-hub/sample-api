package com.sample.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sample.springboot.entity.User;
import com.sample.springboot.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public Optional<User> findUser(Long id) {
		return userRepository.findById(id);
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public List<User> findUsers() {
		return userRepository.findAll();
	}
	
	public User save(User user) {
		return userRepository.save(user);
	}
	
	public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
