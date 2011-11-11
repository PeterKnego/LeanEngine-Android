package com.leanengine.query;

import com.leanengine.LeanError;
import com.leanengine.LeanException;

public class QuerySort {
    private String property;
    private SortDirection direction;

    public QuerySort(String property, SortDirection direction) {
        this.property = property;
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public SortDirection getDirection() {
        return direction;
    }

    public enum SortDirection {
        ASCENDING("asc"),
        DESCENDING("desc");

        private String sortString;

        SortDirection(String sortString) {
            this.sortString = sortString;
        }

        public static SortDirection create(String sortJson) throws LeanException {
            if ("asc".equals(sortJson)) {
                return SortDirection.ASCENDING;
            } else if ("desc".equals(sortJson)) {
                return SortDirection.DESCENDING;
            }
            throw new LeanException(LeanError.Error.UnsupportedQuerySortOperation, sortJson);
        }

        public String toJSON() {
            return sortString;
        }
    }
}
