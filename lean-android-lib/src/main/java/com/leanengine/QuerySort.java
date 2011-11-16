/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

public class QuerySort {
    private String property;
    private LeanQuery.SortDirection direction;

    public QuerySort(String property, LeanQuery.SortDirection direction) {
        this.property = property;
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public LeanQuery.SortDirection getDirection() {
        return direction;
    }

}
