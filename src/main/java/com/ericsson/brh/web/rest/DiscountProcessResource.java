package com.ericsson.brh.web.rest;
import com.ericsson.brh.domain.DiscountProcess;
import com.ericsson.brh.service.DiscountProcessService;
import com.ericsson.brh.web.rest.errors.BadRequestAlertException;
import com.ericsson.brh.web.rest.util.HeaderUtil;
import com.ericsson.brh.web.rest.util.PaginationUtil;
import com.ericsson.brh.service.dto.DiscountProcessCriteria;
import com.ericsson.brh.service.DiscountProcessQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DiscountProcess.
 */
@RestController
@RequestMapping("/api")
public class DiscountProcessResource {

    private final Logger log = LoggerFactory.getLogger(DiscountProcessResource.class);

    private static final String ENTITY_NAME = "discountProcess";

    private final DiscountProcessService discountProcessService;

    private final DiscountProcessQueryService discountProcessQueryService;

    public DiscountProcessResource(DiscountProcessService discountProcessService, DiscountProcessQueryService discountProcessQueryService) {
        this.discountProcessService = discountProcessService;
        this.discountProcessQueryService = discountProcessQueryService;
    }

    /**
     * POST  /discount-processes : Create a new discountProcess.
     *
     * @param discountProcess the discountProcess to create
     * @return the ResponseEntity with status 201 (Created) and with body the new discountProcess, or with status 400 (Bad Request) if the discountProcess has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/discount-processes")
    public ResponseEntity<DiscountProcess> createDiscountProcess(@RequestBody DiscountProcess discountProcess) throws URISyntaxException {
        log.debug("REST request to save DiscountProcess : {}", discountProcess);
        if (discountProcess.getId() != null) {
            throw new BadRequestAlertException("A new discountProcess cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DiscountProcess result = discountProcessService.save(discountProcess);
        return ResponseEntity.created(new URI("/api/discount-processes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /discount-processes : Updates an existing discountProcess.
     *
     * @param discountProcess the discountProcess to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated discountProcess,
     * or with status 400 (Bad Request) if the discountProcess is not valid,
     * or with status 500 (Internal Server Error) if the discountProcess couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/discount-processes")
    public ResponseEntity<DiscountProcess> updateDiscountProcess(@RequestBody DiscountProcess discountProcess) throws URISyntaxException {
        log.debug("REST request to update DiscountProcess : {}", discountProcess);
        if (discountProcess.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DiscountProcess result = discountProcessService.save(discountProcess);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, discountProcess.getId().toString()))
            .body(result);
    }

    /**
     * GET  /discount-processes : get all the discountProcesses.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of discountProcesses in body
     */
    @GetMapping("/discount-processes")
    public ResponseEntity<List<DiscountProcess>> getAllDiscountProcesses(DiscountProcessCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DiscountProcesses by criteria: {}", criteria);
        Page<DiscountProcess> page = discountProcessQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/discount-processes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /discount-processes/count : count all the discountProcesses.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/discount-processes/count")
    public ResponseEntity<Long> countDiscountProcesses(DiscountProcessCriteria criteria) {
        log.debug("REST request to count DiscountProcesses by criteria: {}", criteria);
        return ResponseEntity.ok().body(discountProcessQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /discount-processes/:id : get the "id" discountProcess.
     *
     * @param id the id of the discountProcess to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the discountProcess, or with status 404 (Not Found)
     */
    @GetMapping("/discount-processes/{id}")
    public ResponseEntity<DiscountProcess> getDiscountProcess(@PathVariable Long id) {
        log.debug("REST request to get DiscountProcess : {}", id);
        Optional<DiscountProcess> discountProcess = discountProcessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(discountProcess);
    }

    /**
     * DELETE  /discount-processes/:id : delete the "id" discountProcess.
     *
     * @param id the id of the discountProcess to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/discount-processes/{id}")
    public ResponseEntity<Void> deleteDiscountProcess(@PathVariable Long id) {
        log.debug("REST request to delete DiscountProcess : {}", id);
        discountProcessService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
