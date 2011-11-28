/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class to perform data queries against server datastore.
 * <br/><br/>
 * A simple example query would look like this:
 * <br/>
 *todo finish the example
 *
 */
public class LeanQuery {
    private final String kind;
    private List<QueryFilter> filters = new ArrayList<QueryFilter>();
    private List<QuerySort> sorts = new ArrayList<QuerySort>();
    private String cursor;
    private int limit = 20;
    private int offset = 0;

    /**
     * Constructor to initialize query.
     * @param kind The kind (name) of entities to search for.
     */
    public LeanQuery(String kind) {
        this.kind = kind;
    }

    public void fetchInBackground(NetworkCallback<LeanEntity> callback) {
        // this is initial fetch, reset the cursor
        cursor = null;
        RestService.queryPrivateAsync(this, callback);
    }

    public void fetchNextInBackground(NetworkCallback<LeanEntity> callback) {
        RestService.queryPrivateAsync(this, callback);
    }

    public LeanEntity[] fetch() throws LeanException {
         // this is initial fetch, reset the cursor
        cursor = null;
        return RestService.queryPrivate(this);
    }

    public LeanEntity[] fetchNext() throws LeanException {
        return RestService.queryPrivate(this);
    }

    protected String getCursor() {
        return cursor;
    }

    protected LeanQuery setCursor(String cursor) {
        this.cursor = cursor;
        return this;
    }

    public LeanQuery addFilter(String property, FilterOperator operator, long value) {
        filters.add(new QueryFilter(property, operator, value));
        return this;
    }

    public LeanQuery addFilter(String property, FilterOperator operator, double value) {
        filters.add(new QueryFilter(property, operator, value));
        return this;
    }

    public LeanQuery addFilter(String property, FilterOperator operator, Date value) {
        filters.add(new QueryFilter(property, operator, value));
        return this;
    }

    public LeanQuery addFilter(String property, FilterOperator operator, String value) {
        filters.add(new QueryFilter(property, operator, value));
        return this;
    }

    public LeanQuery addFilter(String property, FilterOperator operator, boolean value) {
        filters.add(new QueryFilter(property, operator, value));
        return this;
    }

    public LeanQuery addSort(String property, SortDirection direction) {
        sorts.add(new QuerySort(property, direction));
        return this;
    }

    public String getKind() {
        return kind;
    }

    public List<QuerySort> getSorts() {
        return sorts;
    }

    public List<QueryFilter> getFilters() {
        return filters;
    }

    public LeanQuery limit(int limit){
        this.limit = limit;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public LeanQuery offset(int offset){
        this.offset = offset;
        return this;
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

        static FilterOperator create(String jsonOperator) {
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
            throw new IllegalArgumentException("Unsupported query operator: '"+jsonOperator+"'");
        }

        FilterOperator( String operatorString) {
            this.operatorString = operatorString;
        }

        public String toJSON() {
            return operatorString;
        }
    }

    public enum SortDirection {
        ASCENDING("asc"),
        DESCENDING("desc");

        private String sortString;

        SortDirection(String sortString) {
            this.sortString = sortString;
        }

        public static SortDirection create(String sortJson) {
            if ("asc".equals(sortJson)) {
                return SortDirection.ASCENDING;
            } else if ("desc".equals(sortJson)) {
                return SortDirection.DESCENDING;
            }
            throw new IllegalArgumentException("Unsupported sort operator: '"+sortJson+"'");
        }

        public String toJSON() {
            return sortString;
        }
    }
}
