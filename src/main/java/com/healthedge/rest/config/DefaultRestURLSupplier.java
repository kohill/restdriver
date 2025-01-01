package com.healthedge.rest.config;

import com.healthedge.config.props.PropertyReader;

import java.util.function.Supplier;

public class DefaultRestURLSupplier implements Supplier<String> {
    @Override
    public String get() {
        return PropertyReader.getProperty(RestConstants.Properties.REST_BASE_URI);
    }
}
