package com.companyname.soap;

import com.companyname.config.props.PropertyReader;
import com.companyname.rest.config.RestConstants;
import com.companyname.rest.driver.IRequestSpec;
import com.companyname.rest.driver.impl.RestAssuredRequestSpecImpl;
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
