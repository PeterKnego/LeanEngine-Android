package com.leanengine.android.mainapp.tests;

import android.util.Log;
import com.leanengine.LeanEntity;
import com.leanengine.LeanException;
import com.leanengine.LeanQuery;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class QueryTest extends TestCase {

    private List<Long> entitiyIDs = new ArrayList<Long>();

    // fixed entity name must be used because of indexes
    // this will clash if multiple clients run tests at the same time
    private String entityName = "nn";

    public QueryTest(String name) {
        super(name);
    }

    public void createQueryEntities() {
        long entitiyID;

        for (int i = 1; i <= 10; i++) {
            LeanEntity testEntity = LeanEntity.init(entityName);
            testEntity.put("x", i);
            try {
                entitiyID = testEntity.save();
                entitiyIDs.add(entitiyID);  // save ID of the entity
                Log.d("QueryTest", "Created entity: " + entityName + "[" + entitiyID + "]");
            } catch (LeanException e) {
                Log.e("QueryTest", "Error creating entity:" + e.getError().getErrorCode() + " msg:" + e.getError().getErrorMessage());
                Assert.assertTrue(false);
                return;
            }
        }
        Assert.assertEquals(10, entitiyIDs.size());
    }


    public void queryEntities() {
        LeanQuery query = new LeanQuery(entityName);
        query.addFilter("x", LeanQuery.FilterOperator.GREATER_THAN_OR_EQUAL, 2);
        query.addSort("x", LeanQuery.SortDirection.ASCENDING);
        query.limit(2);

        LeanEntity[] result = new LeanEntity[0];
        try {
            result = query.fetch();
        } catch (LeanException e) {
            Log.e("QueryTest", "Query error:" + e.getError().getErrorCode() + " msg:" + e.getError().getErrorMessage());
            Assert.assertTrue(false);
            return;
        }

        Assert.assertTrue(result != null && result.length == 2);

        try {
            result = query.fetchNext();
        } catch (LeanException e) {
            Log.e("QueryTest", "Query error:" + e.getError().getErrorCode() + " msg:" + e.getError().getErrorMessage());
            Assert.assertTrue(false);
            return;
        }
        Assert.assertTrue(result != null && result.length == 2);

    }

    public void deleteQueryEntities() {
        for (Long entitiyID : entitiyIDs) {
            try {
                LeanEntity.delete(entityName, entitiyID);
                Log.d("QueryTest", "Deleted entity: " + entityName + "[" + entitiyID + "]");
            } catch (LeanException e) {
                Log.e("QueryTest", "Error deleting entity:" + e.getError().getErrorCode() + " msg:" + e.getError().getErrorMessage());
                Assert.assertTrue(false);
                return;
            }
        }
        Assert.assertTrue(true);
    }

}
