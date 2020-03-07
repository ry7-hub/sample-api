package com.sample.springboot.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sample.springboot.form.LoginForm;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static com.sample.springboot.support.SecurityConstants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	
	private AuthenticationManager authenticationManager;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private Gson gson = new Gson();
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

        // ログイン用のpathを変更する
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(LOGIN_URL, "POST"));

        // ログイン用のID/PWのパラメータ名を変更する
        setUsernameParameter(EMAIL);
        setPasswordParameter(PASSWORD);

    }
	
	// 認証の処理
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            // requestパラメータからユーザ情報を読み取る
            LoginForm loginFrom = new ObjectMapper().readValue(req.getInputStream(), LoginForm.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                    		loginFrom.getEmail(),
                    		loginFrom.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // 認証に成功した場合の処理
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        // loginIdからtokenを設定してヘッダにセットする
        String token = Jwts.builder()
                .setSubject(((User)auth.getPrincipal()).getUsername()) // usernameだけを設定する
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
        // レスポンスヘッダーにセット
//        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    	
    	// token
//    	String token = TokenCreater.createToken(((User)auth.getPrincipal()).getUsername());
        
        HashMap<String, String> b = new HashMap<String, String>();
        b.put("token", TOKEN_PREFIX + token);
        res.setContentType("application/json");
        res.setCharacterEncoding("utf-8");
        res.getWriter().print(gson.toJson(b));

        // ここでレスポンスを組み立てると個別のパラメータを返せるがFilterの責務の範囲内で実施しなければならない
        // auth.getPrincipal()で取得できるUserDetailsは自分で作ったEntityクラスにもできるのでカスタム属性は追加可能
    }
}
