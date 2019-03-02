package com.ericsson.brh.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the DiscountProcess entity. This class is used in DiscountProcessResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /discount-processes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DiscountProcessCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter csvFilePath;

    private InstantFilter createdDate;

    private StringFilter sqlFilePath;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCsvFilePath() {
        return csvFilePath;
    }

    public void setCsvFilePath(StringFilter csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getSqlFilePath() {
        return sqlFilePath;
    }

    public void setSqlFilePath(StringFilter sqlFilePath) {
        this.sqlFilePath = sqlFilePath;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DiscountProcessCriteria that = (DiscountProcessCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(csvFilePath, that.csvFilePath) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(sqlFilePath, that.sqlFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        csvFilePath,
        createdDate,
        sqlFilePath
        );
    }

    @Override
    public String toString() {
        return "DiscountProcessCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (csvFilePath != null ? "csvFilePath=" + csvFilePath + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (sqlFilePath != null ? "sqlFilePath=" + sqlFilePath + ", " : "") +
            "}";
    }

}
