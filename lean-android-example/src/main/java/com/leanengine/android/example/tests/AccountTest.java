/*
 * This software is released under the BSD license. For full license see License-library.txt file.
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine.android.example.tests;

import android.util.Log;
import com.leanengine.LeanAccount;
import com.leanengine.LeanException;
import junit.framework.Assert;
import junit.framework.TestCase;

public class AccountTest extends TestCase {

    public AccountTest(String name) {
        super(name);
    }

    @Override
    protected void runTest() {
        accountNickName();
    }

    public void accountNickName() {
        try {
            LeanAccount currentAccount = LeanAccount.getAccountData();
            Assert.assertNotNull(currentAccount);
            Assert.assertNotNull(currentAccount.getNickName());
            Assert.assertNotNull(currentAccount.getProviderProperties());
            Assert.assertTrue(!currentAccount.getProviderProperties().isEmpty());
        } catch (LeanException e) {
            Log.e("AccountTest", "error:" + e.getError().getDetailCode() + " msg:" + e.getError().getErrorMessage());
            Assert.assertTrue(false);
        }
    }

}
