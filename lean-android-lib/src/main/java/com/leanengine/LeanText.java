package com.leanengine;

/**
 * Internal wrapper class for {@link String}, used to mark string values that need to be treated by server as
 * long unindexed text values.
 * <br></br>
 * Not intended for end-user use.
 */
public class LeanText {

    private final String value;

    LeanText(String value) {
        this.value = value;
    }

    /**
     * Returns the wrapped {@code String} value.
     * @return The wrapped value.
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return this.getClass() == o.getClass() && value.equals(o);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
