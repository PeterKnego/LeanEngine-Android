/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

/**
 * A holder class containing query filter properties.
 */
public class QueryFilter {
    private String property;
    private LeanQuery.FilterOperator operator;
    private Object value;

    QueryFilter(String property, LeanQuery.FilterOperator operator, Object value) {

        this.property = property;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Returns the name of the property the query filters on.
     *
     * @return Name of the property the query filters on.
     */
    public String getProperty() {
        return property;
    }

    /**
     * Returns the filter operator of the query filter.
     *
     * @return {@link LeanQuery.FilterOperator}
     */
    public LeanQuery.FilterOperator getOperator() {
        return operator;
    }

    /**
     * Returns the value of query filter. It's an instance of a supported datastore type.
     *
     * @return Value of query filter.
     */
    public Object getValue() {
        return value;
    }

}