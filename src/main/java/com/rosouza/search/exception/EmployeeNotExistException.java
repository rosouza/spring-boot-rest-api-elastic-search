package com.rosouza.search.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "employee.not-exist.error")
public class EmployeeNotExistException extends RuntimeException {
}
