/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import java.util.Map;

public class LeanAccount {

    public Long id;
    public String nickName;
    public String providerId;
    public String provider;
    public Map<String, Object> providerProperties;

    protected LeanAccount(Long id, String nickName, String providerId, String provider, Map<String, Object> providerProperties) {
        this.id = id;
        this.nickName = nickName;
        this.providerId = providerId;
        this.provider = provider;
        this.providerProperties = providerProperties;
    }

    /**
     * Logs user out of server. Current authentication tokens are removed on server and on client.
     * @return Returns true if user was logged in and was logged out successfully.
     * @throws LeanException
     */
    public static Boolean logout() throws LeanException {
        return RestService.logout();
    }

    /**
     * Logs user out of server. Current authentication tokens are removed on server and on client.
     * Runs on the background thread.
     * @param callback Callback to be invoked when logout finishes.
     * @throws LeanException
     */
    public static void logoutInBackground(NetworkCallback<Boolean> callback) {
        RestService.logoutAsync(callback);
    }

    /**
     * Checks if user is logged in.
     * @return Returns true if user is logged in, false otherwise.
     */
    public static boolean isTokenAvailable() {
        return LeanEngine.getAuthToken() != null;
    }

    /**
     * Gets current user account.
     * @return LeanAccount of the currently logged-in user.
     * @throws LeanException
     */
    public static LeanAccount checkCurrentAccountIsValid() throws LeanException {
        return RestService.getCurrentAccountData();
    }
}
