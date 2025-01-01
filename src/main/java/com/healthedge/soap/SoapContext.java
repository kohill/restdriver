package com.healthedge.soap;

import com.healthedge.config.props.PropertyReader;
import com.healthedge.rest.config.RestConstants;
import com.healthedge.rest.driver.IRequestSpec;
import com.healthedge.rest.driver.impl.RestAssuredRequestSpecImpl;
import io.restassured.http.ContentType;

public class SoapContext {

    protected static String soapBase = PropertyReader.getProperty(RestConstants.Properties.SOAP_BASE_URI);

    public IRequestSpec getSpec() {
        return getSpec(soapBase, ContentType.XML);
    }

    public IRequestSpec getSpec(String uri, ContentType contentType) {
        return new RestAssuredRequestSpecImpl().baseUri(uri).contentType(contentType);
    }

    public IRequestSpec getSpec(String endpoint) {
        return getSpec().basePath(endpoint);
    }
}
