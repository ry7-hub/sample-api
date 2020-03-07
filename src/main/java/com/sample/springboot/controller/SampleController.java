package com.sample.springboot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
	
	@GetMapping(value = "/public")
    public String publicApi() {
        return "this is public";
    }

    @GetMapping(value = "/private")
    public String privateApi() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // JWTAuthenticationFilter#successfulAuthenticationで設定したusernameを取り出す
        String username = (String) (authentication.getPrincipal());

        return "this is private for " + username;
    }

}
