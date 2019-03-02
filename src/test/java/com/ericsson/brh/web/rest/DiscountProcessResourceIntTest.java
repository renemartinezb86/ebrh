package com.ericsson.brh.web.rest;

import com.ericsson.brh.BrhApp;

import com.ericsson.brh.domain.DiscountProcess;
import com.ericsson.brh.repository.DiscountProcessRepository;
import com.ericsson.brh.service.DiscountProcessService;
import com.ericsson.brh.web.rest.errors.ExceptionTranslator;
import com.ericsson.brh.service.dto.DiscountProcessCriteria;
import com.ericsson.brh.service.DiscountProcessQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.ericsson.brh.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DiscountProcessResource REST controller.
 *
 * @see DiscountProcessResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrhApp.class)
public class DiscountProcessResourceIntTest {

    private static final String DEFAULT_CSV_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_CSV_FILE_PATH = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SQL_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_SQL_FILE_PATH = "BBBBBBBBBB";

    @Autowired
    private DiscountProcessRepository discountProcessRepository;

    @Autowired
    private DiscountProcessService discountProcessService;

    @Autowired
    private DiscountProcessQueryService discountProcessQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDiscountProcessMockMvc;

    private DiscountProcess discountProcess;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiscountProcessResource discountProcessResource = new DiscountProcessResource(discountProcessService, discountProcessQueryService);
        this.restDiscountProcessMockMvc = MockMvcBuilders.standaloneSetup(discountProcessResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiscountProcess createEntity(EntityManager em) {
        DiscountProcess discountProcess = new DiscountProcess()
            .csvFilePath(DEFAULT_CSV_FILE_PATH)
            .createdDate(DEFAULT_CREATED_DATE)
            .sqlFilePath(DEFAULT_SQL_FILE_PATH);
        return discountProcess;
    }

    @Before
    public void initTest() {
        discountProcess = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiscountProcess() throws Exception {
        int databaseSizeBeforeCreate = discountProcessRepository.findAll().size();

        // Create the DiscountProcess
        restDiscountProcessMockMvc.perform(post("/api/discount-processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountProcess)))
            .andExpect(status().isCreated());

        // Validate the DiscountProcess in the database
        List<DiscountProcess> discountProcessList = discountProcessRepository.findAll();
        assertThat(discountProcessList).hasSize(databaseSizeBeforeCreate + 1);
        DiscountProcess testDiscountProcess = discountProcessList.get(discountProcessList.size() - 1);
        assertThat(testDiscountProcess.getCsvFilePath()).isEqualTo(DEFAULT_CSV_FILE_PATH);
        assertThat(testDiscountProcess.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDiscountProcess.getSqlFilePath()).isEqualTo(DEFAULT_SQL_FILE_PATH);
    }

    @Test
    @Transactional
    public void createDiscountProcessWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = discountProcessRepository.findAll().size();

        // Create the DiscountProcess with an existing ID
        discountProcess.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscountProcessMockMvc.perform(post("/api/discount-processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountProcess)))
            .andExpect(status().isBadRequest());

        // Validate the DiscountProcess in the database
        List<DiscountProcess> discountProcessList = discountProcessRepository.findAll();
        assertThat(discountProcessList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDiscountProcesses() throws Exception {
        // Initialize the database
        discountProcessRepository.saveAndFlush(discountProcess);

        // Get all the discountProcessList
        restDiscountProcessMockMvc.perform(get("/api/discount-processes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discountProcess.getId().intValue())))
            .andExpect(jsonPath("$.[*].csvFilePath").value(hasItem(DEFAULT_CSV_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].sqlFilePath").value(hasItem(DEFAULT_SQL_FILE_PATH.toString())));
    }
    
    @Test
    @Transactional
    public void getDiscountProcess() throws Exception {
        // Initialize the database
        discountProcessRepository.saveAndFlush(discountProcess);

        // Get the discountProcess
        restDiscountProcessMockMvc.perform(get("/api/discount-processes/{id}", discountProcess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(discountProcess.getId().intValue()))
            .andExpect(jsonPath("$.csvFilePath").value(DEFAULT_CSV_FILE_PATH.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.sqlFilePath").value(DEFAULT_SQL_FILE_PATH.toString()));
    }

    @Test
    @Transactional
    public void getAllDiscountProcessesByCsvFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        discountProcessRepository.saveAndFlush(discountProcess);

        // Get all the discountProcessList where csvFilePath equals to DEFAULT_CSV_FILE_PATH
        defaultDiscountProcessShouldBeFound("csvFilePath.equals=" + DEFAULT_CSV_FILE_PATH);

        // Get all the discountProcessList where csvFilePath equals to UPDATED_CSV_FILE_PATH
        defaultDiscountProcessShouldNotBeFound("csvFilePath.equals=" + UPDATED_CSV_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllDiscountProcessesByCsvFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        discountProcessRepository.saveAndFlush(discountProcess);

        // Get all the discountProcessList where csvFilePath in DEFAULT_CSV_FILE_PATH or UPDATED_CSV_FILE_PATH
        defaultDiscountProcessShouldBeFound("csvFilePath.in=" + DEFAULT_CSV_FILE_PATH + "," + UPDATED_CSV_FILE_PATH);

        // Get all the discountProcessList where csvFilePath equals to UPDATED_CSV_FILE_PATH
        defaultDiscountProcessShouldNotBeFound("csvFilePath.in=" + UPDATED_CSV_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllDiscountProcessesByCsvFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountProcessRepository.saveAndFlush(discountProcess);

        // Get all the discountProcessList where csvFilePath is not null
        defaultDiscountProcessShouldBeFound("csvFilePath.specified=true");

        // Get all the discountProcessList where csvFilePath is null
        defaultDiscountProcessShouldNotBeFound("csvFilePath.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscountProcessesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        discountProcessRepository.saveAndFlush(discountProcess);

        // Get all the discountProcessList where createdDate equals to DEFAULT_CREATED_DATE
        defaultDiscountProcessShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the discountProcessList where createdDate equals to UPDATED_CREATED_DATE
        defaultDiscountProcessShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDiscountProcessesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        discountProcessRepository.saveAndFlush(discountProcess);

        // Get all the discountProcessList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultDiscountProcessShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the discountProcessList where createdDate equals to UPDATED_CREATED_DATE
        defaultDiscountProcessShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllDiscountProcessesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountProcessRepository.saveAndFlush(discountProcess);

        // Get all the discountProcessList where createdDate is not null
        defaultDiscountProcessShouldBeFound("createdDate.specified=true");

        // Get all the discountProcessList where createdDate is null
        defaultDiscountProcessShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscountProcessesBySqlFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        discountProcessRepository.saveAndFlush(discountProcess);

        // Get all the discountProcessList where sqlFilePath equals to DEFAULT_SQL_FILE_PATH
        defaultDiscountProcessShouldBeFound("sqlFilePath.equals=" + DEFAULT_SQL_FILE_PATH);

        // Get all the discountProcessList where sqlFilePath equals to UPDATED_SQL_FILE_PATH
        defaultDiscountProcessShouldNotBeFound("sqlFilePath.equals=" + UPDATED_SQL_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllDiscountProcessesBySqlFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        discountProcessRepository.saveAndFlush(discountProcess);

        // Get all the discountProcessList where sqlFilePath in DEFAULT_SQL_FILE_PATH or UPDATED_SQL_FILE_PATH
        defaultDiscountProcessShouldBeFound("sqlFilePath.in=" + DEFAULT_SQL_FILE_PATH + "," + UPDATED_SQL_FILE_PATH);

        // Get all the discountProcessList where sqlFilePath equals to UPDATED_SQL_FILE_PATH
        defaultDiscountProcessShouldNotBeFound("sqlFilePath.in=" + UPDATED_SQL_FILE_PATH);
    }

    @Test
    @Transactional
    public void getAllDiscountProcessesBySqlFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountProcessRepository.saveAndFlush(discountProcess);

        // Get all the discountProcessList where sqlFilePath is not null
        defaultDiscountProcessShouldBeFound("sqlFilePath.specified=true");

        // Get all the discountProcessList where sqlFilePath is null
        defaultDiscountProcessShouldNotBeFound("sqlFilePath.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDiscountProcessShouldBeFound(String filter) throws Exception {
        restDiscountProcessMockMvc.perform(get("/api/discount-processes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discountProcess.getId().intValue())))
            .andExpect(jsonPath("$.[*].csvFilePath").value(hasItem(DEFAULT_CSV_FILE_PATH)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].sqlFilePath").value(hasItem(DEFAULT_SQL_FILE_PATH)));

        // Check, that the count call also returns 1
        restDiscountProcessMockMvc.perform(get("/api/discount-processes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDiscountProcessShouldNotBeFound(String filter) throws Exception {
        restDiscountProcessMockMvc.perform(get("/api/discount-processes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDiscountProcessMockMvc.perform(get("/api/discount-processes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDiscountProcess() throws Exception {
        // Get the discountProcess
        restDiscountProcessMockMvc.perform(get("/api/discount-processes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscountProcess() throws Exception {
        // Initialize the database
        discountProcessService.save(discountProcess);

        int databaseSizeBeforeUpdate = discountProcessRepository.findAll().size();

        // Update the discountProcess
        DiscountProcess updatedDiscountProcess = discountProcessRepository.findById(discountProcess.getId()).get();
        // Disconnect from session so that the updates on updatedDiscountProcess are not directly saved in db
        em.detach(updatedDiscountProcess);
        updatedDiscountProcess
            .csvFilePath(UPDATED_CSV_FILE_PATH)
            .createdDate(UPDATED_CREATED_DATE)
            .sqlFilePath(UPDATED_SQL_FILE_PATH);

        restDiscountProcessMockMvc.perform(put("/api/discount-processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiscountProcess)))
            .andExpect(status().isOk());

        // Validate the DiscountProcess in the database
        List<DiscountProcess> discountProcessList = discountProcessRepository.findAll();
        assertThat(discountProcessList).hasSize(databaseSizeBeforeUpdate);
        DiscountProcess testDiscountProcess = discountProcessList.get(discountProcessList.size() - 1);
        assertThat(testDiscountProcess.getCsvFilePath()).isEqualTo(UPDATED_CSV_FILE_PATH);
        assertThat(testDiscountProcess.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDiscountProcess.getSqlFilePath()).isEqualTo(UPDATED_SQL_FILE_PATH);
    }

    @Test
    @Transactional
    public void updateNonExistingDiscountProcess() throws Exception {
        int databaseSizeBeforeUpdate = discountProcessRepository.findAll().size();

        // Create the DiscountProcess

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscountProcessMockMvc.perform(put("/api/discount-processes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discountProcess)))
            .andExpect(status().isBadRequest());

        // Validate the DiscountProcess in the database
        List<DiscountProcess> discountProcessList = discountProcessRepository.findAll();
        assertThat(discountProcessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDiscountProcess() throws Exception {
        // Initialize the database
        discountProcessService.save(discountProcess);

        int databaseSizeBeforeDelete = discountProcessRepository.findAll().size();

        // Delete the discountProcess
        restDiscountProcessMockMvc.perform(delete("/api/discount-processes/{id}", discountProcess.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DiscountProcess> discountProcessList = discountProcessRepository.findAll();
        assertThat(discountProcessList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiscountProcess.class);
        DiscountProcess discountProcess1 = new DiscountProcess();
        discountProcess1.setId(1L);
        DiscountProcess discountProcess2 = new DiscountProcess();
        discountProcess2.setId(discountProcess1.getId());
        assertThat(discountProcess1).isEqualTo(discountProcess2);
        discountProcess2.setId(2L);
        assertThat(discountProcess1).isNotEqualTo(discountProcess2);
        discountProcess1.setId(null);
        assertThat(discountProcess1).isNotEqualTo(discountProcess2);
    }
}
