package com.ericsson.brh.repository;

import com.ericsson.brh.domain.DiscountProcess;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DiscountProcess entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscountProcessRepository extends JpaRepository<DiscountProcess, Long>, JpaSpecificationExecutor<DiscountProcess> {

}
