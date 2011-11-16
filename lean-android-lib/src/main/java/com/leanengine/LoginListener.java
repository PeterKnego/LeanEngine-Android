/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

public interface LoginListener {

    public void onSuccess(String token);

    public void onCancel();


    /**
     * Called when lofgin error happens.
     * Error codes depend on provider:
     * Facebook: http://developers.facebook.com/docs/oauth/errors/
     * Twitter:
     */
    public void onError(LeanError error);

}
