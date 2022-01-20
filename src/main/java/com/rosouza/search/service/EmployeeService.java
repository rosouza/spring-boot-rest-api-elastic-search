package com.rosouza.search.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosouza.search.domain.Employee;
import com.rosouza.search.exception.EmployeeNotExistException;
import com.rosouza.search.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EmployeeService {

	private final EmployeeRepository repository;

	public Employee getEmployee(Long id) {

		return repository.findById(id).orElseThrow(EmployeeNotExistException::new);

	}

	public Employee createEmployee(Employee employee) {

		repository.save(employee);
		return employee;

	}
	
	public Employee updateEmployee(Long id, Employee employee) {

		repository.save(employee);
		return employee;

	}	

	public void deleteEmployee(Long id) {

		repository.findById(id).orElseThrow(EmployeeNotExistException::new);
		repository.deleteById(id);

	}
	
	public void bulkDocumentsUpdate(List<Employee> bulkEmployeeList) {

		repository.saveAll(bulkEmployeeList);

	}	

}
