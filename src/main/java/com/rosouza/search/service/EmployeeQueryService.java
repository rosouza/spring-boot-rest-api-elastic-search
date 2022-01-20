package com.rosouza.search.service;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rosouza.search.domain.Employee;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EmployeeQueryService {

    private final static PageRequest DEFAULT_PAGE = PageRequest.of(0, 50);

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * Searches Employee documents in elastic search index according to a given criteria.
     *
     * @param criteriaDTO
     * @return
     */
    public Page<Employee> findByCriteria(Employee criteriaDTO, Pageable page) {

        var queryBuilder = new NativeSearchQueryBuilder();

        applyCriteria(criteriaDTO, queryBuilder);
        applyPageable(page, queryBuilder);

        SearchHits<Employee> searchHits = elasticsearchRestTemplate.search(queryBuilder.build(), Employee.class);

        var employees = searchHits.get()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());

        return new PageImpl<Employee>(employees, page, searchHits.getTotalHits());
    }

    private void applyCriteria(Employee criteriaDTO, NativeSearchQueryBuilder queryBuilder) {

        var boolQuery = new BoolQueryBuilder();

        if (Objects.nonNull(criteriaDTO.getId())) {
            boolQuery.filter(QueryBuilders.matchQuery("id", criteriaDTO.getId()));
        }
        if (StringUtils.isNotEmpty(criteriaDTO.getFirstName())) {
            boolQuery.filter(QueryBuilders.matchQuery("firstName", criteriaDTO.getFirstName().toUpperCase().trim()));
        }
        if (StringUtils.isNotEmpty(criteriaDTO.getLastName())) {
            boolQuery.filter(QueryBuilders.matchQuery("lastName", criteriaDTO.getLastName().toUpperCase().trim()));
        }
        if (StringUtils.isNotEmpty(criteriaDTO.getSsn())) {
            boolQuery.filter(QueryBuilders.matchQuery("ssn", criteriaDTO.getSsn().trim()));
        }
        if (Objects.nonNull(criteriaDTO.getBirthDate())) {
            boolQuery.filter(QueryBuilders.matchQuery("birthDate", criteriaDTO.getBirthDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
        }
        if (StringUtils.isNotEmpty(criteriaDTO.getZipCode())) {
            boolQuery.filter(QueryBuilders.matchQuery("zipCode", criteriaDTO.getZipCode().trim()));
        }
        if (StringUtils.isNotEmpty(criteriaDTO.getPhoneNumber())) {
            boolQuery.filter(QueryBuilders.matchQuery("phoneNumber", criteriaDTO.getPhoneNumber().trim()));
        }

        queryBuilder.withQuery(boolQuery);
    }

    private void applyPageable(Pageable page, NativeSearchQueryBuilder queryBuilder) {

        if (Objects.nonNull(page)
            && Objects.nonNull(page.getPageNumber())
            && Objects.nonNull(page.getPageSize())) {

            queryBuilder.withPageable(page);

            return;

        }

        queryBuilder.withPageable(DEFAULT_PAGE);

    }

}
