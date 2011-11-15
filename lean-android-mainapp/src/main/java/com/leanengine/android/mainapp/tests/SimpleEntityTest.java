package com.leanengine.android.mainapp.tests;

import android.util.Log;
import com.leanengine.LeanEntity;
import junit.framework.Assert;
import junit.framework.TestCase;

public class SimpleEntityTest extends TestCase {

    public SimpleEntityTest(String name) {
        super(name);
    }

    public void testCreateEntity() {
        LeanEntity entity = LeanEntity.init("testEntity");
        entity.put("propLong", (byte)12);
        entity.put("propString", "some short string");

        Assert.assertTrue(true);
    }

    public void testLoadEntity() {
        Assert.assertTrue(true);

    }

    public void testQueryEntity() {
        Assert.assertTrue(true);

    }

    public void testDeleteEntity() {
        Assert.assertTrue(true);

    }

}
