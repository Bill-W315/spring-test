package com.example.mock.order.model.exception;

import com.example.mock.order.view.swagger.model.BasicResponseBody;
import com.example.mock.order.common.dao.DaoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ServerExceptionHandler {

    /** Errors returned by stored procedures: their code, err_msg and sub_code are passed to the client. */
    @ExceptionHandler(DaoException.class)
    public ResponseEntity<BasicResponseBody> handleDao(DaoException e) {
        BasicResponseBody body = new BasicResponseBody();
        body.setCode(e.getCode());
        body.setMessage(e.getMessage());
        body.setSubCode(e.getSubCode());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<BasicResponseBody> handleApp(AppException e) {
        BasicResponseBody body = new BasicResponseBody();
        body.setCode(e.getCode());
        body.setMessage(e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotImplementedException.class)
    public ResponseEntity<BasicResponseBody> handleNotImplemented(NotImplementedException e) {
        BasicResponseBody body = new BasicResponseBody();
        body.setCode(50100);
        body.setMessage(e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_IMPLEMENTED);
    }
}
