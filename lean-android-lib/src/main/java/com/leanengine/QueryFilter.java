/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

public class QueryFilter {
    private String property;
    private LeanQuery.FilterOperator operator;
    private Object value;

    protected QueryFilter(String property, LeanQuery.FilterOperator operator, Object value) {

        this.property = property;
        this.operator = operator;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public LeanQuery.FilterOperator getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }

}