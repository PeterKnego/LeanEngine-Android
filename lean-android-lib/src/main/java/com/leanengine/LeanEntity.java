package com.leanengine;

import java.util.Date;
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
     * Saves this Entity to the server. Saving is performed on the background thread.
     *
     * @param callback NetworkCallback that on success returns ID of the saved Entity.
     */
    public void saveInBackground(NetworkCallback<Long> callback) {
        RestService.putPrivateEntityAsync(this, callback);
    }

    /**
     * Saves this Entity to the server. This method call blocks until result is available.
     *
     * @return ID of the saved Entity.
     * @throws LeanException In case of authentication, network and data parsing errors.
     */
    public long save() throws LeanException {
        return RestService.putPrivateEntity(this);
    }

    /**
     * Retrieves from server an Entities of certain kind and ID.
     * Only returns entity if it belongs to the current user account.
     * This is a blocking operation - it block the execution of current thread until result is returned.
     *
     * @param kind The kind of the Entity to be retrieved.
     * @param id   ID of the Entity
     * @throws IllegalArgumentException If parameter 'kind' is null
     * @throws LeanException            In case of authentication, network and data parsing errors.
     */
    public static void getEntity(String kind, long id) throws LeanException, IllegalArgumentException {
        RestService.getPrivateEntity(kind, id);
    }


    /**
     * Retrieves from server an Entities of certain kind and ID.
     * Only returns entity if it belongs to the current user account.
     * Operation is performed in the background thread.
     *
     * @param kind     The kind of the Entity to be retrieved.
     * @param id       ID of the Entity
     * @param callback Callback to be invoked in case of result or error.
     */
    public static void getEntityInBackground(String kind, long id, NetworkCallback<LeanEntity> callback) {
        RestService.getPrivateEntityAsync(kind, id, callback);
    }

    /**
     * Retrieves from server all Entities of certain kind. Returns only entities belonging to current user account.
     * Operation is performed in the background thread.
     *
     * @param kind     The kind of the Entities to be retrieved.
     * @param callback Callback to be invoked in case of result or error.
     */
    public static void getAllEntitiesInBackground(String kind, NetworkCallback<LeanEntity> callback) {
        RestService.getPrivateEntitiesAsync(kind, callback);
    }

    /**
     * Retrieves from server all Entities of certain kind. Returns only entities belonging to current user account.
     * This is a blocking operation - it block the execution of current thread until result is returned.
     *
     * @param kind The kind of the Entities to be retrieved.
     * @return An array of LeanEntity
     * @throws LeanException In case of authentication, network and data parsing errors.
     */
    public static LeanEntity[] getAllEntities(String kind) throws LeanException {
        return RestService.getPrivateEntities(kind);
    }


    public static void delete(String kind, long entityId) throws LeanException {
        RestService.deletePrivateEntity(kind, entityId);
    }

    public void delete() throws LeanException {
        RestService.deletePrivateEntity(this.kind, this.id);
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

    public Object get(String key) {
        return properties.get(key);
    }

    public String getString(String key) {
        Object val = properties.get(key);
        return (val != null && val.getClass() == String.class) ? (String) val : null;
    }

    public Long getLong(String key) {
        Object val = properties.get(key);
        return (val != null && val.getClass() == Long.class) ? (Long) val : null;
    }

    public Double getDouble(String key) {
        Object val = properties.get(key);
        return (val != null && val.getClass() == Double.class) ? (Double) val : null;
    }

    public Date getDate(String key) {
        Object val = properties.get(key);
        return (val != null && val.getClass() == Date.class) ? (Date) val : null;
    }

    public Boolean getBoolean(String key) {
        Object val = properties.get(key);
        return (val != null && val.getClass() == Boolean.class) ? (Boolean) val : null;
    }

    public void put(String key, long value) {
        properties.put(key, value);
    }

    public void put(String key, double value) {
        properties.put(key, value);
    }

    public void put(String key, String value) {
        properties.put(key, value);
    }

    public void put(String key, Date value) {
        properties.put(key, value);
    }

    public void put(String key, boolean value) {
        properties.put(key, value);
    }

    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }
}
