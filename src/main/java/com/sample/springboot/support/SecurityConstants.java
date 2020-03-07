package com.sample.springboot.support;

public class SecurityConstants {
	public static final String SECRET = "samplesecret";
    public static final long EXPIRATION_TIME = 1_800_000; // 30min
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGNUP_URL = "/user/signup";
    public static final String LOGIN_URL = "/api/auth/login";
    public static final String EMAIL = "email"; // defalut:username
    public static final String PASSWORD = "password"; // default:password
}
