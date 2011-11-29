/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

/**
 * A listener interface used during authentication process.
 */
public interface LoginListener {

    /**
     * User successfully authenticated.
     */
    public void onSuccess();

    /**
     * User cancelled authentication procedure.
     */
    public void onCancel();


    /**
     * An authentication error happened.
     * @param error A {@link LeanError} containing detailed error code and description of error.
     */
    public void onError(LeanError error);

}
