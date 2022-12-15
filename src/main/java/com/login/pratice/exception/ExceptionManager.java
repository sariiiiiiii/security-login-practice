package com.login.pratice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    /**
     * ResponseEntity<?> = ResponseEntity.body에 모든지 들어갈 수 있음
     */

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> runtimeExceptionHandler(AppException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus()) // 409 클라이언트의 요청이 서버의 상태와 충돌
                .body(e.getErrorCode() + " " +  e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 클라이언트의 요청이 서버의 상태와 충돌
                .body(e.getMessage());
    }

}
