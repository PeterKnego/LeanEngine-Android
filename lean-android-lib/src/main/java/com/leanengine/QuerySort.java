/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

/**
 * A holder class containing query sort properties.
 */
public class QuerySort {
    private String property;
    private LeanQuery.SortDirection direction;

    QuerySort(String property, LeanQuery.SortDirection direction) {
        this.property = property;
        this.direction = direction;
    }

    /**
     * Returns the name of the property the query sorts on.
     *
     * @return Name of the property the query sorts on.
     */
    public String getProperty() {
        return property;
    }

    /**
     * Returns the sort direction.
     *
     * @return {@link LeanQuery.SortDirection}
     */
    public LeanQuery.SortDirection getDirection() {
        return direction;
    }

}
