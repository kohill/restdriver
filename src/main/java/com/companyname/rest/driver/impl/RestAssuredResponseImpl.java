package com.companyname.rest.driver.impl;

import com.companyname.rest.driver.Header;
import com.companyname.rest.driver.IResponse;
import com.companyname.rest.driver.model.RestError;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestAssuredResponseImpl<T> implements IResponse<T> {

    private final Response response;
    private final Object entity;


    public <T> RestAssuredResponseImpl(Response response, Class<T> expectedResponseType) {
        this.response = response;
        this.entity = expectedResponseType;
    }

    public <R> R asModel(Class<T> cls) {
        if (!isSuccessful()) {
            return response.as((Type) RestError.class);
        } else {
            return response.body().as((Type) cls);
        }
    }

    @Override
    public <R> R asModel() {
        return asModel((Class<T>) entity);
    }

    @Override
    public <T> T asSuccess(Class<T> cls) {
        if (!isSuccessful()) {
            throw new RuntimeException(String.join("The response was not successful, we expect success here\n", " ", response.asString()));
        }
        return response.body().as((Class<T>) entity);
    }

    @Override
    public <T> T asSuccess() {
        return asSuccess((Class<T>) entity);
    }

    @Override
    public RestError asError() {
        if (isSuccessful()) {
            throw new RuntimeException("The response is not failed, we expect failure here");
        }
        return response.as(RestError.class);
    }

    @Override
    public String getContentType() {
        return response.getContentType();
    }

    @Override
    public Map<String, String> getCookies() {
        return response.getCookies();
    }

    @Override
    public String getHeader(String name) {
        return response.getHeader(name);
    }

    @Override
    public List<Header> getHeadersAsList() {
        return response.getHeaders().asList().stream().map(header -> new Header(header.getName(), header.getValue())).collect(Collectors.toList());
    }

    @Override
    public String getHeadersAsString() {
        return response.getHeaders().toString();
    }

    @Override
    public Boolean isSuccessful() {
        if (response == null) {
            throw new RuntimeException("Response is NULL: Server down or response not received.");
        }
        return String.valueOf(response.statusCode()).matches("20.");
    }

    @Override
    public int getStatusCode() {
        return response.getStatusCode();
    }

    @Override
    public String asString() {
        return response.body().asString();
    }


    @Override
    public XmlPath asXmlPath() {
        return new XmlPath(asString());
    }

    @Override
    public JsonPath asJsonPath() {
        return new JsonPath(asString());
    }

    @Override
    public String asPrettyString() {
        return response.asPrettyString();
    }

    @Override
    public byte[] asByteArray() {
        return response.asByteArray();
    }

    @Override
    public InputStream asInputStream() {
        return response.asInputStream();
    }

    /**
     * @return The original Rest Assured {@link Response}
     */
    @Override
    public Response getOriginalResponse() {
        return response;
    }

}
