/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import com.leanengine.LeanError;

import java.util.concurrent.ExecutionException;

public interface NetworkCallback<T> {

    public abstract void onResult(T... result);

    public abstract void onFailure(LeanError error);

}
