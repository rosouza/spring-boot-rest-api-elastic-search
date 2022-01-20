package com.rosouza.search.web.rest;

import java.net.URI;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rosouza.search.domain.Employee;
import com.rosouza.search.service.EmployeeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
public class EmployeeResource {

	private final EmployeeService employeeService;

	@PostMapping("/v1/employees")
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
		if (Objects.nonNull(employee.getId())) {
			return ResponseEntity.badRequest().build();
		}
		var result = employeeService.createEmployee(employee);
		return ResponseEntity.created(URI.create("/v1/employees/" + result.getId())).body(result);
	}

	@DeleteMapping("/v1/employees/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
		employeeService.deleteEmployee(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/v1/employees/{id}")
	public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
		var result = employeeService.getEmployee(id);
		return ResponseEntity.ok().body(result);
	}

	@PutMapping("/v1/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
		log.debug("REST request to update a Employee: {} {}", id, employee);
		var result = employeeService.updateEmployee(id, employee);
		return ResponseEntity.ok().body(result);
	}

}
