package com.sample.springboot.controller.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sample.springboot.exception.BadRequestException;

@RestControllerAdvice
public class MyExceptionHandler  extends ResponseEntityExceptionHandler{

	// 自分で定義したMyExceptionをキャッチする
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        return super.handleExceptionInternal(ex, ex.toJson(), null, HttpStatus.BAD_REQUEST, request);
    }
}
