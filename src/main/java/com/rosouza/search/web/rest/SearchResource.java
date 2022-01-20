package com.rosouza.search.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rosouza.search.domain.Employee;
import com.rosouza.search.service.EmployeeQueryService;
import com.rosouza.search.util.PaginationUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
public class SearchResource {

    private final EmployeeQueryService EmployeeQueryService;


    /**
     * {@code POST  /v1/search/Employees} : Searches Employee documents in Elastic Search index according to a given criteria
     *
     * @param criteria the DTO with criteria parameters.
     * @return the {@link Employee>} with status {@code 200 (OK)} and with body the list of DonationSearchResponseDTO
     */
    @GetMapping("/v1/search/employees")
    public ResponseEntity<List<Employee>> findByCriteria(
    	Employee criteria,
        //@Parameter(hidden = true)
        @SortDefault(sort = "id", direction = Sort.Direction.ASC)
        @PageableDefault(size = 20) Pageable page
    ) {
        log.debug("REST request to get Donation by criteria: {}", criteria);
        var result = EmployeeQueryService.findByCriteria(criteria, page);
        var headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

}
