package com.leanengine.android.mainapp.tests;

import android.util.Log;
import com.leanengine.LeanAccount;
import com.leanengine.LeanException;
import junit.framework.Assert;
import junit.framework.TestCase;

public class AccountTest extends TestCase {

    public AccountTest(String name) {
        super(name);
    }

    public void accountNickName() {
        try {
            LeanAccount currentAccount = LeanAccount.getCurrentAccount();
            Assert.assertNotNull(currentAccount);
            Assert.assertNotNull(currentAccount.nickName);
            Assert.assertNotNull(currentAccount.providerProperties);
            Assert.assertTrue(!currentAccount.providerProperties.isEmpty());
        } catch (LeanException e) {
            Assert.assertTrue(false);
            Log.e("AccountTest", "error:" + e.getError().getErrorCode() + " msg:" + e.getError().getErrorMessage());
        }
    }

}
