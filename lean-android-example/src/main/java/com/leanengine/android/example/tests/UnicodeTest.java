package com.leanengine.android.example.tests;

import android.util.Log;
import com.leanengine.LeanEntity;
import com.leanengine.LeanException;
import junit.framework.Assert;
import junit.framework.TestCase;

public class UnicodeTest extends TestCase {

       private long entitiyID;

    // fixed entity name must be used because of indexes
    // this will clash if multiple clients run tests at the same time
    private String entityName = "testUnicode";

    private String unicodeTextEncoded = "\u0664\u0520";
    private String unicodeTextPlain = "٤Ԡ";


    public UnicodeTest(String name) {
        super(name);
    }

    @Override
    protected void runTest() {
        createUnicodeEntity();
        checkUnicodeProperty();
        deleteUnicodeEntity();
    }

    private void createUnicodeEntity() {
        LeanEntity testEntity = LeanEntity.init(entityName);
        testEntity.put("unicodeTextEncoded", unicodeTextEncoded);
        testEntity.put("unicodeTextPlain", unicodeTextPlain);

        try {
            entitiyID = testEntity.save();
            Log.d("UnicodeTest", "Created entity: " + entityName + "[" + entitiyID + "]");
        } catch (LeanException e) {
            Log.e("UnicodeTest", "Error creating entity:" + e.getError().getDetailCode() + " msg:" + e.getError().getErrorMessage());
            Assert.assertTrue(false);
            return;
        }
        Assert.assertNotSame(0, entitiyID);

    }

    private void checkUnicodeProperty() {
        LeanEntity testEntity;
        try {
            testEntity = LeanEntity.get(entityName, entitiyID);
        } catch (LeanException e) {
            Log.e("UnicodeTest", "Error loading entity:" + e.getError().getDetailCode() + " msg:" + e.getError().getErrorMessage());
            Assert.assertTrue(false);
            return;
        }

        String ute = testEntity.getString("unicodeTextEncoded");
        Assert.assertNotNull(ute);
        Assert.assertEquals(ute, unicodeTextEncoded);

        String utp = testEntity.getString("unicodeTextPlain");
        Assert.assertNotNull(utp);
        Assert.assertEquals(utp, unicodeTextPlain);
    }

    private void deleteUnicodeEntity() {
        try {
            LeanEntity.delete(entityName, entitiyID);
            Log.d("UnicodeTest", "Deleted entity: " + entityName + "[" + entitiyID + "]");
        } catch (LeanException e) {
            Log.e("UnicodeTest", "Error deleting entity:" + e.getError().getDetailCode() + " msg:" + e.getError().getErrorMessage());
            Assert.assertTrue(false);
            return;
        }
        Assert.assertTrue(true);
    }
}
