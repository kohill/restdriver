package com.healthedge.rest.driver;

public class Header {
    private final String name;
    private final String value;

    /**
     * Create a new header with the given name and value.
     *
     * @param name  The header name, cannot be null.
     * @param value The value (can be null)
     */
    public Header(String name, String value) {
        if (name == null) {
            throw new RuntimeException("Header name can't be NULL");
        }
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean hasSameNameAs(Header header) {
        if (name == null) {
            throw new RuntimeException("Header name can't be NULL");
        }
        return this.name.equalsIgnoreCase(header.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Header header = (Header) o;

        // HTTP header names are always case-insensitive. Values are usually case-insensitive.
        if (name != null ? !name.equalsIgnoreCase(header.name) : header.name != null) return false;
        return value != null ? value.equalsIgnoreCase(header.value) : header.value == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(name);
        if (value != null) {
            builder.append("=").append(value);
        }
        return builder.toString();
    }
}
