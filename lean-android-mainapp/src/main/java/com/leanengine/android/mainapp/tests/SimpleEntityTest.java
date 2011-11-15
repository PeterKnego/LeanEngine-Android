package com.leanengine.android.mainapp.tests;

import android.util.Log;
import com.leanengine.LeanEntity;
import com.leanengine.LeanException;
import com.leanengine.LeanQuery;
import junit.framework.Assert;
import junit.framework.TestCase;

public class SimpleEntityTest extends TestCase {

    public SimpleEntityTest(String name) {
        super(name);
    }

    public void testCreateEntity() {
        LeanEntity entity = LeanEntity.init("testEntity");
        entity.put("propLong", (byte) 12);
        entity.put("propString", "some short string");

        Assert.assertTrue(true);
    }

    public void testLoadEntity() {
        Assert.assertTrue(true);

    }

    public void testQueryEntity() {
        LeanQuery query = new LeanQuery("nn");
        query.addFilter("x", LeanQuery.FilterOperator.GREATER_THAN_OR_EQUAL, 2);
        query.addSort("x", LeanQuery.SortDirection.ASCENDING);
        query.limit(2);

        LeanEntity[] result = new LeanEntity[0];
        try {
            result = query.fetch();
        } catch (LeanException e) {
            Assert.assertTrue(false);
            Log.e("QUERY", "error:" + e.getError().getErrorCode() + " msg:" + e.getError().getErrorMessage());
        }

        Assert.assertTrue(result != null && result.length == 2);

        try {
            result = query.fetchNext();
        } catch (LeanException e) {
            Assert.assertTrue(false);
            Log.e("QUERY", "error:" + e.getError().getErrorCode() + " msg:" + e.getError().getErrorMessage());
        }
        Assert.assertTrue(result != null && result.length == 2);

    }

    public void testDeleteEntity() {
        Assert.assertTrue(true);

    }

}
