package com.healthedge.rest;

import com.healthedge.config.props.ConfigLoader;
import com.healthedge.config.props.PropertyReader;
import com.healthedge.rest.config.DefaultRestURLSupplier;
import com.healthedge.rest.config.RestConstants;
import com.healthedge.rest.driver.IRequestSpec;
import com.healthedge.rest.driver.impl.RestAssuredRequestSpecImpl;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

public class RestContext {
    protected static String baseUri = (String) ConfigLoader.getClassInstance(Supplier.class, DefaultRestURLSupplier.class, RestConstants.Properties.REST_URL_SUPPLIER_CLASS).get();
    protected static String authToken = PropertyReader.getProperty(RestConstants.Properties.REQUEST_AUTH_TOKEN);
    protected static String authFunctionName = PropertyReader.getProperty(RestConstants.Properties.REQUEST_AUTH_FUNCTION_NAME);

    /**
     * Provides pre-configured specification.
     * For RestAssured it provides RestAssured.given() instance with already set URL, Endpoint and ContentType.
     * By default configuration is taken from:
     * URL - form the property <b>'rest.base.uri'</b>
     * Endpoint  and ContentType from configuration <b>{serviceName}</b> file located in <b>'config/dxp'</b> folder
     *
     * @return Pre-configured specification instance
     */
    public IRequestSpec getSpec() {
        IRequestSpec spec = ConfigLoader.getClassInstance(IRequestSpec.class, RestAssuredRequestSpecImpl.class, RestConstants.Properties.REST_SPEC_DRIVER);
        spec.baseUri(baseUri).contentType(ContentType.JSON);

        if (!spec.isAuthSet()) {
            if (StringUtils.isNoneEmpty(authFunctionName)) {
                spec.authFunction(authFunctionName);
            }
            if (StringUtils.isNoneEmpty(authToken)) {
                spec.authHeader(authToken);
            }
        }

        return spec;
    }

    public IRequestSpec getSpec(String endpoint) {
        return getSpec().basePath(endpoint);
    }

    public String getBaseUri() {
        return baseUri;
    }
}
