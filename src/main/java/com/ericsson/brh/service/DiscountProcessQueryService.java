package com.ericsson.brh.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.ericsson.brh.domain.DiscountProcess;
import com.ericsson.brh.domain.*; // for static metamodels
import com.ericsson.brh.repository.DiscountProcessRepository;
import com.ericsson.brh.service.dto.DiscountProcessCriteria;

/**
 * Service for executing complex queries for DiscountProcess entities in the database.
 * The main input is a {@link DiscountProcessCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DiscountProcess} or a {@link Page} of {@link DiscountProcess} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DiscountProcessQueryService extends QueryService<DiscountProcess> {

    private final Logger log = LoggerFactory.getLogger(DiscountProcessQueryService.class);

    private final DiscountProcessRepository discountProcessRepository;

    public DiscountProcessQueryService(DiscountProcessRepository discountProcessRepository) {
        this.discountProcessRepository = discountProcessRepository;
    }

    /**
     * Return a {@link List} of {@link DiscountProcess} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DiscountProcess> findByCriteria(DiscountProcessCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DiscountProcess> specification = createSpecification(criteria);
        return discountProcessRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DiscountProcess} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DiscountProcess> findByCriteria(DiscountProcessCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DiscountProcess> specification = createSpecification(criteria);
        return discountProcessRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DiscountProcessCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DiscountProcess> specification = createSpecification(criteria);
        return discountProcessRepository.count(specification);
    }

    /**
     * Function to convert DiscountProcessCriteria to a {@link Specification}
     */
    private Specification<DiscountProcess> createSpecification(DiscountProcessCriteria criteria) {
        Specification<DiscountProcess> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DiscountProcess_.id));
            }
            if (criteria.getCsvFilePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCsvFilePath(), DiscountProcess_.csvFilePath));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), DiscountProcess_.createdDate));
            }
            if (criteria.getSqlFilePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSqlFilePath(), DiscountProcess_.sqlFilePath));
            }
        }
        return specification;
    }
}
