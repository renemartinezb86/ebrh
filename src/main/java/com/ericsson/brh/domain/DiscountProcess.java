package com.ericsson.brh.domain;



import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DiscountProcess.
 */
@Entity
@Table(name = "discount_process")
public class DiscountProcess implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "csv_file_path")
    private String csvFilePath;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "sql_file_path")
    private String sqlFilePath;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCsvFilePath() {
        return csvFilePath;
    }

    public DiscountProcess csvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        return this;
    }

    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public DiscountProcess createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getSqlFilePath() {
        return sqlFilePath;
    }

    public DiscountProcess sqlFilePath(String sqlFilePath) {
        this.sqlFilePath = sqlFilePath;
        return this;
    }

    public void setSqlFilePath(String sqlFilePath) {
        this.sqlFilePath = sqlFilePath;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiscountProcess discountProcess = (DiscountProcess) o;
        if (discountProcess.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), discountProcess.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DiscountProcess{" +
            "id=" + getId() +
            ", csvFilePath='" + getCsvFilePath() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", sqlFilePath='" + getSqlFilePath() + "'" +
            "}";
    }
}
