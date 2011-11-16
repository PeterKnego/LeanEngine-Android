/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import com.leanengine.LeanEntity;

import java.util.List;

public class QueryResult {
    private List<LeanEntity> result;
    private String cursor;

    public QueryResult(List<LeanEntity> result, String cursor) {
        this.result = result;
        this.cursor = cursor;
    }

    public List<LeanEntity> getResult() {
        return result;
    }

    public String getCursor() {
        return cursor;
    }
}
