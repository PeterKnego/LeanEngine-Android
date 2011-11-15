package com.leanengine.query;

import com.leanengine.LeanError;
import com.leanengine.LeanException;

public class QueryFilter {
    private String property;
    private FilterOperator operator;
    private Object value;

    protected QueryFilter(String property, FilterOperator operator, Object value) {

        this.property = property;
        this.operator = operator;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public FilterOperator getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }

    public enum FilterOperator {
        IN("IN"),
        EQUAL("="),
        GREATER_THAN(">"),
        GREATER_THAN_OR_EQUAL(">="),
        LESS_THAN("<"),
        LESS_THAN_OR_EQUAL("<="),
        NOT_EQUAL("!=");

        private String operatorString;

        static FilterOperator create(String jsonOperator) throws LeanException {
            if("=".equals(jsonOperator)){
                return FilterOperator.EQUAL;
            } else if(">".equals(jsonOperator)){
                return FilterOperator.GREATER_THAN;
            } else if(">=".equals(jsonOperator)){
                return FilterOperator.GREATER_THAN_OR_EQUAL;
            } else if("<".equals(jsonOperator)){
                return FilterOperator.LESS_THAN;
            } else if("<=".equals(jsonOperator)){
                return FilterOperator.LESS_THAN_OR_EQUAL;
            } else if("!=".equals(jsonOperator)){
                return FilterOperator.NOT_EQUAL;
            } else if("IN".equals(jsonOperator)){
                return FilterOperator.IN;
            }
            throw new LeanException(LeanError.Error.UnsupportedQueryFilterOperation, jsonOperator);
        }

        FilterOperator( String operatorString) {
            this.operatorString = operatorString;
        }

        public String toJSON() {
            return operatorString;
        }
    }
}