package com.rosouza.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.rosouza.search.domain.Employee;

@Repository
public interface EmployeeRepository extends ElasticsearchRepository<Employee, Long> {

}