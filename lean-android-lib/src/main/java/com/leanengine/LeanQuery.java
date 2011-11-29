/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to perform data queries against server datastore.
 * <br/><br/>
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
     *
     * @param kind The kind (name) of entities to search for.
     */
    public LeanQuery(String kind) {
        this.kind = kind;
    }

    /**
     * Executes the query and fetches the initial data set. This method executes in the background thread.
     * <br/><br/>
     * The result set returned depends on the {@link #limit(int)} and {@link #offset(int)} called on this query.
     * The defaults are: {@code limit = 20} and {@code offset = 0} as defined by the Google AppEngine.
     * <br/><br/>
     * To get the next batch of results use {@link #fetchNextInBackground(NetworkCallback)} or {@link #fetchNext()}.
     *
     * @param callback {@link NetworkCallback} that will be invoked when background task is done.
     */
    public void fetchInBackground(NetworkCallback<LeanEntity> callback) {
        // this is initial fetch, reset the cursor
        cursor = null;
        RestService.queryPrivateAsync(this, callback);
    }

    /**
     * Executes the query and fetches the next data set. This method executes in the background thread.
     * <br/><br/>
     * This method may only be called after {@link #fetch()} or {@link #fetchInBackground(NetworkCallback)} are invoked.
     * <br/><br/>
     * Query parameters must not be changed between invocation of {@code fetch()} and  {@code fetchNext()} methods.
     *
     * @param callback {@link NetworkCallback} that will be invoked when background task is done.
     * @throws IllegalStateException If called before {@link #fetch()} or {@link #fetchInBackground(NetworkCallback)}.
     */
    public void fetchNextInBackground(NetworkCallback<LeanEntity> callback) throws IllegalStateException {
        if (cursor == null)
            throw new IllegalStateException("This method can be only called after method 'fetch()' or 'fetchInBackground(..)' is executed.");
        RestService.queryPrivateAsync(this, callback);
    }

    /**
     * Executes the query and fetches the initial data set.
     * <br/><br/>
     * The results returned depend on the {@link #limit(int)} and {@link #offset(int)} called on this query.
     * The defaults are: {@code limit = 20} and {@code offset = 0} as defined by the Google AppEngine.
     * <br/><br/>
     * To get the next batch of results use {@link #fetchNext()} or {@link #fetchNextInBackground(NetworkCallback)}.
     *
     * @return The array of LeanEntities representing the result of query.
     * @throws LeanException         In case of errors a {@link LeanException} is thrown.
     * @throws IllegalStateException If called before {@link #fetch()} or {@link #fetchInBackground(NetworkCallback)}.
     */
    public LeanEntity[] fetch() throws LeanException {
        // this is initial fetch, reset the cursor
        cursor = null;
        return RestService.queryPrivate(this);
    }

    /**
     * Executes the query and fetches the next data set.
     * <br/><br/>
     * This method may only be called after {@link #fetch()} or {@link #fetchInBackground(NetworkCallback)} are invoked.
     * <br/><br/>
     * Query parameters must not be changed between invocation of {@code fetch()} and  {@code fetchNext()} methods.
     *
     * @return The array of LeanEntities representing the result of query.
     * @throws LeanException         In case of errors a {@link LeanException} is thrown.
     * @throws IllegalStateException If called before {@link #fetch()} or {@link #fetchInBackground(NetworkCallback)}.
     */
    public LeanEntity[] fetchNext() throws LeanException {
        if (cursor == null)
            throw new IllegalStateException("This method can be only called after method 'fetch()' or 'fetchInBackground(..)' is executed.");
        return RestService.queryPrivate(this);
    }

    String getCursor() {
        return cursor;
    }

    LeanQuery setCursor(String cursor) {
        this.cursor = cursor;
        return this;
    }

    /**
     * Adds a filter on the specified property.
     *
     * @param property The name of the property to which the filter applies.
     * @param operator A {@link FilterOperator} to use.
     * @param value    An instance of a supported datastore type.
     * @return {@link this} (for chaining)
     */
    public LeanQuery addFilter(String property, FilterOperator operator, Object value) {
        filters.add(new QueryFilter(property, operator, value));
        return this;
    }

    /**
     * Adds a sort on the specified property.
     *
     * @param property  The name of the property to which the sort applies.
     * @param direction A {@link SortDirection} to use.
     * @return {@link this} (for chaining)
     */
    public LeanQuery addSort(String property, SortDirection direction) {
        sorts.add(new QuerySort(property, direction));
        return this;
    }

    /**
     * Returns the kind (name) of entities this query is fetching.
     *
     * @return Kind of entity.
     */
    public String getKind() {
        return kind;
    }

    /**
     * Returns a list of QuerySorts set on this query.
     *
     * @return {@link List}<{@link QuerySort}>
     */
    public List<QuerySort> getSorts() {
        return sorts;
    }

    /**
     * Returns a list of QueryFilters set on this query.
     *
     * @return {@link List}<{@link QueryFilter}>
     */
    public List<QueryFilter> getFilters() {
        return filters;
    }

    /**
     * The size of result set returned by this query's fetch methods.
     *
     * @param limit The size of result set.
     * @return {@link this} (for chaining)
     */
    public LeanQuery limit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Returns the limit set on this query - the size of result set to return when fetching.
     * See {@link #limit(int)}
     *
     * @return The size of result set.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Returns the offset set on this query - the number of results to skip before returning any results.
     * See {@link #offset(int)}
     *
     * @return The number of results to skip before returning the result set.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Sets the offset - the number of results to skip before returning any results.
     * <br/><br/>
     * <b>Note: this method has a high cost in terms of datastore access charges - all entities skipped by the offset
     * count as retrieved in AppEngine billing.</b>
     *
     * @param offset The number of results to skip before returning the result set.
     * @return {@link this} (for chaining)
     */
    public LeanQuery offset(int offset) {
        this.offset = offset;
        return this;
    }

    /**
     * FilterOperator specifies what type of operation you want to apply to your filter.
     */
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
            if ("=".equals(jsonOperator)) {
                return FilterOperator.EQUAL;
            } else if (">".equals(jsonOperator)) {
                return FilterOperator.GREATER_THAN;
            } else if (">=".equals(jsonOperator)) {
                return FilterOperator.GREATER_THAN_OR_EQUAL;
            } else if ("<".equals(jsonOperator)) {
                return FilterOperator.LESS_THAN;
            } else if ("<=".equals(jsonOperator)) {
                return FilterOperator.LESS_THAN_OR_EQUAL;
            } else if ("!=".equals(jsonOperator)) {
                return FilterOperator.NOT_EQUAL;
            } else if ("IN".equals(jsonOperator)) {
                return FilterOperator.IN;
            }
            throw new IllegalArgumentException("Unsupported query operator: '" + jsonOperator + "'");
        }

        FilterOperator(String operatorString) {
            this.operatorString = operatorString;
        }

        String toJSON() {
            return operatorString;
        }
    }

    /**
     * SortDirection controls the order of a sort.
     */
    public enum SortDirection {
        ASCENDING("asc"),
        DESCENDING("desc");

        private String sortString;

        SortDirection(String sortString) {
            this.sortString = sortString;
        }

        static SortDirection create(String sortJson) {
            if ("asc".equals(sortJson)) {
                return SortDirection.ASCENDING;
            } else if ("desc".equals(sortJson)) {
                return SortDirection.DESCENDING;
            }
            throw new IllegalArgumentException("Unsupported sort operator: '" + sortJson + "'");
        }

        String toJSON() {
            return sortString;
        }
    }
}
