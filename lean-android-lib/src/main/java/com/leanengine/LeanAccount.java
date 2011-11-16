package com.leanengine;

import java.util.Map;

public class LeanAccount {

    public Long id;
    public String nickName;
    public String providerId;
    public String provider;
    public Map<String, Object> providerProperties;

    public LeanAccount(Long id, String nickName, String providerId, String provider, Map<String, Object> providerProperties) {
        this.id = id;
        this.nickName = nickName;
        this.providerId = providerId;
        this.provider = provider;
        this.providerProperties = providerProperties;
    }

    /**
     * @return
     * @throws LeanException
     */
    public static Boolean logout() throws LeanException {
        return RestService.logout();
    }

    /**
     * @return
     * @throws LeanException
     */
    public static void logoutInBackground(NetworkCallback<Boolean> callback) {
        RestService.logoutAsync(callback);
    }

    public static boolean isLoggedIn() {
        return LeanEngine.getLoginData() != null;
    }

    public static LeanAccount getCurrentAccount() throws LeanException {
        return RestService.getCurrentAccountData();
    }
}
