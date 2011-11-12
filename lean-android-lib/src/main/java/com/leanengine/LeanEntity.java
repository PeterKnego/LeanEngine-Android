package com.leanengine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LeanEntity {
    protected final String kind;
    protected long id;
    protected long accountID;
    protected Map<String, Object> properties = new HashMap<String, Object>();

    protected LeanEntity(String kind) {
        this.kind = kind;
    }

    protected LeanEntity(String kind, long id, long accountID) {
        this.kind = kind;
        this.id = id;
        this.accountID = accountID;
    }

    public static LeanEntity init(String kind) {
        return new LeanEntity(kind);
    }

    /**
     * Saves this Entity to the server. Saving is performed in the background ()
     *
     * @param callback NetworkCallback that on success returns ID of the saved Entity.
     */
    public void saveInBackground(NetworkCallback<Long> callback) {
        RestService.putPrivateEntity(this, callback);
    }

    /**
     * Saves this Entity to the server. This method call blocks until result is available.
     *
     * @return ID of the saved Entity.
     */
    public long save() throws LeanException {
        return RestService.putPrivateEntity(this);
    }

    /**
     * Retrieves from server all Entities of certain kind. Returns only Entities belonging to current user account.
     *
     * @param kind
     * @param callback
     */
    public static void getAllEntitiesInBackground(String kind, NetworkCallback<LeanEntity> callback) {
        RestService.getPrivateEntitiesAsync(kind, callback);
    }

    public Iterator<Map.Entry<String, Object>> getPropertiesIterator() {
        return properties.entrySet().iterator();
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public String getKind() {
        return kind;
    }

    public long getId() {
        return id;
    }

    public long getAccountID() {
        return accountID;
    }

    public String getString(String key) {
        Object val = properties.get(key);
        return (val != null && val.getClass() == String.class) ? (String) val : null;
    }

    public void put(String key, Object value) {
        properties.put(key, value);
    }

    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }
}
