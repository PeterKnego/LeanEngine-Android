/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * LeanEntity is a basic data unit that can be stored on the server. It can be saved, retrieved, deleted and queried.
 * Entities can contain properties of various types: long, double, boolean, String and Date.
 * <br/><br/>
 * Future enhancements: new properties will be Entity reference, Blob, GeoPoint, Image..
 * <br/><br/>
 * Basic usage is to create a named entity via {@link #init(String)} method and then use  {@link #save()} to store it
 * to server. Other methods are {@link #get(String)}, {@link #getAll(String)} and {@link #delete()}.
 * <br/><br/>
 * All method have their asynchronous counterparts, designated by added 'InBackground' to method name, which allow
 * performing the method in the background thread.
 */
public class LeanEntity {
    protected final String kind;
    protected Long id;
    protected Long accountID;
    protected Map<String, Object> properties = new HashMap<String, Object>();

    protected LeanEntity(String kind) {
        this.kind = kind;
    }

    protected LeanEntity(String kind, long id, long accountID) {
        this.kind = kind;
        this.id = id;
        this.accountID = accountID;
    }

    /**
     * Static method to create an entity of given kind.
     * Entities created via {@code init()} method do not have their {@code id} and {@code accountID} fields set,
     * even when they are saved to server.
     *
     * @param kind Tke kind of the entity.
     * @return
     */
    public static LeanEntity init(String kind) {
        return new LeanEntity(kind);
    }

    /**
     * Saves this entity to the server. Saving is performed on the background thread.
     * <br/><br/>
     * This method does not set the  {@code ID} and {@code accountID} fields of this entity.
     *
     * @param callback NetworkCallback that on success returns ID of the saved Entity.
     */
    public void saveInBackground(NetworkCallback<Long> callback) {
        RestService.putPrivateEntityAsync(this, callback);
    }

    /**
     * Saves this entity to the server. This method call blocks until result is available.
     * <br/><br/>
     * This method does not set the  {@code ID} and {@code accountID} fields of this entity.
     *
     * @return ID of the saved Entity.
     * @throws LeanException In case of authentication, network and data parsing errors.
     */
    public long save() throws LeanException {
        return RestService.putPrivateEntity(this);
    }

    /**
     * Retrieves from server an entity of given kind and ID.
     * Only returns entity if it belongs to the current user account.
     * This is a blocking operation - it block the execution of current thread until result is returned.
     *
     * @param kind The kind of the Entity to be retrieved.
     * @param id   ID of the Entity
     * @throws IllegalArgumentException If parameter 'kind' is null
     * @throws LeanException            In case of authentication, network and data parsing errors.
     */
    public static void get(String kind, long id) throws LeanException, IllegalArgumentException {
        RestService.getPrivateEntity(kind, id);
    }


    /**
     * Retrieves from server an entities of certain kind and ID.
     * Only returns entity if it belongs to the current user account.
     * Operation is performed in the background thread.
     *
     * @param kind     The kind of the Entity to be retrieved.
     * @param id       ID of the Entity
     * @param callback Callback to be invoked in case of result or error.
     */
    public static void getInBackground(String kind, long id, NetworkCallback<LeanEntity> callback) {
        RestService.getPrivateEntityAsync(kind, id, callback);
    }

    /**
     * Retrieves from server all entities of certain kind.
     * Returns only entities belonging to current user account.
     * Operation is performed in the background thread.
     *
     * @param kind     The kind of the Entities to be retrieved.
     * @param callback Callback to be invoked in case of result or error.
     */
    public static void getAllInBackground(String kind, NetworkCallback<LeanEntity> callback) {
        RestService.getPrivateEntitiesAsync(kind, callback);
    }

    /**
     * Retrieves from server all entities of certain kind.
     * Returns only entities belonging to current user account (private).
     * This is a blocking operation - it block the execution of current thread until result is returned.
     *
     * @param kind The kind of the entities to be retrieved.
     * @return An array of LeanEntity
     * @throws LeanException In case of authentication, network and data parsing errors.
     */
    public static LeanEntity[] getAll(String kind) throws LeanException {
        return RestService.getPrivateEntities(kind);
    }

    /**
     * Deletes an entity of the given kind an ID fro the server datastore..
     *
     * @param kind     The kind of the entity to delete.
     * @param entityId The ID of the entity to delete.
     * @throws LeanException In case of authentication, network and data parsing errors.
     */
    public static void delete(String kind, long entityId) throws LeanException {
        RestService.deletePrivateEntity(kind, entityId);
    }

    public void delete() throws LeanException {
        RestService.deletePrivateEntity(this.kind, this.id);
    }

    /**
     * Returns an iterator of entity properties.
     *
     * @return Iterator over properties.
     */
    public Iterator<Map.Entry<String, Object>> getPropertiesIterator() {
        return properties.entrySet().iterator();
    }

    /**
     * Returns the entity properties map.
     *
     * @return Map of entity properties.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * @return Returns the kind of the entity.
     */
    public String getKind() {
        return kind;
    }

    /**
     * @return Returns the ID of the entity.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the user account ID that this entity belongs to.
     * Account ID is only available for entities retrieved from server.
     */
    public Long getAccountID() {
        return accountID;
    }

    /**
     * Gets the property with the specified key.
     * <br/><br/>
     * The value returned may not be the same type as originally set via {@code putXYZ(String, Object)}.
     * Object types are internally converted to types supported by LeanEngine server: //todo insert LE Docs link here
     *
     * @param key Key (name) of the property.
     * @return Value of the property or {@code null} if property with given key does not exist.
     */
    public Object get(String key) {
        return properties.get(key);
    }

    /**
     * Gets the property with the specified key.
     * {@code Null} is returned if key does not exist or if property is not of type {@link String}.
     * <br/><br/>
     * The value returned may not be the same type as originally set via {@link #put(String, String)}.
     * Object types are internally converted to types supported by LeanEngine server: //todo insert LE Docs link here
     *
     * @param key Key under which property was stored.
     * @return Value of property or {@code null} if key does not exist or if property is not of type {@link String}.
     */
    public String getString(String key) {
        Object val = properties.get(key);
        return (val != null && val.getClass() == String.class) ? (String) val : null;
    }

    /**
     * Gets the property with the specified key.
     * {@code Null} is returned if key does not exist or if property is not of type {@link Long}.
     * <br/><br/>
     * The value returned may not be the same type as originally set via {@link #put(String, long)}.
     * Object types are internally converted to types supported by LeanEngine server: //todo insert LE Docs link here
     *
     * @param key Key under which property was stored.
     * @return Value of property or {@code null} if key does not exist or if property is not of type {@link Long}.
     */
    public Long getLong(String key) {
        Object val = properties.get(key);
        return (val != null && val.getClass() == Long.class) ? (Long) val : null;
    }

    /**
     * Gets the property with the specified key.
     * {@code Null} is returned if key does not exist or if property is not of type {@link Double}.
     * <br/><br/>
     * The value returned may not be the same type as originally set via {@link #put(String, double)}.
     * Object types are internally converted to types supported by LeanEngine server: //todo insert LE Docs link here
     *
     * @param key Key under which property was stored.
     * @return Value of property or {@code null} if key does not exist or if property is not of type {@link Double}.
     */
    public Double getDouble(String key) {
        Object val = properties.get(key);
        return (val != null && val.getClass() == Double.class) ? (Double) val : null;
    }

    /**
     * Gets the property with the specified key.
     * {@code Null} is returned if key does not exist or if property is not of type {@link java.util.Date}.
     * <br/><br/>
     * The value returned may not be the same type as originally set via {@link #put(String, java.util.Date)}.
     * Object types are internally converted to types supported by LeanEngine server: //todo insert LE Docs link here
     *
     * @param key Key under which property was stored.
     * @return Value of property or {@code null} if key does not exist or if property is not of type {@link java.util.Date}.
     */
    public Date getDate(String key) {
        Object val = properties.get(key);
        return (val != null && val.getClass() == Date.class) ? (Date) val : null;
    }

    /**
     * Gets the property with the specified key.
     * {@code Null} is returned if key does not exist or if property is not of type {@link Boolean}.
     * <br/><br/>
     * The value returned may not be the same type as originally set via {@link #put(String, boolean)}.
     * Vales are internally converted to types supported by LeanEngine server: //todo insert LE Docs link here
     *
     * @param key Key under which property was stored.
     * @return Value of property or {@code null} if key does not exist or if property is not of type {@link Boolean}.
     */
    public Boolean getBoolean(String key) {
        Object val = properties.get(key);
        return (val != null && val.getClass() == Boolean.class) ? (Boolean) val : null;
    }

    /**
     * Sets the property with given {@code key} to {@code value}.
     * <br/><br/>
     * Vales are internally converted to types supported by LeanEngine server: //todo insert LE Docs link here
     *
     * @param key Key under which property will be stored.
     * @param value {@link long} value to be stored.
     */
    public void put(String key, long value) {
        properties.put(key, value);
    }

    /**
     * Sets the property with given {@code key} to {@code value}.
     * <br/><br/>
     * Vales are internally converted to types supported by LeanEngine server: //todo insert LE Docs link here
     *
     * @param key Key under which property will be stored.
     * @param value {@link double} value to be stored.
     */
    public void put(String key, double value) {
        properties.put(key, value);
    }

    /**
     * Sets the property with given {@code key} to {@code value}.
     * <br/><br/>
     * Vales are internally converted to types supported by LeanEngine server: //todo insert LE Docs link here
     *
     * @param key Key under which property will be stored.
     * @param value {@link String} value to be stored.
     */
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
