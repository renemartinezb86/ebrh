package com.ericsson.brh.service;

import com.ericsson.brh.domain.DiscountProcess;
import com.ericsson.brh.repository.DiscountProcessRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing DiscountProcess.
 */
@Service
@Transactional
public class DiscountProcessService {

    private final Logger log = LoggerFactory.getLogger(DiscountProcessService.class);

    private final DiscountProcessRepository discountProcessRepository;

    public DiscountProcessService(DiscountProcessRepository discountProcessRepository) {
        this.discountProcessRepository = discountProcessRepository;
    }

    /**
     * Save a discountProcess.
     *
     * @param discountProcess the entity to save
     * @return the persisted entity
     */
    public DiscountProcess save(DiscountProcess discountProcess) {
        log.debug("Request to save DiscountProcess : {}", discountProcess);
        return discountProcessRepository.save(discountProcess);
    }

    /**
     * Get all the discountProcesses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DiscountProcess> findAll(Pageable pageable) {
        log.debug("Request to get all DiscountProcesses");
        return discountProcessRepository.findAll(pageable);
    }


    /**
     * Get one discountProcess by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DiscountProcess> findOne(Long id) {
        log.debug("Request to get DiscountProcess : {}", id);
        return discountProcessRepository.findById(id);
    }

    /**
     * Delete the discountProcess by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DiscountProcess : {}", id);
        discountProcessRepository.deleteById(id);
    }
}
