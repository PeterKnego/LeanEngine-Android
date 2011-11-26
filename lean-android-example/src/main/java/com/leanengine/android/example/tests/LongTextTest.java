package com.leanengine.android.example.tests;

import android.util.Log;
import com.leanengine.LeanEntity;
import com.leanengine.LeanException;
import junit.framework.Assert;
import junit.framework.TestCase;

public class LongTextTest extends TestCase {

    private long entitiyID;

    // fixed entity name must be used because of indexes
    // this will clash if multiple clients run tests at the same time
    private String entityName = "testLongText";

    private String longUnicodeText = "Lorem ipsum dolor sit amet, elit malesuada consectetuer, massa erat ut amet dui " +
            "sed. Integer sociis condimentum libero, donec in ante vestibulum. Placerat duis nulla elit arcu, pretium " +
            "commodo vitae pellentesque litora dolor neque, nostra lacus in nunc inceptos dapibus, aliquet ac " +
            "pellentesque elementum. Non sed urna ante feugiat purus diam, libero sit mattis nonummy natoque maecenas " +
            "erat, etiam metus ut pede sed vel laoreet, faucibus ut praesent scelerisque justo, luctus erat sit magna " +
            "nec porttitor. Non enim, malesuada viverra, at natoque tristique purus bibendum ad. Aliquet neque " +
            "scelerisque, ante vitae vehicula, faucibus at a, mus porttitor. " +
            "Duis ornare tristique nascetur ut, ante ipsum, nibh a nibh eros dis. Mi placerat et, donec ut urna " +
            "lacinia integer eget, sapien dis vestibulum ligula placerat. Pretium morbi, sed diam praesent, nam mollis " +
            "dictum ante malesuada proin, eget mi fames, massa praesent tristique ut diam elit. Ut interdum aenean " +
            "tempus quisque a quisque, rutrum nam, risus eget, felis quis lorem metus, ultrices metus. Orci nec ipsum " +
            "mi fermentum enim, purus aenean viverra, felis id cras cras, sit mi felis, arcu et metus pellentesque " +
            "neque dui. Quis massa nullam varius accumsan, eleifend risus ligula scelerisque gravida tincidunt sint, " +
            "a a id quam. Ut potenti pellentesque feugiat sem cum." +
            "Convallis neque enim, libero lobortis orci turpis sem. Nonummy sed a dolor, semper suspendisse non mauris. " +
            "Libero duis litora ac curabitur proin, in integer in felis in praesent ut, vel porttitor euismod neque. " +
            "Maecenas nibh id neque pellentesque enim a, ullamcorper risus vitae, est convallis et, vehicula orci " +
            "nulla velit libero pharetra. Velit metus nulla. Nec ligula sapien elit ac magnis, metus eu lobortis " +
            "phasellus donec vel aliquam, porttitor cras in elementum possimus. Id auctor quis pulvinar auctor ligula " +
            "wisi, mollis amet congue potenti sagittis neque arcu. Dui id suspendisse vivamus nam sed, malesuada ut " +
            "ullamcorper dui vestibulum ullamcorper dolor, at praesent, nibh dolor massa rhoncus lectus tellus, lacus " +
            "curabitur nulla. Vitae orci, suscipit a, justo tortor magna sed tristique a vel. Mauris nec ante, mattis " +
            "arcu in, sed ipsum, leo sagittis aliquet ipsum donec erat pretium. Ligula mi aliquam, nulla amet posuere " +
            "parturient, ut ut, rutrum sapien sodales odio magna, vel morbi massa vitae consectetuer. Eget sed " +
            "ornare magna tincidunt sapien nunc, pellentesque id, convallis molestie rutrum, cras faucibus per dis " +
            "vehicula, felis mauris luctus a vivamus.";


    public LongTextTest(String name) {
        super(name);
    }

    @Override
    protected void runTest() {
        createLongTextEntity();
        checkLongTextProperty();
        deleteLongTextEntity();
    }

    private void createLongTextEntity() {
        LeanEntity testEntity = LeanEntity.init(entityName);
        testEntity.putText("longUnicodeText", longUnicodeText);

        try {
            entitiyID = testEntity.save();
            Log.d("LongTextTest", "Created entity: " + entityName + "[" + entitiyID + "]");
        } catch (LeanException e) {
            Log.e("LongTextTest", "Error creating entity:" + e.getError().getErrorCode() + " msg:" + e.getError().getErrorMessage());
            Assert.assertTrue(false);
            return;
        }
        Assert.assertNotSame(0, entitiyID);

    }

    private void checkLongTextProperty() {
        LeanEntity testEntity;
        try {
            testEntity = LeanEntity.get(entityName, entitiyID);
        } catch (LeanException e) {
            Log.e("LongTextTest", "Error loading entity:" + e.getError().getErrorCode() + " msg:" + e.getError().getErrorMessage());
            Assert.assertTrue(false);
            return;
        }

        String lut = testEntity.getText("longUnicodeText");
        Assert.assertNotNull(lut);
        Assert.assertEquals(lut, longUnicodeText);
    }

    private void deleteLongTextEntity() {
        try {
            LeanEntity.delete(entityName, entitiyID);
            Log.d("LongTextTest", "Deleted entity: " + entityName + "[" + entitiyID + "]");
        } catch (LeanException e) {
            Log.e("LongTextTest", "Error deleting entity:" + e.getError().getErrorCode() + " msg:" + e.getError().getErrorMessage());
            Assert.assertTrue(false);
            return;
        }
        Assert.assertTrue(true);
    }


}
