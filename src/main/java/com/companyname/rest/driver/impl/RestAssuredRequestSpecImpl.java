package com.companyname.rest.driver.impl;

import com.companyname.config.props.ConfigLoader;
import com.companyname.config.props.PropertyReader;
import com.companyname.rest.authentication.DefaultAuthFunction;
import com.companyname.rest.config.RestConstants;
import com.companyname.rest.driver.HttpMethod;
import com.companyname.rest.driver.IRequestSpec;
import com.companyname.rest.logging.RALogger;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;

public class RestAssuredRequestSpecImpl implements IRequestSpec {
    static {
        RestAssured.filters(new RALogger.LogFilter());
        RestAssured.config = RestAssured.config()
                .encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false))
                .objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.GSON))
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.socket.timeout", Integer.valueOf(PropertyReader.getProperty(RestConstants.Properties.REST_RESPONSE_TIMEOUT, "180000")))
                        .setParam("http.connection.timeout", Integer.valueOf(PropertyReader.getProperty(RestConstants.Properties.REST_RESPONSE_TIMEOUT, "180000"))));
    }

    private final RequestSpecification spec;
    private static final Function<String, String> authFunction = ConfigLoader.getClassInstance(Function.class, DefaultAuthFunction.class, RestConstants.Properties.REST_OAUTH2_FUNCTION_CLASS);
    private Boolean isAuthSet = false;

    public RestAssuredRequestSpecImpl() {
        spec = RestAssured.given();
    }

    @Override
    public IRequestSpec body(String body) {
        spec.body(body);
        return this;
    }

    @Override
    public IRequestSpec body(InputStream body) {
        spec.body(body);
        return this;
    }

    @Override
    public IRequestSpec body(Object body) {
        spec.body(body);
        return this;
    }

    @Override
    public IRequestSpec params(String firstParameterName, Object firstParameterValue, Object... parameterNameValuePairs) {
        spec.params(firstParameterName, firstParameterValue, parameterNameValuePairs);
        return this;
    }

    @Override
    public IRequestSpec params(Map<String, ?> parametersMap) {
        spec.params(parametersMap);
        return this;
    }

    @Override
    public IRequestSpec queryParams(String firstParameterName, Object firstParameterValue, Object... parameterNameValuePairs) {
        spec.queryParams(firstParameterName, firstParameterValue, parameterNameValuePairs);
        return this;
    }

    @Override
    public IRequestSpec queryParams(Map<String, ?> parametersMap) {
        spec.queryParams(parametersMap);
        return this;
    }

    @Override
    public IRequestSpec formParams(String firstParameterName, Object firstParameterValue, Object... parameterNameValuePairs) {
        spec.formParams(firstParameterName, firstParameterValue, parameterNameValuePairs);
        return this;
    }

    @Override
    public IRequestSpec formParams(Map<String, ?> parametersMap) {
        spec.formParams(parametersMap);
        return this;
    }

    @Override
    public IRequestSpec pathParams(String firstParameterName, Object firstParameterValue, Object... parameterNameValuePairs) {
        spec.pathParams(firstParameterName, firstParameterValue, parameterNameValuePairs);
        return this;
    }

    @Override
    public IRequestSpec pathParams(Map<String, ?> parameterNameValuePairs) {
        spec.pathParams(parameterNameValuePairs);
        return this;
    }

    /**
     * @return The original Rest Assured  {@link RequestSpecification}
     */
    @Override
    public RequestSpecification getOriginalSpec() {
        return spec;
    }

    @Override
    public IRequestSpec baseUri(String baseUri) {
        spec.baseUri(baseUri);
        return this;
    }

    @Override
    public IRequestSpec basePath(String basePath) {
        spec.basePath(basePath);
        return this;
    }

    @Override
    public <T> RestAssuredResponseImpl request(HttpMethod method, Class<T> expectedResponseType) {
        return new RestAssuredResponseImpl(spec.request(method.toString()), expectedResponseType);
    }

    @Override
    public <T> RestAssuredResponseImpl request(String method, Class<T> expectedResponseType) {
        return new RestAssuredResponseImpl(spec.request(method), expectedResponseType);
    }

    @Override
    public IRequestSpec contentType(String contentType) {
        spec.contentType(contentType);
        return this;
    }

    @Override
    public IRequestSpec contentType(ContentType contentType) {
        spec.contentType(contentType);
        return this;
    }

    @Override
    public IRequestSpec accept(String mediaTypes) {
        spec.accept(mediaTypes);
        return this;
    }

    @Override
    public IRequestSpec headers(String firstParameterName, Object firstParameterValue, Object... parameterNameValuePairs) {
        spec.headers(firstParameterName, firstParameterValue, parameterNameValuePairs);
        return this;
    }

    @Override
    public IRequestSpec headers(Map<String, ?> parameterNameValuePairs) {
        spec.headers(parameterNameValuePairs);
        return this;
    }

    @Override
    public IRequestSpec authHeader(String authTokenValue) {
        spec.headers("Authorization", authTokenValue);
        isAuthSet = true;
        return this;
    }

    @Override
    public IRequestSpec authBasic(String userName, String password) {
        spec.auth().preemptive().basic(userName, password);
        isAuthSet = true;
        return this;
    }

    @Override
    public IRequestSpec authBasic(String token) {
        spec.headers("Authorization", "Basic " + token);
        isAuthSet = true;
        return this;
    }

    @Override
    public IRequestSpec authOauth2(String accessToken) {
        spec.auth().preemptive().oauth2(accessToken);
        isAuthSet = true;
        return this;
    }

    @Override
    public IRequestSpec authFunction(String functionName) {
        String token;
        try {
            token = authFunction.apply(functionName);
        } catch (Exception e) {
            throw new RuntimeException("Not valid authentication name or not possible to get authentication data. Auth Function: " + authFunction.getClass().getSimpleName() + "#" + functionName, e);
        }
        return this.authHeader(token);
    }

    @Override
    public Boolean isAuthSet() {
        return isAuthSet;
    }

    @Override
    public IRequestSpec multiPart(File file) {
        spec.multiPart(file);
        return this;
    }

    @Override
    public IRequestSpec multiPart(String controlName, File file) {
        spec.multiPart(controlName, file);
        return this;
    }

    @Override
    public IRequestSpec multiPart(String controlName, File file, String mimeType) {
        spec.multiPart(controlName, file, mimeType);
        return this;
    }

    @Override
    public IRequestSpec multiPart(String controlName, Object object) {
        spec.multiPart(controlName, object);
        return this;
    }

    @Override
    public IRequestSpec multiPart(String controlName, Object object, String mimeType) {
        spec.multiPart(controlName, object, mimeType);
        return this;
    }

    @Override
    public IRequestSpec multiPart(String controlName, String filename, Object object, String mimeType) {
        spec.multiPart(controlName, filename, object, mimeType);
        return this;
    }

    @Override
    public IRequestSpec multiPart(String controlName, String fileName, byte[] bytes) {
        spec.multiPart(controlName, fileName, bytes);
        return this;
    }

    @Override
    public IRequestSpec multiPart(String controlName, String fileName, byte[] bytes, String mimeType) {
        spec.multiPart(controlName, fileName, bytes, mimeType);
        return this;
    }

    @Override
    public IRequestSpec multiPart(String controlName, String fileName, InputStream stream) {
        spec.multiPart(controlName, fileName, stream);
        return this;
    }

    @Override
    public IRequestSpec multiPart(String controlName, String fileName, InputStream stream, String mimeType) {
        spec.multiPart(controlName, fileName, stream, mimeType);
        return this;
    }

    @Override
    public IRequestSpec multiPart(String controlName, String contentBody) {
        spec.multiPart(controlName, contentBody);
        return this;
    }

    @Override
    public IRequestSpec multiPart(String controlName, String contentBody, String mimeType) {
        spec.multiPart(controlName, contentBody, mimeType);
        return this;
    }
}
