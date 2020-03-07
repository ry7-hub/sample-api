package com.sample.springboot.support;

import static com.sample.springboot.support.SecurityConstants.SECRET;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static com.sample.springboot.support.SecurityConstants.*;

public class TokenCreater {
	
	public static String createToken(String email) {
		return Jwts.builder()
                .setSubject(email) 
                // 有効期限30分（ミリ秒で指定）
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
	}

}
