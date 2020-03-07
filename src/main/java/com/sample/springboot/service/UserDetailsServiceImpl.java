package com.sample.springboot.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sample.springboot.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
//	private static List<String> usernameList = Arrays.asList("nyasba", "admin");
//    private static String ENCRYPTED_PASSWORD = "$2a$10$5DF/j5hHnbeHyh85/0Bdzu1HV1KyJKZRt2GhpsfzQ8387A/9duSuq"; // "password"を暗号化した値
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 本来ならここでDBなどからユーザを検索することになるが、サンプルのためリストに含まれるかで判定している
//        if(!usernameList.contains(username)){
//            throw new UsernameNotFoundException(username);
//        }
//
//        return User.withUsername(username)
//                .password(ENCRYPTED_PASSWORD)
//                .authorities("ROLE_USER") // ユーザの権限
//                .build();
    	
    	com.sample.springboot.entity.User user = userRepository.findByEmail(email);
    	
    	// ユーザー情報を取得できなかった場合
        if(user == null){
            throw new UsernameNotFoundException("ユーザーがみつかりません");
        }
        
        // ユーザを登録する時に暗号化したパスワードを登録しておけば、ここで暗号化は不要
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String hash = bCryptPasswordEncoder.encode(user.getPassword());
        
        return new User(email, hash, AuthorityUtils.createAuthorityList("ROLE_USER"));
    }

}
